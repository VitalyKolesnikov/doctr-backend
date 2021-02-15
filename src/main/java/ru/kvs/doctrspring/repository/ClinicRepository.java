package ru.kvs.doctrspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.kvs.doctrspring.model.Clinic;

import java.util.List;

@Transactional(readOnly = true)
public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    List<Clinic> findAllByDoctorId(long id);
    Clinic findByIdAndDoctorId(long id, long doctorId);
}
