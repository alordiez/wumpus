package es.alordiez.wumpus.domain.game;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.RandomUtils;

/**
 * A Game.
 */
@Entity
@Table(name = "game")
public class Game implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Min(value = 3)
	@Column(name = "width", nullable = false)
	private Integer width;

	@NotNull
	@Min(value = 3)
	@Column(name = "height", nullable = false)
	private Integer height;

	@NotNull
	@Min(value = 0)
	@Column(name = "pit_number", nullable = false)
	private Integer pitNumber;

	@NotNull
	@Min(value = 3)
	@Column(name = "arrows", nullable = false)
	private Integer arrows;
	
	@NotNull
	@Min(value = 0)
	@Column(name = "usedArrows", nullable = false)
	private Integer usedArrows;

	@Column(name = "gold_position")
	private Integer goldPosition;
	
	@Column(name = "started", nullable=false)
	private Boolean started;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "game")
	private Set<GamePits> gamePits = new HashSet<>();

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(unique = true)
	private Wumpus wumpus;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(unique = true)
	private Hunter hunter;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "hunter_movements", joinColumns = @JoinColumn(name = "game_id"))
	private List<Integer> movements;
	
	/**
	 * Default empty constructor
	 */
	public Game() {
		// DEFAULT EMPTY CONSTRUCTOR
	}

	/**
	 * Required args constructor
	 * 
	 * @param width
	 * @param height
	 * @param pitNumber
	 * @param arrows
	 */
	public Game(Integer width, Integer height, Integer pitNumber, Integer arrows) {
		this.width = width;
		this.height = height;
		this.pitNumber = pitNumber;
		this.arrows = arrows;
	}

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getWidth() {
		return width;
	}

	public Game width(Integer width) {
		this.width = width;
		return this;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public Game height(Integer height) {
		this.height = height;
		return this;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getPitNumber() {
		return pitNumber;
	}

	public Game pitNumber(Integer pitNumber) {
		this.pitNumber = pitNumber;
		return this;
	}

	public void setPitNumber(Integer pitNumber) {
		this.pitNumber = pitNumber;
	}

	public Integer getArrows() {
		return arrows;
	}

	public Game arrows(Integer arrows) {
		this.arrows = arrows;
		return this;
	}

	public void setArrows(Integer arrows) {
		this.arrows = arrows;
	}
	
	public Integer getUsedArrows() {
		return usedArrows;
	}

	public Game usedArrows(Integer arrows) {
		this.usedArrows = arrows;
		return this;
	}

	public void setUsedArrows(Integer arrows) {
		this.usedArrows = arrows;
	}

	public Integer getGoldPosition() {
		return goldPosition;
	}

	public Game goldPosition(Integer goldPosition) {
		this.goldPosition = goldPosition;
		return this;
	}

	public void setGoldPosition(Integer goldPosition) {
		this.goldPosition = goldPosition;
	}

	public Set<GamePits> getGamePits() {
		return gamePits;
	}

	public Game gamePits(Set<GamePits> gamePits) {
		this.gamePits = gamePits;
		return this;
	}

	public Game addGamePits(GamePits gamePits) {
		this.gamePits.add(gamePits);
		gamePits.setGame(this);
		return this;
	}

	public Game removeGamePits(GamePits gamePits) {
		this.gamePits.remove(gamePits);
		gamePits.setGame(null);
		return this;
	}

	public void setGamePits(Set<GamePits> gamePits) {
		this.gamePits = gamePits;
	}

	public Wumpus getWumpus() {
		return wumpus;
	}

	public Game wumpus(Wumpus wumpus) {
		this.wumpus = wumpus;
		return this;
	}

	public void setWumpus(Wumpus wumpus) {
		this.wumpus = wumpus;
	}

	public Hunter getHunter() {
		return hunter;
	}

	public Game hunter(Hunter hunter) {
		this.hunter = hunter;
		return this;
	}

	public void setHunter(Hunter hunter) {
		this.hunter = hunter;
	}
	
	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here, do not remove

	public List<Integer> getMovements() {
		return movements;
	}

	public void setMovements(List<Integer> movements) {
		this.movements = movements;
	}
	
	public Boolean isStarted() {
		return started;
	}

	public Game started(Boolean started) {
		this.started = started;
		return this;
	}

	public void setStarted(Boolean started) {
		this.started = started;
	}

	public boolean isPositionUsed(Integer positionToCheck) {
		if (hunter != null && hunter.getPosition().compareTo(positionToCheck) == 0) {
			return true;
		}
		if (wumpus != null && wumpus.getPosition().compareTo(positionToCheck) == 0) {
			return true;
		}
		if (gamePits != null && !gamePits.isEmpty()) {
			Optional<GamePits> match = gamePits.stream().filter(pit -> pit.getPosition() == positionToCheck)
					.findFirst();
			if (match.isPresent()) {
				return true;
			}
		}
		return false;
	}

	public Integer getRandomFreeField() {
		Integer boardFileds = height * width - 1;
		Integer generatedValues = 1;
		Integer nextPosition = RandomUtils.nextInt(0, boardFileds);
		while (isPositionUsed(nextPosition)) {
			nextPosition = RandomUtils.nextInt(0, boardFileds);
			generatedValues++;
			if (generatedValues >= boardFileds) {
				return -1;
			}
		}
		return nextPosition;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Game game = (Game) o;
		if (game.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), game.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "Game{" + "id=" + getId() + ", width=" + getWidth() + ", height=" + getHeight() + ", pitNumber="
				+ getPitNumber() + ", arrows=" + getArrows() + ", goldPosition=" + getGoldPosition() + "}";
	}

	public void moveHunter(Integer newPosition) {
		hunter.setPosition(newPosition);
		movements.add(newPosition);
	}
	public void killHunter() {
		hunter.setIsAlive(false);
		started=false;
	}

	public void killWumpus() {
		wumpus.setIsAlive(false);
	}

	public void minusArrows() {
		usedArrows++;
	}
}
