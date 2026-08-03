package quora_api.security.utils;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import quora_api.security.service.UserPrincipal;

@Component
public class SecurityUtils {
    public static UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!ObjectUtils.isEmpty(authentication) || !ObjectUtils.isEmpty(authentication.getPrincipal())) {
            return ((UserPrincipal) authentication.getPrincipal()).getUserId();
        }
        throw new IllegalArgumentException("User not authenticated");
    }
}
