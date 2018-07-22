package es.alordiez.wumpus.service.mapper;

import es.alordiez.wumpus.domain.*;
import es.alordiez.wumpus.service.dto.GamePitsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity GamePits and its DTO GamePitsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GamePitsMapper extends EntityMapper<GamePitsDTO, GamePits> {



    default GamePits fromId(Long id) {
        if (id == null) {
            return null;
        }
        GamePits gamePits = new GamePits();
        gamePits.setId(id);
        return gamePits;
    }
}
