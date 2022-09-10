package ru.kvs.doctrspring.adapters.restapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.kvs.doctrspring.domain.User;

@Getter
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto extends PersonDto {

    private String username;

    public static UserDto fromUser(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

}
