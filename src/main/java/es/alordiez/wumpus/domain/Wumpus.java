package es.alordiez.wumpus.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Wumpus.
 */
@Entity
@Table(name = "wumpus")
public class Wumpus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 0)
    @Column(name = "position")
    private Integer position;

    @NotNull
    @Column(name = "is_alive", nullable = false)
    private Boolean isAlive;

    @OneToOne(mappedBy = "wumpus")
    @JsonIgnore
    private Game game;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public Wumpus position(Integer position) {
        this.position = position;
        return this;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Boolean isIsAlive() {
        return isAlive;
    }

    public Wumpus isAlive(Boolean isAlive) {
        this.isAlive = isAlive;
        return this;
    }

    public void setIsAlive(Boolean isAlive) {
        this.isAlive = isAlive;
    }

    public Game getGame() {
        return game;
    }

    public Wumpus game(Game game) {
        this.game = game;
        return this;
    }

    public void setGame(Game game) {
        this.game = game;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Wumpus wumpus = (Wumpus) o;
        if (wumpus.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wumpus.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Wumpus{" +
            "id=" + getId() +
            ", position=" + getPosition() +
            ", isAlive='" + isIsAlive() + "'" +
            "}";
    }
}
