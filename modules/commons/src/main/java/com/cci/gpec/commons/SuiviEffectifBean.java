package com.cci.gpec.commons;

public class SuiviEffectifBean {

	private String libelle;

	private Integer effectif;
	private String moyenneAge;
	private String moyenneAnciennete;
	private Integer effectifH;
	private String moyenneAgeH;
	private String moyenneAncienneteH;
	private Integer effectifF;
	private String moyenneAgeF;
	private String moyenneAncienneteF;

	private Integer effectifN1;
	private String moyenneAgeN1;
	private String moyenneAncienneteN1;
	private Integer effectifHN1;
	private String moyenneAgeHN1;
	private String moyenneAncienneteHN1;
	private Integer effectifFN1;
	private String moyenneAgeFN1;
	private String moyenneAncienneteFN1;

	private Integer effectifN2;
	private String moyenneAgeN2;
	private String moyenneAncienneteN2;
	private Integer effectifHN2;
	private String moyenneAgeHN2;
	private String moyenneAncienneteHN2;
	private Integer effectifFN2;
	private String moyenneAgeFN2;
	private String moyenneAncienneteFN2;

	private boolean footer;

	public boolean isFooter() {
		return footer;
	}

	public void setFooter(boolean footer) {
		this.footer = footer;
	}

	public Integer getEffectif() {
		return effectif;
	}

	public void setEffectif(Integer effectif) {
		this.effectif = effectif;
	}

	public String getMoyenneAge() {
		if (moyenneAge.equals("0.00") && effectif == 0) {
			return "-";
		} else {
			return moyenneAge;
		}
	}

	public void setMoyenneAge(String moyenneAge) {
		this.moyenneAge = moyenneAge;
	}

	public String getMoyenneAnciennete() {
		if (moyenneAnciennete.equals("0.00") && effectif == 0) {
			return "-";
		} else {
			return moyenneAnciennete;
		}
	}

	public void setMoyenneAnciennete(String moyenneAnciennete) {
		this.moyenneAnciennete = moyenneAnciennete;
	}

	public Integer getEffectifH() {
		return effectifH;
	}

	public void setEffectifH(Integer effectifH) {
		this.effectifH = effectifH;
	}

	public String getMoyenneAgeH() {
		if (moyenneAgeH.equals("0.00") && effectifH == 0) {
			return "-";
		} else {
			return moyenneAgeH;
		}
	}

	public void setMoyenneAgeH(String moyenneAgeH) {
		this.moyenneAgeH = moyenneAgeH;
	}

	public String getMoyenneAncienneteH() {
		if (moyenneAncienneteH.equals("0.00") && effectifH == 0) {
			return "-";
		} else {
			return moyenneAncienneteH;
		}
	}

	public void setMoyenneAncienneteH(String moyenneAncienneteH) {
		this.moyenneAncienneteH = moyenneAncienneteH;
	}

	public Integer getEffectifF() {
		return effectifF;
	}

	public void setEffectifF(Integer effectifF) {
		this.effectifF = effectifF;
	}

	public String getMoyenneAgeF() {
		if (moyenneAgeF.equals("0.00") && effectifF == 0) {
			return "-";
		} else {
			return moyenneAgeF;
		}
	}

	public void setMoyenneAgeF(String moyenneAgeF) {
		this.moyenneAgeF = moyenneAgeF;
	}

	public String getMoyenneAncienneteF() {
		if (moyenneAncienneteF.equals("0.00") && effectifF == 0) {
			return "-";
		} else {
			return moyenneAncienneteF;
		}
	}

