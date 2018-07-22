package es.alordiez.wumpus.domain.game;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

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

    @Column(name = "gold_position")
    private Integer goldPosition;

    @OneToMany(mappedBy = "game")
    private Set<GamePits> gamePits = new HashSet<>();

    @OneToOne
    @JoinColumn(unique = true)
    private Wumpus wumpus;

    @OneToOne
    @JoinColumn(unique = true)
    private Hunter hunter;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
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
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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
        return "Game{" +
            "id=" + getId() +
            ", width=" + getWidth() +
            ", height=" + getHeight() +
            ", pitNumber=" + getPitNumber() +
            ", arrows=" + getArrows() +
            ", goldPosition=" + getGoldPosition() +
            "}";
    }
}
