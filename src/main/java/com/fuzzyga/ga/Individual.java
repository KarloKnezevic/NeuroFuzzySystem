package com.fuzzyga.ga;

import java.io.Serializable;

/**
 * Represents an individual in the population of a genetic algorithm.
 * <p>
 * An individual is a wrapper around a {@link Chromosome} that also holds its calculated
 * fitness score. This allows the GA to sort and select individuals based on their performance.
 * This class is immutable and implements {@link Comparable} to allow for easy ranking,
 * with higher fitness being considered "better".
 *
 * @param chromosome The chromosome containing the genetic data for the solution.
 * @param fitness    The fitness score of the chromosome.
 */
public record Individual(Chromosome chromosome, double fitness) implements Comparable<Individual>, Serializable {

    /**
     * Compares this individual with another based on fitness.
     * The comparison is inverted (o.fitness - this.fitness) so that when a collection
     * of individuals is sorted, those with higher fitness appear first.
     *
     * @param other The other individual to compare to.
     * @return A negative integer, zero, or a positive integer as this individual's fitness
     * is greater than, equal to, or less than the other's.
     */
    @Override
    public int compareTo(Individual other) {
        return Double.compare(other.fitness, this.fitness);
    }
}
