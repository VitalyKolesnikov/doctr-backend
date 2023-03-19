package ru.kvs.doctrspring.security;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.kvs.doctrspring.domain.User;
import ru.kvs.doctrspring.domain.ids.UserId;
import ru.kvs.doctrspring.security.jwt.JwtUser;

public class AuthUtil {

    public static JwtUser getJwtUser() {
        return ((JwtUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
    }

    public static User getAuthUser() {
        JwtUser jwtUser = getJwtUser();

        return User.builder()
                .id(jwtUser.getId())
                .username(jwtUser.getUsername())
                .firstName(jwtUser.getFirstname())
                .lastName(jwtUser.getLastname())
                .password(jwtUser.getPassword())
                .email(jwtUser.getEmail())
                .build();
    }

    public static UserId getAuthUserId() {
        return getJwtUser().getId();
    }
}
