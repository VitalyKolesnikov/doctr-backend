package ru.kvs.doctrspring.repository;

import ru.kvs.doctrspring.model.Clinic;

import java.util.List;

public interface ClinicRepository {
    List<Clinic> findAllByDoctorId(long id);
    Clinic findByIdAndDoctorId(long id, long doctorId);
}
