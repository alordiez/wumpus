package es.alordiez.wumpus.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Wumpus entity.
 */
public class WumpusDTO implements Serializable {

    private Long id;

    @Min(value = 0)
    private Integer position;

    @NotNull
    private Boolean isAlive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Boolean isIsAlive() {
        return isAlive;
    }

    public void setIsAlive(Boolean isAlive) {
        this.isAlive = isAlive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WumpusDTO wumpusDTO = (WumpusDTO) o;
        if (wumpusDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wumpusDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WumpusDTO{" +
            "id=" + getId() +
            ", position=" + getPosition() +
            ", isAlive='" + isIsAlive() + "'" +
            "}";
    }
}
