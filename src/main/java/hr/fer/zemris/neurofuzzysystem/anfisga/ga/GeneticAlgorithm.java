package hr.fer.zemris.neurofuzzysystem.anfisga.ga;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import hr.fer.zemris.neurofuzzysystem.anfisga.rule.Rule;
import hr.fer.zemris.neurofuzzysystem.anfisga.util.Pair;

/**
 * GENETIC ALGORITHM.
 * 
 * Characteristics: o double chromosome o generation algorithm o embedded
 * elitism o crossing: simple arithmetic recombination o mutation: simple
 * mutation o selection: proportional selection (Roulette Wheel)
 * 
 * @author Karlo Knezevic, karlo.knezevic@fer.hr
 *
 */
public class GeneticAlgorithm implements IFuzzyNeuralGenetic {

	// crossover probability
	double χ0;
	double χ;

	// mutation probability
	double μ0;
	double μ;

	// dynamic update
	boolean dynamicχμUpdate;

	// population size
	int populationSize;
	// # generation
	int generations;
	// random
	Random rand;
	// fitness
	IFitness fitness;
	// best solution
	FloatChromosome best;

	// conclusion const?
	boolean constConclusion;
	// evaluation
	List<Pair> learningDataset;
	// double random interval
	Pair[] intervals;
	// parallel fitness
	boolean parallelFitness;

	/**
	 * Constructor.
	 * 
	 * @param χ
	 * @param μ
	 * @param populationSize
	 * @param generations
	 * @param rand
	 * @param fitness
	 * @param constConclusion
	 * @param intervals
	 * @param parallelFitness
	 */
	public GeneticAlgorithm(double χ0, double μ0, int populationSize, int generations, Random rand, IFitness fitness,
			boolean constConclusion, Pair[] intervals, boolean parallelFitness, boolean dynamicχμUpdate) {

		this.χ0 = χ0;
		this.μ0 = μ0;
		this.populationSize = populationSize;
		this.generations = generations;
		this.rand = rand;
		this.fitness = fitness;
		this.constConclusion = constConclusion;
		this.intervals = intervals;
		this.parallelFitness = parallelFitness;
		this.dynamicχμUpdate = dynamicχμUpdate;

	}

	/**
	 * GA MAIN METHOD
	 * 
	 * @param param
	 */
	private void runGA(double[] param) {

		FloatChromosomeDecoder decoder = new FloatChromosomeDecoder();

		FloatChromosome[] population = makePopulation(decoder, true);
		FloatChromosome[] newGeneration = makePopulation(decoder, false);

		evaluatePopulation(population);

		χ = χ0;
		μ = μ0;

		// generations
		for (int generation = 0; generation < generations; generation++) {

			// sort by fitness
			Arrays.sort(population);

			// elizism
			copy(population[0], newGeneration[0]);
			copy(population[1], newGeneration[1]);

			if (dynamicχμUpdate) {
				updateχ(generation);
				updateμ(generation);
			}

			for (int i = 1; i < populationSize / 2; i++) {
				FloatChromosome parent1 = chooseParent(population);
				FloatChromosome parent2 = chooseParent(population);
				FloatChromosome child1 = newGeneration[2 * i];
				FloatChromosome child2 = newGeneration[2 * i + 1];

				crossing(parent1, parent2, child1, child2);
				mutation(child1);
				mutation(child2);
			}

			FloatChromosome[] tmp = population;
			population = newGeneration;
			newGeneration = tmp;

			evaluatePopulation(population);

			best = null;
			for (int i = 0; i < populationSize; i++) {
				if (i == 0 || best.fitness > population[i].fitness) {
					best = population[i];
				}
			}

			if (param != null) {
				param[generation] += best.fitness;
				if (generation % 100 == 0) {
					System.out.println("Generation: " + generation + " Error: " + best.fitness + " Completeness:"
							+ (double) generation / generations * 100 + "%");
				}
			} else {
				System.out.println("Generation: " + generation + " Error: " + best.fitness + " Completeness:"
						+ (double) generation / generations * 100 + "%" + " χ:" + χ + " μ:" + μ);
			}

		}

	}

	/**
	 * Dynamicly updated mutation. Exponential decreasing.
	 * 
	 * @param generation
	 */
	private void updateμ(int generation) {

		double treshold = 0.05;

		// NOTE: update only if initial mutation less then treshold
		// otherwise, no updating.
		if (μ0 <= treshold) {
			// fall rate => e^(-FALL_RATE*x)
			μ = μ0 + (treshold - μ0) * (Math.exp(-10.0 * generation / generations));

		}

	}

	/**
	 * Dynamicly updated crossing. Exponential decreasing.
	 * 
	 * @param generation
	 */
	private void updateχ(int generation) {

		// fall rate => e^(-FALL_RATE*x)
		χ = χ0 + (1 - χ0) * (Math.exp(-10.0 * generation / generations));

	}

	/**
	 * Mutation. Each chromosome knows to mutate each bit.
	 * 
	 * @param child
	 */
	private void mutation(FloatChromosome child) {
		for (int i = 0; i < child.bits.length; i++) {
			if (rand.nextDouble() <= μ) {
				child.mutateBit(i, rand, intervals);
			}
		}

	}

