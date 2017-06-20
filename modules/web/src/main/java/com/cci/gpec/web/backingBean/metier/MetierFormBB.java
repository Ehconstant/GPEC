package com.cci.gpec.web.backingBean.metier;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.DifficulteBean;
import com.cci.gpec.commons.MetierBean;
import com.cci.gpec.metier.implementation.MetierServiceImpl;
import com.cci.gpec.web.backingBean.BackingBeanForm;
import com.icesoft.faces.component.ext.HtmlDataTable;

public class MetierFormBB extends BackingBeanForm {

	private int idFamilleMetier;
	private String nomFamilleMetier;

	private List<DifficulteBean> difficulteList;
	private Integer idDifficulteSelected;

	private boolean difficulteRencontre = false;

	private List<SelectItem> selectItemsDifficulte;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public MetierFormBB() {
		super();
	}

	public MetierFormBB(int id, String nom) {
		super(id, nom);
	}

	public String init() {
		super.id = 0;
		super.nom = "";
		difficulteList = new ArrayList<DifficulteBean>();
		if (idFamilleMetier != 0) {
			idFamilleMetier = 0;
			nomFamilleMetier = "";
		}

		SelectItem niveau0 = new SelectItem(0, "Aucune");
		SelectItem niveau1 = new SelectItem(1, "DIFFICULTES LIEES AU CANDIDAT");
		niveau1.setDisabled(true);
		SelectItem niveau2 = new SelectItem(8, "DIFFICULTES LIEES AU METIER");
		niveau2.setDisabled(true);
		SelectItem niveau3 = new SelectItem(15, "DIFFICULTES AUTRES");
		niveau3.setDisabled(true);
		selectItemsDifficulte = new ArrayList<SelectItem>();

		selectItemsDifficulte.add(niveau0);
		selectItemsDifficulte.add(niveau1);
		selectItemsDifficulte.add(new SelectItem(2,
				"  - Expérience professionnelle insuffisante"));
		selectItemsDifficulte.add(new SelectItem(3,
				"  - Niveau de formation insuffisant"));
		selectItemsDifficulte.add(new SelectItem(4,
				"  - Niveau de qualification insuffisant"));
		selectItemsDifficulte
				.add(new SelectItem(5, "  - Manque de motivation"));
		selectItemsDifficulte.add(new SelectItem(6, "  - Pénurie de candidat"));
		selectItemsDifficulte.add(new SelectItem(7,
				"  - Savoir-être comportemental"));
		selectItemsDifficulte.add(niveau2);
		selectItemsDifficulte.add(new SelectItem(9,
				"  - Attractivité du métier"));
		selectItemsDifficulte.add(new SelectItem(10,
				"  - Contraintes du métier"));
		selectItemsDifficulte.add(new SelectItem(11,
				"  - Inadéquation emploi/formation"));
		selectItemsDifficulte.add(new SelectItem(12,
				"  - Métier en évolution technologique"));
		selectItemsDifficulte
				.add(new SelectItem(13, "  - Pénibilité du métier"));
		selectItemsDifficulte.add(new SelectItem(14,
				"  - Spécificité du métier"));
		selectItemsDifficulte.add(niveau3);
		selectItemsDifficulte.add(new SelectItem(16,
				"  - Conditions de travail"));
		selectItemsDifficulte.add(new SelectItem(17,
				"  - Nature du contrat de travail"));
		selectItemsDifficulte.add(new SelectItem(18,
				"  - Niveau de rémunération"));

		return "addMetierForm";
	}

	public void supprimerMetier(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		MetierBean metierBean = (MetierBean) table.getRowData();

		MetierServiceImpl metierService = new MetierServiceImpl();
		metierService.suppression(metierBean);

	}

