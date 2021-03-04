package utilitaries;


public class LOINCDataBaseModel {
	private String CodeLoinc;
	private String Component;
	private String grandeur;
	private String temps;
	private String Systeme;
	private String Echelle;
	private String methode;
	private String classe;
	public LOINCDataBaseModel(
	String Loinc_NUM,
	String LoinComponent,
	String Property,
	String Time_aspect,
	String System,
	String Scale,
	String method,
	String ClassName){
		
		CodeLoinc=Loinc_NUM;
		Component=LoinComponent;
		grandeur=Property;
		temps=Time_aspect;
		Systeme=System;
		Echelle=Scale;
		methode=method;
		classe=ClassName;
		
	}
	public String getCodeLoinc(){
		return CodeLoinc;
	}
	public String getComponent(){
		return Component;
	}
	public String getgrandeur(){
		return grandeur;
	}
	public String gettemps(){
		return temps;
	}
	public String getSysteme(){
		return Systeme;
	}
	public String getEchelle(){
		return Echelle;
	}
	public String getmethode(){
		return methode;
	}
	public String getclasse(){
		return classe;
	}
	public boolean equals(Object o_) {
		LOINCDataBaseModel o = (LOINCDataBaseModel) o_;

	      return (o.getCodeLoinc().equals(CodeLoinc) && o.getComponent().equals(Component)&&o.getclasse().equals(classe)&&o.getEchelle().equals(Echelle)&&o.getgrandeur().equals(grandeur)&&o.getmethode().equals(methode)&&o.getSysteme().equals(Systeme)&&o.gettemps().equals(temps));
	   }
	
	

}
