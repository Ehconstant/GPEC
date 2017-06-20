package com.cci.gpec.commons;

public class AccidentMetierBean extends MetierBean{

	private int nbAccidentTwoYearAgo;
	private int nbAccidentOneYearAgo;
	private int nbAccidentCurYear;
	
	private int nbAccidentTwoYearAgoWithArret;
	private int nbAccidentOneYearAgoWithArret;
	private int nbAccidentCurYearWithArret;
	
	public AccidentMetierBean() {
		super();
	}
	
	public int getNbAccidentTwoYearAgo() {
		return nbAccidentTwoYearAgo;
	}
	public void setNbAccidentTwoYearAgo(int nbAccidentTwoYearAgo) {
		this.nbAccidentTwoYearAgo = nbAccidentTwoYearAgo;
	}
	public int getNbAccidentOneYearAgo() {
		return nbAccidentOneYearAgo;
	}
	public void setNbAccidentOneYearAgo(int nbAccidentOneYearAgo) {
		this.nbAccidentOneYearAgo = nbAccidentOneYearAgo;
	}
	public int getNbAccidentCurYear() {
		return nbAccidentCurYear;
	}
	public void setNbAccidentCurYear(int nbAccidentCurYear) {
		this.nbAccidentCurYear = nbAccidentCurYear;
	}

	public int getNbAccidentTwoYearAgoWithArret() {
		return nbAccidentTwoYearAgoWithArret;
	}

	public void setNbAccidentTwoYearAgoWithArret(int nbAccidentTwoYearAgoWithArret) {
		this.nbAccidentTwoYearAgoWithArret = nbAccidentTwoYearAgoWithArret;
	}

	public int getNbAccidentOneYearAgoWithArret() {
		return nbAccidentOneYearAgoWithArret;
	}

	public void setNbAccidentOneYearAgoWithArret(int nbAccidentOneYearAgoWithArret) {
		this.nbAccidentOneYearAgoWithArret = nbAccidentOneYearAgoWithArret;
	}

	public int getNbAccidentCurYearWithArret() {
		return nbAccidentCurYearWithArret;
	}

	public void setNbAccidentCurYearWithArret(int nbAccidentCurYearWithArret) {
		this.nbAccidentCurYearWithArret = nbAccidentCurYearWithArret;
	}
	
	
}
