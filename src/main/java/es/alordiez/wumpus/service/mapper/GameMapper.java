package es.alordiez.wumpus.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.alordiez.wumpus.domain.game.Game;
import es.alordiez.wumpus.service.dto.GameDTO;

/**
 * Mapper for the entity Game and its DTO GameDTO.
 */
@Mapper(componentModel = "spring", uses = {WumpusMapper.class, HunterMapper.class})
public interface GameMapper extends EntityMapper<GameDTO, Game> {

    GameDTO toDto(Game game);

    @Mapping(target = "gamePits", ignore = true)
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
