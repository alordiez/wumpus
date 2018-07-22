package es.alordiez.wumpus.repository;

import es.alordiez.wumpus.domain.Hunter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Hunter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HunterRepository extends JpaRepository<Hunter, Long> {

}
