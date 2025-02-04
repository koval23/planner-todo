package ru.javabegin.micro.planner.todo.dto.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreated {

    private String title;

    private Date taskDate;

    private String priorityId;

    private String categoryId;

}
