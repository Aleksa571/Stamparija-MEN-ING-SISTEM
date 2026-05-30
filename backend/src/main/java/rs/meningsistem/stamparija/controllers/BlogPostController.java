package rs.meningsistem.stamparija.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rs.meningsistem.stamparija.constants.RoleConstants;
import rs.meningsistem.stamparija.models.BlogPostModel;
import rs.meningsistem.stamparija.security.UserPrincipal;
import rs.meningsistem.stamparija.services.IBlogPostService;

import java.util.List;

@RestController
@RequestMapping("/api/blog")
@RequiredArgsConstructor
public class BlogPostController {

    private final IBlogPostService blogPostService;

    @GetMapping
    public ResponseEntity<List<BlogPostModel>> findAll() {
        return ResponseEntity.ok(blogPostService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPostModel> findById(@PathVariable Long id) {
        return ResponseEntity.ok(blogPostService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('" + RoleConstants.ADMIN + "')")
    public ResponseEntity<BlogPostModel> create(@AuthenticationPrincipal UserPrincipal principal,
                                                @Valid @RequestBody BlogPostModel model) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blogPostService.create(principal.getUsername(), model));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('" + RoleConstants.ADMIN + "')")
    public ResponseEntity<BlogPostModel> update(@PathVariable Long id,
                                                @Valid @RequestBody BlogPostModel model) {
        return ResponseEntity.ok(blogPostService.update(id, model));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('" + RoleConstants.ADMIN + "')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        blogPostService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
