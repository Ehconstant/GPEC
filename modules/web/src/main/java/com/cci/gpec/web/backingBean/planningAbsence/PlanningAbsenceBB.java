package com.cci.gpec.web.backingBean.planningAbsence;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.ServiceBean;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.cci.gpec.primefaces.component.schedule.ScheduleController;
import com.icesoft.faces.context.ByteArrayResource;

public class PlanningAbsenceBB {

	private ArrayList<SelectItem> entrepriseList;
	protected int idEntrepriseSelected;
	private ArrayList<SelectItem> metierList = new ArrayList<SelectItem>();
	protected int idMetierSelected;
	private ByteArrayResource imagen = new ByteArrayResource(null);

	protected int idServiceSelected;
	private ArrayList<SelectItem> servicesList;

	private ArrayList<SelectItem> monthList;
	private int idMonthSelected;
	private ArrayList<SelectItem> yearList;
	private int idYearSelected;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public PlanningAbsenceBB() throws Exception {
		init();
	}

	public void init() throws Exception {
		SelectItem selectItem;
		SimpleDateFormat formaterDate = new SimpleDateFormat("MMMMMMMMMMMMMMMM");
		SimpleDateFormat formaterDateAnnee = new SimpleDateFormat("yyyy");

		// init monthList
		monthList = new ArrayList<SelectItem>();
		for (int i = 0; i <= 11; i++) {
			selectItem = new SelectItem();
			selectItem.setValue(i);
			selectItem.setLabel(formaterDate.format(date(1, i, 2010)));
			monthList.add(selectItem);
		}

		// init yearList
		yearList = new ArrayList<SelectItem>();
		Date dateToday = new Date();
		GregorianCalendar calendarToday = new GregorianCalendar();
		calendarToday.setTime(dateToday);
		int year = calendarToday.get(Calendar.YEAR);
		for (int i = year - 5; i <= year + 2; i++) {
			selectItem = new SelectItem();
			selectItem.setValue(i);
			selectItem.setLabel(formaterDateAnnee.format(date(1, 1, i)));
			yearList.add(selectItem);
		}

		// init entreprises
		EntrepriseServiceImpl entrepriseService = new EntrepriseServiceImpl();

		List<EntrepriseBean> entrepriseBeanList = entrepriseService
				.getEntreprisesList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));
		entrepriseList = new ArrayList<SelectItem>();

		for (EntrepriseBean entrepriseBean : entrepriseBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(entrepriseBean.getId());
			selectItem.setLabel(entrepriseBean.getNom());
			entrepriseList.add(selectItem);
		}

		this.idMetierSelected = -1;
		this.idServiceSelected = -1;
		this.idMonthSelected = -1;
		this.idYearSelected = -1;

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("entreprise") != null) {
			this.idEntrepriseSelected = Integer.parseInt(session.getAttribute(
					"entreprise").toString());
		} else
			this.idEntrepriseSelected = -1;

		// TODO : Déplacer dans selectOneMenuChange pour eviter la génération
		// d'un faux graphe à la selection de l'entreprise
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		imagen = new ByteArrayResource(baos.toByteArray());
	}

	public void selectOneMenuChange(ValueChangeEvent event) throws Exception {
		if (event.getComponent().getId().equals("entrepriseListPlanningA")) {
			idEntrepriseSelected = Integer.parseInt(event.getNewValue()
					.toString());
			idServiceSelected = -1;
			idMetierSelected = -1;
			fillServicesList();
			metierList.clear();
		} else {
			if (event.getComponent().getId().equals("idServiceListPlanningA")) {
				idServiceSelected = Integer.parseInt(event.getNewValue()
						.toString());
				idMetierSelected = -1;
				fillMetierList();
			} else {
				if (event.getComponent().getId().equals("metierListPlanningA")) {
					idMetierSelected = Integer.parseInt(event.getNewValue()
							.toString());
				}
			}
		}
	}

	public void generatePlanning(ActionEvent event) {
		createPlanningAbsences();
		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}
	}

	public int getIdEntrepriseSelected() {
		return idEntrepriseSelected;
	}

	public void setIdEntrepriseSelected(int idEntrepriseSelected) {
		this.idEntrepriseSelected = idEntrepriseSelected;
	}

	public int getIdMetierSelected() {
		return idMetierSelected;
	}

	public void setIdMetierSelected(int idMetierSelected) {
		this.idMetierSelected = idMetierSelected;
	}

	public int getIdServiceSelected() {
		return idServiceSelected;
	}

	public void setIdServiceSelected(int idServiceSelected) {
		this.idServiceSelected = idServiceSelected;
	}

	public ArrayList<SelectItem> getEntrepriseList() {
		return entrepriseList;
	}

	public ArrayList<SelectItem> fillMetierList() throws Exception {
		SalarieServiceImpl salarieService = new SalarieServiceImpl();
		metierList = new ArrayList<SelectItem>();
		SelectItem selectItem;
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (SalarieBean salarieBean : salarieService.getSalariesList(Integer
				.parseInt(session.getAttribute("groupe").toString()))) {

			if (salarieBean.getIdEntrepriseSelected() == this.idEntrepriseSelected
					&& salarieBean.getIdServiceSelected() == this.idServiceSelected) {
				ParcoursBean parcourBean = getLastParcours(salarieBean);
				if (parcourBean != null) {
					if (!list.contains(parcourBean.getIdMetierSelected())) {
						selectItem = new SelectItem();
						selectItem.setValue(parcourBean.getIdMetierSelected());
						selectItem.setLabel(parcourBean.getNomMetier());
						list.add(parcourBean.getIdMetierSelected());
						metierList.add(selectItem);
					}
				}
			}
		}
		return metierList;
	}

	public ArrayList<SelectItem> getMetierList() throws Exception {
		return metierList;
	}

	private ParcoursBean getLastParcours(SalarieBean salarie) {
		List<ParcoursBean> parcourList = salarie.getParcoursBeanList();

		ParcoursBean parcoursBean = null;
		if (parcourList != null && parcourList.size() > 0) {
			Collections.sort(parcourList);
			Collections.reverse(parcourList);
			parcoursBean = parcourList.get(0);
		}

		return parcoursBean;
	}

	public ArrayList<SelectItem> fillServicesList() {
		servicesList = new ArrayList<SelectItem>();
		if (idEntrepriseSelected > 0) {
			ServiceImpl service = new ServiceImpl();

			ArrayList<ServiceBean> serviceBeanList;
			try {
				serviceBeanList = (ArrayList<ServiceBean>) service
						.getServicesList(Integer.parseInt(session.getAttribute(
								"groupe").toString()));

				for (ServiceBean serviceBean : serviceBeanList) {
					if (idEntrepriseSelected == serviceBean.getIdEntreprise()) {

						servicesList.add(new SelectItem(serviceBean.getId(),
								serviceBean.getNom()));
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return servicesList;
	}

	public ArrayList<SelectItem> getServicesList() {
		return servicesList;
	}

	public void createPlanningAbsences() {
		try {
			Integer[] idSelected = new Integer[3];
			idSelected[0] = this.idEntrepriseSelected;
			idSelected[1] = this.idServiceSelected;
			idSelected[2] = this.idMetierSelected;
			ScheduleController controller = new ScheduleController();
			imagen = new ByteArrayResource(controller.createGantChart(
					"Planning des absences", idSelected, this.idMonthSelected,
					this.idYearSelected));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ByteArrayResource getImagen() throws Exception {
		return imagen;
	}

	public int getIdMonthSelected() {
		return idMonthSelected;
	}

	public void setIdMonthSelected(int idMonthSelected) {
		this.idMonthSelected = idMonthSelected;
	}

	public ArrayList<SelectItem> getMonthList() {
		return monthList;
	}

	public int getIdYearSelected() {
		return idYearSelected;
	}

	public void setIdYearSelected(int idYearSelected) {
		this.idYearSelected = idYearSelected;
	}

	public ArrayList<SelectItem> getYearList() {
		return yearList;
	}

	private static Date date(final int day, final int month, final int year) {

		final Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		final Date result = calendar.getTime();
		return result;

	}
}
