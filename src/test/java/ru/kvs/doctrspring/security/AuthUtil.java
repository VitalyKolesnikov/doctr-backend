package ru.kvs.doctrspring.security;

import org.springframework.context.annotation.Profile;
import ru.kvs.doctrspring.domain.ids.UserId;

@Profile("integration-test")
public class AuthUtil {
    public static UserId getAuthUserId() {
        return UserId.of("548f3c8b-4e06-45c6-b7ee-3c7db137796e");
    }
}
