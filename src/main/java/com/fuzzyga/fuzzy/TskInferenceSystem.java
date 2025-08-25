package com.fuzzyga.fuzzy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implements a Takagi-Sugeno-Kang (TSK) Fuzzy Inference System.
 * <p>
 * This class orchestrates the entire fuzzy inference process, from fuzzification to defuzzification,
 * using a provided rule base. The system is designed to be immutable and generic, handling any number
 * of input variables and rules.
 */
public class TskInferenceSystem implements Serializable {

    private final List<FuzzyRule> ruleBase;

    /**
     * Constructs a TSK inference system with a specific set of rules.
     *
     * @param ruleBase The list of {@link FuzzyRule}s that defines the system's logic.
     *                 The list is copied to ensure the immutability of the system.
     */
    public TskInferenceSystem(List<FuzzyRule> ruleBase) {
        this.ruleBase = List.copyOf(ruleBase);
    }

    /**
     * A private helper record to store the intermediate result for each rule during calculation.
     */
    private record WeightedOutput(double firingStrength, double consequentValue) {}

    /**
     * Calculates the crisp output of the fuzzy system for a given set of crisp inputs.
     *
     * @param inputs A map where keys are {@link InputVariable}s and values are their crisp input values.
     * @return The final, defuzzified output value.
     */
    public double calculate(Map<InputVariable, Double> inputs) {
        if (ruleBase.isEmpty()) {
            return 0.0;
        }

        // For each rule, calculate its firing strength and its consequent's output value.
        List<WeightedOutput> weightedOutputs = ruleBase.stream()
            .map(rule -> {
                // 1. Calculate Firing Strength (α)
                // Get membership of each input in the corresponding fuzzy set of the rule's antecedent.
                List<Double> membershipValues = rule.antecedent().entrySet().stream()
                    .map(entry -> {
                        InputVariable var = entry.getKey();
                        var mf = entry.getValue();
                        double inputValue = inputs.getOrDefault(var, 0.0);
                        return mf.getMembership(inputValue);
                    })
                    .collect(Collectors.toList());

                // Aggregate the membership values using the AND operator (Einstein Product)
                // to get the rule's final firing strength.
                double firingStrength = FuzzyOperators.aggregateWithTNorm(membershipValues, FuzzyOperators.EINSTEIN_PRODUCT);

                // 2. Calculate Consequent Value (z)
                double consequentValue = rule.consequent().calculate(inputs);

                return new WeightedOutput(firingStrength, consequentValue);
            })
            .collect(Collectors.toList());

        // 3. Defuzzify (Aggregate)
        // Sum of all firing strengths (Σα).
        double totalFiringStrength = weightedOutputs.stream()
            .mapToDouble(WeightedOutput::firingStrength)
            .sum();

        // Avoid division by zero. If total firing strength is 0, no rules were activated.
        if (totalFiringStrength == 0) {
            return 0.0;
        }

        // Sum of each rule's firing strength times its consequent value (Σ(α * z)).
        double weightedConsequentSum = weightedOutputs.stream()
            .mapToDouble(wo -> wo.firingStrength() * wo.consequentValue())
            .sum();

        // Final output is the weighted average.
        return weightedConsequentSum / totalFiringStrength;
    }

    /**
     * Gets the list of rules that make up this system.
     *
     * @return An unmodifiable list of the fuzzy rules.
     */
    public List<FuzzyRule> getRuleBase() {
        return ruleBase;
    }
}
