package com.cci.gpec.primefaces.component.schedule;

import java.util.ArrayList;
import java.util.Date;

import com.cci.gpec.commons.AbsenceBean;
import com.cci.gpec.commons.AccidentBean;
import com.cci.gpec.commons.EntretienBean;
import com.cci.gpec.commons.FormationBean;
import com.cci.gpec.commons.HabilitationBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;

public class InsertSalarieBean {

	public void insertSalarieBean() {
		SalarieServiceImpl salarieService = new SalarieServiceImpl();

		int index = 100;
		while (index < 300) {
			SalarieBean salarieBean = new SalarieBean();
			salarieBean.setAdresse("adresse");
			salarieBean.setCivilite("Monsieur");
			salarieBean.setCreditDif(10.0);
			salarieBean.setDateNaissance(new Date());
			salarieBean.setIdEntrepriseSelected(3);
			salarieBean.setIdLienSubordination(7);
			salarieBean.setIdServiceSelected(3);
			salarieBean.setNivFormationAtteint("VB");
			salarieBean.setNivFormationInit("VB");
			salarieBean.setCv("");
			salarieBean.setLieuNaissance("");
			salarieBean.setNom("Nom" + index);
			salarieBean.setPrenom("Prenom" + index);
			salarieBean.setTelephone("12");
			salarieBean.setPresent(true);

			salarieBean.setAbsenceBeanList(new ArrayList<AbsenceBean>());
			salarieBean.setAccidentBeanList(new ArrayList<AccidentBean>());
			salarieBean.setEntretienBeanList(new ArrayList<EntretienBean>());
			salarieBean.setFormationBeanList(new ArrayList<FormationBean>());
			salarieBean
					.setHabilitationBeanList(new ArrayList<HabilitationBean>());
			salarieBean.setParcoursBeanList(new ArrayList<ParcoursBean>());

			salarieService.saveOrUppdate(salarieBean);
			index++;
		}
	}
}
