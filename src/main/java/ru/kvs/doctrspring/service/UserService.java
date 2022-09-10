package ru.kvs.doctrspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kvs.doctrspring.model.User;
import ru.kvs.doctrspring.repository.DoctrRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final DoctrRepository doctrRepository;

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return doctrRepository.getUserByUsernameIgnoreCase(username);
    }

    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return doctrRepository.getUser(userId);
    }

}
