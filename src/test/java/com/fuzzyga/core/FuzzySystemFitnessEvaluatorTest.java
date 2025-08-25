package com.fuzzyga.core;

import com.fuzzyga.core.data.Dataset;
import com.fuzzyga.core.models.FuzzySystemDescriptor;
import com.fuzzyga.fuzzy.FuzzyRule;
import com.fuzzyga.fuzzy.InputVariable;
import com.fuzzyga.fuzzy.TskInferenceSystem;
import com.fuzzyga.fuzzy.membership.MembershipFunction;
import com.fuzzyga.ga.Chromosome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class FuzzySystemFitnessEvaluatorTest {

    private FuzzySystemFitnessEvaluator evaluator;
    private InputVariable x, y;

    @BeforeEach
    void setUp() {
        // 1. Define Descriptor for a 2-input, 2-set system
        x = new InputVariable("x");
        y = new InputVariable("y");
        List<InputVariable> inputs = List.of(x, y);
        int numFuzzySets = 2; // e.g., Low, High
        Map<String, FuzzySystemDescriptor.Range> ranges = Map.of(
            "center", new FuzzySystemDescriptor.Range(0, 1),
            "width", new FuzzySystemDescriptor.Range(0, 1),
            "consequent", new FuzzySystemDescriptor.Range(0, 1)
        );
        FuzzySystemDescriptor descriptor = new FuzzySystemDescriptor(inputs, numFuzzySets, ranges);

        // Dummy dataset, not used for decode test but required by constructor
        Dataset dataset = new Dataset(new ArrayList<>());
        evaluator = new FuzzySystemFitnessEvaluator(descriptor, dataset);
    }

    @Test
    @DisplayName("Should correctly decode a chromosome into a TskInferenceSystem")
    void testDecode() {
        // 2. Create a chromosome with known gene values
        // Structure: 2 inputs, 2 sets => 2*2*2=8 antecedent params
        //            2^2=4 rules, 2+1=3 consequent params/rule => 4*3=12 consequent params
        //            Total genes = 8 + 12 = 20
        double[] genes = {
            // Antecedent: x-low(c,w), x-high(c,w), y-low(c,w), y-high(c,w)
            0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8,
            // Consequent for Rule 1 (x-low, y-low): p, q, r
            1.1, 1.2, 1.3,
            // Consequent for Rule 2 (x-low, y-high): p, q, r
            2.1, 2.2, 2.3,
            // Consequent for Rule 3 (x-high, y-low): p, q, r
            3.1, 3.2, 3.3,
            // Consequent for Rule 4 (x-high, y-high): p, q, r
            4.1, 4.2, 4.3
        };
        Chromosome chromosome = new Chromosome(genes);

        // 3. Decode the chromosome
        TskInferenceSystem system = evaluator.decode(chromosome);
        List<FuzzyRule> rules = system.getRuleBase();

        // 4. Assertions
        assertEquals(4, rules.size(), "Should be 4 rules for a 2-input, 2-set system");

        // Check Rule 1: IF x is Low AND y is Low
        FuzzyRule rule1 = rules.get(0);
        // Check antecedent
        MembershipFunction xLowMf = rule1.antecedent().get(x);
        assertInstanceOf(MembershipFunction.TriangularMembershipFunction.class, xLowMf);
        assertEquals(0.1, ((MembershipFunction.TriangularMembershipFunction) xLowMf).center());
        assertEquals(0.2, ((MembershipFunction.TriangularMembershipFunction) xLowMf).width());
        // Check consequent
        assertEquals(1.1, rule1.consequent().coefficients().get(x));
        assertEquals(1.2, rule1.consequent().coefficients().get(y));
        assertEquals(1.3, rule1.consequent().constant());

        // Check Rule 4: IF x is High AND y is High
        FuzzyRule rule4 = rules.get(3);
        // Check antecedent
        MembershipFunction xHighMf = rule4.antecedent().get(x);
        assertInstanceOf(MembershipFunction.TriangularMembershipFunction.class, xHighMf);
        assertEquals(0.3, ((MembershipFunction.TriangularMembershipFunction) xHighMf).center());
        assertEquals(0.4, ((MembershipFunction.TriangularMembershipFunction) xHighMf).width());
        // Check consequent
        assertEquals(4.1, rule4.consequent().coefficients().get(x));
        assertEquals(4.2, rule4.consequent().coefficients().get(y));
        assertEquals(4.3, rule4.consequent().constant());
    }
}
