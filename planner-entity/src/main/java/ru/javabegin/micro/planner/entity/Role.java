package ru.javabegin.micro.planner.entity;

/*

Все доступные роли, которые будут привязаны к пользователю

*/
//
//@Entity
//@Setter
//@Getter
//@EqualsAndHashCode
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "role_data")
//public class Role {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private UUID id;
//
//    private String name; // название роли
//
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "user_role",
//            joinColumns = @JoinColumn(name = "role_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id"))
//    private Set<User> users;
//
//}
