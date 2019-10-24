package com.illud.service.impl;

import com.illud.service.DrivoDataService;
import com.illud.domain.DrivoData;
import com.illud.repository.DrivoDataRepository;
import com.illud.repository.search.DrivoDataSearchRepository;
import com.illud.service.dto.DrivoDataDTO;
import com.illud.service.mapper.DrivoDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing DrivoData.
 */
@Service
@Transactional
public class DrivoDataServiceImpl implements DrivoDataService {

    private final Logger log = LoggerFactory.getLogger(DrivoDataServiceImpl.class);

    private final DrivoDataRepository drivoDataRepository;

    private final DrivoDataMapper drivoDataMapper;

    private final DrivoDataSearchRepository drivoDataSearchRepository;

    public DrivoDataServiceImpl(DrivoDataRepository drivoDataRepository, DrivoDataMapper drivoDataMapper, DrivoDataSearchRepository drivoDataSearchRepository) {
        this.drivoDataRepository = drivoDataRepository;
        this.drivoDataMapper = drivoDataMapper;
        this.drivoDataSearchRepository = drivoDataSearchRepository;
    }

    /**
     * Save a drivoData.
     *
     * @param drivoDataDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DrivoDataDTO save(DrivoDataDTO drivoDataDTO) {
        log.debug("Request to save DrivoData : {}", drivoDataDTO);
        DrivoData drivoData = drivoDataMapper.toEntity(drivoDataDTO);
        drivoData = drivoDataRepository.save(drivoData);
        DrivoDataDTO result = drivoDataMapper.toDto(drivoData);
        drivoDataSearchRepository.save(drivoData);
        return result;
    }

    /**
     * Get all the drivoData.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DrivoDataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DrivoData");
        return drivoDataRepository.findAll(pageable)
            .map(drivoDataMapper::toDto);
    }


    /**
     * Get one drivoData by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DrivoDataDTO> findOne(Long id) {
        log.debug("Request to get DrivoData : {}", id);
        return drivoDataRepository.findById(id)
            .map(drivoDataMapper::toDto);
    }

    /**
     * Delete the drivoData by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DrivoData : {}", id);        drivoDataRepository.deleteById(id);
        drivoDataSearchRepository.deleteById(id);
    }

    /**
     * Search for the drivoData corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DrivoDataDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DrivoData for query {}", query);
        return drivoDataSearchRepository.search(queryStringQuery(query), pageable)
            .map(drivoDataMapper::toDto);
    }
}
