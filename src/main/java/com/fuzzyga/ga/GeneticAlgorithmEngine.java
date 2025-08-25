package com.fuzzyga.ga;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A modular and configurable engine for running a genetic algorithm.
 */
public class GeneticAlgorithmEngine {

    private static final Logger logger = LoggerFactory.getLogger(GeneticAlgorithmEngine.class);

    private final GaConfig config;
    private final FitnessEvaluator fitnessEvaluator;
    private final Random random = new Random();

    public GeneticAlgorithmEngine(GaConfig config, FitnessEvaluator fitnessEvaluator) {
        this.config = config;
        this.fitnessEvaluator = fitnessEvaluator;
    }

    /**
     * Evolves a population over a number of generations to find an optimal solution.
     *
     * @param initialPopulation An initial population. Can be null, in which case a random population is generated.
     * @param executorService Optional ExecutorService for parallel fitness evaluation. If null, evaluation is serial.
     * @return The best individual found after all generations.
     */
    public Individual evolve(List<Individual> initialPopulation, ExecutorService executorService) {
        // 1. Initialize Population
        List<Individual> population = (initialPopulation == null)
            ? initializePopulation()
            : initialPopulation;

        Individual bestEver = null;

        for (int generation = 0; generation < config.maxGenerations(); generation++) {
            // 2. Fitness Evaluation
            population = fitnessEvaluator.evaluatePopulation(population, executorService);
            Collections.sort(population); // Sorts by fitness, highest first

            Individual bestOfGeneration = population.get(0);
            if (bestEver == null || bestOfGeneration.fitness() > bestEver.fitness()) {
                bestEver = bestOfGeneration;
            }

            logger.info("Generation {}: Best Fitness = {}", generation, bestOfGeneration.fitness());

            // 3. Evolve New Generation
            List<Individual> newPopulation = new ArrayList<>();

            // Elitism
            int elitismCount = Math.min(config.elitismCount(), population.size());
            newPopulation.addAll(population.subList(0, elitismCount));

            // Crossover and Mutation
            while (newPopulation.size() < config.populationSize()) {
                // Selection
                Individual parent1 = select(population);
                Individual parent2 = select(population);

                // Crossover
                Chromosome childChromosome = (random.nextDouble() < config.crossoverRate())
                    ? crossover(parent1.chromosome(), parent2.chromosome())
                    : parent1.chromosome(); // Or just copy parent1

                // Mutation
                mutate(childChromosome);

                // Add new individual to population (fitness will be calculated next generation)
                newPopulation.add(new Individual(childChromosome, -1));
            }
            population = newPopulation;

            // Check for termination
             if (bestOfGeneration.fitness() >= config.fitnessThreshold()) {
                logger.info("Fitness threshold reached. Terminating evolution.");
                break;
            }
        }

        // Final evaluation of the last population
        population = fitnessEvaluator.evaluatePopulation(population, executorService);
        Collections.sort(population);
        Individual finalBest = population.get(0);

        return (bestEver != null && bestEver.fitness() > finalBest.fitness()) ? bestEver : finalBest;
    }

    private List<Individual> initializePopulation() {
        return IntStream.range(0, config.populationSize())
            .mapToObj(i -> new Individual(fitnessEvaluator.createRandomChromosome(), -1))
            .collect(Collectors.toList());
    }

    // --- GA OPERATORS ---

    private Individual select(List<Individual> population) {
        // Tournament Selection
        int tournamentSize = config.tournamentSize();
        List<Individual> tournament = new ArrayList<>();
        for (int i = 0; i < tournamentSize; i++) {
            tournament.add(population.get(random.nextInt(population.size())));
        }
        return Collections.min(tournament); // min because of inverted compareTo
    }

    private Chromosome crossover(Chromosome parent1, Chromosome parent2) {
        // Blend Crossover (BLX-alpha)
        double[] p1Genes = parent1.genes();
        double[] p2Genes = parent2.genes();
        double[] childGenes = new double[parent1.length()];
        double alpha = 0.5;

        for (int i = 0; i < parent1.length(); i++) {
            double d = Math.abs(p1Genes[i] - p2Genes[i]);
            double min = Math.min(p1Genes[i], p2Genes[i]) - alpha * d;
            double max = Math.max(p1Genes[i], p2Genes[i]) + alpha * d;
            childGenes[i] = min + random.nextDouble() * (max - min);
            // Clamping to original gene boundaries can be added here if needed
        }
        return new Chromosome(childGenes);
    }

    private void mutate(Chromosome chromosome) {
        // Gaussian Mutation
        double[] genes = chromosome.genes();
        for (int i = 0; i < chromosome.length(); i++) {
            if (random.nextDouble() < config.mutationRate()) {
                genes[i] += random.nextGaussian() * config.mutationStrength();
                // Clamping to original gene boundaries can be added here if needed
            }
        }
    }
}
