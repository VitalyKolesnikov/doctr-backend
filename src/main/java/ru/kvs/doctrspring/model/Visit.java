package ru.kvs.doctrspring.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "visits")
@Data
public class Visit extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    @JsonIgnore
    private User doctor;

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

    public int getShare() {
        return (int) Math.round(cost / 100.0 * percent);
    }

}
