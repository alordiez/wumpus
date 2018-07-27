package es.alordiez.wumpus.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.ws.http.HTTPException;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.alordiez.wumpus.domain.game.Direction;
import es.alordiez.wumpus.domain.game.Element;
import es.alordiez.wumpus.domain.game.Game;
import es.alordiez.wumpus.domain.game.GamePits;
import es.alordiez.wumpus.domain.game.Hunter;
import es.alordiez.wumpus.domain.game.Perception;
import es.alordiez.wumpus.domain.game.Wumpus;
import es.alordiez.wumpus.repository.GameRepository;
import es.alordiez.wumpus.service.GameService;
import es.alordiez.wumpus.service.dto.FieldDTO;
import es.alordiez.wumpus.service.dto.GameDTO;
import es.alordiez.wumpus.service.mapper.GameMapper;
import es.alordiez.wumpus.service.util.Position2D;
import es.alordiez.wumpus.service.util.PositioningUtils;
import es.alordiez.wumpus.web.rest.errors.CustomHttpException;

/**
 * Service Implementation for managing Game.
 */
@Service
@Transactional
public class GameServiceImpl implements GameService {

	private static final String GAME_NOT_STARTED_MESSAGE = "Game with ID: {0} is not started, please start it before manipulate it.";
	private static final String GAME_ALREADY_STARTED_MESSAGE = "Game with ID: {0} is already started, please try with other one, restart or finish it.";
	private static final String GAME_NOT_EXISTS = "Game doesn't exist with ID: {0}";
	private static final String INVALID_DIRECTION = "Direction '{0}' is not a valid. Accepted values are 'N','S','E','W'";
	private static final String INSUFFICIENT_ARROWS = "Game with ID: {0} doesn't have arrows to shoot";

	private final Logger log = LoggerFactory.getLogger(GameServiceImpl.class);

	@Autowired
	private GameRepository gameRepository;

	public GameServiceImpl() {
		// DEFAULT EMTPY CONSTRUCTOR
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
		Game game = Mappers.getMapper(GameMapper.class).toEntity(gameDTO);
		game = initilizeGame(game);
		game = gameRepository.save(game);
		return Mappers.getMapper(GameMapper.class).toDto(game);
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
		return gameRepository.findAll().stream().map(Mappers.getMapper(GameMapper.class)::toDto)
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
		return gameRepository.findById(id).map(Mappers.getMapper(GameMapper.class)::toDto);
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

	/**
	 * Starts or re-starts a game
	 * 
	 * @param id
	 * @param restart
	 * @return
	 */
	@Override
	public GameDTO startGame(Long id, Boolean restart) {
		Optional<Game> game = gameRepository.findById(id);
		Game selectedGame;
		if (!game.isPresent()) {
			throw new CustomHttpException(HttpStatus.NOT_FOUND, MessageFormat.format(GAME_NOT_EXISTS, id));
		}
		restart = restart == null ? Boolean.FALSE : restart;
		if (game.get().isStarted() && !restart) {
			throw new CustomHttpException(HttpStatus.BAD_REQUEST,
					MessageFormat.format(GAME_ALREADY_STARTED_MESSAGE, id));
		}

		selectedGame = game.get();
		if (restart) {
			selectedGame = initilizeGame(selectedGame);
		}
		selectedGame.setStarted(true);
		gameRepository.save(selectedGame);

		return mapToDto(selectedGame);
	}

	@Override
	public void endGame(Long id) {
		Game selectedGame = findAndCheckStartedGame(id);
		selectedGame.setStarted(false);
		selectedGame.setMovements(new ArrayList<>());
		selectedGame.setUsedArrows(0);
		gameRepository.save(selectedGame);
	}

	/**
	 * Initializes Game Element's positions Hunter at 0 Wumpus, Gold and Pits in
	 * random positions
	 * 
	 * @param game
	 * @return
	 */
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
		game.setStarted(false);
		game.setUsedArrows(0);
		return game;
	}

	/**
	 * Initializes a board with game specs (wumpus, pits, gold, hunter and
	 * perceptions)
	 * 
	 * @param game
	 * @return
	 */
	public HashMap<Integer, HashMap<Integer, FieldDTO>> initializeBoard(Game game) {
		HashMap<Integer, HashMap<Integer, FieldDTO>> board = new HashMap<>();
		for (int y = 0; y < game.getHeight(); y++) {
			HashMap<Integer, FieldDTO> row = new HashMap<>();
			for (int x = 0; x < game.getWidth(); x++) {
				int position = PositioningUtils.getLinearPosition(x, y, game.getWidth());
				FieldDTO field = new FieldDTO(position);
				if (game.getMovements().contains(position)) {
					field.setVisited(true);
				}
				Element element = getElement(game, position);
				if (element != null) {
					field.addElement(element);
				}
				if (element != null && element.equals(Element.GOLD)) {
					field.addPerception(Perception.GLITTER);
				}
				if (element != null && element.equals(Element.HUNTER)) {
					field.setVisited(true);
				}
				row.put(x, field);
			}
			board.put(y, row);
		}

		return updatePerceptions(board, game.getWidth(), game.getHeight());
	}

