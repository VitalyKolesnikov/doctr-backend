package ru.kvs.doctrspring.adapters.database.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kvs.doctrspring.domain.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    User getUserByUsernameIgnoreCase(String username);

}
