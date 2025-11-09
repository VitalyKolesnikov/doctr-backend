package ru.kvs.doctrspring.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.kvs.doctrspring.domain.ids.ReminderId;
import ru.kvs.doctrspring.domain.ids.UserId;

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

    @Column(name = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate date;

    @Column(name = "text")
    private String text;

    public void createNew(UserId doctorId, Patient patient) {
        this.id = ReminderId.newId();
        this.doctorId = doctorId;
        this.patient = patient;
    }

    public void update(Reminder reminder) {
        this.date = reminder.getDate();
        this.text = reminder.getText();
    }

    public void complete() {
        this.status = NOT_ACTIVE;
    }

}
