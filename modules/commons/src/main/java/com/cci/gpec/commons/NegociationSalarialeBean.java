package com.cci.gpec.commons;

public class NegociationSalarialeBean {

	private Integer effectif;
	private Integer effectifH;
	private Integer effectifF;

	private String salaireDeBaseBrutAnnuel;
	private String salaireDeBaseBrutAnnuelH;
	private String salaireDeBaseBrutAnnuelF;
	private String salaireDeBaseBrutAnnuelMoy;
	private String salaireDeBaseBrutAnnuelMoyH;
	private String salaireDeBaseBrutAnnuelMoyF;

	private String remuGlobaleBruteAnnuelle;
	private String remuGlobaleBruteAnnuelleH;
	private String remuGlobaleBruteAnnuelleF;
	private String remuGlobaleBruteAnnuelleMoy;
	private String remuGlobaleBruteAnnuelleMoyH;
	private String remuGlobaleBruteAnnuelleMoyF;

	private String heureSupAnnuelles;
	private String heureSupAnnuellesH;
	private String heureSupAnnuellesF;
	private String heureSupAnnuellesMoy;
	private String heureSupAnnuellesMoyH;
	private String heureSupAnnuellesMoyF;

	private String avantagesNonAssujettisAnnuels;
	private String avantagesNonAssujettisAnnuelsH;
	private String avantagesNonAssujettisAnnuelsF;
	private String avantagesNonAssujettisAnnuelsMoy;
	private String avantagesNonAssujettisAnnuelsMoyH;
	private String avantagesNonAssujettisAnnuelsMoyF;

	private boolean footer;

	private String libelle;

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Integer getEffectif() {
		return effectif;
	}

	public void setEffectif(Integer effectif) {
		this.effectif = effectif;
	}

	public Integer getEffectifH() {
		return effectifH;
	}

	public void setEffectifH(Integer effectifH) {
		this.effectifH = effectifH;
	}

	public Integer getEffectifF() {
		return effectifF;
	}

	public void setEffectifF(Integer effectifF) {
		this.effectifF = effectifF;
	}

	public String getSalaireDeBaseBrutAnnuel() {
		if (salaireDeBaseBrutAnnuel.equals("0.00") && effectif == 0) {
			return "-";
		} else {
			return salaireDeBaseBrutAnnuel;
		}
	}

	public void setSalaireDeBaseBrutAnnuel(String salaireDeBaseBrutAnnuel) {
		this.salaireDeBaseBrutAnnuel = salaireDeBaseBrutAnnuel;
	}

	public String getSalaireDeBaseBrutAnnuelMoy() {
		if (salaireDeBaseBrutAnnuelMoy.equals("0.00") && effectif == 0) {
			return "-";
		} else {
			return salaireDeBaseBrutAnnuelMoy;
		}
	}

	public void setSalaireDeBaseBrutAnnuelMoy(String salaireDeBaseBrutAnnuelMoy) {
		this.salaireDeBaseBrutAnnuelMoy = salaireDeBaseBrutAnnuelMoy;
	}

	public String getSalaireDeBaseBrutAnnuelMoyH() {
		if (salaireDeBaseBrutAnnuelMoyH == null
				|| (salaireDeBaseBrutAnnuelMoyH.equals("0.00") && effectifH == 0)) {
			return "-";
		} else {
			return salaireDeBaseBrutAnnuelMoyH;
		}
	}

	public void setSalaireDeBaseBrutAnnuelMoyH(
			String salaireDeBaseBrutAnnuelMoyH) {
		this.salaireDeBaseBrutAnnuelMoyH = salaireDeBaseBrutAnnuelMoyH;
	}

	public String getSalaireDeBaseBrutAnnuelMoyF() {
		if (salaireDeBaseBrutAnnuelMoyF == null
				|| (salaireDeBaseBrutAnnuelMoyF.equals("0.00") && effectifF == 0)) {
			return "-";
		} else {
			return salaireDeBaseBrutAnnuelMoyF;
		}
	}

	public void setSalaireDeBaseBrutAnnuelMoyF(
			String salaireDeBaseBrutAnnuelMoyF) {
		this.salaireDeBaseBrutAnnuelMoyF = salaireDeBaseBrutAnnuelMoyF;
	}

