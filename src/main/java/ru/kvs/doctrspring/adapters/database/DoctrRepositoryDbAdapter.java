package ru.kvs.doctrspring.adapters.database;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kvs.doctrspring.adapters.database.jpa.*;
import ru.kvs.doctrspring.domain.*;
import ru.kvs.doctrspring.domain.ids.*;

import java.util.List;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class DoctrRepositoryDbAdapter implements DoctrRepository {

    private final UserJpaRepository userJpaRepository;
    private final ClinicJpaRepository clinicJpaRepository;
    private final PatientJpaRepository patientJpaRepository;
    private final VisitJpaRepository visitJpaRepository;
    private final ReminderJpaRepository reminderJpaRepository;

    @Override
    public User getUser(UserId doctorId) {
        return userJpaRepository.findById(doctorId)
                .orElseThrow(() -> new NoSuchElementException(String.format("User with id [%s] not found", doctorId)));
    }

    @Override
    public User getByUsernameIgnoreCase(String username) {
        return userJpaRepository.getByUsernameIgnoreCase(username);
    }

    @Override
    public List<Clinic> getClinicsByDoctorId(UserId doctorId) {
        return clinicJpaRepository.findAllByDoctorId(doctorId);
    }

    @Override
    public Clinic getClinicByIdAndDoctorId(ClinicId clinicId, UserId doctorId) {
        return clinicJpaRepository.findByIdAndDoctorId(clinicId, doctorId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Clinic with id [%s] not found", clinicId)));
    }

    @Override
    public List<Patient> getPatients(UserId doctorId) {
        return patientJpaRepository.getActive(doctorId);
    }

    @Override
    public Patient getPatientByIdAndDoctorId(PatientId patientId, UserId doctorId) {
        return patientJpaRepository.findByIdAndDoctorId(patientId, doctorId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Patient with id [%s] not found", patientId)));
    }

    @Override
    public Patient savePatient(Patient patient) {
        return patientJpaRepository.save(patient);
    }

    @Override
    public List<Visit> getVisits(UserId doctorId) {
        return visitJpaRepository.getActive(doctorId);
    }

    @Override
    public Visit getVisitByIdAndDoctorId(VisitId visitId, UserId doctorId) {
        return visitJpaRepository.findByIdAndDoctorId(visitId, doctorId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Visit with id [%s] not found", visitId)));
    }

    @Override
    public List<Visit> getVisitsOfPatient(UserId doctorId, PatientId patientId) {
        return visitJpaRepository.getActiveForPatient(doctorId, patientId);
    }

    @Override
    public Visit saveVisit(Visit visit) {
        return visitJpaRepository.save(visit);
    }

    @Override
    public List<Reminder> getActualReminders(UserId doctorId) {
        return reminderJpaRepository.getActual(doctorId);
    }

    @Override
    public List<Reminder> getRemindersOfPatient(UserId doctorId, PatientId patientId) {
        return reminderJpaRepository.getAllForPatient(doctorId, patientId);
    }

    @Override
    public Reminder getReminderByIdAndDoctorId(ReminderId reminderId, UserId doctorId) {
        return reminderJpaRepository.findByIdAndDoctorId(reminderId, doctorId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Reminder with id [%s] not found", reminderId)));
    }

    @Override
    public Reminder saveReminder(Reminder reminder) {
        return reminderJpaRepository.save(reminder);
    }

}
