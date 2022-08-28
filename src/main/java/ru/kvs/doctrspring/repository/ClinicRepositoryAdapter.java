package ru.kvs.doctrspring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kvs.doctrspring.model.Clinic;

import java.util.List;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class ClinicRepositoryAdapter implements ClinicRepository {
    private final ClinicJpaRepository clinicJpaRepository;

    @Override
    public List<Clinic> findAllByDoctorId(long doctorId) {
        return clinicJpaRepository.findAllByDoctorId(doctorId);
    }

    @Override
    public Clinic findByIdAndDoctorId(long id, long doctorId) {
        return clinicJpaRepository.findByIdAndDoctorId(id, doctorId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Clinic with id [%s] not found", id)));
    }
}
