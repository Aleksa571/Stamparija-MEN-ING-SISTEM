package rs.meningsistem.stamparija.services;

import rs.meningsistem.stamparija.models.ProductModel;

import java.util.List;

public interface IProductService {
    List<ProductModel> findAll();
    List<ProductModel> findByCategory(Long categoryId);
    List<ProductModel> search(String query);
    ProductModel findById(Long id);
    ProductModel create(ProductModel model);
    ProductModel update(Long id, ProductModel model);
    void delete(Long id);
}
