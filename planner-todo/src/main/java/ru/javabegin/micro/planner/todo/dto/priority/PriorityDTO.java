package ru.javabegin.micro.planner.todo.dto.priority;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PriorityDTO {

    private String id;

    private String title;

    private String color;

    private String userId;

}
