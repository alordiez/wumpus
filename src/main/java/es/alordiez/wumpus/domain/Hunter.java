package es.alordiez.wumpus.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Hunter.
 */
@Entity
@Table(name = "hunter")
public class Hunter implements Serializable {

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

    public Hunter position(Integer position) {
        this.position = position;
        return this;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Boolean isIsAlive() {
        return isAlive;
    }

    public Hunter isAlive(Boolean isAlive) {
        this.isAlive = isAlive;
        return this;
    }

    public void setIsAlive(Boolean isAlive) {
        this.isAlive = isAlive;
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
        Hunter hunter = (Hunter) o;
        if (hunter.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), hunter.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Hunter{" +
            "id=" + getId() +
            ", position=" + getPosition() +
            ", isAlive='" + isIsAlive() + "'" +
            "}";
    }
}
