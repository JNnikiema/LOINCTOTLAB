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
import utilitaries.Couple;

public class CreateTimeAndSystemWithoutSubparts {

	public static void main(String[] args) throws RepositoryException, QueryEvaluationException, MalformedQueryException {
		// TODO Auto-generated method stub
		
			//String namespace="LOINCTotal";
			//String namespace = "TLABANDLOINC";
			String namespace="FrenchLoincAnalysis";
			//String namespace="FrenchLOINC";
				
				
				Repository repo = RepositoryFactory.getRepository(namespace);
				RepositoryConnection conn = repo.getConnection();
				String queryAlltime=getAlltime();
				String queryWttimepart=getTimeWithsubparts();
				String queryTimeAndLabel=getTimecodeAndLabels();
				//System.out.println("queryWAnalytes "+queryWAnalytes);
				
				String queryAllsystem=getAlSystem();
				String queryWSystempart=getSystemWithSuperSystem();
				String queryWSystemAndLabel=getsystemcodeAndLabel();
						
				Set<String> alltime= getresult(conn, queryAlltime);
				Set<String> timewithTimepart= getresult(conn, queryWttimepart);
				Set<String> TimefortimepartsTocreate= elementtocreate(alltime, timewithTimepart);
				HashMap<String, Set<String>> TimeAndLabels=getresultsLabels(conn, queryTimeAndLabel);
				System.out.println("TimeAndLabels "+TimeAndLabels);
				Set<String> allsystem= getresult(conn, queryAllsystem);
				Set<String> systemwithsupersystem= getresult(conn, queryWSystempart);
				Set<String> systemparttocreate= elementtocreate(allsystem, systemwithsupersystem);
				HashMap<String, Set<String>> SystemAndLabels=getresultsLabels(conn, queryWSystemAndLabel);
				System.out.println("SystemAndLabels "+SystemAndLabels);
				
				String prefixe="http://erias.org/loincTotal#";
				ValueFactory vf = conn.getValueFactory();
				
				HashMap<String, Set<String>> systemAndLabeltocreate =getLabelAndcorrespondingcode(SystemAndLabels, systemparttocreate);
				HashMap<String, Set<String>> TimeAndLabeltocreate =getLabelAndcorrespondingcode(TimeAndLabels, TimefortimepartsTocreate);
				HashMap<Couple, Set<String>> systemstep =getsubpartFrompart(prefixe, systemAndLabeltocreate, "SSYS");
				HashMap<Couple, Set<String>> Timestep=getsubpartFrompart(prefixe, TimeAndLabeltocreate, "ASPECT");
				Set<Statement> rdfStatement= new HashSet<Statement>();
				rdfStatement.addAll(getrdfStructure(prefixe, "has_main_system", vf, systemstep));
				//System.out.println(rdfStatement);
				rdfStatement.addAll(getrdfStructure(prefixe, "has_time_aspect", vf, Timestep));
				String GraphLoincFrancais = "http://erias.org/FrenchLoinc";
				
				URI graphLoincFrancais = conn.getValueFactory().createURI(GraphLoincFrancais);
				List<Statement> liste= new ArrayList<Statement>(rdfStatement);
				

				try {	

					conn.add(liste,graphLoincFrancais);
				} catch (RepositoryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("final terminé");

	}
	
	public static HashMap<String, Set<String>> getLabelAndcorrespondingcode(HashMap<String, Set<String>> codeAndLabel,Set<String>codeTocreate){
		HashMap<String, Set<String>> codeAndLabelsTocreate= new HashMap<String, Set<String>>();
		System.out.println("codeAndLabels "+codeAndLabel.size());
		System.out.println("codeTocreate "+codeTocreate.size());
		for(String code:codeAndLabel.keySet()) {
			if(codeTocreate.contains(code)) {
				if(!codeAndLabelsTocreate.containsKey(code)) {
					codeAndLabelsTocreate.put(code, new HashSet<String>());
				}
				codeAndLabelsTocreate.get(code).addAll(codeAndLabel.get(code));
			
			}
			
		}
		return codeAndLabelsTocreate;
	}
	
public static Set<Statement> getrdfStructure(String prefixe,String relation, ValueFactory vf,HashMap<Couple, Set<String>> result){
		
		Set<Statement> lesstatem= new HashSet<Statement>();
		String Prefix_rdfs="http://www.w3.org/2000/01/rdf-schema#";	
		for(Couple codeComponAndAnalyte: result.keySet()){
			//System.out.println(prefixe+"Has_Analyte");
			
			Statement stm = vf.createStatement( vf.createURI(codeComponAndAnalyte.x),
					vf.createURI(prefixe+relation),
					vf.createURI(codeComponAndAnalyte.y));
			lesstatem.add(stm);
			
			for(String lib: result.get(codeComponAndAnalyte)){
				Statement stm1 = vf.createStatement( vf.createURI(codeComponAndAnalyte.y),
						vf.createURI(Prefix_rdfs + "label"),
						vf.createLiteral (lib));
				lesstatem.add(stm1);
			}
		}
		return lesstatem;
		
	}
	public static HashMap<Couple, Set<String>> getsubpartFrompart(String prefixe,HashMap<String, Set<String>>componentCodeAndLabel,String description){
		int i=50000;
		HashMap<Couple, Set<String>> descriptionresult= new HashMap<Couple, Set<String>>();
		for(String codeCo:componentCodeAndLabel.keySet()) {
			i++;
			Couple cp= new Couple(codeCo, prefixe+description+i);
			if(!descriptionresult.containsKey(cp)){
				descriptionresult.put(cp, new HashSet<String>());
			}
			descriptionresult.get(cp).addAll(componentCodeAndLabel.get(codeCo));
		}
		
		return descriptionresult;
		
	}
	public static String getsystemcodeAndLabel() {
		String query="prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" + 
				"				 Select distinct  ?p ?o WHERE { \n" + 
				"				\n" + 
				"                 GRAPH <http://erias.org/FrenchLoinc> {\n" + 
				"                 \n" + 
				"				?s <http://erias.org/loincTotal#has_system> ?p.\n" + 
				"                   \n" + 
				"                   ?p rdfs:label ?o. \n" + 
				"               \n" + 
				"                   \n" + 
				"                       \n" + 
				"                   \n" + 
				"				 }\n" + 
				"                       \n" + 
				"				 }";
		return query;
		
	}
	
	public static HashMap<String,Set<String>> getresultsLabels(RepositoryConnection conn, String query) throws QueryEvaluationException, RepositoryException, MalformedQueryException{
		
		HashMap<String,Set<String>> codeAndLabels= new HashMap<String, Set<String>>();
		
		 
		 ArrayList<BindingSet> ListeWithLabels= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query));
		for(BindingSet e: ListeWithLabels){
			if(!codeAndLabels.containsKey(e.getValue("p").stringValue())) {
				codeAndLabels.put(e.getValue("p").stringValue(), new HashSet<String>());
			}
			codeAndLabels.get(e.getValue("p").stringValue()).add(e.getValue("o").stringValue());
		}
		return codeAndLabels;
	}
	public static String getTimecodeAndLabels() {
		String query="prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" + 
				"				 Select distinct  ?p ?o WHERE { \n" + 
				"				\n" + 
				"                 GRAPH <http://erias.org/FrenchLoinc> {\n" + 
				"                 \n" + 
				"				?s <http://erias.org/loincTotal#has_time> ?p.\n" + 
				"                   \n" + 
				"                   ?p rdfs:label ?o. \n" + 
				"               \n" + 
				"                   \n" + 
				"                       \n" + 
				"                   \n" + 
				"				 }\n" + 
				"                       \n" + 
				"				 } ";
		return query;
	}
	
	
	public static Set<String> elementtocreate(Set<String> allelement, Set<String> elementwithparts){
		Set<String> elementtocreate= new HashSet<String>();
		for(String elem:allelement) {
			if(!elementwithparts.contains(elem)) {
				elementtocreate.add(elem);
			}
		}
		return elementtocreate;
		
		
		//return elementtocreate;
		
	}
	public static Set<String> getresult(RepositoryConnection conn, String query) throws QueryEvaluationException, RepositoryException, MalformedQueryException{
		Set<String> resultat= new HashSet<String>();
		ArrayList<BindingSet> ListeRDFTotal= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query));
		for(BindingSet e: ListeRDFTotal){
			resultat.add(e.getValue("p").stringValue());
		}
	return resultat;
	}
	
	public static String getTimeWithsubparts() {
		String query="prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" + 
				"				 Select distinct  ?p WHERE { \n" + 
				"				\n" + 
				"                 GRAPH <http://erias.org/FrenchLoinc> {\n" + 
				"                 \n" + 
				"				?s <http://erias.org/loincTotal#has_time> ?p.\n" + 
				"                   \n" + 
				"                   ?p <http://erias.org/loincTotal#has_modifier_time> ?g\n" + 
				"                   \n" + 
				"                       \n" + 
				"                   \n" + 
				"				 }\n" + 
				"                       \n" + 
				"				 }";
		return query;
	}
	public static String getAlltime() {
		String query="prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" + 
				"				 Select distinct  ?p WHERE { \n" + 
				"				\n" + 
				"                 GRAPH <http://erias.org/FrenchLoinc> {\n" + 
				"                 \n" + 
				"				?s <http://erias.org/loincTotal#has_time> ?p.\n" + 
				"                   \n" + 
				"               \n" + 
				"                   \n" + 
				"                       \n" + 
				"                   \n" + 
				"				 }\n" + 
				"                       \n" + 
				"				 } ";
		return query;
		
	}
	
	public static String getSystemWithSuperSystem() {
		String query="prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" + 
				"				 Select distinct  ?p WHERE { \n" + 
				"				\n" + 
				"                 GRAPH <http://erias.org/FrenchLoinc> {\n" + 
				"                 \n" + 
				"				?s <http://erias.org/loincTotal#has_system> ?p.\n" + 
				"                   ?p <http://erias.org/loincTotal#has_super_system> ?l\n" + 
				"                   \n" + 
				"                   \n" + 
				"               \n" + 
				"                   \n" + 
				"                       \n" + 
				"                   \n" + 
				"				 }\n" + 
				"                       \n" + 
				"				 } ";
		return query;
	}
	public static String getAlSystem() {
		String query="prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" + 
				"				 Select distinct  ?p WHERE { \n" + 
				"				\n" + 
				"                 GRAPH <http://erias.org/FrenchLoinc> {\n" + 
				"                 \n" + 
				"				?s <http://erias.org/loincTotal#has_system> ?p.\n" + 
				"                   \n" + 
				"                   \n" + 
				"               \n" + 
				"                   \n" + 
				"                       \n" + 
				"                   \n" + 
				"				 }\n" + 
				"                       \n" + 
				"				 } ";
		return query;
	}

}
