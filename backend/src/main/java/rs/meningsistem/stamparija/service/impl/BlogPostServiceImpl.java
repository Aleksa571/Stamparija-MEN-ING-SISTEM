package rs.meningsistem.stamparija.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.meningsistem.stamparija.dto.request.BlogPostRequestDto;
import rs.meningsistem.stamparija.dto.response.BlogPostResponseDto;
import rs.meningsistem.stamparija.exception.ResourceNotFoundException;
import rs.meningsistem.stamparija.model.BlogPost;
import rs.meningsistem.stamparija.model.User;
import rs.meningsistem.stamparija.repository.BlogPostRepository;
import rs.meningsistem.stamparija.repository.UserRepository;
import rs.meningsistem.stamparija.service.BlogPostService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BlogPostServiceImpl implements BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BlogPostResponseDto> findAll() {
        return blogPostRepository.findAllByOrderByCreatedAtDesc().stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BlogPostResponseDto findById(Long id) {
        return toDto(blogPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post", "id", id)));
    }

    @Override
    public BlogPostResponseDto create(String authorUsername, BlogPostRequestDto dto) {
        User author = userRepository.findByUsername(authorUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik", "username", authorUsername));
        BlogPost post = BlogPost.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .imageUrl(dto.getImageUrl())
                .author(author)
                .build();
        return toDto(blogPostRepository.save(post));
    }

    @Override
    public BlogPostResponseDto update(Long id, BlogPostRequestDto dto) {
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post", "id", id));
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setImageUrl(dto.getImageUrl());
        return toDto(blogPostRepository.save(post));
    }

    @Override
    public void delete(Long id) {
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post", "id", id));
        blogPostRepository.delete(post);
    }

    private BlogPostResponseDto toDto(BlogPost p) {
        String fullName = (p.getAuthor().getFirstName() != null ? p.getAuthor().getFirstName() : "") + " " +
                (p.getAuthor().getLastName() != null ? p.getAuthor().getLastName() : "");
        return BlogPostResponseDto.builder()
                .id(p.getId())
                .title(p.getTitle())
                .content(p.getContent())
                .imageUrl(p.getImageUrl())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .authorId(p.getAuthor().getId())
                .authorUsername(p.getAuthor().getUsername())
                .authorFullName(fullName.trim())
                .build();
    }
}
