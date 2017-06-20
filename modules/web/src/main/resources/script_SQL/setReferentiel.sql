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

INSERT  INTO `gpec_user`(`ID_USER`,`ID_GROUPE`,`LOGIN`,`PASSWORD`,`PROFILE`,`EVENEMENT`,`REMUNERATION`,`FICHEDEPOSTE`,`ENTRETIEN`,`ADMIN`,`CONTRAT_TRAVAIL`,`SUPER_ADMIN`,`NOM`,`PRENOM`) VALUES 
(1,NULL,'superadmin','b8910a5ec469efa3a4dcc945acaf8525e3dd8e1a6381859de7f8486cec93b92c',NULL,1,1,1,1,1,1,1,'ne pas','supprimer');

/*Data for the table `domaineformation` */

INSERT  INTO `domaineformation`(`id_domaine_formation`,`NOM_DOMAINE_FORMATION`,`ID_FAMILLE_FORMATION`) VALUES 
(1,'Accueil / assistanat / secrétariat',NULL),
(2,'Agro-alimentaire',NULL),
(3,'Achats',NULL),
(4,'Batiment gros œuvre',NULL),
(5,'Batiment second œuvre',NULL),
(6,'Bilan de compétences / VAE',NULL),
(7,'BTP maintenance',NULL),
(8,'BTP conception / organisation',NULL),
(9,'Bureautique / multi-média',NULL),
(10,'Comptabilité / gestion / finance',NULL),
(11,'Commercial / vente / marketing',NULL),
(12,'Communication',NULL),
(13,'Développement personnel',NULL),
(14,'Industrie maintenance',NULL),
(15,'Industrie conception / organisation',NULL),
(16,'Informatique / système d’information',NULL),
(17,'Juridique / droit',NULL),
(18,'Habilitation',NULL),
(19,'Langues étrangères',NULL),
(20,'Logistique / transports',NULL),
(21,'Management / RH',NULL),
(22,'Q.H.S.E.',NULL),
(23,'Remise à niveau / culture générale',NULL),
(24,'Santé / social',NULL),
(25,'Tourisme / hôtellerie / restauration',NULL);

/*Data for the table `famillemetier` */

