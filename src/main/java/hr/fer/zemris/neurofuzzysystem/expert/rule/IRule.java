package hr.fer.zemris.neurofuzzysystem.expert.rule;

import hr.fer.zemris.neurofuzzysystem.expert.input.IInput;
import hr.fer.zemris.neurofuzzysystem.expert.membershipFunctions.IConclusion;

public interface IRule {
	public void makeConclusion(IInput premise);

	public IConclusion getConsequent();

	public IConclusion getAntecedent();

	public String getStringConsequent();

	public String getStringAntecedent();
}
