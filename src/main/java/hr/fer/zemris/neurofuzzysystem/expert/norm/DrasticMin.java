package hr.fer.zemris.neurofuzzysystem.expert.norm;

import hr.fer.zemris.neurofuzzysystem.expert.input.IInput;
import hr.fer.zemris.neurofuzzysystem.expert.membershipFunctions.IConclusion;

public class DrasticMin extends Norm {

	public DrasticMin() {
	}

	public DrasticMin(IConclusion arg1, IConclusion arg2) {
		super(arg1, arg2);
	}

	@Override
	public double computeConclusion(IInput sensor) {
		double a = arg1.computeConclusion(sensor);
		double b = arg2.computeConclusion(sensor);

		if (Math.max(a, b) == 1) {
			return Math.min(a, b);
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return "( " + arg1.toString() + " I " + arg2.toString() + " )";
	}

	@Override
	public IConclusion x(IConclusion arg1, IConclusion arg2) {
		return new DrasticMin(arg1, arg2);
	}
}