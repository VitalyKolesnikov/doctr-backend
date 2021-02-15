package ru.kvs.doctrspring.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.kvs.doctrspring.dto.VisitDto;
import ru.kvs.doctrspring.model.Visit;
import ru.kvs.doctrspring.model.Status;
import ru.kvs.doctrspring.security.AuthUtil;
import ru.kvs.doctrspring.service.VisitService;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.Date;
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
        return visitService.getAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Visit> get(@PathVariable long id) {
        log.info("Get Visit by id={}", id);
        Visit Visit = visitService.get(id, AuthUtil.getAuthUserId());
        return ResponseEntity.ok(Visit);
    }

    @PutMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Visit Visit, @PathVariable long id) {
        long doctorId = AuthUtil.getAuthUserId();
        assureIdConsistent(Visit, id);
        log.info("update {} for user {}", Visit, doctorId);
        visitService.update(Visit, doctorId);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Visit> createWithLocation(@RequestBody Visit visit) {
        visit.setCreated(new Date());
        visit.setUpdated(new Date());
        visit.setStatus(Status.ACTIVE);
        visit.setDoctor(AuthUtil.getAuthUser());
        visit = visitService.save(visit);
        log.info("Create new visit {}", visit);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(visit.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(visit);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        visitService.delete(id, AuthUtil.getAuthUserId());
    }

}
