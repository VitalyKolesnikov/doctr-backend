package ru.kvs.doctrspring.service;

import ru.kvs.doctrspring.model.Patient;

import java.util.List;

public interface PatientService {
    List<Patient> getAll();
    Patient save(Patient patient);
}
