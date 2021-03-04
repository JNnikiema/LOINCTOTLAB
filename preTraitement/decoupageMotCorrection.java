package preTraitement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class decoupageMotCorrection {
	Set<String> Correctiondd= new HashSet<String>();
	public decoupageMotCorrection(String mot, HashMap<String, Set<String>>corrige, HashMap<String, Set<String>>AbreviationListeDesAbreviationsOriginaux){

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
		//la correction que nous proposons pour le mot
		
	//	HashMap<String, Set<String>> correctionDuMot =new HashMap<String, Set<String>>();
		
		
		
		if(patternautiliserMotEntrcoupeMajuscule.matcher(mot).matches()){
			Correctiondd.addAll(CorrectionMotEntrecoupeMajuscule(mot, corrige, AbreviationListeDesAbreviationsOriginaux));
		}
		else if (patternautiliserMotEntrcoupeMajusculeDebutantParUneminuscule.matcher(mot).matches()) {
			Correctiondd.addAll(CorrectionMotEntrecoupeMajusculeDebutMinuscule(mot, corrige, AbreviationListeDesAbreviationsOriginaux));
		}
		else if(patternautiliserMotCommancantParUneMinusculeFinissantParMajuscule.matcher(mot).matches()){
			Correctiondd.addAll(CorrectionMotdebutMinusculeSuiteMajuscule(mot, corrige, AbreviationListeDesAbreviationsOriginaux));
		}
		else if(patternautiliserMotCommancantParUneMajusculeFinissantParminuscule.matcher(mot).matches()){
			Correctiondd.addAll(CorrectionMotdebutMajusculeSuiteMinuscule(mot, corrige, AbreviationListeDesAbreviationsOriginaux));
		}
		else if(patternautiliserpatternMotEntrecouperMajusculeEtMinuscule.matcher(mot).matches()) {
			CorrectionMotdebutMajusculeETMinuscule(mot, corrige, AbreviationListeDesAbreviationsOriginaux);
		}
		else{
			Correctiondd.add(mot);
		}
		
	
	}
	
public Set<String> CorrectionMotEntrecoupeMajuscule(String libelle, HashMap<String, Set<String>>corrige, HashMap<String, Set<String>>AbreviationListeDesAbreviationsOriginaux){
		
		
		HashMap<String, Set<String>> Correction =new HashMap<String, Set<String>>();
		String patternMotEntrcoupeMajuscule="([A-Z][a-z]+([A-Z][a-z]+)+)";
		Pattern patternautiliserMotEntrcoupeMajuscule= Pattern.compile(patternMotEntrcoupeMajuscule);
		String patternMotMajusculeDebut="([A-Z][a-z]+)";
		Pattern patternMotDebutMajuscule=Pattern.compile(patternMotMajusculeDebut);
	
		if(!Correction.containsKey("0")){
			Correction.put("0", new HashSet<String>());
		}
		Correction.get("0").add(libelle);
		Set<String> resultat=new HashSet<String>();
		
		Matcher motentrecoupe= patternMotDebutMajuscule.matcher(libelle);
		if(patternautiliserMotEntrcoupeMajuscule.matcher(libelle).matches()){
			int interation=0;
			while(motentrecoupe.find()){
				interation++;
				
				Set<String>Corrc=new HashSet<String>();
				String abrr=motentrecoupe.group(1);
				
				if(corrige.containsKey(abrr)){
					Corrc=corrige.get(abrr);
				}
				for(String abrev:AbreviationListeDesAbreviationsOriginaux.keySet()){
					if(AbreviationListeDesAbreviationsOriginaux.get(abrev).contains(abrr)){
						if(corrige.containsKey(abrev))
					Corrc.addAll(corrige.get(abrev));
						
					}
				}
				if(Corrc.isEmpty()){
					Corrc.add(abrr);
				}
				
				
				if(!Corrc.isEmpty()){
					
					for(String ab:Corrc){
						String a=Integer.toString(interation-1);
						
						if(Correction.containsKey(a)){
							
							Set<String> libes=Correction.get(a);
						
							for(String libe :libes){
								String libbd=libe.replaceAll(abrr, " "+ab+" ");
								
								
								String b=Integer.toString(interation);
								if(!Correction.containsKey(b)){
									Correction.put(b, new HashSet<String>());
								}
								
								Correction.get(b).add(libbd);
								//System.out.println(b+" "+libbd);
							}
						}
						
					}
					
				}
				
				
			}
			
			resultat.addAll(Correction.get(Integer.toString(interation)));
			
		}
		return resultat;
		
	}
	
	
	
	
	public Set<String> CorrectionMotEntrecoupeMajusculeDebutMinuscule(String libelle, HashMap<String, Set<String>>corrige, HashMap<String, Set<String>>AbreviationListeDesAbreviationsOriginaux){
		String patternMotCommancantParUneMinusculeMajuscule="([a-z]+[A-Z])";
		Pattern patternautiliserDebutMinuscule= Pattern.compile(patternMotCommancantParUneMinusculeMajuscule);
		Set<String> resultat=new HashSet<String>();
		String patternMotMajusculeDebut="([A-Z][a-z]+)";
		Pattern patternMotDebutMajuscule=Pattern.compile(patternMotMajusculeDebut);
		Matcher motentrecoupe= patternMotDebutMajuscule.matcher(libelle);
		HashMap<String, Set<String>> Correction =new HashMap<String, Set<String>>();
		if(!Correction.containsKey("0")){
			Correction.put("0", new HashSet<String>());
		}
		Correction.get("0").add(libelle);
		//int interation=0;
		int call=1;
		Matcher matcheurMotDebutantMisch= patternautiliserDebutMinuscule.matcher(libelle);
		while(matcheurMotDebutantMisch.find()){
			//interation++;
			String ah=matcheurMotDebutantMisch.group(1);
			String ag=ah.substring(0, ah.length()-1);
			
			//System.out.println("verification "+ag);
			Set<String>Corrc=new HashSet<String>();
			
			
			if(corrige.containsKey(ag)){
				Corrc=corrige.get(ag);
			}
			for(String abrev:AbreviationListeDesAbreviationsOriginaux.keySet()){
				if(AbreviationListeDesAbreviationsOriginaux.get(abrev).contains(ag)){
					if(corrige.containsKey(abrev))
					Corrc.addAll(corrige.get(abrev));
					
				}
			}
			if(Corrc.isEmpty()){
				Corrc.add(ag);
			}
			
			if(!Corrc.isEmpty()){
				//System.out.println("jn jn jn jn jn");
			for(String ab:Corrc){
				String a=Integer.toString(call-1);
				
				if(Correction.containsKey(a)){
					
					Set<String> libes=Correction.get(a);
				
					for(String libe :libes){
						String libbd=libe.replaceAll("\\b"+ag, " "+ab+" ");
						
						
						String b=Integer.toString(call);
						if(!Correction.containsKey(b)){
							Correction.put(b, new HashSet<String>());
						}
						
						Correction.get(b).add(libbd);
						//System.out.println(b+" "+libbd);
					}
				}
				
			}
			}
			
			call++;
			if(call>1){
				break;
			}
				
			}
		
		
		
		while(motentrecoupe.find()){
		//	interation++;
			
			
			Set<String>Corrc=new HashSet<String>();
			String abrr=motentrecoupe.group(1);
			
			if(corrige.containsKey(abrr)){
				Corrc=corrige.get(abrr);
			}
			for(String abrev:AbreviationListeDesAbreviationsOriginaux.keySet()){
				if(AbreviationListeDesAbreviationsOriginaux.get(abrev).contains(abrr)){
					if(corrige.containsKey(abrev))
					Corrc.addAll(corrige.get(abrev));
					
				}
			}
			if(Corrc.isEmpty()){
				Corrc.add(abrr);
			}
			
			
			if(!Corrc.isEmpty()){
				
				for(String ab:Corrc){
					
					String a=Integer.toString(call-1);
					//System.out.println("jn vois et dis moi "+a);
					
					if(Correction.containsKey(a)){
						
						
						Set<String> libes=Correction.get(a);
					
						for(String libe :libes){
							String libbd=libe.replaceAll(abrr, " "+ab+" ");
							
							
							String b=Integer.toString(call);
							if(!Correction.containsKey(b)){
								Correction.put(b, new HashSet<String>());
							}
							
							Correction.get(b).add(libbd);
							//System.out.println(b+" "+libbd);
						}
					}
					
				}
				
			}
			call++;
			
			
		}
		resultat.addAll(Correction.get(Integer.toString(call-1)));
		return resultat;
	
	}
		
	
			
	//
	public Set<String> CorrectionMotdebutMinusculeSuiteMajuscule(String libelle, HashMap<String, Set<String>>corrige, HashMap<String, Set<String>>AbreviationListeDesAbreviationsOriginaux){  
		HashMap<String, Set<String>> Correction =new HashMap<String, Set<String>>();
		Set<String> resultat=new HashSet<String>();
		if(!Correction.containsKey("0")){
			Correction.put("0", new HashSet<String>());
		}
		Correction.get("0").add(libelle);
		String capital="([A-Z][A-Z]+)";
		Pattern patternCapital=Pattern.compile(capital);
		
		String minuscule="([a-z]+)";
		Pattern patternMinuscule=Pattern.compile(minuscule);
		
		int cals=1;
		Matcher matcheurCapital= patternCapital.matcher(libelle);
		while(matcheurCapital.find()){
			String ag=matcheurCapital.group(1);
			//System.out.println("lamajuscule : "+ag);
			Set<String>Corrc=new HashSet<String>();
			
			if(corrige.containsKey(ag)){
				Corrc=corrige.get(ag);
			}
			for(String abrev:AbreviationListeDesAbreviationsOriginaux.keySet()){
				if(AbreviationListeDesAbreviationsOriginaux.get(abrev).contains(ag)){
					if(corrige.containsKey(abrev))
					Corrc.addAll(corrige.get(abrev));
					
				}
			}
			if(Corrc.isEmpty()){
				Corrc.add(ag);
			}
			
			
			if(!Corrc.isEmpty()){					
				for(String ab:Corrc){						
					String a=Integer.toString(cals-1);
					if(Correction.containsKey(a)){
						Set<String> libes=Correction.get(a);
						for(String libe :libes){
							String libbd=libe.replaceAll(ag, " "+ab+" ");
							String b=Integer.toString(cals);
							
							if(!Correction.containsKey(b)){
								Correction.put(b, new HashSet<String>());
							}
							
							Correction.get(b).add(libbd);
							//System.out.println(b+" "+libbd);
						}
					}
					
				}
			}
											
		cals++;
			if(cals>1)
				break;
		}
		String ExpressionDebutCapital="([A-Z][a-z]+)";
		Pattern patternautiliserDebutMajuscule= Pattern.compile(ExpressionDebutCapital);
		
		Matcher matcheurMotDebutantMajuscule= patternautiliserDebutMajuscule.matcher(libelle);
		Matcher matcheurMiscute= patternMinuscule.matcher(libelle);
		
		while(matcheurMotDebutantMajuscule.find()){
			String agj=matcheurMotDebutantMajuscule.group(1);
			
			//System.out.println("le minuscule debut majuscule : "+agj);
			Set<String>Corrc=new HashSet<String>();
			
			
			if(corrige.containsKey(agj)){
				Corrc=corrige.get(agj);
			}
			for(String abrev:AbreviationListeDesAbreviationsOriginaux.keySet()){
				if(AbreviationListeDesAbreviationsOriginaux.get(abrev).contains(agj)){
					if(corrige.containsKey(abrev))
					Corrc.addAll(corrige.get(abrev));
					
				}
			}
			if(Corrc.isEmpty()){
				Corrc.add(agj);
			}
			
			
			if(!Corrc.isEmpty()){	
				
				for(String ab:Corrc){						
					String a=Integer.toString(cals-1);
					if(Correction.containsKey(a)){
						Set<String> libes=Correction.get(a);
						for(String libe :libes){
							String libbd=libe.replaceAll("\\b"+agj, " "+ab+" ");
							//System.out.println(ab);
							String b=Integer.toString(cals);
							
							if(!Correction.containsKey(b)){
								Correction.put(b, new HashSet<String>());
							}
							
							Correction.get(b).add(libbd);
							//System.out.println(b+" "+libbd);
						}
					}
					
				}
			}
				
			cals++;
			if(cals>2)
				break;
		}
		
		
		if(cals<3){
			while(matcheurMiscute.find()){
				String agj=matcheurMiscute.group(1);
				//System.out.println("le minuscule : "+agj);
				Set<String>Corrc=new HashSet<String>();
				if(corrige.containsKey(agj)){
					Corrc=corrige.get(agj);
				}
				for(String abrev:AbreviationListeDesAbreviationsOriginaux.keySet()){
					if(AbreviationListeDesAbreviationsOriginaux.get(abrev).contains(agj)){
						if(corrige.containsKey(abrev))
					Corrc.addAll(corrige.get(abrev));
						
					}
				}
				if(Corrc.isEmpty()){
					Corrc.add(agj);
				}
				
				if(!Corrc.isEmpty()){					
					for(String ab:Corrc){						
						String a=Integer.toString(cals-1);
						if(Correction.containsKey(a)){
							Set<String> libes=Correction.get(a);
							for(String libe :libes){
								String libbd=libe.replaceAll("\\b"+agj, " "+ab+" ");
								String b=Integer.toString(cals);
								
								if(!Correction.containsKey(b)){
									Correction.put(b, new HashSet<String>());
								}
								
								Correction.get(b).add(libbd);
								//System.out.println(b+" "+libbd);
							}
						}
						
					}
				}
				
				
				
					cals++;
				if(cals>2)
				break;
			}
			
		}
		resultat.addAll(Correction.get(Integer.toString(cals-1)));
		return resultat;
		
	}
	
	public Set<String> CorrectionMotdebutMajusculeETMinuscule(String libelle, HashMap<String, Set<String>>corrige, HashMap<String, Set<String>>AbreviationListeDesAbreviationsOriginaux){  
		HashMap<String, Set<String>> Correction =new HashMap<String, Set<String>>();
		Set<String> resultat=new HashSet<String>();
		if(!Correction.containsKey("0")){
			Correction.put("0", new HashSet<String>());
		}
		Correction.get("0").add(libelle);
		String capital="([A-Z][A-Z]+)";
		Pattern patternCapital=Pattern.compile(capital);
		
		String minuscule="([a-z]+)";
		Pattern patternMinuscule=Pattern.compile(minuscule);
		int cals=1;
		Matcher matcheurCapital= patternCapital.matcher(libelle);
		while(matcheurCapital.find()){
			String ag=matcheurCapital.group(1);
			//System.out.println("lamajuscule : "+ag);
			Set<String>Corrc=new HashSet<String>();
			
			if(corrige.containsKey(ag)){
				Corrc=corrige.get(ag);
			}
			for(String abrev:AbreviationListeDesAbreviationsOriginaux.keySet()){
				if(AbreviationListeDesAbreviationsOriginaux.get(abrev).contains(ag)){
					if(corrige.containsKey(abrev))
					Corrc.addAll(corrige.get(abrev));
					
				}
			}
			if(Corrc.isEmpty()){
				Corrc.add(ag);
			}
			
			if(!Corrc.isEmpty()){					
				for(String ab:Corrc){						
					String a=Integer.toString(cals-1);
					if(Correction.containsKey(a)){
						Set<String> libes=Correction.get(a);
						for(String libe :libes){
							String libbd=libe.replaceAll(ag, " "+ab+" ");
							String b=Integer.toString(cals);
							
							if(!Correction.containsKey(b)){
								Correction.put(b, new HashSet<String>());
							}
							
							Correction.get(b).add(libbd);
							//System.out.println(b+" "+libbd);
						}
					}
					
				}
			}
											
		cals++;
		}
		//String ExpressionDebutCapital="([A-Z][a-z]+)";
		Matcher matcheurMiscute= patternMinuscule.matcher(libelle);
		
		while(matcheurMiscute.find()){
			String agj=matcheurMiscute.group(1);
			
			//System.out.println("le minuscule: "+agj);
			Set<String>Corrc=new HashSet<String>();
			
			
			if(corrige.containsKey(agj)){
				Corrc=corrige.get(agj);
			}
			for(String abrev:AbreviationListeDesAbreviationsOriginaux.keySet()){
				if(AbreviationListeDesAbreviationsOriginaux.get(abrev).contains(agj)){
					if(corrige.containsKey(abrev))
					Corrc.addAll(corrige.get(abrev));
					
				}
			}
			if(Corrc.isEmpty()){
				Corrc.add(agj);
			}
			
			
			if(!Corrc.isEmpty()){	
				
				for(String ab:Corrc){						
					String a=Integer.toString(cals-1);
					if(Correction.containsKey(a)){
						Set<String> libes=Correction.get(a);
						for(String libe :libes){
							String libbd=libe.replaceAll("\\b"+agj, " "+ab+" ");
							//System.out.println(ab);
							String b=Integer.toString(cals);
							
							if(!Correction.containsKey(b)){
								Correction.put(b, new HashSet<String>());
							}
							
							Correction.get(b).add(libbd);
							//System.out.println(b+" "+libbd);
						}
					}
					
				}
			}
				
			cals++;
			
		}
		
		
		resultat.addAll(Correction.get(Integer.toString(cals-1)));
		return resultat;
	}
	public Set<String> CorrectionMotdebutMajusculeSuiteMinuscule(String libelle, HashMap<String, Set<String>>corrige, HashMap<String, Set<String>>AbreviationListeDesAbreviationsOriginaux){  
		HashMap<String, Set<String>> Correction =new HashMap<String, Set<String>>();
		Set<String> resultat=new HashSet<String>();
		if(!Correction.containsKey("0")){
			Correction.put("0", new HashSet<String>());
		}
		Correction.get("0").add(libelle);
		String capital="([A-Z][A-Z]+)";
		Pattern patternCapital=Pattern.compile(capital);
		
		String minuscule="([a-z]+)";
		Pattern patternMinuscule=Pattern.compile(minuscule);
		
		int cals=1;
		Matcher matcheurCapital= patternCapital.matcher(libelle);
		while(matcheurCapital.find()){
			String ag=matcheurCapital.group(1);
			//System.out.println("lamajuscule : "+ag);
			Set<String>Corrc=new HashSet<String>();
			
			if(corrige.containsKey(ag)){
				Corrc=corrige.get(ag);
			}
			for(String abrev:AbreviationListeDesAbreviationsOriginaux.keySet()){
				if(AbreviationListeDesAbreviationsOriginaux.get(abrev).contains(ag)){
					if(corrige.containsKey(abrev))
					Corrc.addAll(corrige.get(abrev));
					
				}
			}
			if(Corrc.isEmpty()){
				Corrc.add(ag);
			}
			
			if(!Corrc.isEmpty()){					
				for(String ab:Corrc){						
					String a=Integer.toString(cals-1);
					if(Correction.containsKey(a)){
						Set<String> libes=Correction.get(a);
						for(String libe :libes){
							String libbd=libe.replaceAll(ag, " "+ab+" ");
							String b=Integer.toString(cals);
							
							if(!Correction.containsKey(b)){
								Correction.put(b, new HashSet<String>());
							}
							
							Correction.get(b).add(libbd);
							//System.out.println(b+" "+libbd);
						}
					}
					
				}
			}
											
		cals++;
			if(cals>1)
				break;
		}
		//String ExpressionDebutCapital="([A-Z][a-z]+)";
		Matcher matcheurMiscute= patternMinuscule.matcher(libelle);
		
		while(matcheurMiscute.find()){
			String agj=matcheurMiscute.group(1);
			
			//System.out.println("le minuscule: "+agj);
			Set<String>Corrc=new HashSet<String>();
			
			
			if(corrige.containsKey(agj)){
				Corrc=corrige.get(agj);
			}
			for(String abrev:AbreviationListeDesAbreviationsOriginaux.keySet()){
				if(AbreviationListeDesAbreviationsOriginaux.get(abrev).contains(agj)){
					if(corrige.containsKey(abrev))
					Corrc.addAll(corrige.get(abrev));
					
				}
			}
			if(Corrc.isEmpty()){
				Corrc.add(agj);
			}
			
			
			if(!Corrc.isEmpty()){	
				
				for(String ab:Corrc){						
					String a=Integer.toString(cals-1);
					if(Correction.containsKey(a)){
						Set<String> libes=Correction.get(a);
						for(String libe :libes){
							String libbd=libe.replaceAll("\\b"+agj, " "+ab+" ");
							//System.out.println(ab);
							String b=Integer.toString(cals);
							
							if(!Correction.containsKey(b)){
								Correction.put(b, new HashSet<String>());
							}
							
							Correction.get(b).add(libbd);
							//System.out.println(b+" "+libbd);
						}
					}
					
				}
			}
				
			cals++;
			if(cals>2)
				break;
		}
		
		
		resultat.addAll(Correction.get(Integer.toString(cals-1)));
		return resultat;
		
	}
	public Set<String> getCorrectionMot(){
		return Correctiondd;
	}
		

}
