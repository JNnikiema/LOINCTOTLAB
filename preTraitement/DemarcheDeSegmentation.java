package preTraitement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author erias
 *classe permettant la segmentation des libellés en fonction de la stratégie choisie
 */
public class DemarcheDeSegmentation {
	
	private HashMap<String, Set<String>>  AbbregeEtLaListedeleurTraitement= new HashMap<String, Set<String>>();
	private HashMap<String, Set<String>>  AbbregeTraiteListeAbbre= new HashMap<String, Set<String>>();
	
	private HashMap<String, Set<String>>  AbbregeTraiteListeAbbreFinal= new HashMap<String, Set<String>>();
	
	private HashMap<String, Set<String>>   ChaineDeCaractèreRetrouveParSegmentationEspace=new HashMap<String, Set<String>>();
	private HashMap<String, Set<String>>   ChaineDeCaractèreSansPoints=new HashMap<String, Set<String>>();
	private HashMap<String, Set<String>>   ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules=new HashMap<String, Set<String>>();
	private HashMap<String, Set<String>>   ChaineDeCaractèreSansPointsNonDecouper=new HashMap<String, Set<String>>();
	
	
	private HashMap<String, Set<String>>   MotRetrouveParSegmentationPardecoupageEnFonctionDesPoints=new HashMap<String, Set<String>>();
	//private HashMap<String, Set<String>>   MotRetrouveParSegmentationPardecoupageEnFonctionDesMajuscules=new HashMap<String, Set<String>>();
	private HashMap<String, Set<String>>   MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints=new HashMap<String, Set<String>>();
	private HashMap<String, Set<String>>   MotFinissantParPointNonSegmenter=new HashMap<String, Set<String>>();
	
	private HashMap<String, Set<String>>   MotQueDesConsonnes=new HashMap<String, Set<String>>();
	private HashMap<String, Set<String>>   MotNonTraite=new HashMap<String, Set<String>>();
	
	private Set<String>   chaineDeCaractèreContenantdesPoints=new HashSet<String>();
	
	private Set<String>   chaineDeCaractèreContenantdesPointsDecomposable=new HashSet<String>();
	private Set<String>   chaineDeCaractèreContenantdesPointsNonDecoupable=new HashSet<String>();
	
	private HashMap<String, Set<String>>   Segmentation=new HashMap<String, Set<String>>();
	private HashMap<String, Set<String>>   Selection=new HashMap<String, Set<String>>();
	private HashMap<String, Set<String>>   FiltrageUneLettre=new HashMap<String, Set<String>>();
	private HashMap<String, Set<String>>   FiltrageMotsSansChiffre=new HashMap<String, Set<String>>();
	
	private HashMap<String, Set<String>>   NormalisationSansCaractèreSpeciaux=new HashMap<String, Set<String>>();
	private HashMap<String, Set<String>>   Normalisation=new HashMap<String, Set<String>>();
	Set<FinalResult> resultat= new HashSet<FinalResult>();
	Set<FinalResult> resultat2= new HashSet<FinalResult>();
	
