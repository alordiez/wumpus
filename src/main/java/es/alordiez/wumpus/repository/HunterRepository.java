package es.alordiez.wumpus.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import es.alordiez.wumpus.domain.game.Hunter;


/**
 * Spring Data  repository for the Hunter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HunterRepository extends JpaRepository<Hunter, Long> {

}
