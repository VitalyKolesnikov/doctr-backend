package ru.kvs.doctrspring.adapters.database.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kvs.doctrspring.domain.Visit;

import java.util.List;
import java.util.Optional;

public interface VisitJpaRepository extends JpaRepository<Visit, Long> {

    @Query("SELECT v FROM Visit v WHERE v.doctorId=:doctorId AND v.status = 'ACTIVE' ORDER BY v.date DESC, v.created DESC")
    List<Visit> getActive(@Param("doctorId") long doctorId);

    @Query("SELECT v FROM Visit v WHERE v.doctorId=:doctorId AND " +
            "v.patient.id=:patientId AND v.status = 'ACTIVE' ORDER BY v.date DESC, v.created DESC")
    List<Visit> getActiveForPatient(@Param("doctorId") long doctorId, @Param("patientId") long patientId);

    Optional<Visit> findByIdAndDoctorId(long id, long doctorId);
}