	/**
	 * constructeur de la segmentation et prends en entrée une Hashmap de codes accompagnés de leur libellé
	 * @param codeInterface
	 */
	public DemarcheDeSegmentation(HashMap<String, List<String>> codeInterface){
		
		String ExpressionMotGraceAuxEspaces="([^\\s]+\\.*)";
		Pattern patternMotdulibbeleGraceAespace= Pattern.compile(ExpressionMotGraceAuxEspaces);
		
		String ExpressionMajusculeOuMinusculeFinissantparPoint ="(([A-Z]*[0-9]*[A-Z]+[0-9]*[A-Z]+[0-9]*(\\.|\\/))|([a-zA-Z]*[0-9]*[a-zA-Z]+[0-9]*(\\/|\\.)))";
		Pattern patternAbbreviationmajusculeouPoint= Pattern.compile(ExpressionMajusculeOuMinusculeFinissantparPoint);
		
		String ExpressionDebutCapital="([A-Z][a-z]+)";
		Pattern patternautiliserDebutMajuscule= Pattern.compile(ExpressionDebutCapital);
		
		String capital="([A-Z][A-Z]+)";
		Pattern patternCapital=Pattern.compile(capital);
		
		String minuscule="([a-z]+)";
		Pattern patternMinuscule=Pattern.compile(minuscule);
		
		String ExpressionContenantPasVoyelle="([BCDFGHJKLMNPQRSTVWXZbcdfghjklmnpqrstvwxz]+)";
		Pattern patternPourConsonne= Pattern.compile(ExpressionContenantPasVoyelle);
		
		
		
		String patternMotEntrcoupeMajuscule="([A-Z][a-z]+([A-Z][a-z]+)+)";
		Pattern patternautiliserMotEntrcoupeMajuscule= Pattern.compile(patternMotEntrcoupeMajuscule);
		
		String patternMotEntrecouperMajusculeEtMinuscule="([A-Z][A-Z]+[a-z]+[A-Z][A-Z]+([a-z]+([A-Z][A-Z]+)*)*)";
		Pattern patternautiliserpatternMotEntrecouperMajusculeEtMinuscule= Pattern.compile(patternMotEntrecouperMajusculeEtMinuscule);
		
		
		String patternMotEntrcoupeMajusculeDebutantParUneminuscule="([a-z][a-z]+([A-Z][a-z]+)+)";
		Pattern patternautiliserMotEntrcoupeMajusculeDebutantParUneminuscule= Pattern.compile(patternMotEntrcoupeMajusculeDebutantParUneminuscule);
		
		String patternMotCommancantParUneMajusculeFinissantParminuscule="([A-Z][A-Z]+[a-z][a-z]+)";
		Pattern patternautiliserMotCommancantParUneMajusculeFinissantParminuscule= Pattern.compile(patternMotCommancantParUneMajusculeFinissantParminuscule);
		
		String patternMotCommancantParUneMinusculeFinissantParMajuscule="(([a-z][a-z]+[A-Z]+)|([A-Z][a-z][a-z]+[A-Z]+))";
		Pattern patternautiliserMotCommancantParUneMinusculeFinissantParMajuscule= Pattern.compile(patternMotCommancantParUneMinusculeFinissantParMajuscule);
		
		String patternMotCommancantApresUnPoint="((\\.[a-zA-Z]+$)|(\\.[a-zA-Z]+\\s))";
		Pattern patternautiliserMotCommancantApresUnPoint= Pattern.compile(patternMotCommancantApresUnPoint);
				
		String patternMotCommancantParUneMinusculeMajuscule="([a-z]+[A-Z])";
		Pattern patternautiliserDebutMinuscule= Pattern.compile(patternMotCommancantParUneMinusculeMajuscule);
		
		
		for(String ke :codeInterface.keySet()){
			//System.out.println(codeInterface);
			
			for(String libelle:codeInterface.get(ke)){
				//Supression de tous les accents
				String LibelleSansAccent = StringUtils.stripAccents(libelle);
				//System.out.println(LibelleSansAccent);
				
				
				Matcher matcheurMotgraceAespace= patternMotdulibbeleGraceAespace.matcher(LibelleSansAccent);
				//Matcher matcheurMotMajuscule= patternCapitalText.matcher(LibelleSansAccent);
				
		/**
		 * le processus débute en découpant les libelles sur la base des espace
		 */
				while(matcheurMotgraceAespace.find()){
			
					String ChaineCaractere = matcheurMotgraceAespace.group(1);
					int traitementDuPoint=1;
			
					if(!ChaineDeCaractèreRetrouveParSegmentationEspace.containsKey(ChaineCaractere)){
				
						ChaineDeCaractèreRetrouveParSegmentationEspace.put(ChaineCaractere, new HashSet<String>());
			
					};
			
					ChaineDeCaractèreRetrouveParSegmentationEspace.get(ChaineCaractere).add(libelle);
					
					Matcher matcheurAbbreviationFinissantParPoint= patternAbbreviationmajusculeouPoint.matcher(ChaineCaractere);
					
					/**
					 * ensuite les mots correspondant aux chaines de caractères retrouver grâce aux espaces sont découpé en fonction de la ponctuation
					 */
					while(matcheurAbbreviationFinissantParPoint.find()){
						traitementDuPoint++;
						String motss=matcheurAbbreviationFinissantParPoint.group(1);
						if(!MotRetrouveParSegmentationPardecoupageEnFonctionDesPoints.containsKey(motss)){
							
							MotRetrouveParSegmentationPardecoupageEnFonctionDesPoints.put(motss, new HashSet<String>());
				
						};
				
						MotRetrouveParSegmentationPardecoupageEnFonctionDesPoints.get(motss).add(libelle);
						
						String abreviations=motss.substring(0, motss.length()-1);
						chaineDeCaractèreContenantdesPoints.add(ChaineCaractere);
						
						
						if(patternautiliserMotEntrcoupeMajusculeDebutantParUneminuscule.matcher(abreviations).matches()){
							int call=1;
							chaineDeCaractèreContenantdesPointsDecomposable.add(abreviations);
							Matcher matcheurMotDebutantMajuscule= patternautiliserDebutMajuscule.matcher(abreviations);
							/**
							 * les mots découpés en fonction de la ponctuation sont ensuite découpé en fonction des majuscules si elles existent
							 */
							while(matcheurMotDebutantMajuscule.find()){
								
								String ab= matcheurMotDebutantMajuscule.group(1);
																
								if(!MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.containsKey(ab)){
									MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.put(ab, new HashSet<String>());
								};
								MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.get(ab).add(libelle);
								
								
							}
							Matcher matcheurMotDebutantMisch= patternautiliserDebutMinuscule.matcher(abreviations);
							while(matcheurMotDebutantMisch.find()){
								String ah=matcheurMotDebutantMisch.group(1);
								String ag=ah.substring(0, ah.length()-1);
								
								
								if(!MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.containsKey(ag)){
									MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.put(ag, new HashSet<String>());
								};
								MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.get(ag).add(libelle);
								call++;
								if(call>1)
									break;
							}
							
							
						}
						
						else if(patternautiliserMotCommancantParUneMajusculeFinissantParminuscule.matcher(abreviations).matches()){
							int cals=1;
							Matcher matcheurCapital= patternCapital.matcher(abreviations);
							Matcher matcheurMiscute= patternMinuscule.matcher(abreviations);
							
							while(matcheurCapital.find()){
								String ag=matcheurCapital.group(1);
								
								
								if(!MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.containsKey(ag)){
									MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.put(ag, new HashSet<String>());
								};
								MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.get(ag).add(libelle);
								cals++;
								if(cals>1)
									break;
							}
							while(matcheurMiscute.find()){
								String ag=matcheurMiscute.group(1);
								
																
								if(!MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.containsKey(ag)){
									MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.put(ag, new HashSet<String>());
								};
								MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.get(ag).add(libelle);
								cals++;
								if(cals>2)
									break;
							}
							
							chaineDeCaractèreContenantdesPointsDecomposable.add(abreviations);
						}
						else if(patternautiliserMotCommancantParUneMinusculeFinissantParMajuscule.matcher(abreviations).matches()){
							int cals=1;
							Matcher matcheurCapital= patternCapital.matcher(abreviations);
							Matcher matcheurMiscute= patternMinuscule.matcher(abreviations);
							Matcher matcheurMotDebutantMajuscule= patternautiliserDebutMajuscule.matcher(abreviations);
							
							while(matcheurCapital.find()){
								String ag=matcheurCapital.group(1);
								
																
								if(!MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.containsKey(ag)){
									MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.put(ag, new HashSet<String>());
								};
								MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.get(ag).add(libelle);
								cals++;
								if(cals>1)
									break;
							}
							while(matcheurMotDebutantMajuscule.find()){
								String ag=matcheurMotDebutantMajuscule.group(1);
								
								
								if(!MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.containsKey(ag)){
									MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.put(ag, new HashSet<String>());
								};
								MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.get(ag).add(libelle);
								
								cals++;
								if(cals>2)
									break;
							}
							if(cals<3){
								while(matcheurMiscute.find()){
									String ag=matcheurMiscute.group(1);
								
									if(!MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.containsKey(ag)){
										MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.put(ag, new HashSet<String>());
									};
									MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.get(ag).add(libelle);
									cals++;
									if(cals>2)
									break;
								}
							}
							
							chaineDeCaractèreContenantdesPointsDecomposable.add(abreviations);
						}
						else if(patternautiliserMotEntrcoupeMajuscule.matcher(abreviations).matches()){
							Matcher matcheurMotDebutantMajuscule= patternautiliserDebutMajuscule.matcher(abreviations);
							while(matcheurMotDebutantMajuscule.find()){
								
								String ab= matcheurMotDebutantMajuscule.group(1);
																
								if(!MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.containsKey(ab)){
									MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.put(ab, new HashSet<String>());
								};
								MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.get(ab).add(libelle);
								
								
							}
							chaineDeCaractèreContenantdesPointsDecomposable.add(abreviations);
						}
						else if(patternautiliserpatternMotEntrecouperMajusculeEtMinuscule.matcher(abreviations).matches()) {
							Matcher matcheurMotDebutantMajuscule= patternCapital.matcher(abreviations);
							Matcher matecherMotMinuscule =patternMinuscule.matcher(abreviations);
							while(matcheurMotDebutantMajuscule.find()){
								
								String ab=matcheurMotDebutantMajuscule.group(1);
								if(!MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.containsKey(ab)){
									MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.put(ab, new HashSet<String>());
								};
								MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.get(ab).add(libelle);
								
								
							}
							
							while(matecherMotMinuscule.find()){
								
							//	System.out.println("a voir et rerevoir "+matecherMotMinuscule.group(1));
							
								String abb=matecherMotMinuscule.group(1);
								if(!MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.containsKey(abb)){
									MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.put(abb, new HashSet<String>());
								};
								MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.get(abb).add(libelle);
								
							
							}
							chaineDeCaractèreContenantdesPointsDecomposable.add(abreviations);
							
						}
						else {							
							chaineDeCaractèreContenantdesPointsNonDecoupable.add(motss);
							if(!MotFinissantParPointNonSegmenter.containsKey(motss)){
								MotFinissantParPointNonSegmenter.put(motss, new HashSet<String>());
							};
							MotFinissantParPointNonSegmenter.get(motss).add(libelle);
						}
						
						
					}
					
					Matcher matcheurMotCommancantParPointEtFinissantuneCHcar= patternautiliserMotCommancantApresUnPoint.matcher(ChaineCaractere);
					
					while(matcheurMotCommancantParPointEtFinissantuneCHcar.find()){
						 traitementDuPoint++;
						 chaineDeCaractèreContenantdesPoints.add(ChaineCaractere);
						String mots=matcheurMotCommancantParPointEtFinissantuneCHcar.group(1);
						if(!MotRetrouveParSegmentationPardecoupageEnFonctionDesPoints.containsKey(mots)){
							
							MotRetrouveParSegmentationPardecoupageEnFonctionDesPoints.put(mots, new HashSet<String>());
				
						};
				
						MotRetrouveParSegmentationPardecoupageEnFonctionDesPoints.get(mots).add(libelle);
						String abreviations=mots.substring(1);
						
						
						
						 if(patternautiliserMotEntrcoupeMajusculeDebutantParUneminuscule.matcher(abreviations).matches()){
							int call=1;
							Matcher matcheurMotDebutantMajuscule= patternautiliserDebutMajuscule.matcher(abreviations);
							while(matcheurMotDebutantMajuscule.find()){
								
								String ab= matcheurMotDebutantMajuscule.group(1);
																
								if(!MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.containsKey(ab)){
									MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.put(ab, new HashSet<String>());
								};
								MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.get(ab).add(libelle);
								
								
							}
							Matcher matcheurMotDebutantMisch= patternautiliserDebutMinuscule.matcher(abreviations);
							while(matcheurMotDebutantMisch.find()){
								String ah=matcheurMotDebutantMisch.group(1);
								String ag=ah.substring(0, ah.length()-1);
								
								
								if(!MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.containsKey(ag)){
									MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.put(ag, new HashSet<String>());
								};
								MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.get(ag).add(libelle);
								call++;
								if(call>1)
									break;
							}
							
							chaineDeCaractèreContenantdesPointsDecomposable.add(abreviations);
						}
						else if(patternautiliserMotCommancantParUneMajusculeFinissantParminuscule.matcher(abreviations).matches()){
							int cals=1;
							Matcher matcheurCapital= patternCapital.matcher(abreviations);
							Matcher matcheurMiscute= patternMinuscule.matcher(abreviations);
							
							while(matcheurCapital.find()){
								String ag=matcheurCapital.group(1);
								
								
								if(!MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.containsKey(ag)){
									MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.put(ag, new HashSet<String>());
								};
								MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.get(ag).add(libelle);
								cals++;
								if(cals>1)
									break;
							}
							while(matcheurMiscute.find()){
								String ag=matcheurMiscute.group(1);
								
																
								if(!MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.containsKey(ag)){
									MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.put(ag, new HashSet<String>());
								};
								MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.get(ag).add(libelle);
								cals++;
								if(cals>2)
									break;
							}
							
							chaineDeCaractèreContenantdesPointsDecomposable.add(abreviations);
						}
						else if(patternautiliserMotCommancantParUneMinusculeFinissantParMajuscule.matcher(abreviations).matches()){
							int cals=1;
							Matcher matcheurCapital= patternCapital.matcher(abreviations);
							Matcher matcheurMiscute= patternMinuscule.matcher(abreviations);
							Matcher matcheurMotDebutantMajuscule= patternautiliserDebutMajuscule.matcher(abreviations);
							
							while(matcheurCapital.find()){
								String ag=matcheurCapital.group(1);
								
																
								if(!MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.containsKey(ag)){
									MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.put(ag, new HashSet<String>());
								};
								MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.get(ag).add(libelle);
								cals++;
								if(cals>1)
									break;
							}
							while(matcheurMotDebutantMajuscule.find()){
								String ag=matcheurMotDebutantMajuscule.group(1);
								
								
								if(!MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.containsKey(ag)){
									MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.put(ag, new HashSet<String>());
								};
								MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.get(ag).add(libelle);
								
								cals++;
								if(cals>2)
									break;
							}
							if(cals<3){
								while(matcheurMiscute.find()){
									String ag=matcheurMiscute.group(1);
								
									if(!MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.containsKey(ag)){
										MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.put(ag, new HashSet<String>());
									};
									MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.get(ag).add(libelle);
									cals++;
									if(cals>2)
									break;
								}
							}
							
							chaineDeCaractèreContenantdesPointsDecomposable.add(abreviations);
						}
						else if(patternautiliserMotEntrcoupeMajuscule.matcher(abreviations).matches()){
							Matcher matcheurMotDebutantMajuscule= patternautiliserDebutMajuscule.matcher(abreviations);
							while(matcheurMotDebutantMajuscule.find()){
								
								String ab= matcheurMotDebutantMajuscule.group(1);
																
								if(!MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.containsKey(ab)){
									MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.put(ab, new HashSet<String>());
								};
								MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.get(ab).add(libelle);
								
								
							}
							chaineDeCaractèreContenantdesPointsDecomposable.add(abreviations);
						}
						else if(patternautiliserpatternMotEntrecouperMajusculeEtMinuscule.matcher(abreviations).matches()) {
							Matcher matcheurMotDebutantMajuscule= patternCapital.matcher(abreviations);
							Matcher matecherMotMinuscule =patternMinuscule.matcher(abreviations);
							while(matcheurMotDebutantMajuscule.find()){
								
								String ab=matcheurMotDebutantMajuscule.group(1);
								if(!MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.containsKey(ab)){
									MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.put(ab, new HashSet<String>());
								};
								MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.get(ab).add(libelle);
								
								
							}
							
							while(matecherMotMinuscule.find()){
								
							//	System.out.println("a voir et rerevoir "+matecherMotMinuscule.group(1));
							
								String abb=matecherMotMinuscule.group(1);
								if(!MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.containsKey(abb)){
									MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.put(abb, new HashSet<String>());
								};
								MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.get(abb).add(libelle);
								
							
							}
							chaineDeCaractèreContenantdesPointsDecomposable.add(abreviations);
							
						}
						else {							
							chaineDeCaractèreContenantdesPointsNonDecoupable.add(mots);
							if(!MotFinissantParPointNonSegmenter.containsKey(mots)){
								MotFinissantParPointNonSegmenter.put(mots, new HashSet<String>());
							};
							MotFinissantParPointNonSegmenter.get(mots).add(libelle);
						}
						
					}
					
					if(traitementDuPoint==1){
						
						/**
						 * les mots sans points subissent un découpage également 
						 * en fonction de la majuscule
						 * 
						 */
						if(!ChaineDeCaractèreSansPoints.containsKey(ChaineCaractere)){
							ChaineDeCaractèreSansPoints.put(ChaineCaractere, new HashSet<String>());
						};
						ChaineDeCaractèreSansPoints.get(ChaineCaractere).add(libelle);
						
						
						if(patternautiliserMotEntrcoupeMajusculeDebutantParUneminuscule.matcher(ChaineCaractere).matches()){
							int call=1;
							Matcher matcheurMotDebutantMajuscule= patternautiliserDebutMajuscule.matcher(ChaineCaractere);
							while(matcheurMotDebutantMajuscule.find()){
								
								String ab= matcheurMotDebutantMajuscule.group(1);
																
								if(!ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.containsKey(ab)){
									ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.put(ab, new HashSet<String>());
								};
								ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.get(ab).add(libelle);
								
								
							}
							Matcher matcheurMotDebutantMisch= patternautiliserDebutMinuscule.matcher(ChaineCaractere);
							while(matcheurMotDebutantMisch.find()){
								String ah=matcheurMotDebutantMisch.group(1);
								String ag=ah.substring(0, ah.length()-1);
								
								
								if(!ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.containsKey(ag)){
									ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.put(ag, new HashSet<String>());
								};
								ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.get(ag).add(libelle);
								call++;
								if(call>1)
									break;
							}
							
							
						}
						else if(patternautiliserMotCommancantParUneMajusculeFinissantParminuscule.matcher(ChaineCaractere).matches()){
							int cals=1;
							Matcher matcheurCapital= patternCapital.matcher(ChaineCaractere);
							Matcher matcheurMiscute= patternMinuscule.matcher(ChaineCaractere);
							
							while(matcheurCapital.find()){
								String ag=matcheurCapital.group(1);
								
								
								if(!ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.containsKey(ag)){
									ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.put(ag, new HashSet<String>());
								};
								ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.get(ag).add(libelle);
								cals++;
								if(cals>1)
									break;
							}
							while(matcheurMiscute.find()){
								String ag=matcheurMiscute.group(1);
								
																
								if(!ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.containsKey(ag)){
									ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.put(ag, new HashSet<String>());
								};
								ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.get(ag).add(libelle);
								cals++;
								if(cals>2)
									break;
							}
							
							
						}
						else if(patternautiliserMotCommancantParUneMinusculeFinissantParMajuscule.matcher(ChaineCaractere).matches()){
							int cals=1;
							Matcher matcheurCapital= patternCapital.matcher(ChaineCaractere);
							Matcher matcheurMiscute= patternMinuscule.matcher(ChaineCaractere);
							Matcher matcheurMotDebutantMajuscule= patternautiliserDebutMajuscule.matcher(ChaineCaractere);
							
							while(matcheurCapital.find()){
								String ag=matcheurCapital.group(1);
								
																
								if(!ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.containsKey(ag)){
									ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.put(ag, new HashSet<String>());
								};
								ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.get(ag).add(libelle);
								cals++;
								if(cals>1)
									break;
							}
							while(matcheurMotDebutantMajuscule.find()){
								String ag=matcheurMotDebutantMajuscule.group(1);
								
								
								if(!ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.containsKey(ag)){
									ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.put(ag, new HashSet<String>());
								};
								ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.get(ag).add(libelle);
								
								cals++;
								if(cals>2)
									break;
							}
							if(cals<3){
								while(matcheurMiscute.find()){
									String ag=matcheurMiscute.group(1);
								
									if(!ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.containsKey(ag)){
										ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.put(ag, new HashSet<String>());
									};
									ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.get(ag).add(libelle);
									cals++;
									if(cals>2)
									break;
								}
							}
							
							
						}
						else if(patternautiliserMotEntrcoupeMajuscule.matcher(ChaineCaractere).matches()){
							Matcher matcheurMotDebutantMajuscule= patternautiliserDebutMajuscule.matcher(ChaineCaractere);
							while(matcheurMotDebutantMajuscule.find()){
								
								String ab= matcheurMotDebutantMajuscule.group(1);
																
								if(!ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.containsKey(ab)){
									ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.put(ab, new HashSet<String>());
								};
								ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.get(ab).add(libelle);
								
								
							}
						}
						else if(patternautiliserpatternMotEntrecouperMajusculeEtMinuscule.matcher(ChaineCaractere).matches()) {
							Matcher matcheurMotDebutantMajuscule= patternCapital.matcher(ChaineCaractere);
							Matcher matecherMotMinuscule =patternMinuscule.matcher(ChaineCaractere);
							while(matcheurMotDebutantMajuscule.find()){
								
								String ab=matcheurMotDebutantMajuscule.group(1);
								if(!ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.containsKey(ab)){
									ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.put(ab, new HashSet<String>());
								};
								ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.get(ab).add(libelle);
								
								
							}
							
							while(matecherMotMinuscule.find()){
								
							//	System.out.println("a voir et rerevoir "+matecherMotMinuscule.group(1));
							
								String abb=matecherMotMinuscule.group(1);
								if(!ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.containsKey(abb)){
									ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.put(abb, new HashSet<String>());
								};
								ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.get(abb).add(libelle);
								
							
							}
							
							
						}
						else {							
							
							if(!ChaineDeCaractèreSansPointsNonDecouper.containsKey(ChaineCaractere)){
								ChaineDeCaractèreSansPointsNonDecouper.put(ChaineCaractere, new HashSet<String>());
							};
							ChaineDeCaractèreSansPointsNonDecouper.get(ChaineCaractere).add(libelle);
							
							if (patternPourConsonne.matcher(ChaineCaractere).matches()){
								
								if(!MotQueDesConsonnes.containsKey(ChaineCaractere)){
									MotQueDesConsonnes.put(ChaineCaractere, new HashSet<String>());
								};
								MotQueDesConsonnes.get(ChaineCaractere).add(libelle);
							
							
							}
							else{
								if(!MotNonTraite.containsKey(ChaineCaractere)){
									MotNonTraite.put(ChaineCaractere, new HashSet<String>());
								};
								MotNonTraite.get(ChaineCaractere).add(libelle);
							
							}
							
						}
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
					}
					
					
		
				}
			}
		}
		/**
		 * prise en compte uniquement des mots finissant par point
		 */
		for(String abreviation : MotFinissantParPointNonSegmenter.keySet()){
			if(!Segmentation.containsKey(abreviation)){
				Segmentation.put(abreviation, new HashSet<String>());
			};
			Segmentation.get(abreviation).addAll(MotFinissantParPointNonSegmenter.get(abreviation));
			
		}
		/**
		 * prise en compte uniquement des mots  decouplable par un point
		 */
		for(String abreviation : MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.keySet()){
			if(!Segmentation.containsKey(abreviation)){
				Segmentation.put(abreviation, new HashSet<String>());
			};
			Segmentation.get(abreviation).addAll(MotRetrouveParSegmentationPardecoupageEnFonctionDesMajusculesDansLesPoints.get(abreviation));
			
		}
		/**
		 * prise en compte uniquement des mots  decouplable en fonction des majuscules
		 */
		for(String abreviation : ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.keySet()){
			if(!Segmentation.containsKey(abreviation)){
				Segmentation.put(abreviation, new HashSet<String>());
			};
			Segmentation.get(abreviation).addAll(ChaineDeCaractèreSansPointsDecouperEnFonctionDesMajuscules.get(abreviation));
			
		}
		System.out.println("les mots retrouve par segmentation : "+Segmentation.size());
		System.out.println("les mots ne contenant que des consonnes : "+MotQueDesConsonnes.size());

		for(String  abreviation : Segmentation.keySet()){
			if(!Selection.containsKey(abreviation)){
				Selection.put(abreviation, new HashSet<String>());
			};
			Selection.get(abreviation).addAll(Segmentation.get(abreviation));
		}
		/**
		 * prise en compte des mots ne contenant que des consonnes
		 */
		for(String  abreviation : MotQueDesConsonnes.keySet()){
			if(!Selection.containsKey(abreviation)){
				Selection.put(abreviation, new HashSet<String>());
			};
			Selection.get(abreviation).addAll(MotQueDesConsonnes.get(abreviation));
		}
		
		System.out.println("les abreviations sélectionnées + que des consonnes : "+Selection.size());
		/**
		 * filtrage des mots n'ayant pas plus d'une lettre
		 */
		
		for(String abbr : Selection.keySet()){
			if(abbr.endsWith(".")|| abbr.endsWith("/")||abbr.startsWith(".")){
				if(abbr.length()>2){						
					
					if(!FiltrageUneLettre.containsKey(abbr)){
						FiltrageUneLettre.put(abbr, new HashSet<String>());
					};
					FiltrageUneLettre.get(abbr).addAll(Selection.get(abbr));
					
				}
			}
			else if (!(abbr.endsWith(".")|| abbr.endsWith("/")||abbr.startsWith("."))&&abbr.length()>1){
				if(!FiltrageUneLettre.containsKey(abbr)){
					FiltrageUneLettre.put(abbr, new HashSet<String>());
				};
				FiltrageUneLettre.get(abbr).addAll(Selection.get(abbr));
				
			}
		}
		System.out.println("selection des plus d'une lettre : "+FiltrageUneLettre.size());
		
			String pattern=	"([0-9]+)";
			Pattern patternChiffre= Pattern.compile(pattern);
			/**
			 * filtrage des mots contenant des chiffres
			 */
			
			for(String abr: FiltrageUneLettre.keySet()){
				Set<String> abregésPour =new HashSet<String>();
				
				Matcher matcheur= patternChiffre.matcher(abr);
				while(matcheur.find()) {  
					
						String abregésPoura= matcheur.group(1);
						abregésPour.add(abregésPoura);
				}
				
				
				if(abregésPour.isEmpty()){
					if(!FiltrageMotsSansChiffre.containsKey(abr)){
						FiltrageMotsSansChiffre.put(abr, new HashSet<String>());
					};
					FiltrageMotsSansChiffre.get(abr).addAll(FiltrageUneLettre.get(abr));
					
				}
			
		}
			
			System.out.println("filtrage sans chiffre : "+FiltrageMotsSansChiffre.size());
		/**
		 * suppression des points en fin de mots et normalisation
		 */
			for(String abbr: FiltrageMotsSansChiffre.keySet()){
				if(abbr.endsWith(".")|| abbr.endsWith("/")){
					String abreviationSansSigleFinal= abbr.substring(0, abbr.length()-1);
					if(!NormalisationSansCaractèreSpeciaux.containsKey(abreviationSansSigleFinal)){
						NormalisationSansCaractèreSpeciaux.put(abreviationSansSigleFinal, new HashSet<String>());
					}
					NormalisationSansCaractèreSpeciaux.get(abreviationSansSigleFinal).addAll(FiltrageMotsSansChiffre.get(abbr));
					
					//recuperation des abreviations originaux et la liste des abreviations trouvés
					if(!AbbregeTraiteListeAbbre.containsKey(abreviationSansSigleFinal)){
						AbbregeTraiteListeAbbre.put(abreviationSansSigleFinal, new HashSet<String>());
					}
					AbbregeTraiteListeAbbre.get(abreviationSansSigleFinal).add(abbr);
					
					
					if(!AbbregeEtLaListedeleurTraitement.containsKey(abbr)){
						AbbregeEtLaListedeleurTraitement.put(abbr, new HashSet<String>());
					}
					AbbregeEtLaListedeleurTraitement.get(abbr).add(abreviationSansSigleFinal);
					
				}
				else if(abbr.startsWith(".")){
					String abreviationSansSigleFinal= abbr.substring(1);
					if(!NormalisationSansCaractèreSpeciaux.containsKey(abreviationSansSigleFinal)){
						NormalisationSansCaractèreSpeciaux.put(abreviationSansSigleFinal, new HashSet<String>());
					}
					NormalisationSansCaractèreSpeciaux.get(abreviationSansSigleFinal).addAll(FiltrageMotsSansChiffre.get(abbr));
					
					
					if(!AbbregeTraiteListeAbbre.containsKey(abreviationSansSigleFinal)){
						AbbregeTraiteListeAbbre.put(abreviationSansSigleFinal, new HashSet<String>());
					}
					AbbregeTraiteListeAbbre.get(abreviationSansSigleFinal).add(abbr);
					
					
					if(!AbbregeEtLaListedeleurTraitement.containsKey(abbr)){
						AbbregeEtLaListedeleurTraitement.put(abbr, new HashSet<String>());
					}
					AbbregeEtLaListedeleurTraitement.get(abbr).add(abreviationSansSigleFinal);
				}
				else if (!(abbr.endsWith(".")|| abbr.endsWith("/"))){
					if(!NormalisationSansCaractèreSpeciaux.containsKey(abbr)){
						NormalisationSansCaractèreSpeciaux.put(abbr, new HashSet<String>());
					}
					NormalisationSansCaractèreSpeciaux.get(abbr).addAll(FiltrageMotsSansChiffre.get(abbr));
					if(!AbbregeTraiteListeAbbre.containsKey(abbr)){
						AbbregeTraiteListeAbbre.put(abbr, new HashSet<String>());
					}
					AbbregeTraiteListeAbbre.get(abbr).add(abbr);
				}
				//System.out.println("deuxieme traitement enclenché");
			}
			
			
			System.out.println("la liste des abreviations sans le caractère final : "+NormalisationSansCaractèreSpeciaux.size());
			
			for(String abrr:NormalisationSansCaractèreSpeciaux.keySet()){
				
				
				if(!Normalisation.containsKey(abrr.toLowerCase())){
					Normalisation.put(abrr.toLowerCase(), new HashSet<String>());
				}
				Normalisation.get(abrr.toLowerCase()).addAll(NormalisationSansCaractèreSpeciaux.get(abrr));
				
				

				if(!AbbregeTraiteListeAbbreFinal.containsKey(abrr.toLowerCase())){
					AbbregeTraiteListeAbbreFinal.put(abrr.toLowerCase(), new HashSet<String>());
				}
				Set<String> abreviationTT=new HashSet<String>();
				if(AbbregeTraiteListeAbbre.containsKey(abrr)){
					abreviationTT.addAll(AbbregeTraiteListeAbbre.get(abrr));
				}
				
				AbbregeTraiteListeAbbreFinal.get(abrr.toLowerCase()).addAll(abreviationTT);
				AbbregeTraiteListeAbbreFinal.get(abrr.toLowerCase()).add(abrr.toUpperCase());
				AbbregeTraiteListeAbbreFinal.get(abrr.toLowerCase()).add(StringUtils.capitalize(abrr.toLowerCase()));	
				AbbregeTraiteListeAbbreFinal.get(abrr.toLowerCase()).add(abrr);
				AbbregeTraiteListeAbbreFinal.get(abrr.toLowerCase()).add(abrr.toLowerCase());
			}
			System.out.println("liste des abreviation rendu minuscule : "+Normalisation.size());
			
			for(String abbr: Normalisation.keySet()){
				FinalResult resul= new FinalResult(abbr, Normalisation.get(abbr));
				resultat.add(resul);
			}
			

			
//				String file= "/home/erias/Bureau/IntegrationAncrageLoinc/abreviation/AbreviationTerminologieInterface.csv";
//			
//				String file2= "/home/erias/Bureau/IntegrationAncrageLoinc/abreviation/AbreviationOriginaux.csv";
				String file= "/home/erias/Bureau/graph/Segmentation/AbreviationTerminologieInterface.csv";
				
				String file2= "/home/erias/Bureau/graph/Segmentation/AbreviationOriginaux.csv";
				
				String tableau2="abreviation;SourceAbreviation";
				
				for(String ab:AbbregeTraiteListeAbbreFinal.keySet()){
					for(String lib2:AbbregeTraiteListeAbbreFinal.get(ab)){
						tableau2= tableau2+"\n"+ab+";"+lib2;
					}
				}
			
			String tableau= "abreviation;nombreLibelle;libelle";
			
			for(FinalResult ebb: resultat){
				for(String lib: ebb.getLibelles()){
					tableau= tableau+"\n"+ebb.getabreviation()+";"+ebb.getNbrelibelle()+";"+lib;
					//besion.add((String) ebb.getabreviation());
					
				}
				
			}
			
			FichierCreation af = new FichierCreation(tableau, file);
			FichierCreation af2 = new FichierCreation(tableau2, file2);
			
			
		
	}
	
	public HashMap<String, Set<String>> getresultatAbbrLibe(){
		return Normalisation;
	}

	public HashMap<String, Set<String>> getAbbregeTraiteListeAbbreNontraite(){
		return AbbregeTraiteListeAbbreFinal;
	}

}
