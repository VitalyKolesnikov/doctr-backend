package ru.kvs.doctrspring.data;

import ru.kvs.doctrspring.TestMatcher;
import ru.kvs.doctrspring.model.User;

public class UserTestData {

    public static TestMatcher<User> USER_MATCHER = TestMatcher.usingFieldsWithIgnoringAssertions(User.class, "created", "updated");

    public static final long DOCTOR_ID = 1000L;
    public static final long ANOTHER_DOCTOR_ID = 1001L;

    public static final User DOCTOR = User.builder()
            .id(DOCTOR_ID)
            .lastName("House")
            .firstName("Dr")
            .build();

    public static final User ANOTHER_DOCTOR = User.builder()
            .id(ANOTHER_DOCTOR_ID)
            .lastName("Watson")
            .firstName("Dr")
            .build();
}