	public String getRemuGlobaleBruteAnnuelle() {
		if (remuGlobaleBruteAnnuelle.equals("0.00") && effectif == 0) {
			return "-";
		} else {
			return remuGlobaleBruteAnnuelle;
		}
	}

	public void setRemuGlobaleBruteAnnuelle(String remuGlobaleBruteAnnuelle) {
		this.remuGlobaleBruteAnnuelle = remuGlobaleBruteAnnuelle;
	}

	public String getRemuGlobaleBruteAnnuelleMoy() {
		if (remuGlobaleBruteAnnuelleMoy.equals("0.00") && effectif == 0)
			return "-";
		else
			return remuGlobaleBruteAnnuelleMoy;
	}

	public void setRemuGlobaleBruteAnnuelleMoy(
			String remuGlobaleBruteAnnuelleMoy) {
		this.remuGlobaleBruteAnnuelleMoy = remuGlobaleBruteAnnuelleMoy;
	}

	public String getRemuGlobaleBruteAnnuelleMoyH() {
		if (remuGlobaleBruteAnnuelleMoyH == null
				|| (remuGlobaleBruteAnnuelleMoyH.equals("0.00") && effectifH == 0))
			return "-";
		else
			return remuGlobaleBruteAnnuelleMoyH;
	}

	public void setRemuGlobaleBruteAnnuelleMoyH(
			String remuGlobaleBruteAnnuelleMoyH) {
		this.remuGlobaleBruteAnnuelleMoyH = remuGlobaleBruteAnnuelleMoyH;
	}

	public String getRemuGlobaleBruteAnnuelleMoyF() {
		if (remuGlobaleBruteAnnuelleMoyF == null
				|| (remuGlobaleBruteAnnuelleMoyF.equals("0.00") && effectifF == 0)) {
			return "-";
		} else {
			return remuGlobaleBruteAnnuelleMoyF;
		}
	}

	public void setRemuGlobaleBruteAnnuelleMoyF(
			String remuGlobaleBruteAnnuelleMoyF) {
		this.remuGlobaleBruteAnnuelleMoyF = remuGlobaleBruteAnnuelleMoyF;
	}

	public String getHeureSupAnnuelles() {
		if (heureSupAnnuelles.equals("0.00") && effectif == 0) {
			return "-";
		} else {
			return heureSupAnnuelles;
		}
	}

	public void setHeureSupAnnuelles(String heureSupAnnuelles) {
		this.heureSupAnnuelles = heureSupAnnuelles;
	}

	public String getHeureSupAnnuellesMoy() {
		if (heureSupAnnuellesMoy.equals("0.00") && effectif == 0) {
			return "-";
		} else {
			return heureSupAnnuellesMoy;
		}
	}

	public void setHeureSupAnnuellesMoy(String heureSupAnnuellesMoy) {
		this.heureSupAnnuellesMoy = heureSupAnnuellesMoy;
	}

	public String getHeureSupAnnuellesMoyH() {
		if (heureSupAnnuellesMoyH == null
				|| (heureSupAnnuellesMoyH.equals("0.00") && effectifH == 0)) {
			return "-";
		} else {
			return heureSupAnnuellesMoyH;
		}
	}

	public void setHeureSupAnnuellesMoyH(String heureSupAnnuellesMoyH) {
		this.heureSupAnnuellesMoyH = heureSupAnnuellesMoyH;
	}

	public String getHeureSupAnnuellesMoyF() {
		if (heureSupAnnuellesMoyF == null
				|| (heureSupAnnuellesMoyF.equals("0.00") && effectifF == 0)) {
			return "-";
		} else {
			return heureSupAnnuellesMoyF;
		}
	}

	public void setHeureSupAnnuellesMoyF(String heureSupAnnuellesMoyF) {
		this.heureSupAnnuellesMoyF = heureSupAnnuellesMoyF;
	}

	public String getAvantagesNonAssujettisAnnuels() {
		if (avantagesNonAssujettisAnnuels.equals("0.00") && effectif == 0) {
			return "-";
		} else {
			return avantagesNonAssujettisAnnuels;
		}
	}

	public void setAvantagesNonAssujettisAnnuels(
			String avantagesNonAssujettisAnnuels) {
		this.avantagesNonAssujettisAnnuels = avantagesNonAssujettisAnnuels;
	}

	public String getAvantagesNonAssujettisAnnuelsMoy() {
		if (avantagesNonAssujettisAnnuelsMoy.equals("0.00") && effectif == 0) {
			return "-";
		} else {
			return avantagesNonAssujettisAnnuelsMoy;
		}
	}

