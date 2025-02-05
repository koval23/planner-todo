package ru.javabegin.micro.planner.users.service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.javabegin.micro.planner.users.dto.UserDTO;
import ru.javabegin.micro.planner.users.dto.UserRegister;
import ru.javabegin.micro.planner.users.exception.handling.CommonException;
import ru.javabegin.micro.planner.users.exception.handling.ErrorCode;
import ru.javabegin.micro.planner.users.keycloak.KeycloakUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    public static final String TOPIC_NAME = "first-topic"; // имя столбца id
    // микросервисы для работы с пользователями
    private final KeycloakUtils keycloakUtils;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void addUser(UserRegister userRegister) {

//        validUserDTORequestData(userDTO);

        Response createdResponse = keycloakUtils.createKeycloakUser(userRegister);

        if (createdResponse.getStatus() == HttpStatus.CONFLICT.value()) {
            throw CommonException.of(ErrorCode.EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT, List.of(userRegister.getEmail()));
        }

        String userId = CreatedResponseUtil.getCreatedId(createdResponse);
        System.out.printf("User created with userId: %s%n", userId);

//        отправка сообщения о создании нового пользователя
        kafkaTemplate.send(TOPIC_NAME, userId);
    }

    public void updateUser(UserDTO userDTO) {
        if (userDTO.getId() == null || userDTO.getId().isBlank()) {
            throw CommonException.of(ErrorCode.ID_MUST_NOT_BE_NULL, HttpStatus.BAD_REQUEST);
        }
        keycloakUtils.userExistsById(userDTO.getId());
        keycloakUtils.updateKeycloakUser(userDTO);
    }

    public void deleteUserById(String userId) {
        keycloakUtils.deleteKeycloakUserById(userId);
    }

    public void deleteUserByEmail(String email) {
        String userId = keycloakUtils.findUserByEmail(email).getId();
        keycloakUtils.deleteKeycloakUserById(userId);
    }

    public UserRepresentation findUserById(String userId) {
        return keycloakUtils.findUserById(userId);
    }

    public UserRepresentation findUserByEmail(String email) {
        return keycloakUtils.findUserByEmail(email);
    }

    public List<UserRepresentation> searchAllByAnyText(String text) {
        return keycloakUtils.searchKeycloakUsers(text);
    }

    private void validUserDTORequestData(UserDTO userDTO) {
        if (userDTO.getId() != null && !userDTO.getId().isBlank()) {
            throw CommonException.of(ErrorCode.ID_MUST_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
        }

        if (userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty()) {
            throw CommonException.of(ErrorCode.EMAIL_ALREADY_EXISTS, HttpStatus.NOT_ACCEPTABLE);
        }

        if (userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
            throw CommonException.of(ErrorCode.PASSWORD_IS_NOT_MATCHED, HttpStatus.NOT_ACCEPTABLE);
        }

        if (userDTO.getUsername() == null || userDTO.getUsername().trim().isEmpty()) {
            throw CommonException.of(ErrorCode.USERNAME_IS_NOT_MATCHED, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public List<UserRepresentation> getAllUsers() {
        return keycloakUtils.getAllUsers();
    }

}
