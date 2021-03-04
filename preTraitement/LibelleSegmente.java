package preTraitement;


import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class LibelleSegmente {
	
	public static String  getLibelleSegmente(String libelle){
		System.out.println(libelle);
		
		String ExpressionMotGraceAuxEspaces="([^\\s]+\\.*)";
		Pattern patternMotdulibbeleGraceAespace= Pattern.compile(ExpressionMotGraceAuxEspaces);
		
		String ExpressionDebutCapital="([A-Z][a-z]+)";
		Pattern patternautiliserDebutMajuscule= Pattern.compile(ExpressionDebutCapital);
		
		String capital="([A-Z][A-Z]+)";
		Pattern patternCapital=Pattern.compile(capital);
		
		String minuscule="([a-z]+)";
		Pattern patternMinuscule=Pattern.compile(minuscule);
		
		
		
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
		
		String patternMotCommancantParUneMinusculeMajuscule="([a-z]+[A-Z])";
		Pattern patternautiliserDebutMinuscule= Pattern.compile(patternMotCommancantParUneMinusculeMajuscule);
		
		String libelleFormer="";
		int jh=1;
		Matcher matcheurMotgraceAespace= patternMotdulibbeleGraceAespace.matcher(libelle);
		
		while(matcheurMotgraceAespace.find()){
			for(String edr: matcheurMotgraceAespace.group(1).split("\\.|\\/|:")){
			
			//System.out.println("???"+edr);
			String motTrouve="";
			if(patternautiliserMotEntrcoupeMajusculeDebutantParUneminuscule.matcher(edr).matches()){
				int call=1;
				Matcher matcheurMotDebutantMajuscule= patternautiliserDebutMajuscule.matcher(edr);
				String ab=" ";
				while(matcheurMotDebutantMajuscule.find()){
					
					 ab= matcheurMotDebutantMajuscule.group(1);
													
						
					
				}
				Matcher matcheurMotDebutantMisch= patternautiliserDebutMinuscule.matcher(edr);
				while(matcheurMotDebutantMisch.find()){
					String ah=matcheurMotDebutantMisch.group(1);
					String ag=ah.substring(0, ah.length()-1);
					motTrouve=ag+ab;
					
					call++;
					if(call>1)
						break;
				}
				
				
			}
			else if(patternautiliserMotCommancantParUneMajusculeFinissantParminuscule.matcher(edr).matches()){
				int cals=1;
				Matcher matcheurCapital= patternCapital.matcher(edr);
				Matcher matcheurMiscute= patternMinuscule.matcher(edr);
				String ag="";
				while(matcheurCapital.find()){
					 ag=matcheurCapital.group(1);
					
						cals++;
					if(cals>1)
						break;
				}
				String ad="";
				while(matcheurMiscute.find()){
					 ad=matcheurMiscute.group(1);
					
						cals++;
					if(cals>2)
						break;
				}
				motTrouve=ag+" "+ad;
				
			}
			else if(patternautiliserMotCommancantParUneMinusculeFinissantParMajuscule.matcher(edr).matches()){
				int cals=1;
				Matcher matcheurCapital= patternCapital.matcher(edr);
				Matcher matcheurMiscute= patternMinuscule.matcher(edr);
				Matcher matcheurMotDebutantMajuscule= patternautiliserDebutMajuscule.matcher(edr);
				String ag="";
				while(matcheurCapital.find()){
					 ag=matcheurCapital.group(1);
					
					cals++;
					if(cals>1)
						break;
				}
				String ad="";
				while(matcheurMotDebutantMajuscule.find()){
					 ad=matcheurMotDebutantMajuscule.group(1);
					
					
					
					cals++;
					if(cals>2)
						break;
				}
				if(cals<3){
					while(matcheurMiscute.find()){
						 ad=matcheurMiscute.group(1);
					
						cals++;
						if(cals>2)
						break;
					}
				}
				motTrouve=ad+" "+ag;
				
				
			}
			else if(patternautiliserMotEntrcoupeMajuscule.matcher(edr).matches()){
				Matcher matcheurMotDebutantMajuscule= patternautiliserDebutMajuscule.matcher(edr);
				int uy=1;
				while(matcheurMotDebutantMajuscule.find()){
					
					String ab= matcheurMotDebutantMajuscule.group(1);
					if(uy==1){
						motTrouve=ab;
						uy++;
					}
					else{
						motTrouve=motTrouve+" "+ab;
					}
					
					
				}
			}
			else if(patternautiliserpatternMotEntrecouperMajusculeEtMinuscule.matcher(edr).matches()) {
				Matcher matcheurMotDebutantMajuscule= patternCapital.matcher(edr);
				Matcher matecherMotMinuscule =patternMinuscule.matcher(edr);
				int uy=1;
				HashMap<String, String> resultat = new HashMap<String, String>();
				while(matcheurMotDebutantMajuscule.find()){
					
					String a=matcheurMotDebutantMajuscule.group(1);
					if(!resultat.containsKey(Integer.toString(uy))) {
						resultat.put(Integer.toString(uy), a);
					}
					uy++;
				}
				int uv=1;
				HashMap<String, String> resultat2 = new HashMap<String, String>();
				
				while(matecherMotMinuscule.find()){
					
				//	System.out.println("a voir et rerevoir "+matecherMotMinuscule.group(1));
				
					String b=matecherMotMinuscule.group(1);
					if(!resultat2.containsKey(Integer.toString(uv))) {
						resultat2.put(Integer.toString(uv), b);
					}
					uv++;
				
				}
				int v=1;
				for(String cod:resultat.keySet()) {
					if(v==1) {
						if(resultat2.containsKey(cod)) {
							motTrouve= resultat.get(cod)+" "+resultat2.get(cod);
						}
						else {
							motTrouve= resultat.get(cod);
						}
						
					}
					else {
						if(resultat2.containsKey(cod)) {
						motTrouve= motTrouve+" "+resultat.get(cod)+" "+resultat2.get(cod);
					
						}
						else {
							motTrouve= motTrouve+" "+resultat.get(cod);
						}
					}
					v++;
				}
				System.out.println("tes test avoir "+motTrouve);
			}
			else {							
				
				motTrouve=edr;
		}
		if(jh==1){
			libelleFormer=motTrouve;
			jh++;
		}
		else{
			libelleFormer=libelleFormer+" "+motTrouve;
		}
		}
		
	}
		return libelleFormer;
		
	}

}
