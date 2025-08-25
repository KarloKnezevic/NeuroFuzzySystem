package hr.fer.zemris.neurofuzzysystem.expert.membershipFunctions;

import hr.fer.zemris.neurofuzzysystem.expert.input.IInput;
import hr.fer.zemris.neurofuzzysystem.expert.input.IMeasuredData;

public interface IConclusion {
	public double computeConclusion(IInput sensor);

	public double computeInverseConclusion(double y);

	public IMeasuredData getMeasuredData();
}
