package hr.fer.zemris.neurofuzzysystem.expert.norm;

import hr.fer.zemris.neurofuzzysystem.expert.input.IInput;
import hr.fer.zemris.neurofuzzysystem.expert.input.IMeasuredData;
import hr.fer.zemris.neurofuzzysystem.expert.membershipFunctions.IConclusion;

public class Concentration extends Norm {

	public Concentration() {
		param = 2.0;
	}

	public Concentration(double param) {
		this.param = param;
	}

	public Concentration(IConclusion arg) {
		// ukoliko drugaÄŤije definirano, koncentracija se raÄŤuna kao kvadrat
		// funkcije pripadnosti
		super(arg, 2);
	}

	public Concentration(IConclusion arg, double t) {
		// con(x) = mi(x)^t
		super(arg, t);
	}

	@Override
	public double computeConclusion(IInput sensor) {
		return Math.pow(arg1.computeConclusion(sensor), param);
	}

	@Override
	public double computeInverseConclusion(double y) {
		return arg1.computeInverseConclusion(Math.pow(y, 1 / param));
	}

	@Override
	public IMeasuredData getMeasuredData() {
		return arg1.getMeasuredData();
	}

	@Override
	public String toString() {
		return "( VRLO[" + param + "](" + arg1.toString() + ") )";
	}

	@Override
	public IConclusion x(IConclusion arg) {
		return new Concentration(arg, param);
	}
}