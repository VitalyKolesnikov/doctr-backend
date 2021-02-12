package ru.kvs.doctrspring.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kvs.doctrspring.model.Patient;
import ru.kvs.doctrspring.repository.PatientRepository;
import ru.kvs.doctrspring.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/patients")
public class PatientRestController {

    private final static int DOCTOR_ID = 1000;

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public PatientRestController(PatientRepository patientRepository, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Patient> getAll() {
        log.info("Get all patients");
        return patientRepository.findAll();
    }

//    @GetMapping("/{id}")
//    public Patient get(@PathVariable int id) {
//        log.info("Get patient by id");
//        return patientRepository.findById(id).orElseThrow();
//    }
//
//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//    @Transactional
//    public ResponseEntity<Patient> createWithLocation(@Valid @RequestBody PatientDto patientDto) {
//        checkNew(patientDto);
//        User user = userRepository.findById(DOCTOR_ID).orElseThrow();
//        Patient created = patientRepository.save(new Patient(patientDto, user));
//        log.info("Create new patient {}", created);
//
//        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path(REST_URL + "/{id}")
//                .buildAndExpand(created.getId()).toUri();
//        return ResponseEntity.created(uriOfNewResource).body(created);
//    }

}
