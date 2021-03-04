package loincRDFconstruction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import interactionRelationalDatabase.ConnexionBD;
import interactionTripleStore.RepositoryFactory;
import utilitaries.LOINCDataBaseModel;

public class FrenchLOINC {
	
	public static void main(String[] args) throws SQLException, RepositoryException {
		
	ConnexionBD loinc2019DataBase= new ConnexionBD(1);
		
	String query= "SELECT * FROM frenchLoinc";
		
	PreparedStatement LOINCFrenchLoinc = loinc2019DataBase.getconn().prepareStatement(query);
	ResultSet FrenchLoincresultat = LOINCFrenchLoinc.executeQuery();
	System.out.println("query obtain");
	Set<String> Loinc_NUM= new HashSet<String>();
	Set<String> Component= new HashSet<String>();
	Set<String> Property= new HashSet<String>();
	Set<String> Time_aspect= new HashSet<String>();
	Set<String> SystemLOINC= new HashSet<String>();
	Set<String> Scale= new HashSet<String>();
	Set<String> method= new HashSet<String>();
	Set<String> ClassName= new HashSet<String>();

	
	
	HashMap<String, Set<String>> LoincAndComponent= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> LoincAndProperty= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> LoincAndTime_aspect= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> LoincAndSystem= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> LoincAndScale= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> LoincAndmethod= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> LoincAndClassName= new HashMap<String, Set<String>>();
	
    HashMap<String, Set<String>> LoincAndComponentMAp= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> LoincAndPropertyMAp= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> LoincAndTime_aspectMAp= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> LoincAndSystemMAp= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> LoincAndScaleMAp= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> LoincAndmethodMAp= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> LoincAndClassNameMAp= new HashMap<String, Set<String>>();

	Set<LOINCDataBaseModel> ResultatFrenchLoinc= new HashSet<LOINCDataBaseModel>();
	System.out.println("get the results");

	
	while(FrenchLoincresultat.next()){
		String code=FrenchLoincresultat.getString(1).toLowerCase();
		String Composant=FrenchLoincresultat.getString(2).toLowerCase();
		
		Loinc_NUM.add(code);
		if(Composant != null && !Composant.isEmpty()&& !Composant.equals("null") && !Composant.equals("-")) {
		Component.add(Composant);		
		// Couple coupleCopo= new Couple(code, Composant);
		// LoincAndComponent.add(coupleCopo);
		 
		 if(!LoincAndComponent.containsKey(Composant)) {
			 LoincAndComponent.put(Composant, new HashSet<String>());
		 }
		 LoincAndComponent.get(Composant).add(code);
		
		 
		 
		 if(!LoincAndComponentMAp.containsKey(code)) {
			 LoincAndComponentMAp.put(code, new HashSet<String>());
		 }
		 LoincAndComponentMAp.get(code).add(Composant);
		
		
		}
		
		String propriete=FrenchLoincresultat.getString(3).toLowerCase();
		
		if(propriete != null && !propriete.isEmpty()&& !propriete.equals("null") && !propriete.equals("-")) {
		Property.add(propriete);
		//Couple couplePro= new Couple(code, propriete);
		//LoincAndProperty.add(couplePro);
		
		 if(!LoincAndProperty.containsKey(propriete)) {
			 LoincAndProperty.put(propriete, new HashSet<String>());
		 }
		 LoincAndProperty.get(propriete).add(code);
		
		
		
		 if(!LoincAndPropertyMAp.containsKey(code)) {
			 LoincAndPropertyMAp.put(code, new HashSet<String>());
		 }
		LoincAndPropertyMAp.get(code).add(propriete);
		}
		
		
		String temps =FrenchLoincresultat.getString(4).toLowerCase();
		if(temps != null && !temps.isEmpty()&& !temps.equals("null") && !temps.equals("-")) {
		Time_aspect.add(temps);
	//	Couple coupleTime= new Couple(code, temps);
	//	LoincAndTime_aspect.add(coupleTime);
		
		
		if(!LoincAndTime_aspect.containsKey(temps)) {
			LoincAndTime_aspect.put(temps, new HashSet<String>());
		 }
		LoincAndTime_aspect.get(temps).add(code);
		
		 if(!LoincAndTime_aspectMAp.containsKey(code)) {
			 LoincAndTime_aspectMAp.put(code, new HashSet<String>());
		 }
		LoincAndTime_aspectMAp.get(code).add(temps);
		
		}
		
		String systemeL=FrenchLoincresultat.getString(5).toLowerCase();
		if(systemeL != null && !systemeL.isEmpty()&& !systemeL.equals("null") && !systemeL.equals("-")) {		
		SystemLOINC.add(systemeL);
	//	Couple coupleSystem= new Couple(code, systemeL);
	//	LoincAndSystem.add(coupleSystem);
		
		
		if(!LoincAndSystem.containsKey(systemeL)) {
			LoincAndSystem.put(systemeL, new HashSet<String>());
		 }
		LoincAndSystem.get(systemeL).add(code);
		
		 if(!LoincAndSystemMAp.containsKey(code)) {
			 LoincAndSystemMAp.put(code, new HashSet<String>());
		 }
		LoincAndSystemMAp.get(code).add(systemeL);
		}
		
		String echelle=FrenchLoincresultat.getString(6).toLowerCase();
		//System.out.println("echelle our echelle *"+echelle+"*");
		if(echelle != null && !echelle.isEmpty()&& !echelle.equals("null") && !echelle.equals("-")) {
		Scale.add(echelle);
		//Couple coupleScale= new Couple(code, echelle);
		//LoincAndScale.add(coupleScale);
		
		
		if(!LoincAndScale.containsKey(echelle)) {
			LoincAndScale.put(echelle, new HashSet<String>());
		 }
		LoincAndScale.get(echelle).add(code);
		
		 if(!LoincAndScaleMAp.containsKey(code)) {
			 LoincAndScaleMAp.put(code, new HashSet<String>());
		 }
		LoincAndScaleMAp.get(code).add(echelle);
		
		}
		
		String metho=FrenchLoincresultat.getString(7).toLowerCase();
		
		if(metho != null && !metho.isEmpty() && !metho.equals("null") && !metho.equals("-")) {
		method.add(metho);
	//	Couple coupleMethod= new Couple(code, metho);
	//	LoincAndmethod.add(coupleMethod);
		
		
		if(!LoincAndmethod.containsKey(metho)) {
			LoincAndmethod.put(metho, new HashSet<String>());
		 }
		LoincAndmethod.get(metho).add(code);
		
		
		 if(!LoincAndmethodMAp.containsKey(code)) {
			 LoincAndmethodMAp.put(code, new HashSet<String>());
		 }
		LoincAndmethodMAp.get(code).add(metho);
		//System.out.println(FrenchLoincresult.getString(7).toLowerCase());
		}
		String clas=FrenchLoincresultat.getString(8).toLowerCase();
		if(clas != null && !clas.isEmpty() && !clas.equals("null") && !clas.equals("-")) {
		ClassName.add(clas);
	//	Couple coupleClassName= new Couple(code, clas);
	//	LoincAndClassName.add(coupleClassName);
		
		
		if(!LoincAndClassName.containsKey(clas)) {
			LoincAndClassName.put(clas, new HashSet<String>());
		 }
		LoincAndClassName.get(clas).add(code);
		
		
		 if(!LoincAndClassNameMAp.containsKey(code)) {
			 LoincAndClassNameMAp.put(code, new HashSet<String>());
		 }
		LoincAndClassNameMAp.get(code).add(clas);
		}
		String un =FrenchLoincresultat.getString(1).toLowerCase();
		String deux =FrenchLoincresultat.getString(2).toLowerCase();
		String trois =FrenchLoincresultat.getString(3).toLowerCase();
		String quatre =FrenchLoincresultat.getString(4).toLowerCase();
		String cinq =FrenchLoincresultat.getString(5).toLowerCase();
		String six =FrenchLoincresultat.getString(6).toLowerCase();
		String sept =FrenchLoincresultat.getString(7).toLowerCase();
		String huit =FrenchLoincresultat.getString(8).toLowerCase();
		
		String un1 ="";
		String deux2 ="";
		String trois3 ="";
		String quatre4 ="";
		String cinq5 ="";
		String six6 ="";
		String sept7 ="";
		String huit8 ="";
		if(un != null && !un.isEmpty() && !un.equals("null") && !un.equals("-")) {
		un1=un;
		}
		if(deux != null && !deux.isEmpty() && !deux.equals("null") && !deux.equals("-")) {
			deux2=deux;
			}
		if(trois != null && !trois.isEmpty() && !trois.equals("null") && !trois.equals("-")) {
			trois3=trois;
			}
		if(quatre != null && !quatre.isEmpty() && !quatre.equals("null") && !quatre.equals("-")) {
			quatre4=quatre;
			}
		if(cinq != null && !cinq.isEmpty() && !cinq.equals("null") && !cinq.equals("-")) {
			cinq5=cinq;
			}
		if(six != null && !six.isEmpty() && !six.equals("null") && !six.equals("-")) {
			six6=six;
			}
		if(sept != null && !sept.isEmpty() && !sept.equals("null") && !sept.equals("-")) {
			sept7=sept;
			}
		if(huit != null && !huit.isEmpty() && !huit.equals("null") && !huit.equals("-")) {
			huit8=huit;
			}
		
		
		;
		LOINCDataBaseModel ligne= new LOINCDataBaseModel(
				 un1,
				 deux2,
				 trois3,
				 quatre4,
				 cinq5,
				 six6,
				 sept7,
				 huit8
				);
		
		ResultatFrenchLoinc.add(ligne);

		
	}
	HashMap<String, Set<String>> ComponentAndSynonimies= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> PropertyAndSynonimies= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> TimeAndSynonimies= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> SystemAndSynonimies= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> ScaleAndSynonimies= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> methodAndSynonimies= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> ClassNameAndSynonimies= new HashMap<String, Set<String>>();
	System.out.println("creation of couple of part and LOINC concepts");
	
	

	
	
	for(String composantAndLC: LoincAndComponent.keySet()) {
		//String composant=composantAndLC.y;
		if(!ComponentAndSynonimies.containsKey(composantAndLC)) {
			ComponentAndSynonimies.put(composantAndLC, new HashSet<String>());
		}
		
		for(String loincde: LoincAndComponent.get(composantAndLC)){
		
		ComponentAndSynonimies.get(composantAndLC).addAll(LoincAndComponentMAp.get(loincde));
		for(String syn:LoincAndComponentMAp.get(loincde)) {
			if(!ComponentAndSynonimies.containsKey(syn)) {
				ComponentAndSynonimies.put(syn, new HashSet<String>());
			}
			ComponentAndSynonimies.get(syn).add(composantAndLC);
			
		}
	}
			
		
	}
//	System.out.println(" rajout ");
//	for(String compoen:ComponentAndSynonimies.keySet()) {
//		for(String syn: ComponentAndSynonimies.get(compoen)) {
//			ComponentAndSynonimies.get(compoen).addAll(ComponentAndSynonimies.get(syn));
//		}
//	}
	System.out.println("creation of couple of part and LOINC concepts: composant fini");

	
	for(String PropertyAndLC: LoincAndProperty.keySet()) {
		
		//String Properyry=PropertyAndLC.y;
		if(!PropertyAndSynonimies.containsKey(PropertyAndLC)) {
			PropertyAndSynonimies.put(PropertyAndLC, new HashSet<String>());
		}
	for(String loincde:LoincAndProperty.get(PropertyAndLC)) {
		
		
		PropertyAndSynonimies.get(PropertyAndLC).addAll(LoincAndPropertyMAp.get(loincde));
		for(String syn:LoincAndPropertyMAp.get(loincde)) {
			if(!PropertyAndSynonimies.containsKey(syn)) {
				PropertyAndSynonimies.put(syn, new HashSet<String>());
			}
			PropertyAndSynonimies.get(syn).add(PropertyAndLC);			
			
		}
	}
			
		
	}
	
//	System.out.println(" rajout ");
//	for(String pro:PropertyAndSynonimies.keySet()) {
//		Set<String> synonimes= new HashSet<String>();
//		for(String syn: PropertyAndSynonimies.get(pro)) {
//			System.out.println("debit"+syn);
//			synonimes.addAll(PropertyAndSynonimies.get(syn));
//		}
//		
//	}
	for(String TimeAndLC: LoincAndTime_aspect.keySet()) {
		//String Time=TimeAndLC.y;
		if(!TimeAndSynonimies.containsKey(TimeAndLC)) {
			TimeAndSynonimies.put(TimeAndLC, new HashSet<String>());
		}
		for(String loincde:LoincAndTime_aspect.get(TimeAndLC)) {
		
	TimeAndSynonimies.get(TimeAndLC).addAll(LoincAndTime_aspectMAp.get(loincde));
	for(String syn:LoincAndTime_aspectMAp.get(loincde)) {
		if(!TimeAndSynonimies.containsKey(syn)) {
			TimeAndSynonimies.put(syn, new HashSet<String>());
		}
		TimeAndSynonimies.get(syn).add(TimeAndLC);
		
	}
		}
		
			
		
	}
//	System.out.println(" rajout ");
//	for(String tim:TimeAndSynonimies.keySet()) {
//		for(String syn: TimeAndSynonimies.get(tim)) {
//			TimeAndSynonimies.get(tim).addAll(TimeAndSynonimies.get(syn));
//		}
//	}
//	

	for(String SystemAndLC: LoincAndSystem.keySet()) {
		//String syst=SystemAndLC.y;
		
		if(!SystemAndSynonimies.containsKey(SystemAndLC)) {
			SystemAndSynonimies.put(SystemAndLC, new HashSet<String>());
		}
		for(String loincde:LoincAndSystem.get(SystemAndLC)) {
		
		SystemAndSynonimies.get(SystemAndLC).addAll(LoincAndSystemMAp.get(loincde));
		for(String syn:LoincAndSystemMAp.get(loincde)) {
			if(!SystemAndSynonimies.containsKey(syn)) {
				SystemAndSynonimies.put(syn, new HashSet<String>());
			}
			SystemAndSynonimies.get(syn).add(SystemAndLC);
			
		}
		}
			
	}
//	System.out.println(" rajout ");
//	for(String sys:SystemAndSynonimies.keySet()) {
//		for(String syn: SystemAndSynonimies.get(sys)) {
//			SystemAndSynonimies.get(sys).addAll(SystemAndSynonimies.get(syn));
//		}
//	}

	for(String ScaleAndLC: LoincAndScale.keySet()) {
		//String scale=ScaleAndLC.y;
		if(!ScaleAndSynonimies.containsKey(ScaleAndLC)) {
			ScaleAndSynonimies.put(ScaleAndLC, new HashSet<String>());
		}
		for(String loincde:LoincAndScale.get(ScaleAndLC)) {
		
		ScaleAndSynonimies.get(ScaleAndLC).addAll(LoincAndScaleMAp.get(loincde));
		for(String syn:LoincAndScaleMAp.get(loincde)) {
			if(!ScaleAndSynonimies.containsKey(syn)) {
				ScaleAndSynonimies.put(syn, new HashSet<String>());
			}
			ScaleAndSynonimies.get(syn).add(ScaleAndLC);
			
		}
		}
			
	}
	System.out.println("ScaleAndSynonimies "+ScaleAndSynonimies);
//	System.out.println(" rajout ");
//	for(String scal:ScaleAndSynonimies.keySet()) {
//		for(String syn: ScaleAndSynonimies.get(scal)) {
//			ScaleAndSynonimies.get(scal).addAll(ScaleAndSynonimies.get(syn));
//		}
//	}

	for(String MethodAndLC: LoincAndmethod.keySet()) {
		//String mmethd=MethodAndLC.y;
		if(!methodAndSynonimies.containsKey(MethodAndLC)) {
			methodAndSynonimies.put(MethodAndLC, new HashSet<String>());
		}
		for(String loincde:LoincAndmethod.get(MethodAndLC)){
		
		methodAndSynonimies.get(MethodAndLC).addAll(LoincAndmethodMAp.get(loincde));
		for(String syn:LoincAndmethodMAp.get(loincde)) {
			if(!methodAndSynonimies.containsKey(syn)) {
				methodAndSynonimies.put(syn, new HashSet<String>());
			}
			methodAndSynonimies.get(syn).add(MethodAndLC);
			
		}
		}
			
		
	}
//	System.out.println(" rajout ");
//	for(String meh:methodAndSynonimies.keySet()) {
//		for(String syn: methodAndSynonimies.get(meh)) {
//			methodAndSynonimies.get(meh).addAll(methodAndSynonimies.get(syn));
//		}
//	}
	
	for(String ClassAndLC: LoincAndClassName.keySet()) {
		//String ClassR=ClassAndLC.y;
		if(!ClassNameAndSynonimies.containsKey(ClassAndLC)) {
			ClassNameAndSynonimies.put(ClassAndLC, new HashSet<String>());
		}
		for(String loincde:LoincAndClassName.get(ClassAndLC)) {

		ClassNameAndSynonimies.get(ClassAndLC).addAll(LoincAndClassNameMAp.get(loincde));
		
		for(String syn:LoincAndClassNameMAp.get(loincde)) {
			if(!ClassNameAndSynonimies.containsKey(syn)) {
				ClassNameAndSynonimies.put(syn, new HashSet<String>());
			}
			ClassNameAndSynonimies.get(syn).add(ClassAndLC);
			
		}
		}
			
				
		
		
	}
//	System.out.println(" rajout ");
//	for(String Cls:ClassNameAndSynonimies.keySet()) {
//		for(String syn: ClassNameAndSynonimies.get(Cls)) {
//			ClassNameAndSynonimies.get(Cls).addAll(ClassNameAndSynonimies.get(syn));
//		}
//	}
	
	Set<String> Analytes= new HashSet<String>();
	Set<String> challenge= new HashSet<String>();
	Set<String> Ajustement= new HashSet<String>();
	
	HashMap<String, Set<String>> AnalytesLNC= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> challengeLNC= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> AjustementLNC= new HashMap<String, Set<String>>();
	
	HashMap<String, Set<String>> AnalytesLNCMap= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> challengeLNCMap= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> AjustementLNCMap= new HashMap<String, Set<String>>();

	
	HashMap<String, Set<String>> AnalytesAndSynonimies= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> challengeAndSynonimies= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>> AjustementAndSynonimies= new HashMap<String, Set<String>>();
	System.out.println("creation of the model of component");
	
	for(LOINCDataBaseModel ligne : ResultatFrenchLoinc){
		// description des analytes
		HashMap<String, Set<String>>structure=getComposantDescription(ligne.getComponent());
		if(structure.containsKey(Integer.toString(1))){
			
			Analytes.addAll(structure.get(Integer.toString(1)));
			for(String an:structure.get(Integer.toString(1))) {
				//Couple anC= new Couple(ligne.getCodeLoinc(),an);
			//AnalytesLNC.add(anC);
				
				if(!AnalytesLNC.containsKey(an)) {
					AnalytesLNC.put(an, new HashSet<String>());
				 }
				AnalytesLNC.get(an).add(ligne.getCodeLoinc());
			
				if(!AnalytesLNCMap.containsKey(ligne.getCodeLoinc())) {
					AnalytesLNCMap.put(ligne.getCodeLoinc(), new HashSet<String>());
				 }
				AnalytesLNCMap.get(ligne.getCodeLoinc()).add(an);
			
			}
			
		}
		if(structure.containsKey(Integer.toString(2))){
			Set<String> chall= structure.get(Integer.toString(2));
			if(!chall.isEmpty()) {
				for(String ch:chall) {
					if(ch !=null &&!ch.isEmpty()) {
			challenge.add(ch);
			//for(String ch:structure.get(Integer.toString(2))) {
				//Couple chC= new Couple(ligne.getCodeLoinc(),ch);
				//challengeLNC.add(chC);
				
				if(!challengeLNC.containsKey(ch)) {
					challengeLNC.put(ch, new HashSet<String>());
				 }
				challengeLNC.get(ch).add(ligne.getCodeLoinc());
				
				if(!challengeLNCMap.containsKey(ligne.getCodeLoinc())) {
					challengeLNCMap.put(ligne.getCodeLoinc(), new HashSet<String>());
				 }
				challengeLNCMap.get(ligne.getCodeLoinc()).add(ch);
					
			}
			}
			}
		}
		if(structure.containsKey(Integer.toString(3))){
			Ajustement.addAll(structure.get(Integer.toString(3)));
			for(String aj:structure.get(Integer.toString(3))) {
				//Couple ajC= new Couple(ligne.getCodeLoinc(),aj);
				//AjustementLNC.add(ajC);
				
				if(!AjustementLNC.containsKey(aj)) {
					AjustementLNC.put(aj, new HashSet<String>());
				 }
				AjustementLNC.get(aj).add(ligne.getCodeLoinc());
				
				if(!AjustementLNCMap.containsKey(ligne.getCodeLoinc())) {
					AjustementLNCMap.put(ligne.getCodeLoinc(), new HashSet<String>());
				 }
				AjustementLNCMap.get(ligne.getCodeLoinc()).add(aj);
			}
		}
	}
	for(String AnalyteAndLC: AnalytesLNC.keySet()) {
		//String Analyte=AnalyteAndLC.y;
		if(!AnalytesAndSynonimies.containsKey(AnalyteAndLC)) {
			AnalytesAndSynonimies.put(AnalyteAndLC, new HashSet<String>());
		}
		for(String loincde:AnalytesLNC.get(AnalyteAndLC)) {
		
		AnalytesAndSynonimies.get(AnalyteAndLC).addAll(AnalytesLNCMap.get(loincde));
		for(String syn:AnalytesLNCMap.get(loincde)) {
			if(!AnalytesAndSynonimies.containsKey(syn)) {
				AnalytesAndSynonimies.put(syn, new HashSet<String>());
			}
			AnalytesAndSynonimies.get(syn).add(AnalyteAndLC);
			
		}
		}
			
		
	}
//	System.out.println(" rajout ");
//	for(String An:AnalytesAndSynonimies.keySet()) {
//		for(String syn: AnalytesAndSynonimies.get(An)) {
//			AnalytesAndSynonimies.get(An).addAll(AnalytesAndSynonimies.get(syn));
//		}
//	}
	for(String challengeAndLC: challengeLNC.keySet()) {
		//String chalenge=challengeAndLC.y;
		if(!challengeAndSynonimies.containsKey(challengeAndLC)) {
			challengeAndSynonimies.put(challengeAndLC, new HashSet<String>());
		}
		for(String loincde:challengeLNC.get(challengeAndLC)){
		
		challengeAndSynonimies.get(challengeAndLC).addAll(challengeLNCMap.get(loincde));
		
		for(String syn:challengeLNCMap.get(loincde)) {
			if(!challengeAndSynonimies.containsKey(syn)) {
				challengeAndSynonimies.put(syn, new HashSet<String>());
			}
			challengeAndSynonimies.get(syn).add(challengeAndLC);
			
		}
		}
			
		
	}
//	System.out.println(" rajout ");
//	for(String Ch:challengeAndSynonimies.keySet()) {
//		for(String syn: challengeAndSynonimies.get(Ch)) {
//			challengeAndSynonimies.get(Ch).addAll(challengeAndSynonimies.get(syn));
//		}
//	}
	
	for(String AjustemAndLC: AjustementLNC.keySet()) {
		//String ajustem=AjustemAndLC.y;
		if(!AjustementAndSynonimies.containsKey(AjustemAndLC)) {
			AjustementAndSynonimies.put(AjustemAndLC, new HashSet<String>());
		}
		for(String loincde:AjustementLNC.get(AjustemAndLC)) {
		
		AjustementAndSynonimies.get(AjustemAndLC).addAll(AjustementLNCMap.get(loincde));
		
		for(String syn:AjustementLNCMap.get(loincde)) {
			if(!AjustementAndSynonimies.containsKey(syn)) {
				AjustementAndSynonimies.put(syn, new HashSet<String>());
			}
			AjustementAndSynonimies.get(syn).add(AjustemAndLC);
			
		}
		}
			
		
	}
//	System.out.println(" rajout ");
//	for(String Aj:AjustementAndSynonimies.keySet()) {
//		for(String syn: AjustementAndSynonimies.get(Aj)) {
//			AjustementAndSynonimies.get(Aj).addAll(AjustementAndSynonimies.get(syn));
//		}
//	}
	
	String prefixe="http://erias.org/loincTotal#";
    System.out.println("identifiers creation for each log definitional features");
    System.out.println(PropertyAndSynonimies);
	
    
    HashMap<String, Set<String>> LesgrandeursMesurers= getDescription(prefixe, Property,PropertyAndSynonimies, "PRO");
	//System.out.println("test "+PropertyAndSynonimies );
	
	
	HashMap<String, Set<String>> lesComposantss= getDescription(prefixe, Component,ComponentAndSynonimies, "COMP");
	
	HashMap<String, Set<String>> lesComposantsAnalyte= getDescription(prefixe, Analytes, AnalytesAndSynonimies,"AN");
	HashMap<String, Set<String>> lesComposantsChallenge= getDescription(prefixe, challenge, challengeAndSynonimies,"CH");
	HashMap<String, Set<String>> lesComposantsAjustement= getDescription(prefixe, Ajustement, AjustementAndSynonimies,"ADJ");
	
	HashMap<String, Set<String>> lesChapitress= getDescription(prefixe, ClassName,ClassNameAndSynonimies, "ClAS");
	HashMap<String, Set<String>> Echelles= getDescription(prefixe, Scale, ScaleAndSynonimies,"SCALE");
	
	System.out.println("jn test synonymies "+ScaleAndSynonimies);
	HashMap<String, Set<String>> letempss= getDescription(prefixe, Time_aspect,TimeAndSynonimies, "TIME");
	HashMap<String, Set<String>> lesTechniques= getDescription(prefixe, method,methodAndSynonimies, "METH");
	HashMap<String, Set<String>> leMilieuBiologiques= getDescription(prefixe, SystemLOINC, SystemAndSynonimies,"SYST");
	System.out.println("description finish");
	//String namespace="FrenchLOINC";
	//String namespace="LOINCTotal";
	//String namespace = "TLABANDLOINC";
	String namespace="FrenchLoincAnalysis";
	System.out.println("triple store beguinning");
	Repository repo = RepositoryFactory.getRepository(namespace);
	RepositoryConnection connnex = repo.getConnection();
	System.out.println("connexion realised");
	
	//	Set<Statement> lesSatement= new HashSet<Statement>();
	ValueFactory vf = connnex.getValueFactory();
	
	Set<Statement> TheFrenchLoinc= new HashSet<Statement>();
	
	
	Set<Statement> TheModelDescription= new HashSet<Statement>();
	Set<Statement> TheComposantRelation= new HashSet<Statement>();
	Set<Statement> LabelsForEachConcepts= new HashSet<Statement>();
	
	System.out.println("creation of the model");
	for(LOINCDataBaseModel ligne : ResultatFrenchLoinc) {
		TheModelDescription.addAll(getLoincRelationRDF(ligne, prefixe, lesComposantss, LesgrandeursMesurers, lesChapitress, Echelles, letempss, lesTechniques, leMilieuBiologiques, vf));
	}
	System.out.println("model described");
	TheComposantRelation.addAll(getComposantRelation(lesComposantss, prefixe, lesComposantsAnalyte, lesComposantsChallenge, lesComposantsAjustement,vf));
	
	System.out.println("prise en compte des libellés");
	LabelsForEachConcepts.addAll(getStamentRDFFormat(lesComposantss,vf));
	LabelsForEachConcepts.addAll(getStamentRDFFormat(lesComposantsAnalyte,vf));
	LabelsForEachConcepts.addAll(getStamentRDFFormat(lesComposantsChallenge,vf));
	LabelsForEachConcepts.addAll(getStamentRDFFormat(lesComposantsAjustement,vf));
	LabelsForEachConcepts.addAll(getStamentRDFFormat(LesgrandeursMesurers,vf));
	LabelsForEachConcepts.addAll(getStamentRDFFormat(lesChapitress,vf));
	LabelsForEachConcepts.addAll(getStamentRDFFormat(Echelles,vf));
	LabelsForEachConcepts.addAll(getStamentRDFFormat(letempss,vf));
	LabelsForEachConcepts.addAll(getStamentRDFFormat(lesTechniques,vf));
	LabelsForEachConcepts.addAll(getStamentRDFFormat(leMilieuBiologiques,vf));
	
	TheFrenchLoinc.addAll(LabelsForEachConcepts);
	TheFrenchLoinc.addAll(TheComposantRelation);
	TheFrenchLoinc.addAll(TheModelDescription);
	
	
	
	
	
	System.out.println("debut de l'insertion");
	
	String GraphLoincFrancais = "http://erias.org/FrenchLoinc";
	
	URI graphLoincFrancais = connnex.getValueFactory().createURI(GraphLoincFrancais);
	List<Statement> liste= new ArrayList<Statement>(TheFrenchLoinc);
	

	try {	

		connnex.add(liste,graphLoincFrancais);
	} catch (RepositoryException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	System.out.println("final terminé");
	
	
	
		
	
	}
	
	
	public static Set<Statement>getComposantRelation(
			HashMap<String, Set<String>> lesComposants,
			String prefixe,
			HashMap<String, Set<String>> lesComposantsAnalyte,
	HashMap<String, Set<String>> lesComposantsChallenge,
	HashMap<String, Set<String>> lesComposantsAjustement,
	ValueFactory vf){
		Set<Statement> lesstatem= new HashSet<Statement>();
		
		for(String codeComposant:lesComposants.keySet()){
			for(String libellecomposant: lesComposants.get(codeComposant)){
				//System.out.println("libelle "+libellecomposant);
				HashMap<String, Set<String>>structure=getComposantDescription(libellecomposant);
				//System.out.println("resultat obtenu"+structure);
		if(structure.containsKey(Integer.toString(1))){
			Set<String> LesAnalyte= new HashSet<String>();
					LesAnalyte.addAll(structure.get(Integer.toString(1)));
				//	System.out.println("Analyte"+LesAnalyte);
					for(String codeAnalyte: lesComposantsAnalyte.keySet()){
						for(String lib: lesComposantsAnalyte.get(codeAnalyte)){
							if(LesAnalyte.contains(lib)){
								
								Statement stm = vf.createStatement( vf.createURI(codeComposant),
										vf.createURI(prefixe+"has_analyte"),
										vf.createURI(codeAnalyte));
								lesstatem.add(stm);
							}
						}
					}
		}
		if(structure.containsKey(Integer.toString(2))){
			Set<String> LesChallenge= new HashSet<String>();
			LesChallenge.addAll(structure.get(Integer.toString(2)));
			//System.out.println("challenge "+LesChallenge);
			for(String codeChallenge: lesComposantsChallenge.keySet()){
				for(String lib: lesComposantsChallenge.get(codeChallenge)){
					if(LesChallenge.contains(lib)){
						//System.out.println("*"+codeComposant+"*");
						//System.out.println("*"+codeChallenge+"*");
						Statement stm = vf.createStatement( vf.createURI(codeComposant),
								vf.createURI(prefixe+"has_challenge"),
								vf.createURI(codeChallenge));
						lesstatem.add(stm);
					}
				}
			}
		}
		if(structure.containsKey(Integer.toString(3))){
			Set<String> Lesajustements= new HashSet<String>();
			Lesajustements.addAll(structure.get(Integer.toString(3)));
			//System.out.println("ajustement "+Lesajustements);
			for(String codeAjustement: lesComposantsAjustement.keySet()){
				for(String lib: lesComposantsAjustement.get(codeAjustement)){
					if(Lesajustements.contains(lib)){
						
						Statement stm = vf.createStatement( vf.createURI(codeComposant),
								vf.createURI(prefixe+"has_adjustment"),
								vf.createURI(codeAjustement));
						lesstatem.add(stm);
					}
				}
			}
		}
		}
		
	}
		return lesstatem;
		
	}
	public static Set<Statement> getLoincRelationRDF(LOINCDataBaseModel ligne, String prefixe,
			HashMap<String, Set<String>> lesComposants,
		HashMap<String, Set<String>> LesgrandeursMesurer,
	HashMap<String, Set<String>> lesChapitres,
	HashMap<String, Set<String>> Echelle,
	HashMap<String, Set<String>> letemps,
	HashMap<String, Set<String>> lesTechnique,
	HashMap<String, Set<String>> leMilieuBiologique,ValueFactory vf
	){
		
		Set<Statement> lesSatement= new HashSet<Statement>();
		
	//	System.out.println("begining systems");
		for(String code :leMilieuBiologique.keySet()){
		
				if(leMilieuBiologique.get(code).contains(ligne.getSysteme())){
					
					Statement stm = vf.createStatement( vf.createURI(prefixe+ligne.getCodeLoinc()),
							vf.createURI(prefixe+"has_system"),
							vf.createURI(code));
					lesSatement.add(stm);
				}
			
		}
	//	System.out.println("begining lesTechnique");
		for(String code :lesTechnique.keySet()){
			
				if(lesTechnique.get(code).contains(ligne.getmethode())){
					
					Statement stm = vf.createStatement( vf.createURI(prefixe+ligne.getCodeLoinc()),
							vf.createURI(prefixe+"has_method"),
							vf.createURI(code));
					lesSatement.add(stm);
				}
			
		}
		//System.out.println("begining letemps");
		for(String code :letemps.keySet()){
			
				if(letemps.get(code).contains(ligne.gettemps())){
					
					Statement stm = vf.createStatement( vf.createURI(prefixe+ligne.getCodeLoinc()),
							vf.createURI(prefixe+"has_time"),
							vf.createURI(code));
					lesSatement.add(stm);
				}
			
		}
	//	System.out.println("begining Echelle");
		
		for(String code :Echelle.keySet()){
			
				if( Echelle.get(code).contains(ligne.getEchelle())){
					
					Statement stm = vf.createStatement( vf.createURI(prefixe+ligne.getCodeLoinc()),
							vf.createURI(prefixe+"has_scale"),
							vf.createURI(code));
					lesSatement.add(stm);
				}
			
		}
		
	//	System.out.println("begining lesChapitres");
		
		for(String code :lesChapitres.keySet()){
			
				if(lesChapitres.get(code).contains(ligne.getclasse())){
					
					Statement stm = vf.createStatement( vf.createURI(prefixe+ligne.getCodeLoinc()),
							vf.createURI(prefixe+"has_class"),
							vf.createURI(code));
					lesSatement.add(stm);
				}
			
		}
		//System.out.println("begining lesComposants");
		
		for(String code :lesComposants.keySet()){
			
				if(lesComposants.get(code).contains(ligne.getComponent())){
					
					Statement stm = vf.createStatement( vf.createURI(prefixe+ligne.getCodeLoinc()),
							vf.createURI(prefixe+"has_component"),
							vf.createURI(code));
					lesSatement.add(stm);
				}
			
		}
		//System.out.println("begining LesgrandeursMesurer");
		for(String code :LesgrandeursMesurer.keySet()){
			
				if(LesgrandeursMesurer.get(code).contains(ligne.getgrandeur())){
					
					Statement stm = vf.createStatement( vf.createURI(prefixe+ligne.getCodeLoinc()),
							vf.createURI(prefixe+"has_property"),
							vf.createURI(code));
					lesSatement.add(stm);
				}
			
		}
		
		//System.out.println("next");
		return lesSatement;
		//System.out.println("quoi?");
		
	}
	public static Set<Statement> getStamentRDFFormat(HashMap<String, Set<String>> ListeCaracteristique,ValueFactory vf){

		String Prefix_rdfs="http://www.w3.org/2000/01/rdf-schema#";	
		
		Set<Statement> lesSatement= new HashSet<Statement>();
		
		for(String code: ListeCaracteristique.keySet()){
			for(String libelle: ListeCaracteristique.get(code)){
			
				//System.out.println("*"+code+"*");
				Statement stm = vf.createStatement( vf.createURI(code),
						vf.createURI(Prefix_rdfs + "label"),
						vf.createLiteral (libelle));
				lesSatement.add(stm);
			}
			
		}
		return lesSatement;
		
	}
	
	public static HashMap<String, Set<String>> getUnikey(HashMap<String, Set<String>> synonimies){
		HashMap<String, Set<String>> result= new HashMap<String, Set<String>>();
		for(String keyt :synonimies.keySet()) {
			if(!result.containsKey(keyt)) {
				result.put(keyt, new HashSet<String>());
			}
			result.get(keyt).addAll(synonimies.get(keyt));
			for(String ket: synonimies.keySet()) {
				if(ket.equals("interprétation")) {
					//System.out.println(synonimies);
				//	System.out.println(keyt+" + "+ket+" + "+synonimies.get(ket));
				}
				if(synonimies.get(ket).contains(keyt)) {
					result.get(keyt).addAll(synonimies.get(ket));
					result.get(keyt).add(ket);
				}
			}
			
		}
		return result;
		
	}
	public static HashMap<String, Set<String>> redistributeRelation(HashMap<String, Set<String>> synonimiesMap){
		HashMap<String, Set<String>> result= new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> result1= new HashMap<String, Set<String>>();
		
		for(String keyp:synonimiesMap.keySet()) {
			Set<String> listkeyt= new HashSet<String>();
			listkeyt.add(keyp);
			listkeyt.addAll(synonimiesMap.get(keyp));
			for(String kap:synonimiesMap.keySet()) {
				if(synonimiesMap.get(kap).contains(keyp)) {
					listkeyt.add(kap);
					listkeyt.addAll(synonimiesMap.get(kap));
					
				}
			}
			Set<String>correspond= new HashSet<String>();
			for(String allsy:listkeyt) {
				if(!result.containsKey(allsy)) {
					result.put(allsy, new HashSet<String>());
				}
				correspond.addAll(synonimiesMap.get(allsy));
		
				if(keyp.equals("interprétation")) {
					if(!result1.containsKey(allsy)) {
						result1.put(allsy, new HashSet<String>());
					}
				}
			}
			for(String allsy:listkeyt) {
					result.get(allsy).addAll(correspond);
				if(keyp.equals("interprétation")) {

					result1.get(allsy).addAll(correspond);
				}
			}
//			if(keyp.equals("interprétation")) {
//				System.out.println("synonimiesMap.get(keyp)"+synonimiesMap.get(keyp));
//				System.out.println(listkeyt);
//				System.out.println(result1);
//			}
		}
		
		
		
		return result;
		
	}
	public static HashMap<String, Set<String>> getDescription(String prefixe, Set<String> listecaracteristiques, HashMap<String, Set<String>> listSynonymie, String description){
		int i =1500;
		System.out.println(listecaracteristiques.size()+" "+description);
		//HashMap<String, Set<String>> listSynonymies=getUnikey(listSynonymie);
		HashMap<String, Set<String>> listSynonym=new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> listSynonymies =redistributeRelation(listSynonymie);
		
		HashMap<String, Set<String>> descriptionresult= new HashMap<String, Set<String>>();
		Set<String> listOfAlreadyCreatedCode= new HashSet<String>();
		//Set<String>libCodeCreated
		
		for(String lib: listecaracteristiques){
			//System.out.println(prefix+i);
			
			
			if(!listOfAlreadyCreatedCode.contains(lib)) {
				if(!lib.equals("null")) {
					if(!lib.equals("-")) {
					if(listSynonymies.containsKey(lib)) {
						Set<String> relatedLabel=listSynonymies.get(lib);
						int u=0;
						for(String lit:relatedLabel) {
							if(listOfAlreadyCreatedCode.contains(lit)) {
								u++;
								if (!listSynonym.containsKey(lit)) {
									listSynonym.put(lit, new HashSet<String>());
								}
							listSynonym.get(lit).addAll(relatedLabel);
							}
						}
				
						if(u==0) {
							i++;
				
							if(lib !=null && !lib.isEmpty() && !lib.equals("null") && !lib.equals("-")){
								if(!descriptionresult.containsKey(prefixe+description+i)){
									descriptionresult.put(prefixe+description+i, new HashSet<String>());
								}
								descriptionresult.get(prefixe+description+i).add(lib);
								listOfAlreadyCreatedCode.add(lib);
								if(listSynonymies.containsKey(lib)) {
									descriptionresult.get(prefixe+description+i).addAll(listSynonymies.get(lib));
						
									listOfAlreadyCreatedCode.addAll(listSynonymies.get(lib));
				
								}
			
			
							}
							else{
								System.out.println("llllllllllllll +++++++++"+lib+"2"+description);
							}
				
				
						}	
				
					}
				}
			}
			
				}
		
			
			}
		
		for(String code: descriptionresult.keySet()) {
			Set<String> labels= new HashSet<String>();
			Set<String> additional= new HashSet<String>();
			for(String label:labels) {
				if(listSynonym.containsKey(label)) {
					additional.add(label);
				}
				for(String syn: listSynonym.keySet()) {
					if(listSynonym.get(syn).contains(label)) {
						additional.add(syn);
						additional.addAll(listSynonym.get(syn));
					}
				}
			}
			descriptionresult.get(code).addAll(additional);
			
		}
		return descriptionresult;
			
			
		
	}
	public static HashMap<String, Set<String>> getComposantDescription(String libelleOriginal){

		HashMap<String, Set<String>> lesComposant= new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> lesComposantAnalyte= new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> lesComposantChallenge= new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> lesComposantAjustement= new HashMap<String, Set<String>>();
		
		int i=0;
		Set<String> LesQuotients= new HashSet<String>();
		String[] libelleL=libelleOriginal.split("/");
		if(libelleL.length>1) {
			System.out.println("test"+libelleL.length);
		LesQuotients.add(libelleL[1]);
		}
		String libelle=libelleL[0];
		System.out.println("libelle "+libelle+" LesQuotients "+LesQuotients);
		
		for(String composants:libelle.split("\\^")){
			i++;
			if(i==1){
				if(!lesComposantAnalyte.containsKey(Integer.toString(i))){
					lesComposantAnalyte.put(Integer.toString(i), new HashSet<String>());
				}
				lesComposantAnalyte.get(Integer.toString(i)).add(composants);
		
			}
			else if(i==2){
				if(!lesComposantChallenge.containsKey(Integer.toString(i))){
					lesComposantChallenge.put(Integer.toString(i), new HashSet<String>());
				}
				lesComposantChallenge.get(Integer.toString(i)).add(composants);
		
			}
			else if(i==3){
				if(!lesComposantAjustement.containsKey(Integer.toString(i))){
					lesComposantAjustement.put(Integer.toString(i), new HashSet<String>());
				}
				lesComposantAjustement.get(Integer.toString(i)).add(composants);
			}
			else{
				System.out.println("erreur");
			}
		//	System.out.println(lib);
		}
		if(i>1) {
		for(String a:lesComposantAnalyte.keySet()){
			if(!lesComposant.containsKey(a)){
				lesComposant.put(a, new HashSet<String>());
				
			}
			lesComposant.get(a).addAll(lesComposantAnalyte.get(a));
			
		}
		for(String a:lesComposantAjustement.keySet()){
			if(!lesComposant.containsKey(a)){
				lesComposant.put(a, new HashSet<String>());
				
			}
			lesComposant.get(a).addAll(lesComposantAjustement.get(a));
			
		}
		for(String a:lesComposantChallenge.keySet()){
		
			if(!lesComposant.containsKey(a)){
				lesComposant.put(a, new HashSet<String>());
				
			}
			lesComposant.get(a).addAll(lesComposantChallenge.get(a));
			
		}
		}
		//System.out.println(libelle+" "+lesComposant);
		return lesComposant;
		
		
		
	
	}

}
