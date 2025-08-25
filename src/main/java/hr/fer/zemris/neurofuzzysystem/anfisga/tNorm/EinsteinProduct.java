package hr.fer.zemris.neurofuzzysystem.anfisga.tNorm;

/**
 * Einstein's Product. T-Norm.
 * 
 * @author Karlo Knezevic, karlo.knezevic@fer.hr
 *
 */
public class EinsteinProduct {

	public double computeNorm(double μA, double μB) {

		// 2 - (μA + μB - μA*μB) always different from zero

		return (μA * μB) / (2 - (μA + μB - μA * μB));
	}

}
