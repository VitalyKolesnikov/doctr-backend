package ru.kvs.doctrspring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.kvs.doctrspring.model.User;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto extends PersonDto {
    private String username;

    public User toUser() {
        User user = new User();
        user.setId(getId());
        user.setUsername(getUsername());
        user.setFirstName(getFirstName());
        user.setLastName(getLastName());
        user.setEmail(getEmail());

        return user;
    }

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());

        return userDto;
    }
}
