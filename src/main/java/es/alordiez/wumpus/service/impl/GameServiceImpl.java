package es.alordiez.wumpus.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.ws.http.HTTPException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.alordiez.wumpus.domain.game.Game;
import es.alordiez.wumpus.domain.game.GamePits;
import es.alordiez.wumpus.domain.game.Hunter;
import es.alordiez.wumpus.domain.game.Wumpus;
import es.alordiez.wumpus.repository.GameRepository;
import es.alordiez.wumpus.service.GameService;
import es.alordiez.wumpus.service.dto.GameDTO;
import es.alordiez.wumpus.service.mapper.GameMapper;

/**
 * Service Implementation for managing Game.
 */
@Service
@Transactional
public class GameServiceImpl implements GameService {

	private final Logger log = LoggerFactory.getLogger(GameServiceImpl.class);

	private final GameRepository gameRepository;

	private final GameMapper gameMapper;

	public GameServiceImpl(GameRepository gameRepository, GameMapper gameMapper) {
		this.gameRepository = gameRepository;
		this.gameMapper = gameMapper;
	}

	/**
	 * Save a game.
	 *
	 * @param gameDTO
	 *            the entity to save
	 * @return the persisted entity
	 */
	@Override
	public GameDTO save(GameDTO gameDTO) {
		log.debug("Request to save Game : {}", gameDTO);
		Game game = gameMapper.toEntity(gameDTO);
		game = initilizeGame(game);
		game = gameRepository.save(game);
		return gameMapper.toDto(game);
	}

	public Game initilizeGame(Game game) {
		Hunter hunter = new Hunter();
		hunter = hunter.game(game).position(0).isAlive(true);
		game.setHunter(hunter);

		int nextPosition = game.getRandomFreeField();

		Wumpus wumpus = new Wumpus();
		wumpus = wumpus.game(game).position(nextPosition).isAlive(true);
		game.setWumpus(wumpus);

		nextPosition = game.getRandomFreeField();
		game.setGoldPosition(nextPosition);

		for (int i = 0; i < game.getPitNumber(); i++) {
			nextPosition = game.getRandomFreeField();
			if (nextPosition < 0) {
				throw new HTTPException(HttpStatus.BAD_REQUEST.value());
			}
			GamePits pit = new GamePits();
			pit.game(game).position(nextPosition);
			game.addGamePits(pit);
		}
		game.setMovements(new ArrayList<>());
		return game;
	}

	/**
	 * Get all the games.
	 *
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public List<GameDTO> findAll() {
		log.debug("Request to get all Games");
		return gameRepository.findAll().stream().map(gameMapper::toDto)
				.collect(Collectors.toCollection(LinkedList::new));
	}

	/**
	 * Get one game by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public Optional<GameDTO> findOne(Long id) {
		log.debug("Request to get Game : {}", id);
		return gameRepository.findById(id).map(gameMapper::toDto);
	}

	/**
	 * Delete the game by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	@Override
	public void delete(Long id) {
		log.debug("Request to delete Game : {}", id);
		gameRepository.deleteById(id);
	}
}
