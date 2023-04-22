package ru.kvs.doctrspring.adapters.database;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kvs.doctrspring.adapters.database.jpa.*;
import ru.kvs.doctrspring.domain.*;
import ru.kvs.doctrspring.domain.ids.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DoctrRepositoryDbAdapterTest {

    private static final UserId USER_ID = UserId.of("548f3c8b-4e06-45c6-b7ee-3c7db137796e");
    private static final ClinicId CLINIC_ID = ClinicId.of("333fda40-6b43-4770-912c-81dc6e1ec163");
    private static final PatientId PATIENT_ID = PatientId.of("0a83aef4-b000-407b-905c-8ded6ff00a3d");
    private static final VisitId VISIT1_ID = VisitId.of("6306f5f8-a00b-4d09-a652-c270f63254ad");
    private static final VisitId VISIT2_ID = VisitId.of("c8be3521-7b22-4666-8629-58be9dfe7276");
    private static final ReminderId REMINDER1_ID = ReminderId.of("57f1b65b-ccee-45e1-9322-d1b7ae416b23");
    private static final ReminderId REMINDER2_ID = ReminderId.of("9352cf86-ba52-4466-9dd4-f4882058e4c2");

    @Mock
    private UserJpaRepository userJpaRepository;
    @Mock
    private ClinicJpaRepository clinicJpaRepository;
    @Mock
    private PatientJpaRepository patientJpaRepository;
    @Mock
    private VisitJpaRepository visitJpaRepository;
    @Mock
    private ReminderJpaRepository reminderJpaRepository;

    @InjectMocks
    private DoctrRepositoryDbAdapter adapter;

    @Test
    @DisplayName("getUser should return User if found in repository")
    void testGetUserWhenFound() {
        // given
        User expectedUser = User.builder().id(USER_ID).build();
        when(userJpaRepository.findById(USER_ID)).thenReturn(java.util.Optional.of(expectedUser));

        // when
        User actualUser = adapter.getUser(USER_ID);

        // then
        verify(userJpaRepository).findById(USER_ID);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    @DisplayName("getUser should throw NoSuchElementException if not found in repository")
    void testGetUserWhenNotFound() {
        // given
        when(userJpaRepository.findById(USER_ID)).thenReturn(java.util.Optional.empty());

        // when then
        assertThrows(NoSuchElementException.class, () -> adapter.getUser(USER_ID));
        verify(userJpaRepository).findById(USER_ID);
    }

    @Test
    @DisplayName("getUserByUsernameIgnoreCase should call corresponding method in repository")
    void testGetUserByUsernameIgnoreCase() {
        // given
        String username = "some_username";
        User expectedUser = User.builder().id(USER_ID).build();
        when(userJpaRepository.getUserByUsernameIgnoreCase(username)).thenReturn(expectedUser);

        // when
        User actualUser = adapter.getUserByUsernameIgnoreCase(username);

        // then
        verify(userJpaRepository).getUserByUsernameIgnoreCase(username);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    @DisplayName("getClinicsByDoctorId should call corresponding method in repository")
    void testGetClinicsByDoctorId() {
        // when
        adapter.getClinicsByDoctorId(USER_ID);

        // then
        verify(clinicJpaRepository).findAllByDoctorId(USER_ID);
    }

    @Test
    @DisplayName("getClinicByIdAndDoctorId should return Clinic if found in repository")
    void testGetClinicByIdAndDoctorIdWhenFound() {
        // given
        Clinic expectedClinic = Clinic.builder()
                .id(CLINIC_ID)
                .doctorId(USER_ID)
                .build();
        when(clinicJpaRepository.findByIdAndDoctorId(CLINIC_ID, USER_ID)).thenReturn(java.util.Optional.of(expectedClinic));

        // when
        Clinic actualClinic = adapter.getClinicByIdAndDoctorId(CLINIC_ID, USER_ID);

        // then
        verify(clinicJpaRepository).findByIdAndDoctorId(CLINIC_ID, USER_ID);
        assertEquals(expectedClinic, actualClinic);
    }

    @Test
    @DisplayName("getClinicByIdAndDoctorId should throw NoSuchElementException if not found in repository")
    void testGetClinicByIdAndDoctorIdWhenNotFound() {
        // when
        when(clinicJpaRepository.findByIdAndDoctorId(CLINIC_ID, USER_ID)).thenReturn(java.util.Optional.empty());

        // then
        assertThrows(NoSuchElementException.class, () -> adapter.getClinicByIdAndDoctorId(CLINIC_ID, USER_ID));
        verify(clinicJpaRepository).findByIdAndDoctorId(CLINIC_ID, USER_ID);
    }

    @Test
    @DisplayName("getPatients should call corresponding method in repository")
    void testGetPatients() {
        // when
        adapter.getPatients(USER_ID);

        // then
        verify(patientJpaRepository).getActive(USER_ID);
    }

    @Test
    @DisplayName("getPatientByIdAndDoctorId should return patient if found in repository")
    void testGetPatientByIdAndDoctorIdWhenFound() {
        // given
        Patient expectedPatient = Patient.builder().id(PATIENT_ID).doctorId(USER_ID).build();
        when(patientJpaRepository.findByIdAndDoctorId(PATIENT_ID, USER_ID)).thenReturn(Optional.of(expectedPatient));

        // when
        Patient actualPatient = adapter.getPatientByIdAndDoctorId(PATIENT_ID, USER_ID);

        // then
        verify(patientJpaRepository).findByIdAndDoctorId(PATIENT_ID, USER_ID);
        assertEquals(expectedPatient, actualPatient);
    }

    @Test
    @DisplayName("getPatientByIdAndDoctorId should throw NoSuchElementException if patient not found")
    void testGetPatientByIdAndDoctorIdWhenNotFound() {
        // when
        when(patientJpaRepository.findByIdAndDoctorId(PATIENT_ID, USER_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(NoSuchElementException.class, () -> adapter.getPatientByIdAndDoctorId(PATIENT_ID, USER_ID));
        verify(patientJpaRepository).findByIdAndDoctorId(PATIENT_ID, USER_ID);
    }

    @Test
    @DisplayName("savePatient should return saved patient")
    void testSavePatient() {
        // given
        Patient patient = Patient.builder().id(PATIENT_ID).doctorId(USER_ID).build();
        when(patientJpaRepository.save(patient)).thenReturn(patient);

        // when
        Patient savedPatient = adapter.savePatient(patient);

        // then
        verify(patientJpaRepository).save(patient);
        assertEquals(patient, savedPatient);
    }

    @Test
    @DisplayName("getVisits should return active visits for doctor")
    void testGetVisits() {
        // given
        List<Visit> expectedVisits = List.of(
                Visit.builder().id(VISIT1_ID).doctorId(USER_ID).build(),
                Visit.builder().id(VISIT2_ID).doctorId(USER_ID).build()
        );
        when(visitJpaRepository.getActive(USER_ID)).thenReturn(expectedVisits);

        // when
        List<Visit> actualVisits = adapter.getVisits(USER_ID);

        // then
        verify(visitJpaRepository).getActive(USER_ID);
        assertEquals(expectedVisits, actualVisits);
    }

    @Test
    @DisplayName("getVisitByIdAndDoctorId should return visit if found in repository")
    void testGetVisitByIdAndDoctorIdWhenFound() {
        // given
        Visit expectedVisit = Visit.builder().id(VISIT1_ID).doctorId(USER_ID).build();
        when(visitJpaRepository.findByIdAndDoctorId(VISIT1_ID, USER_ID)).thenReturn(Optional.of(expectedVisit));

        // when
        Visit actualVisit = adapter.getVisitByIdAndDoctorId(VISIT1_ID, USER_ID);

        // then
        verify(visitJpaRepository).findByIdAndDoctorId(VISIT1_ID, USER_ID);
        assertEquals(expectedVisit, actualVisit);
    }

    @Test
    @DisplayName("getVisitByIdAndDoctorId should throw NoSuchElementException if visit not found")
    void testGetVisitByIdAndDoctorIdWhenNotFound() {
        // when
        when(visitJpaRepository.findByIdAndDoctorId(VISIT1_ID, USER_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(NoSuchElementException.class, () -> adapter.getVisitByIdAndDoctorId(VISIT1_ID, USER_ID));
        verify(visitJpaRepository).findByIdAndDoctorId(VISIT1_ID, USER_ID);
    }

    @Test
    @DisplayName("getVisitsOfPatient should return active visits for patient")
    void testGetVisitsOfPatient() {
        // given
        List<Visit> expectedVisits = List.of(
                Visit.builder().id(VISIT1_ID).doctorId(USER_ID).patient(Patient.builder().id(PATIENT_ID).doctorId(USER_ID).build()).build(),
                Visit.builder().id(VISIT2_ID).doctorId(USER_ID).patient(Patient.builder().id(PATIENT_ID).doctorId(USER_ID).build()).build()
        );
        when(visitJpaRepository.getActiveForPatient(USER_ID, PATIENT_ID)).thenReturn(expectedVisits);

        // when
        List<Visit> actualVisits = adapter.getVisitsOfPatient(USER_ID, PATIENT_ID);

        // then
        verify(visitJpaRepository).getActiveForPatient(USER_ID, PATIENT_ID);
        assertEquals(expectedVisits, actualVisits);
    }

    @Test
    @DisplayName("saveVisit should return saved visit")
    void testSaveVisit() {
        // given
        Visit visit = Visit.builder()
                .id(VISIT1_ID)
                .doctorId(USER_ID)
                .patient(Patient.builder().id(PATIENT_ID).doctorId(USER_ID).build())
                .date(LocalDate.now()).build();
        when(visitJpaRepository.save(visit)).thenReturn(visit);

        // when
        Visit savedVisit = adapter.saveVisit(visit);

        // then
        verify(visitJpaRepository).save(visit);
        assertEquals(visit, savedVisit);
    }

    @Test
    @DisplayName("getActualReminders should return actual reminders for doctor")
    void testGetActualReminders() {
        // given
        List<Reminder> expectedReminders = List.of(
                Reminder.builder().id(REMINDER1_ID).doctorId(USER_ID).build(),
                Reminder.builder().id(REMINDER2_ID).doctorId(USER_ID).build()
        );
        when(reminderJpaRepository.getActual(USER_ID)).thenReturn(expectedReminders);

        // when
        List<Reminder> actualReminders = adapter.getActualReminders(USER_ID);

        // then
        verify(reminderJpaRepository).getActual(USER_ID);
        assertEquals(expectedReminders, actualReminders);
    }

    @Test
    @DisplayName("getRemindersOfPatient should return reminders for patient")
    void testGetRemindersOfPatient() {
        // given
        List<Reminder> expectedReminders = List.of(
                Reminder.builder().id(REMINDER1_ID).doctorId(USER_ID).patient(Patient.builder().id(PATIENT_ID).doctorId(USER_ID).build()).build(),
                Reminder.builder().id(REMINDER2_ID).doctorId(USER_ID).patient(Patient.builder().id(PATIENT_ID).doctorId(USER_ID).build()).build()
        );
        when(reminderJpaRepository.getAllForPatient(USER_ID, PATIENT_ID)).thenReturn(expectedReminders);

        // when
        List<Reminder> actualReminders = adapter.getRemindersOfPatient(USER_ID, PATIENT_ID);

        // then
        verify(reminderJpaRepository).getAllForPatient(USER_ID, PATIENT_ID);
        assertEquals(expectedReminders, actualReminders);
    }

    @Test
    @DisplayName("getReminderByIdAndDoctorId should return reminder if found in repository")
    void testGetReminderByIdAndDoctorIdWhenFound() {
        // given
        Reminder expectedReminder = Reminder.builder().id(REMINDER1_ID).doctorId(USER_ID).build();
        when(reminderJpaRepository.findByIdAndDoctorId(REMINDER1_ID, USER_ID)).thenReturn(Optional.of(expectedReminder));

        // when
        Reminder actualReminder = adapter.getReminderByIdAndDoctorId(REMINDER1_ID, USER_ID);

        // then
        verify(reminderJpaRepository).findByIdAndDoctorId(REMINDER1_ID, USER_ID);
        assertEquals(expectedReminder, actualReminder);
    }

    @Test
    @DisplayName("getReminderByIdAndDoctorId should throw NoSuchElementException if reminder not found")
    void testGetReminderByIdAndDoctorIdWhenNotFound() {
        // when
        when(reminderJpaRepository.findByIdAndDoctorId(REMINDER1_ID, USER_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(NoSuchElementException.class, () -> adapter.getReminderByIdAndDoctorId(REMINDER1_ID, USER_ID));
        verify(reminderJpaRepository).findByIdAndDoctorId(REMINDER1_ID, USER_ID);
    }

    @Test
    @DisplayName("saveReminder should return saved reminder")
    void testSaveReminder() {
        // given
        Reminder reminder = Reminder.builder().id(REMINDER1_ID).doctorId(USER_ID).text("Take your medication").build();
        when(reminderJpaRepository.save(reminder)).thenReturn(reminder);

        // when
        Reminder savedReminder = adapter.saveReminder(reminder);

        // then
        verify(reminderJpaRepository).save(reminder);
        assertEquals(reminder, savedReminder);
    }

}
