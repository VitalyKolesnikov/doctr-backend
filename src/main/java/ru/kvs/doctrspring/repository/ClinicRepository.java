package ru.kvs.doctrspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kvs.doctrspring.model.Clinic;

import java.util.List;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    List<Clinic> findAllByDoctorId(long id);
    Clinic findByIdAndDoctorId(long id, long doctorId);
}
