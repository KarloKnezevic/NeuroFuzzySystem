package hr.fer.zemris.neurofuzzysystem.anfisga.util;

/**
 * Pair.
 * 
 * @author Karlo Knezevic, karlo.knezevic@fer.hr
 *
 */
public class Pair {

	public double x;

	public double y;

	public double value;

	public Pair() {
	}

	public Pair(double x, double y) {
		this.x = x;
		this.y = y;
		this.value = Double.NaN;
	}

	public Pair(double x, double y, double value) {
		this.x = x;
		this.y = y;
		this.value = value;
	}

	@Override
	public String toString() {
		return x + " " + y + " " + value;
	}

}
