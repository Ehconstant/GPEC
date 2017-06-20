package com.cci.gpec.commons;

public class PiecesJustificativesBean {

	private int id;

	private boolean carteIdentite;
	private boolean attestationSecu;
	private boolean permisConduire;
	private boolean rib;
	private boolean diplomes;
	private boolean certificatTravail;
	private boolean attestationVisiteMedicale;
	private boolean habilitations;

	private Integer idSalarie;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isCarteIdentite() {
		return carteIdentite;
	}

	public void setCarteIdentite(boolean carteIdentite) {
		this.carteIdentite = carteIdentite;
	}

	public boolean isAttestationSecu() {
		return attestationSecu;
	}

	public void setAttestationSecu(boolean attestationSecu) {
		this.attestationSecu = attestationSecu;
	}

	public boolean isPermisConduire() {
		return permisConduire;
	}

	public void setPermisConduire(boolean permisConduire) {
		this.permisConduire = permisConduire;
	}

	public boolean isRib() {
		return rib;
	}

	public void setRib(boolean rib) {
		this.rib = rib;
	}

	public boolean isDiplomes() {
		return diplomes;
	}

	public void setDiplomes(boolean diplomes) {
		this.diplomes = diplomes;
	}

	public boolean isCertificatTravail() {
		return certificatTravail;
	}

	public void setCertificatTravail(boolean certificatTravail) {
		this.certificatTravail = certificatTravail;
	}

	public boolean isAttestaionVisiteMedicale() {
		return attestationVisiteMedicale;
	}

	public void setAttestaionVisiteMedicale(boolean attestationVisiteMedicale) {
		this.attestationVisiteMedicale = attestationVisiteMedicale;
	}

	public boolean isHabilitations() {
		return habilitations;
	}

	public void setHabilitations(boolean habilitations) {
		this.habilitations = habilitations;
	}

	public Integer getIdSalarie() {
		return idSalarie;
	}

	public void setIdSalarie(Integer idSalarie) {
		this.idSalarie = idSalarie;
	}

}
