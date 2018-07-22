package es.alordiez.wumpus.web.rest;

import es.alordiez.wumpus.WumpusJhApp;

import es.alordiez.wumpus.domain.Wumpus;
import es.alordiez.wumpus.repository.WumpusRepository;
import es.alordiez.wumpus.service.WumpusService;
import es.alordiez.wumpus.service.dto.WumpusDTO;
import es.alordiez.wumpus.service.mapper.WumpusMapper;
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
 * Test class for the WumpusResource REST controller.
 *
 * @see WumpusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WumpusJhApp.class)
public class WumpusResourceIntTest {

    private static final Integer DEFAULT_POSITION = 0;
    private static final Integer UPDATED_POSITION = 1;

    private static final Boolean DEFAULT_IS_ALIVE = false;
    private static final Boolean UPDATED_IS_ALIVE = true;

    @Autowired
    private WumpusRepository wumpusRepository;


    @Autowired
    private WumpusMapper wumpusMapper;
    

    @Autowired
    private WumpusService wumpusService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWumpusMockMvc;

    private Wumpus wumpus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WumpusResource wumpusResource = new WumpusResource(wumpusService);
        this.restWumpusMockMvc = MockMvcBuilders.standaloneSetup(wumpusResource)
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
    public static Wumpus createEntity(EntityManager em) {
        Wumpus wumpus = new Wumpus()
            .position(DEFAULT_POSITION)
            .isAlive(DEFAULT_IS_ALIVE);
        return wumpus;
    }

    @Before
    public void initTest() {
        wumpus = createEntity(em);
    }

    @Test
    @Transactional
    public void createWumpus() throws Exception {
        int databaseSizeBeforeCreate = wumpusRepository.findAll().size();

        // Create the Wumpus
        WumpusDTO wumpusDTO = wumpusMapper.toDto(wumpus);
        restWumpusMockMvc.perform(post("/api/wumpuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wumpusDTO)))
            .andExpect(status().isCreated());

        // Validate the Wumpus in the database
        List<Wumpus> wumpusList = wumpusRepository.findAll();
        assertThat(wumpusList).hasSize(databaseSizeBeforeCreate + 1);
        Wumpus testWumpus = wumpusList.get(wumpusList.size() - 1);
        assertThat(testWumpus.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testWumpus.isIsAlive()).isEqualTo(DEFAULT_IS_ALIVE);
    }

    @Test
    @Transactional
    public void createWumpusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wumpusRepository.findAll().size();

        // Create the Wumpus with an existing ID
        wumpus.setId(1L);
        WumpusDTO wumpusDTO = wumpusMapper.toDto(wumpus);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWumpusMockMvc.perform(post("/api/wumpuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wumpusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Wumpus in the database
        List<Wumpus> wumpusList = wumpusRepository.findAll();
        assertThat(wumpusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkIsAliveIsRequired() throws Exception {
        int databaseSizeBeforeTest = wumpusRepository.findAll().size();
        // set the field null
        wumpus.setIsAlive(null);

        // Create the Wumpus, which fails.
        WumpusDTO wumpusDTO = wumpusMapper.toDto(wumpus);

        restWumpusMockMvc.perform(post("/api/wumpuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wumpusDTO)))
            .andExpect(status().isBadRequest());

        List<Wumpus> wumpusList = wumpusRepository.findAll();
        assertThat(wumpusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWumpuses() throws Exception {
        // Initialize the database
        wumpusRepository.saveAndFlush(wumpus);

        // Get all the wumpusList
        restWumpusMockMvc.perform(get("/api/wumpuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wumpus.getId().intValue())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].isAlive").value(hasItem(DEFAULT_IS_ALIVE.booleanValue())));
    }
    

    @Test
    @Transactional
    public void getWumpus() throws Exception {
        // Initialize the database
        wumpusRepository.saveAndFlush(wumpus);

        // Get the wumpus
        restWumpusMockMvc.perform(get("/api/wumpuses/{id}", wumpus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wumpus.getId().intValue()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.isAlive").value(DEFAULT_IS_ALIVE.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingWumpus() throws Exception {
        // Get the wumpus
        restWumpusMockMvc.perform(get("/api/wumpuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWumpus() throws Exception {
        // Initialize the database
        wumpusRepository.saveAndFlush(wumpus);

        int databaseSizeBeforeUpdate = wumpusRepository.findAll().size();

        // Update the wumpus
        Wumpus updatedWumpus = wumpusRepository.findById(wumpus.getId()).get();
        // Disconnect from session so that the updates on updatedWumpus are not directly saved in db
        em.detach(updatedWumpus);
        updatedWumpus
            .position(UPDATED_POSITION)
            .isAlive(UPDATED_IS_ALIVE);
        WumpusDTO wumpusDTO = wumpusMapper.toDto(updatedWumpus);

        restWumpusMockMvc.perform(put("/api/wumpuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wumpusDTO)))
            .andExpect(status().isOk());

        // Validate the Wumpus in the database
        List<Wumpus> wumpusList = wumpusRepository.findAll();
        assertThat(wumpusList).hasSize(databaseSizeBeforeUpdate);
        Wumpus testWumpus = wumpusList.get(wumpusList.size() - 1);
        assertThat(testWumpus.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testWumpus.isIsAlive()).isEqualTo(UPDATED_IS_ALIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingWumpus() throws Exception {
        int databaseSizeBeforeUpdate = wumpusRepository.findAll().size();

        // Create the Wumpus
        WumpusDTO wumpusDTO = wumpusMapper.toDto(wumpus);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWumpusMockMvc.perform(put("/api/wumpuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wumpusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Wumpus in the database
        List<Wumpus> wumpusList = wumpusRepository.findAll();
        assertThat(wumpusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWumpus() throws Exception {
        // Initialize the database
        wumpusRepository.saveAndFlush(wumpus);

        int databaseSizeBeforeDelete = wumpusRepository.findAll().size();

        // Get the wumpus
        restWumpusMockMvc.perform(delete("/api/wumpuses/{id}", wumpus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Wumpus> wumpusList = wumpusRepository.findAll();
        assertThat(wumpusList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Wumpus.class);
        Wumpus wumpus1 = new Wumpus();
        wumpus1.setId(1L);
        Wumpus wumpus2 = new Wumpus();
        wumpus2.setId(wumpus1.getId());
        assertThat(wumpus1).isEqualTo(wumpus2);
        wumpus2.setId(2L);
        assertThat(wumpus1).isNotEqualTo(wumpus2);
        wumpus1.setId(null);
        assertThat(wumpus1).isNotEqualTo(wumpus2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WumpusDTO.class);
        WumpusDTO wumpusDTO1 = new WumpusDTO();
        wumpusDTO1.setId(1L);
        WumpusDTO wumpusDTO2 = new WumpusDTO();
        assertThat(wumpusDTO1).isNotEqualTo(wumpusDTO2);
        wumpusDTO2.setId(wumpusDTO1.getId());
        assertThat(wumpusDTO1).isEqualTo(wumpusDTO2);
        wumpusDTO2.setId(2L);
        assertThat(wumpusDTO1).isNotEqualTo(wumpusDTO2);
        wumpusDTO1.setId(null);
        assertThat(wumpusDTO1).isNotEqualTo(wumpusDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(wumpusMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(wumpusMapper.fromId(null)).isNull();
    }
}
