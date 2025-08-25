package hr.fer.zemris.neurofuzzysystem.expert.norm;

import hr.fer.zemris.neurofuzzysystem.expert.input.IInput;
import hr.fer.zemris.neurofuzzysystem.expert.membershipFunctions.IConclusion;

public class AlgebraicMax extends Norm {

	public AlgebraicMax() {
	}

	public AlgebraicMax(IConclusion arg1, IConclusion arg2) {
		super(arg1, arg2);
	}

	@Override
	public double computeConclusion(IInput sensor) {
		double a = arg1.computeConclusion(sensor);
		double b = arg2.computeConclusion(sensor);

		return a + b - a * b;
	}

	@Override
	public String toString() {
		return "( " + arg1.toString() + " ILI " + arg2.toString() + " )";
	}

	@Override
	public IConclusion x(IConclusion arg1, IConclusion arg2) {
		return new AlgebraicMax(arg1, arg2);
	}
}