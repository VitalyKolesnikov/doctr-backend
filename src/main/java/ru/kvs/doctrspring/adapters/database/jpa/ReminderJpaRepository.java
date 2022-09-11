package ru.kvs.doctrspring.adapters.database.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kvs.doctrspring.domain.Reminder;

import java.util.List;
import java.util.Optional;

public interface ReminderJpaRepository extends JpaRepository<Reminder, Long> {

    @Query("SELECT r FROM Reminder r WHERE r.doctorId=:doctorId AND r.status='ACTIVE' AND r.date <= CURRENT_DATE")
    List<Reminder> getActual(@Param("doctorId") long doctorId);

    @Query("SELECT r FROM Reminder r WHERE r.doctorId=:doctorId AND " +
            "r.patient.id=:patientId ORDER BY r.status, r.date DESC")
    List<Reminder> getAllForPatient(@Param("doctorId") long doctorId, @Param("patientId") long patientId);

    Optional<Reminder> findByIdAndDoctorId(long id, long doctorId);
}
