package ru.javabegin.micro.planner.entity;


import jakarta.persistence.*;
import lombok.*;

/*

общая статистика по задачам (незвисимо от категорий задач)

 */
//@Table(name = "stat", schema = "todo", catalog = "planner_todo")

@Entity
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stat")
public class Stat { // в этой таблице всего 1 запись, которая обновляется (но никогда не удаляется)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "completed_total", updatable = false)
    private Long completedTotal; // значение задается в триггере в БД

    @Column(name = "uncompleted_total", updatable = false)
    private Long uncompletedTotal; // значение задается в триггере в БД

    @Column(name = "user_id")
    private String userId;

}