	public void setAvantagesNonAssujettisAnnuelsMoy(
			String avantagesNonAssujettisAnnuelsMoy) {
		this.avantagesNonAssujettisAnnuelsMoy = avantagesNonAssujettisAnnuelsMoy;
	}

	public String getAvantagesNonAssujettisAnnuelsMoyH() {
		if (avantagesNonAssujettisAnnuelsMoyH == null
				|| (avantagesNonAssujettisAnnuelsMoyH.equals("0.00"))
				&& effectifH == 0) {
			return "-";
		} else {
			return avantagesNonAssujettisAnnuelsMoyH;
		}
	}

	public void setAvantagesNonAssujettisAnnuelsMoyH(
			String avantagesNonAssujettisAnnuelsMoyH) {
		this.avantagesNonAssujettisAnnuelsMoyH = avantagesNonAssujettisAnnuelsMoyH;
	}

	public String getAvantagesNonAssujettisAnnuelsMoyF() {
		if (avantagesNonAssujettisAnnuelsMoyF == null
				|| (avantagesNonAssujettisAnnuelsMoyF.equals("0.00") && effectifF == 0)) {
			return "-";
		} else {
			return avantagesNonAssujettisAnnuelsMoyF;
		}
	}

	public void setAvantagesNonAssujettisAnnuelsMoyF(
			String avantagesNonAssujettisAnnuelsMoyF) {
		this.avantagesNonAssujettisAnnuelsMoyF = avantagesNonAssujettisAnnuelsMoyF;
	}

	public boolean isFooter() {
		return footer;
	}

	public void setFooter(boolean footer) {
		this.footer = footer;
	}

	public String getSalaireDeBaseBrutAnnuelH() {
		return salaireDeBaseBrutAnnuelH;
	}

	public void setSalaireDeBaseBrutAnnuelH(String salaireDeBaseBrutAnnuelH) {
		this.salaireDeBaseBrutAnnuelH = salaireDeBaseBrutAnnuelH;
	}

	public String getSalaireDeBaseBrutAnnuelF() {
		return salaireDeBaseBrutAnnuelF;
	}

	public void setSalaireDeBaseBrutAnnuelF(String salaireDeBaseBrutAnnuelF) {
		this.salaireDeBaseBrutAnnuelF = salaireDeBaseBrutAnnuelF;
	}

	public String getRemuGlobaleBruteAnnuelleH() {
		return remuGlobaleBruteAnnuelleH;
	}

	public void setRemuGlobaleBruteAnnuelleH(String remuGlobaleBruteAnnuelleH) {
		this.remuGlobaleBruteAnnuelleH = remuGlobaleBruteAnnuelleH;
	}

	public String getRemuGlobaleBruteAnnuelleF() {
		return remuGlobaleBruteAnnuelleF;
	}

	public void setRemuGlobaleBruteAnnuelleF(String remuGlobaleBruteAnnuelleF) {
		this.remuGlobaleBruteAnnuelleF = remuGlobaleBruteAnnuelleF;
	}

	public String getHeureSupAnnuellesH() {
		return heureSupAnnuellesH;
	}

	public void setHeureSupAnnuellesH(String heureSupAnnuellesH) {
		this.heureSupAnnuellesH = heureSupAnnuellesH;
	}

	public String getHeureSupAnnuellesF() {
		return heureSupAnnuellesF;
	}

	public void setHeureSupAnnuellesF(String heureSupAnnuellesF) {
		this.heureSupAnnuellesF = heureSupAnnuellesF;
	}

	public String getAvantagesNonAssujettisAnnuelsH() {
		return avantagesNonAssujettisAnnuelsH;
	}

	public void setAvantagesNonAssujettisAnnuelsH(
			String avantagesNonAssujettisAnnuelsH) {
		this.avantagesNonAssujettisAnnuelsH = avantagesNonAssujettisAnnuelsH;
	}

	public String getAvantagesNonAssujettisAnnuelsF() {
		return avantagesNonAssujettisAnnuelsF;
	}

	public void setAvantagesNonAssujettisAnnuelsF(
			String avantagesNonAssujettisAnnuelsF) {
		this.avantagesNonAssujettisAnnuelsF = avantagesNonAssujettisAnnuelsF;
	}

}