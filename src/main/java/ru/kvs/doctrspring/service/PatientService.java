package ru.kvs.doctrspring.service;

import ru.kvs.doctrspring.model.Patient;

import java.util.List;

public interface PatientService {
    List<Patient> getAll();
    Patient get(long id, long doctorId);
    void update(Patient patient, long doctorId);
    Patient save(Patient patient);
    void delete(long id, long doctorId);
}
