package ru.kvs.doctrspring.adapters.database.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kvs.doctrspring.domain.Reminder;
import ru.kvs.doctrspring.domain.ids.PatientId;
import ru.kvs.doctrspring.domain.ids.ReminderId;
import ru.kvs.doctrspring.domain.ids.UserId;

import java.util.List;
import java.util.Optional;

public interface ReminderJpaRepository extends JpaRepository<Reminder, ReminderId> {
    @Query("SELECT r FROM Reminder r WHERE r.doctorId=:doctorId AND r.status='ACTIVE' AND r.date <= CURRENT_DATE")
    List<Reminder> getActual(@Param("doctorId") UserId doctorId);

    @Query("SELECT r FROM Reminder r WHERE r.doctorId=:doctorId AND " +
            "r.patient.id=:patientId ORDER BY r.status, r.date DESC")
    List<Reminder> getAllForPatient(@Param("doctorId") UserId doctorId, @Param("patientId") PatientId patientId);

    Optional<Reminder> findByIdAndDoctorId(ReminderId id, UserId doctorId);
}
