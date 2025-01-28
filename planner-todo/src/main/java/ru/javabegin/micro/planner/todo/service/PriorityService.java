package ru.javabegin.micro.planner.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javabegin.micro.planner.entity.Priority;
import ru.javabegin.micro.planner.todo.dto.priority.PriorityDTO;
import ru.javabegin.micro.planner.todo.handling.CommonException;
import ru.javabegin.micro.planner.todo.handling.ErrorCode;
import ru.javabegin.micro.planner.todo.mapper.PriorityMapper;
import ru.javabegin.micro.planner.todo.repo.PriorityRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PriorityService {

    private final PriorityMapper priorityMapper;
    private final PriorityRepository priorityRepository;

    public List<Priority> findAll(String userId) {
        return priorityRepository.findByUserIdOrderByIdAsc(userId);
    }

    public PriorityDTO add(PriorityDTO priorityDTO, String userId) {

        if (priorityDTO.getColor().isBlank()) {
            priorityDTO.setColor("white");
        }
        priorityDTO.setUserId(userId);

        Priority priority = priorityRepository.save(priorityMapper.priorityDTOToPriority(priorityDTO));

        return priorityMapper.priorityToPriorityDTO(priority);
    }

    public PriorityDTO update(PriorityDTO priorityDTO) {

        Priority priority = findById(priorityDTO.getId());

        if (priorityDTO.getTitle().equals(priority.getTitle())) {
            priority.setTitle(priorityDTO.getTitle());
        }
        if (priorityDTO.getColor().equals(priority.getColor())) {
            priority.setColor(priorityDTO.getColor());
        }
        priorityRepository.save(priority);

        return priorityMapper.priorityToPriorityDTO(priority);
    }

    public void deleteById(String id) {
        findById(id);
        priorityRepository.deleteById(id);
    }

    public Priority findById(String id) {
        return priorityRepository.findById(id).orElseThrow(
                () -> CommonException.of(ErrorCode.PRIORITY_NOT_FOUND, HttpStatus.NOT_FOUND)
        );
    }

    public List<Priority> find(String title, String userId) {
        return priorityRepository.findByTitle(title, userId);
    }

}
