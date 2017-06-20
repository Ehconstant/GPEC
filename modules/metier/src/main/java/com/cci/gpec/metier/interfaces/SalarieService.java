package com.cci.gpec.metier.interfaces;

import java.util.List;

import com.cci.gpec.commons.SalarieBean;

/**
 * 
 * Service pour la munipulation des Salaries.
 * 
 * 
 */
public interface SalarieService {

	/**
	 * Retourne la liste des salariés.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public List<SalarieBean> getSalariesList(int idGroupe) throws Exception;

	/**
	 * Retourne la liste des salariés ordonné par age
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public List<SalarieBean> getPyramideDataOrderedOderedByAge(
			Integer idEntreprise, Integer idService, int idGroupe)
			throws Exception;

	/**
	 * Retourne la liste des salariés ordonné par ancienneté
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public List<SalarieBean> getPyramideDataOrderedByAnciennete(
			Integer idEntreprise, Integer idService, int idGroupe)
			throws Exception;

	/**
	 * Retourne le salarié à partir de son identifiant.
	 * 
	 * @param idSalarie
	 * @return
	 * @throws Exception
	 */
	// public MoseBean getMoseWithId(Integer idSalarie) throws Exception;

	public void saveOrUppdate(SalarieBean salarieBean) throws Exception;
}
