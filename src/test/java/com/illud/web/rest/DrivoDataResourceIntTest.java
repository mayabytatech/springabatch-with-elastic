package com.illud.web.rest;

import com.illud.BatchApp;

import com.illud.domain.DrivoData;
import com.illud.repository.DrivoDataRepository;
import com.illud.repository.search.DrivoDataSearchRepository;
import com.illud.service.DrivoDataService;
import com.illud.service.dto.DrivoDataDTO;
import com.illud.service.mapper.DrivoDataMapper;
import com.illud.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.illud.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DrivoDataResource REST controller.
 *
 * @see DrivoDataResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BatchApp.class)
public class DrivoDataResourceIntTest {

    private static final String DEFAULT_REG_NO = "AAAAAAAAAA";
    private static final String UPDATED_REG_NO = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OWNER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE_NO = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_NO = "BBBBBBBBBB";

    private static final String DEFAULT_VEHDECSCR = "AAAAAAAAAA";
    private static final String UPDATED_VEHDECSCR = "BBBBBBBBBB";

    @Autowired
    private DrivoDataRepository drivoDataRepository;

    @Autowired
    private DrivoDataMapper drivoDataMapper;

    @Autowired
    private DrivoDataService drivoDataService;

    /**
     * This repository is mocked in the com.illud.repository.search test package.
     *
     * @see com.illud.repository.search.DrivoDataSearchRepositoryMockConfiguration
     */
    @Autowired
    private DrivoDataSearchRepository mockDrivoDataSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDrivoDataMockMvc;

