package rs.meningsistem.stamparija.services;

import rs.meningsistem.stamparija.models.BlogPostModel;

import java.util.List;

public interface IBlogPostService {
    List<BlogPostModel> findAll();
    BlogPostModel findById(Long id);
    BlogPostModel create(String authorUsername, BlogPostModel model);
    BlogPostModel update(Long id, BlogPostModel model);
    void delete(Long id);
}
