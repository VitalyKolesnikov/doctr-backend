package ru.kvs.doctrspring.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.kvs.doctrspring.dto.VisitDto;
import ru.kvs.doctrspring.model.Visit;
import ru.kvs.doctrspring.security.AuthUtil;
import ru.kvs.doctrspring.service.VisitService;

import java.net.URI;
import java.util.List;

import static ru.kvs.doctrspring.rest.VisitRestController.REST_URL;
import static ru.kvs.doctrspring.util.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = REST_URL)
public class VisitRestController {

    public final static String REST_URL = "/api/v1/visits/";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final VisitService visitService;

    public VisitRestController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping
    public List<Visit> getAll() {
        log.info("Get all Visits");
        return visitService.getActive();
    }

    @GetMapping("{id}")
    public ResponseEntity<Visit> get(@PathVariable long id) {
        log.info("Get Visit by id={}", id);
        Visit Visit = visitService.get(id, AuthUtil.getAuthUserId());
        return ResponseEntity.ok(Visit);
    }

    @GetMapping("patient/{id}")
    public List<Visit> getForPatient(@PathVariable long id) {
        log.info("Get visits by patientId={}", id);
        return visitService.getForPatient(AuthUtil.getAuthUserId(), id);
    }

    @PutMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody VisitDto visitDto, @PathVariable long id) {
        long doctorId = AuthUtil.getAuthUserId();
        assureIdConsistent(visitDto, id);
        log.info("update {} for user {}", visitDto, doctorId);
        visitService.update(visitDto, doctorId);
    }

    @PostMapping
    public ResponseEntity<Visit> createWithLocation(@RequestBody VisitDto visitDto) {
        Visit created = visitService.create(visitDto);
        log.info("Create new visit {}", visitDto);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(visitDto.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        visitService.delete(id, AuthUtil.getAuthUserId());
    }

}