INSERT  INTO `famillemetier`(`id_famille_metier`,`NOM_FAMILLE_METIER`) VALUES 
(1,'Agents administratifs d''entreprise'),
(2,'Agents de gardiennage et de sécurité'),
(3,'Agent d''entretien/Agent de service'),
(4,'Employé(e)s des transports et de la logistique\r\n'),
(5,'Aides à domicile et aides ménager(ère)s'),(7,'Assistant(e)s maternel(le)s\r\n'),
(8,'Attachés commerciaux et représentants'),
(9,'Ingénieurs et cadres de l''hôtellerie/restauration\r\n'),
(10,'Comptables'),
(11,'Conducteur(rice)s de véhicules\r\n'),
(12,'Conducteur(rice)s d''engins\r\n'),
(13,'Cadres dirigeants\r\n'),
(14,'Dirigeant(e)s\r\n'),
(15,'Employé(e)s / Caissier(ère)s de libre service\r\n'),
(16,'Employé(e)s de la banque et de l''assurance\r\n'),
(17,'Employé(e)s de l''hôtellerie /restauration'),
(18,'Employé(e)s de maison'),
(19,'Employé(e)s du commerce\r\n'),
(20,'Formateurs(trices)\r\n'),
(21,'Ingénieurs et cadres administratifs comptables et financiers'),
(22,'Ingénieurs et cadres de la banque et de l''assurance'),
(23,'Ingénieurs et cadres de la qualité/hygiène/sécurité/environnement'),
(24,'Ingénieurs et cadres de l''industrie'),
(25,'Ingénieurs et cadres de l''informatique'),
(26,'Ingénieurs et cadres des ressources humaines'),
(27,'Ingénieurs et cadres des transports et de la logistique'),
(28,'Ingénieurs et cadres d''études et de recherche'),
(29,'Ingénieurs et cadres du bâtiment et des travaux publics'),
(30,'Ingénieurs et cadres du commerce et de la distribution'),
(31,'ONQ Autres industries (textile, cuir, bois, ameublement, papier-carton, impression)'),
(32,'ONQ de la manutention'),
(33,'ONQ de la mécanique'),
(34,'ONQ de l''électricité, électronique'),
(35,'ONQ des industries de process ( agroalimentaire, chimie, plasturgie, pharmacie)\r\n'),
(36,'ONQ du bâtiment gros œuvre\r\n'),
(37,'ONQ du second œuvre'),
(38,'ONQ du travail des métaux'),
(39,'OQ Autres industries (textile, cuir, bois, ameublement, papier-carton, impression)\r\n'),
(40,'OQ de la maintenance'),
(41,'OQ de la manutention'),
(42,'OQ de la mécanique'),
(43,'OQ de la réparation automobile'),
(44,'OQ de l''électricité , électronique'),
(45,'OQ des industries de process (agroalimentaire, chimie, plasturgie,pharmacie)\r\n'),
(46,'OQ des métiers de bouche'),
(47,'OQ des travaux publics'),
(48,'OQ du bâtiment second oeuvre'),
(49,'OQ du bâtiment gros œuvre '),
(50,'OQ du travail des métaux'),
(51,'Personnels d''études et de recherche'),
(52,'Professionnel(le)s de l''action sociale, culturelle et sportive'),
(54,'Secrétaires'),
(55,'Assistant(e)s de direction'),
(56,'Technicien(ne)s et Agents de maîtrise de la banque et de l''assurance\r\n'),
(57,'Technicien(ne)s et Agents de maîtrise des ressources humaines\r\n'),
(58,'Technicien(ne)s et Agents de maîtrise de l''informatique\r\n'),
(59,'Technicien(ne)s et Agents de maîtrise des services administratifs, comptables et financiers\r\n'),
(60,'Technicien(ne)s et Agents de maîtrise autres industries (textile, cuir bois, ameublement, papier-carton, impression)\r\n'),
(61,'Technicien(ne)s et Agents de maîtrise de la maintenance\r\n'),
(62,'Technicien(ne)s et Agents de maîtrise de l''hôtellerie/restauration\r\n'),
(63,'Technicien(ne)s et Agents de maîtrise des industries de process ( agroalimentaire, chimie, plasturgie, pharmacie )\r\n'),
(64,'Technicien(ne)s et Agents de maîtrise des industries mécaniques/métallurgiques\r\n'),
(65,'Technicien(ne)s et Agents de maîtrise du bâtiment et des travaux publics\r\n'),
(66,'Technicien(ne)s et Agents de maîtrise des métiers commerciaux (conseil/achat/vente)\r\n'),
(67,'Technicien(ne)s et Agents de maîtrise qualité/hygiène/sécurité/environnement\r\n'),
(68,'Vendeur(se)s\r\n'),
(69,'Employé(e)s divers\r\n'),
(70,'Ingénieurs et Cadres de la santé'),
(71,'Professionnel(le)s intermédiaires de la santé'),
(72,'Professionnel(le)s de l''information, des arts et des spectacles'),
(74,'A définir'),
(79,'Agriculteur(rice)s/éleveur(se)s, salariés de leur exploitation'),
(80,'Agents administratifs des collectivités publiques'),
(81,'Employé(e)s commercial(aux)  (conseil/achat/vente)'),
(82,'Ingénieurs et cadres commerciaux (conseil/achat/vente)'),
(83,'Ingénieurs et cadres de l''agriculture, viticulture, élevage, pêche, eaux et forêts'),
(84,'Ingénieurs et cadres de la communication et de l''information'),
(85,'Ingénieurs et cadres de la maintenance'),
(86,'Ingénieurs et cadres des collectivités publiques'),
(87,'Ingénieurs et cadres des professions juridiques'),
(88,'Ingénieurs et cadres divers'),
(89,'ONQ de l''agriculture, viticulture, élevage, pêche, sylviculture'),
(90,'ONQ des travaux publics'),
(91,'ONQ divers'),
(92,'ONQ du transports et de la logistique'),
(93,'OQ de l''agriculture, viticulture, élevage, pêche, sylviculture'),
(94,'OQ des transports et de la logistique'),
(95,'OQ divers'),
(96,'Professionnel(le)s de la communication et de l''information'),
(97,'Professionnel(le)s de l''enseignement'),
(98,'Professionnel(le)s des soins du corps (coiffeur(euse)s, esthéticien(ne)s…)'),
(99,'Professionnel(le)s intermédiaires des collectivités publiques'),
(100,'Technicien(ne)s et Agents de maîtrise de l''agriculture, viticulture, élevage, pêche, eaux et forêts'),
(101,'Technicien(ne)s et Agents de maîtrise des professions juridiques'),
(102,'Technicien(ne)s et Agents de maîtrise des transport et de la logistique'),
(103,'Techniciens(ne)s et Agents de maîtrise divers'),
(104,'Vétérinaires');

/*Data for the table `motifrupturecontrat` */

INSERT  INTO `motifrupturecontrat`(`ID_MOTIF_RUPTURE_CONTRAT`,`NOM_MOTIF_RUPTURE_CONTRAT`,`ORDRE_AFFICHAGE`) VALUES 
(1,'Démission',1),
(2,'Fin de contrat CDD',3),
(3,'Rupture anticipée de CDD',11),
(4,'Rupture en période d’essai',12),
(5,'Rupture conventionnelle',13),
(6,'Licenciement',6),
(7,'Licenciement économique',7),
(8,'Retraite',14),
(9,'Décès du salarié',15),
(10,'Fin de mission CTT',4),
(11,'Fin de convention de stage',5),
(12,'Renouvellement de CDD',9),
(13,'Autre',16),
(14,'Fin de contrat',2),
(15,'Renouvellement de contrat',8),
(16,'Renouvellement de contrat CTT',10);

/*Data for the table `servicegenerique` */

