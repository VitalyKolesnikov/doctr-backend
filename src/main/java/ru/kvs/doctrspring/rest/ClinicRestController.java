package ru.kvs.doctrspring.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kvs.doctrspring.model.Clinic;
import ru.kvs.doctrspring.security.AuthUtil;
import ru.kvs.doctrspring.service.ClinicService;

import java.util.List;

import static ru.kvs.doctrspring.rest.ClinicRestController.REST_URL;

@RestController
@RequestMapping(value = REST_URL)
public class ClinicRestController {

    public final static String REST_URL = "/api/v1/clinics/";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final ClinicService clinicService;

    public ClinicRestController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @GetMapping
    public List<Clinic> getAll() {
        log.info("Get all clinics");
        return clinicService.getAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Clinic> get(@PathVariable long id) {
        log.info("Get clinic by id={}", id);
        Clinic clinic = clinicService.get(id, AuthUtil.getAuthUserId());
        return ResponseEntity.ok(clinic);
    }

}
