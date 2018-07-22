package es.alordiez.wumpus.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import es.alordiez.wumpus.domain.game.Game;


/**
 * Spring Data  repository for the Game entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

}
