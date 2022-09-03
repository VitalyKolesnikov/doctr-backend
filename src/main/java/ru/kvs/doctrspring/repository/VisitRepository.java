package ru.kvs.doctrspring.repository;

import ru.kvs.doctrspring.model.Visit;

import java.util.List;

public interface VisitRepository {
    List<Visit> getActive(long doctorId);

    Visit findByIdAndDoctorId(long id, long doctorId);

    List<Visit> getActiveForPatient(long doctorId, long patientId);

    Visit save(Visit visit);
}
