package hr.fer.zemris.neurofuzzysystem.anfisga.ga;

/**
 * Float Chromosome Decoder.
 * 
 * @author Karlo Knezevic, karlo.knezevic@fer.hr
 *
 */
public class FloatChromosomeDecoder {

	public void cromosomeDecode(FloatChromosome c) {

		// chromosome is fixed length -> 39 bits
		// fixed rules count -> 9
		// STRICTLY CHROMOSOME ENCODING
		// c11|w11|c21|w21|c31|w31|c42|w42|c52|w52|c62|w62|p1|q1|r1|p2|q2|r2|...

		int XStartIndex = 0;
		int YStartIndex = 6;
		int consequentStartIndex = 12;

		for (int i = 0; i < c.rules.length; i++) {
			c.rules[i].ftA.c = c.bits[XStartIndex++];
			c.rules[i].ftA.w = c.bits[XStartIndex--];

			c.rules[i].ftB.c = c.bits[YStartIndex++];
			c.rules[i].ftB.w = c.bits[YStartIndex++];

			c.rules[i].conclusion.p = c.bits[consequentStartIndex++];
			c.rules[i].conclusion.q = c.bits[consequentStartIndex++];
			c.rules[i].conclusion.r = c.bits[consequentStartIndex++];

			if ((i + 1) % 3 == 0) {
				XStartIndex += 2;
				YStartIndex = 6;
			}
		}

	}

}
