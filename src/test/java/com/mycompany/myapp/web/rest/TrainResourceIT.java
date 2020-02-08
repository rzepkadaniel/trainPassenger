package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TrainPassengerApp;
import com.mycompany.myapp.domain.Train;
import com.mycompany.myapp.repository.TrainRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TrainResource} REST controller.
 */
@SpringBootTest(classes = TrainPassengerApp.class)
public class TrainResourceIT {

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONNECTION = "AAAAAAAAAA";
    private static final String UPDATED_CONNECTION = "BBBBBBBBBB";

    @Autowired
    private TrainRepository trainRepository;

    @Mock
    private TrainRepository trainRepositoryMock;

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

    private MockMvc restTrainMockMvc;

    private Train train;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TrainResource trainResource = new TrainResource(trainRepository);
        this.restTrainMockMvc = MockMvcBuilders.standaloneSetup(trainResource)
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
    public static Train createEntity(EntityManager em) {
        Train train = new Train()
            .model(DEFAULT_MODEL)
            .name(DEFAULT_NAME)
            .connection(DEFAULT_CONNECTION);
        return train;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Train createUpdatedEntity(EntityManager em) {
        Train train = new Train()
            .model(UPDATED_MODEL)
            .name(UPDATED_NAME)
            .connection(UPDATED_CONNECTION);
        return train;
    }

    @BeforeEach
    public void initTest() {
        train = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrain() throws Exception {
        int databaseSizeBeforeCreate = trainRepository.findAll().size();

        // Create the Train
        restTrainMockMvc.perform(post("/api/trains")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(train)))
            .andExpect(status().isCreated());

        // Validate the Train in the database
        List<Train> trainList = trainRepository.findAll();
        assertThat(trainList).hasSize(databaseSizeBeforeCreate + 1);
        Train testTrain = trainList.get(trainList.size() - 1);
        assertThat(testTrain.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testTrain.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTrain.getConnection()).isEqualTo(DEFAULT_CONNECTION);
    }

    @Test
    @Transactional
    public void createTrainWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = trainRepository.findAll().size();

        // Create the Train with an existing ID
        train.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrainMockMvc.perform(post("/api/trains")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(train)))
            .andExpect(status().isBadRequest());

        // Validate the Train in the database
        List<Train> trainList = trainRepository.findAll();
        assertThat(trainList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTrains() throws Exception {
        // Initialize the database
        trainRepository.saveAndFlush(train);

        // Get all the trainList
        restTrainMockMvc.perform(get("/api/trains?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(train.getId().intValue())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].connection").value(hasItem(DEFAULT_CONNECTION)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllTrainsWithEagerRelationshipsIsEnabled() throws Exception {
        TrainResource trainResource = new TrainResource(trainRepositoryMock);
        when(trainRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restTrainMockMvc = MockMvcBuilders.standaloneSetup(trainResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restTrainMockMvc.perform(get("/api/trains?eagerload=true"))
        .andExpect(status().isOk());

        verify(trainRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllTrainsWithEagerRelationshipsIsNotEnabled() throws Exception {
        TrainResource trainResource = new TrainResource(trainRepositoryMock);
            when(trainRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restTrainMockMvc = MockMvcBuilders.standaloneSetup(trainResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restTrainMockMvc.perform(get("/api/trains?eagerload=true"))
        .andExpect(status().isOk());

            verify(trainRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getTrain() throws Exception {
        // Initialize the database
        trainRepository.saveAndFlush(train);

        // Get the train
        restTrainMockMvc.perform(get("/api/trains/{id}", train.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(train.getId().intValue()))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.connection").value(DEFAULT_CONNECTION));
    }

    @Test
    @Transactional
    public void getNonExistingTrain() throws Exception {
        // Get the train
        restTrainMockMvc.perform(get("/api/trains/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrain() throws Exception {
        // Initialize the database
        trainRepository.saveAndFlush(train);

        int databaseSizeBeforeUpdate = trainRepository.findAll().size();

        // Update the train
        Train updatedTrain = trainRepository.findById(train.getId()).get();
        // Disconnect from session so that the updates on updatedTrain are not directly saved in db
        em.detach(updatedTrain);
        updatedTrain
            .model(UPDATED_MODEL)
            .name(UPDATED_NAME)
            .connection(UPDATED_CONNECTION);

        restTrainMockMvc.perform(put("/api/trains")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTrain)))
            .andExpect(status().isOk());

        // Validate the Train in the database
        List<Train> trainList = trainRepository.findAll();
        assertThat(trainList).hasSize(databaseSizeBeforeUpdate);
        Train testTrain = trainList.get(trainList.size() - 1);
        assertThat(testTrain.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testTrain.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTrain.getConnection()).isEqualTo(UPDATED_CONNECTION);
    }

    @Test
    @Transactional
    public void updateNonExistingTrain() throws Exception {
        int databaseSizeBeforeUpdate = trainRepository.findAll().size();

        // Create the Train

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainMockMvc.perform(put("/api/trains")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(train)))
            .andExpect(status().isBadRequest());

        // Validate the Train in the database
        List<Train> trainList = trainRepository.findAll();
        assertThat(trainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTrain() throws Exception {
        // Initialize the database
        trainRepository.saveAndFlush(train);

        int databaseSizeBeforeDelete = trainRepository.findAll().size();

        // Delete the train
        restTrainMockMvc.perform(delete("/api/trains/{id}", train.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Train> trainList = trainRepository.findAll();
        assertThat(trainList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
