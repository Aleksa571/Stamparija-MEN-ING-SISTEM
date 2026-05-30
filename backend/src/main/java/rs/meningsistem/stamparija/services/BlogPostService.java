package rs.meningsistem.stamparija.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.meningsistem.stamparija.entities.BlogPost;
import rs.meningsistem.stamparija.entities.User;
import rs.meningsistem.stamparija.exceptions.ResourceNotFoundException;
import rs.meningsistem.stamparija.mappers.BlogPostMapper;
import rs.meningsistem.stamparija.models.BlogPostModel;
import rs.meningsistem.stamparija.repositories.IBlogPostRepository;
import rs.meningsistem.stamparija.repositories.IUserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BlogPostService implements IBlogPostService {

    private final IBlogPostRepository blogPostRepository;
    private final IUserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BlogPostModel> findAll() {
        return BlogPostMapper.toModelList(blogPostRepository.findAllByOrderByCreatedAtDesc());
    }

    @Override
    @Transactional(readOnly = true)
    public BlogPostModel findById(Long id) {
        return BlogPostMapper.toModel(blogPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post", "id", id)));
    }

    @Override
    public BlogPostModel create(String authorUsername, BlogPostModel model) {
        User author = userRepository.findByUsername(authorUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik", "username", authorUsername));
        BlogPost post = BlogPostMapper.toEntity(model, author);
        return BlogPostMapper.toModel(blogPostRepository.save(post));
    }

    @Override
    public BlogPostModel update(Long id, BlogPostModel model) {
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post", "id", id));
        post.setTitle(model.getTitle());
        post.setContent(model.getContent());
        post.setImageUrl(model.getImageUrl());
        return BlogPostMapper.toModel(blogPostRepository.save(post));
    }

    @Override
    public void delete(Long id) {
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post", "id", id));
        blogPostRepository.delete(post);
    }
}
