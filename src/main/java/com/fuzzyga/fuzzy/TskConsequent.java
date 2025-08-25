package com.fuzzyga.fuzzy;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents the consequent part (the "THEN" part) of a Takagi-Sugeno-Kang (TSK) fuzzy rule.
 * <p>
 * In a TSK system, the consequent is a linear function of the input variables.
 * This record an immutable representation of such a function, e.g., {@code z = (p * x) + (q * y) + ... + r}.
 *
 * @param coefficients A map where keys are {@link InputVariable}s and values are their corresponding coefficients (e.g., p, q).
 *                     Using a TreeMap ensures a consistent ordering of coefficients.
 * @param constant     The constant term of the linear function (e.g., r).
 */
public record TskConsequent(Map<InputVariable, Double> coefficients, double constant) implements Serializable {

    /**
     * Canonical constructor to ensure the coefficients map is non-null and stored immutably.
     */
    public TskConsequent(Map<InputVariable, Double> coefficients, double constant) {
        // Use a TreeMap to ensure a consistent, predictable ordering of the coefficients.
        // This is crucial for reliably mapping the parameters to a GA chromosome.
        this.coefficients = Collections.unmodifiableMap(new TreeMap<>(coefficients));
        this.constant = constant;
    }

    /**
     * Computes the output of the consequent function for a given set of input values.
     *
     * @param inputs A map containing the input variables and their current crisp values.
     * @return The calculated output of the linear function.
     */
    public double calculate(Map<InputVariable, Double> inputs) {
        double result = coefficients.entrySet().stream()
                .mapToDouble(entry -> {
                    InputVariable var = entry.getKey();
                    double coefficient = entry.getValue();
                    // If an input value is not provided, treat it as 0.
                    return inputs.getOrDefault(var, 0.0) * coefficient;
                })
                .sum();
        return result + constant;
    }

    /**
     * A constant consequent is a special case where all coefficients are zero.
     * @return true if the consequent is constant, false otherwise.
     */
    public boolean isConstant() {
        return coefficients.values().stream().allMatch(v -> v == 0.0);
    }
}
