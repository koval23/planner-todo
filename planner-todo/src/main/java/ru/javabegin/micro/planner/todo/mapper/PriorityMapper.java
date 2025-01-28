package ru.javabegin.micro.planner.todo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.javabegin.micro.planner.entity.Priority;
import ru.javabegin.micro.planner.todo.dto.priority.PriorityDTO;

@Mapper
public interface PriorityMapper {

    Priority priorityDTOToPriority(PriorityDTO priorityDTO);

    @Mapping(target = "userId", ignore = true)
    PriorityDTO priorityToPriorityDTO(Priority priority);
}
