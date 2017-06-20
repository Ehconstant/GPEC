package com.cci.gpec.commons;

public class PyramideDataBean implements Comparable<PyramideDataBean>{
	
	//valeur de l'age ou de l'ancienneté selon la pyramide a laquelle c'est destiné
	private Integer donnee;
	//'Homme' ou 'Femme'
	private String sexe;
	//Nombre de personnes
	private int nb;
	
	public PyramideDataBean(Integer donnee, String sexe, int nb){
		this.donnee = donnee;
		this.sexe = sexe;
		this.nb = nb;
	}

	public Integer getDonnee() {
		return donnee;
	}

	public void setDonnee(Integer donnee) {
		this.donnee = donnee;
	}

	public String getSexe() {
		return sexe;
	}

	public void setSexe(String sexe) {
		this.sexe = sexe;
	}

	public int getNb() {
		return nb;
	}

	public void setNb(int nb) {
		this.nb = nb;
	}

	public int compareTo(PyramideDataBean o) {
		
		if(getDonnee().equals(o.getDonnee()))
	    {
	      return getDonnee().compareTo(o.getDonnee());
	    }
	   return getDonnee().compareTo(o.getDonnee());

	}
}
