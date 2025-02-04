package ru.javabegin.micro.planner.todo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.javabegin.micro.planner.entity.Priority;
import ru.javabegin.micro.planner.todo.dto.priority.PriorityCreated;
import ru.javabegin.micro.planner.todo.dto.priority.PriorityDTO;
import ru.javabegin.micro.planner.todo.service.CategoryService;
import ru.javabegin.micro.planner.todo.service.PriorityService;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = {PriorityService.class, CategoryService.class})
public interface PriorityMapper {

    @Mapping(target = "id", source = "id", qualifiedByName = "priorityUUIDToString")
    PriorityDTO toDTO(Priority priority);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", source = "userId")
    Priority toEntity(PriorityCreated priorityCreated, String userId);

    @Named("priorityUUIDToString")
    default String uuidToString(UUID id) {
        return id != null ? id.toString() : null;
    }

    @Named("priorityStringToUUID")
    default UUID stringToUUID(String id) {
        return id != null ? UUID.fromString(id) : null;
    }

}