INSERT  INTO `servicegenerique`(`id_service_generique`,`NOM_SERVICE_GENERIQUE`) VALUES 
(1,'Administratif/Comptabilité'),
(2,'Administratif/Juridique'),
(3,'Administratif/Financier'),
(4,'Commercial'),
(5,'Direction'),
(6,'Exploitation/Production'),
(7,'Logistique/Achats'),
(8,'Maintenance'),
(9,'Marketing'),
(10,'Qualité/Hygiène/Sécurité/Environnement'),
(11,'Recherche/Développement'),
(12,'Ressources humaines');

/*Data for the table `statut` */

INSERT  INTO `statut`(`id_statut`,`NOM_STATUT`) VALUES 
(1,'Agent de maîtrise'),
(2,'Cadre dirigeant'),
(3,'Chef d''entreprise non salarié'),
(4,'Commerçant non salarié'),
(5,'Employé qualifié'),
(6,'Employé non qualifié'),
(7,'Ingénieur & cadre'),
(8,'Ouvrier qualifié'),
(9,'Ouvrier non qualifié'),
(10,'Techniciens'),
(11,'Chef d’entreprise salarié'),
(12,'Stagiaire');

/*Data for the table `typeabsence` */

INSERT  INTO `typeabsence`(`id_type_absence`,`NOM_TYPE_ABSENCE`) VALUES 
(1,'Absence pour accident de travail'),
(2,'Absence pour accident de trajet'),
(3,'Absence pour maladie'),
(4,'Absence pour maladie professionnelle'),
(5,'Absence pour convenance personnelle'),
(6,'Absence injustifiée'),
(7,'Autre Absence'),
(8,'Congé pour raisons familiales (mariage, enfant malade, décès, …)'),
(9,'Congé maternité / paternité'),
(10,'Congé parental'),
(11,'Congé pour JRTT forfait cadre'),
(12,'Congé pour JRTT'),
(13,'Congé pour repos compensateur'),
(14,'Congé payé'),
(15,'Congé sans solde'),
(16,'Période de formation'),
(17,'Période de formation en apprentissage'),
(18,'Autre congé');

/*Data for the table `typeaccident` */

INSERT  INTO `typeaccident`(`id_type_accident`,`NOM_TYPE_ACCIDENT`) VALUES 
(1,'Accident du travail'),
(2,'Accident de trajet'),
(3,'Maladie professionnelle');

/*Data for the table `typecauseaccident` */

INSERT  INTO `typecauseaccident`(`id_type_cause_accident`,`NOM_TYPE_CAUSE_ACCIDENT`) VALUES 
(1,'Machines outils'),
(2,'Appareils de levage'),
(3,'Engins de transport'),
(4,'Explosion - Incendie'),
(5,'Substances toxiques'),
(6,'Substances chaudes'),
(7,'Substances corrosives'),
(8,'Electricité'),
(9,'Chute'),
(10,'Marche sur un objet'),
(11,'Choc contre un objet'),
(12,'Manipulation d''outils'),
(13,'Chute d''objet'),
(14,'Chute sur le sol'),
(15,'Divers'),
(16,'Autres');

/*Data for the table `typecontrat` */

INSERT  INTO `typecontrat`(`id_type_contrat`,`NOM_TYPE_CONTRAT`) VALUES 
(1,'CDI'),
(2,'CDD'),
(3,'CTT (Mission Interim)'),
(4,'Contrat Apprentissage'),
(5,'Contrat de professionnalisation'),
(6,'Autres'),
(7,'Convention de stage'),
(8,'CDIC'),
(9,'Contrats aidés'),
(10,'A définir');

/*Data for the table `typehabilitation` */

INSERT  INTO `typehabilitation`(`id_type_habilitation`,`NOM_TYPE_HABILITATION`) VALUES 
(1,'A.R.I.'),
(2,'Caces chariot élévateur'),
(3,'Caces engins de chantier'),
(4,'Caces nacelle'),
(5,'FIMO / FCOS'),
(6,'Habilitation ATEX'),
(7,'Habilitation électrique'),
(8,'Habilitation sécurité'),
(9,'Permis PL'),
(10,'S.S.T.'),
(11,'Visite médicale'),
(12,'Autre');

/*Data for the table `typelesion` */

INSERT  INTO `typelesion`(`id_type_lesion`,`NOM_TYPE_LESION`) VALUES 
(1,'Tête'),
(2,'Tronc'),
(3,'Membres supérieurs'),
(4,'Membres inférieurs'),
(5,'Mains'),
(6,'Pieds'),
(7,'Générale');

/*Data for the table `typerecoursinterim` */

INSERT  INTO `typerecoursinterim`(`id_type_recours`,`nom_type_recours`) VALUES 
(1,'remplacement d''un salarie absent'),
(2,'attente de l’arrivée effective d’un salarié recruté en CDI'),
(3,'accroissement temporaire d’activité'),
(4,'exercice d’un emploi à caractère saisonnier'),
(5,'exercice d’un emploi où l’usage du recours au CDI est exclu'),
(6,'autre');

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
