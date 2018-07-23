package es.alordiez.wumpus.service.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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

	private List<Integer> movements;

	private HashMap<Integer,HashMap<Integer,FieldDTO>> board;

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
				", arrows=" + getArrows()
				;
	}

	public List<Integer> getMovements() {
		return movements;
	}

	public void setMovements(List<Integer> movements) {
		this.movements = movements;
	}

	public HashMap<Integer, HashMap<Integer, FieldDTO>> getBoard() {
		return board;
	}

	public void setBoard(HashMap<Integer, HashMap<Integer, FieldDTO>> board) {
		this.board = board;
	}
	
}
