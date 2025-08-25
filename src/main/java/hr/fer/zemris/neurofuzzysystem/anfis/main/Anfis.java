package hr.fer.zemris.neurofuzzysystem.anfis.main;

import java.util.Random;

import hr.fer.zemris.neurofuzzysystem.anfis.dataset.*;
import hr.fer.zemris.neurofuzzysystem.anfis.function.*;
import hr.fer.zemris.neurofuzzysystem.anfis.network.*;

/**
 * ANFIS
 * 
 * @author Karlo Knezevic, karlo.knezevic@fer.hr
 */
public class Anfis {

	public static void main(String[] args) {

		// -------------------constants-------------------
		int RULES = 9;
		// 0.002 INITIAL MU
		// 0.006 BIG MU
		// 0.00002 SMALL MU
		double η = 0.002;

		int minDomain = -4;
		int maxDomain = 4;

		IFunction function = new F1();
		Dataset dataset = new Dataset(minDomain, maxDomain, function);
		// ------------------end constants-----------------

		// --------------fuzzy-neural network--------------
		// OnlineGradientDescent / OfflineGradientDescent
		IFuzzyNeuralNetwork network = new OfflineGradientDescent(RULES, η, new Random());

		// ----------------network learning----------------
		network.learnNetworkEpoch(dataset.getTrainingSet(), 15000);

		network.writeLearnedParams2File();

		network.writeRelativeError2File(dataset.getTrainingSet());

		network.writeEpochError2File(dataset.getTrainingSet(), 15000);

		// ---------------network validation---------------
		// network.validateLearned(dataset.getTrainingSet());

	}

}
