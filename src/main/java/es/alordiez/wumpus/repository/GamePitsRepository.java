package es.alordiez.wumpus.repository;

import es.alordiez.wumpus.domain.GamePits;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GamePits entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GamePitsRepository extends JpaRepository<GamePits, Long> {

}
