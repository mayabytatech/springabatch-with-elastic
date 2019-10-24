package com.illud.service;

import com.illud.service.dto.DrivoDataDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing DrivoData.
 */
public interface DrivoDataService {

    /**
     * Save a drivoData.
     *
     * @param drivoDataDTO the entity to save
     * @return the persisted entity
     */
    DrivoDataDTO save(DrivoDataDTO drivoDataDTO);

    /**
     * Get all the drivoData.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DrivoDataDTO> findAll(Pageable pageable);


    /**
     * Get the "id" drivoData.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DrivoDataDTO> findOne(Long id);

    /**
     * Delete the "id" drivoData.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the drivoData corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DrivoDataDTO> search(String query, Pageable pageable);
}
