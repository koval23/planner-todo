package ru.javabegin.micro.planner.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.javabegin.micro.planner.users.dto.UserRegister;
import ru.javabegin.micro.planner.users.keycloak.KeycloakUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    public final AdminService adminService;
    public final KeycloakUtils keycloakUtils;

    @Value("${keycloak.client.redirect}")
    private String redirectUri;
    @Value("${keycloak.get.token.url}")
    private String keycloakTokenUrl;
    @Value("${keycloak.client}")
    private String clientID;
    @Value("${keycloak.client.secret}")
    private String clientSecret;


    // Настройки из файла properties
    public String getToken(String code) {

        // 3. Запрашиваем Access Token с использованием кода авторизации
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("grant_type", "authorization_code");
        requestBody.put("client_id", clientID);
        requestBody.put("client_secret", clientSecret);
        requestBody.put("code", code);
        requestBody.put("redirect_uri", redirectUri);

        ResponseEntity<Map> response = restTemplate.postForEntity(keycloakTokenUrl, requestBody, Map.class);

        // 4. Извлекаем Access Token из ответа
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return (String) response.getBody().get("access_token");
        } else {
            throw new RuntimeException("Не удалось получить токен. Ответ: " + response);
        }
    }


    public void registration(UserRegister userRegister) {
        adminService.addUser(userRegister);
    }


}