	public void add(ValueChangeEvent evt) {
		PhaseId phaseId = evt.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			evt.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			evt.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			idDifficulteSelected = Integer.parseInt(evt.getNewValue()
					.toString());
			String categorie = "";
			if (idDifficulteSelected > 0) {
				if (idDifficulteSelected <= 7) {
					categorie = getSelectItemsDifficulte().get(1).getLabel();
				}
				if (idDifficulteSelected > 8 && idDifficulteSelected <= 14) {
					categorie = getSelectItemsDifficulte().get(8).getLabel();
				}
				if (idDifficulteSelected > 15 && idDifficulteSelected <= 18) {
					categorie = getSelectItemsDifficulte().get(15).getLabel();
				}

				DifficulteBean d = new DifficulteBean(idDifficulteSelected,
						categorie, getSelectItemsDifficulte().get(
								idDifficulteSelected).getLabel());

				difficulteList.add(d);
				getSelectItemsDifficulte().get(idDifficulteSelected)
						.setDisabled(true);
			} else {
				if (idDifficulteSelected == 0) {
					difficulteList.clear();
					for (SelectItem s : getSelectItemsDifficulte()) {
						if (Integer.valueOf(s.getValue().toString()) != 1
								&& Integer.valueOf(s.getValue().toString()) != 8
								&& Integer.valueOf(s.getValue().toString()) != 15) {
							s.setDisabled(false);
						}
					}
				}
			}
			if (idDifficulteSelected != 0)
				idDifficulteSelected = -1;
		}

	}

	public void modifier(ActionEvent evt) {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		MetierBean metierBean = (MetierBean) table.getRowData();

		init();

		this.id = metierBean.getId();
		this.nom = metierBean.getNom();
		this.idFamilleMetier = metierBean.getIdFamilleMetier();
		this.nomFamilleMetier = metierBean.getNomFamilleMetier();
		difficulteList = new ArrayList<DifficulteBean>();

		if (metierBean.getDifficultes() != null)
			if (!metierBean.getDifficultes().equals("")
					&& !metierBean.getDifficultes().equals(";"))
				for (String s : metierBean.getDifficultes().split(";")) {
					Integer idDifficulte = Integer.valueOf(s);
					String categorie = "";
					if (idDifficulte > 0) {
						if (idDifficulte <= 7) {
							categorie = getSelectItemsDifficulte().get(1)
									.getLabel();
						}
						if (idDifficulte > 8 && idDifficulte <= 14) {
							categorie = getSelectItemsDifficulte().get(8)
									.getLabel();
						}
						if (idDifficulte > 15 && idDifficulte <= 18) {
							categorie = getSelectItemsDifficulte().get(15)
									.getLabel();
						}
					}

					DifficulteBean d = new DifficulteBean(idDifficulte,
							categorie, getSelectItemsDifficulte().get(
									idDifficulte).getLabel());

					difficulteList.add(d);

					getSelectItemsDifficulte().get(idDifficulte).setDisabled(
							true);
				}
	}

	public String formLink() {
		return "addMetierForm";
	}

	public String remove(ActionEvent evt) {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		DifficulteBean difficulteBean = (DifficulteBean) table.getRowData();

		difficulteList.remove(difficulteBean);

		getSelectItemsDifficulte().get(difficulteBean.getId()).setDisabled(
				false);

		return "addMetierForm";
	}

	public List<SelectItem> getSelectItemsDifficulte() {

		return selectItemsDifficulte;
	}

	private HtmlDataTable getParentDatatable(UIComponent compo) {
		if (compo == null) {
			return null;
		}
		if (compo instanceof HtmlDataTable) {
			return (HtmlDataTable) compo;
		}
		return getParentDatatable(compo.getParent());
	}

	public String saveOrUpdateMetierFin() {

		MetierServiceImpl metierService = new MetierServiceImpl();
		MetierBean metierBean = new MetierBean();
		metierBean.setId(super.id);
		metierBean.setNom(super.nom);
		if (difficulteList.size() > 0) {
			String difficultes = "";
			for (DifficulteBean d : difficulteList) {
				difficultes = difficultes + d.getId() + ";";
			}
			metierBean.setDifficultes(difficultes);
		}
		metierBean.setIdFamilleMetier(idFamilleMetier);
		metierService.saveOrUppdate(metierBean,
				Integer.parseInt(session.getAttribute("groupe").toString()));

		return "saveOrUpdateMetier";
	}

	public String annuler() {
		return "saveOrUpdateMetier";
	}

	public int getIdFamilleMetier() {
		return idFamilleMetier;
	}

	public void setIdFamilleMetier(int idFamilleMetier) {
		this.idFamilleMetier = idFamilleMetier;
	}

	public String getNomFamilleMetier() {
		return nomFamilleMetier;
	}

	public void setNomFamilleMetier(String nomFamilleMetier) {
		this.nomFamilleMetier = nomFamilleMetier;
	}

	public boolean isDifficulteRencontre() {
		return (!difficulteList.isEmpty());
	}

	public void setDifficulteRencontre(boolean difficulteRencontre) {
		this.difficulteRencontre = difficulteRencontre;
	}

	public List<DifficulteBean> getDifficulteList() {
		return difficulteList;
	}

	public void setDifficulteList(List<DifficulteBean> difficulteList) {
		this.difficulteList = difficulteList;
	}

	public Integer getIdDifficulteSelected() {
		return idDifficulteSelected;
	}

	public void setIdDifficulteSelected(Integer idDifficulteSelected) {
		this.idDifficulteSelected = idDifficulteSelected;
	}

	public void setSelectItemsDifficulte(List<SelectItem> selectItemsDifficulte) {
		this.selectItemsDifficulte = selectItemsDifficulte;
	}

}
