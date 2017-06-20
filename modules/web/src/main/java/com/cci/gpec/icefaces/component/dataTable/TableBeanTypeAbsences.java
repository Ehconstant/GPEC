package com.cci.gpec.icefaces.component.dataTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.TypeAbsenceBean;
import com.cci.gpec.metier.implementation.TypeAbsenceServiceImpl;
import com.cci.gpec.web.backingBean.typeabsence.TypeAbsenceFormBB;

public class TableBeanTypeAbsences {

	private List<TypeAbsenceBean> typeAbsenceInventory = new ArrayList<TypeAbsenceBean>();

	private int id;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public TableBeanTypeAbsences() throws Exception {
		init();
	}

	public List<TypeAbsenceBean> getTypeAbsenceInventory() throws Exception {
		init();
		return typeAbsenceInventory;
	}

	public void setTypeAbsenceInventory(
			List<TypeAbsenceBean> typeAbsenceInventory) {
		this.typeAbsenceInventory = typeAbsenceInventory;
	}

	public void init() throws Exception {
		TypeAbsenceServiceImpl typeAbsence = new TypeAbsenceServiceImpl();
		typeAbsenceInventory = typeAbsence.getTypeAbsenceList(Integer
				.parseInt(session.getAttribute("groupe").toString()));
		Collections.sort(typeAbsenceInventory);

		int i = 0;
		List<TypeAbsenceBean> typeAbsenceInventoryTmp = new ArrayList<TypeAbsenceBean>();
		for (TypeAbsenceBean typeAbsenceBean : typeAbsenceInventory) {
			typeAbsenceInventoryTmp.add(typeAbsenceBean);
		}
		for (TypeAbsenceBean typeAbsenceBean : typeAbsenceInventory) {
			if (typeAbsenceBean.getNom().startsWith("Autre")) {
				typeAbsenceInventoryTmp.remove(typeAbsenceBean);
				typeAbsenceInventoryTmp.add(typeAbsenceBean);
				i++;
				if (i == 2) {
					break;
				}
			}
		}
		typeAbsenceInventory = typeAbsenceInventoryTmp;
	}

	public String modifierService() throws Exception {
		TypeAbsenceServiceImpl typeAbsence = new TypeAbsenceServiceImpl();
		TypeAbsenceBean typeAbsenceBean = typeAbsence
				.getTypeAbsenceBeanById(this.id);
		TypeAbsenceFormBB typeAbsenceFormBB = new TypeAbsenceFormBB();
		typeAbsenceFormBB.setId(typeAbsenceBean.getId());
		typeAbsenceFormBB.setNom(typeAbsenceBean.getNom());

		return "addTypeAbsenceForm";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
