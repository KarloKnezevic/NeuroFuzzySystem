package hr.fer.zemris.neurofuzzysystem.expert.membershipFunctions;

import hr.fer.zemris.neurofuzzysystem.expert.input.IInput;
import hr.fer.zemris.neurofuzzysystem.expert.input.IMeasuredData;

public class Sigmoidal implements IConclusion {

	private IMeasuredData data;
	private double a;
	private double c;

	public Sigmoidal(IMeasuredData data, double a, double c) {
		this.a = a;
		this.c = c;
		this.data = data.getCopy();
	}

	@Override
	public double computeConclusion(IInput sensor) {
		data.pickData(sensor);
		return 1.0 / (1 + Math.pow(Math.E, (-a * (data.getX() - c))));
	}

	@Override
	public double computeInverseConclusion(double y) {
		data.setMemFun(y);
		double inv = Math.log((1 - y) / y) / (-a) + c;

		// provjera, je li dobiveno rjeĹˇenje u domeni
		if (inv >= data.getMinDomain() && inv <= data.getMaxDomain()) {
			data.setX(inv);
			return inv;
		} else {
			return 0.0;
		}
	}

	@Override
	public IMeasuredData getMeasuredData() {
		return data;
	}

	@Override
	public String toString() {
		return "Ď�[var:" + data.getDataName() + ", param:" + a + "," + c + "]";
	}
}