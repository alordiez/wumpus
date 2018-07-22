package es.alordiez.wumpus.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.alordiez.wumpus.service.WumpusService;
import es.alordiez.wumpus.web.rest.errors.BadRequestAlertException;
import es.alordiez.wumpus.web.rest.util.HeaderUtil;
import es.alordiez.wumpus.service.dto.WumpusDTO;
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
 * REST controller for managing Wumpus.
 */
@RestController
@RequestMapping("/api")
public class WumpusResource {

    private final Logger log = LoggerFactory.getLogger(WumpusResource.class);

    private static final String ENTITY_NAME = "wumpus";

    private final WumpusService wumpusService;

    public WumpusResource(WumpusService wumpusService) {
        this.wumpusService = wumpusService;
    }

    /**
     * POST  /wumpuses : Create a new wumpus.
     *
     * @param wumpusDTO the wumpusDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wumpusDTO, or with status 400 (Bad Request) if the wumpus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wumpuses")
    @Timed
    public ResponseEntity<WumpusDTO> createWumpus(@Valid @RequestBody WumpusDTO wumpusDTO) throws URISyntaxException {
        log.debug("REST request to save Wumpus : {}", wumpusDTO);
        if (wumpusDTO.getId() != null) {
            throw new BadRequestAlertException("A new wumpus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WumpusDTO result = wumpusService.save(wumpusDTO);
        return ResponseEntity.created(new URI("/api/wumpuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wumpuses : Updates an existing wumpus.
     *
     * @param wumpusDTO the wumpusDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wumpusDTO,
     * or with status 400 (Bad Request) if the wumpusDTO is not valid,
     * or with status 500 (Internal Server Error) if the wumpusDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wumpuses")
    @Timed
    public ResponseEntity<WumpusDTO> updateWumpus(@Valid @RequestBody WumpusDTO wumpusDTO) throws URISyntaxException {
        log.debug("REST request to update Wumpus : {}", wumpusDTO);
        if (wumpusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WumpusDTO result = wumpusService.save(wumpusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wumpusDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wumpuses : get all the wumpuses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of wumpuses in body
     */
    @GetMapping("/wumpuses")
    @Timed
    public List<WumpusDTO> getAllWumpuses() {
        log.debug("REST request to get all Wumpuses");
        return wumpusService.findAll();
    }

    /**
     * GET  /wumpuses/:id : get the "id" wumpus.
     *
     * @param id the id of the wumpusDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wumpusDTO, or with status 404 (Not Found)
     */
    @GetMapping("/wumpuses/{id}")
    @Timed
    public ResponseEntity<WumpusDTO> getWumpus(@PathVariable Long id) {
        log.debug("REST request to get Wumpus : {}", id);
        Optional<WumpusDTO> wumpusDTO = wumpusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wumpusDTO);
    }

    /**
     * DELETE  /wumpuses/:id : delete the "id" wumpus.
     *
     * @param id the id of the wumpusDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wumpuses/{id}")
    @Timed
    public ResponseEntity<Void> deleteWumpus(@PathVariable Long id) {
        log.debug("REST request to delete Wumpus : {}", id);
        wumpusService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
