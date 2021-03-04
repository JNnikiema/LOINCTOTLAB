package loincRDFconstruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import interactionTripleStore.InteractionBlazgraph;
import interactionTripleStore.RepositoryFactory;

public class CreateSubparts {

	public static void main(String[] args) throws RepositoryException, QueryEvaluationException, MalformedQueryException {
		// TODO Auto-generated method stub
		//String namespace="LOINCTotal";
		//String namespace = "TLABANDLOINC";
		String namespace="FrenchLoincAnalysis";
		//String namespace="FrenchLOINC";
				
		
		Repository repo = RepositoryFactory.getRepository(namespace);
		RepositoryConnection conn = repo.getConnection();
		String querySystem=getquerySystem();
		String queryTime=getqueryTime();
		HashMap<String, Set<String>> codeandLabelTime=getresult(conn, queryTime);
		HashMap<String, Set<String>> codeandLabelSystem=getresult(conn, querySystem);
		//System.out.println("codeandLabelTime "+codeandLabelTime);
		
		Set<String> Timelabel=new HashSet<String>() ;
		Set<String> Systemlabel=new HashSet<String>() ;
		for(String code :codeandLabelTime.keySet() ) {
			Timelabel.addAll(codeandLabelTime.get(code));
		}
		for(String code :codeandLabelSystem.keySet() ) {
			Systemlabel.addAll(codeandLabelSystem.get(code));
		}
		Set<String> timeModifier= new HashSet<String>();
		Set<String> timeAspect= new HashSet<String>();
		Set<String> supersystem= new HashSet<String>();
		Set<String> normalSystem= new HashSet<String>();
		HashMap<String, Set<String>> timeLabelAnsSubtyp1=new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> timeLabelAnsSubtyp2=new HashMap<String, Set<String>>();
		
		for(String lab:Timelabel) {
			System.out.println("lab "+lab);
		HashMap<String, Set<String>> timestrucutre=getTimeAndSystsubDescription(lab);
		if(!timestrucutre.isEmpty()) {
		timeAspect.addAll(timestrucutre.get(Integer.toString(1)));
		timeModifier.addAll(timestrucutre.get(Integer.toString(2)));
		for(String tim:codeandLabelTime.keySet()) {
			for(String lib:codeandLabelTime.get(tim)) {
				if(lib.equals(lab)) {
					if(!timeLabelAnsSubtyp1.containsKey(tim)) {
						timeLabelAnsSubtyp1.put(tim, new HashSet<String>());
					}
					timeLabelAnsSubtyp1.get(tim).addAll(timestrucutre.get(Integer.toString(1)));
					if(!timeLabelAnsSubtyp2.containsKey(tim)) {
						timeLabelAnsSubtyp2.put(tim, new HashSet<String>());
					}
					timeLabelAnsSubtyp2.get(tim).addAll(timestrucutre.get(Integer.toString(2)));
				
				}
			}
		}
		}
		}
		HashMap<String, Set<String>> SystemLabelAnsSubtyp1=new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> SystemLabelAnsSubtyp2=new HashMap<String, Set<String>>();
		
		for(String lab:Systemlabel) {
			System.out.println(lab);
			HashMap<String, Set<String>> systemstrucutre=getTimeAndSystsubDescription(lab);
			if(!systemstrucutre.isEmpty()) {
			normalSystem.addAll(systemstrucutre.get(Integer.toString(1)));
			supersystem.addAll(systemstrucutre.get(Integer.toString(2)));
			for(String sys:codeandLabelSystem.keySet()) {
				for(String lib:codeandLabelSystem.get(sys)) {
					if(lib.equals(lab)) {
						if(!SystemLabelAnsSubtyp1.containsKey(sys)) {
							SystemLabelAnsSubtyp1.put(sys, new HashSet<String>());
						}
						if(!systemstrucutre.get(Integer.toString(1)).isEmpty()) {
						SystemLabelAnsSubtyp1.get(sys).addAll(systemstrucutre.get(Integer.toString(1)));
						}
						if(!SystemLabelAnsSubtyp2.containsKey(sys)) {
							SystemLabelAnsSubtyp2.put(sys, new HashSet<String>());
						}
						SystemLabelAnsSubtyp2.get(sys).addAll(systemstrucutre.get(Integer.toString(2)));
					
					}
				}
			}
		}
			}
		HashMap<String, Set<String>> systemPrinSynonimies= new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> SupersystemSynonimies= new HashMap<String, Set<String>>();
		
		HashMap<String, Set<String>> aspTimeSynonimies= new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> ModifierTimeSynonimies= new HashMap<String, Set<String>>();
		
		for(String code:timeLabelAnsSubtyp1.keySet()) {
			for(String tim: timeLabelAnsSubtyp1.get(code)) {
				if(!aspTimeSynonimies.containsKey(tim)) {
					aspTimeSynonimies.put(tim, new HashSet<String>());
				}
				aspTimeSynonimies.get(tim).addAll(timeLabelAnsSubtyp1.get(code));
				
			}
		}
		
		for(String code:timeLabelAnsSubtyp2.keySet()) {
			for(String tim: timeLabelAnsSubtyp2.get(code)) {
				if(!ModifierTimeSynonimies.containsKey(tim)) {
					ModifierTimeSynonimies.put(tim, new HashSet<String>());
				}
				ModifierTimeSynonimies.get(tim).addAll(timeLabelAnsSubtyp2.get(code));
				
			}
		}
		
		for(String code:SystemLabelAnsSubtyp1.keySet()) {
			for(String syste: SystemLabelAnsSubtyp1.get(code)) {
				if(!systemPrinSynonimies.containsKey(syste)) {
					systemPrinSynonimies.put(syste, new HashSet<String>());
				}
				systemPrinSynonimies.get(syste).addAll(SystemLabelAnsSubtyp1.get(code));
				
			}
		}
		for(String code:SystemLabelAnsSubtyp2.keySet()) {
			for(String syste: SystemLabelAnsSubtyp2.get(code)) {
				if(!SupersystemSynonimies.containsKey(syste)) {
					SupersystemSynonimies.put(syste, new HashSet<String>());
				}
				SupersystemSynonimies.get(syste).addAll(SystemLabelAnsSubtyp2.get(code));
				
			}
		}
		
		String prefixe="http://erias.org/loincTotal#";
		ValueFactory vf = conn.getValueFactory();
		//System.out.println("ModifierTimeSynonimies "+ModifierTimeSynonimies);
		HashMap<String, Set<String>> Lesaspecttime=	FrenchLOINC.getDescription(prefixe, timeAspect, aspTimeSynonimies, "ASPECT");
		HashMap<String, Set<String>> LesModificationtime=	FrenchLOINC.getDescription(prefixe, timeModifier, ModifierTimeSynonimies, "MOD");
		HashMap<String, Set<String>> LesSYStem=	FrenchLOINC.getDescription(prefixe, normalSystem, systemPrinSynonimies, "SSYS");
		HashMap<String, Set<String>> LesSuperSystem=	FrenchLOINC.getDescription(prefixe, supersystem, SupersystemSynonimies, "SUPERSYS");
		
		System.out.println("LesSYStem "+SystemLabelAnsSubtyp1);
		Set<Statement> stames= new HashSet<Statement>();
		stames.addAll(getRdfdescription(prefixe, Lesaspecttime, timeLabelAnsSubtyp1, "has_time_aspect", vf));
		stames.addAll(getRdfdescription(prefixe, LesModificationtime, timeLabelAnsSubtyp2, "has_modifier_time", vf));
		stames.addAll(getRdfdescription(prefixe, LesSYStem, SystemLabelAnsSubtyp1, "has_main_system", vf));
		stames.addAll(getRdfdescription(prefixe, LesSuperSystem, SystemLabelAnsSubtyp2, "has_super_system", vf));
	
		
		System.out.println("debut de l'insertion");
		
		String GraphLoincFrancais = "http://erias.org/FrenchLoinc";
		
		URI graphLoincFrancais = conn.getValueFactory().createURI(GraphLoincFrancais);
		List<Statement> liste= new ArrayList<Statement>(stames);
		

		try {	

			conn.add(liste,graphLoincFrancais);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("final terminé");
		
	
	
	}
	public static Set<Statement> getRdfdescription(String prefixe,HashMap<String, Set<String>> ListeCaracteristique, HashMap<String, Set<String>> MaincodeAndSubPart, String relation, ValueFactory vf){
		Set<Statement> result= new HashSet<Statement>();
		String Prefix_rdfs="http://www.w3.org/2000/01/rdf-schema#";	
		
		for(String code:ListeCaracteristique.keySet()) {
			System.out.println("ListeCaracteristique  code "+code);
			for(String lib: ListeCaracteristique.get(code)) {
				Statement stm = vf.createStatement( vf.createURI(code),
						vf.createURI(Prefix_rdfs + "label"),
						vf.createLiteral (lib));
				result.add(stm);
				System.out.println("ListeCaracteristique  libelle "+lib);
			}
		}
		
		for(String code:MaincodeAndSubPart.keySet()) {
			for(String codeC : ListeCaracteristique.keySet()) {
				for(String libC:ListeCaracteristique.get(codeC)) {
					if(MaincodeAndSubPart.get(code).contains(libC)){
						Statement stm = vf.createStatement( vf.createURI(code),
								vf.createURI(prefixe+relation),
								vf.createURI(codeC));
						System.out.println(codeC+" codec");
						result.add(stm);
					}
				}
			}
		}
		
		return result;
	}
	public static HashMap<String, Set<String>> getresult(RepositoryConnection conn,String query) throws QueryEvaluationException, RepositoryException, MalformedQueryException{
		HashMap<String, Set<String>> codeAndLabels= new HashMap<String, Set<String>>();
		ArrayList<BindingSet> ListeWithLabels= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query));
		for(BindingSet e: ListeWithLabels){
			if(!codeAndLabels.containsKey(e.getValue("o").stringValue())) {
				codeAndLabels.put(e.getValue("o").stringValue(), new HashSet<String>());
			}
			codeAndLabels.get(e.getValue("o").stringValue()).add(e.getValue("h").stringValue());
		}
		return codeAndLabels;
	}
	public static String getqueryTime() {
		String query="prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" + 
				"				 Select distinct ?o ?h WHERE { \n" + 
				"				\n" + 
				"                 GRAPH <http://erias.org/FrenchLoinc> {\n" + 
				"                   \n" + 
				"				?s <http://erias.org/loincTotal#has_time> ?o.\n" + 
				"                   \n" + 
				"                        ?o rdfs:label ?h.\n" + 
				"                   \n" + 
				"				 }\n" + 
				"                       \n" + 
				"				 } ";
		return query;
	}
	public static String getquerySystem() {
		String query="prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" + 
				"				 Select distinct ?o ?h WHERE { \n" + 
				"				\n" + 
				"                 GRAPH <http://erias.org/FrenchLoinc> {\n" + 
				"                   \n" + 
				"				?s <http://erias.org/loincTotal#has_system> ?o.\n" + 
				"                   \n" + 
				"                        ?o rdfs:label ?h.\n" + 
				"                   \n" + 
				"				 }\n" + 
				"                       \n" + 
				"				 }";
		return query;
	}
	public static HashMap<String, Set<String>> getTimeAndSystsubDescription(String libelle) {
		HashMap<String, Set<String>> thetimedescription= new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> theTimeAspect= new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> TimeModifier= new HashMap<String, Set<String>>();
		int i=0;
		for(String time:libelle.split("\\^")){
			i++;
			if(i==1){
				if(!theTimeAspect.containsKey(Integer.toString(i))){
					theTimeAspect.put(Integer.toString(i), new HashSet<String>());
				}
				theTimeAspect.get(Integer.toString(i)).add(time);
		
			}
			else if(i==2){
				if(!TimeModifier.containsKey(Integer.toString(i))){
					TimeModifier.put(Integer.toString(i), new HashSet<String>());
				}
				TimeModifier.get(Integer.toString(i)).add(time);
		
			}
			
			else{
				System.out.println("erreur");
			}
		//	System.out.println(lib);
		}
		if(i>1) {
			for(String a:theTimeAspect.keySet()){
				if(!thetimedescription.containsKey(a)){
					thetimedescription.put(a, new HashSet<String>());
					
				}
				thetimedescription.get(a).addAll(theTimeAspect.get(a));
				
			}
			for(String a:TimeModifier.keySet()){
				if(!thetimedescription.containsKey(a)){
					thetimedescription.put(a, new HashSet<String>());
					
				}
				thetimedescription.get(a).addAll(TimeModifier.get(a));
				
			}
			
			}
		
		
		return thetimedescription;
	}
	
	

}
