package es.alordiez.wumpus.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the GamePits entity.
 */
public class GamePitsDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 0)
    private Integer position;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GamePitsDTO gamePitsDTO = (GamePitsDTO) o;
        if (gamePitsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gamePitsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GamePitsDTO{" +
            "id=" + getId() +
            ", position=" + getPosition() +
            "}";
    }
}
