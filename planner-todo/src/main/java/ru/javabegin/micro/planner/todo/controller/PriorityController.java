package ru.javabegin.micro.planner.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.micro.planner.todo.dto.priority.PriorityCreated;
import ru.javabegin.micro.planner.todo.dto.priority.PriorityDTO;
import ru.javabegin.micro.planner.todo.service.PriorityService;

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
@RequestMapping("/priority") // базовый URI
public class PriorityController {

    private final PriorityService priorityService;

    @PostMapping("/add")
    public ResponseEntity<PriorityDTO> add(@RequestBody PriorityCreated priorityCreated,
                                           @AuthenticationPrincipal Jwt jwt) {
//         jwt.getId() - уточнить действительно ли ето userId
        return new ResponseEntity<>(priorityService.add(priorityCreated, jwt.getId()), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        priorityService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/all/")
    public List<PriorityDTO> findAll() {
        return priorityService.findAll();
    }

    @PostMapping("/all/forUsers")
    public List<PriorityDTO> findAllPriorityByUser(@RequestBody String userId) {
        return priorityService.findAllPriorityByUser(userId);
    }

    @PostMapping("/id")
    public ResponseEntity<PriorityDTO> findById(@RequestBody String id) {
        return ResponseEntity.ok(priorityService.findById(id));
    }

//    @PutMapping("/update")
//    public ResponseEntity<PriorityCreated> update(@RequestBody PriorityCreated priorityCreated) {
//
//        return new ResponseEntity<>(priorityService.update(priorityCreated), HttpStatus.OK); // просто отправляем статус 200 (операция прошла успешно)
//
//    }
//    // поиск по любым параметрам PrioritySearchValues
//    @PostMapping("/search")
//    public ResponseEntity<List<Priority>> search(@RequestBody PrioritySearchValues prioritySearchValues, @AuthenticationPrincipal Jwt jwt) {
//
//        prioritySearchValues.setUserId(jwt.getSubject());
//
//        // проверка на обязательные параметры
//        if (prioritySearchValues.getUserId().isBlank()) {
//            return new ResponseEntity("missed param: user id", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        // если вместо текста будет пусто или null - вернутся все категории
//        return ResponseEntity.ok(priorityService.find(prioritySearchValues.getTitle(), prioritySearchValues.getUserId()));
//    }


}
