package es.alordiez.wumpus.service;

import es.alordiez.wumpus.service.dto.HunterDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Hunter.
 */
public interface HunterService {

    /**
     * Save a hunter.
     *
     * @param hunterDTO the entity to save
     * @return the persisted entity
     */
    HunterDTO save(HunterDTO hunterDTO);

    /**
     * Get all the hunters.
     *
     * @return the list of entities
     */
    List<HunterDTO> findAll();
    /**
     * Get all the HunterDTO where Game is null.
     *
     * @return the list of entities
     */
    List<HunterDTO> findAllWhereGameIsNull();


    /**
     * Get the "id" hunter.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<HunterDTO> findOne(Long id);

    /**
     * Delete the "id" hunter.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
