package rs.meningsistem.stamparija.service;

import rs.meningsistem.stamparija.dto.request.ProductRequestDto;
import rs.meningsistem.stamparija.dto.response.ProductResponseDto;

import java.util.List;

public interface ProductService {
    List<ProductResponseDto> findAll();
    List<ProductResponseDto> findByCategory(Long categoryId);
    List<ProductResponseDto> search(String query);
    ProductResponseDto findById(Long id);
    ProductResponseDto create(ProductRequestDto dto);
    ProductResponseDto update(Long id, ProductRequestDto dto);
    void delete(Long id);
}
