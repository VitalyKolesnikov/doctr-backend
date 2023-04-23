package ru.kvs.doctrspring.adapters.restapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kvs.doctrspring.adapters.restapi.dto.response.UserDto;
import ru.kvs.doctrspring.app.UserService;
import ru.kvs.doctrspring.domain.User;
import ru.kvs.doctrspring.domain.ids.UserId;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users/")
public class UserRestController {

    private final UserService userService;

    @GetMapping(value = "{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") UserId id) {
        User user = userService.findById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        UserDto result = UserDto.fromUser(user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