	public void setMoyenneAncienneteF(String moyenneAncienneteF) {
		this.moyenneAncienneteF = moyenneAncienneteF;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Integer getEffectifN1() {
		return effectifN1;
	}

	public void setEffectifN1(Integer effectifN1) {
		this.effectifN1 = effectifN1;
	}

	public String getMoyenneAgeN1() {
		return moyenneAgeN1;
	}

	public void setMoyenneAgeN1(String moyenneAgeN1) {
		this.moyenneAgeN1 = moyenneAgeN1;
	}

	public String getMoyenneAncienneteN1() {
		return moyenneAncienneteN1;
	}

	public void setMoyenneAncienneteN1(String moyenneAncienneteN1) {
		this.moyenneAncienneteN1 = moyenneAncienneteN1;
	}

	public Integer getEffectifHN1() {
		return effectifHN1;
	}

	public void setEffectifHN1(Integer effectifHN1) {
		this.effectifHN1 = effectifHN1;
	}

	public String getMoyenneAgeHN1() {
		return moyenneAgeHN1;
	}

	public void setMoyenneAgeHN1(String moyenneAgeHN1) {
		this.moyenneAgeHN1 = moyenneAgeHN1;
	}

	public String getMoyenneAncienneteHN1() {
		return moyenneAncienneteHN1;
	}

	public void setMoyenneAncienneteHN1(String moyenneAncienneteHN1) {
		this.moyenneAncienneteHN1 = moyenneAncienneteHN1;
	}

	public Integer getEffectifFN1() {
		return effectifFN1;
	}

	public void setEffectifFN1(Integer effectifFN1) {
		this.effectifFN1 = effectifFN1;
	}

	public String getMoyenneAgeFN1() {
		return moyenneAgeFN1;
	}

	public void setMoyenneAgeFN1(String moyenneAgeFN1) {
		this.moyenneAgeFN1 = moyenneAgeFN1;
	}

	public String getMoyenneAncienneteFN1() {
		return moyenneAncienneteFN1;
	}

	public void setMoyenneAncienneteFN1(String moyenneAncienneteFN1) {
		this.moyenneAncienneteFN1 = moyenneAncienneteFN1;
	}

	public Integer getEffectifN2() {
		return effectifN2;
	}

	public void setEffectifN2(Integer effectifN2) {
		this.effectifN2 = effectifN2;
	}

	public String getMoyenneAgeN2() {
		return moyenneAgeN2;
	}

	public void setMoyenneAgeN2(String moyenneAgeN2) {
		this.moyenneAgeN2 = moyenneAgeN2;
	}

	public String getMoyenneAncienneteN2() {
		return moyenneAncienneteN2;
	}

	public void setMoyenneAncienneteN2(String moyenneAncienneteN2) {
		this.moyenneAncienneteN2 = moyenneAncienneteN2;
	}

	public Integer getEffectifHN2() {
		return effectifHN2;
	}

	public void setEffectifHN2(Integer effectifHN2) {
		this.effectifHN2 = effectifHN2;
	}

	public String getMoyenneAgeHN2() {
		return moyenneAgeHN2;
	}

	public void setMoyenneAgeHN2(String moyenneAgeHN2) {
		this.moyenneAgeHN2 = moyenneAgeHN2;
	}

	public String getMoyenneAncienneteHN2() {
		return moyenneAncienneteHN2;
	}

	public void setMoyenneAncienneteHN2(String moyenneAncienneteHN2) {
		this.moyenneAncienneteHN2 = moyenneAncienneteHN2;
	}

	public Integer getEffectifFN2() {
		return effectifFN2;
	}

	public void setEffectifFN2(Integer effectifFN2) {
		this.effectifFN2 = effectifFN2;
	}

	public String getMoyenneAgeFN2() {
		return moyenneAgeFN2;
	}

	public void setMoyenneAgeFN2(String moyenneAgeFN2) {
		this.moyenneAgeFN2 = moyenneAgeFN2;
	}

	public String getMoyenneAncienneteFN2() {
		return moyenneAncienneteFN2;
	}

	public void setMoyenneAncienneteFN2(String moyenneAncienneteFN2) {
		this.moyenneAncienneteFN2 = moyenneAncienneteFN2;
	}

}
