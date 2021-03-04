package filteringStep;

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
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import interactionTripleStore.InteractionBlazgraph;

public class FilteringByTime {
	public FilteringByTime(RepositoryConnection conn) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		
		
		ValueFactory vf = conn.getValueFactory();
		Set<Statement> lesRelationTime= new HashSet<Statement>();
		Set<Statement> lesRelationTime2= new HashSet<Statement>();
		//String graphAncrageHierarchie=	"http://erias.org/integration/AnchoringCandidats/newHierarchy";	
		String queryGetConceptterminologyandTime="prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> \n" + 
				"prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"select distinct ?s ?p ?o\n" + 
				"where{\n" + 
				"  graph<http://erias.org/InterfaceTerminology/TLAB>{\n" + 
				"    ?s skos:prefLabel ?p\n" + 
				"    }\n" + 
				"       ?s <http://erias.org/loincTotal#has_time> ?o. \n" + 
				"  }";
		//RecuperationGraphRDF lesConceptsTerminologieLOcalEtsystemENLOINC = new RecuperationGraphRDF(conn, "hierarchie", graphAncrageHierarchie);
		
		HashMap<String,List<String>> ConceptsTerminologieLocalEtTimeEnLoinc= new HashMap<String, List<String>>();
		
		
		 ArrayList<BindingSet> ListeRDF= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, queryGetConceptterminologyandTime));
			
		 //}
		 for(BindingSet e: ListeRDF){
			 if(!ConceptsTerminologieLocalEtTimeEnLoinc.containsKey(e.getValue("s").stringValue())){
					
				 ConceptsTerminologieLocalEtTimeEnLoinc.put(e.getValue("s").stringValue(), new ArrayList<String>());
				};
				ConceptsTerminologieLocalEtTimeEnLoinc.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());
			 
		 }
		 
		 String queryLOINCconceptandTime="prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> \n" + 
					"  SELECT distinct ?s ?p ?o \n" + 
					"  	where { \n" + 
					"  	 GRAPH<http://erias.org/FrenchLoinc> { \n" + 
					"  		  ?s <http://erias.org/loincTotal#has_time> ?o.   \n" + 
					"     } \n" + 
					"           \n" + 
					"  }  ";
		 
		 HashMap<String,List<String>> codeLOINCAndTime= new HashMap<String, List<String>>();
		 
		 ArrayList<BindingSet> ListeRDF2= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, queryLOINCconceptandTime));
			
		 //}
		 for(BindingSet e: ListeRDF2){
			 if(!codeLOINCAndTime.containsKey(e.getValue("s").stringValue())){
					
				 codeLOINCAndTime.put(e.getValue("s").stringValue(), new ArrayList<String>());
				};
				codeLOINCAndTime.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());
			 
		 }
			HashMap<String,Set<String>> codeTimeandcodeLOINC=getSystemListeCodeInversion(codeLOINCAndTime);

			String querygetTerminoAndLOINCconcept="select ?s ?p ?o where "
			 		+ "{"
			 		+ "graph<http://erias.org/integration/AnchoringFilteringMethod>{"
			 		+ " ?s ?p ?o "
			 		+ "} "
			 		+ " }";
			
			ArrayList<BindingSet> ListeRDF3= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, querygetTerminoAndLOINCconcept));
			HashMap<String,List<String>> CodeTerminoListeCodeLoincAssocie= new HashMap<String, List<String>>();
			
			 //}
			 for(BindingSet e: ListeRDF3){
				 if(!CodeTerminoListeCodeLoincAssocie.containsKey(e.getValue("s").stringValue())){
						
					 CodeTerminoListeCodeLoincAssocie.put(e.getValue("s").stringValue(), new ArrayList<String>());
					};
					CodeTerminoListeCodeLoincAssocie.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());
				 
			 }
		
		
		System.out.println("CodeTerminoListeCodeLoincAssocie "+CodeTerminoListeCodeLoincAssocie.size());
		
		
		Set<String>codePerdu= new HashSet<String>();
		
		int i=1;
		int v=1;
		for(String codeTerm :CodeTerminoListeCodeLoincAssocie.keySet()){
			System.out.println("codeTerm +"+codeTerm+"*");
			Set<String> Times = new HashSet<String>();
			if(ConceptsTerminologieLocalEtTimeEnLoinc.containsKey(codeTerm)){
				
				Times.addAll(ConceptsTerminologieLocalEtTimeEnLoinc.get(codeTerm));
				//System.out.println("Hierarchie "+Hierarchie);
				
				
				int uv=0;
				for(String tim:Times){
					System.out.println(tim);
					
					if(codeTimeandcodeLOINC.containsKey(tim)){
						
						for(String codeLoinc : CodeTerminoListeCodeLoincAssocie.get(codeTerm)){
														
							if(codeTimeandcodeLOINC.get(tim).contains(codeLoinc)){
								Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
										vf.createURI("http://erias.org/integration/TLAB/model#ValidateAnchorsByTime"),
										vf.createURI(codeLoinc));
								if(v<8000000){
									lesRelationTime.add(hty10);
										v++;
								}else {
									lesRelationTime2.add(hty10);
								}
										uv++;
								
							}
						}
					}
				}
				if(uv==0) {
					//System.out.println(codeTerm+" ");
					codePerdu.add(codeTerm);
			//	}
					for(String codloinc: CodeTerminoListeCodeLoincAssocie.get(codeTerm)){
						//System.out.println(codeTerm+" "+codloinc);
						Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
								vf.createURI("http://erias.org/integration/TLAB/model#ValidateAnchorsByTime"),
								vf.createURI(codloinc));
						if(i<8000000){
						
							lesRelationTime.add(hty10);
								i++;
						}else{
							lesRelationTime2.add(hty10);
						}
					}
					
				}
			}
			else{
				for(String codloinc: CodeTerminoListeCodeLoincAssocie.get(codeTerm)){
					Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
							vf.createURI("http://erias.org/integration/TLAB/model#ValidateAnchorsByTime"),
							vf.createURI(codloinc));
					if(i<8000000){
						lesRelationTime.add(hty10);
							i++;
					}else{
						lesRelationTime2.add(hty10);
					}
				}
				
			
			}
		}
		
		
		
		
String graphAncrageFiltre=	"http://erias.org/integration/AnchoringFilteringTime";
		
		URI AncrageURI = conn.getValueFactory().createURI(graphAncrageFiltre);
		List<Statement> liste= new ArrayList<Statement>(lesRelationTime);
		System.out.println("filtrage hierarchir "+liste.size());
		List<Statement> liste2= new ArrayList<Statement>(lesRelationTime2);
		System.out.println("filtrage hierarchir "+liste2.size());
		
		System.out.println("codePerdu "+codePerdu.size());
		System.out.println("debut d'insertion");
		try {
			
			conn.add(liste,AncrageURI);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		System.out.println("deuxième parti debut ");
		try {
			
			conn.add(liste2,AncrageURI);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("fin d'insertion");
	
		
	
		
	}
	public HashMap<String, Set<String>> getSystemListeCodeInversion(HashMap<String,List<String>> codeEtChapitre){
		HashMap<String, Set<String>> resultat= new HashMap<String, Set<String>>();
		for(String code: codeEtChapitre.keySet()){
			for(String chap: codeEtChapitre.get(code)){
				if(!resultat.containsKey(chap)){
					resultat.put(chap, new HashSet<String>());
				}
				resultat.get(chap).add(code);
				
			}
		}
		return resultat;
	}

}


