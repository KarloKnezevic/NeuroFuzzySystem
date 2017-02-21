package hr.fer.zemris.neurofuzzysystem.anfis.dataset;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.neurofuzzysystem.anfis.function.IFunction;
import hr.fer.zemris.neurofuzzysystem.anfis.util.Pair;

public class Dataset {

	private int min;
	private int max;
	private IFunction function;

	private List<Pair> trainingSet;

	public Dataset(int min, int max, IFunction function) {

		this.min = min;
		this.max = max;
		this.function = function;

		createLearningDataset();

	}

	private void createLearningDataset() {

		trainingSet = new ArrayList<>();

		for (int i = min; i <= max; i++) {
			for (int j = min; j <= max; j++) {
				trainingSet.add(new Pair(i, j, function.compute(i, j)));
			}
		}

	}

	public List<Pair> getTrainingSet() {
		return trainingSet;
	}

}