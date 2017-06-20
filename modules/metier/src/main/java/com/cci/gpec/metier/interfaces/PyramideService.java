package com.cci.gpec.metier.interfaces;

public interface PyramideService {

	/**
	 * @param type
	 *            age ou ancienneté afin de construire une pyramide des âge ou
	 *            une pyramide de l'ancienneté
	 * @param entreprise
	 *            si null pyramide générale sinon données filtrée sur
	 *            l'entreprise spécifiée
	 * @param metier
	 *            si null pyramide générale sinon données filtrée sur le métier
	 *            spécifié
	 * @param projection
	 *            nombre d'années à ajouter pour faire évoluer dans le temps la
	 *            pyramide
	 * @return la pyramide à afficher
	 * @throws Exception
	 */
	public byte[] creerPyramide(String type, Integer entreprise,
			Integer service, Integer metier, Integer projection, int idGroupe)
			throws Exception;

}
