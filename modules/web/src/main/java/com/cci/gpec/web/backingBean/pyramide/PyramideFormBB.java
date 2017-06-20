package com.cci.gpec.web.backingBean.pyramide;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.GroupeBean;
import com.cci.gpec.commons.MetierBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.ServiceBean;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.GroupeServiceImpl;
import com.cci.gpec.metier.implementation.MetierServiceImpl;
import com.cci.gpec.metier.implementation.PyramideServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.icesoft.faces.context.ByteArrayResource;

public class PyramideFormBB {

	private ArrayList<SelectItem> entrepriseList;
	protected int idEntrepriseSelected;
	private ArrayList<SelectItem> metierList;
	protected int idMetierSelected;
	private ByteArrayResource imagen = new ByteArrayResource(null);

	protected int idServiceSelected;
	private ArrayList<SelectItem> servicesList;

	private int projection;

	private List<MetierBean> metierBeanListFact = new ArrayList<MetierBean>();
	private boolean initList = false;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public PyramideFormBB() throws Exception {
		init();
	}

	private ParcoursBean getLastParcours(SalarieBean salarie) {
		List<ParcoursBean> parcourList = salarie.getParcoursBeanList();
		ParcoursBean pb = null;
		for (int i = 0; i < parcourList.size(); i++) {
			ParcoursBean parcour = parcourList.get(i);
			if (pb == null)
				pb = parcour;
			if (parcour.getDebutFonction().after(pb.getDebutFonction())) {
				pb = parcour;
			}
		}
		return pb;
	}

	private boolean contain(int idMet, List<MetierBean> l) {
		for (MetierBean met : l) {
			if (met.getId() == idMet)
				return true;
		}
		return false;
	}

	public MetierBean getMetierById(int id) {
		MetierBean result = new MetierBean();
		for (MetierBean m : metierBeanListFact) {
			if (m.getId() == id)
				result = m;
		}
		return result;
	}

	private List<MetierBean> getMetiersList() throws Exception {
		List<MetierBean> res = new ArrayList<MetierBean>();
		SalarieServiceImpl serv = new SalarieServiceImpl();
		MetierServiceImpl metServ = new MetierServiceImpl();

		if (initList == false) {
			initList = true;
			List<MetierBean> l = new ArrayList<MetierBean>();
			l = metServ.getMetiersList(Integer.parseInt(session.getAttribute(
					"groupe").toString()));
			for (MetierBean s : l) {
				metierBeanListFact.add(s);
			}
		}

		List<SalarieBean> list = serv.getSalariesList(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		for (SalarieBean salarie : list) {
			if ((salarie.getIdEntrepriseSelected() == idEntrepriseSelected)
					&& (salarie.getIdServiceSelected() == idServiceSelected)) {
				ParcoursBean p = getLastParcours(salarie);
				if (p != null) {
					MetierBean metier = getMetierById(p.getIdMetierSelected());
					// metServ.getMetierBeanById(p
					// .getIdMetierSelected());
					if (!contain(metier.getId(), res)) {
						res.add(metier);
					}
				}
			}

		}
		return res;
	}

	public void init() throws Exception {
		// init entreprises
		EntrepriseServiceImpl entrepriseService = new EntrepriseServiceImpl();

		List<EntrepriseBean> entrepriseBeanList = entrepriseService
				.getEntreprisesList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));
		entrepriseList = new ArrayList<SelectItem>();
		SelectItem selectItem;
		GroupeServiceImpl grServ = new GroupeServiceImpl();
		GroupeBean groupe = grServ.getGroupeBeanById(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		// if (list.size() > 0) {
		selectItem = new SelectItem();
		selectItem.setValue(-1);
		selectItem.setLabel(groupe.getNom());
		entrepriseList.add(selectItem);
		// } else {
		// selectItem = new SelectItem();
		// selectItem.setValue(-1);
		// selectItem.setLabel("Groupe");
		// entrepriseList.add(selectItem);
		// }
		for (EntrepriseBean entrepriseBean : entrepriseBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(entrepriseBean.getId());
			selectItem.setLabel(entrepriseBean.getNom());
			entrepriseList.add(selectItem);
		}

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("entreprise") != null) {
			this.idEntrepriseSelected = Integer.parseInt(session.getAttribute(
					"entreprise").toString());
		} else
			this.idEntrepriseSelected = -1;

