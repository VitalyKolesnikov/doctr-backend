package ru.kvs.doctrspring.app;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kvs.doctrspring.domain.DoctrRepository;
import ru.kvs.doctrspring.domain.User;
import ru.kvs.doctrspring.domain.ids.UserId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final UserId USER_ID = UserId.of("548f3c8b-4e06-45c6-b7ee-3c7db137796e");
    private static final String USERNAME = "johndoe@example.com";

    @Mock
    private DoctrRepository doctrRepository;
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("findByUsername should return user by username")
    void testFindByUsername() {
        // given
        User expectedUser = User.builder().id(USER_ID).username(USERNAME).build();
        when(doctrRepository.getByUsernameIgnoreCase(USERNAME)).thenReturn(expectedUser);

        // when
        User actualUser = userService.findByUsername(USERNAME);

        // then
        verify(doctrRepository).getByUsernameIgnoreCase(USERNAME);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    @DisplayName("findById should return user by ID")
    void testFindById() {
        // given
        User expectedUser = User.builder().id(USER_ID).username(USERNAME).build();
        when(doctrRepository.getUser(USER_ID)).thenReturn(expectedUser);

        // when
        User actualUser = userService.findById(USER_ID);

        // then
        verify(doctrRepository).getUser(USER_ID);
        assertEquals(expectedUser, actualUser);
    }

}
