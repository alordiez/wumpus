package es.alordiez.wumpus.service;

import es.alordiez.wumpus.service.dto.WumpusDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Wumpus.
 */
public interface WumpusService {

    /**
     * Save a wumpus.
     *
     * @param wumpusDTO the entity to save
     * @return the persisted entity
     */
    WumpusDTO save(WumpusDTO wumpusDTO);

    /**
     * Get all the wumpuses.
     *
     * @return the list of entities
     */
    List<WumpusDTO> findAll();
    /**
     * Get all the WumpusDTO where Game is null.
     *
     * @return the list of entities
     */
    List<WumpusDTO> findAllWhereGameIsNull();


    /**
     * Get the "id" wumpus.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<WumpusDTO> findOne(Long id);

    /**
     * Delete the "id" wumpus.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
