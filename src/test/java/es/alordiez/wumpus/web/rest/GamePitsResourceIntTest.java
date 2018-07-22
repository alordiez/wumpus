package es.alordiez.wumpus.web.rest;

import es.alordiez.wumpus.WumpusJhApp;

import es.alordiez.wumpus.domain.GamePits;
import es.alordiez.wumpus.repository.GamePitsRepository;
import es.alordiez.wumpus.service.GamePitsService;
import es.alordiez.wumpus.service.dto.GamePitsDTO;
import es.alordiez.wumpus.service.mapper.GamePitsMapper;
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
 * Test class for the GamePitsResource REST controller.
 *
 * @see GamePitsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WumpusJhApp.class)
public class GamePitsResourceIntTest {

    private static final Integer DEFAULT_POSITION = 0;
    private static final Integer UPDATED_POSITION = 1;

    @Autowired
    private GamePitsRepository gamePitsRepository;


    @Autowired
    private GamePitsMapper gamePitsMapper;
    

    @Autowired
    private GamePitsService gamePitsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGamePitsMockMvc;

    private GamePits gamePits;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GamePitsResource gamePitsResource = new GamePitsResource(gamePitsService);
        this.restGamePitsMockMvc = MockMvcBuilders.standaloneSetup(gamePitsResource)
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
    public static GamePits createEntity(EntityManager em) {
        GamePits gamePits = new GamePits()
            .position(DEFAULT_POSITION);
        return gamePits;
    }

    @Before
    public void initTest() {
        gamePits = createEntity(em);
    }

    @Test
    @Transactional
    public void createGamePits() throws Exception {
        int databaseSizeBeforeCreate = gamePitsRepository.findAll().size();

        // Create the GamePits
        GamePitsDTO gamePitsDTO = gamePitsMapper.toDto(gamePits);
        restGamePitsMockMvc.perform(post("/api/game-pits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gamePitsDTO)))
            .andExpect(status().isCreated());

        // Validate the GamePits in the database
        List<GamePits> gamePitsList = gamePitsRepository.findAll();
        assertThat(gamePitsList).hasSize(databaseSizeBeforeCreate + 1);
        GamePits testGamePits = gamePitsList.get(gamePitsList.size() - 1);
        assertThat(testGamePits.getPosition()).isEqualTo(DEFAULT_POSITION);
    }

    @Test
    @Transactional
    public void createGamePitsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gamePitsRepository.findAll().size();

        // Create the GamePits with an existing ID
        gamePits.setId(1L);
        GamePitsDTO gamePitsDTO = gamePitsMapper.toDto(gamePits);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGamePitsMockMvc.perform(post("/api/game-pits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gamePitsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GamePits in the database
        List<GamePits> gamePitsList = gamePitsRepository.findAll();
        assertThat(gamePitsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPositionIsRequired() throws Exception {
        int databaseSizeBeforeTest = gamePitsRepository.findAll().size();
        // set the field null
        gamePits.setPosition(null);

        // Create the GamePits, which fails.
        GamePitsDTO gamePitsDTO = gamePitsMapper.toDto(gamePits);

        restGamePitsMockMvc.perform(post("/api/game-pits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gamePitsDTO)))
            .andExpect(status().isBadRequest());

        List<GamePits> gamePitsList = gamePitsRepository.findAll();
        assertThat(gamePitsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGamePits() throws Exception {
        // Initialize the database
        gamePitsRepository.saveAndFlush(gamePits);

        // Get all the gamePitsList
        restGamePitsMockMvc.perform(get("/api/game-pits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gamePits.getId().intValue())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }
    

    @Test
    @Transactional
    public void getGamePits() throws Exception {
        // Initialize the database
        gamePitsRepository.saveAndFlush(gamePits);

        // Get the gamePits
        restGamePitsMockMvc.perform(get("/api/game-pits/{id}", gamePits.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gamePits.getId().intValue()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }
    @Test
    @Transactional
    public void getNonExistingGamePits() throws Exception {
        // Get the gamePits
        restGamePitsMockMvc.perform(get("/api/game-pits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGamePits() throws Exception {
        // Initialize the database
        gamePitsRepository.saveAndFlush(gamePits);

        int databaseSizeBeforeUpdate = gamePitsRepository.findAll().size();

        // Update the gamePits
        GamePits updatedGamePits = gamePitsRepository.findById(gamePits.getId()).get();
        // Disconnect from session so that the updates on updatedGamePits are not directly saved in db
        em.detach(updatedGamePits);
        updatedGamePits
            .position(UPDATED_POSITION);
        GamePitsDTO gamePitsDTO = gamePitsMapper.toDto(updatedGamePits);

        restGamePitsMockMvc.perform(put("/api/game-pits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gamePitsDTO)))
            .andExpect(status().isOk());

        // Validate the GamePits in the database
        List<GamePits> gamePitsList = gamePitsRepository.findAll();
        assertThat(gamePitsList).hasSize(databaseSizeBeforeUpdate);
        GamePits testGamePits = gamePitsList.get(gamePitsList.size() - 1);
        assertThat(testGamePits.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void updateNonExistingGamePits() throws Exception {
        int databaseSizeBeforeUpdate = gamePitsRepository.findAll().size();

        // Create the GamePits
        GamePitsDTO gamePitsDTO = gamePitsMapper.toDto(gamePits);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGamePitsMockMvc.perform(put("/api/game-pits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gamePitsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GamePits in the database
        List<GamePits> gamePitsList = gamePitsRepository.findAll();
        assertThat(gamePitsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGamePits() throws Exception {
        // Initialize the database
        gamePitsRepository.saveAndFlush(gamePits);

        int databaseSizeBeforeDelete = gamePitsRepository.findAll().size();

        // Get the gamePits
        restGamePitsMockMvc.perform(delete("/api/game-pits/{id}", gamePits.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<GamePits> gamePitsList = gamePitsRepository.findAll();
        assertThat(gamePitsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GamePits.class);
        GamePits gamePits1 = new GamePits();
        gamePits1.setId(1L);
        GamePits gamePits2 = new GamePits();
        gamePits2.setId(gamePits1.getId());
        assertThat(gamePits1).isEqualTo(gamePits2);
        gamePits2.setId(2L);
        assertThat(gamePits1).isNotEqualTo(gamePits2);
        gamePits1.setId(null);
        assertThat(gamePits1).isNotEqualTo(gamePits2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GamePitsDTO.class);
        GamePitsDTO gamePitsDTO1 = new GamePitsDTO();
        gamePitsDTO1.setId(1L);
        GamePitsDTO gamePitsDTO2 = new GamePitsDTO();
        assertThat(gamePitsDTO1).isNotEqualTo(gamePitsDTO2);
        gamePitsDTO2.setId(gamePitsDTO1.getId());
        assertThat(gamePitsDTO1).isEqualTo(gamePitsDTO2);
        gamePitsDTO2.setId(2L);
        assertThat(gamePitsDTO1).isNotEqualTo(gamePitsDTO2);
        gamePitsDTO1.setId(null);
        assertThat(gamePitsDTO1).isNotEqualTo(gamePitsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(gamePitsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(gamePitsMapper.fromId(null)).isNull();
    }
}
