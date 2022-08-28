package ru.kvs.doctrspring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kvs.doctrspring.model.Patient;

import java.util.List;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class PatientRepositoryAdapter implements PatientRepository {

    private final PatientJpaRepository patientJpaRepository;

    @Override
    public List<Patient> getActive(long doctorId) {
        return patientJpaRepository.getActive(doctorId);
    }

    @Override
    public Patient findByIdAndDoctorId(long id, long doctorId) {
        return patientJpaRepository.findOneByIdAndDoctorId(id, doctorId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Patient with id [%s] not found", id)));
    }

    @Override
    public Patient save(Patient patient) {
        return patientJpaRepository.save(patient);
    }

}
