package es.alordiez.wumpus.service.impl;

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

/**
 * Service Implementation for managing Game.
 */
@Service
@Transactional
public class GameServiceImpl implements GameService {

	private final Logger log = LoggerFactory.getLogger(GameServiceImpl.class);

	@Autowired
	private GameRepository gameRepository;

//	private final GameMapper gameMapper = Mappers.getMapper(GameMapper.class);

	public GameServiceImpl() {
		//DEFAULT EMTPY CONSTRUCTOR
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
	 * Initializes Game Element's positions
	 * Hunter at 0
	 * Wumpus, Gold and Pits in random positions
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
     * @param id
     * @param restart
     * @return
     */
	@Override
	public GameDTO startGame(Long id, Boolean restart) {
		Optional<Game> game = gameRepository.findById(id);
		Game selectedGame;
		if (!game.isPresent()) {
			throw new HTTPException(HttpStatus.NOT_FOUND.value());
		} 
		restart = restart == null ? Boolean.FALSE : restart;
		selectedGame = game.get();
		if (restart) {
			selectedGame = initilizeGame(selectedGame);
			gameRepository.save(selectedGame);
		}

		GameDTO gameDto = Mappers.getMapper(GameMapper.class).toDto(selectedGame);
		gameDto.setBoard(initializeBoard(selectedGame));
		
		return gameDto;
	}

	/**
	 * Initializes a board with game specs (wumpus, pits, gold, hunter and perceptions)
	 * @param game
	 * @return
	 */
	public HashMap<Integer, HashMap<Integer, FieldDTO>> initializeBoard(Game game) {
		HashMap<Integer, HashMap<Integer, FieldDTO>> board = new HashMap<>();
		for (int x = 0; x < game.getWidth(); x++) {
			HashMap<Integer, FieldDTO> row = new HashMap<>();
			for (int y = 0; y < game.getHeight(); y++) {
				int position = PositioningUtils.getLinearPosition(x, y, game.getWidth());
				FieldDTO field = new FieldDTO(position);
				if(game.getMovements().contains(position)) {
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
				row.put(y, field);
			}
			board.put(x, row);
		}
		
		return updatePerceptions(board, game.getWidth(), game.getHeight());
	}

	/**
	 * Updates given board with perceptions on each field
	 * @param board
	 * @param width
	 * @param height
	 * @return
	 */
	private HashMap<Integer, HashMap<Integer, FieldDTO>> updatePerceptions(HashMap<Integer, HashMap<Integer, FieldDTO>> board,
			int width, int height) {
		for (int x = 0; x < board.size(); x++) {
			HashMap<Integer, FieldDTO> row = board.get(x);
			for (int y = 0; y < row.size(); y++) {
				FieldDTO field = row.get(y);
				Position2D[] neighbors = PositioningUtils.getNeighbors(new Position2D(x, y), width, height);
				Position2D position = neighbors[0];
				if(position != null) {
					field.addPerception(getNeighborPerceptions(board.get(position.x).get(position.y)));
				}
				position = neighbors[1];
				if(position != null) {
					field.addPerception(getNeighborPerceptions(board.get(position.x).get(position.y)));
				}
				position = neighbors[2];
				if(position != null) {
					field.addPerception(getNeighborPerceptions(board.get(position.x).get(position.y)));
				}
				position = neighbors[3];
				if(position != null) {
					field.addPerception(getNeighborPerceptions(board.get(position.x).get(position.y)));
				}
			}
		}
		return board;
	}

	/**
	 * Given a field, returns perceptions should appear on neighbor fields
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
}
