package hr.fer.zemris.neurofuzzysystem.anfisga.ga;

import hr.fer.zemris.neurofuzzysystem.anfisga.rule.Rule;

/**
 * Interface.
 * 
 * @author Karlo Knezevic, karlo.knezevic@fer.hr
 *
 */
public interface IFitness {

	public double compute(Rule[] rules, double x, double y);

}
