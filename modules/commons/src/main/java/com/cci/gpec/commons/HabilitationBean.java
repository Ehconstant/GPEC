package com.cci.gpec.commons;

import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;

public class HabilitationBean implements Comparable<HabilitationBean> {
	// primary key
	private int id;

	// fields
	private Date delivrance;
	private Date expiration;
	private Integer dureeValidite;
	private String justificatif;
	private String commentaire;

	private Integer idTypeHabilitationSelected;
	private Integer idSalarie;
	private String nomTypeHabilitation;

	private File justif;

	private boolean fileError;

	public HabilitationBean() {
		super();
	}

	public HabilitationBean(int id, Date delivrance, Date expiration,
			Integer dureeValidite, String justificatif) {
		super();
		this.id = id;
		this.delivrance = delivrance;
		this.expiration = expiration;
		this.dureeValidite = dureeValidite;
		this.justificatif = justificatif;
	}

	public File getJustif() {
		if (justificatif != null && !justificatif.equals(""))
			return new File(justificatif);
		else
			return null;
	}

	public void setJustif(File justif) {
		this.justif = justif;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDelivrance() {
		return delivrance;
	}

	public void setDelivrance(Date delivrance) {
		this.delivrance = delivrance;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public Integer getDureeValidite() {
		Long nbJoursValidite = getNbJoursValidite();
		this.dureeValidite = nbJoursValidite.intValue();
		return dureeValidite;
	}

	public void setDureeValidite(Integer dureeValidite) {
		this.dureeValidite = dureeValidite;
	}

	public String getJustificatif() {
		return justificatif;
	}

	public void setJustificatif(String justificatif) {
		this.justificatif = justificatif;
	}

	public Integer getIdTypeHabilitationSelected() {
		return idTypeHabilitationSelected;
	}

	public void setIdTypeHabilitationSelected(Integer idTypeHabilitationSelected) {
		this.idTypeHabilitationSelected = idTypeHabilitationSelected;
	}

	public String getNomTypeHabilitation() {
		return nomTypeHabilitation;
	}

	public void setNomTypeHabilitation(String nomTypeHabilitation) {
		this.nomTypeHabilitation = nomTypeHabilitation;
	}

	public int compareTo(HabilitationBean o) {
		if (delivrance.equals(o.delivrance))
			return id < o.id ? -1 : 1;
		else
			return delivrance.compareTo(o.delivrance);
	}

	public Integer getIdSalarie() {
		return idSalarie;
	}

	public void setIdSalarie(Integer idSalarie) {
		this.idSalarie = idSalarie;
	}

	private Long getNbJoursValidite() {

		Long diffMillis;
		Long diffenjours = null;
		// if(this.delivrance != null && this.dureeValidite != null){
		GregorianCalendar dateExpiration = new GregorianCalendar();
		GregorianCalendar dateDuJour = new GregorianCalendar();
		GregorianCalendar dateDelivrance = new GregorianCalendar();

		dateExpiration.setTime(this.expiration);
		dateDelivrance.setTime(this.delivrance);

		if (dateDelivrance.before(dateDuJour)) {
			diffMillis = dateExpiration.getTimeInMillis()
					- dateDuJour.getTimeInMillis();
			if (!dateExpiration.equals(dateDuJour)) {
				diffenjours = (diffMillis / (24 * 60 * 60 * 1000)) + 1;
			} else {
				diffenjours = diffMillis / (24 * 60 * 60 * 1000);
			}
		} else {
			diffMillis = dateExpiration.getTimeInMillis()
					- dateDelivrance.getTimeInMillis();

			diffenjours = diffMillis / (24 * 60 * 60 * 1000);
		}
		// }
		return diffenjours;
	}

	public boolean isFileError() {
		if (getJustif() != null) {
			if (getJustif().exists() && getJustif().isFile()
					&& getJustif().canRead())
				return false;
			else
				return true;
		} else
			return false;
	}

	public void setFileError(boolean fileError) {
		this.fileError = fileError;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
}
