package ru.kvs.doctrspring.domain;

import java.util.List;

public interface DoctrRepository {

    User getUser(long doctorId);

    User getUserByUsernameIgnoreCase(String username);

    List<Clinic> getClinicsByDoctorId(long doctorId);

    Clinic getClinicByIdAndDoctorId(long clinicId, long doctorId);

    List<Patient> getPatients(long doctorId);

    Patient getPatientByIdAndDoctorId(long patientId, long doctorId);

    Patient savePatient(Patient patient);

    List<Visit> getVisits(long doctorId);

    Visit getVisitByIdAndDoctorId(long visitId, long doctorId);

    List<Visit> getVisitsOfPatient(long doctorId, long patientId);

    Visit saveVisit(Visit visit);

    List<Reminder> getActualReminders(long doctorId);

    List<Reminder> getRemindersOfPatient(long doctorId, long patientId);

    Reminder getReminderByIdAndDoctorId(long id, long doctorId);

    Reminder saveReminder(Reminder reminder);

}
