package ru.kvs.doctrspring.util;

import ru.kvs.doctrspring.HasId;
import ru.kvs.doctrspring.model.BaseEntity;
import ru.kvs.doctrspring.util.exception.IllegalRequestDataException;
import ru.kvs.doctrspring.util.exception.NotFoundException;

public class ValidationUtil {

    public static void assureIdConsistent(HasId bean, long id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean + " must be with id=" + id);
        }
    }

}
