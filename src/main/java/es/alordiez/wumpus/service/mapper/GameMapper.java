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

    @Mapping(source = "wumpus.id", target = "wumpusId")
    @Mapping(source = "hunter.id", target = "hunterId")
    GameDTO toDto(Game game);

    @Mapping(target = "gamePits", ignore = true)
    @Mapping(source = "wumpusId", target = "wumpus")
    @Mapping(source = "hunterId", target = "hunter")
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
