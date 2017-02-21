package hr.fer.zemris.neurofuzzysystem.anfisga.ga;

import java.util.Random;

import hr.fer.zemris.neurofuzzysystem.anfisga.membershipFunction.FuzzyTriangle;
import hr.fer.zemris.neurofuzzysystem.anfisga.rule.Conclusion;
import hr.fer.zemris.neurofuzzysystem.anfisga.rule.Rule;
import hr.fer.zemris.neurofuzzysystem.anfisga.tNorm.EinsteinProduct;
import hr.fer.zemris.neurofuzzysystem.anfisga.util.Pair;

/**
 * Floating-point Chromosome.
 * 
 * @author Karlo Knezevic, karlo.knezevic@fer.hr
 *
 */
public class FloatChromosome implements Comparable<FloatChromosome> {

	double[] bits;

	double fitness;

	Rule[] rules;

	FloatChromosomeDecoder decoder;

	public FloatChromosome(FloatChromosomeDecoder decoder, boolean constConclusion) {
		this.decoder = decoder;
		// MAGIC NUMBER
		// STRICTLY CHROMOSOME ENCODING
		// c11|w11|c21|w21|c31|w31|c42|w42|c52|w52|c62|w62|p1|q1|r1|p2|q2|r2|...
		this.bits = new double[39];
		this.fitness = 0;
		createRules(constConclusion);
	}

	public FloatChromosome(FloatChromosomeDecoder decoder, boolean constConclusion, Random rand, Pair[] intervals) {
		this.decoder = decoder;

		if (intervals.length != 5) {
			System.err.println("It is necessary to specify exactly five parameters intervals.");
			System.exit(-1);
		}

		// MAGIC NUMBER
		// STRICTLY CHROMOSOME ENCODING
		// c11|w11|c21|w21|c31|w31|c42|w42|c52|w52|c62|w62|p1|q1|r1|p2|q2|r2|...
		this.bits = new double[39];
		this.fitness = 0;

		for (int i = 0; i < this.bits.length; i++) {

			bits[i] = i < 12 ? intervals[i % 2].x + (intervals[i % 2].y - intervals[i % 2].x) * rand.nextDouble()
					: intervals[i % 3 + 2].x + (intervals[i % 3 + 2].y - intervals[i % 3 + 2].x) * rand.nextDouble();

		}

		createRules(constConclusion);
	}

	private void createRules(boolean constConclusion) {
		// 9 rules
		// fixed number
		int RULES = 9;
		this.rules = new Rule[RULES];

		for (int i = 0; i < RULES; i++) {
			rules[i] = new Rule(new FuzzyTriangle(), new FuzzyTriangle(), new EinsteinProduct(),
					new Conclusion(constConclusion));
		}

	}

	public void mutateBit(int index, Random rand, Pair[] intervals) {

		bits[index] = index < 12
				? intervals[index % 2].x + (intervals[index % 2].y - intervals[index % 2].x) * rand.nextDouble()
				: intervals[index % 3 + 2].x
						+ (intervals[index % 3 + 2].y - intervals[index % 3 + 2].x) * rand.nextDouble();

	}

	@Override
	public int compareTo(FloatChromosome o) {
		if (this.fitness < o.fitness)
			return -1;
		if (this.fitness > o.fitness)
			return 1;
		return 0;
	}
}