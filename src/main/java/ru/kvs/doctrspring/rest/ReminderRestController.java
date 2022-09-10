package ru.kvs.doctrspring.rest;

import lombok.RequiredArgsConstructor;
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

@RestController
@RequiredArgsConstructor
@RequestMapping(value = REST_URL)
public class ReminderRestController {

    public final static String REST_URL = "/api/v1/reminders/";

    private final ReminderService reminderService;

    @GetMapping
    public List<Reminder> getActive() {
        return reminderService.getActive();
    }

    @GetMapping("{id}")
    public ResponseEntity<Reminder> get(@PathVariable long id) {
        Reminder reminder = reminderService.get(id, AuthUtil.getAuthUserId());
        return ResponseEntity.ok(reminder);
    }

    @GetMapping("patient/{id}")
    public List<Reminder> getForPatient(@PathVariable long id) {
        return reminderService.getForPatient(AuthUtil.getAuthUserId(), id);
    }

    @GetMapping("count/")
    public int getActiveCount() {
        return reminderService.getActiveCount();
    }

    @PostMapping
    public ResponseEntity<Reminder> create(@RequestBody ReminderDto reminderDto) {
        long doctorId = AuthUtil.getAuthUserId();
        Reminder created = reminderService.create(reminderDto, doctorId);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(reminderDto.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping("{id}")
    public int update(@RequestBody ReminderDto reminderDto, @PathVariable long id) {
        assureIdConsistent(reminderDto, id);
        return reminderService.update(reminderDto);
    }

    @PatchMapping("complete/{id}")
    public int complete(@PathVariable long id) {
        return reminderService.complete(id);
    }

}
