package rs.meningsistem.stamparija.mappers;

import rs.meningsistem.stamparija.entities.BlogPost;
import rs.meningsistem.stamparija.entities.User;
import rs.meningsistem.stamparija.models.BlogPostModel;

import java.util.ArrayList;
import java.util.List;

public class BlogPostMapper {

    private BlogPostMapper() {}

    public static BlogPost toEntity(BlogPostModel model, User author) {
        return BlogPost.builder()
                .title(model.getTitle())
                .content(model.getContent())
                .imageUrl(model.getImageUrl())
                .author(author)
                .build();
    }

    public static BlogPostModel toModel(BlogPost entity) {
        var author = entity.getAuthor();
        String firstName = author.getFirstName() != null ? author.getFirstName() : "";
        String lastName = author.getLastName() != null ? author.getLastName() : "";
        String fullName = (firstName + " " + lastName).trim();

        return BlogPostModel.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .imageUrl(entity.getImageUrl())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .authorId(author.getId())
                .authorUsername(author.getUsername())
                .authorFullName(fullName)
                .build();
    }

    public static List<BlogPostModel> toModelList(List<BlogPost> entities) {
        var list = new ArrayList<BlogPostModel>();
        for (var entity : entities) {
            list.add(toModel(entity));
        }
        return list;
    }
}
