package ru.kvs.doctrspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kvs.doctrspring.model.Clinic;

import java.util.List;
import java.util.Optional;

public interface ClinicJpaRepository extends JpaRepository<Clinic, Long> {

    List<Clinic> findAllByDoctorId(long id);

    Optional<Clinic> findByIdAndDoctorId(long id, long doctorId);

}
