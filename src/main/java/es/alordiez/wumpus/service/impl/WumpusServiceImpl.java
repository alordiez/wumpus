package es.alordiez.wumpus.service.impl;

import es.alordiez.wumpus.service.WumpusService;
import es.alordiez.wumpus.domain.Wumpus;
import es.alordiez.wumpus.repository.WumpusRepository;
import es.alordiez.wumpus.service.dto.WumpusDTO;
import es.alordiez.wumpus.service.mapper.WumpusMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
/**
 * Service Implementation for managing Wumpus.
 */
@Service
@Transactional
public class WumpusServiceImpl implements WumpusService {

    private final Logger log = LoggerFactory.getLogger(WumpusServiceImpl.class);

    private final WumpusRepository wumpusRepository;

    private final WumpusMapper wumpusMapper;

    public WumpusServiceImpl(WumpusRepository wumpusRepository, WumpusMapper wumpusMapper) {
        this.wumpusRepository = wumpusRepository;
        this.wumpusMapper = wumpusMapper;
    }

    /**
     * Save a wumpus.
     *
     * @param wumpusDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public WumpusDTO save(WumpusDTO wumpusDTO) {
        log.debug("Request to save Wumpus : {}", wumpusDTO);
        Wumpus wumpus = wumpusMapper.toEntity(wumpusDTO);
        wumpus = wumpusRepository.save(wumpus);
        return wumpusMapper.toDto(wumpus);
    }

    /**
     * Get all the wumpuses.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<WumpusDTO> findAll() {
        log.debug("Request to get all Wumpuses");
        return wumpusRepository.findAll().stream()
            .map(wumpusMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
     *  get all the wumpuses where Game is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<WumpusDTO> findAllWhereGameIsNull() {
        log.debug("Request to get all wumpuses where Game is null");
        return StreamSupport
            .stream(wumpusRepository.findAll().spliterator(), false)
            .filter(wumpus -> wumpus.getGame() == null)
            .map(wumpusMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one wumpus by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<WumpusDTO> findOne(Long id) {
        log.debug("Request to get Wumpus : {}", id);
        return wumpusRepository.findById(id)
            .map(wumpusMapper::toDto);
    }

    /**
     * Delete the wumpus by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Wumpus : {}", id);
        wumpusRepository.deleteById(id);
    }
}
