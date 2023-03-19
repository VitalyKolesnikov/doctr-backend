package ru.kvs.doctrspring.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.kvs.doctrspring.domain.ids.UserId;
import ru.kvs.doctrspring.domain.ids.VisitId;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "visits")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Visit extends BaseEntity {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private VisitId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "doctor_id"))
    private UserId doctorId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @Column(name = "visit_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate date;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "percent")
    private Integer percent;

    @Column(name = "child")
    private Boolean child;

    @Column(name = "first")
    private Boolean first;

    @Column(name = "info")
    private String info;

    @JsonProperty("share")
    public int getShare() {
        return (int) Math.round(cost / 100.0 * percent);
    }

    public void create(UserId doctorId, Patient patient, Clinic clinic) {
        this.doctorId = doctorId;
        this.patient = patient;
        this.clinic = clinic;
        this.onCreate();
    }

    public void update(Visit visit) {
        this.date = visit.getDate();
        this.cost = visit.getCost();
        this.percent = visit.getPercent();
        this.child = visit.getChild();
        this.first = visit.getFirst();
        this.info = visit.getInfo();
        this.onUpdate();
    }

    @Override
    protected void generateId() {
        this.id = VisitId.newId();
    }

}
