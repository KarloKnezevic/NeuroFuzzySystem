package hr.fer.zemris.neurofuzzysystem.anfisga.main;

import java.util.Random;

import hr.fer.zemris.neurofuzzysystem.anfisga.dataset.Dataset;
import hr.fer.zemris.neurofuzzysystem.anfisga.ga.*;
import hr.fer.zemris.neurofuzzysystem.anfisga.network.*;
import hr.fer.zemris.neurofuzzysystem.anfisga.util.Pair;

/**
 * ANFISGA. ANFIS learned by Genetic Algorithm.
 * 
 * @author Karlo Knezevic, karlo.knezevic@fer.hr
 *
 */
public class AnfisGa {

	public static void main(String[] args) {

		// -------------------constants-------------------

		// dataset
		Dataset dataset = new Dataset();
		// conclusion const
		boolean constConclusion = false;

		// ----------GA const----------

		// crossover probability
		double χ = 0.7;
		// mutation probability
		double μ = 0.016;
		// dynamic χ an μ update
		boolean dynamicχμUpdate = false;
		// population size
		int populationSize = 30;
		// evaluation
		int evaluation = 1000000;
		// random double values for init chromosome
		Pair[] intervals = {
				// c
				new Pair(-5, 15),
				// w
				new Pair(1, 5),
				// p
				new Pair(-10, 10),
				// q
				new Pair(-10, 10),
				// r
				new Pair(-65, 65) };

		boolean parallelFitness = true;

		// genetic algorithm
		IFuzzyNeuralGenetic ga = new GeneticAlgorithm(χ, μ, populationSize,
				// generation = evaluation / size of population
				evaluation / populationSize, new Random(), new NetworkOutput(), constConclusion, intervals,
				parallelFitness, dynamicχμUpdate);

		// --------GA const end---------

		// ------------------end constants-----------------

		// --------------fuzzy-neural network--------------
		FuzzyNeuralNetwork network = new FuzzyNeuralNetwork(ga);

		// ----------------network training----------------
		network.train(dataset.getTrainingSet());

		// ------------------network rules-----------------
		network.writeLearnedRules();

		// ---------------------(x y z)--------------------
		network.writeXYnetZ(dataset.getTrainingSet());

		// ----------------------δ(x,y)--------------------
		network.writeRelativeError(dataset.getTrainingSet());

		// ---------------network validation---------------
		network.validateLearned(dataset.getTrainingSet());

		// ----------------mutation testing----------------
		if (constConclusion) {
			double[] M = { μ / 50, μ / 10, μ, μ * 10, μ * 50 };
			network.trainAndWriteAverageValue(M, 5, dataset.getTrainingSet());
		}

	}

}