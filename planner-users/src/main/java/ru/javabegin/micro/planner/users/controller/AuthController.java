package ru.javabegin.micro.planner.users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import ru.javabegin.micro.planner.users.dto.UserRegister;
import ru.javabegin.micro.planner.users.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody UserRegister userRegister) {
        System.out.println("я в контроллере register");
        authService.registration(userRegister);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/redirect")
    public ResponseEntity<Map<String, String>> handleRedirect(
            @RequestParam("code") String code) {
        System.out.println("я в контроллере redirect");

        WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build();

        Map<String, String> tokens = webClient.post()
                .uri("/realms/todoapp-realm/protocol/openid-connect/token")
                .bodyValue(getTokenRequestBody(code))
                .retrieve()
                .bodyToMono(Map.class)
                .block(); // Блокируем для получения синхронного ответа

        System.out.println(tokens.get("access_token"));

        if (tokens != null) {
            // Отправляем токены на фронтенд
            return ResponseEntity.ok(tokens);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private MultiValueMap<String, String> getTokenRequestBody(String code) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", "todoapp-client"); // Заменить на свой client_id
        map.add("client_secret", "UTrqFVsKShdSC9ClXxNEVaXsJYJav47V"); // Секретный ключ клиента
        map.add("code", code); // Код из редиректа
        map.add("redirect_uri", "http://localhost:8765/planner-users/auth/redirect"); // Должен совпадать с тем, что в Keycloak
        return map;
    }

}
