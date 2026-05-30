package rs.meningsistem.stamparija.mappers;

import rs.meningsistem.stamparija.entities.Category;
import rs.meningsistem.stamparija.models.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

    private CategoryMapper() {}

    public static Category toEntity(CategoryModel model) {
        return Category.builder()
                .id(model.getId())
                .name(model.getName())
                .description(model.getDescription())
                .imageUrl(model.getImageUrl())
                .build();
    }

    public static CategoryModel toModel(Category entity) {
        return CategoryModel.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .imageUrl(entity.getImageUrl())
                .productCount(entity.getProducts() != null ? entity.getProducts().size() : 0)
                .build();
    }

    public static List<CategoryModel> toModelList(List<Category> entities) {
        var list = new ArrayList<CategoryModel>();
        for (var entity : entities) {
            list.add(toModel(entity));
        }
        return list;
    }
}
