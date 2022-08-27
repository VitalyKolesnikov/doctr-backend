package ru.kvs.doctrspring.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.kvs.doctrspring.dto.ReminderDto;
import ru.kvs.doctrspring.model.Reminder;
import ru.kvs.doctrspring.security.AuthUtil;
import ru.kvs.doctrspring.service.ReminderService;

import java.net.URI;
import java.util.List;

import static ru.kvs.doctrspring.rest.ReminderRestController.REST_URL;
import static ru.kvs.doctrspring.util.ValidationUtil.assureIdConsistent;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = REST_URL)
public class ReminderRestController {

    public final static String REST_URL = "/api/v1/reminders/";

    private final ReminderService reminderService;

    @GetMapping
    public List<Reminder> getActive() {
        log.info("Get all active reminders");
        return reminderService.getActive();
    }

    @GetMapping("{id}")
    public ResponseEntity<Reminder> get(@PathVariable long id) {
        log.info("Get Reminder by id={}", id);
        Reminder reminder = reminderService.get(id, AuthUtil.getAuthUserId());
        return ResponseEntity.ok(reminder);
    }

    @GetMapping("patient/{id}")
    public List<Reminder> getForPatient(@PathVariable long id) {
        log.info("Get reminders by patientId={}", id);
        return reminderService.getForPatient(AuthUtil.getAuthUserId(), id);
    }

    @GetMapping("count/")
    public int getActiveCount() {
        log.info("Get active reminders count");
        return reminderService.getActiveCount();
    }

    @PatchMapping("complete/{id}")
    public int complete(@PathVariable long id) {
        log.info("complete reminder with id={}", id);
        return reminderService.complete(id);
    }

    @PutMapping("{id}")
    public int update(@RequestBody ReminderDto reminderDto, @PathVariable long id) {
        assureIdConsistent(reminderDto, id);
        log.info("update {}", reminderDto);
        return reminderService.update(reminderDto);
    }

    @PostMapping
    public ResponseEntity<Reminder> create(@RequestBody ReminderDto reminderDto) {
        Reminder created = reminderService.create(reminderDto);
        log.info("Create new reminder {}", reminderDto);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(reminderDto.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

}
