package ru.kvs.doctrspring.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kvs.doctrspring.domain.BaseEntity;
import ru.kvs.doctrspring.domain.Clinic;
import ru.kvs.doctrspring.domain.DoctrRepository;
import ru.kvs.doctrspring.domain.ids.ClinicId;
import ru.kvs.doctrspring.domain.ids.UserId;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClinicService {

    private final DoctrRepository doctrRepository;

    @Transactional(readOnly = true)
    public List<Clinic> getAll(UserId userId) {
        List<Clinic> clinics = doctrRepository.getClinicsByDoctorId(userId);
        return clinics.stream()
                .filter(BaseEntity::isActive)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Clinic get(ClinicId id, UserId doctorId) {
        return doctrRepository.getClinicByIdAndDoctorId(id, doctorId);
    }

}
