package rs.meningsistem.stamparija.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.meningsistem.stamparija.constants.RoleConstants;
import rs.meningsistem.stamparija.models.ProductModel;
import rs.meningsistem.stamparija.services.IProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductModel>> findAll(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search) {
        if (categoryId != null) {
            return ResponseEntity.ok(productService.findByCategory(categoryId));
        }
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(productService.search(search));
        }
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductModel> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('" + RoleConstants.ADMIN + "')")
    public ResponseEntity<ProductModel> create(@Valid @RequestBody ProductModel model) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(model));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('" + RoleConstants.ADMIN + "')")
    public ResponseEntity<ProductModel> update(@PathVariable Long id, @Valid @RequestBody ProductModel model) {
        return ResponseEntity.ok(productService.update(id, model));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('" + RoleConstants.ADMIN + "')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
