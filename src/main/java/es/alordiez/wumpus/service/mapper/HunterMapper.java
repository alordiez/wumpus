package es.alordiez.wumpus.service.mapper;

import es.alordiez.wumpus.domain.*;
import es.alordiez.wumpus.service.dto.HunterDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Hunter and its DTO HunterDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HunterMapper extends EntityMapper<HunterDTO, Hunter> {


    @Mapping(target = "game", ignore = true)
    Hunter toEntity(HunterDTO hunterDTO);

    default Hunter fromId(Long id) {
        if (id == null) {
            return null;
        }
        Hunter hunter = new Hunter();
        hunter.setId(id);
        return hunter;
    }
}
