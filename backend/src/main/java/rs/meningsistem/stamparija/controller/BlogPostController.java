package rs.meningsistem.stamparija.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rs.meningsistem.stamparija.dto.request.BlogPostRequestDto;
import rs.meningsistem.stamparija.dto.response.BlogPostResponseDto;
import rs.meningsistem.stamparija.security.UserPrincipal;
import rs.meningsistem.stamparija.service.BlogPostService;

import java.util.List;

@RestController
@RequestMapping("/api/blog")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;

    @GetMapping
    public ResponseEntity<List<BlogPostResponseDto>> findAll() {
        return ResponseEntity.ok(blogPostService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPostResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(blogPostService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BlogPostResponseDto> create(@AuthenticationPrincipal UserPrincipal principal,
                                                      @Valid @RequestBody BlogPostRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blogPostService.create(principal.getUsername(), dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BlogPostResponseDto> update(@PathVariable Long id,
                                                      @Valid @RequestBody BlogPostRequestDto dto) {
        return ResponseEntity.ok(blogPostService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        blogPostService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
