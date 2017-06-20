package com.cci.gpec.web.backingBean.ParamsGeneraux;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.ParamsGenerauxBean;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.ParamsGenerauxServiceImpl;
import com.cci.gpec.web.backingBean.salarie.SalarieFormBB;
import com.icesoft.faces.context.effects.JavascriptContext;

public class ParamsGenerauxFormBB {

	// primary key
	private Integer id;

	// fields
	private String ageLegalRetraiteAnN;
	private String ageLegalRetraiteAnN1;
	private String ageLegalRetraiteAnN2;

	private boolean canSave;

	private boolean ageLegalRetraiteAnNError = false;
	private boolean ageLegalRetraiteAnN1Error = false;
	private boolean ageLegalRetraiteAnN2Error = false;

	private boolean effectifMoyenAnNError = false;
	private boolean effectifMoyenAnN1Error = false;
	private boolean effectifMoyenAnN2Error = false;

	private boolean masseSalarialeAnNError = false;
	private boolean masseSalarialeAnN1Error = false;
	private boolean masseSalarialeAnN2Error = false;

	private boolean pourcentageFormationAnNError = false;
	private boolean pourcentageFormationAnN1Error = false;
	private boolean pourcentageFormationAnN2Error = false;

	private boolean dureeTravailAnNHeuresRealiseesEffectifTotError = false;
	private boolean dureeTravailAnN1HeuresRealiseesEffectifTotError = false;
	private boolean dureeTravailAnN2HeuresRealiseesEffectifTotError = false;

	private boolean dureeTravailAnNJoursEffectifTotError = false;
	private boolean dureeTravailAnN1JoursEffectifTotError = false;
	private boolean dureeTravailAnN2JoursEffectifTotError = false;

	private boolean dureeTravailAnNJoursSalError = false;
	private boolean dureeTravailAnN1JoursSalError = false;
	private boolean dureeTravailAnN2JoursSalError = false;

	private boolean dureeTravailAnNHeuresSalError = false;
	private boolean dureeTravailAnN1HeuresSalError = false;
	private boolean dureeTravailAnN2HeuresSalError = false;

	private boolean effectifPhysiqueAnNError = false;
	private boolean effectifPhysiqueAnN1Error = false;
	private boolean effectifPhysiqueAnN2Error = false;

	private boolean effectifEtpAnNError = false;
	private boolean effectifEtpAnN1Error = false;
	private boolean effectifEtpAnN2Error = false;

	private String effectifMoyenAnN;
	private String effectifMoyenAnN1;
	private String effectifMoyenAnN2;

	private String masseSalarialeAnN;
	private String masseSalarialeAnN1;
	private String masseSalarialeAnN2;

	private String pourcentageFormationAnN;
	private String pourcentageFormationAnN1;
	private String pourcentageFormationAnN2;

	private String dureeTravailAnNHeuresRealiseesEffectifTot;
	private String dureeTravailAnN1HeuresRealiseesEffectifTot;
	private String dureeTravailAnN2HeuresRealiseesEffectifTot;

	private String dureeTravailAnNJoursEffectifTot;
	private String dureeTravailAnN1JoursEffectifTot;
	private String dureeTravailAnN2JoursEffectifTot;

	private String dureeTravailAnNJoursSal;
	private String dureeTravailAnN1JoursSal;
	private String dureeTravailAnN2JoursSal;

	private String dureeTravailAnNHeuresSal;
	private String dureeTravailAnN1HeuresSal;
	private String dureeTravailAnN2HeuresSal;

	private String effectifPhysiqueAnN;
	private String effectifPhysiqueAnN1;
	private String effectifPhysiqueAnN2;

	private String effectifEtpAnN;
	private String effectifEtpAnN1;
	private String effectifEtpAnN2;

	private boolean modalRenderedConfirm = false;

	private String anneeN;
	private String anneeN1;
	private String anneeN2;

	private ArrayList<SelectItem> entrepriseList;
	protected int idEntrepriseSelected;

	private boolean saved;

	DecimalFormat df = new DecimalFormat();

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
			.getCurrentInstance().getCurrentInstance().getExternalContext()
			.getSessionMap().get("salarieFormBB");

	public ParamsGenerauxFormBB() throws Exception {
		init();
	}

