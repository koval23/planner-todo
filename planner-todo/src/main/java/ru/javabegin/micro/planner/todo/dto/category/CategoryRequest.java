package ru.javabegin.micro.planner.todo.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {

    private String title;

    private Long completedCount;

    private Long uncompletedCount;

    private String userId;

}
