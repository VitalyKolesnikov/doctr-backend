package ru.kvs.doctrspring.adapters.database.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kvs.doctrspring.domain.Visit;
import ru.kvs.doctrspring.domain.ids.PatientId;
import ru.kvs.doctrspring.domain.ids.UserId;
import ru.kvs.doctrspring.domain.ids.VisitId;

import java.util.List;
import java.util.Optional;

public interface VisitJpaRepository extends JpaRepository<Visit, VisitId> {
    @Query("SELECT v FROM Visit v WHERE v.doctorId=:doctorId AND v.status = 'ACTIVE' ORDER BY v.date DESC, v.created DESC")
    List<Visit> getActive(@Param("doctorId") UserId doctorId);

    @Query("SELECT v FROM Visit v WHERE v.doctorId=:doctorId AND " +
            "v.patient.id=:patientId AND v.status = 'ACTIVE' ORDER BY v.date DESC, v.created DESC")
    List<Visit> getActiveForPatient(@Param("doctorId") UserId doctorId, @Param("patientId") PatientId patientId);

    Optional<Visit> findByIdAndDoctorId(VisitId id, UserId doctorId);
}
