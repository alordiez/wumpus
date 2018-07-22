package es.alordiez.wumpus.service.mapper;

import es.alordiez.wumpus.domain.*;
import es.alordiez.wumpus.service.dto.WumpusDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Wumpus and its DTO WumpusDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WumpusMapper extends EntityMapper<WumpusDTO, Wumpus> {



    default Wumpus fromId(Long id) {
        if (id == null) {
            return null;
        }
        Wumpus wumpus = new Wumpus();
        wumpus.setId(id);
        return wumpus;
    }
}
