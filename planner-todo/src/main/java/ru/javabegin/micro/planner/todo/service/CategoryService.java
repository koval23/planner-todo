package ru.javabegin.micro.planner.todo.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javabegin.micro.planner.entity.Category;
import ru.javabegin.micro.planner.todo.dto.category.CategoryCreated;
import ru.javabegin.micro.planner.todo.dto.category.CategoryDTO;
import ru.javabegin.micro.planner.todo.handling.CommonException;
import ru.javabegin.micro.planner.todo.handling.ErrorCode;
import ru.javabegin.micro.planner.todo.mapper.CategoryMapper;
import ru.javabegin.micro.planner.todo.repo.CategoryRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    public CategoryDTO add(CategoryCreated categoryCreated, String userId) {

        if (userId == null) {
            throw CommonException.of(ErrorCode.ID_MUST_NOT_BE_NULL, HttpStatus.NOT_FOUND);
        }

        if (categoryCreated.getTitle() == null || categoryCreated.getTitle().trim().isEmpty()) {
            throw CommonException.of(ErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST);
        }

        Category category = categoryRepository.save(categoryMapper.toEntity(categoryCreated, userId));

        return categoryMapper.toDTO(category);
    }

    public List<CategoryDTO> findAll(String userId) {
        List<Category> categoryList = categoryRepository.findByUserIdOrderByTitleAsc(userId);
        return categoryList.stream()
                .map(categoryMapper::toDTO)
                .toList();
    }

    @Named("mapCategory")
    public Category findById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST));
    }

    public void deleteById(String id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw CommonException.of(ErrorCode.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

    }

//    public CategoryDTO update(CategoryDTO categoryDTO) {
//
//        Category category =findById(categoryDTO.getId());
//
//        if (!category.getTitle().equals(categoryDTO.getTitle())) {
//            category.setTitle(categoryDTO.getTitle());
//            categoryRepository.save(category);
//            return categoryMapper.toDTO(category);
//        }
//
//        return categoryMapper.toDTO(category);
//    }
//
//    // поиск категорий пользователя по названию
//    public List<CategoryDTO> findByTitle(String text, String userId) {
//        List<Category> categoryList = categoryRepository.findByTitle(text, userId);
//
//        return categoryList.stream()
//                .map(categoryMapper::toDTO)
//                .toList();
//    }



}
