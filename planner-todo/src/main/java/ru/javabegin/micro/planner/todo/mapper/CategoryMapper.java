package ru.javabegin.micro.planner.todo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.javabegin.micro.planner.entity.Category;
import ru.javabegin.micro.planner.todo.dto.category.CategoryCreated;
import ru.javabegin.micro.planner.todo.dto.category.CategoryDTO;

@Mapper(componentModel = "spring", uses = {})
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "completedCount", ignore = true)
    @Mapping(target = "uncompletedCount", ignore = true)
    Category toEntity(CategoryCreated categoryCreated, String userId);

    CategoryDTO toDTO(Category category);

}
