package ru.kvs.doctrspring.adapters.restapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kvs.doctrspring.adapters.restapi.dto.response.ClinicDto;
import ru.kvs.doctrspring.adapters.restapi.mapper.ClinicMapper;
import ru.kvs.doctrspring.app.ClinicService;
import ru.kvs.doctrspring.domain.Clinic;
import ru.kvs.doctrspring.domain.ids.ClinicId;
import ru.kvs.doctrspring.security.AuthUtil;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/clinics/")
public class ClinicRestController {

    private final ClinicService clinicService;
    private final ClinicMapper clinicMapper;

    @GetMapping
    public ResponseEntity<List<ClinicDto>> getAll() {
        log.info("Get all clinics");
        List<Clinic> clinics = clinicService.getAll(AuthUtil.getAuthUserId());

        return ResponseEntity.ok(clinicMapper.toClinicDtos(clinics));
    }

    @GetMapping("{id}")
    public ResponseEntity<ClinicDto> get(@PathVariable ClinicId id) {
        log.info("Get clinic by id={}", id);
        Clinic clinic = clinicService.get(id, AuthUtil.getAuthUserId());

        return ResponseEntity.ok(clinicMapper.toClinicDto(clinic));
    }

}
