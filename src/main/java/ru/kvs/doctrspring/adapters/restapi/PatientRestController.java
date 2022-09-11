package ru.kvs.doctrspring.adapters.restapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.kvs.doctrspring.adapters.restapi.dto.request.PatientCreateOrUpdateRequest;
import ru.kvs.doctrspring.adapters.restapi.dto.response.PatientDto;
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
    public ResponseEntity<List<PatientDto>> getActive() {
        long doctorId = AuthUtil.getAuthUserId();
        List<Patient> patients = patientService.getActive(doctorId);

        return ResponseEntity.ok(patientMapper.toPatientDtos(patients));
    }

    @GetMapping("{id}")
    public ResponseEntity<PatientDto> get(@PathVariable long id) {
        Patient patient = patientService.get(id, AuthUtil.getAuthUserId());

        return ResponseEntity.ok(patientMapper.toPatientDto(patient));
    }

    @GetMapping("suggest/{part}")
    public ResponseEntity<List<PatientDto>> getSuggested(@PathVariable String part) {
        long doctorId = AuthUtil.getAuthUserId();
        List<Patient> patients = patientService.getSuggested(doctorId, part);

        return ResponseEntity.ok(patientMapper.toPatientDtos(patients));
    }

    @PostMapping
    public ResponseEntity<PatientDto> create(@RequestBody PatientCreateOrUpdateRequest patientCreateOrUpdateRequest) {
        long doctorId = AuthUtil.getAuthUserId();
        Patient patient = patientMapper.toPatient(patientCreateOrUpdateRequest);
        Patient created = patientService.create(patient, doctorId);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(patientMapper.toPatientDto(created));
    }

    @PutMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody PatientCreateOrUpdateRequest patientCreateOrUpdateRequest, @PathVariable long id) {
        long doctorId = AuthUtil.getAuthUserId();
        Patient patient = patientMapper.toPatient(patientCreateOrUpdateRequest);

        patientService.update(patient, id, doctorId);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        patientService.delete(id, AuthUtil.getAuthUserId());
    }

}
