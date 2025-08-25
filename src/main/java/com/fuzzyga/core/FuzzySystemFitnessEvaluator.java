package com.fuzzyga.core;

import com.fuzzyga.core.data.DataPoint;
import com.fuzzyga.core.data.Dataset;
import com.fuzzyga.core.models.FuzzySystemDescriptor;
import com.fuzzyga.fuzzy.*;
import com.fuzzyga.fuzzy.membership.MembershipFunction;
import com.fuzzyga.ga.Chromosome;
import com.fuzzyga.ga.FitnessEvaluator;
import com.fuzzyga.ga.Individual;
import com.google.common.collect.Lists;


import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FuzzySystemFitnessEvaluator implements FitnessEvaluator {

    private final FuzzySystemDescriptor descriptor;
    private final Dataset dataset;
    private final Random random = new Random();
    private final int chromosomeLength;
    private final int antecedentGeneCount;

    public FuzzySystemFitnessEvaluator(FuzzySystemDescriptor descriptor, Dataset dataset) {
        this.descriptor = descriptor;
        this.dataset = dataset;
        this.antecedentGeneCount = descriptor.inputVariables().size() * descriptor.numFuzzySetsPerVariable() * 2;
        this.chromosomeLength = calculateChromosomeLength();
    }

    @Override
    public List<Individual> evaluatePopulation(List<Individual> population, ExecutorService executorService) {
        if (executorService != null) {
            return parallelEvaluate(population, executorService);
        } else {
            return serialEvaluate(population);
        }
    }

    @Override
    public Chromosome createRandomChromosome() {
        double[] genes = new double[chromosomeLength];
        FuzzySystemDescriptor.Range centerRange = descriptor.parameterRanges().get("center");
        FuzzySystemDescriptor.Range widthRange = descriptor.parameterRanges().get("width");
        FuzzySystemDescriptor.Range consequentRange = descriptor.parameterRanges().get("consequent");

        // Antecedent parameters (c and w for each membership function)
        for (int i = 0; i < antecedentGeneCount; i += 2) {
            genes[i] = random.nextDouble(centerRange.min(), centerRange.max()); // center
            genes[i + 1] = random.nextDouble(widthRange.min(), widthRange.max()); // width
        }

        // Consequent parameters
        for (int i = antecedentGeneCount; i < chromosomeLength; i++) {
            genes[i] = random.nextDouble(consequentRange.min(), consequentRange.max());
        }
        return new Chromosome(genes);
    }

    private TskInferenceSystem decode(Chromosome chromosome) {
        double[] genes = chromosome.genes();
        int geneIndex = 0;

        // 1. Decode Antecedent Membership Functions
        Map<InputVariable, List<MembershipFunction>> fuzzySets = new LinkedHashMap<>();
        for (InputVariable var : descriptor.inputVariables()) {
            List<MembershipFunction> setsForVar = new ArrayList<>();
            for (int i = 0; i < descriptor.numFuzzySetsPerVariable(); i++) {
                double center = genes[geneIndex++];
                double width = genes[geneIndex++];
                setsForVar.add(new MembershipFunction.TriangularMembershipFunction(center, width));
            }
            fuzzySets.put(var, setsForVar);
        }

        // 2. Generate Rule Base using Cartesian Product of fuzzy sets
        List<List<MembershipFunction>> setsToCombine = new ArrayList<>(fuzzySets.values());
        List<List<MembershipFunction>> ruleAntecedentCombinations = Lists.cartesianProduct(setsToCombine);

        List<FuzzyRule> rules = new ArrayList<>();
        for (List<MembershipFunction> combination : ruleAntecedentCombinations) {
            // Build the antecedent map for this rule
            Map<InputVariable, MembershipFunction> antecedent = new LinkedHashMap<>();
            for (int i = 0; i < descriptor.inputVariables().size(); i++) {
                antecedent.put(descriptor.inputVariables().get(i), combination.get(i));
            }

            // Decode the consequent for this rule
            Map<InputVariable, Double> coefficients = new LinkedHashMap<>();
            for (InputVariable var : descriptor.inputVariables()) {
                coefficients.put(var, genes[geneIndex++]);
            }
            double constant = genes[geneIndex++];
            TskConsequent consequent = new TskConsequent(coefficients, constant);

            rules.add(new FuzzyRule(antecedent, consequent));
        }

        return new TskInferenceSystem(rules);
    }

    private Individual evaluateIndividual(Individual individual) {
        if (individual.fitness() >= 0) {
            return individual; // Already evaluated (e.g., via elitism)
        }

        TskInferenceSystem system = decode(individual.chromosome());

        double totalError = dataset.dataPoints().stream()
            .mapToDouble(point -> {
                double actualOutput = system.calculate(point.inputs());
                double error = point.expectedOutput() - actualOutput;
                return error * error;
            })
            .sum();

        double mse = totalError / dataset.size();
        double fitness = 1.0 / (1.0 + mse);

        return new Individual(individual.chromosome(), fitness);
    }

    private List<Individual> serialEvaluate(List<Individual> population) {
        return population.stream().map(this::evaluateIndividual).collect(Collectors.toList());
    }

    private List<Individual> parallelEvaluate(List<Individual> population, ExecutorService executorService) {
        List<Future<Individual>> futures = population.stream()
            .map(ind -> executorService.submit(() -> evaluateIndividual(ind)))
            .collect(Collectors.toList());

        return futures.stream().map(f -> {
            try {
                return f.get();
            } catch (Exception e) {
                throw new RuntimeException("Error during parallel fitness evaluation", e);
            }
        }).collect(Collectors.toList());
    }

    private int calculateChromosomeLength() {
        int numInputs = descriptor.inputVariables().size();
        int numSetsPerVar = descriptor.numFuzzySetsPerVariable();

        // Part 1: Antecedent parameters (c, w for each membership function)
        int antecedentParams = numInputs * numSetsPerVar * 2;

        // Part 2: Consequent parameters (p, q, ... r for each rule)
        int numRules = (int) Math.pow(numSetsPerVar, numInputs);
        int consequentParamsPerRule = numInputs + 1; // one coefficient per input + one constant
        int consequentParams = numRules * consequentParamsPerRule;

        return antecedentParams + consequentParams;
    }
}
