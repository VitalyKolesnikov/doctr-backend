package ru.kvs.doctrspring.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kvs.doctrspring.domain.DoctrRepository;
import ru.kvs.doctrspring.domain.Patient;
import ru.kvs.doctrspring.domain.ids.PatientId;
import ru.kvs.doctrspring.domain.ids.UserId;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final DoctrRepository doctrRepository;

    @Transactional(readOnly = true)
    public List<Patient> getActive(UserId doctorId) {
        return doctrRepository.getPatients(doctorId);
    }

    @Transactional(readOnly = true)
    public Patient get(PatientId patientId, UserId doctorId) {
        return doctrRepository.getPatientByIdAndDoctorId(patientId, doctorId);
    }

    @Transactional(readOnly = true)
    public List<Patient> getSuggested(UserId doctorId, String part) {
        String partLowerCased = part.toLowerCase();

        return doctrRepository.getPatients(doctorId).stream()
                .filter(patient -> (patient.getLastName().toLowerCase().contains(partLowerCased) ||
                        patient.getFirstName().toLowerCase().contains(partLowerCased)) ||
                        patient.getMiddleName().toLowerCase().contains(partLowerCased))
                .collect(Collectors.toList());
    }

    @Transactional
    public Patient create(Patient patient, UserId doctorId) {
        patient.create(doctorId);
        return doctrRepository.savePatient(patient);
    }

    @Transactional
    public void update(Patient patient, PatientId patientId, UserId doctorId) {
        Patient storedPatient = doctrRepository.getPatientByIdAndDoctorId(patientId, doctorId);
        storedPatient.update(patient);
    }

    @Transactional
    public void delete(PatientId id, UserId doctorId) {
        Patient patient = doctrRepository.getPatientByIdAndDoctorId(id, doctorId);
        patient.softDelete();
    }

}
