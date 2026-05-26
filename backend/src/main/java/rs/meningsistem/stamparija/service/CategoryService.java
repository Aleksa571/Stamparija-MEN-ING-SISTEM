package rs.meningsistem.stamparija.service;

import rs.meningsistem.stamparija.dto.request.CategoryRequestDto;
import rs.meningsistem.stamparija.dto.response.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    List<CategoryResponseDto> findAll();
    CategoryResponseDto findById(Long id);
    CategoryResponseDto create(CategoryRequestDto dto);
    CategoryResponseDto update(Long id, CategoryRequestDto dto);
    void delete(Long id);
}
