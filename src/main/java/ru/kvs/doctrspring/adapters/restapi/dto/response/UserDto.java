package ru.kvs.doctrspring.adapters.restapi.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.kvs.doctrspring.domain.User;

@NoArgsConstructor
@Getter
@SuperBuilder
public class UserDto extends PersonDto {

    private String username;

    public static UserDto fromUser(User user) {
        return UserDto.builder()
                .id(user.getId().asString())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

}
