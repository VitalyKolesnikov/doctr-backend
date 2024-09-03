package ru.kvs.doctrspring.adapters.database.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kvs.doctrspring.domain.User;
import ru.kvs.doctrspring.domain.ids.UserId;

public interface UserJpaRepository extends JpaRepository<User, UserId> {
    User getByUsernameIgnoreCase(String username);
}
