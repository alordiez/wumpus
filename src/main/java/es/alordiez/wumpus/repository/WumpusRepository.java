package es.alordiez.wumpus.repository;

import es.alordiez.wumpus.domain.Wumpus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Wumpus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WumpusRepository extends JpaRepository<Wumpus, Long> {

}
