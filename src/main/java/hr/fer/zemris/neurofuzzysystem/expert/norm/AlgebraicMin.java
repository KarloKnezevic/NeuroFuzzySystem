package hr.fer.zemris.neurofuzzysystem.expert.norm;

import hr.fer.zemris.neurofuzzysystem.expert.input.IInput;
import hr.fer.zemris.neurofuzzysystem.expert.membershipFunctions.IConclusion;

public class AlgebraicMin extends Norm {

	public AlgebraicMin() {
	}

	public AlgebraicMin(IConclusion arg1, IConclusion arg2) {
		super(arg1, arg2);
	}

	@Override
	public double computeConclusion(IInput sensor) {
		return arg1.computeConclusion(sensor) * arg2.computeConclusion(sensor);
	}

	@Override
	public String toString() {
		return "( " + arg1.toString() + " I " + arg2.toString() + " )";
	}

	@Override
	public IConclusion x(IConclusion arg1, IConclusion arg2) {
		return new AlgebraicMin(arg1, arg2);
	}
}