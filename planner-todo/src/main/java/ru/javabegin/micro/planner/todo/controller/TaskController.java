package ru.javabegin.micro.planner.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.micro.planner.entity.Task;
import ru.javabegin.micro.planner.todo.dto.task.TaskCreated;
import ru.javabegin.micro.planner.todo.dto.task.TaskResponse;
import ru.javabegin.micro.planner.todo.service.TaskService;
import ru.javabegin.micro.planner.utils.rest.resttemplate.UserRestBuilder;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    // микросервисы для работы с пользователями
    private UserRestBuilder userRestBuilder;

    @PostMapping("/add")
    public ResponseEntity<TaskResponse> add(@RequestBody TaskCreated taskCreated,
                                            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity(taskService.add(taskCreated, jwt.getId()), HttpStatus.NOT_ACCEPTABLE);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        taskService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // получение всех данных
    @PostMapping("/all")
    public ResponseEntity<List<Task>> findAll(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(taskService.findAll(jwt.getSubject()));
    }


    // получение объекта по id
    @PostMapping("/id")
    public ResponseEntity<TaskResponse> findById(@RequestBody String id) {

        return ResponseEntity.ok(taskService.findById(id));
    }

//    // обновление
//    @PutMapping("/update")
//    public ResponseEntity<Task> update(@RequestBody Task task) {
//
//        // проверка на обязательные параметры
//        if (task.getId() == null || task.getId() == 0) {
//            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        // если передали пустое значение
//        if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
//            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//
//        // save работает как на добавление, так и на обновление
//        taskService.update(task);
//
//        return new ResponseEntity(HttpStatus.OK); // просто отправляем статус 200 (операция прошла успешно)
//
//    }
//    // поиск по любым параметрам TaskSearchValues
//    @PostMapping("/search")
//    public ResponseEntity<Page<Task>> search(@RequestBody TaskSearchValues taskSearchValues,
//                                             @AuthenticationPrincipal Jwt jwt) throws ParseException {
//
//        taskSearchValues.setUserId(jwt.getSubject());
//
//        // все заполненные условия проверяются одновременно (т.е. И, а не ИЛИ)
//        // это можно изменять в запросе репозитория
//
//        // можно передавать не полный title, а любой текст для поиска
//        String title = taskSearchValues.getTitle() != null ? taskSearchValues.getTitle() : null;
//
//        // конвертируем Boolean в Integer
//        Boolean completed = taskSearchValues.getCompleted() != null && taskSearchValues.getCompleted() == 1 ? true : false;
//
//        Long priorityId = taskSearchValues.getPriorityId() != null ? taskSearchValues.getPriorityId() : null;
//        Long categoryId = taskSearchValues.getCategoryId() != null ? taskSearchValues.getCategoryId() : null;
//
//        String sortColumn = taskSearchValues.getSortColumn() != null ? taskSearchValues.getSortColumn() : null;
//        String sortDirection = taskSearchValues.getSortDirection() != null ? taskSearchValues.getSortDirection() : null;
//
//        Integer pageNumber = taskSearchValues.getPageNumber() != null ? taskSearchValues.getPageNumber() : null;
//        Integer pageSize = taskSearchValues.getPageSize() != null ? taskSearchValues.getPageSize() : null;
//
//        // чтобы захватить в выборке все задачи по датам, независимо от времени - можно выставить время с 00:00 до 23:59
//
//        Date dateFrom = null;
//        Date dateTo = null;
//
//
//        // выставить 00:01 для начальной даты (если она указана)
//        if (taskSearchValues.getDateFrom() != null) {
//            Calendar calendarFrom = Calendar.getInstance();
//            calendarFrom.setTime(taskSearchValues.getDateFrom());
//            calendarFrom.set(Calendar.HOUR_OF_DAY, 0);
//            calendarFrom.set(Calendar.MINUTE, 1);
//            calendarFrom.set(Calendar.SECOND, 1);
//            calendarFrom.set(Calendar.MILLISECOND, 1);
//
//            dateFrom = calendarFrom.getTime(); // записываем начальную дату с 00:01
//
//        }
//
//
//        // выставить 23:59 для конечной даты (если она указана)
//        if (taskSearchValues.getDateTo() != null) {
//
//            Calendar calendarTo = Calendar.getInstance();
//            calendarTo.setTime(taskSearchValues.getDateTo());
//            calendarTo.set(Calendar.HOUR_OF_DAY, 23);
//            calendarTo.set(Calendar.MINUTE, 59);
//            calendarTo.set(Calendar.SECOND, 59);
//            calendarTo.set(Calendar.MILLISECOND, 999);
//
//            dateTo = calendarTo.getTime(); // записываем конечную дату с 23:59
//
//        }
//
//
//        // направление сортировки
//        Sort.Direction direction = sortDirection == null || sortDirection.trim().length() == 0 || sortDirection.trim().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
//
//        /* Вторым полем для сортировки добавляем id, чтобы всегда сохранялся строгий порядок.
//            Например, если у 2-х задач одинаковое значение приоритета и мы сортируем по этому полю.
//            Порядок следования этих 2-х записей после выполнения запроса может каждый раз меняться, т.к. не указано второе поле сортировки.
//            Поэтому и используем ID - тогда все записи с одинаковым значением приоритета будут следовать в одном порядке по ID.
//         */
//
//        // объект сортировки, который содержит стобец и направление
//        Sort sort = Sort.by(direction, sortColumn, ID_COLUMN);
//
//        // объект постраничности
//        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
//
//        // результат запроса с постраничным выводом
//        Page<Task> result = taskService.findByParams(title,
//                completed,
//                priorityId,
//                categoryId,
//                taskSearchValues.getUserId(),
//                dateFrom,
//                dateTo,
//                pageRequest);
//
//        // результат запроса
//        return ResponseEntity.ok(result);
//
//    }

}
