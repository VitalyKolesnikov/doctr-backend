package ru.kvs.doctrspring.security;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.kvs.doctrspring.domain.ids.UserId;
import ru.kvs.doctrspring.security.jwt.JwtUser;

public class AuthUtil {

    public static JwtUser getJwtUser() {
        return ((JwtUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
    }

    public static UserId getAuthUserId() {
        return getJwtUser().getId();
    }

}
