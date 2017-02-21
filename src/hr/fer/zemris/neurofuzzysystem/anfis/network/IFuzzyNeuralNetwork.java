package hr.fer.zemris.neurofuzzysystem.anfis.network;

import java.util.List;

import hr.fer.zemris.neurofuzzysystem.anfis.util.Pair;

/**
 * Interface IFuzzyNeuralNetwork.
 * 
 * @author Karlo Knezevic, karlo.knezevic@fer.hr
 *
 */
public interface IFuzzyNeuralNetwork {

	/**
	 * Učenje mreže do određene pogreške. Mogućnost beskonačnog učenja ukoliko
	 * se zaglavi u lokalnom optimumu.
	 * 
	 * @param learningDataset
	 * @param error
	 */
	public void learnNetworkError(List<Pair> learningDataset, double error);

	/**
	 * Učenje mreže određen broj epoha.
	 * 
	 * @param learningDataset
	 * @param epoch
	 */
	public void learnNetworkEpoch(List<Pair> learningDataset, int epoch);

	/**
	 * Ispis naučenih parametara u datoteku. Postoji posebna mapa za online i
	 * offline učenje.
	 */
	public void writeLearnedParams2File();

	/**
	 * Nakon učenja, pozivom ove metode, u datoteku se ispisuju relativne
	 * stvarnih vrijednosti i izlaza koje naučena mreža daje. (x, y, greška)
	 * Greška je definirana kao: stvarna vrijednost - vrijednost mreže
	 * 
	 * @param learningDataset
	 */
	public void writeRelativeError2File(List<Pair> learningDataset);

	/**
	 * Za svaku epohu učenja, u datoteku se ispisuje pogreška.
	 * 
	 * @param learningDataset
	 * @param epoch
	 */
	public void writeEpochError2File(List<Pair> learningDataset, int epoch);

	/**
	 * Za doređenu stopu učenja, u svakoj epohi učenja, u datoteku se ispisuje
	 * pogreška.
	 * 
	 * @param η
	 * @param learningDataset
	 * @param epoch
	 */
	public void writeEpochErrorForEta2File(double η, List<Pair> learningDataset, int epoch);

	/**
	 * Učenje optimalnog broja pravila za inicijalno postavljenu stopu učenja.
	 * 
	 * @param learningDataset
	 * @param startRuleNum
	 *            s koliko pravila počinjemo
	 * @param endRuleNum
	 *            s koliko pravila završavamo
	 * @param epochPerRule
	 *            koliko epoha učenja
	 */
	public void learnNetworkRules(List<Pair> learningDataset, int startRuleNum, int endRuleNum, int epochPerRule);

	/**
	 * Validacija naučene mreže.
	 * 
	 * @param learningDataset
	 */
	public void validateLearned(List<Pair> learningDataset);

}
