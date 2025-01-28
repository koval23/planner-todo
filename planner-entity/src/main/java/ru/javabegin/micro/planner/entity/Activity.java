package ru.javabegin.micro.planner.entity;

/*

Вся активность пользователя (активация аккаунта, другие
действия по необходимости)

*/
//
//@Entity
//@Setter
//@Getter
//@NoArgsConstructor
//@EqualsAndHashCode
//@AllArgsConstructor
//@Table(name = "activity")
//public class Activity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private UUID id;
//
//    @Column(name = "is_active")
//    // для автоматической конвертации числа в true/false
//    private Boolean activated; // становится true только после подтверждения активации пользователем (обратно false уже стать не может по логике)
//
//    @Column(updatable = false)
//    private String uuid; // создается только один раз с помощью триггера в БД
//
//    @Column(name = "user_id")
//    private String userId;
//
//}
