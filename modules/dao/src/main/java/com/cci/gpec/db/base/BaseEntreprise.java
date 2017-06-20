package com.cci.gpec.db.base;

import java.io.Serializable;

/**
 * This is an object that contains data related to the ENTREPRISE table. Do not
 * modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 * 
 * @hibernate.class table="ENTREPRISE"
 */

public abstract class BaseEntreprise implements Serializable {

	public static String REF = "Entreprise";
	public static String PROP_NOM_ENTREPRISE = "NomEntreprise";
	public static String PROP_ID = "Id";
	public static String PROP_ID_GROUPE = "IdGroupe";
	public static String PROP_NUMSIRET = "Numsiret";
	public static String PROP_CODEAPE = "Codeape";
	public static String PROP_DATE_CREATION = "DateCreation";
	public static String PROP_CCI_RATTACHEMENT = "CciRattachement";
	public static String PROP_SUIVI_FORMATIONS = "Id";
	public static String PROP_SUIVI_ACCIDENTS = "Id";
	public static String PROP_SUIVI_ABSENCES = "Id";
	public static String PROP_SUIVI_COMPETENCES = "Id";
	public static String PROP_SUIVI_DIF = "Id";

	// constructors
	public BaseEntreprise() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseEntreprise(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String nomEntreprise;
	private java.lang.Long numsiret;
	private java.lang.String codeape;
	private java.lang.String cciRattachement;
	private java.util.Date dateCreation;
	private java.lang.String _justificatif;
	private boolean suiviFormations;
	private boolean suiviAccidents;
	private boolean suiviAbsences;
	private boolean suiviCompetences;
	private boolean suiviDIF;
	private Integer DIFMax;

	// many to one
	private com.cci.gpec.db.Groupe groupe;

	// collections
	private java.util.Set<com.cci.gpec.db.Service> sERVICEs;
	private java.util.Set<com.cci.gpec.db.Salarie> sALARIEs;
	private java.util.Set<com.cci.gpec.db.Paramsgeneraux> pARAMSGENERAUXs;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="identity" column="ID_ENTREPRISE"
	 */
	public java.lang.Integer getId() {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param id
	 *            the new ID
	 */
	public void setId(java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: NOM_ENTREPRISE
	 */
	public java.lang.String getNomEntreprise() {
		return nomEntreprise;
	}

	/**
	 * Set the value related to the column: NOM_ENTREPRISE
	 * 
	 * @param nomEntreprise
	 *            the NOM_ENTREPRISE value
	 */
	public void setNomEntreprise(java.lang.String nomEntreprise) {
		this.nomEntreprise = nomEntreprise;
	}

	/**
	 * Return the value associated with the column: NUMSIRET
	 */
	public java.lang.Long getNumsiret() {
		return numsiret;
	}

	/**
	 * Set the value related to the column: NUMSIRET
	 * 
	 * @param numsiret
	 *            the NUMSIRET value
	 */
	public void setNumsiret(java.lang.Long numsiret) {
		this.numsiret = numsiret;
	}

	/**
	 * Return the value associated with the column: CODEAPE
	 */
	public java.lang.String getCodeape() {
		return codeape;
	}

	/**
	 * Set the value related to the column: CODEAPE
	 * 
	 * @param codeape
	 *            the CODEAPE value
	 */
	public void setCodeape(java.lang.String codeape) {
		this.codeape = codeape;
	}

	/**
	 * Return the value associated with the column: ID_GROUPE
	 */
	public com.cci.gpec.db.Groupe getGroupe() {
		return groupe;
	}

	/**
	 * Set the value related to the column: ID_GROUPE
	 * 
	 * @param idGroupe
	 *            the ID_GROUPE value
	 */
	public void setGroupe(com.cci.gpec.db.Groupe groupe) {
		this.groupe = groupe;
	}

	/**
	 * Return the value associated with the column: JUSTIFICATIF
	 */
	public java.lang.String getJustificatif() {
		return _justificatif;
	}

	/**
	 * Set the value related to the column: JUSTIFICATIF
	 * 
	 * @param _justificatif
	 *            the JUSTIFICATIF value
	 */
	public void setJustificatif(java.lang.String _justificatif) {
		this._justificatif = _justificatif;
	}

	/**
	 * Return the value associated with the column: SERVICEs
	 */
	public java.util.Set<com.cci.gpec.db.Service> getSERVICEs() {
		return sERVICEs;
	}

	/**
	 * Set the value related to the column: SERVICEs
	 * 
	 * @param sERVICEs
	 *            the SERVICEs value
	 */
	public void setSERVICEs(java.util.Set<com.cci.gpec.db.Service> sERVICEs) {
		this.sERVICEs = sERVICEs;
	}

	public void addToSERVICEs(com.cci.gpec.db.Service service) {
		if (null == getSERVICEs())
			setSERVICEs(new java.util.TreeSet<com.cci.gpec.db.Service>());
		getSERVICEs().add(service);
	}

	/**
	 * Return the value associated with the column: SALARIEs
	 */
	public java.util.Set<com.cci.gpec.db.Salarie> getSALARIEs() {
		return sALARIEs;
	}

	/**
	 * Set the value related to the column: SALARIEs
	 * 
	 * @param sALARIEs
	 *            the SALARIEs value
	 */
	public void setSALARIEs(java.util.Set<com.cci.gpec.db.Salarie> sALARIEs) {
		this.sALARIEs = sALARIEs;
	}

	public void addToSALARIEs(com.cci.gpec.db.Salarie salarie) {
		if (null == getSALARIEs())
			setSALARIEs(new java.util.TreeSet<com.cci.gpec.db.Salarie>());
		getSALARIEs().add(salarie);
	}

	/**
	 * Return the value associated with the column: PARAMSGENERAUXs
	 */
	public java.util.Set<com.cci.gpec.db.Paramsgeneraux> getPARAMSGENERAUXs() {
		return pARAMSGENERAUXs;
	}

	/**
	 * Set the value related to the column: PARAMSGENERAUXs
	 * 
	 * @param pARAMSGENERAUXs
	 *            the PARAMSGENERAUXs value
	 */
	public void setPARAMSGENERAUXs(
			java.util.Set<com.cci.gpec.db.Paramsgeneraux> pARAMSGENERAUXs) {
		this.pARAMSGENERAUXs = pARAMSGENERAUXs;
	}

	public void addToPARAMSGENERAUXs(
			com.cci.gpec.db.Paramsgeneraux paramsgeneraux) {
		if (null == getPARAMSGENERAUXs())
			setPARAMSGENERAUXs(new java.util.TreeSet<com.cci.gpec.db.Paramsgeneraux>());
		getPARAMSGENERAUXs().add(paramsgeneraux);
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.cci.gpec.db.Entreprise))
			return false;
		else {
			com.cci.gpec.db.Entreprise entreprise = (com.cci.gpec.db.Entreprise) obj;
			if (null == this.getId() || null == entreprise.getId())
				return false;
			else
				return (this.getId().equals(entreprise.getId()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId())
				return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":"
						+ this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

	public java.lang.String getCciRattachement() {
		return cciRattachement;
	}

	public void setCciRattachement(java.lang.String cciRattachement) {
		this.cciRattachement = cciRattachement;
	}

	public java.util.Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(java.util.Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public boolean isSuiviFormations() {
		return suiviFormations;
	}

	public void setSuiviFormations(boolean suiviFormations) {
		this.suiviFormations = suiviFormations;
	}

	public boolean isSuiviAccidents() {
		return suiviAccidents;
	}

	public void setSuiviAccidents(boolean suiviAccidents) {
		this.suiviAccidents = suiviAccidents;
	}

	public boolean isSuiviAbsences() {
		return suiviAbsences;
	}

	public void setSuiviAbsences(boolean suiviAbsences) {
		this.suiviAbsences = suiviAbsences;
	}

	public boolean isSuiviCompetences() {
		return suiviCompetences;
	}

	public void setSuiviCompetences(boolean suiviCompetences) {
		this.suiviCompetences = suiviCompetences;
	}

	public boolean isSuiviDIF() {
		return suiviDIF;
	}

	public void setSuiviDIF(boolean suiviDIF) {
		this.suiviDIF = suiviDIF;
	}

	public Integer getDIFMax() {
		return DIFMax;
	}

	public void setDIFMax(Integer max) {
		DIFMax = max;
	}

}