	/**
	 * Crossing.
	 * 
	 * @param parent1
	 * @param parent2
	 * @param child1
	 * @param child2
	 */
	private void crossing(FloatChromosome parent1, FloatChromosome parent2, FloatChromosome child1,
			FloatChromosome child2) {

		if (rand.nextDouble() <= χ) {
			int hiasma = rand.nextInt(parent1.bits.length - 1) + 1;

			for (int i = 0; i < hiasma; i++) {
				child1.bits[i] = parent1.bits[i];
				child2.bits[i] = parent2.bits[i];
			}

			for (int i = hiasma; i < parent1.bits.length; i++) {
				double average = (parent1.bits[i] + parent2.bits[i]) / 2.0;
				child1.bits[i] = average;
				child2.bits[i] = average;
			}
		} else {
			for (int i = 0; i < parent1.bits.length; i++) {
				child1.bits[i] = parent1.bits[i];
				child2.bits[i] = parent2.bits[i];
			}
		}

	}

	/**
	 * Roulette Wheel.
	 * 
	 * @param population
	 * @return
	 */
	private FloatChromosome chooseParent(FloatChromosome[] population) {

		double σφ = 0;
		double maxφ = 0;

		for (int i = 0; i < population.length; i++) {
			σφ += population[i].fitness;
			if (i == 0 || maxφ < population[i].fitness) {
				maxφ = population[i].fitness;
			}
		}

		σφ = population.length * maxφ - σφ;
		double rnd = rand.nextDouble() * σφ;
		double Σ = 0;

		for (int i = 0; i < population.length; i++) {
			Σ += maxφ - population[i].fitness;
			if (rnd < Σ)
				return population[i];
		}

		return population[population.length - 1];
	}

	/**
	 * Copying.
	 * 
	 * @param org
	 * @param cpy
	 */
	private void copy(FloatChromosome org, FloatChromosome cpy) {

		for (int i = 0; i < org.bits.length; i++) {
			cpy.bits[i] = org.bits[i];
		}

	}

	/**
	 * Population evaluation.
	 * 
	 * @param population
	 */
	private void evaluatePopulation(FloatChromosome[] population) {

		if (parallelFitness) {
			// parallel evaluation
			parallelEvaluation(population);

		} else {
			// serial evaluation
			for (int i = 0; i < populationSize; i++) {
				evaluate(population[i]);
			}

		}

	}

	/**
	 * Parallel evaluation. Using ExecutorService and Fixed Thread Pool.
	 * 
	 * @param population
	 */
	private void parallelEvaluation(FloatChromosome[] population) {

		ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		List<Future<Void>> results = new ArrayList<>();

		for (int i = 0; i < populationSize; i++) {
			ParallelFitness pf = new ParallelFitness(population[i]);
			results.add(pool.submit(pf));
		}

		for (Future<Void> result : results) {
			try {
				result.get();
			} catch (InterruptedException | ExecutionException e) {
			}
		}

		pool.shutdown();

	}

	/**
	 * Serial Evaluation.
	 * 
	 * @param floatChromosome
	 */
	private void evaluate(FloatChromosome floatChromosome) {

		floatChromosome.decoder.cromosomeDecode(floatChromosome);

		double Σ = 0;
		for (int i = 0; i < learningDataset.size(); i++) {
			double x = learningDataset.get(i).x;
			double y = learningDataset.get(i).y;
			double z = learningDataset.get(i).value;

			double o = fitness.compute(floatChromosome.rules, x, y);

			Σ += 0.5 * (o - z) * (o - z);
		}

		if (learningDataset.size() > 0) {
			Σ /= learningDataset.size();
		}

		floatChromosome.fitness = Σ;

	}

	/**
	 * Population initialization.
	 * 
	 * @param decoder
	 * @param randomFill
	 * @return
	 */
	private FloatChromosome[] makePopulation(FloatChromosomeDecoder decoder, boolean randomFill) {

		FloatChromosome[] population = new FloatChromosome[populationSize];
		for (int i = 0; i < populationSize; i++) {
			if (randomFill) {
				population[i] = new FloatChromosome(decoder, constConclusion, rand, intervals);
			} else {
				population[i] = new FloatChromosome(decoder, constConclusion);
			}
		}

		return population;
	}

	/**
	 * Learning.
	 */
	@Override
	public Rule[] learn(List<Pair> learningDataset) {

		this.learningDataset = learningDataset;

		runGA(null);

		return best.rules;
	}

	/**
	 * Training one or more times.
	 */
	@Override
	public double[] trainAndGetBestAverageFitness(double μ, int times, List<Pair> learningDataset) {

		this.learningDataset = learningDataset;
		this.μ = μ;
		double[] bestAverage = new double[generations];
		Arrays.fill(bestAverage, 0.0);

		for (int i = 0; i < times; i++) {

			System.out.println(i + ". learning begins");

			runGA(bestAverage);
		}

		if (times != 0) {
			for (int i = 0; i < bestAverage.length; i++) {
				bestAverage[i] /= times;
			}
		}

		return bestAverage;

	}

	private class ParallelFitness implements Callable<Void> {

		FloatChromosome floatChromosome;

		public ParallelFitness(FloatChromosome floatChromosome) {
			this.floatChromosome = floatChromosome;
		}

		@Override
		public Void call() throws Exception {

			floatChromosome.decoder.cromosomeDecode(floatChromosome);

			double Σ = 0;
			for (int i = 0; i < learningDataset.size(); i++) {
				double x = learningDataset.get(i).x;
				double y = learningDataset.get(i).y;
				double z = learningDataset.get(i).value;

				double o = fitness.compute(floatChromosome.rules, x, y);

				Σ += 0.5 * (o - z) * (o - z);
			}

			if (learningDataset.size() > 0) {
				Σ /= learningDataset.size();
			}

			floatChromosome.fitness = Σ;

			return null;
		}

	}

}
