package es.alordiez.wumpus.service;

import es.alordiez.wumpus.service.dto.GamePitsDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing GamePits.
 */
public interface GamePitsService {

    /**
     * Save a gamePits.
     *
     * @param gamePitsDTO the entity to save
     * @return the persisted entity
     */
    GamePitsDTO save(GamePitsDTO gamePitsDTO);

    /**
     * Get all the gamePits.
     *
     * @return the list of entities
     */
    List<GamePitsDTO> findAll();


    /**
     * Get the "id" gamePits.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GamePitsDTO> findOne(Long id);

    /**
     * Delete the "id" gamePits.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
