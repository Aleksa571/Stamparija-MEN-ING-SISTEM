package rs.meningsistem.stamparija.service;

import rs.meningsistem.stamparija.dto.request.BlogPostRequestDto;
import rs.meningsistem.stamparija.dto.response.BlogPostResponseDto;

import java.util.List;

public interface BlogPostService {
    List<BlogPostResponseDto> findAll();
    BlogPostResponseDto findById(Long id);
    BlogPostResponseDto create(String authorUsername, BlogPostRequestDto dto);
    BlogPostResponseDto update(Long id, BlogPostRequestDto dto);
    void delete(Long id);
}
