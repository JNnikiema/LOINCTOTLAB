package preTraitement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import interactionRelationalDatabase.ConnexionBD;
import utilitaries.CoupleCorrection;
import utilitaries.LibelleAvecEspaceCorrecte;
/**
 * cette classe décrit le processus de correction de la terminologie d'interface en utilisant
 * le dictionnaire wikipedia 
 * @author erias
 *
 */
public class ProcessusDeCorrectionParWikipedia {

	HashMap<String, Set<String>> Result=new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> ResultCorrectionWikipediaLibelleIncorrect=new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> ResultCorrectionWikipedia=new HashMap<String, Set<String>>();

	/**
	 * constructeur processus de correction des mots delimiter par des espaces et qui correspondent 
	 * à des abreviation dans wikipedia
	 * @param codeInterface
	 */
	public ProcessusDeCorrectionParWikipedia(HashMap<String, List<String>> codeInterface){
		
		ConnexionBD df = new ConnexionBD(11);
//		String recuperationAbreviationCorrectionWIki = "Select abreviation, "
//				+ "traduction "
//				+ "from  accronymes_wikipedia";
		String recuperationAbreviationCorrectionWIki2 = "Select abreviation, "
				+ "traduction "
				+ "from  DictionnaireCreeWikipedia";
		
		// requ�te pr�par� pour recup�r� les concepts de la classification
				try {
				//	PreparedStatement ps = df.getconn().prepareStatement(recuperationAbreviationCorrectionWIki);
					//ex�cution de la requ�te
					PreparedStatement ps2 = df.getconn().prepareStatement(recuperationAbreviationCorrectionWIki2);
					
				//	ResultSet resultatAbreciation = ps.executeQuery();
					ResultSet resultatAbreciation2 = ps2.executeQuery();
					
//						while(resultatAbreciation.next()){
//							
//							if(!Result.containsKey(resultatAbreciation.getObject(1).toString())){
//								Result.put(resultatAbreciation.getObject(1).toString(), new HashSet<String>());
//							}
//							Result.get(resultatAbreciation.getObject(1).toString()).add(resultatAbreciation.getString(2));
//							}
						
						while(resultatAbreciation2.next()){
							
							if(!Result.containsKey(resultatAbreciation2.getObject(1).toString())){
								Result.put(resultatAbreciation2.getObject(1).toString(), new HashSet<String>());
							}
							Result.get(resultatAbreciation2.getObject(1).toString()).add(resultatAbreciation2.getString(2));
							}
				//	System.out.println("la liste a traiter"+listeAbreviation.size());
						
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//String resultatCorrection="codeInterface; libelleNonCorrige; libelleCorrige;NombreCorrection";
				
				for(String code:codeInterface.keySet()){
					
					for(String lib:codeInterface.get(code)){
						String LibelleSans = StringUtils.stripAccents(lib);
						String LibelleSansAc= LibelleSans.replaceAll("\\(", "« ");
						String LibelleSansAcs= LibelleSansAc.replaceAll("\\{", "& ");
						String LibelleSansAcsP= LibelleSansAcs.replaceAll("\\*", "§ ");
						String LibelleSansAcsPP= LibelleSansAcsP.replaceAll("\\?", " , ");
						String LibelleSansAcsPPP= LibelleSansAcsPP.replaceAll("\\[", " @ ");
						String LibelleSansAcsPPPP= LibelleSansAcsPPP.replaceAll("\\]", " % ");
						String LibelleSansAcsPPPPP= LibelleSansAcsPPPP.replaceAll("\\+", " £ ");
						String LibelleSansAccent= LibelleSansAcsPPPPP.replaceAll("\\)", " » ");
						//String LibelleSansAccent = StringUtils.stripAccents(lib);
						//System.out.println("avant : "+LibelleSansAccent);
				//		int nombreCorrection=0;
						CoupleCorrection libelleCorrecNbr= new CoupleCorrection(0, LibelleSansAccent);
						Set<CoupleCorrection> libbel=new HashSet<CoupleCorrection>();
						libbel.add(libelleCorrecNbr);
						String ExpressionMotGraceAuxEspaces="([^\\s]+\\.*)";
						Pattern patternMotdulibbeleGraceAespace= Pattern.compile(ExpressionMotGraceAuxEspaces);
						HashMap<String, Set<String>> Correction =new HashMap<String, Set<String>>();
						Matcher matcheurMotgraceAespace= patternMotdulibbeleGraceAespace.matcher(LibelleSansAccent);
						if(!Correction.containsKey("0")){
							Correction.put("0", new HashSet<String>());
						}
						Correction.get("0").add(LibelleSansAccent);
						int cals=1;
						System.out.println("libelle générateur d'erreur : "+lib);
						while(matcheurMotgraceAespace.find()){
							String abreviationP=matcheurMotgraceAespace.group(1);
							Set<String>CorrectionPourLemotDelimiter=new HashSet<String>();
							if(Result.containsKey(abreviationP)){
								CorrectionPourLemotDelimiter.addAll(Result.get(abreviationP));
								
							}
							else if(abreviationP.equals("/")||abreviationP.equals(".")){
								CorrectionPourLemotDelimiter.add(abreviationP);
							}
							else if((abreviationP.endsWith(".")||abreviationP.endsWith("/")||abreviationP.endsWith(":"))&&(abreviationP.startsWith(".")|abreviationP.startsWith("/"))){
								if(Result.containsKey(abreviationP.substring(1, abreviationP.length()-1))){
									CorrectionPourLemotDelimiter.addAll(Result.get(abreviationP.substring(1, abreviationP.length()-1)));
								}
								else{
									CorrectionPourLemotDelimiter.add(abreviationP.substring(1, abreviationP.length()-1));
								}
								
							}
							else if(abreviationP.endsWith(".")||abreviationP.endsWith("/")||abreviationP.endsWith(":")){
								System.out.println(abreviationP+" abreviationP");
								if(Result.containsKey(abreviationP.substring(0, abreviationP.length()-1))){
									CorrectionPourLemotDelimiter.addAll(Result.get(abreviationP.substring(0, abreviationP.length()-1)));
								}
								else{
									CorrectionPourLemotDelimiter.add(abreviationP.substring(0, abreviationP.length()-1));
								}
								
							}
							else if(abreviationP.startsWith(".")|abreviationP.startsWith("/")){
								
								if(Result.containsKey(abreviationP.substring(1))){
									CorrectionPourLemotDelimiter.addAll(Result.get(abreviationP.substring(1)));
									
								}
								else{
									CorrectionPourLemotDelimiter.add(abreviationP.substring(1));
								}
							
							}
							else{
								CorrectionPourLemotDelimiter.add(abreviationP);
							}
							
							
							
							System.out.println("CorrectionPourLemotDelimiter "+CorrectionPourLemotDelimiter);
							
							for(String ab:CorrectionPourLemotDelimiter){						
								String a=Integer.toString(cals-1);
								if(Correction.containsKey(a)){
									Set<String> libes=Correction.get(a);
									for(String libe :libes){
										if(abreviationP.equals(".")|abreviationP.equals("/")){
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
//										System.out.println("libelle initial : "+libe);
//										System.out.println("libelle final : "+libbd);
//										System.out.println("abreviationP :  "+abreviationP+"   correction "+ab);
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
						System.out.println(cals);
						Set<String> libelleCorrigeFinal= new HashSet<String>();
						System.out.println(Correction);
						if(Correction.keySet().size()==0){
							libelleCorrigeFinal.addAll(Correction.get("0"));
							
						}else{
							libelleCorrigeFinal.addAll(Correction.get(Integer.toString(cals-1)));
							
						}
						System.out.println(libelleCorrigeFinal);
					
						//System.out.println(LibelleSansAccent);
						//System.out.println(libelleCorrigeFinal);
						if(!ResultCorrectionWikipedia.containsKey(code)){
							ResultCorrectionWikipedia.put(code, new HashSet<String>());
						}
						
						for(String linb: libelleCorrigeFinal){
							
							//String libee=LibelleAvecEspaceCorrecte.getLibelleCorrect(linb);
							String libe=LibelleAvecEspaceCorrecte.getLibelleCorrect(linb);
							String LibelleSasAc= libe.replaceAll( "«", "(");
							String LibelleSasAcs= LibelleSasAc.replaceAll( "&","{");
							String LibelleSasAcsP= LibelleSasAcs.replaceAll("§","*");
							String LibelleSasAcsPP= LibelleSasAcsP.replaceAll(",","?" );
							String LibelleSasAcsPPP= LibelleSasAcsPP.replaceAll("@","[" );
							String LibelleSasAcsPPPP= LibelleSasAcsPPP.replaceAll("%","]");
							String LibelleSasAcsPPPPP= LibelleSasAcsPPPP.replaceAll( "£","+");
							String libells= LibelleSasAcsPPPPP.replaceAll("»",")");
							
							
							ResultCorrectionWikipedia.get(code).add(libells);
						}
					
						
					}
				}
				
				
	}
	/**
	 * liste de codes de la terminologie locale avec les corrections retrouvés grâce 
	 * @return
	 */
	public HashMap<String,Set<String>> getresultatCorrection(){
		return ResultCorrectionWikipedia;
	}

}
