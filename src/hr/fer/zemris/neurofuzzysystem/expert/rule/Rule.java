package hr.fer.zemris.neurofuzzysystem.expert.rule;

import hr.fer.zemris.neurofuzzysystem.expert.input.IInput;
import hr.fer.zemris.neurofuzzysystem.expert.membershipFunctions.IConclusion;

public class Rule implements IRule {

	private IConclusion antecedent;
	private IConclusion consequent;

	public Rule(IConclusion antecedent, IConclusion consequent) {
		this.antecedent = antecedent;
		this.consequent = consequent;
	}

	@Override
	public void makeConclusion(IInput premise) {
		consequent.computeInverseConclusion(antecedent.computeConclusion(premise));
	}

	@Override
	public IConclusion getConsequent() {
		return consequent;
	}

	@Override
	public IConclusion getAntecedent() {
		return antecedent;
	}

	@Override
	public String getStringConsequent() {
		return consequent.toString();
	}

	@Override
	public String getStringAntecedent() {
		return antecedent.toString();
	}
}