package es.alordiez.wumpus.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import es.alordiez.wumpus.service.GameService;
import es.alordiez.wumpus.service.dto.GameDTO;
import es.alordiez.wumpus.web.rest.errors.BadRequestAlertException;
import es.alordiez.wumpus.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Game.
 */
@RestController
@RequestMapping("/api")
public class GameResource {

	private final Logger log = LoggerFactory.getLogger(GameResource.class);

	private static final String ENTITY_NAME = "game";

	private final GameService gameService;

	public GameResource(GameService gameService) {
		this.gameService = gameService;
	}

	/**
	 * POST /games : Create a new game.
	 *
	 * @param gameDTO
	 *            the gameDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         gameDTO, or with status 400 (Bad Request) if the game has already an
	 *         ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/games")
	@Timed
	public ResponseEntity<GameDTO> createGame(@Valid @RequestBody GameDTO gameDTO) throws URISyntaxException {
		log.debug("REST request to save Game : {}", gameDTO);
		if (gameDTO.getId() != null) {
			throw new BadRequestAlertException("A new game cannot already have an ID", ENTITY_NAME, "idexists");
		}
		GameDTO result = gameService.save(gameDTO);
		return ResponseEntity.created(new URI("/api/games/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /games : Updates an existing game.
	 *
	 * @param gameDTO
	 *            the gameDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         gameDTO, or with status 400 (Bad Request) if the gameDTO is not
	 *         valid, or with status 500 (Internal Server Error) if the gameDTO
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/games")
	@Timed
	public ResponseEntity<GameDTO> updateGame(@Valid @RequestBody GameDTO gameDTO) throws URISyntaxException {
		log.debug("REST request to update Game : {}", gameDTO);
		if (gameDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		GameDTO result = gameService.save(gameDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gameDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET /games : get all the games.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of games in body
	 */
	@GetMapping("/games")
	@Timed
	public List<GameDTO> getAllGames() {
		log.debug("REST request to get all Games");
		return gameService.findAll();
	}

	/**
	 * GET /games/:id : get the "id" game.
	 *
	 * @param id
	 *            the id of the gameDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the gameDTO, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/games/{id}")
	@Timed
	public ResponseEntity<GameDTO> getGame(@PathVariable Long id) {
		log.debug("REST request to get Game : {}", id);
		Optional<GameDTO> gameDTO = gameService.findOne(id);
		return ResponseUtil.wrapOrNotFound(gameDTO);
	}

	/**
	 * GET /games/:id : get the "id" game.
	 *
	 * @param id
	 *            the id of the gameDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the gameDTO, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/games/{id}/start-game")
	@Timed
	public ResponseEntity<GameDTO> startGame(@PathVariable Long id,
			@RequestParam(value = "restart", required = false) Boolean restart) {
		log.debug("REST request to start Game : {}", id);
		GameDTO gameDTO = gameService.startGame(id, restart);
		return ResponseEntity.ok().body(gameDTO);
	}
	
	/**
	 * GET /games/:id/end-game : get the "id" game.
	 *
	 * @param id
	 *            the id of the gameDTO to end
	 * @return the ResponseEntity with status 200 (OK) and with body the gameDTO, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/games/{id}/end-game")
	@Timed
	public ResponseEntity<Void> endGame(@PathVariable Long id) {
		log.debug("REST request to end Game : {}", id);
		gameService.endGame(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * PATCH /games/:id/move : get the "id" game.
	 *
	 * @param id
	 *            the id of the game to retrieve
	 * @param direction
	 *            where player should be moved
	 * @return the ResponseEntity with status 200 (OK) and with body the game, or
	 *         with status 404 (Not Found)
	 */
	@PatchMapping("/games/{id}/move")
	@Timed
	public ResponseEntity<GameDTO> movePlayer(@PathVariable Long id, @RequestBody String direction) {
		log.debug("REST request to move player on game : {}", id);
		GameDTO game = gameService.movePlayer(id, direction);
		return new ResponseEntity<>(game, HttpStatus.OK);
	}

	/**
	 * PATCH /games/:id/shoot : get the "id" game.
	 *
	 * @param id
	 *            the id of the game to retrieve
	 * @param direction
	 *            where player would shoot
	 * @return the ResponseEntity with status 200 (OK) and with body the game, or
	 *         with status 404 (Not Found)
	 */
	@PatchMapping("/games/{id}/shoot")
//	@RequestMapping(name="/games/{id}/move", method = RequestMethod.PATCH)
	@Timed
	public ResponseEntity<GameDTO> shootWumpus(@PathVariable Long id, @RequestBody String direction) {
		log.debug("REST request to shoot wumpus on game : {}", id);
		GameDTO game = gameService.shootWumpus(id, direction);
		return new ResponseEntity<GameDTO>(game, HttpStatus.OK);
	}

	/**
	 * DELETE /games/:id : delete the "id" game.
	 *
	 * @param id
	 *            the id of the gameDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/games/{id}")
	@Timed
	public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
		log.debug("REST request to delete Game : {}", id);
		gameService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}
}
