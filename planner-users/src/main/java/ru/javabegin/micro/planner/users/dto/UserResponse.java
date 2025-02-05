package ru.javabegin.micro.planner.users.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    @NotBlank
    private String id;
    @NotBlank
    private String email;
    @NotBlank
    private String username;

}
