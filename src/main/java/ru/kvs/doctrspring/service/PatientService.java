package ru.kvs.doctrspring.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.kvs.doctrspring.dto.PatientDto;
import ru.kvs.doctrspring.model.Patient;
import ru.kvs.doctrspring.model.Status;
import ru.kvs.doctrspring.repository.PatientRepository;
import ru.kvs.doctrspring.repository.UserRepository;
import ru.kvs.doctrspring.security.AuthUtil;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public PatientService(PatientRepository patientRepository, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    public List<Patient> getAll() {
        List<Patient> patients = patientRepository.findAllByDoctorId(AuthUtil.getAuthUserId());
        log.info("Filtering active patients");
        return patients.stream()
                .filter(p -> Status.ACTIVE.equals(p.getStatus()))
                .collect(Collectors.toList());
    }

    public Patient get(long id, long doctorId) {
        return patientRepository.findByIdAndDoctorId(id, doctorId);
    }

    public List<Patient> getSuggested(String part) {
        return getAll().stream()
                .filter(p -> (p.getLastName().toLowerCase().contains(part) ||
                        p.getFirstName().toLowerCase().contains(part)) ||
                        p.getMiddleName().toLowerCase().contains(part))
                .collect(Collectors.toList());
    }

    public void update(Patient patient, long doctorId) {
        Assert.notNull(patient, "patient must not be null");
        Patient storedPatient = patientRepository.findByIdAndDoctorId(patient.getId(), doctorId);
        Assert.notNull(storedPatient, "no patient found!");
        patient.setDoctor(userRepository.getOne(doctorId));
        patient.setCreated(storedPatient.getCreated());
        patient.setUpdated(new Date());
        patient.setStatus(storedPatient.getStatus());
        patientRepository.save(patient);
    }

    public Patient create(PatientDto patientDto) {
        Patient created = patientDto.toPatient();
        created.setDoctor(AuthUtil.getAuthUser());
        return patientRepository.save(created);
    }

    public void delete(long id, long doctorId) {
        Patient patient = patientRepository.findByIdAndDoctorId(id, doctorId);
        if (!Status.DELETED.equals(patient.getStatus())) {
            patient.setUpdated(new Date());
            patient.setStatus(Status.DELETED);
            patientRepository.save(patient);
        }
    }

}
