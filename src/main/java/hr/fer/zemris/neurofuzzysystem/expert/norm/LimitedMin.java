package hr.fer.zemris.neurofuzzysystem.expert.norm;

import hr.fer.zemris.neurofuzzysystem.expert.input.IInput;
import hr.fer.zemris.neurofuzzysystem.expert.membershipFunctions.IConclusion;

public class LimitedMin extends Norm {

	public LimitedMin() {
	}

	public LimitedMin(IConclusion arg1, IConclusion arg2) {
		super(arg1, arg2);
	}

	@Override
	public double computeConclusion(IInput sensor) {
		return Math.max(0, arg1.computeConclusion(sensor) + arg2.computeConclusion(sensor) - 1);
	}

	@Override
	public String toString() {
		return "( " + arg1.toString() + " I " + arg2.toString() + " )";
	}

	@Override
	public IConclusion x(IConclusion arg1, IConclusion arg2) {
		return new LimitedMin(arg1, arg2);
	}
}