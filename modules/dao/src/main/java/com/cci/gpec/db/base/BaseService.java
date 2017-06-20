package com.cci.gpec.db.base;

import java.io.Serializable;

/**
 * This is an object that contains data related to the SERVICE table. Do not
 * modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 * 
 * @hibernate.class table="SERVICE"
 */

public abstract class BaseService implements Serializable {

	public static String REF = "Service";
	public static String PROP_ID_ENTREPRISE = "IdEntreprise";
	public static String PROP_ID = "Id";
	public static String PROP_NOM_SERVICE = "NomService";

	// constructors
	public BaseService() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseService(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String nomService;

	// many to one
	private com.cci.gpec.db.Entreprise entreprise;

	// collections
	private java.util.Set<com.cci.gpec.db.Salarie> sALARIEs;
	private java.util.Set<com.cci.gpec.db.Entretien> eNTRETIENs;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="identity" column="ID_SERVICE"
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
	 * Return the value associated with the column: NOM_SERVICE
	 */
	public java.lang.String getNomService() {
		return nomService;
	}

	/**
	 * Set the value related to the column: NOM_SERVICE
	 * 
	 * @param nomService
	 *            the NOM_SERVICE value
	 */
	public void setNomService(java.lang.String nomService) {
		this.nomService = nomService;
	}

	/**
	 * Return the value associated with the column: ID_ENTREPRISE
	 */
	public com.cci.gpec.db.Entreprise getEntreprise() {
		return entreprise;
	}

	/**
	 * Set the value related to the column: ID_ENTREPRISE
	 * 
	 * @param idEntreprise
	 *            the ID_ENTREPRISE value
	 */
	public void setEntreprise(com.cci.gpec.db.Entreprise entreprise) {
		this.entreprise = entreprise;
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
	 * Return the value associated with the column: ENTRETIENs
	 */
	public java.util.Set<com.cci.gpec.db.Entretien> getENTRETIENs() {
		return eNTRETIENs;
	}

	/**
	 * Set the value related to the column: ENTRETIENs
	 * 
	 * @param eNTRETIENs
	 *            the ENTRETIENs value
	 */
	public void setENTRETIENs(
			java.util.Set<com.cci.gpec.db.Entretien> eNTRETIENs) {
		this.eNTRETIENs = eNTRETIENs;
	}

	public void addToENTRETIENs(com.cci.gpec.db.Entretien entretien) {
		if (null == getENTRETIENs())
			setENTRETIENs(new java.util.TreeSet<com.cci.gpec.db.Entretien>());
		getENTRETIENs().add(entretien);
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.cci.gpec.db.Service))
			return false;
		else {
			com.cci.gpec.db.Service service = (com.cci.gpec.db.Service) obj;
			if (null == this.getId() || null == service.getId())
				return false;
			else
				return (this.getId().equals(service.getId()));
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

}