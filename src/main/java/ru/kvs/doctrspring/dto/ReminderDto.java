package ru.kvs.doctrspring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.kvs.doctrspring.model.Reminder;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReminderDto extends BaseDto {
    private Long patientId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate date;
    private String text;

    public Reminder toReminder() {
        Reminder reminder = new Reminder();
        reminder.setDate(getDate());
        reminder.setText(getText());
        return reminder;
    }
}
