package com.fuzzyga.core.data;

import com.fuzzyga.fuzzy.InputVariable;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * Represents a single data point in a dataset, with multiple inputs and one expected output.
 *
 * @param inputs         An unmodifiable map of {@link InputVariable} to their crisp double values.
 * @param expectedOutput The single expected output value for this set of inputs.
 */
public record DataPoint(Map<InputVariable, Double> inputs, double expectedOutput) implements Serializable {

    public DataPoint(Map<InputVariable, Double> inputs, double expectedOutput) {
        this.inputs = Collections.unmodifiableMap(new HashMap<>(inputs));
        this.expectedOutput = expectedOutput;
    }
}
