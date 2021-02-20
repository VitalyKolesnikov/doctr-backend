package ru.kvs.doctrspring.service;

import lombok.extern.slf4j.Slf4j;
import ru.kvs.doctrspring.model.BaseEntity;
import ru.kvs.doctrspring.model.Status;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BaseService {

    protected <T extends BaseEntity> List<T> filterActive(List<T> entities) {
        log.info("Filtering active entities");
        return entities.stream()
                .filter(p -> Status.ACTIVE.equals(p.getStatus()))
                .collect(Collectors.toList());
    }

}
