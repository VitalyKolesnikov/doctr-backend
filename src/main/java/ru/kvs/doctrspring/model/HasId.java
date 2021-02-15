package ru.kvs.doctrspring.model;

import org.springframework.util.Assert;

public interface HasId {
    Long getId();

    void setId(Long id);

    default boolean isNew() {
        return getId() == null;
    }

    default long id() {
        Assert.notNull(getId(), "Entity must have id");
        return getId();
    }
}
