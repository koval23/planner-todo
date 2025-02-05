package ru.javabegin.micro.planner.users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javabegin.micro.planner.users.dto.UserRegister;
import ru.javabegin.micro.planner.users.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody UserRegister userRegister) {
        System.out.println("я в контроллере");
        authService.registration(userRegister);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
