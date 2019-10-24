package com.illud.web.rest;
import com.illud.service.DrivoDataService;
import com.illud.web.rest.errors.BadRequestAlertException;
import com.illud.web.rest.util.HeaderUtil;
import com.illud.web.rest.util.PaginationUtil;
import com.illud.service.dto.DrivoDataDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing DrivoData.
 */
@RestController
@RequestMapping("/api")
public class DrivoDataResource {

    private final Logger log = LoggerFactory.getLogger(DrivoDataResource.class);

    private static final String ENTITY_NAME = "batchDrivoData";

    private final DrivoDataService drivoDataService;

    public DrivoDataResource(DrivoDataService drivoDataService) {
        this.drivoDataService = drivoDataService;
    }

    /**
     * POST  /drivo-data : Create a new drivoData.
     *
     * @param drivoDataDTO the drivoDataDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new drivoDataDTO, or with status 400 (Bad Request) if the drivoData has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/drivo-data")
    public ResponseEntity<DrivoDataDTO> createDrivoData(@RequestBody DrivoDataDTO drivoDataDTO) throws URISyntaxException {
        log.debug("REST request to save DrivoData : {}", drivoDataDTO);
        if (drivoDataDTO.getId() != null) {
            throw new BadRequestAlertException("A new drivoData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DrivoDataDTO result = drivoDataService.save(drivoDataDTO);
        return ResponseEntity.created(new URI("/api/drivo-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /drivo-data : Updates an existing drivoData.
     *
     * @param drivoDataDTO the drivoDataDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated drivoDataDTO,
     * or with status 400 (Bad Request) if the drivoDataDTO is not valid,
     * or with status 500 (Internal Server Error) if the drivoDataDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/drivo-data")
    public ResponseEntity<DrivoDataDTO> updateDrivoData(@RequestBody DrivoDataDTO drivoDataDTO) throws URISyntaxException {
        log.debug("REST request to update DrivoData : {}", drivoDataDTO);
        if (drivoDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DrivoDataDTO result = drivoDataService.save(drivoDataDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, drivoDataDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /drivo-data : get all the drivoData.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of drivoData in body
     */
    @GetMapping("/drivo-data")
    public ResponseEntity<List<DrivoDataDTO>> getAllDrivoData(Pageable pageable) {
        log.debug("REST request to get a page of DrivoData");
        Page<DrivoDataDTO> page = drivoDataService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/drivo-data");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /drivo-data/:id : get the "id" drivoData.
     *
     * @param id the id of the drivoDataDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the drivoDataDTO, or with status 404 (Not Found)
     */
    @GetMapping("/drivo-data/{id}")
    public ResponseEntity<DrivoDataDTO> getDrivoData(@PathVariable Long id) {
        log.debug("REST request to get DrivoData : {}", id);
        Optional<DrivoDataDTO> drivoDataDTO = drivoDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(drivoDataDTO);
    }

    /**
     * DELETE  /drivo-data/:id : delete the "id" drivoData.
     *
     * @param id the id of the drivoDataDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/drivo-data/{id}")
    public ResponseEntity<Void> deleteDrivoData(@PathVariable Long id) {
        log.debug("REST request to delete DrivoData : {}", id);
        drivoDataService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/drivo-data?query=:query : search for the drivoData corresponding
     * to the query.
     *
     * @param query the query of the drivoData search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/drivo-data")
    public ResponseEntity<List<DrivoDataDTO>> searchDrivoData(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DrivoData for query {}", query);
        Page<DrivoDataDTO> page = drivoDataService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/drivo-data");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
