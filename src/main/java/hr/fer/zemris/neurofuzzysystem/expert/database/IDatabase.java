package hr.fer.zemris.neurofuzzysystem.expert.database;

import hr.fer.zemris.neurofuzzysystem.expert.rule.IRule;

public interface IDatabase {
	// dodaj pravilo
	public void addRule(IRule rule);

	// dodaj skup pravila
	public void addRules(IRule[] rules);

	// izbriši bazu
	public void dropDB();

	// izbriši pravilo
	public void deleteRule(int index);

	// vrati veličinu baze
	public int sizeDB();

	// vrati sva pravila
	public IRule[] returnAllRules();

	// vrati određeno pravilo
	public IRule returnRule(int index);

	// ispiši pravila u datoteku
	public void printDB2File();
}
