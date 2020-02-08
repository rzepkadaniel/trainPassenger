package com.mycompany.myapp.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Train.
 */
@Entity
@Table(name = "train")
public class Train implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model")
    private String model;

    @Column(name = "name")
    private String name;

    @Column(name = "connection")
    private String connection;

    @ManyToMany
    @JoinTable(name = "train_passenger",
               joinColumns = @JoinColumn(name = "train_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "passenger_id", referencedColumnName = "id"))
    private Set<Passenger> passengers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public Train model(String model) {
        this.model = model;
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public Train name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConnection() {
        return connection;
    }

    public Train connection(String connection) {
        this.connection = connection;
        return this;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public Train passengers(Set<Passenger> passengers) {
        this.passengers = passengers;
        return this;
    }

    public Train addPassenger(Passenger passenger) {
        this.passengers.add(passenger);
        passenger.getTrains().add(this);
        return this;
    }

    public Train removePassenger(Passenger passenger) {
        this.passengers.remove(passenger);
        passenger.getTrains().remove(this);
        return this;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Train)) {
            return false;
        }
        return id != null && id.equals(((Train) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Train{" +
            "id=" + getId() +
            ", model='" + getModel() + "'" +
            ", name='" + getName() + "'" +
            ", connection='" + getConnection() + "'" +
            "}";
    }
}
