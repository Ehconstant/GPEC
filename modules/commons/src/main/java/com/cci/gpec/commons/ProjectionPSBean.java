package com.cci.gpec.commons;

import com.cci.gpec.ModelBean;

public class ProjectionPSBean extends ModelBean implements Comparable {

	public int tranche1;
	public int tranche2;
	public int tranche3;
	public int taux;
	public boolean footer;

	public int getTranche1() {
		return tranche1;
	}

	public void setTranche1(int tranche1) {
		this.tranche1 = tranche1;
	}

	public int getTranche2() {
		return tranche2;
	}

	public void setTranche2(int tranche2) {
		this.tranche2 = tranche2;
	}

	public int getTranche3() {
		return tranche3;
	}

	public void setTranche3(int tranche3) {
		this.tranche3 = tranche3;
	}

	public int getTaux() {
		return taux;
	}

	public void setTaux(int taux) {
		this.taux = taux;
	}

	public boolean isFooter() {
		return footer;
	}

	public void setFooter(boolean footer) {
		this.footer = footer;
	}

	public int compareTo(Object o) {
		return this.getNom().compareTo(((ProjectionPSBean) o).getNom());
	}

}
