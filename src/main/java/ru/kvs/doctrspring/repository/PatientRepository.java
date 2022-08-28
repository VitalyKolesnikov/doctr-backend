package ru.kvs.doctrspring.repository;

import ru.kvs.doctrspring.model.Patient;

import java.util.List;

public interface PatientRepository {
    List<Patient> getActive(long doctorId);

    Patient findByIdAndDoctorId(long id, long doctorId);

    Patient save(Patient patient);
}
