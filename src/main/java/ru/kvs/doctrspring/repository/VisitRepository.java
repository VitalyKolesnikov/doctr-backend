package ru.kvs.doctrspring.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.kvs.doctrspring.model.Visit;

import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findAllByDoctorIdOrderByDateDescCreatedDesc(long id, Sort sort);
    List<Visit> findAllByDoctorIdAndPatientIdOrderByDateDescCreatedDesc(long doctorId, long patientId, Sort sort);
    Visit findByIdAndDoctorId(long id, long doctorId);
}
