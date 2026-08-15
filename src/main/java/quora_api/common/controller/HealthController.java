package quora_api.common.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HealthController {
    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/health/redis")
    public String checkRedis() {
        try {
            redisTemplate.opsForValue().set("health-check", "ok");
            String value = (String) redisTemplate.opsForValue().get("health-check");
            return "Redis is healthy: " + value;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "Redis connection failed: " + e.getClass().getName() + " : " + e.getMessage();

        }
    }
}
