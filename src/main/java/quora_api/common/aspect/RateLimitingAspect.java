package quora_api.common.aspect;

import java.time.Duration;
import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import quora_api.common.annotation.RateLimited;
import quora_api.common.config.RequestUtils;
import quora_api.security.utils.SecurityUtils;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitingAspect {
    private final RedisTemplate<String, Object> redisTemplate;

    @Around("@annotation(rateLimited)")
    public Object rateLimit(ProceedingJoinPoint joinPoint, RateLimited rateLimited) throws Throwable {
        
        String keyPrefix = rateLimited.key().isEmpty() ? joinPoint.getSignature().toShortString() : rateLimited.key();

        String identifier;
        try {
            UUID currentUserId = SecurityUtils.getCurrentUserId();
            identifier = "user:" + currentUserId.toString();
        } catch (Exception e) {
            // TODO: handle exception
            String ip = RequestUtils.getClientIpAddress();
            identifier = "ip:" + ip;
        }

        String redisKey = String.format("rate_limit:%s:%s", keyPrefix, identifier);

        long count = redisTemplate.opsForValue().increment(redisKey);
        // if (count == 0) {
        //     count = 0L;
        // }
        if (count == 1) {
            redisTemplate.expire(redisKey, Duration.ofSeconds(rateLimited.windowSeconds()));
        }

        long ttl = redisTemplate.getExpire(redisKey);

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletResponse response = attributes.getResponse();
            if (response != null) {
                response.setHeader("X-RateLimit-Limit", String.valueOf(rateLimited.limit()));
                long remaining = Math.max(0, rateLimited.limit() - count);
                response.setHeader("X-RateLimit-Remaining", String.valueOf(remaining));
                long resetTimestamp = System.currentTimeMillis() / 1000 + ttl;
                response.setHeader("X-RateLimit-Reset", String.valueOf(resetTimestamp));
            }
        }

        if (count > rateLimited.limit()) {
            // long ttl = redisTemplate.getExpire(redisKey);
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "Rate limit exceeded. Try again in " + ttl + " seconds.");
        }

        return joinPoint.proceed();

    }
}
