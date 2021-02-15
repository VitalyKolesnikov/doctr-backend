package ru.kvs.doctrspring.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.kvs.doctrspring.model.Status;
import ru.kvs.doctrspring.model.Visit;
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

    public VisitService(VisitRepository visitRepository, UserRepository userRepository) {
        this.visitRepository = visitRepository;
        this.userRepository = userRepository;
    }

    public List<Visit> getAll() {
        List<Visit> visits = visitRepository.findAllByDoctorId(AuthUtil.getAuthUserId());
        log.info("Filtering active visits");
        return visits.stream()
                .filter(p -> Status.ACTIVE.equals(p.getStatus()))
                .collect(Collectors.toList());
    }

    public Visit get(long id, long doctorId) {
        return visitRepository.findByIdAndDoctorId(id, doctorId);
    }

    public void update(Visit visit, long doctorId) {
        Assert.notNull(visit, "visit must not be null");
        Visit storedVisit = visitRepository.findByIdAndDoctorId(visit.getId(), doctorId);
        Assert.notNull(storedVisit, "no visit found!");
        visit.setDoctor(userRepository.getOne(doctorId));
        visit.setCreated(storedVisit.getCreated());
        visit.setUpdated(new Date());
        visit.setStatus(storedVisit.getStatus());
        visitRepository.save(visit);
    }

    public Visit save(Visit visit) {
        return visitRepository.save(visit);
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
