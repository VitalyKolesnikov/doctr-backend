package ru.kvs.doctrspring.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kvs.doctrspring.model.Patient;
import ru.kvs.doctrspring.model.Status;
import ru.kvs.doctrspring.repository.PatientRepository;
import ru.kvs.doctrspring.security.AuthUtil;
import ru.kvs.doctrspring.service.PatientService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public List<Patient> getAll() {
        List<Patient> patients = patientRepository.findByDoctorId(AuthUtil.getAuthUserId());
        log.info("Filtering active patients");
        return patients.stream()
                .filter(p -> Status.ACTIVE.equals(p.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }


}
