package com.fuzzyga.core.models;

import com.fuzzyga.fuzzy.InputVariable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Describes the architecture of a TSK fuzzy system to be optimized.
 * This blueprint is used by the fitness evaluator to understand the structure
 * of the chromosome and how to decode it into a functional fuzzy system.
 *
 * @param inputVariables        A list of the system's input variables. The order is important.
 * @param numFuzzySetsPerVariable The number of fuzzy sets for each input variable (e.g., 3 for Low, Medium, High).
 *                                For simplicity, this is the same for all variables.
 * @param parameterRanges       A map defining the valid min/max range for each type of parameter
 *                              (e.g., "center", "width", "consequent").
 */
public record FuzzySystemDescriptor(
    List<InputVariable> inputVariables,
    int numFuzzySetsPerVariable,
    Map<String, Range> parameterRanges
) implements Serializable {

    /**
     * A simple record to define a min-max range.
     *
     * @param min The minimum value in the range.
     * @param max The maximum value in the range.
     */
    public record Range(double min, double max) implements Serializable {
    }
}
