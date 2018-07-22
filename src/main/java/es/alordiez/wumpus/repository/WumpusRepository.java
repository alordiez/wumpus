package es.alordiez.wumpus.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import es.alordiez.wumpus.domain.game.Wumpus;


/**
 * Spring Data  repository for the Wumpus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WumpusRepository extends JpaRepository<Wumpus, Long> {

}
