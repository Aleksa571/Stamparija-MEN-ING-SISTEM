package rs.meningsistem.stamparija.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.meningsistem.stamparija.constants.RoleConstants;
import rs.meningsistem.stamparija.models.CategoryModel;
import rs.meningsistem.stamparija.services.ICategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryModel>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryModel> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('" + RoleConstants.ADMIN + "')")
    public ResponseEntity<CategoryModel> create(@Valid @RequestBody CategoryModel model) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(model));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('" + RoleConstants.ADMIN + "')")
    public ResponseEntity<CategoryModel> update(@PathVariable Long id, @Valid @RequestBody CategoryModel model) {
        return ResponseEntity.ok(categoryService.update(id, model));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('" + RoleConstants.ADMIN + "')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
