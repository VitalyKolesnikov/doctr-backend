package ru.kvs.doctrspring.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.kvs.doctrspring.dto.PatientDto;
import ru.kvs.doctrspring.model.Patient;
import ru.kvs.doctrspring.security.AuthUtil;
import ru.kvs.doctrspring.service.PatientService;

import java.net.URI;
import java.util.List;

import static ru.kvs.doctrspring.rest.PatientRestController.REST_URL;
import static ru.kvs.doctrspring.util.ValidationUtil.assureIdConsistent;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = REST_URL)
public class PatientRestController {

    public final static String REST_URL = "/api/v1/patients/";

    private final PatientService patientService;

    @GetMapping
    public List<Patient> getActive() {
        long doctorId = AuthUtil.getAuthUserId();
        log.info("Get all active patients for doctor with id={}", doctorId);
        return patientService.getActive(doctorId);
    }

    @GetMapping("{id}")
    public ResponseEntity<Patient> get(@PathVariable long id) {
        log.info("Get patient by id={}", id);
        Patient patient = patientService.get(id, AuthUtil.getAuthUserId());
        return ResponseEntity.ok(patient);
    }

    @GetMapping("suggest/{part}")
    public List<Patient> getSuggested(@PathVariable String part) {
        long doctorId = AuthUtil.getAuthUserId();
        log.info("Get patients by suggestion={}", part);
        return patientService.getSuggested(doctorId, part);
    }

    @PostMapping
    public ResponseEntity<Patient> create(@RequestBody PatientDto patientDto) {
        long doctorId = AuthUtil.getAuthUserId();
        Patient created = patientService.create(patientDto, doctorId);
        log.info("Create new patient {}", created);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Patient patient, @PathVariable long id) {
        long doctorId = AuthUtil.getAuthUserId();
        assureIdConsistent(patient, id);
        log.info("update {} for user {}", patient, doctorId);
        patientService.update(patient, doctorId);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        patientService.delete(id, AuthUtil.getAuthUserId());
    }

}
