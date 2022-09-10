package ru.kvs.doctrspring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.kvs.doctrspring.dto.ReminderDto;
import ru.kvs.doctrspring.model.Patient;
import ru.kvs.doctrspring.model.Reminder;
import ru.kvs.doctrspring.model.Status;
import ru.kvs.doctrspring.repository.DoctrRepository;
import ru.kvs.doctrspring.security.AuthUtil;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReminderService {

    private final Clock clock;
    private final DoctrRepository doctrRepository;

    @Transactional(readOnly = true)
    public List<Reminder> getActive() {
        return doctrRepository.getActualReminders(AuthUtil.getAuthUserId());
    }

    @Transactional(readOnly = true)
    public Reminder get(long reminderId, long doctorId) {
        return doctrRepository.getReminderByIdAndDoctorId(reminderId, doctorId);
    }

    @Transactional(readOnly = true)
    public List<Reminder> getForPatient(long doctorId, long patientId) {
        return doctrRepository.getRemindersOfPatient(doctorId, patientId);
    }

    @Transactional(readOnly = true)
    public int getActiveCount() {
        return doctrRepository.getActualReminders(AuthUtil.getAuthUserId()).size();
    }

    @Transactional
    public Reminder create(ReminderDto reminderDto, long doctorId) {
        Reminder created = reminderDto.toReminder();
        created.setDoctor(doctrRepository.getUser(doctorId));
        setPatient(reminderDto.getPatientId(), created);
        return doctrRepository.saveReminder(created);
    }

    @Transactional
    public int update(ReminderDto reminderDto) {
        Assert.notNull(reminderDto, "reminder must not be null");
        Reminder storedReminder = doctrRepository.getReminderByIdAndDoctorId(reminderDto.getId(), AuthUtil.getAuthUserId());
        setPatient(reminderDto.getPatientId(), storedReminder);
        storedReminder.setDate(reminderDto.getDate());
        storedReminder.setText(reminderDto.getText());
        storedReminder.setUpdated(LocalDateTime.now(clock));
        return getActiveCount();
    }

    @Transactional
    public int complete(long id) {
        Reminder storedReminder = doctrRepository.getReminderByIdAndDoctorId(id, AuthUtil.getAuthUserId());
        storedReminder.setStatus(Status.NOT_ACTIVE);
        storedReminder.setUpdated(LocalDateTime.now(clock));
        return getActiveCount();
    }

    private void setPatient(long patientId, Reminder reminder) {
        Patient patient = doctrRepository.getPatientByIdAndDoctorId(patientId, AuthUtil.getAuthUserId());
        reminder.setPatient(patient);
    }

}
