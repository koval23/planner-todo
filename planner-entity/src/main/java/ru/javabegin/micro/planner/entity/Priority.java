package ru.javabegin.micro.planner.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/*

справочноное значение - приоритет пользователя
может использовать для своих задач

 */
//@Table(name = "priority", schema = "todo", catalog = "planner_todo")
@Entity
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "priority")
public class Priority implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String title;
    private String color;

    @Column(name = "user_id")
    private String userId;

}
