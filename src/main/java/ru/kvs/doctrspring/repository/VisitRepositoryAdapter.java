package ru.kvs.doctrspring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kvs.doctrspring.model.Visit;

import java.util.List;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class VisitRepositoryAdapter implements VisitRepository {

    private final VisitJpaRepository visitJpaRepository;

    @Override
    public List<Visit> getActive(long doctorId) {
        return visitJpaRepository.getActive(doctorId);
    }

    @Override
    public Visit findByIdAndDoctorId(long id, long doctorId) {
        return visitJpaRepository.findByIdAndDoctorId(id, doctorId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Visit with id [%s] not found", id)));
    }

    @Override
    public List<Visit> getActiveForPatient(long doctorId, long patientId) {
        return visitJpaRepository.getActiveForPatient(doctorId, patientId);
    }

    @Override
    public Visit save(Visit visit) {
        return visitJpaRepository.save(visit);
    }

}
