package es.alordiez.wumpus.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.alordiez.wumpus.service.GamePitsService;
import es.alordiez.wumpus.web.rest.errors.BadRequestAlertException;
import es.alordiez.wumpus.web.rest.util.HeaderUtil;
import es.alordiez.wumpus.service.dto.GamePitsDTO;
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
 * REST controller for managing GamePits.
 */
@RestController
@RequestMapping("/api")
public class GamePitsResource {

    private final Logger log = LoggerFactory.getLogger(GamePitsResource.class);

    private static final String ENTITY_NAME = "gamePits";

    private final GamePitsService gamePitsService;

    public GamePitsResource(GamePitsService gamePitsService) {
        this.gamePitsService = gamePitsService;
    }

    /**
     * POST  /game-pits : Create a new gamePits.
     *
     * @param gamePitsDTO the gamePitsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gamePitsDTO, or with status 400 (Bad Request) if the gamePits has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/game-pits")
    @Timed
    public ResponseEntity<GamePitsDTO> createGamePits(@Valid @RequestBody GamePitsDTO gamePitsDTO) throws URISyntaxException {
        log.debug("REST request to save GamePits : {}", gamePitsDTO);
        if (gamePitsDTO.getId() != null) {
            throw new BadRequestAlertException("A new gamePits cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GamePitsDTO result = gamePitsService.save(gamePitsDTO);
        return ResponseEntity.created(new URI("/api/game-pits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /game-pits : Updates an existing gamePits.
     *
     * @param gamePitsDTO the gamePitsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gamePitsDTO,
     * or with status 400 (Bad Request) if the gamePitsDTO is not valid,
     * or with status 500 (Internal Server Error) if the gamePitsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/game-pits")
    @Timed
    public ResponseEntity<GamePitsDTO> updateGamePits(@Valid @RequestBody GamePitsDTO gamePitsDTO) throws URISyntaxException {
        log.debug("REST request to update GamePits : {}", gamePitsDTO);
        if (gamePitsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GamePitsDTO result = gamePitsService.save(gamePitsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gamePitsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /game-pits : get all the gamePits.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of gamePits in body
     */
    @GetMapping("/game-pits")
    @Timed
    public List<GamePitsDTO> getAllGamePits() {
        log.debug("REST request to get all GamePits");
        return gamePitsService.findAll();
    }

    /**
     * GET  /game-pits/:id : get the "id" gamePits.
     *
     * @param id the id of the gamePitsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gamePitsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/game-pits/{id}")
    @Timed
    public ResponseEntity<GamePitsDTO> getGamePits(@PathVariable Long id) {
        log.debug("REST request to get GamePits : {}", id);
        Optional<GamePitsDTO> gamePitsDTO = gamePitsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gamePitsDTO);
    }

    /**
     * DELETE  /game-pits/:id : delete the "id" gamePits.
     *
     * @param id the id of the gamePitsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/game-pits/{id}")
    @Timed
    public ResponseEntity<Void> deleteGamePits(@PathVariable Long id) {
        log.debug("REST request to delete GamePits : {}", id);
        gamePitsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
