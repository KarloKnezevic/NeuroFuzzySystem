package hr.fer.zemris.neurofuzzysystem.anfisga.network;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

import hr.fer.zemris.neurofuzzysystem.anfisga.ga.IFuzzyNeuralGenetic;
import hr.fer.zemris.neurofuzzysystem.anfisga.rule.Rule;
import hr.fer.zemris.neurofuzzysystem.anfisga.util.Pair;

/**
 * Fuzzy Neural Network.
 * 
 * @author Karlo Knezevic, karlo.knezevic@fer.hr
 *
 */
public class FuzzyNeuralNetwork {

	private Rule[] rules;

	// interface
	private IFuzzyNeuralGenetic fuzzyNeuralGenetic;

	private NetworkOutput output;

	/**
	 * Constructor.
	 * 
	 * @param fuzzyNeuralGenetic
	 */
	public FuzzyNeuralNetwork(IFuzzyNeuralGenetic fuzzyNeuralGenetic) {

		this.fuzzyNeuralGenetic = fuzzyNeuralGenetic;

		output = new NetworkOutput();

	}

	/**
	 * Computing Network Output For Input.
	 * 
	 * @param x
	 * @param y
	 */
	private double compute(double x, double y) {

		return output.compute(rules, x, y);

	}

	/**
	 * Online Network Learning.
	 * 
	 * @param learningDataset
	 * @param error
	 */
	private void learnNetwork(List<Pair> learningDataset) {

		rules = fuzzyNeuralGenetic.learn(learningDataset);

	}

	/**
	 * Network training, rules learning.
	 * 
	 * @param learningDataset
	 */
	public void train(List<Pair> learningDataset) {

		learnNetwork(learningDataset);

	}

	/**
	 * Validation.
	 * 
	 * @param learningDataset
	 */
	public void validateLearned(List<Pair> learningDataset) {

		for (int i = 0; i < learningDataset.size(); i++) {
			double x = learningDataset.get(i).x;
			double y = learningDataset.get(i).y;
			double value = learningDataset.get(i).value;

			double f = compute(x, y);

			System.out.println("Learned: " + f + " Real: " + value + " Error: " + Math.abs(f - value));
		}

	}

	/**
	 * For each input data (X,Y), print network output.
	 * 
	 * @param learningDataset
	 */
	public void writeXYnetZ(List<Pair> learningDataset) {

		String filename = "res/learnedSystem3D.txt";

		Writer bw = null;

		try {
			bw = new BufferedWriter(
					new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(filename)), "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		PrintWriter pw = new PrintWriter(bw);

		for (int i = 0; i < learningDataset.size(); i++) {
			double x = learningDataset.get(i).x;
			double y = learningDataset.get(i).y;

			double f = compute(x, y);

			pw.println(x + " " + y + " " + f);

			System.out.println("X: " + x + " Y: " + y + " Z: " + f);

		}

		pw.close();

	}

	/**
	 * Printing the relative error between the learned and the true value.
	 * 
	 * @param learningDataset
	 */
	public void writeRelativeError(List<Pair> learningDataset) {

		String filename = "res/relativeError.txt";

		Writer bw = null;

		try {
			bw = new BufferedWriter(
					new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(filename)), "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		PrintWriter pw = new PrintWriter(bw);

		for (int i = 0; i < learningDataset.size(); i++) {
			double yk = learningDataset.get(i).value;
			double ok = compute(learningDataset.get(i).x, learningDataset.get(i).y);

			double error = ok - yk;

			pw.println(learningDataset.get(i).x + " " + learningDataset.get(i).y + " " + error);

			System.out
					.println("X: " + learningDataset.get(i).x + " Y: " + learningDataset.get(i).y + " ERROR: " + error);

		}

		pw.close();

	}

	/**
	 * Print rules.
	 */
	public void writeLearnedRules() {

		String filename = "res/learnedRules.txt";

		Writer bw = null;

		try {
			bw = new BufferedWriter(
					new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(filename)), "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		PrintWriter pw = new PrintWriter(bw);

		for (int i = 0; i < rules.length; i++) {

			pw.println(rules[i].ftA.c + " " + rules[i].ftA.w + " " + rules[i].ftB.c + " " + rules[i].ftB.w + " "
					+ rules[i].conclusion.p + " " + rules[i].conclusion.q + " " + rules[i].conclusion.r);
		}

		pw.close();
	}

	/**
	 * Print best individual average fitness through generations.
	 * 
	 * @param μ
	 *            mutation
	 * @param times
	 * @param learningDataset
	 */
	public void trainAndWriteAverageValue(double μ, int times, List<Pair> learningDataset) {

		double[] average = fuzzyNeuralGenetic.trainAndGetBestAverageFitness(μ, times, learningDataset);

		String filename = "res/average_" + μ + "_.txt";

		Writer bw = null;

		try {
			bw = new BufferedWriter(
					new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(filename)), "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		PrintWriter pw = new PrintWriter(bw);

		System.out.println("Printing...");
		for (int i = 0; i < average.length; i++) {

			pw.println(i + " " + average[i]);

			System.out.println(i + " " + average[i]);

		}

		pw.close();

	}

	/**
	 * Print best individual average fitness through generations for each
	 * mutation probability value.
	 * 
	 * @param μ
	 *            array of mutation probabilities
	 * @param times
	 * @param learningDataset
	 */
	public void trainAndWriteAverageValue(double[] μ, int times, List<Pair> learningDataset) {

		for (int i = 0; i < μ.length; i++) {

			trainAndWriteAverageValue(μ[i], times, learningDataset);

		}

	}

}