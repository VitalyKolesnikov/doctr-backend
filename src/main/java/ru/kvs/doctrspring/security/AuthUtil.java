package ru.kvs.doctrspring.security;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.kvs.doctrspring.model.User;
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
        User user = new User();
        user.setId(jwtUser.getId());
        user.setUsername(jwtUser.getUsername());
        user.setFirstName(jwtUser.getFirstname());
        user.setLastName(jwtUser.getLastname());
        user.setPassword(jwtUser.getPassword());
        user.setEmail(jwtUser.getEmail());
        return user;
    }

    public static Long getAuthUserId() {
        return getJwtUser().getId();
    }
}
