package ru.kvs.doctrspring.service;

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

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Transactional
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final PatientRepository patientRepository;

    public ReminderService(ReminderRepository reminderRepository, PatientRepository patientRepository) {
        this.reminderRepository = reminderRepository;
        this.patientRepository = patientRepository;
    }

    public List<Reminder> getActive() {
        return reminderRepository.getActual(AuthUtil.getAuthUserId());
    }

    public Reminder get(long id, long doctorId) {
        return reminderRepository.findByIdAndDoctorId(id, doctorId);
    }

    public List<Reminder> getForPatient(long doctorId, long patientId) {
        return reminderRepository.getAllForPatient(doctorId, patientId);
    }

    public int getActiveCount() {
        return reminderRepository.getActual(AuthUtil.getAuthUserId()).size();
    }

    public int complete(long id) {
        Reminder storedReminder = reminderRepository.findByIdAndDoctorId(id, AuthUtil.getAuthUserId());
        Assert.notNull(storedReminder, "no reminder found!");
        storedReminder.setStatus(Status.NOT_ACTIVE);
        storedReminder.setUpdated(new Date());
        reminderRepository.save(storedReminder);
        return getActiveCount();
    }

    public Reminder create(ReminderDto reminderDto) {
        Reminder created = reminderDto.toReminder();
        created.setDoctor(AuthUtil.getAuthUser());
        setPatient(reminderDto.getPatientId(), created);
        return reminderRepository.save(created);
    }

    public int update(ReminderDto reminderDto) {
        Assert.notNull(reminderDto, "reminder must not be null");
        Reminder storedReminder = reminderRepository.findByIdAndDoctorId(reminderDto.getId(), AuthUtil.getAuthUserId());
        Assert.notNull(storedReminder, "no reminder found!");
        setPatient(reminderDto.getPatientId(), storedReminder);
        storedReminder.setDate(reminderDto.getDate());
        storedReminder.setText(reminderDto.getText());
        storedReminder.setUpdated(new Date());
        reminderRepository.save(storedReminder);
        return getActiveCount();
    }

    private void setPatient(long patientId, Reminder reminder) {
        Patient patient = patientRepository.findByIdAndDoctorId(patientId, AuthUtil.getAuthUserId());
        reminder.setPatient(patient);
    }
}
