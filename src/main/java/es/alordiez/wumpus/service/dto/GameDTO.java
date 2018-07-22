package es.alordiez.wumpus.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the Game entity.
 */
public class GameDTO implements Serializable {

	private static final long serialVersionUID = -90109309987491150L;

	private Long id;

	@NotNull
	@Min(value = 3)
	private Integer width;

	@NotNull
	@Min(value = 3)
	private Integer height;

	@NotNull
	@Min(value = 0)
	private Integer pitNumber;

	@NotNull
	@Min(value = 3)
	private Integer arrows;

	private Integer goldPosition;

	private Long wumpusId;

	private Long hunterId;

	private List<Integer> movements;

	private List<String> feelings;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getPitNumber() {
		return pitNumber;
	}

	public void setPitNumber(Integer pitNumber) {
		this.pitNumber = pitNumber;
	}

	public Integer getArrows() {
		return arrows;
	}

	public void setArrows(Integer arrows) {
		this.arrows = arrows;
	}

	public Integer getGoldPosition() {
		return goldPosition;
	}

	public void setGoldPosition(Integer goldPosition) {
		this.goldPosition = goldPosition;
	}

	public Long getWumpusId() {
		return wumpusId;
	}

	public void setWumpusId(Long wumpusId) {
		this.wumpusId = wumpusId;
	}

	public Long getHunterId() {
		return hunterId;
	}

	public void setHunterId(Long hunterId) {
		this.hunterId = hunterId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		GameDTO gameDTO = (GameDTO) o;
		if (gameDTO.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), gameDTO.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "GameDTO{" + 
				"id=" + getId() + 
				", width=" + getWidth() + 
				", height=" + getHeight() + 
				", pitNumber=" + getPitNumber() + 
				", arrows=" + getArrows() + 
				", goldPosition=" + getGoldPosition() + 
				", wumpus=" + getWumpusId() + 
				", hunter=" + getHunterId() + "}";
	}

	public List<Integer> getMovements() {
		return movements;
	}

	public void setMovements(List<Integer> movements) {
		this.movements = movements;
	}

	public List<String> getFeelings() {
		return feelings;
	}

	public void setFeelings(List<String> feelings) {
		this.feelings = feelings;
	}
}
