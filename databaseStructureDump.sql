/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

USE `gpec`;

/*Table structure for table `absence` */

DROP TABLE IF EXISTS `absence`;

CREATE TABLE `absence` (
  `id_absence` int(11) NOT NULL AUTO_INCREMENT,
  `DEBUT_ABSENCE` date DEFAULT NULL,
  `FIN_ABSENCE` date DEFAULT NULL,
  `NOMBRE_JOUR_OUVRE` double DEFAULT NULL,
  `JUSTIFICATIF` longtext,
  `id_type_absence` int(11) DEFAULT NULL,
  `ID_SALARIE` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_absence`),
  KEY `fk_absence_salarie` (`ID_SALARIE`),
  KEY `fk_absence_type_absence` (`id_type_absence`),
  CONSTRAINT `fk_absence_salarie` FOREIGN KEY (`ID_SALARIE`) REFERENCES `salarie` (`id_salarie`),
  CONSTRAINT `fk_absence_type_absence` FOREIGN KEY (`id_type_absence`) REFERENCES `typeabsence` (`id_type_absence`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `accident` */

DROP TABLE IF EXISTS `accident`;

CREATE TABLE `accident` (
  `id_accident` int(11) NOT NULL AUTO_INCREMENT,
  `DATE_ACCIDENT` date DEFAULT NULL,
  `NOMBRE_JOUR_ARRET` int(11) DEFAULT NULL,
  `JUSTIFICATIF` longtext,
  `ID_TYPE_LESION` int(11) DEFAULT NULL,
  `ID_TYPE_CAUSE_ACCIDENT` int(11) DEFAULT NULL,
  `ID_TYPE_ACCIDENT` int(11) DEFAULT NULL,
  `ID_SALARIE` int(11) DEFAULT NULL,
  `ID_ABSENCE` int(11) DEFAULT NULL,
  `INITIAL` tinyint(1) DEFAULT NULL,
  `AGGRAVATION` tinyint(1) DEFAULT NULL,
  `DATE_RECHUTE` date DEFAULT NULL,
  `NOMBRE_JOUR_ARRET_RECHUTE` int(11) DEFAULT NULL,
  `COMMENTAIRE` longtext,
  `ID_TYPE_LESION_RECHUTE` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_accident`),
  KEY `fk_accident_type_lesion` (`ID_TYPE_LESION`),
  KEY `fk_accident_type_cause_accident` (`ID_TYPE_CAUSE_ACCIDENT`),
  KEY `fk_accident_type_accident` (`ID_TYPE_ACCIDENT`),
  KEY `fk_accident_salarie` (`ID_SALARIE`),
  KEY `fk_accident_absence` (`ID_ABSENCE`),
  KEY `fk_accident_type_lesion_rechute` (`ID_TYPE_LESION_RECHUTE`),
  CONSTRAINT `fk_accident_absence` FOREIGN KEY (`ID_ABSENCE`) REFERENCES `absence` (`id_absence`),
  CONSTRAINT `fk_accident_salarie` FOREIGN KEY (`ID_SALARIE`) REFERENCES `salarie` (`ID_SALARIE`),
  CONSTRAINT `fk_accident_type_accident` FOREIGN KEY (`ID_TYPE_ACCIDENT`) REFERENCES `typeaccident` (`ID_TYPE_ACCIDENT`),
  CONSTRAINT `fk_accident_type_cause_accident` FOREIGN KEY (`ID_TYPE_CAUSE_ACCIDENT`) REFERENCES `typecauseaccident` (`ID_TYPE_CAUSE_ACCIDENT`),
  CONSTRAINT `fk_accident_type_lesion` FOREIGN KEY (`ID_TYPE_LESION`) REFERENCES `typelesion` (`ID_TYPE_LESION`),
  CONSTRAINT `fk_accident_type_lesion_rechute` FOREIGN KEY (`ID_TYPE_LESION_RECHUTE`) REFERENCES `typelesion` (`id_type_lesion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `admin` */

DROP TABLE IF EXISTS `admin`;

CREATE TABLE `admin` (
  `id_admin` int(11) NOT NULL AUTO_INCREMENT,
  `id_groupe` int(11) DEFAULT NULL,
  `migration` int(11) DEFAULT NULL,
  `logo` longtext,
  PRIMARY KEY (`id_admin`),
  KEY `fk_admin_groupe` (`id_groupe`),
  CONSTRAINT `fk_admin_groupe` FOREIGN KEY (`id_groupe`) REFERENCES `groupe` (`ID_GROUPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `contrat_travail` */

DROP TABLE IF EXISTS `contrat_travail`;

CREATE TABLE `contrat_travail` (
  `ID_CONTRAT` int(11) NOT NULL AUTO_INCREMENT,
  `DEBUT_CONTRAT` date DEFAULT NULL,
  `FIN_CONTRAT` date DEFAULT NULL,
  `RENOUVELLEMENT_CDD` tinyint(1) DEFAULT NULL,
  `JUSTIFICATIF` longtext,
  `NATURE_CONTRAT` longtext,
  `ID_METIER` int(11) DEFAULT NULL,
  `ID_TYPE_CONTRAT` int(11) DEFAULT NULL,
  `ID_SALARIE` int(11) DEFAULT NULL,
  `ID_MOTIF_RUPTURE_CONTRAT` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_CONTRAT`),
  KEY `fk_contrat_travail_salarie` (`ID_SALARIE`),
  KEY `fk_contrat_travail_type_contrat` (`ID_TYPE_CONTRAT`),
  KEY `fk_contrat_travail_metier` (`ID_METIER`),
  KEY `fk_contrat_travail_motif_rupture_contrat` (`ID_MOTIF_RUPTURE_CONTRAT`),
  CONSTRAINT `fk_contrat_travail_metier` FOREIGN KEY (`ID_METIER`) REFERENCES `metier` (`id_metier`),
  CONSTRAINT `fk_contrat_travail_motif_rupture_contrat` FOREIGN KEY (`ID_MOTIF_RUPTURE_CONTRAT`) REFERENCES `motifrupturecontrat` (`ID_MOTIF_RUPTURE_CONTRAT`),
  CONSTRAINT `fk_contrat_travail_salarie` FOREIGN KEY (`ID_SALARIE`) REFERENCES `salarie` (`id_salarie`),
  CONSTRAINT `fk_contrat_travail_type_contrat` FOREIGN KEY (`ID_TYPE_CONTRAT`) REFERENCES `typecontrat` (`id_type_contrat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `domaineformation` */

DROP TABLE IF EXISTS `domaineformation`;

CREATE TABLE `domaineformation` (
  `id_domaine_formation` int(11) NOT NULL AUTO_INCREMENT,
  `NOM_DOMAINE_FORMATION` longtext,
  `ID_FAMILLE_FORMATION` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_domaine_formation`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `entreprise` */

DROP TABLE IF EXISTS `entreprise`;

CREATE TABLE `entreprise` (
  `id_entreprise` int(11) NOT NULL AUTO_INCREMENT,
  `NOM_ENTREPRISE` longtext,
  `NUMSIRET` bigint(20) DEFAULT NULL,
  `CODEAPE` varchar(5) DEFAULT NULL,
  `DATE_CREATION` date DEFAULT NULL,
  `CCI_RATTACHEMENT` longtext,
  `ID_GROUPE` int(11) DEFAULT NULL,
  `SUIVI_FORMATIONS` tinyint(1) DEFAULT '0',
  `SUIVI_ACCIDENTS` tinyint(1) DEFAULT '0',
  `SUIVI_ABSENCES` tinyint(1) DEFAULT '0',
  `SUIVI_COMPETENCES` tinyint(1) DEFAULT '0',
  `SUIVI_DIF` tinyint(1) DEFAULT '0',
  `JUSTIFICATIF` longtext,
  `DIf_MAX` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_entreprise`),
  KEY `fk_entreprise_groupe` (`ID_GROUPE`),
  CONSTRAINT `fk_entreprise_groupe` FOREIGN KEY (`ID_GROUPE`) REFERENCES `groupe` (`ID_GROUPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `entretien` */

DROP TABLE IF EXISTS `entretien`;

CREATE TABLE `entretien` (
  `id_entretien` int(11) NOT NULL AUTO_INCREMENT,
  `PRINCIPALE_CONCLUSION` longtext,
  `SOUHAIT` longtext,
  `COMPETENCE` longtext,
  `DATE_ENTRETIEN` date DEFAULT NULL,
  `CR_SIGNE` varchar(3) DEFAULT NULL,
  `OBJ_MOYENS` longtext,
  `SERVICE_MANAGER` longtext,
  `FORMATION_N_MOINS_UN` longtext,
  `COMMENTAIRE_FORMATION` longtext,
  `MODIF_PROFIL` longtext,
  `bilan_dessous` longtext,
  `bilan_egal` longtext,
  `OBSERVATIONS` longtext,
  `bilan_dessus` longtext,
  `FORMATIONS` longtext,
  `FORMATIONS2` longtext,
  `FORMATIONS3` longtext,
  `FORMATIONS4` longtext,
  `FORMATIONS5` longtext,
  `COMMENTAIRE_BILAN` longtext,
  `OBJ_DELAIS` longtext,
  `EVOLUTION_PERSO` longtext,
  `SYNTHESE` longtext,
  `NOM_MANAGER` longtext,
  `OBJ_INTITULE` longtext,
  `OBJ_CRITERES` longtext,
  `DOMAINES_FORMATION` int(11) DEFAULT NULL,
  `DOMAINES_FORMATION2` int(11) DEFAULT NULL,
  `DOMAINES_FORMATION3` int(11) DEFAULT NULL,
  `DOMAINES_FORMATION4` int(11) DEFAULT NULL,
  `DOMAINES_FORMATION5` int(11) DEFAULT NULL,
  `JUSTIFICATIF` longtext,
  `ID_SALARIE` int(11) DEFAULT NULL,
  `ID_METIER` int(11) DEFAULT NULL,
  `ID_SERVICE` int(11) DEFAULT NULL,
  `EVALUATION_GLOBALE` longtext,
  `ANNEE_REFERENCE` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_entretien`),
  KEY `fk_entretien_metier` (`ID_METIER`),
  KEY `fk_entretien_service` (`ID_SERVICE`),
  KEY `fk_entretien_salarie` (`ID_SALARIE`),
  CONSTRAINT `fk_entretien_metier` FOREIGN KEY (`ID_METIER`) REFERENCES `metier` (`ID_METIER`),
  CONSTRAINT `fk_entretien_salarie` FOREIGN KEY (`ID_SALARIE`) REFERENCES `salarie` (`ID_SALARIE`),
  CONSTRAINT `fk_entretien_service` FOREIGN KEY (`ID_SERVICE`) REFERENCES `service` (`ID_SERVICE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `evenement` */

DROP TABLE IF EXISTS `evenement`;

CREATE TABLE `evenement` (
  `ID_EVENEMENT` int(11) NOT NULL AUTO_INCREMENT,
  `DATE_EVENEMENT` date DEFAULT NULL,
  `COMMENTAIRE` longtext,
  `NATURE` longtext,
  `DECISION` longtext,
  `JUSTIFICATIF` longtext,
  `ID_SALARIE` int(11) DEFAULT NULL,
  `INTERLOCUTEUR` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID_EVENEMENT`),
  KEY `fk_evenement_salarie` (`ID_SALARIE`),
  CONSTRAINT `fk_evenement_salarie` FOREIGN KEY (`ID_SALARIE`) REFERENCES `salarie` (`id_salarie`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `famillemetier` */

DROP TABLE IF EXISTS `famillemetier`;

CREATE TABLE `famillemetier` (
  `id_famille_metier` int(11) NOT NULL AUTO_INCREMENT,
  `NOM_FAMILLE_METIER` longtext,
  PRIMARY KEY (`id_famille_metier`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `fichedeposte` */

DROP TABLE IF EXISTS `fichedeposte`;

CREATE TABLE `fichedeposte` (
  `id_fiche_de_poste` int(11) NOT NULL AUTO_INCREMENT,
  `ID_SALARIE` int(11) DEFAULT NULL,
  `ID_METIER_TYPE` int(11) DEFAULT NULL,
  `DATE_MODIFICATION` date DEFAULT NULL,
  `COMPETENCES_SPECIFIQUES_TEXTE` longtext,
  `COMPETENCES_SPECIFIQUES` longtext,
  `SAVOIR` longtext,
  `SAVOIR_FAIRE` longtext,
  `SAVOIR_ETRE` longtext,
  `EVALUATION_GLOBALE` longtext,
  `CATEGORIE_COMPETENCE` longtext,
  `CATEGORIE_COMPETENCE2` longtext,
  `CATEGORIE_COMPETENCE3` longtext,
  `CATEGORIE_COMPETENCE4` longtext,
  `CATEGORIE_COMPETENCE5` longtext,
  `MOBILITE_ENVISAGEE` longtext,
  `COMMENTAIRES` longtext,
  `DATE_CREATION` date DEFAULT NULL,
  `COMPETENCES_NOUVELLES` longtext,
  `COMPETENCES_NOUVELLES2` longtext,
  `COMPETENCES_NOUVELLES3` longtext,
  `COMPETENCES_NOUVELLES4` longtext,
  `COMPETENCES_NOUVELLES5` longtext,
  `ACTIVITES_SPECIFIQUES` longtext,
  `JUSTIFICATIF` longtext,
  PRIMARY KEY (`id_fiche_de_poste`),
  KEY `fk_fichedeposte_metier_type` (`ID_METIER_TYPE`),
  KEY `fk_fichedeposte_salarie` (`ID_SALARIE`),
  CONSTRAINT `fk_fichedeposte_metier_type` FOREIGN KEY (`ID_METIER_TYPE`) REFERENCES `fichemetier` (`ID_FICHE_METIER`),
  CONSTRAINT `fk_fichedeposte_salarie` FOREIGN KEY (`ID_SALARIE`) REFERENCES `salarie` (`ID_SALARIE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `fichemetier` */

DROP TABLE IF EXISTS `fichemetier`;

CREATE TABLE `fichemetier` (
  `id_fiche_metier` int(11) NOT NULL AUTO_INCREMENT,
  `FINALITE_METIER` longtext,
  `SAVOIR_FAIRE` longtext,
  `ACTIVITES_RESPONSABILITES` longtext,
  `INTITULE_METIER_TYPE` longtext,
  `CSP_REFERENCE` int(11) DEFAULT NULL,
  `NIVEAU_FORMATION_REQUIS` longtext,
  `SAVOIR_ETRE` longtext,
  `SAVOIR` longtext,
  `PARTICULIARITE` longtext,
  `JUSTIFICATIF` longtext,
  `ID_GROUPE` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_fiche_metier`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `fichemetierentreprise` */

DROP TABLE IF EXISTS `fichemetierentreprise`;

CREATE TABLE `fichemetierentreprise` (
  `id_fiche_metier` int(11) NOT NULL AUTO_INCREMENT,
  `ID_ENTREPRISE` int(11) NOT NULL,
  PRIMARY KEY (`id_fiche_metier`,`ID_ENTREPRISE`),
  KEY `fk_fichemetierentreprise_entreprise` (`ID_ENTREPRISE`),
  KEY `fk_fichemetierentreprise_fiche_metier` (`id_fiche_metier`),
  CONSTRAINT `fk_fichemetierentreprise_entreprise` FOREIGN KEY (`ID_ENTREPRISE`) REFERENCES `entreprise` (`id_entreprise`),
  CONSTRAINT `fk_fichemetierentreprise_fiche_metier` FOREIGN KEY (`id_fiche_metier`) REFERENCES `fichemetier` (`id_fiche_metier`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `formation` */

DROP TABLE IF EXISTS `formation`;

CREATE TABLE `formation` (
  `id_formation` int(11) NOT NULL AUTO_INCREMENT,
  `DEBUT_FORMATION` date DEFAULT NULL,
  `FIN_FORMATION` date DEFAULT NULL,
  `NOM_FORMATION` longtext,
  `ORGANISME_FORMATINO` longtext,
  `MODE` varchar(50) DEFAULT NULL,
  `VOLUME_HORAIRE` int(11) DEFAULT NULL,
  `dif` int(10) DEFAULT NULL,
  `NOMBRE_JOUR_OUVRE` double DEFAULT NULL,
  `JUSTIFICATIF` longtext,
  `ID_DOMAINE_FORMATION` int(11) DEFAULT NULL,
  `ID_SALARIE` int(11) DEFAULT NULL,
  `ID_ABSENCE` int(11) DEFAULT NULL,
  `COUT_OPCA` double DEFAULT NULL,
  `COUT_ENTREPRISE` double DEFAULT NULL,
  `COUT_AUTRE` double DEFAULT NULL,
  `hors_dif` int(10) DEFAULT NULL,
  PRIMARY KEY (`id_formation`),
  KEY `fk_formation_salarie` (`ID_SALARIE`),
  KEY `fk_formation_absence` (`ID_ABSENCE`),
  KEY `fk_formation_domaine_formation` (`ID_DOMAINE_FORMATION`),
  CONSTRAINT `fk_formation_absence` FOREIGN KEY (`ID_ABSENCE`) REFERENCES `absence` (`id_absence`),
  CONSTRAINT `fk_formation_domaine_formation` FOREIGN KEY (`ID_DOMAINE_FORMATION`) REFERENCES `domaineformation` (`id_domaine_formation`),
  CONSTRAINT `fk_formation_salarie` FOREIGN KEY (`ID_SALARIE`) REFERENCES `salarie` (`ID_SALARIE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `gpec_user` */

DROP TABLE IF EXISTS `gpec_user`;

CREATE TABLE `gpec_user` (
  `ID_USER` int(11) NOT NULL AUTO_INCREMENT,
  `id_groupe` int(11) DEFAULT NULL,
  `LOGIN` varchar(255) DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `PROFILE` varchar(255) DEFAULT NULL,
  `EVENEMENT` tinyint(1) DEFAULT NULL,
  `REMUNERATION` tinyint(1) DEFAULT NULL,
  `FICHEDEPOSTE` tinyint(1) DEFAULT NULL,
  `ENTRETIEN` tinyint(1) DEFAULT NULL,
  `ADMIN` tinyint(1) DEFAULT NULL,
  `CONTRAT_TRAVAIL` tinyint(1) DEFAULT NULL,
  `super_admin` tinyint(1) DEFAULT NULL,
  `NOM` varchar(255) DEFAULT NULL,
  `PRENOM` varchar(255) DEFAULT NULL,
  `TELEPHONE` varchar(255) DEFAULT NULL,
  `DATE_DEMANDE_VERSION_ESSAI` date DEFAULT NULL,
  `NOM_ENTREPRISE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID_USER`),
  KEY `fk_user_groupe` (`id_groupe`),
  CONSTRAINT `fk_user_groupe` FOREIGN KEY (`id_groupe`) REFERENCES `groupe` (`ID_GROUPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `groupe` */

DROP TABLE IF EXISTS `groupe`;

CREATE TABLE `groupe` (
  `id_groupe` int(11) NOT NULL AUTO_INCREMENT,
  `NOM_GROUPE` longtext,
  `deleted` tinyint(1) DEFAULT '0',
  `fin_periode_essai` date DEFAULT NULL,
  PRIMARY KEY (`id_groupe`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `habilitation` */

DROP TABLE IF EXISTS `habilitation`;

CREATE TABLE `habilitation` (
  `id_habilitation` int(11) NOT NULL AUTO_INCREMENT,
  `DELIVRANCE` date DEFAULT NULL,
  `EXPIRATION` date DEFAULT NULL,
  `DUREE_VALIDITE` int(11) DEFAULT NULL,
  `JUSTIFICATIF` longtext,
  `ID_SALARIE` int(11) DEFAULT NULL,
  `ID_TYPE_HABILITATION` int(11) DEFAULT NULL,
  `COMMENTAIRE` longtext,
  PRIMARY KEY (`id_habilitation`),
  KEY `fk_habilitation_type_habilitation` (`ID_TYPE_HABILITATION`),
  KEY `fk_habilitation_salarie` (`ID_SALARIE`),
  CONSTRAINT `fk_habilitation_salarie` FOREIGN KEY (`ID_SALARIE`) REFERENCES `salarie` (`ID_SALARIE`),
  CONSTRAINT `fk_habilitation_type_habilitation` FOREIGN KEY (`ID_TYPE_HABILITATION`) REFERENCES `typehabilitation` (`ID_TYPE_HABILITATION`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `lien_remuneration_revenu_complementaire` */

DROP TABLE IF EXISTS `lien_remuneration_revenu_complementaire`;

CREATE TABLE `lien_remuneration_revenu_complementaire` (
  `id_lien_remuneration_revenu_complementaire` int(11) NOT NULL AUTO_INCREMENT,
  `MONTANT` double DEFAULT NULL,
  `COMMENTAIRE` longtext,
  `ACTUALISATION` double DEFAULT NULL,
  `ID_REMUNERATION` int(11) DEFAULT NULL,
  `ID_REVENU_COMPLEMENTAIRE` int(11) DEFAULT NULL,
  `REMONTE_N_PREC` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id_lien_remuneration_revenu_complementaire`),
  KEY `fk_lien_remu_rev_comp_revenu_complementaire` (`ID_REVENU_COMPLEMENTAIRE`),
  KEY `fk_lien_remu_rev_comp_remuneration` (`ID_REMUNERATION`),
  CONSTRAINT `fk_lien_remu_rev_comp_remuneration` FOREIGN KEY (`ID_REMUNERATION`) REFERENCES `remuneration` (`ID_REMUNERATION`),
  CONSTRAINT `fk_lien_remu_rev_comp_revenu_complementaire` FOREIGN KEY (`ID_REVENU_COMPLEMENTAIRE`) REFERENCES `ref_revenu_complementaire` (`ID_REVENU_COMPLEMENTAIRE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `mapping_reprise` */

DROP TABLE IF EXISTS `mapping_reprise`;

CREATE TABLE `mapping_reprise` (
  `id_mapping_reprise` int(11) NOT NULL AUTO_INCREMENT,
  `id_groupe` int(11) DEFAULT NULL,
  `entite` longtext,
  `old_id` int(11) DEFAULT NULL,
  `new_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_mapping_reprise`),
  KEY `fk_mapping_reprise_groupe` (`id_groupe`),
  CONSTRAINT `fk_mapping_reprise_groupe` FOREIGN KEY (`id_groupe`) REFERENCES `groupe` (`id_groupe`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `metier` */

DROP TABLE IF EXISTS `metier`;

CREATE TABLE `metier` (
  `id_metier` int(11) NOT NULL AUTO_INCREMENT,
  `NOM_METIER` longtext,
  `ID_FAMILLE_METIER` int(11) DEFAULT NULL,
  `DIFFICULTES` longtext,
  `ID_GROUPE` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_metier`),
  KEY `fk_metier_famille_metier` (`ID_FAMILLE_METIER`),
  KEY `fk_metier_groupe` (`ID_GROUPE`),
  CONSTRAINT `fk_metier_famille_metier` FOREIGN KEY (`ID_FAMILLE_METIER`) REFERENCES `famillemetier` (`id_famille_metier`),
  CONSTRAINT `fk_metier_groupe` FOREIGN KEY (`ID_GROUPE`) REFERENCES `groupe` (`id_groupe`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `motifrupturecontrat` */

DROP TABLE IF EXISTS `motifrupturecontrat`;

CREATE TABLE `motifrupturecontrat` (
  `ID_MOTIF_RUPTURE_CONTRAT` int(11) NOT NULL AUTO_INCREMENT,
  `NOM_MOTIF_RUPTURE_CONTRAT` longtext,
  `ORDRE_AFFICHAGE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_MOTIF_RUPTURE_CONTRAT`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `objectifsentretien` */

DROP TABLE IF EXISTS `objectifsentretien`;

CREATE TABLE `objectifsentretien` (
  `ID_OBJECTIF` int(11) NOT NULL AUTO_INCREMENT,
  `INTITULE` longtext,
  `DELAI` longtext,
  `MOYEN` longtext,
  `RESULTAT` longtext,
  `COMMENTAIRE` longtext,
  `ID_ENTRETIEN` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_OBJECTIF`),
  KEY `fk_objectifsentretien_entretien` (`ID_ENTRETIEN`),
  CONSTRAINT `fk_objectifsentretien_entretien` FOREIGN KEY (`ID_ENTRETIEN`) REFERENCES `entretien` (`id_entretien`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `paramsgeneraux` */

DROP TABLE IF EXISTS `paramsgeneraux`;

CREATE TABLE `paramsgeneraux` (
  `id_params_generaux` int(11) NOT NULL AUTO_INCREMENT,
  `AGE_LEGAL_RETRAITE_AN_N` int(11) DEFAULT NULL,
  `AGE_LEGAL_RETRAITE_AN_N_1` int(11) DEFAULT NULL,
  `AGE_LEGAL_RETRAITE_AN_N_2` int(11) DEFAULT NULL,
  `DUREE_TRAVAIL_AN_N_HEURES_REALISEES_EFFECTIF_TOT` double DEFAULT NULL,
  `DUREE_TRAVAIL_AN_N_1_HEURES_REALISEES_EFFECTIF_TOT` double DEFAULT NULL,
  `DUREE_TRAVAIL_AN_N_2_HEURES_REALISEES_EFFECTIF_TOT` double DEFAULT NULL,
  `DUREE_TRAVAIL_AN_N_JOURS_EFFECTIF_TOT` double DEFAULT NULL,
  `DUREE_TRAVAIL_AN_N_1_JOURS_EFFECTIF_TOT` double DEFAULT NULL,
  `DUREE_TRAVAIL_AN_N_2_JOURS_EFFECTIF_TOT` double DEFAULT NULL,
  `DUREE_TRAVAIL_AN_N_JOURS_SAL` int(11) DEFAULT NULL,
  `DUREE_TRAVAIL_AN_N_1_JOURS_SAL` int(11) DEFAULT NULL,
  `DUREE_TRAVAIL_AN_N_2_JOURS_SAL` int(11) DEFAULT NULL,
  `DUREE_TRAVAIL_AN_N_HEURES_SAL` int(11) DEFAULT NULL,
  `DUREE_TRAVAIL_AN_N_1_HEURES_SAL` int(11) DEFAULT NULL,
  `DUREE_TRAVAIL_AN_N_2_HEURES_SAL` int(11) DEFAULT NULL,
  `ID_ENTREPRISE` int(11) DEFAULT NULL,
  `MASSE_SALARIALE_AN_N` double DEFAULT NULL,
  `MASSE_SALARIALE_AN_N_1` double DEFAULT NULL,
  `MASSE_SALARIALE_AN_N_2` double DEFAULT NULL,
  `EFFECTIF_MOYEN_AN_N` double DEFAULT NULL,
  `EFFECTIF_MOYEN_AN_N_1` double DEFAULT NULL,
  `EFFECTIF_MOYEN_AN_N_2` double DEFAULT NULL,
  `POURCENTAGE_FORMATION_AN_N` double DEFAULT NULL,
  `POURCENTAGE_FORMATION_AN_N_1` double DEFAULT NULL,
  `POURCENTAGE_FORMATION_AN_N_2` double DEFAULT NULL,
  `EFFECTIF_PHYSIQUE_AN_N` int(11) DEFAULT NULL,
  `EFFECTIF_PHYSIQUE_AN_N_1` int(11) DEFAULT NULL,
  `EFFECTIF_PHYSIQUE_AN_N_2` int(11) DEFAULT NULL,
  `EFFECTIF_ETP_AN_N` double DEFAULT NULL,
  `EFFECTIF_ETP_AN_N_1` double DEFAULT NULL,
  `EFFECTIF_ETP_AN_N_2` double DEFAULT NULL,
  `ANNEE` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_params_generaux`),
  KEY `fk_paramsgeneraux_entreprise` (`ID_ENTREPRISE`),
  CONSTRAINT `fk_paramsgeneraux_entreprise` FOREIGN KEY (`ID_ENTREPRISE`) REFERENCES `entreprise` (`id_entreprise`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `parcours` */

DROP TABLE IF EXISTS `parcours`;

CREATE TABLE `parcours` (
  `id_parcours` int(11) NOT NULL AUTO_INCREMENT,
  `DEBUT_FONCTION` date DEFAULT NULL,
  `FIN_FONCTION` date DEFAULT NULL,
  `EQUIVALENCE_TEMPS_PLEIN` int(11) DEFAULT NULL,
  `JUSTIFICATIF` longtext,
  `ID_METIER` int(11) DEFAULT NULL,
  `ID_TYPE_CONTRAT` int(11) DEFAULT NULL,
  `ID_SALARIE` int(11) DEFAULT NULL,
  `ID_STATUT` int(11) DEFAULT NULL,
  `COEFFICIENT` longtext,
  `NIVEAU` longtext,
  `ECHELON` longtext,
  `ID_TYPE_RECOURS` int(11) DEFAULT NULL,
  `DEBUT_CONTRAT` date DEFAULT NULL,
  `FIN_CONTRAT` date DEFAULT NULL,
  `NATURE_CONTRAT` longtext,
  `ID_MOTIF_RUPTURE_CONTRAT` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_parcours`),
  KEY `fk_parcours_type_contrat` (`ID_TYPE_CONTRAT`),
  KEY `fk_parcours_metier` (`ID_METIER`),
  KEY `fk_parcours_statut` (`ID_STATUT`),
  KEY `fk_parcours_salarie` (`ID_SALARIE`),
  KEY `fk_parcours_type_recours` (`ID_TYPE_RECOURS`),
  KEY `FK7495CD89DBE222E7` (`ID_MOTIF_RUPTURE_CONTRAT`),
  CONSTRAINT `FK7495CD89DBE222E7` FOREIGN KEY (`ID_MOTIF_RUPTURE_CONTRAT`) REFERENCES `motifrupturecontrat` (`ID_MOTIF_RUPTURE_CONTRAT`),
  CONSTRAINT `fk_parcours_metier` FOREIGN KEY (`ID_METIER`) REFERENCES `metier` (`id_metier`),
  CONSTRAINT `fk_parcours_salarie` FOREIGN KEY (`ID_SALARIE`) REFERENCES `salarie` (`ID_SALARIE`),
  CONSTRAINT `fk_parcours_statut` FOREIGN KEY (`ID_STATUT`) REFERENCES `statut` (`ID_STATUT`),
  CONSTRAINT `fk_parcours_type_contrat` FOREIGN KEY (`ID_TYPE_CONTRAT`) REFERENCES `typecontrat` (`ID_TYPE_CONTRAT`),
  CONSTRAINT `fk_parcours_type_recours` FOREIGN KEY (`ID_TYPE_RECOURS`) REFERENCES `typerecoursinterim` (`id_type_recours`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `personne_a_charge` */

DROP TABLE IF EXISTS `personne_a_charge`;

CREATE TABLE `personne_a_charge` (
  `ID_PERSONNE_A_CHARGE` int(11) NOT NULL AUTO_INCREMENT,
  `ID_SALARIE` int(11) DEFAULT NULL,
  `NOM` varchar(50) DEFAULT NULL,
  `PRENOM` varchar(50) DEFAULT NULL,
  `DATE_NAISSANCE` date DEFAULT NULL,
  `LIEN_PARENTE` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID_PERSONNE_A_CHARGE`),
  KEY `fk_personne_a_charge_salarie` (`ID_SALARIE`),
  CONSTRAINT `fk_personne_a_charge_salarie` FOREIGN KEY (`ID_SALARIE`) REFERENCES `salarie` (`id_salarie`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `pieces_justificatives` */

DROP TABLE IF EXISTS `pieces_justificatives`;

CREATE TABLE `pieces_justificatives` (
  `ID_PIECES_JUSTIFICATIVES` int(11) NOT NULL AUTO_INCREMENT,
  `ID_SALARIE` int(11) DEFAULT NULL,
  `CARTE_IDENTITE` tinyint(1) DEFAULT '0',
  `ATTESTATION_SECU` tinyint(1) DEFAULT '0',
  `PERMIS_CONDUIRE` tinyint(1) DEFAULT '0',
  `RIB` tinyint(1) DEFAULT '0',
  `DIPLOMES` tinyint(1) DEFAULT '0',
  `CERTIFICAT_TRAVAIL` tinyint(1) DEFAULT '0',
  `ATTESTATION_VISITE_MEDICALE` tinyint(1) DEFAULT '0',
  `HABILITATIONS` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`ID_PIECES_JUSTIFICATIVES`),
  KEY `fk_pieces_justificatives_salarie` (`ID_SALARIE`),
  CONSTRAINT `fk_pieces_justificatives_salarie` FOREIGN KEY (`ID_SALARIE`) REFERENCES `salarie` (`id_salarie`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `ref_revenu_complementaire` */

DROP TABLE IF EXISTS `ref_revenu_complementaire`;

CREATE TABLE `ref_revenu_complementaire` (
  `id_revenu_complementaire` int(11) NOT NULL AUTO_INCREMENT,
  `LIBELLE_REVENU_COMPLEMENTAIRE` longtext,
  `TYPE_REVENU_COMPLEMENTAIRE` longtext,
  `ID_GROUPE` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_revenu_complementaire`),
  KEY `fk_ref_revenu_complementaire_groupe` (`ID_GROUPE`),
  CONSTRAINT `fk_ref_revenu_complementaire_groupe` FOREIGN KEY (`ID_GROUPE`) REFERENCES `groupe` (`id_groupe`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `remuneration` */

DROP TABLE IF EXISTS `remuneration`;

CREATE TABLE `remuneration` (
  `id_remuneration` int(11) NOT NULL AUTO_INCREMENT,
  `HEURES_SUP_50_COMMENTAIRE` longtext,
  `AUGMENTATION_COLLECTIVE` double DEFAULT NULL,
  `HEURES_SUP_50` double DEFAULT NULL,
  `SALAIRE_MENSUEL_CENVENTIONNEL_BRUT` double DEFAULT NULL,
  `ANNEE` int(11) DEFAULT NULL,
  `NIVEAU` longtext,
  `SALAIRE_MINIMUM_CONVENTIONNEL_ACTUALISATION` double DEFAULT NULL,
  `AUGMENTATION_COLLECTIVE_ACTUALISATION` double DEFAULT NULL,
  `SALAIRE_DE_BASE_ACTUALISATION` double DEFAULT NULL,
  `HORAIRE_MENSUEL_TRAVAILLE` longtext,
  `ID_METIER` int(11) DEFAULT NULL,
  `SALAIRE_MINIMUM_CONVENTIONNEL_COMMENTAIRE` longtext,
  `ECHELON` longtext,
  `SALAIRE_DE_BASE` double DEFAULT NULL,
  `SALAIRE_DE_BASE_COMMENTAIRE` longtext,
  `HEURES_SUP_25_ACTUALISATION` double DEFAULT NULL,
  `COMMENTAIRE` longtext,
  `AUGMENTATION_INDIVIDUELLE` double DEFAULT NULL,
  `HEURES_SUP_25` double DEFAULT NULL,
  `HEURES_SUP_50_ACTUALISATION` double DEFAULT NULL,
  `ID_STATUT` int(11) DEFAULT NULL,
  `TAUX_HORAIRE_BRUT` double DEFAULT NULL,
  `AUGMENTATION_INDIVIDUELLE_ACTUALISATION` double DEFAULT NULL,
  `AUGMENTATION_INDIVIDUELLE_COMMENTAIRE` longtext,
  `COEFFICIENT` longtext,
  `ID_REVENU_COMPLEMENTAIRE` int(11) DEFAULT NULL,
  `SALAIRE_MINIMUM_CONVENTIONNEL` double DEFAULT NULL,
  `HEURES_SUP_25_COMMENTAIRE` longtext,
  `AUGMENTATION_COLLECTIVE_COMMENTAIRE` longtext,
  `SALAIRE_MENSUEL_BRUT` double DEFAULT NULL,
  `ID_SALARIE` int(11) DEFAULT NULL,
  `BASE_BRUTE_ANNUELLE` double DEFAULT NULL,
  `REMUNERATION_GLOBALE` double DEFAULT NULL,
  `HEURES_SUP_ANNUELLES` double DEFAULT NULL,
  `AVANTAGES_NON_ASSUJETTIS_ANNUELS` double DEFAULT NULL,
  PRIMARY KEY (`id_remuneration`),
  KEY `fk_remuneration_salarie` (`ID_SALARIE`),
  CONSTRAINT `fk_remuneration_salarie` FOREIGN KEY (`ID_SALARIE`) REFERENCES `salarie` (`ID_SALARIE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `salarie` */

DROP TABLE IF EXISTS `salarie`;

CREATE TABLE `salarie` (
  `id_salarie` int(11) NOT NULL AUTO_INCREMENT,
  `CIVILITE` varchar(20) DEFAULT NULL,
  `NOM` varchar(100) DEFAULT NULL,
  `PRENOM` varchar(100) DEFAULT NULL,
  `ADRESSE` varchar(200) DEFAULT NULL,
  `DATE_NAISSANCE` date DEFAULT NULL,
  `LIEU_NAISSANCE` varchar(100) DEFAULT NULL,
  `TELEPHONE` varchar(15) DEFAULT NULL,
  `CREDIT_DIF` double DEFAULT NULL,
  `ID_LIEN_SUBORDINATION` int(11) DEFAULT NULL,
  `NIV_FORMATION_INIT` varchar(11) DEFAULT NULL,
  `NIV_FORMATION_ATTEINT` varchar(11) DEFAULT NULL,
  `CV` varchar(200) DEFAULT NULL,
  `PRESENT` tinyint(1) DEFAULT NULL,
  `DATE_LAST_SAISIE_DIF` date DEFAULT NULL,
  `ID_ENTREPRISE` int(11) DEFAULT NULL,
  `ID_SERVICE` int(11) DEFAULT NULL,
  `TELEPHONE_PORTABLE` varchar(15) DEFAULT NULL,
  `SITUATION_FAMILIALE` varchar(100) DEFAULT NULL,
  `PERSONNE_A_PREVENIR_NOM` varchar(100) DEFAULT NULL,
  `PERSONNE_A_PREVENIR_PRENOM` varchar(100) DEFAULT NULL,
  `PERSONNE_A_PREVENIR_ADRESSE` varchar(100) DEFAULT NULL,
  `PERSONNE_A_PREVENIR_TELEPHONE` varchar(15) DEFAULT NULL,
  `PERSONNE_A_PREVENIR_LIEN_PARENTE` varchar(100) DEFAULT NULL,
  `IMPRESSION` tinyint(1) DEFAULT NULL,
  `COMMENTAIRE` longtext,
  `POSSEDE_PERMIS_CONDUIRE` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id_salarie`),
  KEY `fk_salarie_entreprise` (`ID_ENTREPRISE`),
  KEY `fk_salarie_service` (`ID_SERVICE`),
  CONSTRAINT `fk_salarie_entreprise` FOREIGN KEY (`ID_ENTREPRISE`) REFERENCES `entreprise` (`id_entreprise`),
  CONSTRAINT `fk_salarie_service` FOREIGN KEY (`ID_SERVICE`) REFERENCES `service` (`ID_SERVICE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `service` */

DROP TABLE IF EXISTS `service`;

CREATE TABLE `service` (
  `id_service` int(11) NOT NULL AUTO_INCREMENT,
  `NOM_SERVICE` longtext,
  `ID_ENTREPRISE` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_service`),
  KEY `fk_service_entreprise` (`ID_ENTREPRISE`),
  CONSTRAINT `fk_service_entreprise` FOREIGN KEY (`ID_ENTREPRISE`) REFERENCES `entreprise` (`id_entreprise`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `servicegenerique` */

DROP TABLE IF EXISTS `servicegenerique`;

CREATE TABLE `servicegenerique` (
  `id_service_generique` int(11) NOT NULL AUTO_INCREMENT,
  `NOM_SERVICE_GENERIQUE` longtext,
  PRIMARY KEY (`id_service_generique`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `statut` */

DROP TABLE IF EXISTS `statut`;

CREATE TABLE `statut` (
  `id_statut` int(11) NOT NULL AUTO_INCREMENT,
  `NOM_STATUT` longtext,
  PRIMARY KEY (`id_statut`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `transmission` */

DROP TABLE IF EXISTS `transmission`;

CREATE TABLE `transmission` (
  `id_transmission` int(11) NOT NULL AUTO_INCREMENT,
  `id_groupe` int(11) DEFAULT NULL,
  `DATE_DERNIERE_DEMANDE` date DEFAULT NULL,
  `DATE_TRANSMISSION` date DEFAULT NULL,
  PRIMARY KEY (`id_transmission`),
  KEY `fk_transmission_groupe` (`id_groupe`),
  CONSTRAINT `fk_transmission_groupe` FOREIGN KEY (`id_groupe`) REFERENCES `groupe` (`id_groupe`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `typeabsence` */

DROP TABLE IF EXISTS `typeabsence`;

CREATE TABLE `typeabsence` (
  `id_type_absence` int(11) NOT NULL AUTO_INCREMENT,
  `id_groupe` int(11) DEFAULT NULL,
  `NOM_TYPE_ABSENCE` longtext,
  PRIMARY KEY (`id_type_absence`),
  KEY `fk_typeabsence_groupe` (`id_groupe`),
  CONSTRAINT `fk_typeabsence_groupe` FOREIGN KEY (`id_groupe`) REFERENCES `groupe` (`id_groupe`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `typeaccident` */

DROP TABLE IF EXISTS `typeaccident`;

CREATE TABLE `typeaccident` (
  `id_type_accident` int(11) NOT NULL AUTO_INCREMENT,
  `NOM_TYPE_ACCIDENT` longtext,
  PRIMARY KEY (`id_type_accident`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `typecauseaccident` */

DROP TABLE IF EXISTS `typecauseaccident`;

CREATE TABLE `typecauseaccident` (
  `id_type_cause_accident` int(11) NOT NULL AUTO_INCREMENT,
  `NOM_TYPE_CAUSE_ACCIDENT` longtext,
  PRIMARY KEY (`id_type_cause_accident`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `typecontrat` */

DROP TABLE IF EXISTS `typecontrat`;

CREATE TABLE `typecontrat` (
  `id_type_contrat` int(11) NOT NULL AUTO_INCREMENT,
  `NOM_TYPE_CONTRAT` longtext,
  PRIMARY KEY (`id_type_contrat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `typehabilitation` */

DROP TABLE IF EXISTS `typehabilitation`;

CREATE TABLE `typehabilitation` (
  `id_type_habilitation` int(11) NOT NULL AUTO_INCREMENT,
  `id_groupe` int(11) DEFAULT NULL,
  `NOM_TYPE_HABILITATION` longtext,
  PRIMARY KEY (`id_type_habilitation`),
  KEY `fk_typehabilitation_groupe` (`id_groupe`),
  CONSTRAINT `fk_typehabilitation_groupe` FOREIGN KEY (`id_groupe`) REFERENCES `groupe` (`id_groupe`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `typelesion` */

DROP TABLE IF EXISTS `typelesion`;

CREATE TABLE `typelesion` (
  `id_type_lesion` int(11) NOT NULL AUTO_INCREMENT,
  `NOM_TYPE_LESION` longtext,
  PRIMARY KEY (`id_type_lesion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `typerecoursinterim` */

DROP TABLE IF EXISTS `typerecoursinterim`;

CREATE TABLE `typerecoursinterim` (
  `id_type_recours` int(11) NOT NULL AUTO_INCREMENT,
  `nom_type_recours` longtext,
  PRIMARY KEY (`id_type_recours`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
