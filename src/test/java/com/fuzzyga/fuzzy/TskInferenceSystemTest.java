package com.fuzzyga.fuzzy;

import com.fuzzyga.fuzzy.membership.MembershipFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TskInferenceSystemTest {

    private InputVariable x, y;
    private TskInferenceSystem system;

    @BeforeEach
    void setUp() {
        // Define input variables
        x = new InputVariable("x");
        y = new InputVariable("y");

        // Define fuzzy sets (membership functions)
        // For variable x: "Low" is centered at 0, "High" is centered at 10
        MembershipFunction xLow = new MembershipFunction.TriangularMembershipFunction(0, 5); // Range [-5, 5]
        MembershipFunction xHigh = new MembershipFunction.TriangularMembershipFunction(10, 5); // Range [5, 15]

        // For variable y: "Slow" is centered at 0, "Fast" is centered at 10
        MembershipFunction ySlow = new MembershipFunction.TriangularMembershipFunction(0, 5); // Range [-5, 5]
        MembershipFunction yFast = new MembershipFunction.TriangularMembershipFunction(10, 5); // Range [5, 15]

        // Define consequents for two rules
        // Rule 1: z1 = 0.5x + 0.2y + 1
        TskConsequent consequent1 = new TskConsequent(Map.of(x, 0.5, y, 0.2), 1.0);
        // Rule 2: z2 = -0.1x + 0.8y + 2
        TskConsequent consequent2 = new TskConsequent(Map.of(x, -0.1, y, 0.8), 2.0);

        // Create rules
        // Rule 1: IF x is Low AND y is Slow THEN z1
        FuzzyRule rule1 = new FuzzyRule(Map.of(x, xLow, y, ySlow), consequent1);
        // Rule 2: IF x is High AND y is Fast THEN z2
        FuzzyRule rule2 = new FuzzyRule(Map.of(x, xHigh, y, yFast), consequent2);

        // Create the system
        system = new TskInferenceSystem(List.of(rule1, rule2));
    }

    @Test
    @DisplayName("TskInferenceSystem should calculate correct output for a given input")
    void testCalculate() {
        // Input values
        Map<InputVariable, Double> inputs = Map.of(x, 2.5, y, 7.5);

        // --- Manual Calculation ---
        // Expected memberships
        // For x=2.5: mf_xLow(2.5) is halfway up the slope from 0 to 5 -> 0.5
        //            mf_xHigh(2.5) is outside the triangle -> 0
        double mu_x_low = 0.5;
        double mu_x_high = 0.0;

        // For y=7.5: mf_ySlow(7.5) is outside the triangle -> 0
        //            mf_yFast(7.5) is halfway up the slope from 5 to 10 -> 0.5
        double mu_y_slow = 0.0;
        double mu_y_fast = 0.5;

        // Expected firing strengths (alpha) using Einstein Product: (a*b) / (2-(a+b-a*b))
        // Rule 1: IF x is Low (0.5) AND y is Slow (0.0)
        double alpha1 = (0.5 * 0.0) / (2 - (0.5 + 0.0 - 0.5 * 0.0)); // Should be 0
        assertEquals(0.0, alpha1, 1e-6);

        // Rule 2: IF x is High (0.0) AND y is Fast (0.5)
        double alpha2 = (0.0 * 0.5) / (2 - (0.0 + 0.5 - 0.0 * 0.5)); // Should be 0
        assertEquals(0.0, alpha2, 1e-6);

        // This input results in zero firing strength, let's pick a better one.
        inputs = Map.of(x, 2.5, y, 2.5);

        // --- Recalculate ---
        // For x=2.5: mf_xLow(2.5) -> 0.5
        // For y=2.5: mf_ySlow(2.5) -> 0.5
        mu_x_low = 0.5;
        mu_y_slow = 0.5;

        // For x=2.5: mf_xHigh(2.5) -> 0.0
        // For y=2.5: mf_yFast(2.5) -> 0.0
        mu_x_high = 0.0;
        mu_y_fast = 0.0;

        // Firing strengths
        // Rule 1: IF x is Low (0.5) AND y is Slow (0.5)
        alpha1 = (0.5 * 0.5) / (2 - (0.5 + 0.5 - 0.5*0.5)); // (0.25) / (2 - (1 - 0.25)) = 0.25 / 1.25 = 0.2
        assertEquals(0.2, alpha1, 1e-6);

        // Rule 2: IF x is High (0.0) AND y is Fast (0.0)
        alpha2 = 0.0;

        // Expected consequent outputs (z)
        // Rule 1: z1 = 0.5*2.5 + 0.2*2.5 + 1 = 1.25 + 0.5 + 1 = 2.75
        double z1 = 2.75;
        // Rule 2: z2 = -0.1*2.5 + 0.8*2.5 + 2 = -0.25 + 2.0 + 2 = 3.75
        double z2 = 3.75;

        // Expected final output: (alpha1*z1 + alpha2*z2) / (alpha1 + alpha2)
        double expectedOutput = (alpha1 * z1 + alpha2 * z2) / (alpha1 + alpha2);
        assertEquals(2.75, expectedOutput, 1e-6); // Since alpha2 is 0, output is just z1

        // --- Run the System ---
        double actualOutput = system.calculate(inputs);

        assertEquals(expectedOutput, actualOutput, 1e-6);
    }
}
