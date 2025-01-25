package ru.javabegin.micro.planner.users.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserDTO {

    private String id;
    @NotBlank
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private String password;

}
