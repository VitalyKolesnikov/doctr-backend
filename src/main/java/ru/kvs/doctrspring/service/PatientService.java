package ru.kvs.doctrspring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.kvs.doctrspring.dto.PatientDto;
import ru.kvs.doctrspring.model.Patient;
import ru.kvs.doctrspring.model.Status;
import ru.kvs.doctrspring.repository.PatientRepository;
import ru.kvs.doctrspring.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepositoryAdapter;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Patient> getActive(long doctorId) {
        return patientRepositoryAdapter.getActive(doctorId);
    }

    @Transactional(readOnly = true)
    public Patient get(long id, long doctorId) {
        return patientRepositoryAdapter.findByIdAndDoctorId(id, doctorId);
    }

    @Transactional(readOnly = true)
    public List<Patient> getSuggested(long doctorId, String part) {
        String partLowerCased = part.toLowerCase();
        List<Patient> matched = getActive(doctorId).stream()
                .filter(p -> (
                        p.getLastName().toLowerCase().contains(partLowerCased) ||
                                p.getFirstName().toLowerCase().contains(partLowerCased)) ||
                        p.getMiddleName().toLowerCase().contains(partLowerCased)
                )
                .collect(Collectors.toList());
        log.info("Found by suggestion: {}", matched.size());
        return matched;
    }

    @Transactional
    public Patient create(PatientDto patientDto, long doctorId) {
        Patient created = patientDto.toPatient();
        created.setDoctor(userRepository.getOne(doctorId));
        return patientRepositoryAdapter.save(created);
    }

    @Transactional
    public void update(Patient patient, long doctorId) {
        Assert.notNull(patient, "patient must not be null");
        Patient storedPatient = patientRepositoryAdapter.findByIdAndDoctorId(patient.getId(), doctorId);

        patient.setDoctor(storedPatient.getDoctor());
        patient.setCreated(storedPatient.getCreated());
        patient.setUpdated(LocalDateTime.now());
        patient.setStatus(storedPatient.getStatus());
        patientRepositoryAdapter.save(patient);
    }

    @Transactional
    public void delete(long id, long doctorId) {
        Patient patient = patientRepositoryAdapter.findByIdAndDoctorId(id, doctorId);
        if (!Status.DELETED.equals(patient.getStatus())) {
            patient.setUpdated(LocalDateTime.now());
            patient.setStatus(Status.DELETED);
            patientRepositoryAdapter.save(patient);
        }
    }

}
