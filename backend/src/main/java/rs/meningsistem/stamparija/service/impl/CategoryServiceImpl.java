package rs.meningsistem.stamparija.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.meningsistem.stamparija.dto.request.CategoryRequestDto;
import rs.meningsistem.stamparija.dto.response.CategoryResponseDto;
import rs.meningsistem.stamparija.exception.ConflictException;
import rs.meningsistem.stamparija.exception.ResourceNotFoundException;
import rs.meningsistem.stamparija.model.Category;
import rs.meningsistem.stamparija.repository.CategoryRepository;
import rs.meningsistem.stamparija.service.CategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDto findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategorija", "id", id));
        return toDto(category);
    }

    @Override
    public CategoryResponseDto create(CategoryRequestDto dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            throw new ConflictException("Kategorija sa nazivom '" + dto.getName() + "' vec postoji");
        }
        Category category = Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .imageUrl(dto.getImageUrl())
                .build();
        return toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryResponseDto update(Long id, CategoryRequestDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategorija", "id", id));
        if (!category.getName().equals(dto.getName()) && categoryRepository.existsByName(dto.getName())) {
            throw new ConflictException("Kategorija sa nazivom '" + dto.getName() + "' vec postoji");
        }
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setImageUrl(dto.getImageUrl());
        return toDto(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategorija", "id", id));
        if (!category.getProducts().isEmpty()) {
            throw new ConflictException("Kategorija sadrzi proizvode i ne moze biti obrisana");
        }
        categoryRepository.delete(category);
    }

    private CategoryResponseDto toDto(Category c) {
        return CategoryResponseDto.builder()
                .id(c.getId())
                .name(c.getName())
                .description(c.getDescription())
                .imageUrl(c.getImageUrl())
                .productCount(c.getProducts() != null ? c.getProducts().size() : 0)
                .build();
    }
}
