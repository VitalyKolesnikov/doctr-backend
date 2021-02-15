package ru.kvs.doctrspring.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kvs.doctrspring.model.Clinic;
import ru.kvs.doctrspring.model.Status;
import ru.kvs.doctrspring.repository.ClinicRepository;
import ru.kvs.doctrspring.security.AuthUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClinicService {

    private final ClinicRepository clinicRepository;

    public ClinicService(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
    }

    public List<Clinic> getAll() {
        List<Clinic> clinics = clinicRepository.findAllByDoctorId(AuthUtil.getAuthUserId());
        log.info("Filtering active clinics");
        return clinics.stream()
                .filter(p -> Status.ACTIVE.equals(p.getStatus()))
                .collect(Collectors.toList());
    }

    public Clinic get(long id, long doctorId) {
        return clinicRepository.findByIdAndDoctorId(id, doctorId);
    }

}
