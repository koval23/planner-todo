package ru.javabegin.micro.planner.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javabegin.micro.planner.users.dto.UserRegister;
import ru.javabegin.micro.planner.users.keycloak.KeycloakUtils;

@Service
@RequiredArgsConstructor
public class AuthService {

    public final AdminService adminService;
    public final KeycloakUtils keycloakUtils;

    private final String REGISTR_URL = "http://localhost:8080/realms/todoapp-realm/protocol/openid-connect/auth?response_type=code&client_id=todoapp-client&state=sidyuf8s67dfisdgf&scope=openid phone&redirect_uri=https://localhost:8080/redirect";

    public void registration(UserRegister userRegister) {
        adminService.addUser(userRegister);
    }


}
