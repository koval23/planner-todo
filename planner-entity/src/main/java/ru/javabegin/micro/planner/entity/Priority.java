package ru.javabegin.micro.planner.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

/*

справочноное значение - приоритет пользователя
может использовать для своих задач

 */

@Entity
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "priority")
public class Priority implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    private String color;

    @Column(name = "user_id")
    private String userId;

}
