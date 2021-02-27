package ru.kvs.doctrspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kvs.doctrspring.model.Reminder;

import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    @Query("SELECT r FROM Reminder r WHERE r.doctor.id=:doctorId AND r.status='ACTIVE' AND r.date <= CURRENT_DATE")
    List<Reminder> getActual(long doctorId);

    @Query("SELECT r FROM Reminder r WHERE r.doctor.id=:doctorId AND " +
            "r.patient.id=:patientId ORDER BY r.status, r.date DESC")
    List<Reminder> getAllForPatient(long doctorId, long patientId);

    Reminder findByIdAndDoctorId(long id, long doctorId);
}
