package ru.javabegin.micro.planner.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.micro.planner.todo.dto.category.CategoryRequest;
import ru.javabegin.micro.planner.todo.dto.category.CategoryResponse;
import ru.javabegin.micro.planner.todo.service.CategoryService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/category") // базовый URI
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/all")
    public List<CategoryResponse> findAll(@AuthenticationPrincipal Jwt jwt) {
        return categoryService.findAll(jwt.getSubject());
    }

    @PostMapping("/add")
    public ResponseEntity<Void> add(@RequestBody CategoryRequest categoryRequest,
                                    @AuthenticationPrincipal Jwt jwt) {
        categoryService.add(categoryRequest, jwt.getId());
        return ResponseEntity.ok().build();

    }

    @PutMapping("/update")
    public ResponseEntity<CategoryResponse> update(@RequestParam String title,
                                                   @AuthenticationPrincipal Jwt jwt) {
        return  ResponseEntity.ok(categoryService.update(jwt.getId(), title));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        categoryService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/search")
    public ResponseEntity<List<CategoryResponse>> search(@RequestParam String query,
                                                         @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(categoryService.findByTitle(query, jwt.getId()));
    }

    @PostMapping("/id")
    public ResponseEntity<CategoryResponse> findById(@RequestBody String id) {

        return ResponseEntity.ok(categoryService.findById(id));
    }

}
