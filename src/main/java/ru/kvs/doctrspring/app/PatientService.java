package ru.kvs.doctrspring.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.kvs.doctrspring.adapters.restapi.dto.PatientDto;
import ru.kvs.doctrspring.domain.DoctrRepository;
import ru.kvs.doctrspring.domain.Patient;
import ru.kvs.doctrspring.domain.Status;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PatientService {

    private final Clock clock;
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
        created.setDoctor(doctrRepository.getUser(doctorId));
        return doctrRepository.savePatient(created);
    }

    @Transactional
    public void update(Patient patient, long patientId, long doctorId) {
        Assert.notNull(patient, "patient must not be null");
        Patient storedPatient = doctrRepository.getPatientByIdAndDoctorId(patientId, doctorId);

        storedPatient.setFirstName(patient.getFirstName());
        storedPatient.setMiddleName(patient.getMiddleName());
        storedPatient.setLastName(patient.getLastName());
        storedPatient.setBirthDate(patient.getBirthDate());
        storedPatient.setEmail(patient.getEmail());
        storedPatient.setPhone(patient.getPhone());
        storedPatient.setInfo(patient.getInfo());
        storedPatient.setUpdated(LocalDateTime.now(clock));
    }

    @Transactional
    public void delete(long id, long doctorId) {
        Patient patient = doctrRepository.getPatientByIdAndDoctorId(id, doctorId);
        patient.setUpdated(LocalDateTime.now(clock));
        patient.setStatus(Status.DELETED);
    }

}
