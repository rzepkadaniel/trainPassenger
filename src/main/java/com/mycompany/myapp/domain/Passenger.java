package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Passenger.
 */
@Entity
@Table(name = "passenger")
public class Passenger implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "card_id")
    private Integer cardId;

    @ManyToMany(mappedBy = "passengers")
    @JsonIgnore
    private Set<Train> trains = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Passenger name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public Passenger lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getCardId() {
        return cardId;
    }

    public Passenger cardId(Integer cardId) {
        this.cardId = cardId;
        return this;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public Set<Train> getTrains() {
        return trains;
    }

    public Passenger trains(Set<Train> trains) {
        this.trains = trains;
        return this;
    }

    public Passenger addTrain(Train train) {
        this.trains.add(train);
        train.getPassengers().add(this);
        return this;
    }

    public Passenger removeTrain(Train train) {
        this.trains.remove(train);
        train.getPassengers().remove(this);
        return this;
    }

    public void setTrains(Set<Train> trains) {
        this.trains = trains;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Passenger)) {
            return false;
        }
        return id != null && id.equals(((Passenger) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Passenger{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", cardId=" + getCardId() +
            "}";
    }
}
