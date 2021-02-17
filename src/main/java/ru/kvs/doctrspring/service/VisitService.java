package ru.kvs.doctrspring.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.kvs.doctrspring.dto.VisitDto;
import ru.kvs.doctrspring.model.Clinic;
import ru.kvs.doctrspring.model.Patient;
import ru.kvs.doctrspring.model.Status;
import ru.kvs.doctrspring.model.Visit;
import ru.kvs.doctrspring.repository.ClinicRepository;
import ru.kvs.doctrspring.repository.PatientRepository;
import ru.kvs.doctrspring.repository.UserRepository;
import ru.kvs.doctrspring.repository.VisitRepository;
import ru.kvs.doctrspring.security.AuthUtil;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VisitService {

    private final VisitRepository visitRepository;
    private final UserRepository userRepository;
    private final ClinicRepository clinicRepository;
    private final PatientRepository patientRepository;

    public VisitService(VisitRepository visitRepository, UserRepository userRepository, ClinicRepository clinicRepository, PatientRepository patientRepository) {
        this.visitRepository = visitRepository;
        this.userRepository = userRepository;
        this.clinicRepository = clinicRepository;
        this.patientRepository = patientRepository;
    }

    public List<Visit> getAll() {
        List<Visit> visits = visitRepository.findAllByDoctorId(AuthUtil.getAuthUserId(),
                Sort.by(Sort.Direction.DESC, "date"));
        log.info("Filtering active visits");
        return visits.stream()
                .filter(p -> Status.ACTIVE.equals(p.getStatus()))
                .collect(Collectors.toList());
    }

    public Visit get(long id, long doctorId) {
        return visitRepository.findByIdAndDoctorId(id, doctorId);
    }

    public void update(VisitDto visitDto, long doctorId) {
        Assert.notNull(visitDto, "visit must not be null");
        Visit storedVisit = visitRepository.findByIdAndDoctorId(visitDto.getId(), doctorId);
        Assert.notNull(storedVisit, "no visit found!");

        Clinic clinic = clinicRepository.findByIdAndDoctorId(visitDto.getClinicId(), AuthUtil.getAuthUserId());
        Patient patient = patientRepository.findByIdAndDoctorId(visitDto.getPatientId(), AuthUtil.getAuthUserId());

        storedVisit.setClinic(clinic);
        storedVisit.setPatient(patient);

        storedVisit.setDate(visitDto.getDate());
        storedVisit.setCost(visitDto.getCost());
        storedVisit.setInfo(visitDto.getInfo());

        storedVisit.setUpdated(new Date());
        visitRepository.save(storedVisit);
    }

    public Visit save(VisitDto visitDto) {
        Visit created = visitDto.toVisit();
        created.setCreated(new Date());
        created.setUpdated(new Date());
        created.setStatus(Status.ACTIVE);
        created.setDoctor(AuthUtil.getAuthUser());

        Clinic clinic = clinicRepository.findByIdAndDoctorId(visitDto.getClinicId(), AuthUtil.getAuthUserId());
        Patient patient = patientRepository.findByIdAndDoctorId(visitDto.getPatientId(), AuthUtil.getAuthUserId());

        created.setClinic(clinic);
        created.setPatient(patient);

        return visitRepository.save(created);
    }

    public void delete(long id, long doctorId) {
        Visit visit = visitRepository.findByIdAndDoctorId(id, doctorId);
        if (!Status.DELETED.equals(visit.getStatus())) {
            visit.setUpdated(new Date());
            visit.setStatus(Status.DELETED);
            visitRepository.save(visit);
        }
    }

}
