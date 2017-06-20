package com.cci.gpec.commons;

import java.util.Date;

import com.cci.gpec.ModelBean;

public class UserBean extends ModelBean implements Comparable<UserBean> {

	private String login;
	private String password;
	private String profile;
	private String nom;
	private String prenom;
	private String telephone;
	private Date dateDemandeVersionEssai;
	private String nomEntreprise;
	private boolean periodeEssaiTerminee;
	private Date finPeriodeEssai;

	private boolean evenement;
	private boolean remuneration;
	private boolean ficheDePoste;
	private boolean entretien;
	private boolean contratTravail;
	private boolean admin;
	private boolean superAdmin;

	private int idGroupe;

	public boolean isEvenement() {
		return evenement;
	}

	public void setEvenement(boolean evenement) {
		this.evenement = evenement;
	}

	public boolean isRemuneration() {
		return remuneration;
	}

	public void setRemuneration(boolean remuneration) {
		this.remuneration = remuneration;
	}

	public boolean isFicheDePoste() {
		return ficheDePoste;
	}

	public void setFicheDePoste(boolean ficheDePoste) {
		this.ficheDePoste = ficheDePoste;
	}

	public boolean isEntretien() {
		return entretien;
	}

	public void setEntretien(boolean entretien) {
		this.entretien = entretien;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public UserBean() {
		super();

	}

	public UserBean(int id, String nom) {
		super(id, nom);
		// TODO Auto-generated constructor stub
	}

	public int compareTo(UserBean o) {
		UserBean p = o;

		return getLogin().toUpperCase().compareTo(p.getLogin().toUpperCase());
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isContratTravail() {
		return contratTravail;
	}

	public void setContratTravail(boolean contratTravail) {
		this.contratTravail = contratTravail;
	}

	public int getIdGroupe() {
		return idGroupe;
	}

	public void setIdGroupe(int idGroupe) {
		this.idGroupe = idGroupe;
	}

	public boolean isSuperAdmin() {
		return superAdmin;
	}

	public void setSuperAdmin(boolean superAdmin) {
		this.superAdmin = superAdmin;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Date getDateDemandeVersionEssai() {
		return dateDemandeVersionEssai;
	}

	public void setDateDemandeVersionEssai(Date dateDemandeVersionEssai) {
		this.dateDemandeVersionEssai = dateDemandeVersionEssai;
	}

	public String getNomEntreprise() {
		return nomEntreprise;
	}

	public void setNomEntreprise(String nomEntreprise) {
		this.nomEntreprise = nomEntreprise;
	}

	public boolean isPeriodeEssaiTerminee() {
		return periodeEssaiTerminee;
	}

	public void setPeriodeEssaiTerminee(boolean periodeEssaiTerminee) {
		this.periodeEssaiTerminee = periodeEssaiTerminee;
	}

	public Date getFinPeriodeEssai() {
		return finPeriodeEssai;
	}

	public void setFinPeriodeEssai(Date finPeriodeEssai) {
		this.finPeriodeEssai = finPeriodeEssai;
	}

}
