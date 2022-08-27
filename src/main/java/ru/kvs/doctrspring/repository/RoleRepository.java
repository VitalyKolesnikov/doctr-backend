package ru.kvs.doctrspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kvs.doctrspring.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
