package ru.kvs.doctrspring.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.kvs.doctrspring.model.Patient;
import ru.kvs.doctrspring.model.Status;
import ru.kvs.doctrspring.repository.PatientRepository;
import ru.kvs.doctrspring.repository.UserRepository;
import ru.kvs.doctrspring.security.AuthUtil;
import ru.kvs.doctrspring.service.PatientService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public PatientServiceImpl(PatientRepository patientRepository, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Patient> getAll() {
        List<Patient> patients = patientRepository.findAllByDoctorId(AuthUtil.getAuthUserId());
        log.info("Filtering active patients");
        return patients.stream()
                .filter(p -> Status.ACTIVE.equals(p.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public Patient get(long id, long doctorId) {
        return patientRepository.findByIdAndDoctorId(id, doctorId);
    }

    @Override
    public void update(Patient patient, long doctorId) {
        Assert.notNull(patient, "patient must not be null");
        Patient savedPatient = patientRepository.findByIdAndDoctorId(patient.getId(), doctorId);
        Assert.notNull(savedPatient, "no patient found!");
        patient.setDoctor(userRepository.getOne(doctorId));
        patient.setCreated(savedPatient.getCreated());
        patient.setUpdated(new Date());
        patient.setStatus(savedPatient.getStatus());
        patientRepository.save(patient);
    }

    @Override
    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public void delete(long id, long doctorId) {
        Patient patient = patientRepository.findByIdAndDoctorId(id, doctorId);
        if (!Status.DELETED.equals(patient.getStatus())) {
            patient.setUpdated(new Date());
            patient.setStatus(Status.DELETED);
            patientRepository.save(patient);
        }
    }

}
