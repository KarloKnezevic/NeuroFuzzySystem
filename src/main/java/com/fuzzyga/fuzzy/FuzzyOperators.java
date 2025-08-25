package com.fuzzyga.fuzzy;

import java.util.Collection;
import java.util.function.DoubleBinaryOperator;

/**
 * A utility class providing static methods for fuzzy logic operations.
 * <p>
 * This class centralizes the implementation of fuzzy operators like t-norms (AND) and s-norms (OR).
 */
public final class FuzzyOperators {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private FuzzyOperators() {
    }

    /**
     * The Einstein Product, a parametric t-norm used for the fuzzy AND operation.
     * <p>
     * Formula: T(a, b) = (a * b) / (2 - (a + b - a * b))
     */
    public static final DoubleBinaryOperator EINSTEIN_PRODUCT = (a, b) -> {
        double denominator = 2 - (a + b - a * b);
        // The denominator can only be zero if a=1 and b=1, in which case it is 2 - (1+1-1) = 1.
        // It's safe from division by zero for valid membership values [0, 1].
        return (a * b) / denominator;
    };

    /**
     * Aggregates a collection of membership values using a specified t-norm.
     * This is used to calculate the total firing strength of a rule's antecedent.
     *
     * @param memberships The collection of membership values (e.g., from each condition in the antecedent).
     * @param tNorm       The t-norm operator to apply (e.g., {@link #EINSTEIN_PRODUCT}).
     * @return The aggregated result. Returns 1.0 for an empty or null collection, representing the identity element for t-norms.
     */
    public static double aggregateWithTNorm(Collection<Double> memberships, DoubleBinaryOperator tNorm) {
        if (memberships == null || memberships.isEmpty()) {
            return 1.0; // Identity for t-norm
        }
        return memberships.stream()
                .mapToDouble(Double::doubleValue)
                .reduce(1.0, tNorm);
    }
}
