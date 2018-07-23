package es.alordiez.wumpus.service;

import es.alordiez.wumpus.service.dto.GameDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Game.
 */
public interface GameService {

    /**
     * Save a game.
     *
     * @param gameDTO the entity to save
     * @return the persisted entity
     */
    GameDTO save(GameDTO gameDTO);

    /**
     * Get all the games.
     *
     * @return the list of entities
     */
    List<GameDTO> findAll();


    /**
     * Get the "id" game.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GameDTO> findOne(Long id);

    /**
     * Delete the "id" game.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Starts or re-starts a game
     * @param id
     * @param restart
     * @return
     */
    GameDTO startGame(Long id, Boolean restart);
}
