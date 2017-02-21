package hr.fer.zemris.neurofuzzysystem.anfisga.membershipFunction;

/**
 * FuzzyTriangle. Uses Λ function for membership value computing.
 * 
 * @author Karlo Knezevic, karlo.knezevic@fer.hr
 */
public class FuzzyTriangle {

	public double c;

	public double w;

	public double compute(double x) {

		// CAUTION: division by 0
		if (w == 0)
			return 0;

		return Λ(x, c - w, c, c + w);

	}

	/**
	 * Λ Function.
	 * 
	 * @param x
	 * @param α
	 * @param β
	 * @param γ
	 * @return membership value
	 */
	private double Λ(double x, double α, double β, double γ) {

		if (x < α) {
			return 0;
		} else if (x >= α && x < β) {
			return (x - α) / (β - α);
		} else if (x >= β && x < γ) {
			return (γ - x) / (γ - β);
		} else if (x >= γ) {
			return 0;
		}

		return 0;

	}

}
