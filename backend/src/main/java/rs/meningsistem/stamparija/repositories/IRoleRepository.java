package rs.meningsistem.stamparija.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.meningsistem.stamparija.entities.Role;
import rs.meningsistem.stamparija.entities.enums.RoleName;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
