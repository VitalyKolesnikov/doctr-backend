package ru.kvs.doctrspring.adapters.restapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.kvs.doctrspring.adapters.restapi.dto.PatientDto;
import ru.kvs.doctrspring.adapters.restapi.mapper.PatientMapper;
import ru.kvs.doctrspring.app.PatientService;
import ru.kvs.doctrspring.domain.Patient;
import ru.kvs.doctrspring.security.AuthUtil;

import java.net.URI;
import java.util.List;

import static ru.kvs.doctrspring.adapters.restapi.PatientRestController.REST_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = REST_URL)
public class PatientRestController {

    public final static String REST_URL = "/api/v1/patients/";

    private final PatientService patientService;
    private final PatientMapper patientMapper;

    @GetMapping
    public List<Patient> getActive() {
        long doctorId = AuthUtil.getAuthUserId();
        return patientService.getActive(doctorId);
    }

    @GetMapping("{id}")
    public ResponseEntity<PatientDto> get(@PathVariable long id) {
        Patient patient = patientService.get(id, AuthUtil.getAuthUserId());
        return ResponseEntity.ok(patientMapper.toDto(patient));
    }

    @GetMapping("suggest/{part}")
    public List<Patient> getSuggested(@PathVariable String part) {
        long doctorId = AuthUtil.getAuthUserId();
        return patientService.getSuggested(doctorId, part);
    }

    @PostMapping
    public ResponseEntity<Patient> create(@RequestBody PatientDto patientDto) {
        long doctorId = AuthUtil.getAuthUserId();
        Patient created = patientService.create(patientDto, doctorId);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Patient patient, @PathVariable long id) {
        long doctorId = AuthUtil.getAuthUserId();
        patientService.update(patient, id, doctorId);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        patientService.delete(id, AuthUtil.getAuthUserId());
    }

}
