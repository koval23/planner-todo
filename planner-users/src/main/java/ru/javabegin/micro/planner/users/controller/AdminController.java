package ru.javabegin.micro.planner.users.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.micro.planner.users.dto.UserDTO;
import ru.javabegin.micro.planner.users.dto.UserRegister;
import ru.javabegin.micro.planner.users.service.AdminService;

import java.util.List;


/*

Чтобы дать меньше шансов для взлома (например, CSRF атак): POST/PUT запросы могут изменять/фильтровать закрытые данные, а GET запросы - для получения незащищенных данных
Т.е. GET-запросы не должны использоваться для изменения/получения секретных данных

Если возникнет exception - вернется код  500 Internal Server Error, поэтому не нужно все действия оборачивать в try-catch

Используем @RestController вместо обычного @Controller, чтобы все ответы сразу оборачивались в JSON,
иначе пришлось бы добавлять лишние объекты в код, использовать @ResponseBody для ответа, указывать тип отправки JSON

Названия методов могут быть любыми, главное не дублировать их имена и URL mapping

*/

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/user") // базовый URI
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/add")
    public ResponseEntity<Void> add(@RequestBody UserRegister userRegister) {
        adminService.addUser(userRegister);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //    todo Передать id и обект для изменения
    @PostMapping("/update")
    public ResponseEntity<Void> update(@Valid @RequestBody UserDTO userDTO) {
        adminService.updateUser(userDTO);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/id")
    public ResponseEntity<Void> deleteByUserId(@RequestParam @NotBlank String userId) {
        adminService.deleteUserById(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/email")
    public ResponseEntity<Void> deleteByUserEmail(@RequestParam @NotBlank String email) {
        adminService.deleteUserByEmail(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/id")
    public ResponseEntity<UserRepresentation> findById(@RequestParam @NotBlank String id) {

        return ResponseEntity.ok(adminService.findUserById(id));
    }

    @GetMapping("/email")
    public ResponseEntity<UserRepresentation> findByEmail(@RequestParam @NotBlank String email) {

        return ResponseEntity.ok(adminService.findUserByEmail(email));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserRepresentation>> search(@RequestParam @NotBlank String text) {

        return ResponseEntity.ok(adminService.searchAllByAnyText(text));
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<UserRepresentation>> allUsers() {

        return ResponseEntity.ok(adminService.getAllUsers());
    }
}