    private DrivoData drivoData;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DrivoDataResource drivoDataResource = new DrivoDataResource(drivoDataService);
        this.restDrivoDataMockMvc = MockMvcBuilders.standaloneSetup(drivoDataResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DrivoData createEntity(EntityManager em) {
        DrivoData drivoData = new DrivoData()
            .regNo(DEFAULT_REG_NO)
            .ownerName(DEFAULT_OWNER_NAME)
            .mobileNo(DEFAULT_MOBILE_NO)
            .vehdecscr(DEFAULT_VEHDECSCR);
        return drivoData;
    }

    @Before
    public void initTest() {
        drivoData = createEntity(em);
    }

    @Test
    @Transactional
    public void createDrivoData() throws Exception {
        int databaseSizeBeforeCreate = drivoDataRepository.findAll().size();

        // Create the DrivoData
        DrivoDataDTO drivoDataDTO = drivoDataMapper.toDto(drivoData);
        restDrivoDataMockMvc.perform(post("/api/drivo-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(drivoDataDTO)))
            .andExpect(status().isCreated());

        // Validate the DrivoData in the database
        List<DrivoData> drivoDataList = drivoDataRepository.findAll();
        assertThat(drivoDataList).hasSize(databaseSizeBeforeCreate + 1);
        DrivoData testDrivoData = drivoDataList.get(drivoDataList.size() - 1);
        assertThat(testDrivoData.getRegNo()).isEqualTo(DEFAULT_REG_NO);
        assertThat(testDrivoData.getOwnerName()).isEqualTo(DEFAULT_OWNER_NAME);
        assertThat(testDrivoData.getMobileNo()).isEqualTo(DEFAULT_MOBILE_NO);
        assertThat(testDrivoData.getVehdecscr()).isEqualTo(DEFAULT_VEHDECSCR);

        // Validate the DrivoData in Elasticsearch
        verify(mockDrivoDataSearchRepository, times(1)).save(testDrivoData);
    }

    @Test
    @Transactional
    public void createDrivoDataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = drivoDataRepository.findAll().size();

        // Create the DrivoData with an existing ID
        drivoData.setId(1L);
        DrivoDataDTO drivoDataDTO = drivoDataMapper.toDto(drivoData);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDrivoDataMockMvc.perform(post("/api/drivo-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(drivoDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DrivoData in the database
        List<DrivoData> drivoDataList = drivoDataRepository.findAll();
        assertThat(drivoDataList).hasSize(databaseSizeBeforeCreate);

        // Validate the DrivoData in Elasticsearch
        verify(mockDrivoDataSearchRepository, times(0)).save(drivoData);
    }

    @Test
    @Transactional
    public void getAllDrivoData() throws Exception {
        // Initialize the database
        drivoDataRepository.saveAndFlush(drivoData);

        // Get all the drivoDataList
        restDrivoDataMockMvc.perform(get("/api/drivo-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(drivoData.getId().intValue())))
            .andExpect(jsonPath("$.[*].regNo").value(hasItem(DEFAULT_REG_NO.toString())))
            .andExpect(jsonPath("$.[*].ownerName").value(hasItem(DEFAULT_OWNER_NAME.toString())))
            .andExpect(jsonPath("$.[*].mobileNo").value(hasItem(DEFAULT_MOBILE_NO.toString())))
            .andExpect(jsonPath("$.[*].vehdecscr").value(hasItem(DEFAULT_VEHDECSCR.toString())));
    }
    
    @Test
    @Transactional
    public void getDrivoData() throws Exception {
        // Initialize the database
        drivoDataRepository.saveAndFlush(drivoData);

        // Get the drivoData
        restDrivoDataMockMvc.perform(get("/api/drivo-data/{id}", drivoData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(drivoData.getId().intValue()))
            .andExpect(jsonPath("$.regNo").value(DEFAULT_REG_NO.toString()))
            .andExpect(jsonPath("$.ownerName").value(DEFAULT_OWNER_NAME.toString()))
            .andExpect(jsonPath("$.mobileNo").value(DEFAULT_MOBILE_NO.toString()))
            .andExpect(jsonPath("$.vehdecscr").value(DEFAULT_VEHDECSCR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDrivoData() throws Exception {
        // Get the drivoData
        restDrivoDataMockMvc.perform(get("/api/drivo-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDrivoData() throws Exception {
        // Initialize the database
        drivoDataRepository.saveAndFlush(drivoData);

        int databaseSizeBeforeUpdate = drivoDataRepository.findAll().size();

        // Update the drivoData
        DrivoData updatedDrivoData = drivoDataRepository.findById(drivoData.getId()).get();
        // Disconnect from session so that the updates on updatedDrivoData are not directly saved in db
        em.detach(updatedDrivoData);
        updatedDrivoData
            .regNo(UPDATED_REG_NO)
            .ownerName(UPDATED_OWNER_NAME)
            .mobileNo(UPDATED_MOBILE_NO)
            .vehdecscr(UPDATED_VEHDECSCR);
        DrivoDataDTO drivoDataDTO = drivoDataMapper.toDto(updatedDrivoData);

        restDrivoDataMockMvc.perform(put("/api/drivo-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(drivoDataDTO)))
            .andExpect(status().isOk());

        // Validate the DrivoData in the database
        List<DrivoData> drivoDataList = drivoDataRepository.findAll();
        assertThat(drivoDataList).hasSize(databaseSizeBeforeUpdate);
        DrivoData testDrivoData = drivoDataList.get(drivoDataList.size() - 1);
        assertThat(testDrivoData.getRegNo()).isEqualTo(UPDATED_REG_NO);
        assertThat(testDrivoData.getOwnerName()).isEqualTo(UPDATED_OWNER_NAME);
        assertThat(testDrivoData.getMobileNo()).isEqualTo(UPDATED_MOBILE_NO);
        assertThat(testDrivoData.getVehdecscr()).isEqualTo(UPDATED_VEHDECSCR);

        // Validate the DrivoData in Elasticsearch
        verify(mockDrivoDataSearchRepository, times(1)).save(testDrivoData);
    }

    @Test
    @Transactional
    public void updateNonExistingDrivoData() throws Exception {
        int databaseSizeBeforeUpdate = drivoDataRepository.findAll().size();

        // Create the DrivoData
        DrivoDataDTO drivoDataDTO = drivoDataMapper.toDto(drivoData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDrivoDataMockMvc.perform(put("/api/drivo-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(drivoDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DrivoData in the database
        List<DrivoData> drivoDataList = drivoDataRepository.findAll();
        assertThat(drivoDataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DrivoData in Elasticsearch
        verify(mockDrivoDataSearchRepository, times(0)).save(drivoData);
    }

    @Test
    @Transactional
    public void deleteDrivoData() throws Exception {
        // Initialize the database
        drivoDataRepository.saveAndFlush(drivoData);

        int databaseSizeBeforeDelete = drivoDataRepository.findAll().size();

        // Delete the drivoData
        restDrivoDataMockMvc.perform(delete("/api/drivo-data/{id}", drivoData.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DrivoData> drivoDataList = drivoDataRepository.findAll();
        assertThat(drivoDataList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DrivoData in Elasticsearch
        verify(mockDrivoDataSearchRepository, times(1)).deleteById(drivoData.getId());
    }

    @Test
    @Transactional
    public void searchDrivoData() throws Exception {
        // Initialize the database
        drivoDataRepository.saveAndFlush(drivoData);
        when(mockDrivoDataSearchRepository.search(queryStringQuery("id:" + drivoData.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(drivoData), PageRequest.of(0, 1), 1));
        // Search the drivoData
        restDrivoDataMockMvc.perform(get("/api/_search/drivo-data?query=id:" + drivoData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(drivoData.getId().intValue())))
            .andExpect(jsonPath("$.[*].regNo").value(hasItem(DEFAULT_REG_NO)))
            .andExpect(jsonPath("$.[*].ownerName").value(hasItem(DEFAULT_OWNER_NAME)))
            .andExpect(jsonPath("$.[*].mobileNo").value(hasItem(DEFAULT_MOBILE_NO)))
            .andExpect(jsonPath("$.[*].vehdecscr").value(hasItem(DEFAULT_VEHDECSCR)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DrivoData.class);
        DrivoData drivoData1 = new DrivoData();
        drivoData1.setId(1L);
        DrivoData drivoData2 = new DrivoData();
        drivoData2.setId(drivoData1.getId());
        assertThat(drivoData1).isEqualTo(drivoData2);
        drivoData2.setId(2L);
        assertThat(drivoData1).isNotEqualTo(drivoData2);
        drivoData1.setId(null);
        assertThat(drivoData1).isNotEqualTo(drivoData2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DrivoDataDTO.class);
        DrivoDataDTO drivoDataDTO1 = new DrivoDataDTO();
        drivoDataDTO1.setId(1L);
        DrivoDataDTO drivoDataDTO2 = new DrivoDataDTO();
        assertThat(drivoDataDTO1).isNotEqualTo(drivoDataDTO2);
        drivoDataDTO2.setId(drivoDataDTO1.getId());
        assertThat(drivoDataDTO1).isEqualTo(drivoDataDTO2);
        drivoDataDTO2.setId(2L);
        assertThat(drivoDataDTO1).isNotEqualTo(drivoDataDTO2);
        drivoDataDTO1.setId(null);
        assertThat(drivoDataDTO1).isNotEqualTo(drivoDataDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(drivoDataMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(drivoDataMapper.fromId(null)).isNull();
    }
}
