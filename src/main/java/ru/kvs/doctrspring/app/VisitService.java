package ru.kvs.doctrspring.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kvs.doctrspring.domain.DoctrRepository;
import ru.kvs.doctrspring.domain.Visit;

import java.time.Clock;
import java.time.LocalDate;
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
    public Map<LocalDate, List<Visit>> getAllGroupByDate(long doctorId) {
        return getLastActive(doctorId).stream()
                .collect(groupingBy(Visit::getDate));
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
    public Visit create(Visit visit, long doctorId, long patientId, long clinicId) {
        var patient = doctrRepository.getPatientByIdAndDoctorId(patientId, doctorId);
        var clinic = doctrRepository.getClinicByIdAndDoctorId(clinicId, doctorId);

        visit.create(doctorId, patient, clinic);
        return doctrRepository.saveVisit(visit);
    }

    @Transactional
    public void update(Visit visit, long visitId, long doctorId) {
        Visit storedVisit = doctrRepository.getVisitByIdAndDoctorId(visitId, doctorId);
        storedVisit.update(visit);
    }

    @Transactional
    public void delete(long id, long doctorId) {
        Visit visit = doctrRepository.getVisitByIdAndDoctorId(id, doctorId);
        visit.softDelete();
    }

}
