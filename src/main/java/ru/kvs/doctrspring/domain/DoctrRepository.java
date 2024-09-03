package ru.kvs.doctrspring.domain;

import ru.kvs.doctrspring.domain.ids.*;

import java.util.List;

public interface DoctrRepository {

    User getUser(UserId doctorId);

    User getByUsernameIgnoreCase(String username);

    List<Clinic> getClinicsByDoctorId(UserId doctorId);

    Clinic getClinicByIdAndDoctorId(ClinicId clinicId, UserId doctorId);

    List<Patient> getPatients(UserId doctorId);

    Patient getPatientByIdAndDoctorId(PatientId patientId, UserId doctorId);

    Patient savePatient(Patient patient);

    List<Visit> getVisits(UserId doctorId);

    Visit getVisitByIdAndDoctorId(VisitId visitId, UserId doctorId);

    List<Visit> getVisitsOfPatient(UserId doctorId, PatientId patientId);

    Visit saveVisit(Visit visit);

    List<Reminder> getActualReminders(UserId doctorId);

    List<Reminder> getRemindersOfPatient(UserId doctorId, PatientId patientId);

    Reminder getReminderByIdAndDoctorId(ReminderId id, UserId doctorId);

    Reminder saveReminder(Reminder reminder);

}
