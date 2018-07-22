package es.alordiez.wumpus.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import es.alordiez.wumpus.domain.game.GamePits;


/**
 * Spring Data  repository for the GamePits entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GamePitsRepository extends JpaRepository<GamePits, Long> {

}
