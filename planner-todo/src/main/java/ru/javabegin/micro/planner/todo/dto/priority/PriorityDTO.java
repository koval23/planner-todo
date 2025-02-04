package ru.javabegin.micro.planner.todo.dto.priority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PriorityDTO {

    private String id;

    private String title;

    private String color;
}
