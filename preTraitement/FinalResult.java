package preTraitement;

import java.util.Set;

public class FinalResult {
	private String abreviation;
	private int liste;
	private Set<String> libelle;
	
	FinalResult(String a, Set<String> b){
		this.abreviation=a;
		this.liste=b.size();
		this.libelle=b;
	}
	
	
	public Object getabreviation(){
		return abreviation;
		
	}
	public int getNbrelibelle(){
		return liste;
		
	}
	public Set<String> getLibelles(){
		return libelle;
		
	}
	
	
	
	public boolean equals(FinalResult b){
		return abreviation.equals(b.getabreviation())&& liste==b.getNbrelibelle()&& libelle.equals(b.getLibelles());
	}
	
	
	
}
