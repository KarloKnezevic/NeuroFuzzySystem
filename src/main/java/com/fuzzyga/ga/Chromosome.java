package com.fuzzyga.ga;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Represents a chromosome in the genetic algorithm.
 * <p>
 * This record is a simple, immutable data carrier for an array of doubles, which are the "genes".
 * It has no knowledge of what the genes represent; it is purely a data structure for the GA to manipulate.
 * The interpretation of these genes is handled by a separate decoder component.
 *
 * @param genes The array of double values representing the solution's parameters.
 */
public record Chromosome(double[] genes) implements Serializable {
    /**
     * Canonical constructor. Creates a defensive copy of the genes array to ensure immutability.
     */
    public Chromosome(double[] genes) {
        this.genes = Arrays.copyOf(genes, genes.length);
    }

    /**
     * Provides a defensive copy of the genes array to maintain immutability.
     *
     * @return A copy of the genes array.
     */
    @Override
    public double[] genes() {
        return Arrays.copyOf(genes, genes.length);
    }

    /**
     * Returns the number of genes in the chromosome.
     * @return The length of the genes array.
     */
    public int length() {
        return genes.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chromosome that = (Chromosome) o;
        return Arrays.equals(genes, that.genes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(genes);
    }

    @Override
    public String toString() {
        return "Chromosome{" +
               "genes=" + Arrays.toString(genes) +
               '}';
    }
}
