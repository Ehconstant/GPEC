package com.cci.gpec.web.backingBean.projectionps;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.GroupeBean;
import com.cci.gpec.commons.MetierBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.ProjectionPSBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.SalarieBeanExport;
import com.cci.gpec.commons.ServiceBean;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.GroupeServiceImpl;
import com.cci.gpec.metier.implementation.MetierServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.cci.gpec.web.backingBean.excel.ExcelBB;
import com.icesoft.faces.context.effects.JavascriptContext;

public class ProjectionPSBB {

	// Les id mise à jour lors des selections
	private int idService = -1;
	private int idMetier = -1;
	private int idEntreprise = -1;
	private int totalTranche1;
	private int totalTranche2;
	private int totalTranche3;
	private int tauxTotal;
	private ArrayList<SelectItem> entreprisesListItem;
	private ArrayList<SelectItem> servicesListItem;
	private ArrayList<SelectItem> metiersListItem;
	protected ArrayList<ProjectionPSBean> listBean;
	private ArrayList<SalarieBean> listSalarie;
	private boolean init = false;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public void change(ValueChangeEvent event) throws Exception {

		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}

		if (event.getComponent().getId().equals("selectEntreprise")) {
			idEntreprise = Integer.parseInt(event.getNewValue().toString());
			idService = -1;
			idMetier = -1;
			initialiserEntreprisesList2();
			if ((idEntreprise != -1) && (servicesListItem.size() == 0)) {
				ResourceBundle bundle = ResourceBundle.getBundle("errors");
				FacesMessage message = new FacesMessage(
						bundle.getString("AfficheSansService"));
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				FacesContext.getCurrentInstance().addMessage(
						"idForm:idDataEntreprise1", message);
			}
			metiersListItem = new ArrayList<SelectItem>();
		} else {
			if (event.getComponent().getId().equals("selectService")) {
				idService = Integer.parseInt(event.getNewValue().toString());
				idMetier = -1;
				if (idService != -1) {
					initialiserEntreprisesList2();
				} else {
					initialiserEntreprisesList2();
					metiersListItem = new ArrayList<SelectItem>();
				}
				if ((idService != -1) && (metiersListItem.size() == 0)) {
					ResourceBundle bundle = ResourceBundle.getBundle("errors");
					FacesMessage message = new FacesMessage(
							bundle.getString("AfficheSansMetier"));
					message.setSeverity(FacesMessage.SEVERITY_INFO);
					FacesContext.getCurrentInstance().addMessage(
							"idForm:idDataService", message);
				}
			} else {
				idMetier = Integer.parseInt(event.getNewValue().toString());
				if (idMetier == -1) {
					initialiserEntreprisesList2();
				} else {
					initialiserEntreprisesList2();
				}
			}
		}

	}

	private int getTauxEntreprise(EntrepriseBean bean) throws Exception {
		double total = (double) this.nbEmployeByEntreprise(bean);
		double plus50ans = (double) getTrancheEntreprise(50, 900, bean);
		if (total == 0) {
			return 0;
		}
		int d = (int) ((plus50ans * 100) / total);
		return d;
	}

	private int getTauxService(ServiceBean bean) throws Exception {
		double total = (double) nbEmployeByService(bean);
		double plus50ans = (double) getTrancheService(50, 900, bean);
		if (total == 0) {
			return 0;
		}
		int d = (int) ((plus50ans * 100) / total);
		return d;
	}

	private int getTauxMetier(MetierBean bean) throws Exception {
		double total = (double) this.nbEmployeByMetier(bean);
		double plus50ans = (double) getTrancheMetier(50, 900, bean);
		if (total == 0) {
			return 0;
		}
		int d = (int) ((plus50ans * 100) / total);
		return d;
	}

	private int nbEmployeByEntreprise(EntrepriseBean bean) throws Exception {
		int res = 0;
		SalarieServiceImpl serv = new SalarieServiceImpl();
		List<SalarieBean> l = serv.getSalariesList(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		for (int i = 0; i < l.size(); i++) {
			SalarieBean salarie = l.get(i);
			ParcoursBean p = getLastParcours(salarie);
			if (p != null && p.getIdTypeContratSelected() != 3
					&& p.getIdTypeContratSelected() != 10
					&& salarie.isPresent()) {
				if (salarie.getIdEntrepriseSelected() == bean.getId()) {
					if (p != null) {
						Calendar fin = new GregorianCalendar();
						if (p.getFinFonction() == null) {
							res++;
						} else {
							fin.setTime(p.getFinFonction());
							Calendar today = new GregorianCalendar();
							today.setTime(new Date());
							if (fin.after(today)) {
								res++;
							}
						}
					}
				}
			}
		}
		return res;
	}

	private int nbEmployeByService(ServiceBean bean) throws Exception {
		int res = 0;
		SalarieServiceImpl serv = new SalarieServiceImpl();
		List<SalarieBean> l = serv.getSalariesList(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		for (int i = 0; i < l.size(); i++) {
			SalarieBean salarie = l.get(i);
			ParcoursBean p = getLastParcours(salarie);
			if (p != null && p.getIdTypeContratSelected() != 3
					&& p.getIdTypeContratSelected() != 10
					&& salarie.isPresent()) {
				if (salarie.getIdServiceSelected() == bean.getId()) {
					if (p != null) {
						Calendar fin = new GregorianCalendar();
						if (p.getFinFonction() == null) {
							res++;
						} else {
							fin.setTime(p.getFinFonction());
							Calendar today = new GregorianCalendar();
							today.setTime(new Date());
							if (fin.after(today)) {
								res++;
							}
						}
					}
				}
			}
		}
		return res;
	}

	private int nbEmployeByMetier(MetierBean bean) throws Exception {
		int res = 0;
		SalarieServiceImpl serv = new SalarieServiceImpl();
		List<SalarieBean> l = serv.getSalariesList(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		for (int i = 0; i < l.size(); i++) {
			SalarieBean salarie = l.get(i);
			ParcoursBean p = getLastParcours(salarie);
			if (p != null && p.getIdTypeContratSelected() != 3
					&& p.getIdTypeContratSelected() != 10
					&& salarie.isPresent()) {
				if ((p != null) && (p.getIdMetierSelected() == bean.getId())) {
					Calendar cal = new GregorianCalendar();
					if (p.getFinFonction() == null) {
						res++;
					} else {
						cal.setTime(p.getFinFonction());
						Calendar today = new GregorianCalendar();
						today.setTime(new Date());
						if (cal.after(today)) {
							res++;
						}
					}
				}
			}
		}
		return res;
	}

	private boolean contain(ArrayList<SalarieBean> l, SalarieBean s) {
		for (SalarieBean sal : l) {
			if (sal.getId().intValue() == s.getId().intValue()) {
				return true;
			}
		}
		return false;
	}

	private int getTrancheEntreprise(int age1, int age2, EntrepriseBean bean)
			throws Exception {
		SalarieServiceImpl serv = new SalarieServiceImpl();
		List<SalarieBeanExport> l = serv
				.getSalarieBeanExportListByIdEntreprise(bean.getId());
		int res = 0;
		for (int i = 0; i < l.size(); i++) {
			SalarieBean salarie = l.get(i);
			ParcoursBean p = getLastParcours(salarie);
			if (p != null && p.getIdTypeContratSelected() != 3
					&& p.getIdTypeContratSelected() != 10
					&& salarie.isPresent()) {
				if (salarie.getIdEntrepriseSelected() == bean.getId()) {
					if (!this.contain(listSalarie, salarie)) {
						listSalarie.add(salarie);
					}
					int age = ExcelBB.calculer(salarie.getDateNaissance());
					if ((age1 <= age) && (age <= age2)) {
						if (p != null) {
							Calendar cal = new GregorianCalendar();
							if (p.getFinFonction() == null) {
								res++;
							} else {
								cal.setTime(p.getFinFonction());
								Calendar today = new GregorianCalendar();
								today.setTime(new Date());
								if (cal.after(today)) {
									res++;
								}
							}
						}
					}
				}
			}
		}
		return res;
	}

	private int getTrancheService(int age1, int age2, ServiceBean bean)
			throws Exception {
		SalarieServiceImpl serv = new SalarieServiceImpl();
		List<SalarieBean> l = serv.getSalariesList(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		int res = 0;
		for (int i = 0; i < l.size(); i++) {
			SalarieBean salarie = l.get(i);
			ParcoursBean p = getLastParcours(salarie);
			if (p != null && p.getIdTypeContratSelected() != 3
					&& p.getIdTypeContratSelected() != 10
					&& salarie.isPresent()) {
				if (salarie.getIdServiceSelected() == bean.getId()) {
					if (!this.contain(listSalarie, salarie)) {
						listSalarie.add(salarie);
					}
					int age = ExcelBB.calculer(salarie.getDateNaissance());
					if ((age1 <= age) && (age <= age2)) {
						if (p != null) {
							Calendar cal = new GregorianCalendar();
							if (p.getFinFonction() == null) {
								res++;
							} else {
								cal.setTime(p.getFinFonction());
								Calendar today = new GregorianCalendar();
								today.setTime(new Date());
								if (cal.after(today)) {
									res++;
								}
							}
						}
					}
				}
			}
		}
		return res;
	}

	private int getTrancheMetier(int age1, int age2, MetierBean bean)
			throws Exception {
		SalarieServiceImpl serv = new SalarieServiceImpl();
		List<SalarieBean> l = serv.getSalariesList(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		int res = 0;
		for (int i = 0; i < l.size(); i++) {
			SalarieBean salarie = l.get(i);
			ParcoursBean p = getLastParcours(salarie);
			if (p != null && p.getIdTypeContratSelected() != 3
					&& p.getIdTypeContratSelected() != 10
					&& salarie.isPresent()) {
				MetierBean metier = this.getMetierBeanByIdSalarie(salarie
						.getId());
				if ((metier != null) && (metier.getId() == bean.getId())) {
					if (!this.contain(listSalarie, salarie)) {
						listSalarie.add(salarie);
					}
					int age = ExcelBB.calculer(salarie.getDateNaissance());
					if ((age1 <= age) && (age <= age2)) {
						if (p != null) {
							Calendar cal = new GregorianCalendar();
							if (p.getFinFonction() == null) {
								res++;
							} else {
								cal.setTime(p.getFinFonction());
								Calendar today = new GregorianCalendar();
								today.setTime(new Date());
								if (cal.after(today)) {
									res++;
								}
							}
						}
					}
				}
			}
		}
		return res;
	}

	private boolean isListBeancontainId(int id) {
		for (int i = 0; i < this.listBean.size(); i++) {
			if (this.listBean.get(i).getId() == id)
				return true;
		}
		return false;
	}

	private ParcoursBean getLastParcours(SalarieBean salarie) {
		List<ParcoursBean> parcourList = salarie.getParcoursBeanList();
		ParcoursBean pb = null;
		for (int i = 0; i < parcourList.size(); i++) {
			ParcoursBean parcour = parcourList.get(i);
			// Test le 1er appel
			if (pb == null) {
				pb = parcour;
			}
			if (parcour.getDebutFonction().after(pb.getDebutFonction())) {
				pb = parcour;
			}
		}
		return pb;
	}

	private MetierBean getMetierBeanByIdSalarie(int idSalarie) throws Exception {
		SalarieServiceImpl serviceSalarie = new SalarieServiceImpl();
		SalarieBean salarie = serviceSalarie.getSalarieBeanById(idSalarie);
		ParcoursBean parcour = getLastParcours(salarie);
		if (parcour != null) {
			int idMetier = parcour.getIdMetierSelected();
			MetierServiceImpl metierService = new MetierServiceImpl();
			MetierBean res = metierService.getMetierBeanById(idMetier);
			return res;
		}
		return null;
	}

	private void initialiserTotal() throws Exception {
		int res = 0;
		SalarieServiceImpl serv = new SalarieServiceImpl();
		List<SalarieBean> l = serv.getSalariesList(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		for (int i = 0; i < l.size(); i++) {
			SalarieBean salarie = l.get(i);
			ParcoursBean p = getLastParcours(salarie);
			if (p != null && p.getIdTypeContratSelected() != 3
					&& p.getIdTypeContratSelected() != 10
					&& salarie.isPresent()) {
				if (p != null) {
					Calendar fin = new GregorianCalendar();
					if (p.getFinFonction() == null) {
						res++;
					} else {
						fin.setTime(p.getFinFonction());
						Calendar today = new GregorianCalendar();
						today.setTime(new Date());
						if (fin.after(today)) {
							res++;
						}
					}
				}
			}
		}

		double plus50ans = totalTranche2 + totalTranche3;
		if (res == 0) {
			tauxTotal = 0;
		} else {
			int d = (int) ((plus50ans * 100) / res);
			tauxTotal = d;
		}
	}

	private void initialiserTotalByEntreprise() throws Exception {
		int res = 0;
		SalarieServiceImpl serv = new SalarieServiceImpl();
		List<SalarieBean> l = serv.getSalariesList(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		for (int i = 0; i < l.size(); i++) {
			SalarieBean salarie = l.get(i);
			ParcoursBean p = getLastParcours(salarie);
			if (p != null && p.getIdTypeContratSelected() != 3
					&& p.getIdTypeContratSelected() != 10
					&& salarie.isPresent()) {
				if (salarie.getIdEntrepriseSelected() == idEntreprise) {
					if (p != null) {
						Calendar fin = new GregorianCalendar();
						if (p.getFinFonction() == null) {
							res++;
						} else {
							fin.setTime(p.getFinFonction());
							Calendar today = new GregorianCalendar();
							today.setTime(new Date());
							if (fin.after(today)) {
								res++;
							}
						}
					}
				}
			}
		}
		double plus50ans = totalTranche2 + totalTranche3;
		if (res == 0) {
			tauxTotal = 0;
		} else {
			int d = (int) ((plus50ans * 100) / res);
			tauxTotal = d;
		}
	}

	private void initialiserTotalByService() throws Exception {
		int res = 0;
		SalarieServiceImpl serv = new SalarieServiceImpl();
		List<SalarieBean> l = serv.getSalariesList(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		for (int i = 0; i < l.size(); i++) {
			SalarieBean salarie = l.get(i);
			ParcoursBean p = getLastParcours(salarie);
			if (p != null && p.getIdTypeContratSelected() != 3
					&& p.getIdTypeContratSelected() != 10
					&& salarie.isPresent()) {
				if (salarie.getIdServiceSelected() == idService) {
					if (p != null) {
						Calendar fin = new GregorianCalendar();
						if (p.getFinFonction() == null) {
							res++;
						} else {
							fin.setTime(p.getFinFonction());
							Calendar today = new GregorianCalendar();
							today.setTime(new Date());
							if (fin.after(today)) {
								res++;
							}
						}
					}
				}
			}
		}

		double plus50ans = totalTranche2 + totalTranche3;
		if (res == 0) {
			tauxTotal = 0;
		} else {
			int d = (int) ((plus50ans * 100) / res);
			tauxTotal = d;
		}
	}

	private void initialiserTotalByMetier() throws Exception {
		int res = 0;
		SalarieServiceImpl serv = new SalarieServiceImpl();
		List<SalarieBean> l = serv.getSalariesList(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		for (int i = 0; i < l.size(); i++) {
			SalarieBean salarie = l.get(i);
			ParcoursBean p = this.getLastParcours(salarie);
			if (p != null && p.getIdTypeContratSelected() != 3
					&& p.getIdTypeContratSelected() != 10
					&& salarie.isPresent()) {
				if ((p != null) && (p.getIdMetierSelected() == idMetier)) {
					Calendar cal = new GregorianCalendar();
					if (p.getFinFonction() == null) {
						res++;
					} else {
						cal.setTime(p.getFinFonction());
						Calendar today = new GregorianCalendar();
						today.setTime(new Date());
						if (cal.after(today)) {
							res++;
						}
					}
				}
			}
		}

		double plus50ans = totalTranche2 + totalTranche3;
		if (res == 0) {
			tauxTotal = 0;
		} else {
			int d = (int) ((plus50ans * 100) / res);
			tauxTotal = d;
		}
	}

	public void initialiserEntreprisesList2() throws Exception {

		EntrepriseServiceImpl serv = new EntrepriseServiceImpl();
		SalarieServiceImpl salServ = new SalarieServiceImpl();
		List<EntrepriseBean> entrepList = serv.getEntreprisesList(Integer
				.parseInt(session.getAttribute("groupe").toString()));
		Collections.sort(entrepList);
		listBean = new ArrayList<ProjectionPSBean>();
		listSalarie = new ArrayList<SalarieBean>();

		Map<Integer, Integer[]> map = new HashMap<Integer, Integer[]>();
		Integer[] tabInt = new Integer[4];
		// 45-49 / 50-54 / 55-900 / nb employes entreprise
		tabInt[0] = 0;
		tabInt[1] = 0;
		tabInt[2] = 0;
		tabInt[3] = 0;

		int nbSalarieTotal = 0;

		if (idEntreprise != -1) {
			if (idService != -1) {
				if (idMetier != -1) {
					map.put(idMetier, tabInt);
				} else {

					List<MetierBean> metList = new ArrayList<MetierBean>();
					SalarieServiceImpl service = new SalarieServiceImpl();
					List<SalarieBean> l = service
							.getSalariesList(Integer.parseInt(session
									.getAttribute("groupe").toString()));
					Collections.sort(l);
					for (int i = 0; i < l.size(); i++) {
						SalarieBean salariebean = l.get(i);
						if ((salariebean.getIdEntrepriseSelected() == idEntreprise)
								&& (salariebean.getIdServiceSelected() == idService)) {
							MetierBean metBean = getMetierBeanByIdSalarie(salariebean
									.getId());
							if (metBean != null && !metList.contains(metBean)) {
								metList.add(metBean);
							}
						}
					}
					for (MetierBean m : metList) {
						map.put(m.getId(), tabInt);
					}
				}
			} else {
				ServiceImpl servServ = new ServiceImpl();
				for (ServiceBean s : servServ
						.getServiceBeanListByIdEntreprise(idEntreprise)) {
					map.put(s.getId(), tabInt);
				}
			}
		} else {
			for (EntrepriseBean e : entrepList) {
				map.put(e.getId(), tabInt);
			}
		}

		List<SalarieBean> salarieList = new ArrayList<SalarieBean>();
		if (idEntreprise != -1) {
			if (idService != -1) {
				if (idMetier != -1) {
					for (SalarieBean s : salServ
							.getSalarieBeanListByIdEntrepriseAndIdService(
									idEntreprise, idService)) {
						ParcoursBean p = getLastParcours(s);
						if (p.getIdMetierSelected() == idMetier) {
							salarieList.add(s);
						}
					}
				} else {
					salarieList = salServ
							.getSalarieBeanListByIdEntrepriseAndIdService(
									idEntreprise, idService);
				}
			} else {
				salarieList = salServ
						.getSalarieBeanListByIdEntreprise(idEntreprise);
			}
		} else {
			salarieList = salServ.getSalariesList(Integer.parseInt(session
					.getAttribute("groupe").toString()));
		}

		for (SalarieBean s : salarieList) {
			int n0 = 0;
			int n1 = 0;
			int n2 = 0;
			int n3 = 0;
			if (idEntreprise != -1) {
				if (idService != -1) {
					if (idMetier != -1) {
						n0 = map.get(idMetier)[0];
						n1 = map.get(idMetier)[1];
						n2 = map.get(idMetier)[2];
						n3 = map.get(idMetier)[3];
					} else {
						int idMetier = getLastParcours(s).getIdMetierSelected();

						n0 = map.get(idMetier)[0];
						n1 = map.get(idMetier)[1];
						n2 = map.get(idMetier)[2];
						n3 = map.get(idMetier)[3];
					}
				} else {
					n0 = map.get(s.getIdServiceSelected())[0];
					n1 = map.get(s.getIdServiceSelected())[1];
					n2 = map.get(s.getIdServiceSelected())[2];
					n3 = map.get(s.getIdServiceSelected())[3];
				}
			} else {
				n0 = map.get(s.getIdEntrepriseSelected())[0];
				n1 = map.get(s.getIdEntrepriseSelected())[1];
				n2 = map.get(s.getIdEntrepriseSelected())[2];
				n3 = map.get(s.getIdEntrepriseSelected())[3];
			}

			ParcoursBean p = getLastParcours(s);
			if (p != null && p.getIdTypeContratSelected() != 3
					&& p.getIdTypeContratSelected() != 10 && s.isPresent()) {
				if (!this.contain(listSalarie, s)) {
					listSalarie.add(s);
				}

				int age = ExcelBB.calculer(s.getDateNaissance());

				if (age >= 45) {
					Calendar fin = new GregorianCalendar();
					if (p.getFinFonction() == null) {
						n3 = n3 + 1;
						nbSalarieTotal++;
					} else {
						fin.setTime(p.getFinFonction());
						Calendar today = new GregorianCalendar();
						today.setTime(new Date());
						if (fin.after(today) || fin.equals(today)) {
							n3 = n3 + 1;
							nbSalarieTotal++;
						}
					}
				}

				if ((45 <= age) && (age <= 49)) {
					if (p != null) {

						Calendar cal = new GregorianCalendar();
						if (p.getFinFonction() == null) {
							n0 = n0 + 1;
						} else {
							cal.setTime(p.getFinFonction());
							Calendar today = new GregorianCalendar();
							today.setTime(new Date());
							if (cal.after(today) || cal.equals(today)) {
								n0 = n0 + 1;
							}
						}
					}
				}
				if ((50 <= age) && (age <= 54)) {
					if (p != null) {

						Calendar cal = new GregorianCalendar();
						if (p.getFinFonction() == null) {
							n1 = n1 + 1;
						} else {
							cal.setTime(p.getFinFonction());
							Calendar today = new GregorianCalendar();
							today.setTime(new Date());
							if (cal.after(today) || cal.equals(today)) {
								n1 = n1 + 1;
							}
						}
					}
				}
				if ((55 <= age) && (age <= 900)) {
					if (p != null) {

						Calendar cal = new GregorianCalendar();
						if (p.getFinFonction() == null) {
							n2 = n2 + 1;
						} else {
							cal.setTime(p.getFinFonction());
							Calendar today = new GregorianCalendar();
							today.setTime(new Date());
							if (cal.after(today) || cal.equals(today)) {
								n2 = n2 + 1;
							}
						}
					}
				}
			}
			tabInt = new Integer[4];
			tabInt[0] = n0;
			tabInt[1] = n1;
			tabInt[2] = n2;
			tabInt[3] = n3;
			if (idEntreprise != -1) {
				if (idService != -1) {
					if (idMetier != -1) {
						map.put(idMetier, tabInt);
					} else {
						int idMetier = getLastParcours(s).getIdMetierSelected();
						map.put(idMetier, tabInt);
					}
				} else {
					map.put(s.getIdServiceSelected(), tabInt);
				}
			} else {
				map.put(s.getIdEntrepriseSelected(), tabInt);
			}
		}

		listBean = new ArrayList<ProjectionPSBean>();
		if (idEntreprise == -1) {
			for (int i = 0; i < entrepList.size(); i++) {
				EntrepriseBean entreprise = entrepList.get(i);
				ProjectionPSBean entreprisePS = new ProjectionPSBean();
				entreprisePS.setFooter(false);
				entreprisePS.setTranche1(map.get(entreprise.getId())[0]);
				entreprisePS.setTranche2(map.get(entreprise.getId())[1]);
				entreprisePS.setTranche3(map.get(entreprise.getId())[2]);
				double total = (double) map.get(entreprise.getId())[3];
				double plus50ans = (double) map.get(entreprise.getId())[1]
						+ map.get(entreprise.getId())[2];
				if (total == 0) {
					entreprisePS.setTaux(0);
				} else {
					int d = (int) ((plus50ans * 100) / total);
					entreprisePS.setTaux(d);
				}
				entreprisePS.setId(entreprise.getId());
				entreprisePS.setNom(entreprise.getNom());
				if (!isListBeancontainId(entreprise.getId())) {
					listBean.add(entreprisePS);
				}
			}
		} else {
			if (idService != -1) {
				MetierServiceImpl metServ = new MetierServiceImpl();
				if (idMetier != -1) {
					MetierBean beanMetier = metServ.getMetierBeanById(idMetier);
					ProjectionPSBean hMetier = new ProjectionPSBean();
					hMetier.setFooter(false);
					hMetier.setId(beanMetier.getId());
					hMetier.setNom(beanMetier.getNom());
					hMetier.setTranche1(map.get(idMetier)[0]);
					hMetier.setTranche2(map.get(idMetier)[1]);
					hMetier.setTranche3(map.get(idMetier)[2]);
					double total = (double) map.get(idMetier)[3];
					double plus50ans = (double) map.get(idMetier)[1]
							+ map.get(idMetier)[2];
					if (total == 0) {
						hMetier.setTaux(0);
					} else {
						int d = (int) ((plus50ans * 100) / total);
						hMetier.setTaux(d);
					}
					listBean = new ArrayList<ProjectionPSBean>();
					listBean.add(hMetier);
				} else {
					SalarieServiceImpl service = new SalarieServiceImpl();
					List<SalarieBean> l = service
							.getSalariesList(Integer.parseInt(session
									.getAttribute("groupe").toString()));
					Collections.sort(l);
					for (int i = 0; i < l.size(); i++) {
						SalarieBean salariebean = l.get(i);
						if ((salariebean.getIdEntrepriseSelected() == idEntreprise)
								&& (salariebean.getIdServiceSelected() == idService)) {
							MetierBean metBean = getMetierBeanByIdSalarie(salariebean
									.getId());
							if (metBean != null) {
								ParcoursBean p = getLastParcours(salariebean);
								if (p != null) {
									ProjectionPSBean hMetier = new ProjectionPSBean();
									hMetier.setFooter(false);
									hMetier.setTranche1(map.get(metBean.getId())[0]);
									hMetier.setTranche2(map.get(metBean.getId())[1]);
									hMetier.setTranche3(map.get(metBean.getId())[2]);
									double total = (double) map.get(metBean
											.getId())[3];
									double plus50ans = (double) map.get(metBean
											.getId())[1]
											+ map.get(metBean.getId())[2];
									if (total == 0) {
										hMetier.setTaux(0);
									} else {
										int d = (int) ((plus50ans * 100) / total);
										hMetier.setTaux(d);
									}
									hMetier.setId(metBean.getId());
									hMetier.setNom(metBean.getNom());
									if (!isListBeancontainId(metBean.getId())) {
										listBean.add(hMetier);
									}
								}
							}
						}
					}
				}
			} else {
				ServiceImpl service = new ServiceImpl();
				List<ServiceBean> l = service.getServicesList(Integer
						.parseInt(session.getAttribute("groupe").toString()));
				Collections.sort(l);
				for (int i = 0; i < l.size(); i++) {
					ServiceBean bean = l.get(i);
					if (bean.getIdEntreprise() == idEntreprise) {
						ProjectionPSBean hService = new ProjectionPSBean();
						hService.setFooter(false);
						hService.setTranche1(map.get(bean.getId())[0]);
						hService.setTranche2(map.get(bean.getId())[1]);
						hService.setTranche3(map.get(bean.getId())[2]);
						double total = (double) map.get(bean.getId())[3];
						double plus50ans = (double) map.get(bean.getId())[1]
								+ map.get(bean.getId())[2];
						if (total == 0) {
							hService.setTaux(0);
						} else {
							int d = (int) ((plus50ans * 100) / total);
							hService.setTaux(d);
						}
						hService.setId(bean.getId());
						hService.setNom(bean.getNom());
						if (!isListBeancontainId(bean.getId())) {
							listBean.add(hService);
						}
					}
				}
			}
		}

		if (idEntreprise != -1 && idService == -1) {
			// Initialisation des items
			servicesListItem = new ArrayList<SelectItem>();
			this.totalTranche1 = 0;
			this.totalTranche2 = 0;
			this.totalTranche3 = 0;
			for (ProjectionPSBean sb : listBean) {
				servicesListItem.add(new SelectItem(sb.getId(), sb.getNom()));
				totalTranche1 += sb.getTranche1();
				totalTranche2 += sb.getTranche2();
				totalTranche3 += sb.getTranche3();
			}
		}

		if (idService != -1 && idMetier == -1) {
			// Initialisation des items
			metiersListItem = new ArrayList<SelectItem>();
			this.totalTranche1 = 0;
			this.totalTranche2 = 0;
			this.totalTranche3 = 0;
			Collections.sort(listBean);
			for (ProjectionPSBean sm : listBean) {
				metiersListItem.add(new SelectItem(sm.getId(), sm.getNom()));
				totalTranche1 += sm.getTranche1();
				totalTranche2 += sm.getTranche2();
				totalTranche3 += sm.getTranche3();
			}

		}

		if (idMetier != -1) {
			this.totalTranche1 = map.get(idMetier)[0];
			this.totalTranche2 = map.get(idMetier)[1];
			this.totalTranche3 = map.get(idMetier)[2];
		}
		if (!init) {
			init = true;
			// Initialisation des items
			entreprisesListItem = new ArrayList<SelectItem>();
			GroupeServiceImpl grServ = new GroupeServiceImpl();
			GroupeBean groupe = grServ.getGroupeBeanById(Integer
					.parseInt(session.getAttribute("groupe").toString()));
			SelectItem selectItem = new SelectItem();
			selectItem = new SelectItem();
			selectItem.setValue(-1);
			selectItem.setLabel(groupe.getNom());
			entreprisesListItem.add(selectItem);
			for (ProjectionPSBean sb : listBean) {
				entreprisesListItem
						.add(new SelectItem(sb.getId(), sb.getNom()));
			}
		}
		this.totalTranche1 = 0;
		this.totalTranche2 = 0;
		this.totalTranche3 = 0;
		for (ProjectionPSBean sb : listBean) {
			totalTranche1 += sb.getTranche1();
			totalTranche2 += sb.getTranche2();
			totalTranche3 += sb.getTranche3();
		}

		double plus50ans = totalTranche2 + totalTranche3;
		if (nbSalarieTotal == 0) {
			tauxTotal = 0;
		} else {
			int d = (int) ((plus50ans * 100) / nbSalarieTotal);
			tauxTotal = d;
		}
	}

	public void initialiserEntreprisesList() throws Exception {
		init = true;
		EntrepriseServiceImpl serv = new EntrepriseServiceImpl();
		List<EntrepriseBean> entrepList = serv.getEntreprisesList(Integer
				.parseInt(session.getAttribute("groupe").toString()));
		Collections.sort(entrepList);
		listBean = new ArrayList<ProjectionPSBean>();
		listSalarie = new ArrayList<SalarieBean>();
		for (int i = 0; i < entrepList.size(); i++) {
			EntrepriseBean entreprise = entrepList.get(i);
			ProjectionPSBean entreprisePS = new ProjectionPSBean();
			entreprisePS.setFooter(false);
			entreprisePS.setTranche1(this.getTrancheEntreprise(45, 49,
					entreprise));
			entreprisePS.setTranche2(this.getTrancheEntreprise(50, 54,
					entreprise));
			entreprisePS.setTranche3(this.getTrancheEntreprise(55, 900,
					entreprise));
			entreprisePS.setTaux(this.getTauxEntreprise(entreprise));
			entreprisePS.setId(entreprise.getId());
			entreprisePS.setNom(entreprise.getNom());
			if (!isListBeancontainId(entreprise.getId())) {
				listBean.add(entreprisePS);
			}
		}
		// Initialisation des items
		entreprisesListItem = new ArrayList<SelectItem>();
		this.totalTranche1 = 0;
		this.totalTranche2 = 0;
		this.totalTranche3 = 0;
		GroupeServiceImpl grServ = new GroupeServiceImpl();
		GroupeBean groupe = grServ.getGroupeBeanById(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		SelectItem selectItem = new SelectItem();
		selectItem = new SelectItem();
		selectItem.setValue(-1);
		selectItem.setLabel(groupe.getNom());
		entreprisesListItem.add(selectItem);
		for (ProjectionPSBean sb : listBean) {
			entreprisesListItem.add(new SelectItem(sb.getId(), sb.getNom()));
			totalTranche1 += sb.getTranche1();
			totalTranche2 += sb.getTranche2();
			totalTranche3 += sb.getTranche3();
		}
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("entreprise") != null) {
			this.idEntreprise = Integer.parseInt(session.getAttribute(
					"entreprise").toString());
		} else
			this.idEntreprise = -1;
		initialiserTotal();
	}

	public void initialiserServicesItem() throws Exception {
		ServiceImpl service = new ServiceImpl();
		List<ServiceBean> l = service.getServicesList(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		Collections.sort(l);
		listBean = new ArrayList<ProjectionPSBean>();
		listSalarie = new ArrayList<SalarieBean>();
		for (int i = 0; i < l.size(); i++) {
			ServiceBean bean = l.get(i);
			if (bean.getIdEntreprise() == idEntreprise) {
				ProjectionPSBean hService = new ProjectionPSBean();
				hService.setFooter(false);
				hService.setTranche1(this.getTrancheService(45, 49, bean));
				hService.setTranche2(this.getTrancheService(50, 54, bean));
				hService.setTranche3(this.getTrancheService(55, 900, bean));
				hService.setTaux(this.getTauxService(bean));
				hService.setId(bean.getId());
				hService.setNom(bean.getNom());
				if (!isListBeancontainId(bean.getId())) {
					listBean.add(hService);
				}
			}
		}
		// Initialisation des items
		servicesListItem = new ArrayList<SelectItem>();
		this.totalTranche1 = 0;
		this.totalTranche2 = 0;
		this.totalTranche3 = 0;
		for (ProjectionPSBean sb : listBean) {
			servicesListItem.add(new SelectItem(sb.getId(), sb.getNom()));
			totalTranche1 += sb.getTranche1();
			totalTranche2 += sb.getTranche2();
			totalTranche3 += sb.getTranche3();
		}
		initialiserTotalByEntreprise();
	}

	public void initialiserMetiersItem() throws Exception {
		SalarieServiceImpl service = new SalarieServiceImpl();
		List<SalarieBean> l = service.getSalariesList(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		Collections.sort(l);
		listBean = new ArrayList<ProjectionPSBean>();
		listSalarie = new ArrayList<SalarieBean>();
		for (int i = 0; i < l.size(); i++) {
			SalarieBean salariebean = l.get(i);
			if ((salariebean.getIdEntrepriseSelected() == idEntreprise)
					&& (salariebean.getIdServiceSelected() == idService)) {
				MetierBean metBean = getMetierBeanByIdSalarie(salariebean
						.getId());
				if (metBean != null) {
					ParcoursBean p = getLastParcours(salariebean);
					if (p != null) {
						ProjectionPSBean hMetier = new ProjectionPSBean();
						hMetier.setFooter(false);
						hMetier.setTranche1(getTrancheMetier(45, 49, metBean));
						hMetier.setTranche2(getTrancheMetier(50, 54, metBean));
						hMetier.setTranche3(getTrancheMetier(55, 900, metBean));
						hMetier.setTaux(getTauxMetier(metBean));
						hMetier.setId(metBean.getId());
						hMetier.setNom(metBean.getNom());
						if (!isListBeancontainId(metBean.getId())) {
							listBean.add(hMetier);
						}
					}
				}
			}
		}
		// Initialisation des items
		metiersListItem = new ArrayList<SelectItem>();
		this.totalTranche1 = 0;
		this.totalTranche2 = 0;
		this.totalTranche3 = 0;
		for (ProjectionPSBean sm : listBean) {
			metiersListItem.add(new SelectItem(sm.getId(), sm.getNom()));
			totalTranche1 += sm.getTranche1();
			totalTranche2 += sm.getTranche2();
			totalTranche3 += sm.getTranche3();
		}
		initialiserTotalByService();
	}

	public void initialiserMetier() throws Exception {
		MetierServiceImpl serv = new MetierServiceImpl();
		MetierBean beanMetier = serv.getMetierBeanById(idMetier);
		ProjectionPSBean hMetier = new ProjectionPSBean();
		hMetier.setFooter(false);
		hMetier.setId(beanMetier.getId());
		hMetier.setNom(beanMetier.getNom());
		hMetier.setTranche1(getTrancheMetier(45, 49, beanMetier));
		hMetier.setTranche2(getTrancheMetier(50, 54, beanMetier));
		hMetier.setTranche3(getTrancheMetier(55, 900, beanMetier));
		hMetier.setTaux(getTauxMetier(beanMetier));
		listBean = new ArrayList<ProjectionPSBean>();
		listBean.add(hMetier);
		metiersListItem = new ArrayList<SelectItem>();
		metiersListItem.add(new SelectItem(hMetier.getId(), hMetier.getNom()));
		this.totalTranche1 = hMetier.getTranche1();
		this.totalTranche2 = hMetier.getTranche2();
		this.totalTranche3 = hMetier.getTranche3();
		initialiserTotalByMetier();
	}

	public int getIdMetier() {
		return idMetier;
	}

	public void setIdMetier(int idMetier) {
		this.idMetier = idMetier;
	}

	public int getIdService() {
		return idService;
	}

	public void setIdService(int idService) {
		this.idService = idService;
	}

	public int getIdEntreprise() {
		return idEntreprise;
	}

	public void setIdEntreprise(int idEntreprise) {
		this.idEntreprise = idEntreprise;
	}

	public ArrayList<SelectItem> getEntreprisesListItem() throws Exception {
		if (!init)
			this.initialiserEntreprisesList2();
		return entreprisesListItem;
	}

	public ArrayList<SelectItem> getServicesListItem() throws Exception {
		return servicesListItem;
	}

	public ArrayList<SelectItem> getMetiersListItem() throws Exception {
		return metiersListItem;
	}

	public ArrayList<ProjectionPSBean> getListBean() throws Exception {
		return listBean;
	}

	public int getTotalTranche1() throws Exception {
		return totalTranche1;
	}

	public int getTotalTranche2() throws Exception {
		return totalTranche2;
	}

	public int getTotalTranche3() throws Exception {
		return totalTranche3;
	}

	public int getTauxTotal() throws Exception {
		return tauxTotal;
	}

	public String download(ActionEvent e) {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportDataTableToExcelServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[5];
		ResourceBundle rb = ResourceBundle.getBundle("accents");
		entete[0] = rb.getString("Intitules");
		entete[1] = "45-49 ans";
		entete[2] = "50-54 ans";
		entete[3] = "55 ans et +";
		entete[4] = "% des 50 ans et + par rapport a l'effectif";

		ServiceImpl servServ = new ServiceImpl();
		MetierServiceImpl metServ = new MetierServiceImpl();

		try {
			eContext.getSessionMap().put("datatable", listBean);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "Plan senior");
			eContext.getSessionMap().put("idEntreprise", idEntreprise);
			eContext.getSessionMap()
					.put("groupe",
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));
			if (idService != -1)
				eContext.getSessionMap().put("nomService",
						servServ.getServiceBeanById(idService).getNom());
			else
				eContext.getSessionMap().put("nomService", null);
			if (idMetier != -1)
				eContext.getSessionMap().put("nomMetier",
						metServ.getMetierBeanById(idMetier).getNom());
			else
				eContext.getSessionMap().put("nomMetier", null);

			HashMap<String, Integer> mapTotaux = new HashMap<String, Integer>();
			mapTotaux.put("totalCurYear", this.totalTranche3);
			mapTotaux.put("totalOneYearAgo", this.totalTranche2);
			mapTotaux.put("totalTwoYearAgo", this.totalTranche1);
			mapTotaux.put("total", this.tauxTotal);

			eContext.getSessionMap().put("mapTotaux", mapTotaux);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

}
