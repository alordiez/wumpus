package es.alordiez.wumpus.service.impl;

import es.alordiez.wumpus.service.GamePitsService;
import es.alordiez.wumpus.domain.game.GamePits;
import es.alordiez.wumpus.repository.GamePitsRepository;
import es.alordiez.wumpus.service.dto.GamePitsDTO;
import es.alordiez.wumpus.service.mapper.GamePitsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service Implementation for managing GamePits.
 */
@Service
@Transactional
public class GamePitsServiceImpl implements GamePitsService {

    private final Logger log = LoggerFactory.getLogger(GamePitsServiceImpl.class);

    private final GamePitsRepository gamePitsRepository;

    private final GamePitsMapper gamePitsMapper;

    public GamePitsServiceImpl(GamePitsRepository gamePitsRepository, GamePitsMapper gamePitsMapper) {
        this.gamePitsRepository = gamePitsRepository;
        this.gamePitsMapper = gamePitsMapper;
    }

    /**
     * Save a gamePits.
     *
     * @param gamePitsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GamePitsDTO save(GamePitsDTO gamePitsDTO) {
        log.debug("Request to save GamePits : {}", gamePitsDTO);
        GamePits gamePits = gamePitsMapper.toEntity(gamePitsDTO);
        gamePits = gamePitsRepository.save(gamePits);
        return gamePitsMapper.toDto(gamePits);
    }

    /**
     * Get all the gamePits.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<GamePitsDTO> findAll() {
        log.debug("Request to get all GamePits");
        return gamePitsRepository.findAll().stream()
            .map(gamePitsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one gamePits by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GamePitsDTO> findOne(Long id) {
        log.debug("Request to get GamePits : {}", id);
        return gamePitsRepository.findById(id)
            .map(gamePitsMapper::toDto);
    }

    /**
     * Delete the gamePits by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete GamePits : {}", id);
        gamePitsRepository.deleteById(id);
    }
}
