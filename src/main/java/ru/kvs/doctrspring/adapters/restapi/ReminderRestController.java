package ru.kvs.doctrspring.adapters.restapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.kvs.doctrspring.adapters.restapi.dto.request.ReminderCreateOrUpdateRequest;
import ru.kvs.doctrspring.adapters.restapi.dto.response.ReminderDto;
import ru.kvs.doctrspring.adapters.restapi.mapper.ReminderMapper;
import ru.kvs.doctrspring.app.ReminderService;
import ru.kvs.doctrspring.domain.Reminder;
import ru.kvs.doctrspring.domain.ids.PatientId;
import ru.kvs.doctrspring.domain.ids.ReminderId;
import ru.kvs.doctrspring.domain.ids.UserId;
import ru.kvs.doctrspring.security.AuthUtil;

import java.net.URI;
import java.util.List;

import static ru.kvs.doctrspring.adapters.restapi.ReminderRestController.REST_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = REST_URL)
public class ReminderRestController {

    public final static String REST_URL = "/api/v1/reminders/";

    private final ReminderService reminderService;
    private final ReminderMapper reminderMapper;

    @GetMapping
    public ResponseEntity<List<ReminderDto>> getActive() {
        List<Reminder> reminders = reminderService.getActive(AuthUtil.getAuthUserId());

        return ResponseEntity.ok(reminderMapper.toReminderDtos(reminders));
    }

    @GetMapping("{id}")
    public ResponseEntity<ReminderDto> get(@PathVariable ReminderId id) {
        Reminder reminder = reminderService.get(id, AuthUtil.getAuthUserId());

        return ResponseEntity.ok(reminderMapper.toReminderDto(reminder));
    }

    @GetMapping("patient/{id}")
    public ResponseEntity<List<ReminderDto>> getForPatient(@PathVariable PatientId id) {
        List<Reminder> reminders = reminderService.getByPatient(AuthUtil.getAuthUserId(), id);

        return ResponseEntity.ok(reminderMapper.toReminderDtos(reminders));
    }

    @GetMapping("count/")
    public ResponseEntity<Integer> getActiveCount() {
        return ResponseEntity.ok(reminderService.getActiveCount(AuthUtil.getAuthUserId()));
    }

    @PostMapping
    public ResponseEntity<ReminderDto> create(@RequestBody ReminderCreateOrUpdateRequest reminderCreateOrUpdateRequest) {
        UserId doctorId = AuthUtil.getAuthUserId();
        Reminder reminder = reminderMapper.toReminder(reminderCreateOrUpdateRequest);
        Reminder created = reminderService.create(reminder, reminderCreateOrUpdateRequest.getPatientId(), doctorId);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(reminderMapper.toReminderDto(created));
    }

    @PutMapping("{id}")
    public ResponseEntity<Integer> update(@RequestBody ReminderCreateOrUpdateRequest reminderCreateOrUpdateRequest, @PathVariable ReminderId id) {
        Reminder reminder = reminderMapper.toReminder(reminderCreateOrUpdateRequest);

        return ResponseEntity.ok(reminderService.update(reminder, id, AuthUtil.getAuthUserId()));
    }

    @PatchMapping("complete/{id}")
    public ResponseEntity<Integer> complete(@PathVariable ReminderId id) {
        return ResponseEntity.ok(reminderService.complete(id, AuthUtil.getAuthUserId()));
    }

}
