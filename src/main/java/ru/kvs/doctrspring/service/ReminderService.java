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
import ru.kvs.doctrspring.repository.PatientRepository;
import ru.kvs.doctrspring.repository.ReminderRepository;
import ru.kvs.doctrspring.security.AuthUtil;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final PatientRepository patientRepository;

    @Transactional(readOnly = true)
    public List<Reminder> getActive() {
        return reminderRepository.getActual(AuthUtil.getAuthUserId());
    }

    @Transactional(readOnly = true)
    public Reminder get(long id, long doctorId) {
        return reminderRepository.findByIdAndDoctorId(id, doctorId);
    }

    @Transactional(readOnly = true)
    public List<Reminder> getForPatient(long doctorId, long patientId) {
        return reminderRepository.getAllForPatient(doctorId, patientId);
    }

    @Transactional(readOnly = true)
    public int getActiveCount() {
        return reminderRepository.getActual(AuthUtil.getAuthUserId()).size();
    }

    @Transactional
    public int complete(long id) {
        Reminder storedReminder = reminderRepository.findByIdAndDoctorId(id, AuthUtil.getAuthUserId());
        Assert.notNull(storedReminder, "no reminder found!");
        storedReminder.setStatus(Status.NOT_ACTIVE);
        storedReminder.setUpdated(LocalDateTime.now());
        reminderRepository.save(storedReminder);
        return getActiveCount();
    }

    @Transactional
    public Reminder create(ReminderDto reminderDto) {
        Reminder created = reminderDto.toReminder();
        created.setDoctor(AuthUtil.getAuthUser());
        setPatient(reminderDto.getPatientId(), created);
        return reminderRepository.save(created);
    }

    @Transactional
    public int update(ReminderDto reminderDto) {
        Assert.notNull(reminderDto, "reminder must not be null");
        Reminder storedReminder = reminderRepository.findByIdAndDoctorId(reminderDto.getId(), AuthUtil.getAuthUserId());
        Assert.notNull(storedReminder, "no reminder found!");
        setPatient(reminderDto.getPatientId(), storedReminder);
        storedReminder.setDate(reminderDto.getDate());
        storedReminder.setText(reminderDto.getText());
        storedReminder.setUpdated(LocalDateTime.now());
        reminderRepository.save(storedReminder);
        return getActiveCount();
    }

    private void setPatient(long patientId, Reminder reminder) {
        Patient patient = patientRepository.findByIdAndDoctorId(patientId, AuthUtil.getAuthUserId());
        reminder.setPatient(patient);
    }
}
