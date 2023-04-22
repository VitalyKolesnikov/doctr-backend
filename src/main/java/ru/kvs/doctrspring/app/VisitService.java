package ru.kvs.doctrspring.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kvs.doctrspring.domain.DoctrRepository;
import ru.kvs.doctrspring.domain.Visit;
import ru.kvs.doctrspring.domain.ids.ClinicId;
import ru.kvs.doctrspring.domain.ids.PatientId;
import ru.kvs.doctrspring.domain.ids.UserId;
import ru.kvs.doctrspring.domain.ids.VisitId;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
@Slf4j
@RequiredArgsConstructor
public class VisitService {

    private final Clock clock;
    private final DoctrRepository doctrRepository;

    @Transactional(readOnly = true)
    public Map<LocalDate, List<Visit>> getAllInTimeRangeGroupByDate(UserId doctorId, int months) {
        LocalDate dateLimit = LocalDate.now(clock).minusMonths(months);

        return doctrRepository.getVisits(doctorId).stream()
                .filter(visit -> visit.getDate().isAfter(dateLimit))
                .collect(groupingBy(Visit::getDate));
    }

    @Transactional(readOnly = true)
    public Visit get(VisitId visitId, UserId doctorId) {
        return doctrRepository.getVisitByIdAndDoctorId(visitId, doctorId);
    }

    @Transactional(readOnly = true)
    public List<Visit> getForPatient(UserId doctorId, PatientId patientId) {
        return doctrRepository.getVisitsOfPatient(doctorId, patientId);
    }

    @Transactional
    public Visit create(Visit visit, UserId doctorId, PatientId patientId, ClinicId clinicId) {
        var patient = doctrRepository.getPatientByIdAndDoctorId(patientId, doctorId);
        var clinic = doctrRepository.getClinicByIdAndDoctorId(clinicId, doctorId);

        visit.create(doctorId, patient, clinic);
        return doctrRepository.saveVisit(visit);
    }

    @Transactional
    public void update(Visit visit, VisitId visitId, UserId doctorId) {
        Visit storedVisit = doctrRepository.getVisitByIdAndDoctorId(visitId, doctorId);
        storedVisit.update(visit);
    }

    @Transactional
    public void delete(VisitId id, UserId doctorId) {
        Visit visit = doctrRepository.getVisitByIdAndDoctorId(id, doctorId);
        visit.softDelete();
    }

}
