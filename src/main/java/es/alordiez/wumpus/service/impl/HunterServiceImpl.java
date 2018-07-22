package es.alordiez.wumpus.service.impl;

import es.alordiez.wumpus.service.HunterService;
import es.alordiez.wumpus.domain.Hunter;
import es.alordiez.wumpus.repository.HunterRepository;
import es.alordiez.wumpus.service.dto.HunterDTO;
import es.alordiez.wumpus.service.mapper.HunterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service Implementation for managing Hunter.
 */
@Service
@Transactional
public class HunterServiceImpl implements HunterService {

    private final Logger log = LoggerFactory.getLogger(HunterServiceImpl.class);

    private final HunterRepository hunterRepository;

    private final HunterMapper hunterMapper;

    public HunterServiceImpl(HunterRepository hunterRepository, HunterMapper hunterMapper) {
        this.hunterRepository = hunterRepository;
        this.hunterMapper = hunterMapper;
    }

    /**
     * Save a hunter.
     *
     * @param hunterDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public HunterDTO save(HunterDTO hunterDTO) {
        log.debug("Request to save Hunter : {}", hunterDTO);
        Hunter hunter = hunterMapper.toEntity(hunterDTO);
        hunter = hunterRepository.save(hunter);
        return hunterMapper.toDto(hunter);
    }

    /**
     * Get all the hunters.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<HunterDTO> findAll() {
        log.debug("Request to get all Hunters");
        return hunterRepository.findAll().stream()
            .map(hunterMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one hunter by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<HunterDTO> findOne(Long id) {
        log.debug("Request to get Hunter : {}", id);
        return hunterRepository.findById(id)
            .map(hunterMapper::toDto);
    }

    /**
     * Delete the hunter by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Hunter : {}", id);
        hunterRepository.deleteById(id);
    }
}
