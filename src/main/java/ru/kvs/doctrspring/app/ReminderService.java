package ru.kvs.doctrspring.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kvs.doctrspring.domain.DoctrRepository;
import ru.kvs.doctrspring.domain.Reminder;

import java.time.Clock;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReminderService {

    private final Clock clock;
    private final DoctrRepository doctrRepository;

    @Transactional(readOnly = true)
    public List<Reminder> getActive(long doctorId) {
        return doctrRepository.getActualReminders(doctorId);
    }

    @Transactional(readOnly = true)
    public Reminder get(long reminderId, long doctorId) {
        return doctrRepository.getReminderByIdAndDoctorId(reminderId, doctorId);
    }

    @Transactional(readOnly = true)
    public List<Reminder> getByPatient(long doctorId, long patientId) {
        return doctrRepository.getRemindersOfPatient(doctorId, patientId);
    }

    @Transactional(readOnly = true)
    public int getActiveCount(long doctorId) {
        return doctrRepository.getActualReminders(doctorId).size();
    }

    @Transactional
    public Reminder create(Reminder reminder, Long patientId, long doctorId) {
        var patient = doctrRepository.getPatientByIdAndDoctorId(patientId, doctorId);
        reminder.create(doctorId, patient);
        return doctrRepository.saveReminder(reminder);
    }

    @Transactional
    public int update(Reminder reminder, long reminderId, long doctorId) {
        Reminder storedReminder = doctrRepository.getReminderByIdAndDoctorId(reminderId, doctorId);
        storedReminder.update(reminder);
        return getActiveCount(doctorId);
    }

    @Transactional
    public int complete(long id, long doctorId) {
        Reminder storedReminder = doctrRepository.getReminderByIdAndDoctorId(id, doctorId);
        storedReminder.complete();
        return getActiveCount(doctorId);
    }

}
