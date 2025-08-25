package com.fuzzyga.ga;

import java.io.Serializable;

/**
 * A configuration record for the Genetic Algorithm.
 * <p>
 * This record holds all the hyperparameters that control the behavior of the
 * {@link GeneticAlgorithmEngine}. It is immutable and serves as a single source
 * of configuration.
 *
 * @param populationSize     The number of individuals in the population.
 * @param maxGenerations     The maximum number of generations to evolve.
 * @param elitismCount       The number of the best individuals to carry over to the next generation.
 * @param crossoverRate      The probability (0.0 to 1.0) that a crossover operation will occur.
 * @param mutationRate       The probability (0.0 to 1.0) that a mutation operation will occur for each gene.
 * @param mutationStrength   The standard deviation of the Gaussian distribution used for mutation.
 * @param tournamentSize     The number of individuals to select for a tournament selection.
 * @param fitnessThreshold   A fitness level that, if reached by the best individual, will terminate the evolution early.
 */
public record GaConfig(
    int populationSize,
    int maxGenerations,
    int elitismCount,
    double crossoverRate,
    double mutationRate,
    double mutationStrength,
    int tournamentSize,
    double fitnessThreshold
) implements Serializable {
}
