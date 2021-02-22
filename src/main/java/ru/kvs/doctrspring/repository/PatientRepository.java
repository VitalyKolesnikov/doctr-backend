package ru.kvs.doctrspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kvs.doctrspring.model.Patient;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findAllByDoctorIdOrderByCreatedDesc(long id);
    Patient findByIdAndDoctorId(long id, long doctorId);
}
