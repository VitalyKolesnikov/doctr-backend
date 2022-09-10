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
import ru.kvs.doctrspring.repository.DoctrRepository;

import java.time.Clock;
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

    private final Clock clock;
    private final DoctrRepository doctrRepository;

    @Transactional(readOnly = true)
    public List<Visit> getLastActive(long doctorId) {
        LocalDate sixMonthsAgo = LocalDate.now(clock).minusMonths(6);
        return doctrRepository.getVisits(doctorId).stream()
                .filter(visit -> visit.getDate().isAfter(sixMonthsAgo))
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
        return doctrRepository.getVisitByIdAndDoctorId(id, doctorId);
    }

    @Transactional(readOnly = true)
    public List<Visit> getForPatient(long doctorId, long patientId) {
        return doctrRepository.getVisitsOfPatient(doctorId, patientId);
    }

    @Transactional
    public Visit create(VisitDto visitDto, long doctorId) {
        Visit created = visitDto.toVisit();
        created.setDoctor(doctrRepository.getUser(doctorId));

        created.setClinic(doctrRepository.getClinicByIdAndDoctorId(visitDto.getClinicId(), doctorId));
        created.setPatient(doctrRepository.getPatientByIdAndDoctorId(visitDto.getPatientId(), doctorId));

        return doctrRepository.saveVisit(created);
    }

    @Transactional
    public void update(VisitDto visitDto, long doctorId) {
        Assert.notNull(visitDto, "visit must not be null");
        Visit storedVisit = doctrRepository.getVisitByIdAndDoctorId(visitDto.getId(), doctorId);

        storedVisit.setClinic(doctrRepository.getClinicByIdAndDoctorId(visitDto.getClinicId(), doctorId));
        storedVisit.setDate(visitDto.getDate());
        storedVisit.setCost(visitDto.getCost());
        storedVisit.setPercent(visitDto.getPercent());
        storedVisit.setChild(visitDto.getChild());
        storedVisit.setFirst(visitDto.getFirst());
        storedVisit.setInfo(visitDto.getInfo());
        storedVisit.setUpdated(LocalDateTime.now(clock));
    }

    @Transactional
    public void delete(long id, long doctorId) {
        Visit visit = doctrRepository.getVisitByIdAndDoctorId(id, doctorId);
        if (!Status.DELETED.equals(visit.getStatus())) {
            visit.setUpdated(LocalDateTime.now(clock));
            visit.setStatus(Status.DELETED);
        }
    }

}
