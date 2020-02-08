package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TrainPassengerApp;
import com.mycompany.myapp.domain.Passenger;
import com.mycompany.myapp.repository.PassengerRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PassengerResource} REST controller.
 */
@SpringBootTest(classes = TrainPassengerApp.class)
public class PassengerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_CARD_ID = 1;
    private static final Integer UPDATED_CARD_ID = 2;

    @Autowired
    private PassengerRepository passengerRepository;

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

    private MockMvc restPassengerMockMvc;

    private Passenger passenger;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PassengerResource passengerResource = new PassengerResource(passengerRepository);
        this.restPassengerMockMvc = MockMvcBuilders.standaloneSetup(passengerResource)
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
    public static Passenger createEntity(EntityManager em) {
        Passenger passenger = new Passenger()
            .name(DEFAULT_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .cardId(DEFAULT_CARD_ID);
        return passenger;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Passenger createUpdatedEntity(EntityManager em) {
        Passenger passenger = new Passenger()
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .cardId(UPDATED_CARD_ID);
        return passenger;
    }

    @BeforeEach
    public void initTest() {
        passenger = createEntity(em);
    }

    @Test
    @Transactional
    public void createPassenger() throws Exception {
        int databaseSizeBeforeCreate = passengerRepository.findAll().size();

        // Create the Passenger
        restPassengerMockMvc.perform(post("/api/passengers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isCreated());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeCreate + 1);
        Passenger testPassenger = passengerList.get(passengerList.size() - 1);
        assertThat(testPassenger.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPassenger.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPassenger.getCardId()).isEqualTo(DEFAULT_CARD_ID);
    }

    @Test
    @Transactional
    public void createPassengerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = passengerRepository.findAll().size();

        // Create the Passenger with an existing ID
        passenger.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPassengerMockMvc.perform(post("/api/passengers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isBadRequest());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPassengers() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get all the passengerList
        restPassengerMockMvc.perform(get("/api/passengers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passenger.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].cardId").value(hasItem(DEFAULT_CARD_ID)));
    }
    
    @Test
    @Transactional
    public void getPassenger() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get the passenger
        restPassengerMockMvc.perform(get("/api/passengers/{id}", passenger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(passenger.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.cardId").value(DEFAULT_CARD_ID));
    }

    @Test
    @Transactional
    public void getNonExistingPassenger() throws Exception {
        // Get the passenger
        restPassengerMockMvc.perform(get("/api/passengers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePassenger() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        int databaseSizeBeforeUpdate = passengerRepository.findAll().size();

        // Update the passenger
        Passenger updatedPassenger = passengerRepository.findById(passenger.getId()).get();
        // Disconnect from session so that the updates on updatedPassenger are not directly saved in db
        em.detach(updatedPassenger);
        updatedPassenger
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .cardId(UPDATED_CARD_ID);

        restPassengerMockMvc.perform(put("/api/passengers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPassenger)))
            .andExpect(status().isOk());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeUpdate);
        Passenger testPassenger = passengerList.get(passengerList.size() - 1);
        assertThat(testPassenger.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPassenger.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPassenger.getCardId()).isEqualTo(UPDATED_CARD_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingPassenger() throws Exception {
        int databaseSizeBeforeUpdate = passengerRepository.findAll().size();

        // Create the Passenger

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPassengerMockMvc.perform(put("/api/passengers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isBadRequest());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePassenger() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        int databaseSizeBeforeDelete = passengerRepository.findAll().size();

        // Delete the passenger
        restPassengerMockMvc.perform(delete("/api/passengers/{id}", passenger.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
