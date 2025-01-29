package ru.javabegin.micro.planner.todo.dto.priority;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PriorityCreated {

    private String title;

    private String color;

}
