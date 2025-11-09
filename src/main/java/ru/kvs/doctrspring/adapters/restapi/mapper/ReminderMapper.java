package ru.kvs.doctrspring.adapters.restapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.kvs.doctrspring.adapters.restapi.dto.request.ReminderCreateOrUpdateRequest;
import ru.kvs.doctrspring.adapters.restapi.dto.response.ReminderDto;
import ru.kvs.doctrspring.domain.Reminder;
import ru.kvs.doctrspring.domain.ids.ReminderId;

import java.util.List;

@Mapper(componentModel = "spring", uses = PatientMapper.class)
public interface ReminderMapper {

    ReminderDto toReminderDto(Reminder reminder);

    List<ReminderDto> toReminderDtos(List<Reminder> reminders);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctorId", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    Reminder toReminder(ReminderCreateOrUpdateRequest reminderCreateOrUpdateRequest);

    default String map(ReminderId id) {
        return id.asString();
    }

}
