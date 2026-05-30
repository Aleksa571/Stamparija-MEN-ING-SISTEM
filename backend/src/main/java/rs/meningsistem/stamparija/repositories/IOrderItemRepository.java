package rs.meningsistem.stamparija.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.meningsistem.stamparija.entities.OrderItem;

@Repository
public interface IOrderItemRepository extends JpaRepository<OrderItem, Long> {
}
