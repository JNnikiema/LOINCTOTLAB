package utilitaries;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LibelleAvecEspaceCorrecte {
	
	public static String getLibelleCorrect(String Libelle){
		String ExpressionMotGraceAuxEspaces="([^\\s]+\\.*)";
	Pattern patternMotdulibbeleGraceAespace= Pattern.compile(ExpressionMotGraceAuxEspaces);
	
		Matcher matcheurMotLnbgraceAespace= patternMotdulibbeleGraceAespace.matcher(Libelle);
		
		String libee="";
		
		
		int i=0;
		while(matcheurMotLnbgraceAespace.find()){
			if(i==0){
				libee=libee+matcheurMotLnbgraceAespace.group(1);
			}
			else{
				libee=libee+" "+matcheurMotLnbgraceAespace.group(1);
			}
			i++;
		}
		return libee;
	}

}
