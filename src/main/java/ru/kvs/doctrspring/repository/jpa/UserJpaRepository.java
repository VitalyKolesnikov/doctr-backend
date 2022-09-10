package ru.kvs.doctrspring.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kvs.doctrspring.model.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    User getUserByUsernameIgnoreCase(String username);

}
