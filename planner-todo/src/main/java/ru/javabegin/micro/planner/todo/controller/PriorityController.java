package ru.javabegin.micro.planner.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.micro.planner.entity.Priority;
import ru.javabegin.micro.planner.todo.dto.priority.PriorityDTO;
import ru.javabegin.micro.planner.todo.search.PrioritySearchValues;
import ru.javabegin.micro.planner.todo.service.PriorityService;
import ru.javabegin.micro.planner.utils.rest.resttemplate.UserRestBuilder;

import java.util.List;
import java.util.NoSuchElementException;


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

    // доступ к данным из БД
    private final PriorityService priorityService;

    // микросервисы для работы с пользователями
    private final UserRestBuilder userRestBuilder;

    @PostMapping("/all")
    public List<Priority> findAll(@RequestBody String userId) {
        return priorityService.findAll(userId);
    }

    @PostMapping("/add")
    public ResponseEntity<PriorityDTO> add(@RequestBody PriorityDTO priorityRequest,
                                           @AuthenticationPrincipal Jwt jwt) {

        return new ResponseEntity<>(priorityService.add(priorityRequest, jwt.getId()), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<PriorityDTO> update(@RequestBody PriorityDTO priorityDTO) {

        return new ResponseEntity<>(priorityService.update(priorityDTO), HttpStatus.OK); // просто отправляем статус 200 (операция прошла успешно)

    }

    // параметр id передаются не в BODY запроса, а в самом URL
    @PostMapping("/id")
    public ResponseEntity<Priority> findById(@RequestBody String id) {

        Priority priority = null;

        // можно обойтись и без try-catch, тогда будет возвращаться полная ошибка (stacktrace)
        // здесь показан пример, как можно обрабатывать исключение и отправлять свой текст/статус
        try {
            priority = priorityService.findById(id);
        } catch (NoSuchElementException e) { // если объект не будет найден
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(priority);
    }


    // для удаления используем типа запроса put, а не delete, т.к. он позволяет передавать значение в body, а не в адресной строке
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        priorityService.deleteById(id);
        return ResponseEntity.ok().build();
    }


    // поиск по любым параметрам PrioritySearchValues
    @PostMapping("/search")
    public ResponseEntity<List<Priority>> search(@RequestBody PrioritySearchValues prioritySearchValues, @AuthenticationPrincipal Jwt jwt) {

        prioritySearchValues.setUserId(jwt.getSubject());

        // проверка на обязательные параметры
        if (prioritySearchValues.getUserId().isBlank()) {
            return new ResponseEntity("missed param: user id", HttpStatus.NOT_ACCEPTABLE);
        }

        // если вместо текста будет пусто или null - вернутся все категории
        return ResponseEntity.ok(priorityService.find(prioritySearchValues.getTitle(), prioritySearchValues.getUserId()));
    }


}
