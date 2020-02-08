package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Train;
import com.mycompany.myapp.repository.TrainRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Train}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TrainResource {

    private final Logger log = LoggerFactory.getLogger(TrainResource.class);

    private static final String ENTITY_NAME = "train";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrainRepository trainRepository;

    public TrainResource(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    /**
     * {@code POST  /trains} : Create a new train.
     *
     * @param train the train to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new train, or with status {@code 400 (Bad Request)} if the train has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trains")
    public ResponseEntity<Train> createTrain(@RequestBody Train train) throws URISyntaxException {
        log.debug("REST request to save Train : {}", train);
        if (train.getId() != null) {
            throw new BadRequestAlertException("A new train cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Train result = trainRepository.save(train);
        return ResponseEntity.created(new URI("/api/trains/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trains} : Updates an existing train.
     *
     * @param train the train to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated train,
     * or with status {@code 400 (Bad Request)} if the train is not valid,
     * or with status {@code 500 (Internal Server Error)} if the train couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trains")
    public ResponseEntity<Train> updateTrain(@RequestBody Train train) throws URISyntaxException {
        log.debug("REST request to update Train : {}", train);
        if (train.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Train result = trainRepository.save(train);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, train.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /trains} : get all the trains.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trains in body.
     */
    @GetMapping("/trains")
    public List<Train> getAllTrains(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Trains");
        return trainRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /trains/:id} : get the "id" train.
     *
     * @param id the id of the train to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the train, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trains/{id}")
    public ResponseEntity<Train> getTrain(@PathVariable Long id) {
        log.debug("REST request to get Train : {}", id);
        Optional<Train> train = trainRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(train);
    }

    /**
     * {@code DELETE  /trains/:id} : delete the "id" train.
     *
     * @param id the id of the train to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trains/{id}")
    public ResponseEntity<Void> deleteTrain(@PathVariable Long id) {
        log.debug("REST request to delete Train : {}", id);
        trainRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
