package ru.javabegin.micro.planner.todo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.javabegin.micro.planner.entity.Priority;
import ru.javabegin.micro.planner.todo.dto.priority.PriorityCreated;
import ru.javabegin.micro.planner.todo.dto.priority.PriorityDTO;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface PriorityMapper {

    @Mapping(target = "id", source = "id", qualifiedByName = "uuidToString")
    PriorityDTO toDTO(Priority priority);

    @Mapping(target = "userId", source = "userId")
    Priority toEntity(PriorityCreated priorityCreated, String userId);

    @Named("uuidToString")
    default String uuidToString(UUID id) {
        return id != null ? id.toString() : null;
    }

    @Named("stringToUUID")
    default UUID stringToUUID(String id) {
        return id != null ? UUID.fromString(id) : null;
    }

}
