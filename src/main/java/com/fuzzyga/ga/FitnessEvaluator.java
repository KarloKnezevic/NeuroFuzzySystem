package com.fuzzyga.ga;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Defines the contract for a fitness evaluator.
 * <p>
 * The fitness evaluator acts as the bridge between the problem domain (e.g., a fuzzy system)
 * and the genetic algorithm. It is responsible for creating random chromosomes and for
 * calculating the fitness of individuals in a population.
 */
public interface FitnessEvaluator {

    /**
     * Evaluates the fitness of each individual in a population.
     *
     * @param population      The list of individuals to evaluate.
     * @param executorService An optional {@link ExecutorService} for parallel evaluation.
     *                        If null, the implementation should perform serial evaluation.
     * @return A new list of individuals with their fitness scores updated.
     */
    List<Individual> evaluatePopulation(List<Individual> population, ExecutorService executorService);

    /**
     * Creates a single, randomly initialized chromosome.
     * The structure and parameter ranges of the chromosome are specific to the problem domain.
     *
     * @return A new {@link Chromosome} with random gene values.
     */
    Chromosome createRandomChromosome();
}
