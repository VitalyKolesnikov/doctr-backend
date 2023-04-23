package ru.kvs.doctrspring.adapters.restapi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.kvs.doctrspring.app.UserService;
import ru.kvs.doctrspring.domain.User;
import ru.kvs.doctrspring.domain.ids.UserId;
import ru.kvs.doctrspring.security.JwtTokenFilter;

import java.util.NoSuchElementException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserRestControllerTest {

    private static final UserId USER_ID = UserId.of("548f3c8b-4e06-45c6-b7ee-3c7db137796e");

    private final static String REST_URL = "/api/v1/users/";

    @MockBean
    private UserService userService;
    @MockBean
    JwtTokenFilter jwtTokenFilter;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("getUserById should return user if it exists")
    void testGetUserById() throws Exception {
        // given
        User USER = User.builder()
                .id(USER_ID)
                .username("johndoe")
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@example.com")
                .build();
        when(userService.findById(USER_ID)).thenReturn(USER);

        // when then
        mockMvc.perform(get(REST_URL + USER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(USER_ID.asString()))
                .andExpect(jsonPath("username").value("johndoe"))
                .andExpect(jsonPath("firstName").value("John"))
                .andExpect(jsonPath("lastName").value("Doe"))
                .andExpect(jsonPath("email").value("johndoe@example.com"));

        verify(userService).findById(USER_ID);
    }

    @Test
    @DisplayName("getUserById should return NOT_FOUND if user is not found")
    void testGetUserByIdNotFound() throws Exception {
        // given
        when(userService.findById(USER_ID)).thenThrow(NoSuchElementException.class);

        // when then
        mockMvc.perform(get(REST_URL + USER_ID))
                .andExpect(status().isNotFound());

        verify(userService).findById(USER_ID);
    }

}
