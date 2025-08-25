package com.fuzzyga.fuzzy;

import com.fuzzyga.fuzzy.membership.MembershipFunction;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a single IF-THEN rule in a Takagi-Sugeno-Kang (TSK) fuzzy system.
 * <p>
 * A rule is composed of an antecedent (the "IF" part) and a consequent (the "THEN" part).
 * This record is immutable and designed to be flexible, supporting any number of input variables.
 *
 * @param antecedent A map defining the "IF" conditions of the rule. Each entry maps an
 *                   {@link InputVariable} to a corresponding {@link MembershipFunction}.
 *                   For example, { (varX, mfLow), (varY, mfHigh) } represents "IF x is Low AND y is High".
 * @param consequent The {@link TskConsequent} that defines the output of the rule as a linear function.
 */
public record FuzzyRule(Map<InputVariable, MembershipFunction> antecedent, TskConsequent consequent) implements Serializable {

    /**
     * Canonical constructor to ensure the antecedent map is non-null and stored immutably.
     */
    public FuzzyRule(Map<InputVariable, MembershipFunction> antecedent, TskConsequent consequent) {
        // Use a TreeMap to ensure a consistent, predictable ordering of the antecedent parts.
        // This is crucial for reliably mapping the parameters to a GA chromosome.
        this.antecedent = Collections.unmodifiableMap(new TreeMap<>(antecedent));
        this.consequent = consequent;
    }
}
