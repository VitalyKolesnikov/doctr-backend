package ru.kvs.doctrspring.adapters.restapi.mapper;

import org.mapstruct.Mapper;
import ru.kvs.doctrspring.adapters.restapi.dto.request.ReminderCreateOrUpdateRequest;
import ru.kvs.doctrspring.adapters.restapi.dto.response.ReminderDto;
import ru.kvs.doctrspring.domain.Reminder;

import java.util.List;

@Mapper(componentModel = "spring", uses = PatientMapper.class)
public interface ReminderMapper {

    ReminderDto toReminderDto(Reminder reminder);

    List<ReminderDto> toReminderDtos(List<Reminder> reminders);

    Reminder toReminder(ReminderCreateOrUpdateRequest reminderCreateOrUpdateRequest);

}
