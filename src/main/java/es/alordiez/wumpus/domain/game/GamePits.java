package es.alordiez.wumpus.domain.game;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A GamePits.
 */
@Entity
@Table(name = "game_pits")
public class GamePits implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Min(value = 0)
	@Column(name = "position", nullable = false)
	private Integer position;

	@ManyToOne(optional = false)
	@NotNull
	@JsonIgnore
	private Game game;

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPosition() {
		return position;
	}

	public GamePits position(Integer position) {
		this.position = position;
		return this;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Game getGame() {
		return game;
	}

	public GamePits game(Game game) {
		this.game = game;
		return this;
	}

	public void setGame(Game game) {
		this.game = game;
	}
	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here, do not remove

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		GamePits gamePits = (GamePits) o;
		if (gamePits.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), gamePits.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "GamePits{" + "id=" + getId() + ", position=" + getPosition() + "}";
	}
}
