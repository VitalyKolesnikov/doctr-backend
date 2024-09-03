package ru.kvs.doctrspring.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kvs.doctrspring.domain.DoctrRepository;
import ru.kvs.doctrspring.domain.User;
import ru.kvs.doctrspring.domain.ids.UserId;

@Service
@RequiredArgsConstructor
public class UserService {

    private final DoctrRepository doctrRepository;

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return doctrRepository.getByUsernameIgnoreCase(username);
    }

    @Transactional(readOnly = true)
    public User findById(UserId userId) {
        return doctrRepository.getUser(userId);
    }

}
