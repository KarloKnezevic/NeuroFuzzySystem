package hr.fer.zemris.neurofuzzysystem.expert.conclusionEngine;

import hr.fer.zemris.neurofuzzysystem.expert.input.IInput;

public interface IConclusionEngine {

	public void readInput(IInput input);

	public void conclude();

	public int[] getConclusion();

}
