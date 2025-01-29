package ru.javabegin.micro.planner.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javabegin.micro.planner.entity.Task;
import ru.javabegin.micro.planner.todo.dto.task.TaskCreated;
import ru.javabegin.micro.planner.todo.dto.task.TaskResponse;
import ru.javabegin.micro.planner.todo.handling.CommonException;
import ru.javabegin.micro.planner.todo.handling.ErrorCode;
import ru.javabegin.micro.planner.todo.mapper.TaskMapper;
import ru.javabegin.micro.planner.todo.repo.TaskRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final PriorityService priorityService;
    private final CategoryService categoryService;

    public TaskResponse add(TaskCreated taskCreated, String userId) {

        Task newTask = taskMapper.toEntity(taskCreated, userId);
        taskRepository.save(newTask);

        return taskMapper.toResponse(newTask);
    }

    public void deleteById(String id) {
        taskRepository.deleteById(id);
    }

    public List<Task> findAll(String userId) {
        return taskRepository.findByUserIdOrderByTitleAsc(userId);
    }

    public TaskResponse findById(String id) {
        Task task = taskRepository.findById(id).orElseThrow(
                ()-> CommonException.of(ErrorCode.INVALID_ID, HttpStatus.NOT_FOUND)
        );
        return taskMapper.toResponse(task);
    }

//    public TaskResponse update(TaskUpdate taskUpdate) {
//
//        Task task = taskRepository.findById(taskUpdate.getId()).orElseThrow(
//                ()-> CommonException.of(ErrorCode.INVALID_ID, HttpStatus.NOT_FOUND)
//        );
//        if(!task.getPriority().getId().toString().equals(taskUpdate.getPriorityId())){
//            task.setPriority( priorityService.priorityExists(taskUpdate.getPriorityId()));
//        };
//
//        if(!task.getCategory().getId().toString().equals(taskUpdate.getCategoryId())){
//            task.setCategory(categoryService.findById(taskUpdate.getCategoryId()));
//        };
//
//        task.setTaskDate(taskUpdate.getTaskDate());
//        task.setTitle(taskUpdate.getTitle());
//        taskRepository.save(task);
//
//        return taskMapper.toResponse(task);
//    }
//
//    public Page<Task> findByParams(String text,
//                                   Boolean completed,
//                                   Long priorityId,
//                                   Long categoryId,
//                                   String userId,
//                                   Date dateFrom,
//                                   Date dateTo,
//                                   PageRequest paging) {
//        return taskRepository.findByParams(text, completed, priorityId, categoryId, userId, dateFrom, dateTo, paging);
//    }

}
