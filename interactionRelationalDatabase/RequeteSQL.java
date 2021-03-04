package interactionRelationalDatabase;

public class RequeteSQL {
	String classi;
	String requ;
	public RequeteSQL (String classi){
		
		if(classi.equals("a")){
			/* cette requete d'obtenir la table csv de la loinc
			 * 
			 */
			 requ="SELECT* FROM loinc Where status like 'ACTIVE' or status like 'TRIAL'";
		}
		else if (classi.equals("b")){
			/* cette requête permet d'obtenir la loinc en francais sur rigel
			 * le re
			 */
			 requ="select CUI, loincNum, loincLabel, sab FROM LOINCFR";
		}
		else if(classi.equals("c")){
			requ="select codeLoinc,Composant,Grandeur,Temps,Milieu_biologique,Echelle,Technique,Chapitre from loincfrancais" ;
		}
		else if(classi.equals("asip")){
			requ="select CodeLoinc,Composantfrançais,Grandeur,Temps,Milieubiologique,Echelle,Technique,Chapitre,Statut from loincasip where statut like 'actif' " ;
		}
		else if(classi.equals("grandeur")){
			requ="select IDGrandeur, Grandeur from grandeur";
		}
		else if(classi.equals("chapitre")){
			requ="select IDChapitre,Chapitre from chapitre";
		}
		else if(classi.equals("echelle")){
			requ="select IDEchelle, Echelle from echelle";
		}
		else if(classi.equals("temps")){
			requ="select IDTemps, Temps from temps";
		}
		else if(classi.equals("technique")){
			requ="select IDTechnique, technique from technique";
		}
		else if(classi.equals("milieu")){
			requ="select IDMilieuBiologique, MilieuBiologique from milieubiologique";
		}
		else if(classi.equals("stop")){
			requ="select* from stopword";
		}
		else if(classi.equals("canada")){
			requ="select CodeLoinc, Component, grandeur, temps, Systeme, Echelle, methode, classe, sous_categorie, status, Translation_Status from loinccanada where status like 'active' ";
		}
		else{
			System.out.println("vous n'avez pas entrer les bon chiffres veuillez choisir a s'il vous faut obtenir la loinc en anglais et b si vous chercher la loinc en francais");
		}
}
	
	public String getrequ(){
		return requ;
	}
}
