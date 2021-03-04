package preTraitement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import interactionRelationalDatabase.ConnexionBD;
import interactionTripleStore.InteractionBlazgraph;
import utilitaries.LibelleAvecEspaceCorrecte;


/**
 * 
 * @author erias
 *Cette classe decrit le processus de correction de la terminologie d'interface à partir de 
 */
public class CorrectionLibelle {
	HashMap<String, Set<String>> interfaceCorrige= new HashMap<String, Set<String>>();
	Set<String> LesLibellesdistinct= new HashSet<String>();
	HashMap<String, Set<String>> AbreviationListeDesAbreviationsOriginaux=new HashMap<String, Set<String>>();
	
	public CorrectionLibelle(HashMap<String, Set<String>>codelibelleCorrection, String namespace){
		HashMap<String, Set<String>> ResultatCorrection=new HashMap<String, Set<String>>();
		
		ConnexionBD df = new ConnexionBD(11);

		String recuperationAbreviationCorrectionManuelle2 = "Select abreviation, "
				+ "mot_complet "
				+ "from  dictionnairemanuel where mot_complet is not null";
		String recuperationdesindexAbreviation = "Select abreviation, "
				+ "SourceAbreviation "
				+ "from  indexabreviation";
		String recuperationAbreviationCorrectionWIki2 = "Select abreviation, "
				+ "traduction "
				+ "from  DictionnaireCreeWikipedia";
	
				try {
					PreparedStatement ps = df.getconn().prepareStatement(recuperationdesindexAbreviation);
					PreparedStatement ps3 = df.getconn().prepareStatement(recuperationAbreviationCorrectionWIki2);
					
					PreparedStatement ps2 = df.getconn().prepareStatement(recuperationAbreviationCorrectionManuelle2);
					
					//ex�cution de la requ�te
					
					ResultSet resultatIndexabreviationOrigianux = ps.executeQuery();
					ResultSet resultatAbreciation2 = ps2.executeQuery();
					ResultSet resultatAbreciationWIki = ps3.executeQuery();
					while(resultatIndexabreviationOrigianux.next()){
						if(!resultatIndexabreviationOrigianux.getObject(1).toString().trim().equals("NA")||resultatIndexabreviationOrigianux.getObject(1)!=null){
							if(!AbreviationListeDesAbreviationsOriginaux.containsKey(resultatIndexabreviationOrigianux.getObject(1).toString())){
								AbreviationListeDesAbreviationsOriginaux.put(resultatIndexabreviationOrigianux.getObject(1).toString().trim(), new HashSet<String>());
							}
							
							AbreviationListeDesAbreviationsOriginaux.get(resultatIndexabreviationOrigianux.getObject(1).toString().trim()).add(resultatIndexabreviationOrigianux.getString(2));
						}
						}
						
						while(resultatAbreciation2.next()){
							if(!ResultatCorrection.containsKey(resultatAbreciation2.getString(1))){
									ResultatCorrection.put(resultatAbreciation2.getString(1), new HashSet<String>());
								}
							
								ResultatCorrection.get(resultatAbreciation2.getString(1).trim()).add(resultatAbreciation2.getString(2));
							
							}
						while(resultatAbreciationWIki.next()){
							if(ResultatCorrection.containsKey(resultatAbreciationWIki.getString(1).toLowerCase().trim())){
								ResultatCorrection.get(resultatAbreciationWIki.getString(1).toLowerCase().trim()).add(resultatAbreciationWIki.getString(2));
								}
							
								
							}
						
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		
		String ExpressionMotGraceAuxEspaces="([^\\s]+\\.*)";
		Pattern patternMotdulibbeleGraceAespace= Pattern.compile(ExpressionMotGraceAuxEspaces);
		Set<Statement> lesSatement= new HashSet<Statement>();
		RepositoryConnection connnex= InteractionBlazgraph.connex(namespace);
		RecuperationGraphRDF inter = new RecuperationGraphRDF(connnex, "b","http://erias.org/InterfaceTerminology/TLAB");
		String Prefix_rdfs="http://www.w3.org/2000/01/rdf-schema#";
		ValueFactory vf = connnex.getValueFactory();
		HashMap<String, List<String>> codelibelleOriginaux=inter.getRDFlisteLibellé();
		for(String code : codelibelleOriginaux.keySet()){
			Set<String> libelleOrgi= new HashSet<>();
			libelleOrgi.addAll(codelibelleOriginaux.get(code));
			for(String line:libelleOrgi){
			//	String ldf=LibelleSegmente.getLibelleSegmente(line);
				String lins=LibelleSegmente.getLibelleSegmente(line);
				//System.out.println("++++++++++++++++++++++++"+lins);
				//System.out.println("------*------*--------*-"+line);
				Statement stm = vf.createStatement(vf.createURI(code), 
						vf.createURI(Prefix_rdfs + "label"),
						vf.createLiteral (lins));
				lesSatement.add(stm);
			}
		}
		
		for(String CodeInterface :codelibelleCorrection.keySet()){
			Set<String> EnsembleLibelle= new HashSet<String>();
			EnsembleLibelle.addAll(codelibelleCorrection.get(CodeInterface));
			for(String lin :codelibelleCorrection.get(CodeInterface) ){
				EnsembleLibelle.add(LibelleSegmente.getLibelleSegmente(lin));
			}
			
			
			for(String libelleCorrige:EnsembleLibelle){
				
				/**
				 * Supression de tous les accents et des caractères spéciaux
				 * empéchant l'exécution de l'expression régulière
				 */
				
				String LibelleSans = StringUtils.stripAccents(libelleCorrige);
				String LibelleSansAc= LibelleSans.replaceAll("\\(", "« ");
				String LibelleSansAcs= LibelleSansAc.replaceAll("\\{", "& ");
				String LibelleSansAcsP= LibelleSansAcs.replaceAll("\\*", "§ ");
				String LibelleSansAcsPP= LibelleSansAcsP.replaceAll("\\?", " , ");
				String LibelleSansAcsPPP= LibelleSansAcsPP.replaceAll("\\[", " @ ");
				String LibelleSansAcsPPPP= LibelleSansAcsPPP.replaceAll("\\]", " % ");
				String LibelleSansAcsPPPPP= LibelleSansAcsPPPP.replaceAll("\\+", " £ ");
				String LibelleSansAccent= LibelleSansAcsPPPPP.replaceAll("\\)", " » ");
				System.out.println(LibelleSansAccent);
				HashMap<String, Set<String>> Correction =new HashMap<String, Set<String>>();
		//		Set<String> resultat=new HashSet<String>();
				
				if(!Correction.containsKey("0")){
					Correction.put("0", new HashSet<String>());
				}
				Correction.get("0").add(LibelleSansAccent);
				
		
				Matcher matcheurMotgraceAespace= patternMotdulibbeleGraceAespace.matcher(LibelleSansAccent);
				
				int cals=1;
				
				while(matcheurMotgraceAespace.find()){
					
					// nous avons déterminier le mot délimiter par des espaces
					
					String abreviationP=matcheurMotgraceAespace.group(1);
					//System.out.println("l'abreviation traitée "+abreviationP);
					int motvalide=1;
					Set<String>CorrectionPourLemotDelimiter=new HashSet<String>();
					// nous recherchons dans le dictionnaire créer si le mot ne présente pas de traduction
					if(ResultatCorrection.containsKey(abreviationP)){
						CorrectionPourLemotDelimiter.addAll(ResultatCorrection.get(abreviationP));
						motvalide++;
					}
					for(String abr:AbreviationListeDesAbreviationsOriginaux.keySet()){
						//System.out.println("important");
						
						if(AbreviationListeDesAbreviationsOriginaux.get(abr).contains(abreviationP)){
							//System.out.println("le mot racine source "+abr);
							if(ResultatCorrection.containsKey(abr)){
							CorrectionPourLemotDelimiter.addAll(ResultatCorrection.get(abr));
							}
							else{
								// code ajouté 11/12/2017
								CorrectionPourLemotDelimiter.add(abr);
							}
							
							motvalide++;
							
						}
						
					}
					
					// si le mot en entier ne possède pas de correction procédons a son découpage
					
					if(motvalide==1){
						String patternMotEntrcoupeMajuscule="([A-Z][a-z]+([A-Z][a-z]+)+)";
						Pattern patternautiliserMotEntrcoupeMajuscule= Pattern.compile(patternMotEntrcoupeMajuscule);
						
						String patternMotEntrcoupeMajusculeDebutantParUneminuscule="([a-z][a-z]+([A-Z][a-z]+)+)";
						Pattern patternautiliserMotEntrcoupeMajusculeDebutantParUneminuscule= Pattern.compile(patternMotEntrcoupeMajusculeDebutantParUneminuscule);
						String patternMotEntrecouperMajusculeEtMinuscule="([A-Z][A-Z]+[a-z]+[A-Z][A-Z]+([a-z]+([A-Z][A-Z]+)*)*)";
						Pattern patternautiliserpatternMotEntrecouperMajusculeEtMinuscule= Pattern.compile(patternMotEntrecouperMajusculeEtMinuscule);
						
						
						String patternMotCommancantParUneMajusculeFinissantParminuscule="([A-Z][A-Z]+[a-z][a-z]+)";
						Pattern patternautiliserMotCommancantParUneMajusculeFinissantParminuscule= Pattern.compile(patternMotCommancantParUneMajusculeFinissantParminuscule);
						
						String patternMotCommancantParUneMinusculeFinissantParMajuscule="(([a-z][a-z]+[A-Z]+)|([A-Z][a-z][a-z]+[A-Z]+))";
						Pattern patternautiliserMotCommancantParUneMinusculeFinissantParMajuscule= Pattern.compile(patternMotCommancantParUneMinusculeFinissantParMajuscule);
						String ExpressionParterminalPoint ="(([A-Z]*[0-9]*[A-Z]+[0-9]*[A-Z]+[0-9]*(\\.|\\/))|([a-zA-Z]*[0-9]*[a-zA-Z]+[0-9]*(\\/|\\.)))";
						Pattern patternAbbreviatfinalPoint= Pattern.compile(ExpressionParterminalPoint);
						
						if(patternautiliserMotCommancantParUneMinusculeFinissantParMajuscule.matcher(abreviationP).matches()
								||
								patternautiliserMotCommancantParUneMajusculeFinissantParminuscule.matcher(abreviationP).matches()
								||patternautiliserMotEntrcoupeMajusculeDebutantParUneminuscule.matcher(abreviationP).matches()
								||patternautiliserMotEntrcoupeMajuscule.matcher(abreviationP).matches()||patternautiliserpatternMotEntrecouperMajusculeEtMinuscule.matcher(abreviationP).matches()
								){
							decoupageMotCorrection fg= new decoupageMotCorrection(abreviationP, ResultatCorrection, AbreviationListeDesAbreviationsOriginaux);
							CorrectionPourLemotDelimiter.addAll(fg.getCorrectionMot());
						}
						else if(patternAbbreviatfinalPoint.matcher(abreviationP).matches()){
							String valeur=abreviationP.substring(0, abreviationP.length()-1);
							decoupageMotCorrection fgf= new decoupageMotCorrection(valeur, ResultatCorrection, AbreviationListeDesAbreviationsOriginaux);
							if(fgf.getCorrectionMot().isEmpty()){
								CorrectionPourLemotDelimiter.add(valeur);
							}
							else{
							CorrectionPourLemotDelimiter.addAll(fgf.getCorrectionMot());
							}
						
						}
						else {
						
						String ExpressionMajusculeOuMinusculeFinissantparPoint ="(([A-Z]*[0-9]*[A-Z]+[0-9]*[A-Z]+[0-9]*(\\.|\\/))|([a-zA-Z]*[0-9]*[a-zA-Z]+[0-9]*(\\/|\\.)))";
						Pattern patternAbbreviationmajusculeouPoint= Pattern.compile(ExpressionMajusculeOuMinusculeFinissantparPoint);
					//	String motPoint="(\\w*(\\/|\\.)+)";
						Matcher matcheurAbbreviationFinissantParPoint= patternAbbreviationmajusculeouPoint.matcher(abreviationP);
						//la correction que nous proposons pour le mot
						int traitementDupoint=1;
						HashMap<String, Set<String>> correctionDuMot =new HashMap<String, Set<String>>();
						
						if(!correctionDuMot.containsKey("0")){
							correctionDuMot.put("0", new HashSet<String>());
						}
						correctionDuMot.get("0").add(abreviationP);
						
						while(matcheurAbbreviationFinissantParPoint.find()){
							
							String SousMot=matcheurAbbreviationFinissantParPoint.group(1);
							//System.out.println("lacorrectionPoint : "+SousMot);
							Set<String>CorrectionSousMot=new HashSet<String>();
							int ttmtMot=0;
							
							if(ResultatCorrection.containsKey(SousMot)){
								CorrectionSousMot.addAll(ResultatCorrection.get(SousMot));
								ttmtMot++;
							}
							for(String abrv:AbreviationListeDesAbreviationsOriginaux.keySet()){
								if(AbreviationListeDesAbreviationsOriginaux.get(abrv).contains(SousMot)){
									if(ResultatCorrection.containsKey(abrv)){
									CorrectionSousMot.addAll(ResultatCorrection.get(abrv));
									}
									else{
										// code ajouté 11/12/2017
											CorrectionPourLemotDelimiter.add(abrv);
										
									}
									
									ttmtMot++;
									
								}
							}
							
							if(ttmtMot==0&& SousMot.endsWith(".")){
								ttmtMot++;
								
								String AbreviationSeco= SousMot.substring(0, SousMot.length()-1);
								
								if (AbreviationSeco.contains("."))
									System.out.println("toto");
								//Set<String> Corection= new HashSet<String>();
								decoupageMotCorrection decompose= new decoupageMotCorrection(AbreviationSeco, ResultatCorrection, AbreviationListeDesAbreviationsOriginaux);
								CorrectionSousMot.addAll(decompose.getCorrectionMot());
								//String b=Integer.toString(traitementDupoint);
//								if(!correctionDuMot.containsKey(b)){
//									correctionDuMot.put(b, new HashSet<String>());
//								}
//								correctionDuMot.get(b).addAll(Corection);
							}
							if(CorrectionSousMot.isEmpty()){
								CorrectionSousMot.add(SousMot.substring(0, SousMot.length()-1));
							}
							//int tttrr=1;
							for(String ab:CorrectionSousMot){
								
								String a=Integer.toString(traitementDupoint-1);
								
								if(correctionDuMot.containsKey(a)){
									Set<String> libes=correctionDuMot.get(a);
									for(String libe :libes){
										String libbd=libe.replaceAll(SousMot.substring(0, SousMot.length()-1)+"\\.", " "+ab+" ");
										String b=Integer.toString(traitementDupoint);
										
										if(!correctionDuMot.containsKey(b)){
											correctionDuMot.put(b, new HashSet<String>());
										}
										correctionDuMot.get(b).add(libbd);
										//System.out.println(SousMot+" remplacer par "+ab);
										//System.out.println(b+" mot finissant par point "+libbd);
									}
								}
								
							}
							traitementDupoint++;
							
							
						}
						
						HashMap<String, Set<String>> correctionDuMotFinal =new HashMap<String, Set<String>>();
						if(!correctionDuMotFinal.containsKey("0")){
							correctionDuMotFinal.put("0", new HashSet<String>());
						}
						//System.out.println("test de correction des mot finissant par point "+traitementDupoint);
						correctionDuMotFinal.get("0").addAll(correctionDuMot.get(Integer.toString(traitementDupoint-1)));
						String patternMotCommancantApresUnPoint="(((\\.|\\/)[a-zA-Z]+$)|((\\.|\\/)[a-zA-Z]+\\s))";
						//String patternMotCommancantApresUnPoint="((\\.[a-zA-Z]+$)|(\\.[a-zA-Z]+\\s))";
						Pattern patternautiliserMotCommancantApresUnPoint= Pattern.compile(patternMotCommancantApresUnPoint);
						Matcher matcheurMotCommancantParPointEtFinissantuneCHcar= patternautiliserMotCommancantApresUnPoint.matcher(abreviationP);
						Set<String> corrDuMot= new HashSet<String>();
						int motTrouve=1;
						int compteur=1;
						while(matcheurMotCommancantParPointEtFinissantuneCHcar.find()){
							String motAchanger=matcheurMotCommancantParPointEtFinissantuneCHcar.group(1);
							//System.out.println("mot commancant par un point a traiter "+motAchanger);
							if(ResultatCorrection.containsKey(motAchanger)){
								corrDuMot.addAll(ResultatCorrection.get(motAchanger));
								compteur++;
							}
							for(String abr:AbreviationListeDesAbreviationsOriginaux.keySet()){
								
								if(AbreviationListeDesAbreviationsOriginaux.get(abr).contains(motAchanger)){
									//System.out.println(motAchanger+" voiiiiiiiiiiiiiiiiiiii "+abr);
								//	Set<String> cor= new HashSet<String>();
									//System.out.println("il possède une correction "+abr);
									if(ResultatCorrection.containsKey(abr)){
										corrDuMot.addAll(ResultatCorrection.get(abr));
									}else{
										corrDuMot.add(abr);
									}
									compteur++;
								}
								else if(motAchanger.startsWith("/")&& AbreviationListeDesAbreviationsOriginaux.get(abr).contains(motAchanger.substring(1))){
									if(ResultatCorrection.containsKey(abr)){
										corrDuMot.addAll(ResultatCorrection.get(abr));
									}else{
										corrDuMot.add(abr);
									}
									compteur++;
								}
							}
									//System.out.println(motTrouve);					
							if(compteur==1){
								String MotSousMot= motAchanger.substring(1);
								//System.out.println("final des points "+MotSousMot);
								decoupageMotCorrection decom= new decoupageMotCorrection(MotSousMot, ResultatCorrection, AbreviationListeDesAbreviationsOriginaux);
								
								corrDuMot.addAll(decom.getCorrectionMot());
								
							}
							//System.out.println(corrDuMot);
							String MotSousMotFinal= motAchanger.substring(1);
							if(corrDuMot.size()==1&&corrDuMot.contains(null))
								corrDuMot.add(MotSousMotFinal);
								
							for(String ab:corrDuMot){						
								String a=Integer.toString(motTrouve-1);
								if(correctionDuMotFinal.containsKey(a)){
									Set<String> libes=correctionDuMotFinal.get(a);
									for(String libe :libes){
										//System.out.println("libelle a corriger : "+libe);
										String libbd=libe.replaceAll("\\b"+MotSousMotFinal+"\\b", " "+ab+" ");
										String b=Integer.toString(motTrouve);
										
										if(!correctionDuMotFinal.containsKey(b)){
											correctionDuMotFinal.put(b, new HashSet<String>());
										}
										correctionDuMotFinal.get(b).add(libbd);
										//System.out.println(b+" "+libbd);
									}
								}
								
							}
							motTrouve++;
							
						}
						//System.out.println("test d'erruer "+motTrouve);
						CorrectionPourLemotDelimiter.addAll(correctionDuMotFinal.get(Integer.toString(motTrouve-1)));
						
					}
						
					}
					if(CorrectionPourLemotDelimiter.isEmpty()){
						System.out.println(abreviationP+"        hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
						if((abreviationP.endsWith(".")||abreviationP.endsWith("\\/"))&&(abreviationP.startsWith(".")|abreviationP.startsWith("/"))){
							CorrectionPourLemotDelimiter.add(abreviationP.substring(1, abreviationP.length()-1));
							
						}
						else if(abreviationP.endsWith(".")||abreviationP.endsWith("\\/")){
						CorrectionPourLemotDelimiter.add(abreviationP.substring(0, abreviationP.length()-1));
						}
						else if(abreviationP.startsWith(".")|abreviationP.startsWith("/")){
							CorrectionPourLemotDelimiter.add(abreviationP.substring(1));
							
						}
						else{
							CorrectionPourLemotDelimiter.add(abreviationP);
							
						}
					}
					
					// pour chacune des traductions procédons à la corrections dans le libelle
					for(String ab:CorrectionPourLemotDelimiter){						
						String a=Integer.toString(cals-1);
						if(Correction.containsKey(a)){
							Set<String> libes=Correction.get(a);
							for(String libe :libes){
								if(abreviationP.equals(".")|abreviationP.equals("/")|abreviationP.equals(":")){
									String libbd=libe;
											String b=Integer.toString(cals);
									if(!Correction.containsKey(b)){
										Correction.put(b, new HashSet<String>());
									}
									Correction.get(b).add(libbd);
								}
								else if((abreviationP.endsWith(".")||abreviationP.endsWith("/")||abreviationP.endsWith(":"))&&(abreviationP.startsWith(".")|abreviationP.startsWith("/"))){
									String libbd=libe.replaceAll("\\b"+abreviationP.substring(1,abreviationP.length()-1)+"\\b", " "+ab+" ");
									System.out.println("libelle initial : "+libe);
									System.out.println("libelle final : "+libbd);
									System.out.println("abreviationP :  "+abreviationP+"   correction "+ab);
									String b=Integer.toString(cals);
									if(!Correction.containsKey(b)){
										Correction.put(b, new HashSet<String>());
									}
									Correction.get(b).add(libbd);
									
								}
								else if(abreviationP.endsWith(".")||abreviationP.endsWith("/")||abreviationP.endsWith(":")){
								String libbd=libe.replaceAll("\\b"+abreviationP.substring(0,abreviationP.length()-1)+"\\b", " "+ab+" ");
//								System.out.println("libelle initial : "+libe);
//								System.out.println("libelle final : "+libbd);
//								System.out.println("abreviationP :  "+abreviationP+"   correction "+ab);
								String b=Integer.toString(cals);
								if(!Correction.containsKey(b)){
									Correction.put(b, new HashSet<String>());
								}
								Correction.get(b).add(libbd);
								}
								else if(abreviationP.startsWith(".")|abreviationP.startsWith("/")){
									String libbd=libe.replaceAll("\\b"+abreviationP.substring(1)+"\\b", " "+ab+" ");
									String b=Integer.toString(cals);
									if(!Correction.containsKey(b)){
										Correction.put(b, new HashSet<String>());
									}
									Correction.get(b).add(libbd);
									
									
								}
								else{
									String libbd=libe.replaceAll("\\b"+abreviationP+"\\b", " "+ab+" ");
									String b=Integer.toString(cals);
									if(!Correction.containsKey(b)){
										Correction.put(b, new HashSet<String>());
									}
									Correction.get(b).add(libbd);
									
								}
								
								//System.out.println(b+" "+libbd);
							}
						}
						
					}
					cals++;
					
				
				}
				Set<String> libelleCorrigeFinal= new HashSet<String>();
				libelleCorrigeFinal.addAll(Correction.get(Integer.toString(cals-1)));
				//System.out.println(LibelleSansAccent);
				//System.out.println(libelleCorrigeFinal);
				if(!interfaceCorrige.containsKey(CodeInterface)){
					interfaceCorrige.put(CodeInterface, new HashSet<String>());
				}
				
				interfaceCorrige.get(CodeInterface).addAll(libelleCorrigeFinal);
				LesLibellesdistinct.addAll(libelleCorrigeFinal);
				
			}
		}
		
		
		
		String interfaceCorrection = "http://erias.org/InterfaceTerminology/TLAB/Correction";
		
		URI graphTerm_URI = connnex.getValueFactory().createURI(interfaceCorrection);
		
		System.out.println("les codes a inserrer "+interfaceCorrige.size());
		System.out.println("le nombre de libelle "+LesLibellesdistinct.size());
		
		
	//	Statement tmp = null;
		//int i=1;
		for(String Code: interfaceCorrige.keySet()){
		//	System.out.println("tester le code que ca retrouve "+Code);
			
		
			for(String libelles: interfaceCorrige.get(Code)){

			//	i++;
				String LibelleSansAc= libelles.replaceAll( "«", "(");
				String LibelleSansAcs= LibelleSansAc.replaceAll( "&","{");
				String LibelleSansAcsP= LibelleSansAcs.replaceAll("§","*");
				String LibelleSansAcsPP= LibelleSansAcsP.replaceAll(",","?" );
				String LibelleSansAcsPPP= LibelleSansAcsPP.replaceAll("@","[" );
				String LibelleSansAcsPPPP= LibelleSansAcsPPP.replaceAll("%","]");
				String LibelleSansAcsPPPPP= LibelleSansAcsPPPP.replaceAll( "£","+");
				String libells= LibelleSansAcsPPPPP.replaceAll("»",")");
				String libser="";
				for(String edr:libells.split("\\.")){
					libser=libser+edr;
				}
				
				
				String libelle=LibelleAvecEspaceCorrecte.getLibelleCorrect(libser);
				
				
				
				Statement stm = vf.createStatement(vf.createURI(Code), 
						vf.createURI(Prefix_rdfs + "label"),
						vf.createLiteral (libelle));
				lesSatement.add(stm);
				
				
				
			}
			
			
		}
		List<Statement> liste= new ArrayList<Statement>(lesSatement);
		System.out.println(liste.size());
		
		System.out.println("debut d'insertion");
		try {
			

			connnex.add(liste,graphTerm_URI);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("fin d'insertion");
		
		if (connnex != null) {
			try {
				connnex.close(); 
			} catch (final RepositoryException ignore) { 
				System.out.println("problème de fermeture de la connexion");
			}
		}
		
	}

}
