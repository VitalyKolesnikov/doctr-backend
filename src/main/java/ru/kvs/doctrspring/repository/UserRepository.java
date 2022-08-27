package ru.kvs.doctrspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kvs.doctrspring.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameIgnoreCase(String name);
}
