package ru.javabegin.micro.planner.todo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.javabegin.micro.planner.entity.Task;
import ru.javabegin.micro.planner.todo.dto.task.TaskCreated;
import ru.javabegin.micro.planner.todo.dto.task.TaskResponse;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = {PriorityMapper.class, CategoryMapper.class})
public interface TaskMapper {

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "priority", source = "priorityId", qualifiedByName = "mapPriority")
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "mapCategory")
    Task toEntity(TaskCreated taskCreated, String userId);

    @Mapping(target = "priorityDTO", source = "priority")
    @Mapping(target = "categoryDTO", source = "category")
    @Mapping(target = "id", source = "id", qualifiedByName = "uuidToString")
    TaskResponse toResponse(Task task);

    @Named("uuidToString")
    default String uuidToString(UUID id) {
        return id != null ? id.toString() : null;
    }

    @Named("stringToUUID")
    default UUID stringToUUID(String id) {
        return id != null ? UUID.fromString(id) : null;
    }

}