	public void init() throws Exception {

		saved = false;
		modalRenderedConfirm = false;

		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");

		// init entreprises
		EntrepriseServiceImpl entrepriseService = new EntrepriseServiceImpl();

		List<EntrepriseBean> entrepriseBeanList = entrepriseService
				.getEntreprisesList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));
		entrepriseList = new ArrayList<SelectItem>();
		SelectItem selectItem;
		for (EntrepriseBean entrepriseBean : entrepriseBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(entrepriseBean.getId());
			selectItem.setLabel(entrepriseBean.getNom());
			entrepriseList.add(selectItem);
		}
		this.idEntrepriseSelected = -1;

		this.id = 0;
		this.ageLegalRetraiteAnN = null;
		this.ageLegalRetraiteAnN1 = null;
		this.ageLegalRetraiteAnN2 = null;

		this.dureeTravailAnNJoursEffectifTot = null;
		this.dureeTravailAnN1JoursEffectifTot = null;
		this.dureeTravailAnN2JoursEffectifTot = null;

		this.dureeTravailAnNHeuresSal = null;
		this.dureeTravailAnN1HeuresSal = null;
		this.dureeTravailAnN2HeuresSal = null;

		this.dureeTravailAnNHeuresRealiseesEffectifTot = null;
		this.dureeTravailAnN1HeuresRealiseesEffectifTot = null;
		this.dureeTravailAnN2HeuresRealiseesEffectifTot = null;

		this.dureeTravailAnNJoursSal = null;
		this.dureeTravailAnN1JoursSal = null;
		this.dureeTravailAnN2JoursSal = null;

		this.effectifMoyenAnN = null;
		this.effectifMoyenAnN1 = null;
		this.effectifMoyenAnN2 = null;

		this.pourcentageFormationAnN = null;
		this.pourcentageFormationAnN1 = null;
		this.pourcentageFormationAnN2 = null;

		this.masseSalarialeAnN = null;
		this.masseSalarialeAnN1 = null;
		this.masseSalarialeAnN2 = null;

		this.effectifPhysiqueAnN = null;
		this.effectifPhysiqueAnN1 = null;
		this.effectifPhysiqueAnN2 = null;

		this.effectifEtpAnN = null;
		this.effectifEtpAnN1 = null;
		this.effectifEtpAnN2 = null;

	}

	public boolean isDouble(String s) {
		Pattern p = Pattern.compile("^[0-9]+(\\.[0-9]{1,2})?$");
		Matcher m = p.matcher(s);

		return m.matches() || s.equals("");
	}

	public boolean isInteger(String s) {
		Pattern p = Pattern.compile("^[0-9]+$");
		Matcher m = p.matcher(s);

		return m.matches() || s.equals("");
	}

	public void checkDouble(ValueChangeEvent evt) {

		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");

		PhaseId phaseId = evt.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			evt.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			evt.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			String s = "";
			if (evt.getNewValue().toString().contains(",")) {
				s = evt.getNewValue().toString().replace(",", ".");
			} else {
				s = evt.getNewValue().toString();
			}
			if (!isDouble(s)) {
				if (evt.getComponent().getId().equals("effectifMoyenAnN")) {
					this.effectifMoyenAnNError = true;
				}
				if (evt.getComponent().getId().equals("effectifMoyenAnN1")) {
					this.effectifMoyenAnN1Error = true;
				}
				if (evt.getComponent().getId().equals("effectifMoyenAnN2")) {
					this.effectifMoyenAnN2Error = true;
				}

				if (evt.getComponent().getId().equals("effectifEtpAnN")) {
					this.effectifEtpAnNError = true;
				}
				if (evt.getComponent().getId().equals("effectifEtpAnN1")) {
					this.effectifEtpAnN1Error = true;
				}
				if (evt.getComponent().getId().equals("effectifEtpAnN2")) {
					this.effectifEtpAnN2Error = true;
				}

				if (evt.getComponent().getId()
						.equals("dureeTravailAnNJoursEffectifTot")) {
					this.dureeTravailAnNJoursEffectifTotError = true;
				}
				if (evt.getComponent().getId()
						.equals("dureeTravailAnN1JoursEffectifTot")) {
					this.dureeTravailAnN1JoursEffectifTotError = true;
				}
				if (evt.getComponent().getId()
						.equals("dureeTravailAnN2JoursEffectifTot")) {
					this.dureeTravailAnN2JoursEffectifTotError = true;
				}

				if (evt.getComponent().getId()
						.equals("dureeTravailAnNHeuresRealiseesEffectifTot")) {
					this.dureeTravailAnNHeuresRealiseesEffectifTotError = true;
				}
				if (evt.getComponent().getId()
						.equals("dureeTravailAnN1HeuresRealiseesEffectifTot")) {
					this.dureeTravailAnN1HeuresRealiseesEffectifTotError = true;
				}
				if (evt.getComponent().getId()
						.equals("dureeTravailAnN2HeuresRealiseesEffectifTot")) {
					this.dureeTravailAnN2HeuresRealiseesEffectifTotError = true;
				}

				if (evt.getComponent().getId().equals("masseSalarialeAnN")) {
					this.masseSalarialeAnNError = true;
				}
				if (evt.getComponent().getId().equals("masseSalarialeAnN1")) {
					this.masseSalarialeAnN1Error = true;
				}
				if (evt.getComponent().getId().equals("masseSalarialeAnN2")) {
					this.masseSalarialeAnN2Error = true;
				}

				if (evt.getComponent().getId()
						.equals("pourcentageFormationAnN")) {
					this.pourcentageFormationAnNError = true;
				}
				if (evt.getComponent().getId()
						.equals("pourcentageFormationAnN1")) {
					this.pourcentageFormationAnN1Error = true;
				}
				if (evt.getComponent().getId()
						.equals("pourcentageFormationAnN2")) {
					this.pourcentageFormationAnN2Error = true;
				}
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"L'effectif physique total et l'\u00e2ge de la retraite sont exprim\u00E9s par des nombres entiers. Les autres valeurs peuvent \u00eatre décimales.",
						"L'effectif physique total et l'\u00e2ge de la retraite sont exprim\u00E9s par des nombres entiers. Les autres valeurs peuvent \u00eatre décimales.");
				FacesContext.getCurrentInstance().addMessage(
						"j_id159:ageLegalRetraiteAnN", message);
				modalRenderedConfirm = false;
			} else {
				if (evt.getComponent().getId().equals("effectifMoyenAnN")) {
					this.effectifMoyenAnN = df.format(Double.valueOf(s));
					effectifMoyenAnNError = false;
				}
				if (evt.getComponent().getId().equals("effectifMoyenAnN1")) {
					this.effectifMoyenAnN1 = df.format(Double.valueOf(s));
					effectifMoyenAnN1Error = false;
				}
				if (evt.getComponent().getId().equals("effectifMoyenAnN2")) {
					this.effectifMoyenAnN2 = df.format(Double.valueOf(s));
					effectifMoyenAnN2Error = false;
				}

				if (evt.getComponent().getId().equals("effectifEtpAnN")) {
					this.effectifEtpAnN = df.format(Double.valueOf(s));
					effectifEtpAnNError = false;
				}
				if (evt.getComponent().getId().equals("effectifEtpAnN1")) {
					this.effectifEtpAnN1 = df.format(Double.valueOf(s));
					effectifEtpAnN1Error = false;
				}
				if (evt.getComponent().getId().equals("effectifEtpAnN2")) {
					this.effectifEtpAnN2 = df.format(Double.valueOf(s));
					effectifEtpAnN2Error = false;
				}

				if (evt.getComponent().getId()
						.equals("dureeTravailAnNJoursEffectifTot")) {
					this.dureeTravailAnNJoursEffectifTot = df.format(Double
							.valueOf(s));
					dureeTravailAnNJoursEffectifTotError = false;
				}
				if (evt.getComponent().getId()
						.equals("dureeTravailAnN1JoursEffectifTot")) {
					this.dureeTravailAnN1JoursEffectifTot = df.format(Double
							.valueOf(s));
					dureeTravailAnN1JoursEffectifTotError = false;
				}
				if (evt.getComponent().getId()
						.equals("dureeTravailAnN2JoursEffectifTot")) {
					this.dureeTravailAnN2JoursEffectifTot = df.format(Double
							.valueOf(s));
					dureeTravailAnN2JoursEffectifTotError = false;
				}

				if (evt.getComponent().getId()
						.equals("dureeTravailAnNHeuresRealiseesEffectifTot")) {
					this.dureeTravailAnNHeuresRealiseesEffectifTot = df
							.format(Double.valueOf(s));
					dureeTravailAnNHeuresRealiseesEffectifTotError = false;
				}
				if (evt.getComponent().getId()
						.equals("dureeTravailAnN1HeuresRealiseesEffectifTot")) {
					this.dureeTravailAnN1HeuresRealiseesEffectifTot = df
							.format(Double.valueOf(s));
					dureeTravailAnN1HeuresRealiseesEffectifTotError = false;
				}
				if (evt.getComponent().getId()
						.equals("dureeTravailAnN2HeuresRealiseesEffectifTot")) {
					this.dureeTravailAnN2HeuresRealiseesEffectifTot = df
							.format(Double.valueOf(s));
					dureeTravailAnN2HeuresRealiseesEffectifTotError = false;
				}

				if (evt.getComponent().getId().equals("masseSalarialeAnN")) {
					this.masseSalarialeAnN = df.format(Double.valueOf(s));
					masseSalarialeAnNError = false;
				}
				if (evt.getComponent().getId().equals("masseSalarialeAnN1")) {
					this.masseSalarialeAnN1 = df.format(Double.valueOf(s));
					masseSalarialeAnN1Error = false;
				}
				if (evt.getComponent().getId().equals("masseSalarialeAnN2")) {
					this.masseSalarialeAnN2 = df.format(Double.valueOf(s));
					masseSalarialeAnN2Error = false;
				}

				if (evt.getComponent().getId()
						.equals("pourcentageFormationAnN")) {
					this.pourcentageFormationAnN = df.format(Double.valueOf(s));
					pourcentageFormationAnNError = false;
				}
				if (evt.getComponent().getId()
						.equals("pourcentageFormationAnN1")) {
					this.pourcentageFormationAnN1 = df
							.format(Double.valueOf(s));
					pourcentageFormationAnN1Error = false;
				}
				if (evt.getComponent().getId()
						.equals("pourcentageFormationAnN2")) {
					this.pourcentageFormationAnN2 = df
							.format(Double.valueOf(s));
					pourcentageFormationAnN2Error = false;
				}
				if (effectifMoyenAnNError || effectifMoyenAnN1Error
						|| effectifMoyenAnN2Error || effectifEtpAnNError
						|| effectifEtpAnN1Error || effectifEtpAnN2Error
						|| dureeTravailAnNJoursEffectifTotError
						|| dureeTravailAnN1JoursEffectifTotError
						|| dureeTravailAnN2JoursEffectifTotError
						|| dureeTravailAnNHeuresRealiseesEffectifTotError
						|| dureeTravailAnN1HeuresRealiseesEffectifTotError
						|| dureeTravailAnN2HeuresRealiseesEffectifTotError
						|| masseSalarialeAnNError || masseSalarialeAnN1Error
						|| masseSalarialeAnN2Error
						|| pourcentageFormationAnNError
						|| pourcentageFormationAnN1Error
						|| pourcentageFormationAnN2Error) {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"L'effectif physique total et l'\u00e2ge de la retraite sont exprim\u00E9s par des nombres entiers. Les autres valeurs peuvent \u00eatre décimales.",
							"L'effectif physique total et l'\u00e2ge de la retraite sont exprim\u00E9s par des nombres entiers. Les autres valeurs peuvent \u00eatre décimales.");
					FacesContext.getCurrentInstance().addMessage(
							"j_id159:ageLegalRetraiteAnN", message);
				} else {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "", "");
					FacesContext.getCurrentInstance().addMessage(
							"j_id159:ageLegalRetraiteAnN", message);
				}
				modalRenderedConfirm = false;
			}
		}
	}

	public void checkInteger(ValueChangeEvent evt) {

		PhaseId phaseId = evt.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			evt.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			evt.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			if (!isInteger(evt.getNewValue().toString())) {
				if (evt.getComponent().getId().equals("ageLegalRetraiteAnN")) {
					ageLegalRetraiteAnNError = true;
				}
				if (evt.getComponent().getId().equals("ageLegalRetraiteAnN1")) {
					ageLegalRetraiteAnN1Error = true;
				}
				if (evt.getComponent().getId().equals("ageLegalRetraiteAnN2")) {
					ageLegalRetraiteAnN2Error = true;
				}

				if (evt.getComponent().getId().equals("effectifPhysiqueAnN")) {
					this.effectifPhysiqueAnNError = true;
				}
				if (evt.getComponent().getId().equals("effectifPhysiqueAnN1")) {
					this.effectifPhysiqueAnN1Error = true;
				}
				if (evt.getComponent().getId().equals("effectifPhysiqueAnN2")) {
					this.effectifPhysiqueAnN2Error = true;
				}

				if (evt.getComponent().getId()
						.equals("dureeTravailAnNHeuresSal")) {
					this.dureeTravailAnNHeuresSalError = true;
				}
				if (evt.getComponent().getId()
						.equals("dureeTravailAnN1HeuresSal")) {
					this.dureeTravailAnN1HeuresSalError = true;
				}
				if (evt.getComponent().getId()
						.equals("dureeTravailAnN2HeuresSal")) {
					this.dureeTravailAnN2HeuresSalError = true;
				}

				if (evt.getComponent().getId()
						.equals("dureeTravailAnNJoursSal")) {
					this.dureeTravailAnNJoursSalError = true;
				}
				if (evt.getComponent().getId()
						.equals("dureeTravailAnN1JoursSal")) {
					this.dureeTravailAnN1JoursSalError = true;
				}
				if (evt.getComponent().getId()
						.equals("dureeTravailAnN2JoursSal")) {
					this.dureeTravailAnN2JoursSalError = true;
				}

				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"L'effectif physique total et l'\u00e2ge de la retraite sont exprim\u00E9s par des nombres entiers. Les autres valeurs peuvent \u00eatre décimales.",
						"L'effectif physique total et l'\u00e2ge de la retraite sont exprim\u00E9s par des nombres entiers. Les autres valeurs peuvent \u00eatre décimales.");
				FacesContext.getCurrentInstance().addMessage(
						"j_id159:ageLegalRetraiteAnN", message);
				modalRenderedConfirm = false;
				return;
			} else {
				if (evt.getComponent().getId().equals("ageLegalRetraiteAnN")) {
					ageLegalRetraiteAnNError = false;
				}
				if (evt.getComponent().getId().equals("ageLegalRetraiteAnN1")) {
					ageLegalRetraiteAnN1Error = false;
				}
				if (evt.getComponent().getId().equals("ageLegalRetraiteAnN2")) {
					ageLegalRetraiteAnN2Error = false;
				}
				if (evt.getComponent().getId().equals("effectifPhysiqueAnN")) {
					this.effectifPhysiqueAnNError = false;
				}
				if (evt.getComponent().getId().equals("effectifPhysiqueAnN1")) {
					this.effectifPhysiqueAnN1Error = false;
				}
				if (evt.getComponent().getId().equals("effectifPhysiqueAnN2")) {
					this.effectifPhysiqueAnN2Error = false;
				}

				if (evt.getComponent().getId()
						.equals("dureeTravailAnNHeuresSal")) {
					this.dureeTravailAnNHeuresSalError = false;
				}
				if (evt.getComponent().getId()
						.equals("dureeTravailAnN1HeuresSal")) {
					this.dureeTravailAnN1HeuresSalError = false;
				}
				if (evt.getComponent().getId()
						.equals("dureeTravailAnN2HeuresSal")) {
					this.dureeTravailAnN2HeuresSalError = false;
				}

				if (evt.getComponent().getId()
						.equals("dureeTravailAnNJoursSal")) {
					this.dureeTravailAnNJoursSalError = false;
				}
				if (evt.getComponent().getId()
						.equals("dureeTravailAnN1JoursSal")) {
					this.dureeTravailAnN1JoursSalError = false;
				}
				if (evt.getComponent().getId()
						.equals("dureeTravailAnN2JoursSal")) {
					this.dureeTravailAnN2JoursSalError = false;
				}
				if (ageLegalRetraiteAnNError || ageLegalRetraiteAnN1Error
						|| ageLegalRetraiteAnN2Error
						|| effectifPhysiqueAnNError
						|| effectifPhysiqueAnN1Error
						|| effectifPhysiqueAnN2Error
						|| dureeTravailAnNHeuresSalError
						|| dureeTravailAnN1HeuresSalError
						|| dureeTravailAnN2HeuresSalError
						|| dureeTravailAnNJoursSalError
						|| dureeTravailAnN1JoursSalError
						|| dureeTravailAnN2JoursSalError) {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"L'effectif physique total et l'\u00e2ge de la retraite sont exprim\u00E9s par des nombres entiers. Les autres valeurs peuvent \u00eatre décimales.",
							"L'effectif physique total et l'\u00e2ge de la retraite sont exprim\u00E9s par des nombres entiers. Les autres valeurs peuvent \u00eatre décimales.");
					FacesContext.getCurrentInstance().addMessage(
							"j_id159:ageLegalRetraiteAnN", message);
				} else {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "", "");
					FacesContext.getCurrentInstance().addMessage(
							"j_id159:ageLegalRetraiteAnN", message);
				}
				modalRenderedConfirm = false;
			}

		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAgeLegalRetraiteAnN() {
		return ageLegalRetraiteAnN;
	}

	public void setAgeLegalRetraiteAnN(String ageLegalRetraiteAnN) {
		this.ageLegalRetraiteAnN = ageLegalRetraiteAnN;
	}

	public String getAgeLegalRetraiteAnN1() {
		return ageLegalRetraiteAnN1;
	}

	public void setAgeLegalRetraiteAnN1(String ageLegalRetraiteAnN1) {
		this.ageLegalRetraiteAnN1 = ageLegalRetraiteAnN1;
	}

	public String getAgeLegalRetraiteAnN2() {
		return ageLegalRetraiteAnN2;
	}

	public void setAgeLegalRetraiteAnN2(String ageLegalRetraiteAnN2) {
		this.ageLegalRetraiteAnN2 = ageLegalRetraiteAnN2;
	}

	public String getDureeTravailAnNHeuresRealiseesEffectifTot() {
		return dureeTravailAnNHeuresRealiseesEffectifTot;
	}

	public void setDureeTravailAnNHeuresRealiseesEffectifTot(
			String dureeTravailAnNHeuresRealiseesEffectifTot) {
		this.dureeTravailAnNHeuresRealiseesEffectifTot = dureeTravailAnNHeuresRealiseesEffectifTot;
	}

	public String getDureeTravailAnNJoursEffectifTot() {
		return dureeTravailAnNJoursEffectifTot;
	}

	public void setDureeTravailAnNJoursEffectifTot(
			String dureeTravailAnNJoursEffectifTot) {
		this.dureeTravailAnNJoursEffectifTot = dureeTravailAnNJoursEffectifTot;
	}

	public String getDureeTravailAnNJoursSal() {
		return dureeTravailAnNJoursSal;
	}

	public void setDureeTravailAnNJoursSal(String dureeTravailAnNJoursSal) {
		this.dureeTravailAnNJoursSal = dureeTravailAnNJoursSal;
	}

	public String getDureeTravailAnNHeuresSal() {
		return dureeTravailAnNHeuresSal;
	}

	public void setDureeTravailAnNHeuresSal(String dureeTravailAnNHeuresSal) {
		this.dureeTravailAnNHeuresSal = dureeTravailAnNHeuresSal;
	}

	public boolean isCanSave() {
		return (effectifMoyenAnNError || effectifMoyenAnN1Error
				|| effectifMoyenAnN2Error || effectifEtpAnNError
				|| effectifEtpAnN1Error || effectifEtpAnN2Error
				|| dureeTravailAnNJoursEffectifTotError
				|| dureeTravailAnN1JoursEffectifTotError
				|| dureeTravailAnN2JoursEffectifTotError
				|| dureeTravailAnNHeuresRealiseesEffectifTotError
				|| dureeTravailAnN1HeuresRealiseesEffectifTotError
				|| dureeTravailAnN2HeuresRealiseesEffectifTotError
				|| masseSalarialeAnNError || masseSalarialeAnN1Error
				|| masseSalarialeAnN2Error || pourcentageFormationAnNError
				|| pourcentageFormationAnN1Error
				|| pourcentageFormationAnN2Error || ageLegalRetraiteAnNError
				|| ageLegalRetraiteAnN1Error || ageLegalRetraiteAnN2Error
				|| effectifPhysiqueAnNError || effectifPhysiqueAnN1Error
				|| effectifPhysiqueAnN2Error || dureeTravailAnNHeuresSalError
				|| dureeTravailAnN1HeuresSalError
				|| dureeTravailAnN2HeuresSalError
				|| dureeTravailAnNJoursSalError
				|| dureeTravailAnN1JoursSalError || dureeTravailAnN2JoursSalError);
	}

	public void saveOrUpdateParamsG(ActionEvent event) {
		saved = true;
		ParamsGenerauxServiceImpl paramsGenerauxService = new ParamsGenerauxServiceImpl();

		ParamsGenerauxBean paramsGenerauxBean = new ParamsGenerauxBean();

		paramsGenerauxBean.setId(this.id);
		Date aujourdhui = new Date();
		int annee = 1900 + aujourdhui.getYear();
		paramsGenerauxBean.setAnnee(annee);
		paramsGenerauxBean
				.setAgeLegalRetraiteAnN((!this.ageLegalRetraiteAnN.equals("")) ? Integer
						.valueOf(this.ageLegalRetraiteAnN) : null);
		paramsGenerauxBean.setAgeLegalRetraiteAnN1((!this.ageLegalRetraiteAnN1
				.equals("")) ? Integer.valueOf(this.ageLegalRetraiteAnN1)
				: null);
		paramsGenerauxBean.setAgeLegalRetraiteAnN2((!this.ageLegalRetraiteAnN2
				.equals("")) ? Integer.valueOf(this.ageLegalRetraiteAnN2)
				: null);

		if (this.dureeTravailAnNJoursEffectifTot != null) {
			paramsGenerauxBean
					.setDureeTravailAnNJoursEffectifTot((!this.dureeTravailAnNJoursEffectifTot
							.equals("")) ? Double
							.valueOf(this.dureeTravailAnNJoursEffectifTot)
							: null);
		}

		if (this.dureeTravailAnN1JoursEffectifTot != null) {
			paramsGenerauxBean
					.setDureeTravailAnN1JoursEffectifTot((!this.dureeTravailAnN1JoursEffectifTot
							.equals("")) ? Double
							.valueOf(this.dureeTravailAnN1JoursEffectifTot)
							: null);
		}

		if (this.dureeTravailAnN2JoursEffectifTot != null) {
			paramsGenerauxBean
					.setDureeTravailAnN2JoursEffectifTot((!this.dureeTravailAnN2JoursEffectifTot
							.equals("")) ? Double
							.valueOf(this.dureeTravailAnN2JoursEffectifTot)
							: null);
		}

		paramsGenerauxBean
				.setDureeTravailAnNHeuresSal((!this.dureeTravailAnNHeuresSal
						.equals("")) ? Integer
						.valueOf(this.dureeTravailAnNHeuresSal) : null);
		paramsGenerauxBean
				.setDureeTravailAnN1HeuresSal((!this.dureeTravailAnN1HeuresSal
						.equals("")) ? Integer
						.valueOf(this.dureeTravailAnN1HeuresSal) : null);
		paramsGenerauxBean
				.setDureeTravailAnN2HeuresSal((!this.dureeTravailAnN2HeuresSal
						.equals("")) ? Integer
						.valueOf(this.dureeTravailAnN2HeuresSal) : null);

		if (this.dureeTravailAnNHeuresRealiseesEffectifTot != null) {
			paramsGenerauxBean
					.setDureeTravailAnNHeuresRealiseesEffectifTot((!this.dureeTravailAnNHeuresRealiseesEffectifTot
							.equals("")) ? Double
							.valueOf(this.dureeTravailAnNHeuresRealiseesEffectifTot)
							: null);
		}

		if (this.dureeTravailAnN1HeuresRealiseesEffectifTot != null) {
			paramsGenerauxBean
					.setDureeTravailAnN1HeuresRealiseesEffectifTot((!this.dureeTravailAnN1HeuresRealiseesEffectifTot
							.equals("")) ? Double
							.valueOf(this.dureeTravailAnN1HeuresRealiseesEffectifTot)
							: null);
		}

		if (this.dureeTravailAnN2HeuresRealiseesEffectifTot != null) {
			paramsGenerauxBean
					.setDureeTravailAnN2HeuresRealiseesEffectifTot((!this.dureeTravailAnN2HeuresRealiseesEffectifTot
							.equals("")) ? Double
							.valueOf(this.dureeTravailAnN2HeuresRealiseesEffectifTot)
							: null);
		}

		paramsGenerauxBean
				.setDureeTravailAnNJoursSal((!this.dureeTravailAnNJoursSal
						.equals("")) ? Integer
						.valueOf(this.dureeTravailAnNJoursSal) : null);
		paramsGenerauxBean
				.setDureeTravailAnN1JoursSal((!this.dureeTravailAnN1JoursSal
						.equals("")) ? Integer
						.valueOf(this.dureeTravailAnN1JoursSal) : null);
		paramsGenerauxBean
				.setDureeTravailAnN2JoursSal((!this.dureeTravailAnN2JoursSal
						.equals("")) ? Integer
						.valueOf(this.dureeTravailAnN2JoursSal) : null);

		if (this.effectifMoyenAnN != null) {
			paramsGenerauxBean
					.setEffectifMoyenAnN((!this.effectifMoyenAnN.equals("")) ? Double
							.valueOf(this.effectifMoyenAnN) : null);
		}

		if (this.effectifMoyenAnN1 != null) {
			paramsGenerauxBean.setEffectifMoyenAnN1((!this.effectifMoyenAnN1
					.equals("")) ? Double.valueOf(this.effectifMoyenAnN1)
					: null);
		}

		if (this.effectifMoyenAnN2 != null) {
			paramsGenerauxBean.setEffectifMoyenAnN2((!this.effectifMoyenAnN2
					.equals("")) ? Double.valueOf(this.effectifMoyenAnN2)
					: null);
		}

		if (this.masseSalarialeAnN != null) {
			paramsGenerauxBean.setMasseSalarialeAnN((!this.masseSalarialeAnN
					.equals("")) ? Double.valueOf(this.masseSalarialeAnN)
					: null);
		}
		if (this.masseSalarialeAnN1 != null) {
			paramsGenerauxBean.setMasseSalarialeAnN1((!this.masseSalarialeAnN1
					.equals("")) ? Double.valueOf(this.masseSalarialeAnN1)
					: null);
		}
		if (this.masseSalarialeAnN2 != null) {
			paramsGenerauxBean.setMasseSalarialeAnN2((!this.masseSalarialeAnN2
					.equals("")) ? Double.valueOf(this.masseSalarialeAnN2)
					: null);
		}
		if (this.pourcentageFormationAnN != null) {
			paramsGenerauxBean
					.setPourcentageFormationAnN((!this.pourcentageFormationAnN
							.equals("")) ? Double
							.valueOf(this.pourcentageFormationAnN) : null);
		}
		if (this.pourcentageFormationAnN1 != null) {
			paramsGenerauxBean
					.setPourcentageFormationAnN1((!this.pourcentageFormationAnN1
							.equals("")) ? Double
							.valueOf(this.pourcentageFormationAnN1) : null);
		}
		if (this.pourcentageFormationAnN2 != null) {
			paramsGenerauxBean
					.setPourcentageFormationAnN2((!this.pourcentageFormationAnN2
							.equals("")) ? Double
							.valueOf(this.pourcentageFormationAnN2) : null);
		}
		paramsGenerauxBean
				.setEffectifPhysiqueAnN((!this.effectifPhysiqueAnN.equals("")) ? Integer
						.valueOf(this.effectifPhysiqueAnN) : null);
		paramsGenerauxBean.setEffectifPhysiqueAnN1((!this.effectifPhysiqueAnN1
				.equals("")) ? Integer.valueOf(this.effectifPhysiqueAnN1)
				: null);
		paramsGenerauxBean.setEffectifPhysiqueAnN2((!this.effectifPhysiqueAnN2
				.equals("")) ? Integer.valueOf(this.effectifPhysiqueAnN2)
				: null);
		if (this.effectifEtpAnN != null) {
			paramsGenerauxBean.setEffectifEtpAnN((!this.effectifEtpAnN
					.equals("")) ? Double.valueOf(this.effectifEtpAnN) : null);
		}

		if (this.effectifEtpAnN1 != null) {
			paramsGenerauxBean.setEffectifEtpAnN1((!this.effectifEtpAnN1
					.equals("")) ? Double.valueOf(this.effectifEtpAnN1) : null);
		}

		if (this.effectifEtpAnN2 != null) {
			paramsGenerauxBean.setEffectifEtpAnN2((!this.effectifEtpAnN2
					.equals("")) ? Double.valueOf(this.effectifEtpAnN2) : null);
		}

		paramsGenerauxBean.setIdEntreprise(this.idEntrepriseSelected);

		paramsGenerauxService.saveOrUppdate(paramsGenerauxBean);

		modalRenderedConfirm = true;

	}

	public boolean isModalRenderedConfirm() {
		return modalRenderedConfirm;
	}

	public void setModalRenderedConfirm(boolean modalRenderedConfirm) {
		this.modalRenderedConfirm = modalRenderedConfirm;
	}

	public String getDureeTravailAnN1HeuresRealiseesEffectifTot() {
		return dureeTravailAnN1HeuresRealiseesEffectifTot;
	}

	public void setDureeTravailAnN1HeuresRealiseesEffectifTot(
			String dureeTravailAnN1HeuresRealiseesEffectifTot) {
		this.dureeTravailAnN1HeuresRealiseesEffectifTot = dureeTravailAnN1HeuresRealiseesEffectifTot;
	}

	public String getDureeTravailAnN2HeuresRealiseesEffectifTot() {
		return dureeTravailAnN2HeuresRealiseesEffectifTot;
	}

	public void setDureeTravailAnN2HeuresRealiseesEffectifTot(
			String dureeTravailAnN2HeuresRealiseesEffectifTot) {
		this.dureeTravailAnN2HeuresRealiseesEffectifTot = dureeTravailAnN2HeuresRealiseesEffectifTot;
	}

	public String getDureeTravailAnN1JoursEffectifTot() {
		return dureeTravailAnN1JoursEffectifTot;
	}

	public void setDureeTravailAnN1JoursEffectifTot(
			String dureeTravailAnN1JoursEffectifTot) {
		this.dureeTravailAnN1JoursEffectifTot = dureeTravailAnN1JoursEffectifTot;
	}

	public String getDureeTravailAnN2JoursEffectifTot() {
		return dureeTravailAnN2JoursEffectifTot;
	}

	public void setDureeTravailAnN2JoursEffectifTot(
			String dureeTravailAnN2JoursEffectifTot) {
		this.dureeTravailAnN2JoursEffectifTot = dureeTravailAnN2JoursEffectifTot;
	}

	public String getDureeTravailAnN1JoursSal() {
		return dureeTravailAnN1JoursSal;
	}

	public void setDureeTravailAnN1JoursSal(String dureeTravailAnN1JoursSal) {
		this.dureeTravailAnN1JoursSal = dureeTravailAnN1JoursSal;
	}

	public String getDureeTravailAnN2JoursSal() {
		return dureeTravailAnN2JoursSal;
	}

	public void setDureeTravailAnN2JoursSal(String dureeTravailAnN2JoursSal) {
		this.dureeTravailAnN2JoursSal = dureeTravailAnN2JoursSal;
	}

	public String getDureeTravailAnN1HeuresSal() {
		return dureeTravailAnN1HeuresSal;
	}

	public void setDureeTravailAnN1HeuresSal(String dureeTravailAnN1HeuresSal) {
		this.dureeTravailAnN1HeuresSal = dureeTravailAnN1HeuresSal;
	}

	public String getDureeTravailAnN2HeuresSal() {
		return dureeTravailAnN2HeuresSal;
	}

	public void setDureeTravailAnN2HeuresSal(String dureeTravailAnN2HeuresSal) {
		this.dureeTravailAnN2HeuresSal = dureeTravailAnN2HeuresSal;
	}

	public int getIdEntrepriseSelected() {
		return idEntrepriseSelected;
	}

	public void setIdEntrepriseSelected(int idEntrepriseSelected) {
		this.idEntrepriseSelected = idEntrepriseSelected;
	}

	public ArrayList<SelectItem> getEntrepriseList() {
		return entrepriseList;
	}

	public void changeParamsG(ValueChangeEvent event) throws Exception {

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				"", "");
		FacesContext.getCurrentInstance().addMessage(
				"j_id159:ageLegalRetraiteAnN", message);

		if (event.getComponent().getId().equals("entrepriseListParamsG")) {
			idEntrepriseSelected = Integer.parseInt(event.getNewValue()
					.toString());
		}

		getParamsG();

		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}
	}

	public void printParamsGeneraux(ActionEvent evt) {
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("idEntreprise", this.idEntrepriseSelected);
		eContext.getSessionMap().put("nomGroupe", salarieFormBB.getNomGroupe());

		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.printParamsGeneraux?contentType=pdf \",\"_Reports\");");
	}

	public String quitter() {
		this.idEntrepriseSelected = -1;
		return "paramsGeneraux";
	}

	public void getParamsG() throws Exception {
		ParamsGenerauxServiceImpl paramsGenerauxService = new ParamsGenerauxServiceImpl();

		modalRenderedConfirm = false;

		ageLegalRetraiteAnNError = false;
		ageLegalRetraiteAnN1Error = false;
		ageLegalRetraiteAnN2Error = false;

		verifSiChangementAnnee();

		ArrayList<ParamsGenerauxBean> paramsGenerauxBeanList = (ArrayList<ParamsGenerauxBean>) paramsGenerauxService
				.getParamsGenerauxBeanListByIdEntreprise(idEntrepriseSelected);

		if (paramsGenerauxBeanList != null && paramsGenerauxBeanList.size() > 0) {
			saved = true;
			ParamsGenerauxBean paramsGenerauxBean = paramsGenerauxBeanList
					.get(0);

			this.id = paramsGenerauxBean.getId();
			this.ageLegalRetraiteAnN = (paramsGenerauxBean
					.getAgeLegalRetraiteAnN() == null) ? ""
					: paramsGenerauxBean.getAgeLegalRetraiteAnN().toString();
			this.ageLegalRetraiteAnN1 = (paramsGenerauxBean
					.getAgeLegalRetraiteAnN1() == null) ? ""
					: paramsGenerauxBean.getAgeLegalRetraiteAnN1().toString();
			this.ageLegalRetraiteAnN2 = (paramsGenerauxBean
					.getAgeLegalRetraiteAnN2() == null) ? ""
					: paramsGenerauxBean.getAgeLegalRetraiteAnN2().toString();

			this.dureeTravailAnNJoursEffectifTot = paramsGenerauxBean
					.getDureeTravailAnNJoursEffectifTot() != null ? df
					.format(paramsGenerauxBean
							.getDureeTravailAnNJoursEffectifTot()) : null;
			this.dureeTravailAnN1JoursEffectifTot = paramsGenerauxBean
					.getDureeTravailAnN1JoursEffectifTot() != null ? df
					.format(paramsGenerauxBean
							.getDureeTravailAnN1JoursEffectifTot()) : null;
			this.dureeTravailAnN2JoursEffectifTot = paramsGenerauxBean
					.getDureeTravailAnN2JoursEffectifTot() != null ? df
					.format(paramsGenerauxBean
							.getDureeTravailAnN2JoursEffectifTot()) : null;

			this.dureeTravailAnNHeuresSal = (paramsGenerauxBean
					.getDureeTravailAnNHeuresSal() != null) ? paramsGenerauxBean
					.getDureeTravailAnNHeuresSal().toString() : null;
			this.dureeTravailAnN1HeuresSal = (paramsGenerauxBean
					.getDureeTravailAnN1HeuresSal() != null) ? paramsGenerauxBean
					.getDureeTravailAnN1HeuresSal().toString() : null;
			this.dureeTravailAnN2HeuresSal = (paramsGenerauxBean
					.getDureeTravailAnN2HeuresSal() != null) ? paramsGenerauxBean
					.getDureeTravailAnN2HeuresSal().toString() : null;

			this.dureeTravailAnNHeuresRealiseesEffectifTot = paramsGenerauxBean
					.getDureeTravailAnNHeuresRealiseesEffectifTot() != null ? df
					.format(paramsGenerauxBean
							.getDureeTravailAnNHeuresRealiseesEffectifTot())
					: null;
			this.dureeTravailAnN1HeuresRealiseesEffectifTot = paramsGenerauxBean
					.getDureeTravailAnN1HeuresRealiseesEffectifTot() != null ? df
					.format(paramsGenerauxBean
							.getDureeTravailAnN1HeuresRealiseesEffectifTot())
					: null;
			this.dureeTravailAnN2HeuresRealiseesEffectifTot = paramsGenerauxBean
					.getDureeTravailAnN2HeuresRealiseesEffectifTot() != null ? df
					.format(paramsGenerauxBean
							.getDureeTravailAnN2HeuresRealiseesEffectifTot())
					: null;

			this.dureeTravailAnNJoursSal = (paramsGenerauxBean
					.getDureeTravailAnNJoursSal() != null) ? paramsGenerauxBean
					.getDureeTravailAnNJoursSal().toString() : null;
			this.dureeTravailAnN1JoursSal = (paramsGenerauxBean
					.getDureeTravailAnN1JoursSal() != null) ? paramsGenerauxBean
					.getDureeTravailAnN1JoursSal().toString() : null;
			this.dureeTravailAnN2JoursSal = (paramsGenerauxBean
					.getDureeTravailAnN2JoursSal() != null) ? paramsGenerauxBean
					.getDureeTravailAnN2JoursSal().toString() : null;

			this.effectifMoyenAnN = paramsGenerauxBean.getEffectifMoyenAnN() != null ? df
					.format(paramsGenerauxBean.getEffectifMoyenAnN()) : null;
			this.effectifMoyenAnN1 = paramsGenerauxBean.getEffectifMoyenAnN1() != null ? df
					.format(paramsGenerauxBean.getEffectifMoyenAnN1()) : null;
			this.effectifMoyenAnN2 = paramsGenerauxBean.getEffectifMoyenAnN2() != null ? df
					.format(paramsGenerauxBean.getEffectifMoyenAnN2()) : null;

			this.pourcentageFormationAnN = paramsGenerauxBean
					.getPourcentageFormationAnN() != null ? df
					.format(paramsGenerauxBean.getPourcentageFormationAnN())
					: null;
			this.pourcentageFormationAnN1 = paramsGenerauxBean
					.getPourcentageFormationAnN1() != null ? df
					.format(paramsGenerauxBean.getPourcentageFormationAnN1())
					: null;
			this.pourcentageFormationAnN2 = paramsGenerauxBean
					.getPourcentageFormationAnN2() != null ? df
					.format(paramsGenerauxBean.getPourcentageFormationAnN2())
					: null;

			this.masseSalarialeAnN = paramsGenerauxBean.getMasseSalarialeAnN() != null ? df
					.format(paramsGenerauxBean.getMasseSalarialeAnN()) : null;
			this.masseSalarialeAnN1 = paramsGenerauxBean
					.getMasseSalarialeAnN1() != null ? df
					.format(paramsGenerauxBean.getMasseSalarialeAnN1()) : null;
			this.masseSalarialeAnN2 = paramsGenerauxBean
					.getMasseSalarialeAnN2() != null ? df
					.format(paramsGenerauxBean.getMasseSalarialeAnN2()) : null;

			this.effectifPhysiqueAnN = (paramsGenerauxBean
					.getEffectifPhysiqueAnN() != null) ? paramsGenerauxBean
					.getEffectifPhysiqueAnN().toString() : null;
			this.effectifPhysiqueAnN1 = (paramsGenerauxBean
					.getEffectifPhysiqueAnN1() != null) ? paramsGenerauxBean
					.getEffectifPhysiqueAnN1().toString() : null;
			this.effectifPhysiqueAnN2 = (paramsGenerauxBean
					.getEffectifPhysiqueAnN2() != null) ? paramsGenerauxBean
					.getEffectifPhysiqueAnN2().toString() : null;

			this.effectifEtpAnN = paramsGenerauxBean.getEffectifEtpAnN() != null ? df
					.format(paramsGenerauxBean.getEffectifEtpAnN()) : null;
			this.effectifEtpAnN1 = paramsGenerauxBean.getEffectifEtpAnN1() != null ? df
					.format(paramsGenerauxBean.getEffectifEtpAnN1()) : null;
			this.effectifEtpAnN2 = paramsGenerauxBean.getEffectifEtpAnN2() != null ? df
					.format(paramsGenerauxBean.getEffectifEtpAnN2()) : null;

		} else {
			saved = false;
			this.id = 0;
			this.ageLegalRetraiteAnN = null;
			this.ageLegalRetraiteAnN1 = null;
			this.ageLegalRetraiteAnN2 = null;

			this.dureeTravailAnNJoursEffectifTot = null;
			this.dureeTravailAnN1JoursEffectifTot = null;
			this.dureeTravailAnN2JoursEffectifTot = null;

			this.dureeTravailAnNHeuresSal = null;
			this.dureeTravailAnN1HeuresSal = null;
			this.dureeTravailAnN2HeuresSal = null;

			this.dureeTravailAnNHeuresRealiseesEffectifTot = null;
			this.dureeTravailAnN1HeuresRealiseesEffectifTot = null;
			this.dureeTravailAnN2HeuresRealiseesEffectifTot = null;

			this.dureeTravailAnNJoursSal = null;
			this.dureeTravailAnN1JoursSal = null;
			this.dureeTravailAnN2JoursSal = null;

			this.effectifMoyenAnN = null;
			this.effectifMoyenAnN1 = null;
			this.effectifMoyenAnN2 = null;

			this.masseSalarialeAnN = null;
			this.masseSalarialeAnN1 = null;
			this.masseSalarialeAnN2 = null;

			this.pourcentageFormationAnN = null;
			this.pourcentageFormationAnN1 = null;
			this.pourcentageFormationAnN2 = null;

			this.effectifPhysiqueAnN = null;
			this.effectifPhysiqueAnN1 = null;
			this.effectifPhysiqueAnN2 = null;

			this.effectifEtpAnN = null;
			this.effectifEtpAnN1 = null;
			this.effectifEtpAnN2 = null;
		}
	}

	// On vérifie si l'année a changé et si c'est le cas, on décal les années (N
	// devient N-1 et N-1 devient N-2)
	public void verifSiChangementAnnee() throws Exception {

		ParamsGenerauxServiceImpl paramsGenerauxService = new ParamsGenerauxServiceImpl();

		ArrayList<ParamsGenerauxBean> paramsGenerauxBeanList = (ArrayList<ParamsGenerauxBean>) paramsGenerauxService
				.getParamsGenerauxBeanListByIdEntreprise(idEntrepriseSelected);

		if (paramsGenerauxBeanList != null && paramsGenerauxBeanList.size() > 0) {
			ParamsGenerauxBean paramsGenerauxBean = paramsGenerauxBeanList
					.get(0);

			Date aujourdhui = new Date();
			int annee = 1900 + aujourdhui.getYear();

			if (paramsGenerauxBean.getAnnee() != null) {
				if (paramsGenerauxBean.getAnnee() < annee) {
					ParamsGenerauxBean newPG = new ParamsGenerauxBean();

					newPG.setAnnee(annee);

					newPG.setId(paramsGenerauxBean.getId());
					newPG.setIdEntreprise(paramsGenerauxBean.getIdEntreprise());

					newPG.setAgeLegalRetraiteAnN(null);
					newPG.setAnneeAnN("Année " + annee);
					newPG.setDureeTravailAnNHeuresRealiseesEffectifTot(null);
					newPG.setDureeTravailAnNHeuresSal(null);
					newPG.setDureeTravailAnNJoursEffectifTot(null);
					newPG.setDureeTravailAnNJoursSal(null);
					newPG.setEffectifEtpAnN(null);
					newPG.setEffectifMoyenAnN(null);
					newPG.setEffectifPhysiqueAnN(null);
					newPG.setMasseSalarialeAnN(null);
					newPG.setPourcentageFormationAnN(null);

					newPG.setAgeLegalRetraiteAnN1(paramsGenerauxBean
							.getAgeLegalRetraiteAnN());
					newPG.setAnneeAnN1("Année " + (annee - 1));
					newPG.setDureeTravailAnN1HeuresRealiseesEffectifTot(paramsGenerauxBean
							.getDureeTravailAnNHeuresRealiseesEffectifTot());
					newPG.setDureeTravailAnN1HeuresSal(paramsGenerauxBean
							.getDureeTravailAnNHeuresSal());
					newPG.setDureeTravailAnN1JoursEffectifTot(paramsGenerauxBean
							.getDureeTravailAnNJoursEffectifTot());
					newPG.setDureeTravailAnN1JoursSal(paramsGenerauxBean
							.getDureeTravailAnNJoursSal());
					newPG.setEffectifEtpAnN1(paramsGenerauxBean
							.getEffectifEtpAnN());
					newPG.setEffectifMoyenAnN1(paramsGenerauxBean
							.getEffectifMoyenAnN());
					newPG.setEffectifPhysiqueAnN1(paramsGenerauxBean
							.getEffectifPhysiqueAnN());
					newPG.setMasseSalarialeAnN1(paramsGenerauxBean
							.getMasseSalarialeAnN());
					newPG.setPourcentageFormationAnN1(paramsGenerauxBean
							.getPourcentageFormationAnN());

					newPG.setAgeLegalRetraiteAnN2(paramsGenerauxBean
							.getAgeLegalRetraiteAnN1());
					newPG.setAnneeAnN2("Année " + (annee - 2));
					newPG.setDureeTravailAnN2HeuresRealiseesEffectifTot(paramsGenerauxBean
							.getDureeTravailAnN1HeuresRealiseesEffectifTot());
					newPG.setDureeTravailAnN2HeuresSal(paramsGenerauxBean
							.getDureeTravailAnN1HeuresSal());
					newPG.setDureeTravailAnN2JoursEffectifTot(paramsGenerauxBean
							.getDureeTravailAnN1JoursEffectifTot());
					newPG.setDureeTravailAnN2JoursSal(paramsGenerauxBean
							.getDureeTravailAnN1JoursSal());
					newPG.setEffectifEtpAnN2(paramsGenerauxBean
							.getEffectifEtpAnN1());
					newPG.setEffectifMoyenAnN2(paramsGenerauxBean
							.getEffectifMoyenAnN1());
					newPG.setEffectifPhysiqueAnN2(paramsGenerauxBean
							.getEffectifPhysiqueAnN1());
					newPG.setMasseSalarialeAnN2(paramsGenerauxBean
							.getMasseSalarialeAnN1());
					newPG.setPourcentageFormationAnN2(paramsGenerauxBean
							.getPourcentageFormationAnN1());

					paramsGenerauxService.saveOrUppdate(newPG);
				}
			}
		}
	}

	public String getPourcentageFormationAnN() {
		return pourcentageFormationAnN;
	}

	public void setPourcentageFormationAnN(String pourcentageFormationAnN) {
		this.pourcentageFormationAnN = pourcentageFormationAnN;
	}

	public String getPourcentageFormationAnN1() {
		return pourcentageFormationAnN1;
	}

	public void setPourcentageFormationAnN1(String pourcentageFormationAnN1) {
		this.pourcentageFormationAnN1 = pourcentageFormationAnN1;
	}

	public String getPourcentageFormationAnN2() {
		return pourcentageFormationAnN2;
	}

	public void setPourcentageFormationAnN2(String pourcentageFormationAnN2) {
		this.pourcentageFormationAnN2 = pourcentageFormationAnN2;
	}

	public String getAnneeN() {
		Date aujourdhui = new Date();
		int annee = aujourdhui.getYear();
		return "Année " + (1900 + annee);
	}

	public void setAnneeN(String anneeN) {
		this.anneeN = anneeN;
	}

	public String getAnneeN1() {
		Date aujourdhui = new Date();
		int annee = aujourdhui.getYear();
		return "Année " + (1900 + (annee - 1));
	}

	public void setAnneeN1(String anneeN1) {
		this.anneeN1 = anneeN1;
	}

	public String getAnneeN2() {
		Date aujourdhui = new Date();
		int annee = aujourdhui.getYear();
		return "Année " + (1900 + (annee - 2));
	}

	public void setAnneeN2(String anneeN2) {
		this.anneeN2 = anneeN2;
	}

	public String getEffectifMoyenAnN() {
		return effectifMoyenAnN;
	}

	public void setEffectifMoyenAnN(String effectifMoyenAnN) {
		this.effectifMoyenAnN = effectifMoyenAnN;
	}

	public String getEffectifMoyenAnN1() {
		return effectifMoyenAnN1;
	}

	public void setEffectifMoyenAnN1(String effectifMoyenAnN1) {
		this.effectifMoyenAnN1 = effectifMoyenAnN1;
	}

	public String getEffectifMoyenAnN2() {
		return effectifMoyenAnN2;
	}

	public void setEffectifMoyenAnN2(String effectifMoyenAnN2) {
		this.effectifMoyenAnN2 = effectifMoyenAnN2;
	}

	public String getMasseSalarialeAnN() {
		return masseSalarialeAnN;
	}

	public void setMasseSalarialeAnN(String masseSalarialeAnN) {
		this.masseSalarialeAnN = masseSalarialeAnN;
	}

	public String getMasseSalarialeAnN1() {
		return masseSalarialeAnN1;
	}

	public void setMasseSalarialeAnN1(String masseSalarialeAnN1) {
		this.masseSalarialeAnN1 = masseSalarialeAnN1;
	}

	public String getMasseSalarialeAnN2() {
		return masseSalarialeAnN2;
	}

	public void setMasseSalarialeAnN2(String masseSalarialeAnN2) {
		this.masseSalarialeAnN2 = masseSalarialeAnN2;
	}

	public String getEffectifPhysiqueAnN() {
		return effectifPhysiqueAnN;
	}

	public void setEffectifPhysiqueAnN(String effectifPhysiqueAnN) {
		this.effectifPhysiqueAnN = effectifPhysiqueAnN;
	}

	public String getEffectifPhysiqueAnN1() {
		return effectifPhysiqueAnN1;
	}

	public void setEffectifPhysiqueAnN1(String effectifPhysiqueAnN1) {
		this.effectifPhysiqueAnN1 = effectifPhysiqueAnN1;
	}

	public String getEffectifPhysiqueAnN2() {
		return effectifPhysiqueAnN2;
	}

	public void setEffectifPhysiqueAnN2(String effectifPhysiqueAnN2) {
		this.effectifPhysiqueAnN2 = effectifPhysiqueAnN2;
	}

	public String getEffectifEtpAnN() {
		return effectifEtpAnN;
	}

	public void setEffectifEtpAnN(String effectifEtpAnN) {
		this.effectifEtpAnN = effectifEtpAnN;
	}

	public String getEffectifEtpAnN1() {
		return effectifEtpAnN1;
	}

	public void setEffectifEtpAnN1(String effectifEtpAnN1) {
		this.effectifEtpAnN1 = effectifEtpAnN1;
	}

	public String getEffectifEtpAnN2() {
		return effectifEtpAnN2;
	}

	public void setEffectifEtpAnN2(String effectifEtpAnN2) {
		this.effectifEtpAnN2 = effectifEtpAnN2;
	}

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	public boolean isAgeLegalRetraiteAnNError() {
		return ageLegalRetraiteAnNError;
	}

	public void setAgeLegalRetraiteAnNError(boolean ageLegalRetraiteAnNError) {
		this.ageLegalRetraiteAnNError = ageLegalRetraiteAnNError;
	}

	public boolean isAgeLegalRetraiteAnN1Error() {
		return ageLegalRetraiteAnN1Error;
	}

	public void setAgeLegalRetraiteAnN1Error(boolean ageLegalRetraiteAnN1Error) {
		this.ageLegalRetraiteAnN1Error = ageLegalRetraiteAnN1Error;
	}

	public boolean isAgeLegalRetraiteAnN2Error() {
		return ageLegalRetraiteAnN2Error;
	}

	public void setAgeLegalRetraiteAnN2Error(boolean ageLegalRetraiteAnN2Error) {
		this.ageLegalRetraiteAnN2Error = ageLegalRetraiteAnN2Error;
	}

	public boolean isEffectifMoyenAnNError() {
		return effectifMoyenAnNError;
	}

	public void setEffectifMoyenAnNError(boolean effectifMoyenAnNError) {
		this.effectifMoyenAnNError = effectifMoyenAnNError;
	}

	public boolean isEffectifMoyenAnN1Error() {
		return effectifMoyenAnN1Error;
	}

	public void setEffectifMoyenAnN1Error(boolean effectifMoyenAnN1Error) {
		this.effectifMoyenAnN1Error = effectifMoyenAnN1Error;
	}

	public boolean isEffectifMoyenAnN2Error() {
		return effectifMoyenAnN2Error;
	}

	public void setEffectifMoyenAnN2Error(boolean effectifMoyenAnN2Error) {
		this.effectifMoyenAnN2Error = effectifMoyenAnN2Error;
	}

	public boolean isMasseSalarialeAnNError() {
		return masseSalarialeAnNError;
	}

	public void setMasseSalarialeAnNError(boolean masseSalarialeAnNError) {
		this.masseSalarialeAnNError = masseSalarialeAnNError;
	}

	public boolean isMasseSalarialeAnN1Error() {
		return masseSalarialeAnN1Error;
	}

	public void setMasseSalarialeAnN1Error(boolean masseSalarialeAnN1Error) {
		this.masseSalarialeAnN1Error = masseSalarialeAnN1Error;
	}

	public boolean isMasseSalarialeAnN2Error() {
		return masseSalarialeAnN2Error;
	}

	public void setMasseSalarialeAnN2Error(boolean masseSalarialeAnN2Error) {
		this.masseSalarialeAnN2Error = masseSalarialeAnN2Error;
	}

	public boolean isPourcentageFormationAnNError() {
		return pourcentageFormationAnNError;
	}

	public void setPourcentageFormationAnNError(
			boolean pourcentageFormationAnNError) {
		this.pourcentageFormationAnNError = pourcentageFormationAnNError;
	}

	public boolean isPourcentageFormationAnN1Error() {
		return pourcentageFormationAnN1Error;
	}

	public void setPourcentageFormationAnN1Error(
			boolean pourcentageFormationAnN1Error) {
		this.pourcentageFormationAnN1Error = pourcentageFormationAnN1Error;
	}

	public boolean isPourcentageFormationAnN2Error() {
		return pourcentageFormationAnN2Error;
	}

	public void setPourcentageFormationAnN2Error(
			boolean pourcentageFormationAnN2Error) {
		this.pourcentageFormationAnN2Error = pourcentageFormationAnN2Error;
	}

	public boolean isDureeTravailAnNHeuresRealiseesEffectifTotError() {
		return dureeTravailAnNHeuresRealiseesEffectifTotError;
	}

	public void setDureeTravailAnNHeuresRealiseesEffectifTotError(
			boolean dureeTravailAnNHeuresRealiseesEffectifTotError) {
		this.dureeTravailAnNHeuresRealiseesEffectifTotError = dureeTravailAnNHeuresRealiseesEffectifTotError;
	}

	public boolean isDureeTravailAnN1HeuresRealiseesEffectifTotError() {
		return dureeTravailAnN1HeuresRealiseesEffectifTotError;
	}

	public void setDureeTravailAnN1HeuresRealiseesEffectifTotError(
			boolean dureeTravailAnN1HeuresRealiseesEffectifTotError) {
		this.dureeTravailAnN1HeuresRealiseesEffectifTotError = dureeTravailAnN1HeuresRealiseesEffectifTotError;
	}

	public boolean isDureeTravailAnN2HeuresRealiseesEffectifTotError() {
		return dureeTravailAnN2HeuresRealiseesEffectifTotError;
	}

	public void setDureeTravailAnN2HeuresRealiseesEffectifTotError(
			boolean dureeTravailAnN2HeuresRealiseesEffectifTotError) {
		this.dureeTravailAnN2HeuresRealiseesEffectifTotError = dureeTravailAnN2HeuresRealiseesEffectifTotError;
	}

	public boolean isDureeTravailAnNJoursEffectifTotError() {
		return dureeTravailAnNJoursEffectifTotError;
	}

	public void setDureeTravailAnNJoursEffectifTotError(
			boolean dureeTravailAnNJoursEffectifTotError) {
		this.dureeTravailAnNJoursEffectifTotError = dureeTravailAnNJoursEffectifTotError;
	}

	public boolean isDureeTravailAnN1JoursEffectifTotError() {
		return dureeTravailAnN1JoursEffectifTotError;
	}

	public void setDureeTravailAnN1JoursEffectifTotError(
			boolean dureeTravailAnN1JoursEffectifTotError) {
		this.dureeTravailAnN1JoursEffectifTotError = dureeTravailAnN1JoursEffectifTotError;
	}

	public boolean isDureeTravailAnN2JoursEffectifTotError() {
		return dureeTravailAnN2JoursEffectifTotError;
	}

	public void setDureeTravailAnN2JoursEffectifTotError(
			boolean dureeTravailAnN2JoursEffectifTotError) {
		this.dureeTravailAnN2JoursEffectifTotError = dureeTravailAnN2JoursEffectifTotError;
	}

	public boolean isDureeTravailAnNJoursSalError() {
		return dureeTravailAnNJoursSalError;
	}

	public void setDureeTravailAnNJoursSalError(
			boolean dureeTravailAnNJoursSalError) {
		this.dureeTravailAnNJoursSalError = dureeTravailAnNJoursSalError;
	}

	public boolean isDureeTravailAnN1JoursSalError() {
		return dureeTravailAnN1JoursSalError;
	}

	public void setDureeTravailAnN1JoursSalError(
			boolean dureeTravailAnN1JoursSalError) {
		this.dureeTravailAnN1JoursSalError = dureeTravailAnN1JoursSalError;
	}

	public boolean isDureeTravailAnN2JoursSalError() {
		return dureeTravailAnN2JoursSalError;
	}

	public void setDureeTravailAnN2JoursSalError(
			boolean dureeTravailAnN2JoursSalError) {
		this.dureeTravailAnN2JoursSalError = dureeTravailAnN2JoursSalError;
	}

	public boolean isDureeTravailAnNHeuresSalError() {
		return dureeTravailAnNHeuresSalError;
	}

	public void setDureeTravailAnNHeuresSalError(
			boolean dureeTravailAnNHeuresSalError) {
		this.dureeTravailAnNHeuresSalError = dureeTravailAnNHeuresSalError;
	}

	public boolean isDureeTravailAnN1HeuresSalError() {
		return dureeTravailAnN1HeuresSalError;
	}

	public void setDureeTravailAnN1HeuresSalError(
			boolean dureeTravailAnN1HeuresSalError) {
		this.dureeTravailAnN1HeuresSalError = dureeTravailAnN1HeuresSalError;
	}

	public boolean isDureeTravailAnN2HeuresSalError() {
		return dureeTravailAnN2HeuresSalError;
	}

	public void setDureeTravailAnN2HeuresSalError(
			boolean dureeTravailAnN2HeuresSalError) {
		this.dureeTravailAnN2HeuresSalError = dureeTravailAnN2HeuresSalError;
	}

	public boolean isEffectifPhysiqueAnNError() {
		return effectifPhysiqueAnNError;
	}

	public void setEffectifPhysiqueAnNError(boolean effectifPhysiqueAnNError) {
		this.effectifPhysiqueAnNError = effectifPhysiqueAnNError;
	}

	public boolean isEffectifPhysiqueAnN1Error() {
		return effectifPhysiqueAnN1Error;
	}

	public void setEffectifPhysiqueAnN1Error(boolean effectifPhysiqueAnN1Error) {
		this.effectifPhysiqueAnN1Error = effectifPhysiqueAnN1Error;
	}

	public boolean isEffectifPhysiqueAnN2Error() {
		return effectifPhysiqueAnN2Error;
	}

	public void setEffectifPhysiqueAnN2Error(boolean effectifPhysiqueAnN2Error) {
		this.effectifPhysiqueAnN2Error = effectifPhysiqueAnN2Error;
	}

	public boolean isEffectifEtpAnNError() {
		return effectifEtpAnNError;
	}

	public void setEffectifEtpAnNError(boolean effectifEtpAnNError) {
		this.effectifEtpAnNError = effectifEtpAnNError;
	}

	public boolean isEffectifEtpAnN1Error() {
		return effectifEtpAnN1Error;
	}

	public void setEffectifEtpAnN1Error(boolean effectifEtpAnN1Error) {
		this.effectifEtpAnN1Error = effectifEtpAnN1Error;
	}

	public boolean isEffectifEtpAnN2Error() {
		return effectifEtpAnN2Error;
	}

	public void setEffectifEtpAnN2Error(boolean effectifEtpAnN2Error) {
		this.effectifEtpAnN2Error = effectifEtpAnN2Error;
	}

	public void setCanSave(boolean canSave) {
		this.canSave = canSave;
	}

}
