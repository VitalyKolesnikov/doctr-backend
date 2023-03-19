package ru.kvs.doctrspring.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kvs.doctrspring.domain.DoctrRepository;
import ru.kvs.doctrspring.domain.Reminder;
import ru.kvs.doctrspring.domain.ids.PatientId;
import ru.kvs.doctrspring.domain.ids.ReminderId;
import ru.kvs.doctrspring.domain.ids.UserId;

import java.time.Clock;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReminderService {

    private final Clock clock;
    private final DoctrRepository doctrRepository;

    @Transactional(readOnly = true)
    public List<Reminder> getActive(UserId doctorId) {
        return doctrRepository.getActualReminders(doctorId);
    }

    @Transactional(readOnly = true)
    public Reminder get(ReminderId reminderId, UserId doctorId) {
        return doctrRepository.getReminderByIdAndDoctorId(reminderId, doctorId);
    }

    @Transactional(readOnly = true)
    public List<Reminder> getByPatient(UserId doctorId, PatientId patientId) {
        return doctrRepository.getRemindersOfPatient(doctorId, patientId);
    }

    @Transactional(readOnly = true)
    public int getActiveCount(UserId doctorId) {
        return doctrRepository.getActualReminders(doctorId).size();
    }

    @Transactional
    public Reminder create(Reminder reminder, PatientId patientId, UserId doctorId) {
        var patient = doctrRepository.getPatientByIdAndDoctorId(patientId, doctorId);
        reminder.create(doctorId, patient);
        return doctrRepository.saveReminder(reminder);
    }

    @Transactional
    public int update(Reminder reminder, ReminderId reminderId, UserId doctorId) {
        Reminder storedReminder = doctrRepository.getReminderByIdAndDoctorId(reminderId, doctorId);
        storedReminder.update(reminder);
        return getActiveCount(doctorId);
    }

    @Transactional
    public int complete(ReminderId id, UserId doctorId) {
        Reminder storedReminder = doctrRepository.getReminderByIdAndDoctorId(id, doctorId);
        storedReminder.complete();
        return getActiveCount(doctorId);
    }

}
