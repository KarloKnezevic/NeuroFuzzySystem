package hr.fer.zemris.neurofuzzysystem.anfisga.rule;

/**
 * Conclusion.
 * 
 * @author Karlo Knazevic, karlo.knezevic@fer.hr
 *
 */
public class Conclusion {

	public double p;

	public double q;

	public double r;

	private boolean constant;

	/**
	 * Construcotr. Conclusion is constant or linear combination of inputs.
	 * 
	 * @param constant
	 */
	public Conclusion(boolean constant) {
		this.constant = constant;
	}

	public double compute(double x, double y) {

		// z = r
		if (constant)
			return r;

		// z = x*p + y*q + r
		return x * p + y * q + r;
	}

}
