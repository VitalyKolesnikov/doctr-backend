package ru.kvs.doctrspring.service;

import lombok.RequiredArgsConstructor;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@Slf4j
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;
    private final ClinicRepository clinicRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Visit> getLastActive(long doctorId) {
        LocalDate twoMonthsAgo = LocalDate.now().minusMonths(2);
        return visitRepository.getActive(doctorId).stream()
                .filter(visit -> visit.getDate().isAfter(twoMonthsAgo))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DatedVisitListDto> getAllGroupByDate(long doctorId) {
        Map<LocalDate, List<Visit>> map = getLastActive(doctorId).stream()
                .collect(groupingBy(Visit::getDate));
        List<DatedVisitListDto> list = new ArrayList<>();
        map.forEach((key, value) -> list.add(new DatedVisitListDto(key, value)));
        list.sort(Collections.reverseOrder());
        return list;
    }

    @Transactional(readOnly = true)
    public Visit get(long id, long doctorId) {
        return visitRepository.findByIdAndDoctorId(id, doctorId);
    }

    @Transactional(readOnly = true)
    public List<Visit> getForPatient(long doctorId, long patientId) {
        return visitRepository.getActiveForPatient(doctorId, patientId);
    }

    @Transactional
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

    @Transactional
    public Visit create(VisitDto visitDto, long doctorId) {
        Visit created = visitDto.toVisit();
        created.setDoctor(userRepository.getOne(doctorId));

        created.setClinic(clinicRepository.findByIdAndDoctorId(visitDto.getClinicId(), doctorId));
        created.setPatient(patientRepository.findByIdAndDoctorId(visitDto.getPatientId(), doctorId));

        return visitRepository.save(created);
    }

    @Transactional
    public void delete(long id, long doctorId) {
        Visit visit = visitRepository.findByIdAndDoctorId(id, doctorId);
        if (!Status.DELETED.equals(visit.getStatus())) {
            visit.setUpdated(LocalDateTime.now());
            visit.setStatus(Status.DELETED);
            visitRepository.save(visit);
        }
    }

}