	/**
	 * Updates given board with perceptions on each field
	 * 
	 * @param board
	 * @param width
	 * @param height
	 * @return
	 */
	private HashMap<Integer, HashMap<Integer, FieldDTO>> updatePerceptions(
			HashMap<Integer, HashMap<Integer, FieldDTO>> board, int width, int height) {
		for (int y = 0; y < board.size(); y++) {
			HashMap<Integer, FieldDTO> row = board.get(y);
			for (int x = 0; x < row.size(); x++) {
				FieldDTO field = row.get(x);
				Position2D[] neighbors = PositioningUtils.getNeighbors(new Position2D(x, y), width, height);
				Position2D position = neighbors[0];
				if (position != null) {
					field.addPerception(getNeighborPerceptions(board.get(position.y).get(position.x)));
				}
				position = neighbors[1];
				if (position != null) {
					field.addPerception(getNeighborPerceptions(board.get(position.y).get(position.x)));
				}
				position = neighbors[2];
				if (position != null) {
					field.addPerception(getNeighborPerceptions(board.get(position.y).get(position.x)));
				}
				position = neighbors[3];
				if (position != null) {
					field.addPerception(getNeighborPerceptions(board.get(position.y).get(position.x)));
				}
			}
		}
		return board;
	}

	/**
	 * Given a field, returns perceptions should appear on neighbor fields
	 * 
	 * @param neighbor
	 * @return
	 */
	private Perception getNeighborPerceptions(FieldDTO neighbor) {
		Element element = neighbor.getSingleElement();
		if (element == null) {
			return null;
		}

		switch (element) {
		case PIT:
			return Perception.BREEZE;
		case WUMPUS:
			return Perception.STENCH;
		default:
			return null;
		}

	}

	/**
	 * Retrieves the @Element linked to a given position
	 * 
	 * @param game
	 * @param position
	 * @return
	 */
	private Element getElement(Game game, int position) {
		if (position == game.getGoldPosition()) {
			return Element.GOLD;
		}
		if (position == game.getHunter().getPosition()) {
			return Element.HUNTER;
		}
		if (position == game.getWumpus().getPosition()) {
			return Element.WUMPUS;
		}
		for (GamePits pit : game.getGamePits()) {
			if (position == pit.getPosition()) {
				return Element.PIT;
			}
		}
		return null;
	}

	@Override
	public GameDTO movePlayer(Long id, String direction) {

		Direction repDirection = Direction.fromCode(direction);
		if (repDirection == null) {
			throw new CustomHttpException(HttpStatus.BAD_REQUEST, MessageFormat.format(INVALID_DIRECTION, direction));
		}

		Game selectedGame = findAndCheckStartedGame(id);

		Integer currentPosition = selectedGame.getHunter().getPosition();
		Integer newPosition = PositioningUtils.getNewPosition(currentPosition, repDirection, selectedGame.getWidth(),
				selectedGame.getHeight());

		if (newPosition != null && newPosition > 0) {
			Element newPositionElement = getElement(selectedGame, newPosition);
			selectedGame.moveHunter(newPosition);
			if (newPositionElement != null && (newPositionElement.compareTo(Element.PIT) == 0
					||( newPositionElement.compareTo(Element.WUMPUS) == 0) && selectedGame.getWumpus().isIsAlive())) {
				selectedGame.killHunter();
			}
			gameRepository.save(selectedGame);
		}

		return mapToDto(selectedGame);
	}

	public Game findAndCheckStartedGame(Long id) {
		Optional<Game> game = gameRepository.findById(id);
		if (!game.isPresent()) {
			throw new CustomHttpException(HttpStatus.NOT_FOUND, MessageFormat.format(GAME_NOT_EXISTS, id));
		}

		if (!game.get().isStarted()) {
			throw new CustomHttpException(HttpStatus.BAD_REQUEST, MessageFormat.format(GAME_NOT_STARTED_MESSAGE, id));
		}

		return game.get();
	}

	@Override
	public GameDTO shootWumpus(Long id, String direction) {

		Direction repDirection = Direction.fromCode(direction);
		if (repDirection == null) {
			throw new CustomHttpException(HttpStatus.BAD_REQUEST, MessageFormat.format(INVALID_DIRECTION, direction));
		}

		Game selectedGame = findAndCheckStartedGame(id);

		if (selectedGame.getUsedArrows() >= selectedGame.getArrows()) {
			throw new CustomHttpException(HttpStatus.BAD_REQUEST, MessageFormat.format(INSUFFICIENT_ARROWS, id));
		}

		Integer currentPosition = selectedGame.getHunter().getPosition();
		Integer positionToShoot = PositioningUtils.getNewPosition(currentPosition, repDirection,
				selectedGame.getWidth(), selectedGame.getHeight());

		if (positionToShoot != null && positionToShoot > 0) {
			Element newPositionElement = getElement(selectedGame, positionToShoot);
			if (newPositionElement != null && newPositionElement.compareTo(Element.WUMPUS) == 0) {
				selectedGame.killWumpus();
			}
		}
		selectedGame.minusArrows();
		gameRepository.save(selectedGame);

		return mapToDto(selectedGame);
	}

	private GameDTO mapToDto(Game game) {
		GameDTO gameDto = Mappers.getMapper(GameMapper.class).toDto(game);
		gameDto.setArrows(game.getArrows() - game.getUsedArrows());
		gameDto.setBoard(initializeBoard(game));
		gameDto.setHunterAlive(game.getHunter().isIsAlive());
		gameDto.setGameover(!game.getHunter().isIsAlive());
		gameDto.setWumpusAlive(game.getWumpus().isIsAlive());
		gameDto.setGoldObtained(game.getMovements().contains(game.getGoldPosition()));
		gameDto.setWin(gameDto.getGoldObtained() && !game.getWumpus().isIsAlive());
		return gameDto;
	}
}
