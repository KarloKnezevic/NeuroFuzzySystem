package hr.fer.zemris.neurofuzzysystem.anfisga.ga;

import java.util.List;

import hr.fer.zemris.neurofuzzysystem.anfisga.rule.Rule;
import hr.fer.zemris.neurofuzzysystem.anfisga.util.Pair;

/**
 * Interface to the system which learns fuzzy-neural network.
 * 
 * @author Karlo Knezevic, karlo.knezevic@fer.hr
 *
 */
public interface IFuzzyNeuralGenetic {

	/**
	 * Rules learning.
	 * 
	 * @param learningDataset
	 * @return
	 */
	public Rule[] learn(List<Pair> learningDataset);

	/**
	 * Training more then one time. DOES NOT SET RULES; ONLY MAKES STATISTICS
	 * ABOUT BEST INDIVIDUAL THROUGH GENERATIONS!
	 * 
	 * @param μ
	 * @param times
	 * @param learningDataset
	 * @return
	 */
	public double[] trainAndGetBestAverageFitness(double μ, int times, List<Pair> learningDataset);

}
