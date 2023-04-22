package ru.kvs.doctrspring.app;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kvs.doctrspring.domain.DoctrRepository;
import ru.kvs.doctrspring.domain.Patient;
import ru.kvs.doctrspring.domain.Reminder;
import ru.kvs.doctrspring.domain.ids.PatientId;
import ru.kvs.doctrspring.domain.ids.ReminderId;
import ru.kvs.doctrspring.domain.ids.UserId;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReminderServiceTest {

    private static final UserId USER_ID = UserId.of("548f3c8b-4e06-45c6-b7ee-3c7db137796e");
    private static final PatientId PATIENT_ID = PatientId.of("0a83aef4-b000-407b-905c-8ded6ff00a3d");
    private static final ReminderId REMINDER_ID = ReminderId.of("57f1b65b-ccee-45e1-9322-d1b7ae416b23");

    @Mock
    private DoctrRepository doctrRepository;
    @Mock
    private Clock clock;
    @InjectMocks
    private ReminderService reminderService;

    @Test
    @DisplayName("getActive should return actual reminders")
    void testGetActive() {
        // given
        List<Reminder> expectedReminders = List.of(
                Reminder.builder()
                        .id(REMINDER_ID)
                        .doctorId(USER_ID)
                        .patient(Patient.builder()
                                .id(PATIENT_ID)
                                .doctorId(USER_ID)
                                .build())
                        .build()
        );
        when(doctrRepository.getActualReminders(USER_ID)).thenReturn(expectedReminders);

        // when
        List<Reminder> actualReminders = reminderService.getActive(USER_ID);

        // then
        verify(doctrRepository).getActualReminders(USER_ID);
        assertEquals(expectedReminders, actualReminders);
    }

    @Test
    @DisplayName("get should return reminder if found in repository")
    void testGetWhenFound() {
        // given
        Reminder expectedReminder = Reminder.builder()
                .id(REMINDER_ID)
                .doctorId(USER_ID)
                .patient(Patient.builder()
                        .id(PATIENT_ID)
                        .doctorId(USER_ID)
                        .build())
                .build();
        when(doctrRepository.getReminderByIdAndDoctorId(REMINDER_ID, USER_ID)).thenReturn(expectedReminder);

        // when
        Reminder actualReminder = reminderService.get(REMINDER_ID, USER_ID);

        // then
        verify(doctrRepository).getReminderByIdAndDoctorId(REMINDER_ID, USER_ID);
        assertEquals(expectedReminder, actualReminder);
    }

    @Test
    @DisplayName("getByPatient should return reminders of patient for doctor")
    void testGetByPatient() {
        // given
        List<Reminder> expectedReminders = List.of(
                Reminder.builder()
                        .id(REMINDER_ID)
                        .doctorId(USER_ID)
                        .patient(Patient.builder()
                                .id(PATIENT_ID)
                                .doctorId(USER_ID)
                                .build())
                        .build()
        );
        when(doctrRepository.getRemindersOfPatient(USER_ID, PATIENT_ID)).thenReturn(expectedReminders);

        // when
        List<Reminder> actualReminders = reminderService.getByPatient(USER_ID, PATIENT_ID);

        // then
        verify(doctrRepository).getRemindersOfPatient(USER_ID, PATIENT_ID);
        assertEquals(expectedReminders, actualReminders);
    }

    @Test
    @DisplayName("getActiveCount should return count of actual reminders")
    void testGetActiveCount() {
        // given
        List<Reminder> reminders = List.of(
                Reminder.builder()
                        .id(REMINDER_ID)
                        .doctorId(USER_ID)
                        .patient(Patient.builder()
                                .id(PATIENT_ID)
                                .doctorId(USER_ID)
                                .build())
                        .date(LocalDate.now())
                        .build()
        );
        when(doctrRepository.getActualReminders(USER_ID)).thenReturn(reminders);
        int expectedCount = reminders.size();

        // when
        int actualCount = reminderService.getActiveCount(USER_ID);

        // then
        verify(doctrRepository).getActualReminders(USER_ID);
        assertEquals(expectedCount, actualCount);
    }

    @Test
    @DisplayName("create should save reminder for patient and doctor")
    void testCreate() {
        // given
        Reminder reminder = Reminder.builder().id(REMINDER_ID).build();
        Reminder savedReminder = Reminder.builder()
                .id(REMINDER_ID)
                .doctorId(USER_ID)
                .patient(Patient.builder()
                        .id(PATIENT_ID)
                        .doctorId(USER_ID)
                        .build())
                .date(LocalDate.now())
                .build();
        when(doctrRepository.getPatientByIdAndDoctorId(PATIENT_ID, USER_ID)).thenReturn(Patient.builder().build());
        when(doctrRepository.saveReminder(reminder)).thenReturn(savedReminder);

        // when
        Reminder actualReminder = reminderService.create(reminder, PATIENT_ID, USER_ID);

        // then
        verify(doctrRepository).getPatientByIdAndDoctorId(PATIENT_ID, USER_ID);
        verify(doctrRepository).saveReminder(reminder);
        assertEquals(savedReminder, actualReminder);
    }

    @Test
    @DisplayName("complete should complete reminder and return count of actual reminders")
    void testComplete() {
        // given
        Reminder storedReminder = Reminder.builder()
                .id(REMINDER_ID)
                .doctorId(USER_ID)
                .patient(Patient.builder()
                        .id(PATIENT_ID)
                        .doctorId(USER_ID)
                        .build())
                .build();
        when(doctrRepository.getReminderByIdAndDoctorId(REMINDER_ID, USER_ID)).thenReturn(storedReminder);
        int expectedCount = 0;

        // when
        int actualCount = reminderService.complete(REMINDER_ID, USER_ID);

        // then
        verify(doctrRepository).getReminderByIdAndDoctorId(REMINDER_ID, USER_ID);
        assertEquals(expectedCount, actualCount);
    }

}
