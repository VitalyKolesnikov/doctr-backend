package ru.kvs.doctrspring.adapters.restapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.kvs.doctrspring.adapters.restapi.dto.request.VisitCreateOrUpdateRequest;
import ru.kvs.doctrspring.adapters.restapi.dto.response.DatedVisitListDto;
import ru.kvs.doctrspring.adapters.restapi.dto.response.VisitDto;
import ru.kvs.doctrspring.adapters.restapi.mapper.VisitMapper;
import ru.kvs.doctrspring.app.VisitService;
import ru.kvs.doctrspring.domain.Visit;
import ru.kvs.doctrspring.security.AuthUtil;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.kvs.doctrspring.adapters.restapi.VisitRestController.REST_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = REST_URL)
public class VisitRestController {

    public final static String REST_URL = "/api/v1/visits/";

    private final VisitService visitService;
    private final VisitMapper visitMapper;

    @GetMapping
    public List<DatedVisitListDto> getAllGroupByDate() {
        long doctorId = AuthUtil.getAuthUserId();
        Map<LocalDate, List<Visit>> visitsByDate = visitService.getAllGroupByDate(doctorId);
        List<DatedVisitListDto> datedVisitListDtos = new ArrayList<>();

        visitsByDate.forEach((key, value) -> datedVisitListDtos.add(new DatedVisitListDto(key, value)));
        datedVisitListDtos.sort(Collections.reverseOrder());

        return datedVisitListDtos;
    }

    @GetMapping("{id}")
    public ResponseEntity<VisitDto> get(@PathVariable long id) {
        Visit visit = visitService.get(id, AuthUtil.getAuthUserId());

        return ResponseEntity.ok(visitMapper.toVisitDto(visit));
    }

    @GetMapping("patient/{patientId}")
    public ResponseEntity<List<VisitDto>> getForPatient(@PathVariable long patientId) {
        List<Visit> visits = visitService.getForPatient(AuthUtil.getAuthUserId(), patientId);

        return ResponseEntity.ok(visitMapper.toVisitDtos(visits));
    }

    @PostMapping
    public ResponseEntity<VisitDto> create(@RequestBody VisitCreateOrUpdateRequest visitCreateOrUpdateRequest) {
        long doctorId = AuthUtil.getAuthUserId();
        Visit visit = visitMapper.toVisit(visitCreateOrUpdateRequest);
        Visit created = visitService.create(
                visit,
                doctorId,
                visitCreateOrUpdateRequest.getPatientId(),
                visitCreateOrUpdateRequest.getClinicId()
        );

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(visitMapper.toVisitDto(created));
    }

    @PutMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> update(@RequestBody VisitCreateOrUpdateRequest visitCreateOrUpdateRequest, @PathVariable long id) {
        long doctorId = AuthUtil.getAuthUserId();
        Visit visit = visitMapper.toVisit(visitCreateOrUpdateRequest);

        visitService.update(visit, id, doctorId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable long id) {
        visitService.delete(id, AuthUtil.getAuthUserId());

        return ResponseEntity.noContent().build();
    }

}
