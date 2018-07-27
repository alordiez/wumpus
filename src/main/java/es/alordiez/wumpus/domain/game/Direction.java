package es.alordiez.wumpus.domain.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Direction {
	NORTH("N")
	, SOUTH("S")
	, EAST("E")
	, WEST("W")
	;

	private String code;

	private static class Holder {
		static final Map<String, Direction> VALUES_LIST = new HashMap<>();
	}

	Direction(String code) {
		this.code = code;
		Holder.VALUES_LIST.put(code, this);
	}

	public String getCode() {
		return this.code;
	}

	public static List<String> getCodes() {
		return new ArrayList<>(Holder.VALUES_LIST.keySet());
	}

	public static Direction fromCode(String code) {
		return Holder.VALUES_LIST.get(code);
	}

}
