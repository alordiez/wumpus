package es.alordiez.wumpus.service.dto;

import java.util.HashSet;
import java.util.Set;

import es.alordiez.wumpus.domain.game.Element;
import es.alordiez.wumpus.domain.game.Perception;

public class FieldDTO {

	private int position;

	private Set<Perception> perceptions;

	private Set<Element> elements;
	
	private Boolean visited = false;

	public FieldDTO(int position) {
		this.position = position;
		perceptions = new HashSet<>();
		elements = new HashSet<>();
	}
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Set<Perception> getPerceptions() {
		return perceptions;
	}

	public void setPerceptions(Set<Perception> perceptions) {
		this.perceptions = perceptions;
	}

	public Set<Element> getElements() {
		return elements;
	}

	public void setElements(Set<Element> elements) {
		this.elements = elements;
	}

	public Boolean getVisited() {
		return visited;
	}

	public void setVisited(Boolean visited) {
		this.visited = visited;
	}

	public void addPerception(Perception perception) {
		this.perceptions.add(perception);
	}

	public void removePerception(Perception perception) {
		this.perceptions.remove(perception);
	}

	public void addElement(Element element) {
		this.elements.add(element);
	}

	public void removeElement(Element element) {
		this.elements.remove(element);
	}

	public Element getSingleElement() {
		if(!elements.isEmpty()) {
			return elements.stream().findFirst().get();
		}
		return null;
	}

}
