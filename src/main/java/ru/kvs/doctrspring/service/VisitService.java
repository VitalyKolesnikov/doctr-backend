package ru.kvs.doctrspring.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.kvs.doctrspring.dto.DatedVisitListDto;
import ru.kvs.doctrspring.dto.VisitDto;
import ru.kvs.doctrspring.model.Status;
import ru.kvs.doctrspring.model.Visit;
import ru.kvs.doctrspring.repository.ClinicRepository;
import ru.kvs.doctrspring.repository.PatientRepository;
import ru.kvs.doctrspring.repository.UserRepository;
import ru.kvs.doctrspring.repository.VisitRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

@Service
@Slf4j
@Transactional
public class VisitService {

    private final VisitRepository visitRepository;
    private final ClinicRepository clinicRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public VisitService(VisitRepository visitRepository,
                        ClinicRepository clinicRepository,
                        PatientRepository patientRepository,
                        UserRepository userRepository) {
        this.visitRepository = visitRepository;
        this.clinicRepository = clinicRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    public List<Visit> getActive(long doctorId) {
        return visitRepository.getActive(doctorId);
    }

    public List<DatedVisitListDto> getAllGroupByDate(long doctorId) {
        Map<LocalDate, List<Visit>> map = getActive(doctorId).stream()
                .collect(groupingBy(Visit::getDate));
        List<DatedVisitListDto> list = new ArrayList<>();
        map.forEach((key, value) -> list.add(new DatedVisitListDto(key, value)));
        list.sort(Collections.reverseOrder());
        return list;
    }

    public Visit get(long id, long doctorId) {
        return visitRepository.findByIdAndDoctorId(id, doctorId);
    }

    public List<Visit> getForPatient(long doctorId, long patientId) {
        return visitRepository.getActiveForPatient(doctorId, patientId);
    }

    public void update(VisitDto visitDto, long doctorId) {
        Assert.notNull(visitDto, "visit must not be null");
        Visit storedVisit = visitRepository.findByIdAndDoctorId(visitDto.getId(), doctorId);
        Assert.notNull(storedVisit, "no visit found!");

        storedVisit.setClinic(clinicRepository.findByIdAndDoctorId(visitDto.getClinicId(), doctorId));
        storedVisit.setDate(visitDto.getDate());
        storedVisit.setCost(visitDto.getCost());
        storedVisit.setPercent(visitDto.getPercent());
        storedVisit.setChild(visitDto.getChild());
        storedVisit.setFirst(visitDto.getFirst());
        storedVisit.setInfo(visitDto.getInfo());
        storedVisit.setUpdated(LocalDateTime.now());

        visitRepository.save(storedVisit);
    }

    public Visit create(VisitDto visitDto, long doctorId) {
        Visit created = visitDto.toVisit();
        created.setDoctor(userRepository.getOne(doctorId));

        created.setClinic(clinicRepository.findByIdAndDoctorId(visitDto.getClinicId(), doctorId));
        created.setPatient(patientRepository.findByIdAndDoctorId(visitDto.getPatientId(), doctorId));

        return visitRepository.save(created);
    }

    public void delete(long id, long doctorId) {
        Visit visit = visitRepository.findByIdAndDoctorId(id, doctorId);
        if (!Status.DELETED.equals(visit.getStatus())) {
            visit.setUpdated(LocalDateTime.now());
            visit.setStatus(Status.DELETED);
            visitRepository.save(visit);
        }
    }

}
