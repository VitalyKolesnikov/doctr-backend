package ru.kvs.doctrspring.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.kvs.doctrspring.dto.VisitDto;
import ru.kvs.doctrspring.model.Clinic;
import ru.kvs.doctrspring.model.Patient;
import ru.kvs.doctrspring.model.Status;
import ru.kvs.doctrspring.model.Visit;
import ru.kvs.doctrspring.repository.ClinicRepository;
import ru.kvs.doctrspring.repository.PatientRepository;
import ru.kvs.doctrspring.repository.VisitRepository;
import ru.kvs.doctrspring.security.AuthUtil;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Transactional
public class VisitService extends BaseService {

    private static final Sort SORT_BY_DATE = Sort.by(Sort.Direction.DESC, "date");

    private final VisitRepository visitRepository;
    private final ClinicRepository clinicRepository;
    private final PatientRepository patientRepository;

    public VisitService(VisitRepository visitRepository,
                        ClinicRepository clinicRepository,
                        PatientRepository patientRepository) {
        this.visitRepository = visitRepository;
        this.clinicRepository = clinicRepository;
        this.patientRepository = patientRepository;
    }

    public List<Visit> getAll() {
        List<Visit> visits =
                visitRepository.findAllByDoctorIdOrderByDateDescCreatedDesc(AuthUtil.getAuthUserId(), SORT_BY_DATE);
        return filterActive(visits);
    }

    public Visit get(long id, long doctorId) {
        return visitRepository.findByIdAndDoctorId(id, doctorId);
    }

    public List<Visit> getForPatient(long doctorId, long patientId) {
        List<Visit> visits =
                visitRepository.findAllByDoctorIdAndPatientIdOrderByDateDescCreatedDesc(doctorId, patientId, SORT_BY_DATE);
        return filterActive(visits);
    }

    public void update(VisitDto visitDto, long doctorId) {
        Assert.notNull(visitDto, "visit must not be null");
        Visit storedVisit = visitRepository.findByIdAndDoctorId(visitDto.getId(), doctorId);
        Assert.notNull(storedVisit, "no visit found!");
        setClinicAndPatient(visitDto.getClinicId(), visitDto.getPatientId(), storedVisit);
        storedVisit.setDate(visitDto.getDate());
        storedVisit.setCost(visitDto.getCost());
        storedVisit.setPercent(visitDto.getPercent());
        storedVisit.setChild(visitDto.getChild());
        storedVisit.setFirst(visitDto.getFirst());
        storedVisit.setInfo(visitDto.getInfo());
        storedVisit.setUpdated(new Date());
        visitRepository.save(storedVisit);
    }

    public Visit create(VisitDto visitDto) {
        Visit created = visitDto.toVisit();
        created.setDoctor(AuthUtil.getAuthUser());
        setClinicAndPatient(visitDto.getClinicId(), visitDto.getPatientId(), created);
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

    private void setClinicAndPatient(long clinicId, long patientId, Visit visit) {
        Clinic clinic = clinicRepository.findByIdAndDoctorId(clinicId, AuthUtil.getAuthUserId());
        Patient patient = patientRepository.findByIdAndDoctorId(patientId, AuthUtil.getAuthUserId());
        visit.setClinic(clinic);
        visit.setPatient(patient);
    }

}
