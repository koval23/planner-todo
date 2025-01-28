package ru.javabegin.micro.planner.todo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.javabegin.micro.planner.entity.Category;
import ru.javabegin.micro.planner.todo.dto.category.CategoryRequest;
import ru.javabegin.micro.planner.todo.dto.category.CategoryResponse;

@Mapper
public interface CategoryMapper {

    @Mapping(target = "userId", ignore = true)
    CategoryResponse categoryToCategoryResponse(Category category);

//    @Mapping(target = "userId", ignore = true)
    Category categoryResponceToCategory(CategoryRequest categoryRequest);

    Category categoryRequestToCategory(CategoryRequest categoryRequest);

}
