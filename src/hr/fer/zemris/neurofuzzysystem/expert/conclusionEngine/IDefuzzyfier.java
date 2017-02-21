package hr.fer.zemris.neurofuzzysystem.expert.conclusionEngine;

import hr.fer.zemris.neurofuzzysystem.expert.rule.IRule;

public interface IDefuzzyfier {
	public double defuzzyfy(IRule[] concludedRules);
}
