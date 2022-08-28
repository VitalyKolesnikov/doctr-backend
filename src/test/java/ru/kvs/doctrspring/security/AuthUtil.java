package ru.kvs.doctrspring.security;

import org.springframework.context.annotation.Profile;

@Profile("integration-test")
public class AuthUtil {
    public static Long getAuthUserId() {
        return 1000L;
    }
}
