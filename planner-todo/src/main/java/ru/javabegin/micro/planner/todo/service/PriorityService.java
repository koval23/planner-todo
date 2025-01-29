package ru.javabegin.micro.planner.todo.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javabegin.micro.planner.entity.Priority;
import ru.javabegin.micro.planner.todo.dto.priority.PriorityCreated;
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

    public PriorityDTO add(PriorityCreated priorityCreated, String userId) {

        if (priorityCreated.getColor().isBlank()) {
            priorityCreated.setColor("white");
        }

        Priority priority = priorityRepository.save(priorityMapper.toEntity(priorityCreated, userId));

        return priorityMapper.toDTO(priority);
    }

    public void deleteById(String id) {
        priorityExists(id);
        priorityRepository.deleteById(id);
    }

    public List<PriorityDTO> findAllPriorityByUser(String userId) {
        return priorityRepository.findByUserIdOrderByIdAsc(userId).stream()
                .map(priorityMapper::toDTO)
                .toList();
    }

    public List<PriorityDTO> findAll() {
        return priorityRepository.findAll().stream()
                .map(priorityMapper::toDTO)
                .toList();
    }

    public PriorityDTO findById(String id) {
        return priorityMapper.toDTO(priorityExists(id));
    }

    @Named("mapPriority")
    public Priority priorityExists(String id) {
        return priorityRepository.findById(id).orElseThrow(
                () -> CommonException.of(ErrorCode.PRIORITY_NOT_FOUND, HttpStatus.NOT_FOUND)
        );
    }


//    public PriorityDTO update(PriorityDTO priorityDTO) {
//
//        Priority priority = priorityExists(priorityDTO.getId());
//
//        if (priorityDTO.getTitle().equals(priority.getTitle())) {
//            priority.setTitle(priorityDTO.getTitle());
//        }
//        if (priorityDTO.getColor().equals(priority.getColor())) {
//            priority.setColor(priorityDTO.getColor());
//        }
//        priorityRepository.save(priority);
//
//        return priorityMapper.toDTO(priority);
//    }

//    public List<PriorityDTO> find(String title, String userId) {
//        return priorityRepository.findByTitle(title, userId).stream()
//                .map(priorityMapper::toDTO)
//                .toList();
//    }

}
