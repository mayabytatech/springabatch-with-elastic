package com.illud.service.mapper;

import com.illud.domain.*;
import com.illud.service.dto.DrivoDataDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DrivoData and its DTO DrivoDataDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DrivoDataMapper extends EntityMapper<DrivoDataDTO, DrivoData> {



    default DrivoData fromId(Long id) {
        if (id == null) {
            return null;
        }
        DrivoData drivoData = new DrivoData();
        drivoData.setId(id);
        return drivoData;
    }
}
