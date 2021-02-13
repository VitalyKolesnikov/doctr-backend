package ru.kvs.doctrspring.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.kvs.doctrspring.dto.PatientDto;
import ru.kvs.doctrspring.model.Patient;
import ru.kvs.doctrspring.model.Status;
import ru.kvs.doctrspring.security.AuthUtil;
import ru.kvs.doctrspring.service.PatientService;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.Date;
import java.util.List;

import static ru.kvs.doctrspring.rest.PatientRestController.REST_URL;

@RestController
@RequestMapping(value = REST_URL)
public class PatientRestController {

    public final static String REST_URL = "/api/v1/patients/";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final PatientService patientService;

    public PatientRestController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public List<Patient> getAll() {
        log.info("Get all patients");
        return patientService.getAll();
    }

//    @GetMapping("{id}")
//    public Patient get(@PathVariable int id) {
//        log.info("Get patient by id");
//        return patientRepository.findById(id).orElseThrow();
//    }

    @PostMapping
    @Transactional
    public ResponseEntity<Patient> createWithLocation(@RequestBody PatientDto patientDto) {
        Patient created = patientDto.toPatient();
        created.setCreated(new Date());
        created.setUpdated(new Date());
        created.setStatus(Status.ACTIVE);
        created.setDoctor(AuthUtil.getAuthUser());
        created = patientService.save(created);
        log.info("Create new patient {}", created);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

}
