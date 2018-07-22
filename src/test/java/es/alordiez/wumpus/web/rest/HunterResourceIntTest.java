package es.alordiez.wumpus.web.rest;

import es.alordiez.wumpus.WumpusJhApp;

import es.alordiez.wumpus.domain.Hunter;
import es.alordiez.wumpus.repository.HunterRepository;
import es.alordiez.wumpus.service.HunterService;
import es.alordiez.wumpus.service.dto.HunterDTO;
import es.alordiez.wumpus.service.mapper.HunterMapper;
import es.alordiez.wumpus.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static es.alordiez.wumpus.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the HunterResource REST controller.
 *
 * @see HunterResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WumpusJhApp.class)
public class HunterResourceIntTest {

    private static final Integer DEFAULT_POSITION = 0;
    private static final Integer UPDATED_POSITION = 1;

    private static final Boolean DEFAULT_IS_ALIVE = false;
    private static final Boolean UPDATED_IS_ALIVE = true;

    @Autowired
    private HunterRepository hunterRepository;


    @Autowired
    private HunterMapper hunterMapper;
    

    @Autowired
    private HunterService hunterService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHunterMockMvc;

    private Hunter hunter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HunterResource hunterResource = new HunterResource(hunterService);
        this.restHunterMockMvc = MockMvcBuilders.standaloneSetup(hunterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hunter createEntity(EntityManager em) {
        Hunter hunter = new Hunter()
            .position(DEFAULT_POSITION)
            .isAlive(DEFAULT_IS_ALIVE);
        return hunter;
    }

    @Before
    public void initTest() {
        hunter = createEntity(em);
    }

    @Test
    @Transactional
    public void createHunter() throws Exception {
        int databaseSizeBeforeCreate = hunterRepository.findAll().size();

        // Create the Hunter
        HunterDTO hunterDTO = hunterMapper.toDto(hunter);
        restHunterMockMvc.perform(post("/api/hunters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hunterDTO)))
            .andExpect(status().isCreated());

        // Validate the Hunter in the database
        List<Hunter> hunterList = hunterRepository.findAll();
        assertThat(hunterList).hasSize(databaseSizeBeforeCreate + 1);
        Hunter testHunter = hunterList.get(hunterList.size() - 1);
        assertThat(testHunter.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testHunter.isIsAlive()).isEqualTo(DEFAULT_IS_ALIVE);
    }

    @Test
    @Transactional
    public void createHunterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hunterRepository.findAll().size();

        // Create the Hunter with an existing ID
        hunter.setId(1L);
        HunterDTO hunterDTO = hunterMapper.toDto(hunter);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHunterMockMvc.perform(post("/api/hunters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hunterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Hunter in the database
        List<Hunter> hunterList = hunterRepository.findAll();
        assertThat(hunterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkIsAliveIsRequired() throws Exception {
        int databaseSizeBeforeTest = hunterRepository.findAll().size();
        // set the field null
        hunter.setIsAlive(null);

        // Create the Hunter, which fails.
        HunterDTO hunterDTO = hunterMapper.toDto(hunter);

        restHunterMockMvc.perform(post("/api/hunters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hunterDTO)))
            .andExpect(status().isBadRequest());

        List<Hunter> hunterList = hunterRepository.findAll();
        assertThat(hunterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHunters() throws Exception {
        // Initialize the database
        hunterRepository.saveAndFlush(hunter);

        // Get all the hunterList
        restHunterMockMvc.perform(get("/api/hunters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hunter.getId().intValue())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].isAlive").value(hasItem(DEFAULT_IS_ALIVE.booleanValue())));
    }
    

    @Test
    @Transactional
    public void getHunter() throws Exception {
        // Initialize the database
        hunterRepository.saveAndFlush(hunter);

        // Get the hunter
        restHunterMockMvc.perform(get("/api/hunters/{id}", hunter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hunter.getId().intValue()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.isAlive").value(DEFAULT_IS_ALIVE.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingHunter() throws Exception {
        // Get the hunter
        restHunterMockMvc.perform(get("/api/hunters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHunter() throws Exception {
        // Initialize the database
        hunterRepository.saveAndFlush(hunter);

        int databaseSizeBeforeUpdate = hunterRepository.findAll().size();

        // Update the hunter
        Hunter updatedHunter = hunterRepository.findById(hunter.getId()).get();
        // Disconnect from session so that the updates on updatedHunter are not directly saved in db
        em.detach(updatedHunter);
        updatedHunter
            .position(UPDATED_POSITION)
            .isAlive(UPDATED_IS_ALIVE);
        HunterDTO hunterDTO = hunterMapper.toDto(updatedHunter);

        restHunterMockMvc.perform(put("/api/hunters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hunterDTO)))
            .andExpect(status().isOk());

        // Validate the Hunter in the database
        List<Hunter> hunterList = hunterRepository.findAll();
        assertThat(hunterList).hasSize(databaseSizeBeforeUpdate);
        Hunter testHunter = hunterList.get(hunterList.size() - 1);
        assertThat(testHunter.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testHunter.isIsAlive()).isEqualTo(UPDATED_IS_ALIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingHunter() throws Exception {
        int databaseSizeBeforeUpdate = hunterRepository.findAll().size();

        // Create the Hunter
        HunterDTO hunterDTO = hunterMapper.toDto(hunter);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHunterMockMvc.perform(put("/api/hunters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hunterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Hunter in the database
        List<Hunter> hunterList = hunterRepository.findAll();
        assertThat(hunterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHunter() throws Exception {
        // Initialize the database
        hunterRepository.saveAndFlush(hunter);

        int databaseSizeBeforeDelete = hunterRepository.findAll().size();

        // Get the hunter
        restHunterMockMvc.perform(delete("/api/hunters/{id}", hunter.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Hunter> hunterList = hunterRepository.findAll();
        assertThat(hunterList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hunter.class);
        Hunter hunter1 = new Hunter();
        hunter1.setId(1L);
        Hunter hunter2 = new Hunter();
        hunter2.setId(hunter1.getId());
        assertThat(hunter1).isEqualTo(hunter2);
        hunter2.setId(2L);
        assertThat(hunter1).isNotEqualTo(hunter2);
        hunter1.setId(null);
        assertThat(hunter1).isNotEqualTo(hunter2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HunterDTO.class);
        HunterDTO hunterDTO1 = new HunterDTO();
        hunterDTO1.setId(1L);
        HunterDTO hunterDTO2 = new HunterDTO();
        assertThat(hunterDTO1).isNotEqualTo(hunterDTO2);
        hunterDTO2.setId(hunterDTO1.getId());
        assertThat(hunterDTO1).isEqualTo(hunterDTO2);
        hunterDTO2.setId(2L);
        assertThat(hunterDTO1).isNotEqualTo(hunterDTO2);
        hunterDTO1.setId(null);
        assertThat(hunterDTO1).isNotEqualTo(hunterDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(hunterMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(hunterMapper.fromId(null)).isNull();
    }
}
