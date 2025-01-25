package ru.javabegin.micro.planner.users.keycloak;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeycloakTokenManager {
    // Настройки из файла properties
    @Value("${keycloak.auth-server-url}")
    private String serverURL;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientID;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    private final RestTemplateBuilder restTemplateBuilder;

    public synchronized void ensureValidAccessToken(Keycloak keycloak) {
        AccessTokenResponse token = keycloak.tokenManager().getAccessToken();
        if (token == null) {
            System.out.println("Keycloak token is null");
            return;
        }
        if (isAccessTokenExpired(token)) {
            updateAccessToken(token);
        }
    }

    private boolean isAccessTokenExpired(AccessTokenResponse token) {
        long expiresIn = token.getExpiresIn(); // Время жизни токена в секундах
        long safetyMargin = 10;
        return expiresIn <= safetyMargin; // Если меньше safetyMargin, считаем, что токен истёк
    }

    private synchronized void updateAccessToken(AccessTokenResponse token) {
        String newAccessToken = requestNewAccessToken(); // Запрашиваем новый токен
        if (newAccessToken != null) {
            token.setToken(newAccessToken); // Обновляем токен
            System.out.println("Токен успешно обновлён");
        } else {
            throw new IllegalStateException("Не удалось получить новый токен");
        }
    }

    private String requestNewAccessToken() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");
        map.add("client_id", clientID);
        map.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                serverURL + "/realms/" + realm + "/protocol/openid-connect/token",
                HttpMethod.POST,
                entity,
                Map.class
        );

        return (String) response.getBody().get("access_token");
    }

}
