package rs.meningsistem.stamparija.mappers;

import rs.meningsistem.stamparija.entities.Category;
import rs.meningsistem.stamparija.entities.Product;
import rs.meningsistem.stamparija.models.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class ProductMapper {

    private ProductMapper() {}

    public static Product toEntity(ProductModel model, Category category) {
        return Product.builder()
                .name(model.getName())
                .description(model.getDescription())
                .price(model.getPrice())
                .dimensions(model.getDimensions())
                .imageUrl(model.getImageUrl())
                .stock(model.getStock())
                .available(model.getAvailable() != null ? model.getAvailable() : true)
                .category(category)
                .build();
    }

    public static void updateEntity(Product entity, ProductModel model, Category category) {
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setPrice(model.getPrice());
        entity.setDimensions(model.getDimensions());
        entity.setImageUrl(model.getImageUrl());
        entity.setStock(model.getStock());
        if (model.getAvailable() != null) {
            entity.setAvailable(model.getAvailable());
        }
        entity.setCategory(category);
    }

    public static ProductModel toModel(Product entity) {
        return ProductModel.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .dimensions(entity.getDimensions())
                .imageUrl(entity.getImageUrl())
                .stock(entity.getStock())
                .available(entity.getAvailable())
                .createdAt(entity.getCreatedAt())
                .categoryId(entity.getCategory().getId())
                .categoryName(entity.getCategory().getName())
                .build();
    }

    public static List<ProductModel> toModelList(List<Product> entities) {
        var list = new ArrayList<ProductModel>();
        for (var entity : entities) {
            list.add(toModel(entity));
        }
        return list;
    }
}
