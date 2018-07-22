package es.alordiez.wumpus.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.alordiez.wumpus.service.HunterService;
import es.alordiez.wumpus.web.rest.errors.BadRequestAlertException;
import es.alordiez.wumpus.web.rest.util.HeaderUtil;
import es.alordiez.wumpus.service.dto.HunterDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Hunter.
 */
@RestController
@RequestMapping("/api")
public class HunterResource {

    private final Logger log = LoggerFactory.getLogger(HunterResource.class);

    private static final String ENTITY_NAME = "hunter";

    private final HunterService hunterService;

    public HunterResource(HunterService hunterService) {
        this.hunterService = hunterService;
    }

    /**
     * POST  /hunters : Create a new hunter.
     *
     * @param hunterDTO the hunterDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hunterDTO, or with status 400 (Bad Request) if the hunter has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/hunters")
    @Timed
    public ResponseEntity<HunterDTO> createHunter(@Valid @RequestBody HunterDTO hunterDTO) throws URISyntaxException {
        log.debug("REST request to save Hunter : {}", hunterDTO);
        if (hunterDTO.getId() != null) {
            throw new BadRequestAlertException("A new hunter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HunterDTO result = hunterService.save(hunterDTO);
        return ResponseEntity.created(new URI("/api/hunters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hunters : Updates an existing hunter.
     *
     * @param hunterDTO the hunterDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hunterDTO,
     * or with status 400 (Bad Request) if the hunterDTO is not valid,
     * or with status 500 (Internal Server Error) if the hunterDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/hunters")
    @Timed
    public ResponseEntity<HunterDTO> updateHunter(@Valid @RequestBody HunterDTO hunterDTO) throws URISyntaxException {
        log.debug("REST request to update Hunter : {}", hunterDTO);
        if (hunterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HunterDTO result = hunterService.save(hunterDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, hunterDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hunters : get all the hunters.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of hunters in body
     */
    @GetMapping("/hunters")
    @Timed
    public List<HunterDTO> getAllHunters() {
        log.debug("REST request to get all Hunters");
        return hunterService.findAll();
    }

    /**
     * GET  /hunters/:id : get the "id" hunter.
     *
     * @param id the id of the hunterDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hunterDTO, or with status 404 (Not Found)
     */
    @GetMapping("/hunters/{id}")
    @Timed
    public ResponseEntity<HunterDTO> getHunter(@PathVariable Long id) {
        log.debug("REST request to get Hunter : {}", id);
        Optional<HunterDTO> hunterDTO = hunterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hunterDTO);
    }

    /**
     * DELETE  /hunters/:id : delete the "id" hunter.
     *
     * @param id the id of the hunterDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/hunters/{id}")
    @Timed
    public ResponseEntity<Void> deleteHunter(@PathVariable Long id) {
        log.debug("REST request to delete Hunter : {}", id);
        hunterService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
