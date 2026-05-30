package rs.meningsistem.stamparija.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.meningsistem.stamparija.entities.BlogPost;

import java.util.List;

@Repository
public interface IBlogPostRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findAllByOrderByCreatedAtDesc();
}
