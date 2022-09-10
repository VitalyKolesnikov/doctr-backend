package ru.kvs.doctrspring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kvs.doctrspring.model.Clinic;
import ru.kvs.doctrspring.model.Patient;
import ru.kvs.doctrspring.model.User;
import ru.kvs.doctrspring.model.Visit;

import java.util.List;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class DoctrRepositoryDbAdapter implements DoctrRepository {

    private final UserJpaRepository userJpaRepository;
    private final ClinicJpaRepository clinicJpaRepository;
    private final PatientJpaRepository patientJpaRepository;
    private final VisitJpaRepository visitJpaRepository;

    @Override
    public User getUser(long doctorId) {
        return userJpaRepository.findById(doctorId)
                .orElseThrow(() -> new NoSuchElementException(String.format("User with id [%s] not found", doctorId)));
    }

    @Override
    public User getUserByUsernameIgnoreCase(String username) {
        return userJpaRepository.getUserByUsernameIgnoreCase(username);
    }

    @Override
    public List<Clinic> getClinicsByDoctorId(long doctorId) {
        return clinicJpaRepository.findAllByDoctorId(doctorId);
    }

    @Override
    public Clinic getClinicByIdAndDoctorId(long clinicId, long doctorId) {
        return clinicJpaRepository.findByIdAndDoctorId(clinicId, doctorId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Clinic with id [%s] not found", clinicId)));
    }

    @Override
    public List<Patient> getPatients(long doctorId) {
        return patientJpaRepository.getActive(doctorId);
    }

    @Override
    public Patient getPatientByIdAndDoctorId(long patientId, long doctorId) {
        return patientJpaRepository.findByIdAndDoctorId(patientId, doctorId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Patient with id [%s] not found", patientId)));
    }

    @Override
    public Patient savePatient(Patient patient) {
        return patientJpaRepository.save(patient);
    }

    @Override
    public List<Visit> getVisits(long doctorId) {
        return visitJpaRepository.getActive(doctorId);
    }

    @Override
    public Visit getVisitByIdAndDoctorId(long visitId, long doctorId) {
        return visitJpaRepository.findByIdAndDoctorId(visitId, doctorId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Visit with id [%s] not found", visitId)));
    }

    @Override
    public List<Visit> getVisitsOfPatient(long doctorId, long patientId) {
        return visitJpaRepository.getActiveForPatient(doctorId, patientId);
    }

    @Override
    public Visit saveVisit(Visit visit) {
        return visitJpaRepository.save(visit);
    }

}
