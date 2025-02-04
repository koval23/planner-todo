package ru.javabegin.micro.planner.todo.dto.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.javabegin.micro.planner.todo.dto.category.CategoryDTO;
import ru.javabegin.micro.planner.todo.dto.priority.PriorityDTO;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private String id;

    private String title;

    private Date taskDate;

    private PriorityDTO priorityDTO;

    private CategoryDTO categoryDTO;

}
