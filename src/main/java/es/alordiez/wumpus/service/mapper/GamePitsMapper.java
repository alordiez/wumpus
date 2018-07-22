package es.alordiez.wumpus.service.mapper;

import es.alordiez.wumpus.domain.*;
import es.alordiez.wumpus.service.dto.GamePitsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity GamePits and its DTO GamePitsDTO.
 */
@Mapper(componentModel = "spring", uses = {GameMapper.class})
public interface GamePitsMapper extends EntityMapper<GamePitsDTO, GamePits> {

    @Mapping(source = "game.id", target = "gameId")
    GamePitsDTO toDto(GamePits gamePits);

    @Mapping(source = "gameId", target = "game")
    GamePits toEntity(GamePitsDTO gamePitsDTO);

    default GamePits fromId(Long id) {
        if (id == null) {
            return null;
        }
        GamePits gamePits = new GamePits();
        gamePits.setId(id);
        return gamePits;
    }
}
