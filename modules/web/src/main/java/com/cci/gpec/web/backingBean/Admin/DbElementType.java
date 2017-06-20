package com.cci.gpec.web.backingBean.Admin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.emory.mathcs.backport.java.util.Arrays;
import edu.emory.mathcs.backport.java.util.Collections;

public enum DbElementType {
	ADMIN("id_admin", "admin", false, 1),
	ABSENCE_TYPE("id_type_absence", "typeabsence", false, 1), //Référentiel customisable
	ACCIDENT_TYPE("id_type_accident", "typeaccident", true, 0),
	ACCIDENT_REASON_TYPE("id_type_cause_accident", "typecauseaccident", true, 0),
	CAPACITATION_TYPE("id_type_habilitation", "typehabilitation", false, 1), //Référentiel customisable
	COMPANY("id_entreprise", "entreprise", false, 1),
	CONTRACT_BREACH_REASON("ID_MOTIF_RUPTURE_CONTRAT", "motifrupturecontrat", false, 0),
	CONTRACT_TYPE("id_type_contrat", "typecontrat", true, 0),
	GENERAL_PARAMS("id_params_generaux", "paramsgeneraux", false, 2),
	GPEC_USER("ID_USER", "gpec_user", false, 1),
	GROUP("id_groupe", "groupe", false, 0),
	JOB("id_metier", "metier", false, 1),
	JOB_FAMILLY("id_famille_metier", "famillemetier", true, 0),
	LESION_TYPE("id_type_lesion", "typelesion", true, 0),
	REMEDY_INTERIM_TYPE("id_type_recours", "typerecoursinterim", true, 0),
	SERVICE("id_service", "service", false, 2),
	STATUT("id_statut", "statut", true, 0),
	TRANSMISION("id_transmission", "transmission", false, 0),
	WORKER("id_salarie", "salarie", false, 4),
	
	ABSENCE("id_absence", "absence", false, 5),
	ACCIDENT("id_accident", "accident", false, 5),
	JOB_CONTRACT("ID_CONTRAT", "contrat_travail", false, 5),
	TRAINING_AREA("id_domaine_formation", "domaineformation", true, 0),
	PROFESSIONNAL_MEETING("id_entretien", "entretien", false, 5),
	EVENT("id_evenement", "evenement", false, 5),
	JOB_DESCRIPTION("id_fiche_de_poste", "fichedeposte", false, 6),
	POSITION_DESCRIPTION("id_fiche_metier", "fichemetier", false, 5),
	
	ENTERPRISE_POSITION_DESCRIPTION("id_fiche_metier", "fichemetierentreprise", false, 6),
	
	TRAINING("id_formation", "formation", false, 5),
	CAPACITATION("id_habilitation", "habilitation", false, 5),
	
	SALARY_COMPLEMENTARY_INCOME_LINK("id_lien_remuneration_revenu_complementaire", "lien_remuneration_revenu_complementaire", false, 6),
	
	RESUME_MAPPING("id_mapping_reprise", "mapping_reprise", false, 1),
	PROFESSIONNAL_MEETING_OBJECTIVE("ID_OBJECTIF", "objectifsentretien", false, 6),
	CAREER("id_parcours", "parcours", false, 5),
	DEPENDENT("id_personne_a_charge", "personne_a_charge", false, 5),
	SUPPORTING_DOCUMENTS("id_pieces_justificatives", "pieces_justificatives", false, 5),
	COMPLEMENTARY_INCOME_REF("id_revenu_complementaire", "ref_revenu_complementaire", false, 2),
	SALARY("id_remuneration", "remuneration", false, 5),
	GENERIC_SERVICE("id_service_generique", "servicegenerique", true, 0);
	
	// ATTRIBUTS
	private String autoIncrementField;
	private String tableName;
	private boolean referentielTable;
	private int order;
	
	// BUILDERS
	private DbElementType(String autoIncrementField, String tableName, boolean referentielTable, int order) {
		this.autoIncrementField = autoIncrementField;
		this.tableName = tableName;
		this.referentielTable = referentielTable;
		this.order = order;
	}

	// GET / SET
	public String getAutoIncrementField() {
		return autoIncrementField;
	}

	public void setAutoIncrementField(String autoIncrementField) {
		this.autoIncrementField = autoIncrementField;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public boolean isReferentielTable() {
		return referentielTable;
	}

	public void setReferentielTable(boolean referentielTable) {
		this.referentielTable = referentielTable;
	}
	
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@SuppressWarnings("unchecked")
	public static List<DbElementType> getElement(boolean includeReference) {
		DbElementType[] elts = values();
		List<DbElementType> out;
		
		if (includeReference) {
			out = Arrays.asList(elts);
		} else {
			out = new ArrayList<>();
	
			for (DbElementType e : elts) {
				if (!e.isReferentielTable()) {
					out.add(e);
				}
			}
		}
		
		Collections.sort(out, new Comparator<DbElementType>() {
			@Override
			public int compare(DbElementType o1, DbElementType o2) {
				return o1.getOrder() - o2.getOrder();
			}
		});
		
		return out;
	}
}