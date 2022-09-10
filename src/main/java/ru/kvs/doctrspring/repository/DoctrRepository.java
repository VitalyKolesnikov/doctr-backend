package ru.kvs.doctrspring.repository;

import ru.kvs.doctrspring.model.Clinic;
import ru.kvs.doctrspring.model.Patient;
import ru.kvs.doctrspring.model.User;
import ru.kvs.doctrspring.model.Visit;

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

}
