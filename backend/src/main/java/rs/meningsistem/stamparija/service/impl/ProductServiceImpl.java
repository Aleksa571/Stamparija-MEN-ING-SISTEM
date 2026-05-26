package rs.meningsistem.stamparija.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.meningsistem.stamparija.dto.request.ProductRequestDto;
import rs.meningsistem.stamparija.dto.response.ProductResponseDto;
import rs.meningsistem.stamparija.exception.ResourceNotFoundException;
import rs.meningsistem.stamparija.model.Category;
import rs.meningsistem.stamparija.model.Product;
import rs.meningsistem.stamparija.repository.CategoryRepository;
import rs.meningsistem.stamparija.repository.ProductRepository;
import rs.meningsistem.stamparija.service.ProductService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findAll() {
        return productRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Kategorija", "id", categoryId);
        }
        return productRepository.findByCategoryId(categoryId).stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> search(String query) {
        return productRepository.findByNameContainingIgnoreCase(query).stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto findById(Long id) {
        return toDto(productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proizvod", "id", id)));
    }

    @Override
    public ProductResponseDto create(ProductRequestDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Kategorija", "id", dto.getCategoryId()));
        Product product = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .dimensions(dto.getDimensions())
                .imageUrl(dto.getImageUrl())
                .stock(dto.getStock())
                .available(dto.getAvailable() != null ? dto.getAvailable() : true)
                .category(category)
                .build();
        return toDto(productRepository.save(product));
    }

    @Override
    public ProductResponseDto update(Long id, ProductRequestDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proizvod", "id", id));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Kategorija", "id", dto.getCategoryId()));
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setDimensions(dto.getDimensions());
        product.setImageUrl(dto.getImageUrl());
        product.setStock(dto.getStock());
        if (dto.getAvailable() != null) product.setAvailable(dto.getAvailable());
        product.setCategory(category);
        return toDto(productRepository.save(product));
    }

    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proizvod", "id", id));
        productRepository.delete(product);
    }

    private ProductResponseDto toDto(Product p) {
        return ProductResponseDto.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .dimensions(p.getDimensions())
                .imageUrl(p.getImageUrl())
                .stock(p.getStock())
                .available(p.getAvailable())
                .createdAt(p.getCreatedAt())
                .categoryId(p.getCategory().getId())
                .categoryName(p.getCategory().getName())
                .build();
    }
}
