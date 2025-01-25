package ru.javabegin.micro.planner.users.keycloak;

import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.javabegin.micro.planner.users.dto.UserDTO;
import ru.javabegin.micro.planner.users.exception.handling.CommonException;
import ru.javabegin.micro.planner.users.exception.handling.ErrorCode;
import ru.javabegin.micro.planner.utils.rest.webclient.UserWebClientBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakUtils {

    // Настройки из файла properties
    @Value("${keycloak.auth-server-url}")
    private String serverURL;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientID;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;
    private final static String keycloakUrl = "http://localhost:8080";

    private final UserWebClientBuilder webClientBuilder;

    private static Keycloak keycloak; // Ссылка на единственный экземпляр Keycloak
    private static RealmResource realmResource;// доступ к API realm
    private static UsersResource usersResource;// доступ к API для работы с пользователями
    private final KeycloakTokenManager keycloakTokenManager;

    // Создание объекта Keycloak
//    todo PostConstruct ?
//    todo synchronized?
    @PostConstruct
    public synchronized Keycloak initKeycloak() {

        if (keycloak == null) { // Создаем объект только один раз
            keycloak = KeycloakBuilder.builder()
                    .realm(realm)
                    .serverUrl(serverURL)
                    .clientId(clientID)
                    .clientSecret(clientSecret)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .build();
        }

        realmResource = keycloak.realm(realm);
        usersResource = realmResource.users();

        return keycloak;
    }


    public Response createKeycloakUser(UserDTO user) {
        keycloakTokenManager.ensureValidAccessToken(keycloak);
        realmResource.users();
        // Создаем объект учетных данных для пароля
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(user.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(user.getUsername());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setEmail(user.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);

        Response response = usersResource.create(kcUser);

        return response;

    }

    public void updateKeycloakUser(UserDTO userDTO) {

        // данные пароля - специальный объект-контейнер CredentialRepresentation
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(userDTO.getPassword());

        // какие поля обновляем
        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(userDTO.getUsername());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setEmail(userDTO.getEmail());

        // получаем пользователя
        UserResource uniqueUserResource = usersResource.get(userDTO.getId());
        uniqueUserResource.update(kcUser); // обновление

    }

    public void deleteKeycloakUserById(String userId) {
        userExistsById(userId);
        // получаем пользователя
        UserResource uniqueUserResource = usersResource.get(userId);
        uniqueUserResource.remove();

    }

    public List<UserRepresentation> getAllUsers() {
        return usersResource.list();
    }

    // поиск пользователя по любым атрибутам (вхождение текста)
    public List<UserRepresentation> searchKeycloakUsers(String text) {
        // получаем пользователя
        return usersResource.searchByAttributes(text);

    }

    public UserRepresentation findUserById(String userId) {
        userExistsById(userId);
        return usersResource.get(userId).toRepresentation();
    }

    public UserRepresentation findUserByEmail(String email) {
        try {
            List<UserRepresentation> users = usersResource.search(null, null, null, email, 0, 1);

            if (!users.isEmpty()) {
                return users.get(0);
            } else {
                throw CommonException.of(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        } catch (NotFoundException e) {
            throw CommonException.of(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw CommonException.of(ErrorCode.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, List.of(e.getMessage()));
        }
    }

    public void userExistsById(String userId) {
        try {
            usersResource.get(userId).toRepresentation();
        } catch (NotFoundException e) {
            throw CommonException.of(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new CommonException(ErrorCode.INTERNAL_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    List.of(e.getMessage()));
        }
    }

    // Метод для создания пользователя в Keycloak
    public void addRoles(String userId, List<String> roles) {
        List<RoleRepresentation> kcRoles = new ArrayList<>();
        for (String role : roles) {
            RoleRepresentation roleRep = realmResource
                    .roles()
                    .get(role)
                    .toRepresentation();
            kcRoles.add(roleRep);
        }

        UserResource uniqueUserResource = usersResource.get(userId);

        uniqueUserResource.roles().realmLevel().add(kcRoles);
    }

    // Метод для создания данных о пароле
    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false); // Указывает, что пароль постоянный, не требует смены
        passwordCredentials.setType(CredentialRepresentation.PASSWORD); // Тип учетных данных - пароль
        passwordCredentials.setValue(password); // Устанавливаем значение пароля
        return passwordCredentials; // Возвращаем созданный объект
    }

}
