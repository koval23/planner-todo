package ru.javabegin.micro.planner.entity;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;


/*

справочноное значение - категория пользователя
может использовать для своих задач
содержит статистику по каждой категории

 */
//@Table(name = "category", schema = "todo", catalog = "planner_todo")
@Entity
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class Category implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String title;
    // т.к. это поле высчитывается автоматически в триггерах - вручную его не обновляем (updatable = false)
    @Column(name = "completed_count", updatable = false)
    private Long completedCount;
    // т.к. это поле высчитывается автоматически в триггерах - вручную его не обновляем (updatable = false)
    @Column(name = "uncompleted_count", updatable = false)
    private Long uncompletedCount;

    @Column(name="user_id")
    private String userId;

}
