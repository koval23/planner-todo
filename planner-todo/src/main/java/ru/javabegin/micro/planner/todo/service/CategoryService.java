package ru.javabegin.micro.planner.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javabegin.micro.planner.entity.Category;
import ru.javabegin.micro.planner.todo.dto.category.CategoryRequest;
import ru.javabegin.micro.planner.todo.dto.category.CategoryResponse;
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

    public List<CategoryResponse> findAll(String userId) {
        List<Category> categoryList = categoryRepository.findByUserIdOrderByTitleAsc(userId);
        return categoryList.stream()
                .map(categoryMapper::categoryToCategoryResponse)
                .toList();
    }

    public CategoryResponse add(CategoryRequest categoryRequest, String userId) {

        if (userId == null) {
            throw CommonException.of(ErrorCode.ID_MUST_NOT_BE_NULL, HttpStatus.NOT_FOUND);
        }

        categoryRequest.setUserId(userId);

        if (categoryRequest.getTitle() == null || categoryRequest.getTitle().trim().isEmpty()) {
            throw CommonException.of(ErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST);
        }

        Category category = categoryRepository.save(categoryMapper.categoryRequestToCategory(categoryRequest));

        return categoryMapper.categoryToCategoryResponse(category);
    }

    public CategoryResponse update(String id, String updateTitle) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST));
        if (!category.getTitle().equals(updateTitle)) {
            category.setTitle(updateTitle);
            categoryRepository.save(category);
            return categoryMapper.categoryToCategoryResponse(category);
        }

        return categoryMapper.categoryToCategoryResponse(category);
    }

    public void deleteById(String id) {
        if (categoryRepository.existsById(id)){
            categoryRepository.deleteById(id);
        }else {
            throw CommonException.of(ErrorCode.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

    }

    // поиск категорий пользователя по названию
    public List<CategoryResponse> findByTitle(String text, String userId) {
        List<Category> categoryList = categoryRepository.findByTitle(text, userId);

        return categoryList.stream()
                .map(categoryMapper::categoryToCategoryResponse)
                .toList();
    }

    // поиск категории по ID
    public CategoryResponse findById(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST));
        return categoryMapper.categoryToCategoryResponse(category);
    }

}
