package ru.kvs.doctrspring.adapters.database.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kvs.doctrspring.domain.Clinic;
import ru.kvs.doctrspring.domain.ids.ClinicId;
import ru.kvs.doctrspring.domain.ids.UserId;

import java.util.List;
import java.util.Optional;

public interface ClinicJpaRepository extends JpaRepository<Clinic, ClinicId> {
    List<Clinic> findAllByDoctorId(UserId doctorId);

    Optional<Clinic> findByIdAndDoctorId(ClinicId clinicId, UserId doctorId);
}