		this.idMetierSelected = -1;
		this.idServiceSelected = -1;
		this.projection = 0;

		// create pyramide
		PyramideServiceImpl pyramideService = new PyramideServiceImpl();
		imagen = new ByteArrayResource(pyramideService.creerPyramide("Age",
				null, null, null, this.projection,
				Integer.parseInt(session.getAttribute("groupe").toString())));

		imagen = new ByteArrayResource(null);
		setImagen(imagen);
	}

	public ByteArrayResource getImagen() {
		imagen = new ByteArrayResource(null);
		try {
			createImagePyramide();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return imagen;
	}

	public void recalculePyramide(ValueChangeEvent event) throws Exception {

		if (event.getComponent().getId().equals("entrepriseListPyramide")) {
			idEntrepriseSelected = Integer.parseInt(event.getNewValue()
					.toString());
			idServiceSelected = -1;
			idMetierSelected = -1;
			this.metierList = new ArrayList<SelectItem>();
		} else {
			if (event.getComponent().getId().equals("idServiceListPyramide")) {
				idServiceSelected = Integer.parseInt(event.getNewValue()
						.toString());
				idMetierSelected = -1;
				if (idServiceSelected == -1)
					this.metierList = new ArrayList<SelectItem>();
			} else {
				if (event.getComponent().getId().equals("metierListPyramide")) {
					idMetierSelected = Integer.parseInt(event.getNewValue()
							.toString());
				}
			}
		}

		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}
	}

	protected void createImagePyramide() throws Exception {
		Integer idEntreprise = null;
		if (getIdEntrepriseSelected() != -1) {
			idEntreprise = getIdEntrepriseSelected();
		}

		Integer idService = null;
		if (getIdServiceSelected() != -1) {
			idService = getIdServiceSelected();
		}

		Integer idMetier = null;
		if (getIdMetierSelected() != -1) {
			idMetier = getIdMetierSelected();
		}

		// create pyramide
		PyramideServiceImpl pyramideService = new PyramideServiceImpl();
		imagen = new ByteArrayResource(pyramideService.creerPyramide("Age",
				idEntreprise, idService, idMetier, this.projection,
				Integer.parseInt(session.getAttribute("groupe").toString())));

	}

	public void setImagen(ByteArrayResource imagen) {
		this.imagen = imagen;
	}

	public ArrayList<SelectItem> getEntrepriseList() {
		return entrepriseList;
	}

	public int getIdEntrepriseSelected() {
		if (this.idEntrepriseSelected == -1) {
			this.idMetierSelected = -1;
			this.idServiceSelected = -1;
		}
		return idEntrepriseSelected;
	}

	public void setIdEntrepriseSelected(int idEntrepriseSelected) {
		this.idEntrepriseSelected = idEntrepriseSelected;
	}

	public ArrayList<SelectItem> getMetierList() throws Exception {
		if (this.idEntrepriseSelected > 0) {
			if (this.idServiceSelected > 0) {
				List<MetierBean> metierBeanList = this.getMetiersList();
				SelectItem selectItem;
				metierList = new ArrayList<SelectItem>();
				for (MetierBean metierBean : metierBeanList) {
					selectItem = new SelectItem();
					selectItem.setValue(metierBean.getId());
					selectItem.setLabel(metierBean.getNom());
					metierList.add(selectItem);
				}
			}
		}
		return metierList;
	}

	public int getIdMetierSelected() {
		return idMetierSelected;
	}

	public void setIdMetierSelected(int idMetierSelected) {
		this.idMetierSelected = idMetierSelected;
	}

	public int getIdServiceSelected() {
		if (this.idServiceSelected == -1) {
			this.idMetierSelected = -1;
		}
		return idServiceSelected;
	}

	public void setIdServiceSelected(int idServiceSelected) {
		this.idServiceSelected = idServiceSelected;
	}

	public ArrayList<SelectItem> getServicesList() {

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

	public int getProjection() {
		return projection;
	}

	public void setProjection(int projection) {
		this.projection = projection;
	}

}
