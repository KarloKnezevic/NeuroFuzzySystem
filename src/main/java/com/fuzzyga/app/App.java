package com.fuzzyga.app;

import com.fuzzyga.core.FuzzySystemFitnessEvaluator;
import com.fuzzyga.core.data.Dataset;
import com.fuzzyga.core.models.FuzzySystemDescriptor;
import com.fuzzyga.fuzzy.InputVariable;
import com.fuzzyga.fuzzy.TskInferenceSystem;
import com.fuzzyga.ga.GaConfig;
import com.fuzzyga.ga.GeneticAlgorithmEngine;
import com.fuzzyga.ga.Individual;
import com.fuzzyga.utils.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        try {
            // 1. Load Configuration
            GaConfig config = ConfigLoader.loadConfig("config.properties");
            logger.info("Configuration loaded: {}", config);

            // 2. Load Dataset
            Dataset dataset = Dataset.fromResource("datasets/3dfunc.txt");
            logger.info("Dataset loaded with {} data points.", dataset.size());

            // 3. Define the Fuzzy System Architecture
            FuzzySystemDescriptor descriptor = createFuzzySystemDescriptor();
            logger.info("Fuzzy System Descriptor created.");

            // 4. Create the Fitness Evaluator
            FuzzySystemFitnessEvaluator evaluator = new FuzzySystemFitnessEvaluator(descriptor, dataset);

            // 5. Create and Run the Genetic Algorithm
            GeneticAlgorithmEngine ga = new GeneticAlgorithmEngine(config, evaluator);
            logger.info("Starting Genetic Algorithm evolution...");

            Individual bestSolution = ga.evolve(null, null);

            // 6. Print Results
            logger.info("----------------------------------------------------");
            logger.info("Evolution finished.");
            logger.info("Best solution fitness: {}", bestSolution.fitness());
            logger.info("Best solution chromosome: {}", Arrays.toString(bestSolution.chromosome().genes()));
            logger.info("----------------------------------------------------");


            // 7. Demonstrate the optimized system
            logger.info("Demonstrating the optimized fuzzy system:");
            TskInferenceSystem optimizedSystem = evaluator.decode(bestSolution.chromosome());

            // Create a sample input
            Map<InputVariable, Double> sampleInput = Map.of(
                descriptor.inputVariables().get(0), 3.0,
                descriptor.inputVariables().get(1), -2.0
            );

            // Get the prediction
            double prediction = optimizedSystem.calculate(sampleInput);
            logger.info("Prediction for input {}: {}", sampleInput, prediction);


        } catch (IOException e) {
            logger.error("Failed to run application", e);
        }
    }

    private static FuzzySystemDescriptor createFuzzySystemDescriptor() {
        // Define the input variables for the system
        InputVariable x = new InputVariable("x");
        InputVariable y = new InputVariable("y");
        List<InputVariable> inputs = List.of(x, y);

        // Define the number of fuzzy sets for each variable (e.g., Low, Medium, High)
        int numFuzzySets = 3;

        // Define the valid ranges for the parameters that the GA will optimize
        Map<String, FuzzySystemDescriptor.Range> parameterRanges = Map.of(
            "center", new FuzzySystemDescriptor.Range(-5.0, 15.0),
            "width", new FuzzySystemDescriptor.Range(1.0, 5.0),
            "consequent", new FuzzySystemDescriptor.Range(-10.0, 10.0)
        );

        return new FuzzySystemDescriptor(inputs, numFuzzySets, parameterRanges);
    }
}
