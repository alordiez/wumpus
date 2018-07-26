package es.alordiez.wumpus.service.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to perform positioning calculations
 * 
 * @author alvortega
 *
 */
public final class PositioningUtils {

	
	private PositioningUtils() {
	}

	/**
	 * Given (x,y) 2D position and the width of a board, returns its linear position
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @return
	 */
	public static int getLinearPosition(int x, int y, int width) {
		return (x + y * width);
	}

	/**
	 * Given (x,y) 2D position and width & height of a board, returns 4 linear
	 * positions of its direct neighbors.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	public static int[] getNeighbors(int x, int y, int width, int height) {
		int[] neighbors = { -1, -1, -1, -1 };

		int north = y - 1;
		int south = y + 1;
		int west = x - 1;
		int east = x + 1;

		if (north >= 0) {
			neighbors[0] = getLinearPosition(x, north, width);
		}
		if (south < height) {
			neighbors[2] = getLinearPosition(x, south, width);
		}
		if (east < width) {
			neighbors[1] = getLinearPosition(east, y, width);
		}
		if (west < width) {
			neighbors[3] = getLinearPosition(west, y, width);
		}

		return neighbors;
	}
	
	/**
	 * Given (x,y) 2D position and width & height of a board, returns 4 2D
	 * positions of its direct neighbors (null if no neighbor present).
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	public static Position2D[] getNeighbors(Position2D position, int width, int height) {
		Position2D[] neighbors = {null, null, null, null};

		int north = position.y - 1;
		int south = position.y + 1;
		int west = position.x - 1;
		int east = position.x + 1;

		if (north >= 0) {
			neighbors[0] = new Position2D(position.x, north);
		}
		if (south < height && south >= 0) {
			neighbors[2] = new Position2D(position.x, south);
		}
		if (east < width && east >= 0) {
			neighbors[1] = new Position2D(east, position.y);
		}
		if (west < width && west >= 0) {
			neighbors[3] = new Position2D(west, position.y);
		}
		
		return neighbors;
	}
}
