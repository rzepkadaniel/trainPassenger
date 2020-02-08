package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Train;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Train entity.
 */
@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {

    @Query(value = "select distinct train from Train train left join fetch train.passengers",
        countQuery = "select count(distinct train) from Train train")
    Page<Train> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct train from Train train left join fetch train.passengers")
    List<Train> findAllWithEagerRelationships();

    @Query("select train from Train train left join fetch train.passengers where train.id =:id")
    Optional<Train> findOneWithEagerRelationships(@Param("id") Long id);

}
