package ru.kvs.doctrspring.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kvs.doctrspring.domain.DoctrRepository;
import ru.kvs.doctrspring.domain.Patient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final DoctrRepository doctrRepository;

    @Transactional(readOnly = true)
    public List<Patient> getActive(long doctorId) {
        return doctrRepository.getPatients(doctorId);
    }

    @Transactional(readOnly = true)
    public Patient get(long id, long doctorId) {
        return doctrRepository.getPatientByIdAndDoctorId(id, doctorId);
    }

    @Transactional(readOnly = true)
    public List<Patient> getSuggested(long doctorId, String part) {
        String partLowerCased = part.toLowerCase();
        return getActive(doctorId).stream()
                .filter(patient -> (patient.getLastName().toLowerCase().contains(partLowerCased) ||
                        patient.getFirstName().toLowerCase().contains(partLowerCased)) ||
                        patient.getMiddleName().toLowerCase().contains(partLowerCased))
                .collect(Collectors.toList());
    }

    @Transactional
    public Patient create(Patient patient, long doctorId) {
        patient.create(doctorId);
        return doctrRepository.savePatient(patient);
    }

    @Transactional
    public void update(Patient patient, long patientId, long doctorId) {
        Patient storedPatient = doctrRepository.getPatientByIdAndDoctorId(patientId, doctorId);
        storedPatient.update(patient);
    }

    @Transactional
    public void delete(long id, long doctorId) {
        Patient patient = doctrRepository.getPatientByIdAndDoctorId(id, doctorId);
        patient.softDelete();
    }

}
