package es.alordiez.wumpus.service.mapper;

import es.alordiez.wumpus.domain.*;
import es.alordiez.wumpus.service.dto.GameDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Game and its DTO GameDTO.
 */
@Mapper(componentModel = "spring", uses = {PlayerMapper.class, WumpusMapper.class})
public interface GameMapper extends EntityMapper<GameDTO, Game> {

    @Mapping(source = "player.id", target = "playerId")
    @Mapping(source = "wumpus.id", target = "wumpusId")
    GameDTO toDto(Game game);

    @Mapping(source = "playerId", target = "player")
    @Mapping(target = "gamePits", ignore = true)
    @Mapping(source = "wumpusId", target = "wumpus")
    Game toEntity(GameDTO gameDTO);

    default Game fromId(Long id) {
        if (id == null) {
            return null;
        }
        Game game = new Game();
        game.setId(id);
        return game;
    }
}
