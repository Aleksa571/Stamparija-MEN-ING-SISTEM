package rs.meningsistem.stamparija.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.meningsistem.stamparija.entities.Category;
import rs.meningsistem.stamparija.entities.Product;
import rs.meningsistem.stamparija.exceptions.ResourceNotFoundException;
import rs.meningsistem.stamparija.mappers.ProductMapper;
import rs.meningsistem.stamparija.models.ProductModel;
import rs.meningsistem.stamparija.repositories.ICategoryRepository;
import rs.meningsistem.stamparija.repositories.IProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService implements IProductService {

    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductModel> findAll() {
        return ProductMapper.toModelList(productRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductModel> findByCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Kategorija", "id", categoryId);
        }
        return ProductMapper.toModelList(productRepository.findByCategoryId(categoryId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductModel> search(String query) {
        return ProductMapper.toModelList(productRepository.findByNameContainingIgnoreCase(query));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductModel findById(Long id) {
        return ProductMapper.toModel(productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proizvod", "id", id)));
    }

    @Override
    public ProductModel create(ProductModel model) {
        Category category = categoryRepository.findById(model.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Kategorija", "id", model.getCategoryId()));
        Product product = ProductMapper.toEntity(model, category);
        return ProductMapper.toModel(productRepository.save(product));
    }

    @Override
    public ProductModel update(Long id, ProductModel model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proizvod", "id", id));
        Category category = categoryRepository.findById(model.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Kategorija", "id", model.getCategoryId()));
        ProductMapper.updateEntity(product, model, category);
        return ProductMapper.toModel(productRepository.save(product));
    }

    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proizvod", "id", id));
        productRepository.delete(product);
    }
}
