package ru.kvs.doctrspring.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.kvs.doctrspring.domain.ids.PatientId;
import ru.kvs.doctrspring.domain.ids.ReminderId;
import ru.kvs.doctrspring.domain.ids.UserId;

import javax.persistence.*;
import java.time.LocalDate;

import static ru.kvs.doctrspring.domain.Status.NOT_ACTIVE;

@Entity
@Table(name = "reminders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Reminder extends BaseEntity {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private ReminderId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "doctor_id"))
    private UserId doctorId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Column(name = "date", columnDefinition = "DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate date;

    @Column(name = "text")
    private String text;

    public void create(UserId doctorId, Patient patient) {
        this.doctorId = doctorId;
        this.patient = patient;
        this.onCreate();
    }

    public void update(Reminder reminder) {
        this.date = reminder.getDate();
        this.text = reminder.getText();
        this.onUpdate();
    }

    public void complete() {
        this.status = NOT_ACTIVE;
        this.onUpdate();
    }

    @Override
    protected void generateId() {
        this.id = ReminderId.newId();
    }

}
