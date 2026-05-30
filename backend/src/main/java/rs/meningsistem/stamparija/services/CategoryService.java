package rs.meningsistem.stamparija.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.meningsistem.stamparija.entities.Category;
import rs.meningsistem.stamparija.exceptions.ConflictException;
import rs.meningsistem.stamparija.exceptions.ResourceNotFoundException;
import rs.meningsistem.stamparija.mappers.CategoryMapper;
import rs.meningsistem.stamparija.models.CategoryModel;
import rs.meningsistem.stamparija.repositories.ICategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService implements ICategoryService {

    private final ICategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryModel> findAll() {
        return CategoryMapper.toModelList(categoryRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryModel findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategorija", "id", id));
        return CategoryMapper.toModel(category);
    }

    @Override
    public CategoryModel create(CategoryModel model) {
        if (categoryRepository.existsByName(model.getName())) {
            throw new ConflictException("Kategorija sa nazivom '" + model.getName() + "' vec postoji");
        }
        Category category = CategoryMapper.toEntity(model);
        category.setId(null);
        return CategoryMapper.toModel(categoryRepository.save(category));
    }

    @Override
    public CategoryModel update(Long id, CategoryModel model) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategorija", "id", id));
        if (!category.getName().equals(model.getName()) && categoryRepository.existsByName(model.getName())) {
            throw new ConflictException("Kategorija sa nazivom '" + model.getName() + "' vec postoji");
        }
        category.setName(model.getName());
        category.setDescription(model.getDescription());
        category.setImageUrl(model.getImageUrl());
        return CategoryMapper.toModel(categoryRepository.save(category));
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
}
