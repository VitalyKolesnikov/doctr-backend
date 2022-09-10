package ru.kvs.doctrspring.adapters.restapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kvs.doctrspring.domain.Clinic;
import ru.kvs.doctrspring.security.AuthUtil;
import ru.kvs.doctrspring.app.ClinicService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/clinics/")
public class ClinicRestController {

    private final ClinicService clinicService;

    @GetMapping
    public List<Clinic> getAll() {
        log.info("Get all clinics");
        return clinicService.getAll(AuthUtil.getAuthUserId());
    }

    @GetMapping("{id}")
    public ResponseEntity<Clinic> get(@PathVariable long id) {
        log.info("Get clinic by id={}", id);
        Clinic clinic = clinicService.get(id, AuthUtil.getAuthUserId());
        return ResponseEntity.ok(clinic);
    }

}
