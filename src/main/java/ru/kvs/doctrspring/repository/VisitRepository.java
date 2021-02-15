package ru.kvs.doctrspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.kvs.doctrspring.model.Visit;

import java.util.List;

@Transactional(readOnly = true)
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findAllByDoctorId(long id);
    Visit findByIdAndDoctorId(long id, long doctorId);
}
