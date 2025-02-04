package ru.javabegin.micro.planner.todo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.javabegin.micro.planner.entity.Task;
import ru.javabegin.micro.planner.todo.dto.task.TaskCreated;
import ru.javabegin.micro.planner.todo.dto.task.TaskResponse;
import ru.javabegin.micro.planner.todo.service.CategoryService;
import ru.javabegin.micro.planner.todo.service.PriorityService;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = {PriorityMapper.class, CategoryMapper.class, PriorityService.class, CategoryService.class})
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "completed", constant = "false")
    @Mapping(target = "priority", source = "taskCreated.priorityId", qualifiedByName = "mapPriority")
    @Mapping(target = "category", source = "taskCreated.categoryId", qualifiedByName = "mapCategory")
    Task toEntity(TaskCreated taskCreated, String userId);

    @Mapping(target = "priorityDTO", source = "priority")
    @Mapping(target = "categoryDTO", source = "category")
    @Mapping(target = "id", source = "id", qualifiedByName = "taskUUIDToString")
    TaskResponse toResponse(Task task);

    @Named("taskUUIDToString")
    default String uuidToString(UUID id) {
        return id != null ? id.toString() : null;
    }

    @Named("taskStringToUUID")
    default UUID stringToUUID(String id) {
        return id != null ? UUID.fromString(id) : null;
    }

}
