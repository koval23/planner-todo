package ru.javabegin.micro.planner.todo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.javabegin.micro.planner.entity.Category;
import ru.javabegin.micro.planner.todo.dto.category.CategoryCreated;
import ru.javabegin.micro.planner.todo.dto.category.CategoryDTO;

@Mapper
public interface CategoryMapper {

    //    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "userId", source = "userId")
    Category toEntity(CategoryCreated categoryCreated, String userId);

    CategoryDTO toDTO(Category category);

}
