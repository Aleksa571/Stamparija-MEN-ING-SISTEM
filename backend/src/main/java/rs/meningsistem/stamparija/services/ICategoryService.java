package rs.meningsistem.stamparija.services;

import rs.meningsistem.stamparija.models.CategoryModel;

import java.util.List;

public interface ICategoryService {
    List<CategoryModel> findAll();
    CategoryModel findById(Long id);
    CategoryModel create(CategoryModel model);
    CategoryModel update(Long id, CategoryModel model);
    void delete(Long id);
}
