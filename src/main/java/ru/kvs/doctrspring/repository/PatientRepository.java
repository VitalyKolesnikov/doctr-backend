package ru.kvs.doctrspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.kvs.doctrspring.model.Patient;

import java.util.List;

@Transactional(readOnly = true)
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByDoctorId(Long id);
}
