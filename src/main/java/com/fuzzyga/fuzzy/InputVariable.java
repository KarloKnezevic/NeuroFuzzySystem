package com.fuzzyga.fuzzy;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents an input variable to the fuzzy system (e.g., "Temperature", "Pressure").
 * <p>
 * This record provides a type-safe, immutable representation of an input variable,
 * primarily identified by its name. Using a dedicated type instead of a raw String
 * improves clarity and prevents errors.
 *
 * @param name The unique name of the input variable.
 */
public record InputVariable(String name) implements Serializable, Comparable<InputVariable> {

    /**
     * Compact constructor to ensure the name is not null or blank.
     *
     * @param name The name of the input variable.
     */
    public InputVariable {
        Objects.requireNonNull(name, "Input variable name cannot be null.");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Input variable name cannot be blank.");
        }
    }

    @Override
    public int compareTo(InputVariable other) {
        return this.name.compareTo(other.name);
    }
}
