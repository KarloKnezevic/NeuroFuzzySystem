package hr.fer.zemris.neurofuzzysystem.anfis.rule;

import hr.fer.zemris.neurofuzzysystem.anfis.membershipFunction.MF1;
import hr.fer.zemris.neurofuzzysystem.anfis.tNorm.HamacherProduct;

public class Rule {

	public MF1 mfA;
	public MF1 mfB;
	public HamacherProduct norm;
	public Conclusion conclusion;

	public Rule(MF1 mfA, MF1 mfB, HamacherProduct norm, Conclusion conclusion) {
		this.mfA = mfA;
		this.mfB = mfB;
		this.norm = norm;
		this.conclusion = conclusion;
	}

	public double conclude(double x, double y) {
		return conclusion.compute(x, y);
	}

	public double weight(double x, double y) {
		return norm.computeNorm(mfA.compute(x), mfB.compute(y));
	}

}