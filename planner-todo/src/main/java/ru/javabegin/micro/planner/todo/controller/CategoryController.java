package ru.javabegin.micro.planner.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.micro.planner.todo.dto.category.CategoryCreated;
import ru.javabegin.micro.planner.todo.dto.category.CategoryDTO;
import ru.javabegin.micro.planner.todo.service.CategoryService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/category") // базовый URI
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<Void> add(@RequestBody CategoryCreated categoryCreated,
                                    @AuthenticationPrincipal Jwt jwt) {
        categoryService.add(categoryCreated, jwt.getId());
        return ResponseEntity.ok().build();

    }

    @PostMapping("/all")
    public List<CategoryDTO> findAll(@AuthenticationPrincipal Jwt jwt) {
        return categoryService.findAll(jwt.getId());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        categoryService.deleteById(id);
        return ResponseEntity.ok().build();
    }


//    @PutMapping("/update")
//    public ResponseEntity<CategoryDTO> update(@RequestParam String title,
//                                              @AuthenticationPrincipal Jwt jwt) {
//        return  ResponseEntity.ok(categoryService.update(jwt.getId(), title));
//    }
//
//    @PostMapping("/search")
//    public ResponseEntity<List<CategoryDTO>> search(@RequestParam String query,
//                                                    @AuthenticationPrincipal Jwt jwt) {
//        return ResponseEntity.ok(categoryService.findByTitle(query, jwt.getId()));
//    }
//    @PostMapping("/id")
//    public ResponseEntity<CategoryDTO> findById(@RequestBody String id) {
//
//        return ResponseEntity.ok(categoryService.findById(id));
//    }

}
