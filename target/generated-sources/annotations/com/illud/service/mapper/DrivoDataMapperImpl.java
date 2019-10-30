package com.illud.service.mapper;

import com.illud.domain.DrivoData;
import com.illud.service.dto.DrivoDataDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-10-24T12:50:17+0530",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_222 (Private Build)"
)
@Component
public class DrivoDataMapperImpl implements DrivoDataMapper {

    @Override
    public DrivoData toEntity(DrivoDataDTO dto) {
        if ( dto == null ) {
            return null;
        }

        DrivoData drivoData = new DrivoData();

        drivoData.setId( dto.getId() );
        drivoData.setRegNo( dto.getRegNo() );
        drivoData.setOwnerName( dto.getOwnerName() );
        drivoData.setMobileNo( dto.getMobileNo() );
        drivoData.setVehdecscr( dto.getVehdecscr() );

        return drivoData;
    }

    @Override
    public DrivoDataDTO toDto(DrivoData entity) {
        if ( entity == null ) {
            return null;
        }

        DrivoDataDTO drivoDataDTO = new DrivoDataDTO();

        drivoDataDTO.setId( entity.getId() );
        drivoDataDTO.setRegNo( entity.getRegNo() );
        drivoDataDTO.setOwnerName( entity.getOwnerName() );
        drivoDataDTO.setMobileNo( entity.getMobileNo() );
        drivoDataDTO.setVehdecscr( entity.getVehdecscr() );

        return drivoDataDTO;
    }

    @Override
    public List<DrivoData> toEntity(List<DrivoDataDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<DrivoData> list = new ArrayList<DrivoData>( dtoList.size() );
        for ( DrivoDataDTO drivoDataDTO : dtoList ) {
            list.add( toEntity( drivoDataDTO ) );
        }

        return list;
    }

    @Override
    public List<DrivoDataDTO> toDto(List<DrivoData> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<DrivoDataDTO> list = new ArrayList<DrivoDataDTO>( entityList.size() );
        for ( DrivoData drivoData : entityList ) {
            list.add( toDto( drivoData ) );
        }

        return list;
    }
}
