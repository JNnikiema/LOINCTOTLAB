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

public class FiltrageSelonLeMilieuBiologique {
	public FiltrageSelonLeMilieuBiologique(RepositoryConnection conn) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		
		
		ValueFactory vf = conn.getValueFactory();
		Set<Statement> lesRelationSystem= new HashSet<Statement>();
		Set<Statement> lesRelationSystem2= new HashSet<Statement>();
		//String graphAncrageHierarchie=	"http://erias.org/integration/AnchoringCandidats/newHierarchy";	
		String queryGetConceptterminologyandSystem="prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> \n" + 
			"prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"select distinct ?s ?p ?o\n" + 
				"where{\n" + 
				"  graph<http://erias.org/InterfaceTerminology/TLAB>{\n" + 
				"    ?s skos:prefLabel ?p\n" + 
				"    }\n" + 
				" graph<http://erias.org/integration/AnchoringFromWordReducedEnrichiTOTALCORRIGE>{"+
				"       ?s <http://erias.org/loincTotal#has_system> ?o. \n" + 
				"  }"
				+ " }";
	
		String queryGetConceptterminologyandSystem2="prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> \n" + 
				"prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"select distinct ?s ?p ?o\n" + 
				"where{\n" + 
				"  graph<http://erias.org/InterfaceTerminology/TLAB>{\n" + 
				"    ?s skos:prefLabel ?p\n" + 
				"    }\n" + 
				" graph<http://erias.org/integration/AnchoringFromWordReducedTOTALCORRIGE>{"+
				"       ?s <http://erias.org/loincTotal#has_system> ?o. \n" + 
				"  }"
				+ " }";
		//http://erias.org/integration/AnchoringFromWordReducedTestEnrichiTOTAL
		//
		//RecuperationGraphRDF lesConceptsTerminologieLOcalEtsystemENLOINC = new RecuperationGraphRDF(conn, "hierarchie", graphAncrageHierarchie);
		System.out.println(" un ");
		HashMap<String,List<String>> ConceptsTerminologieLocalEtSystemEnLoinc= new HashMap<String, List<String>>();
		
		
		 ArrayList<BindingSet> ListeRDF= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, queryGetConceptterminologyandSystem));
		 ArrayList<BindingSet> ListeRDFAlpha= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, queryGetConceptterminologyandSystem2));
		 for(BindingSet e: ListeRDFAlpha){
			 if(!ConceptsTerminologieLocalEtSystemEnLoinc.containsKey(e.getValue("s").stringValue())){
					
				 ConceptsTerminologieLocalEtSystemEnLoinc.put(e.getValue("s").stringValue(), new ArrayList<String>());
				};
				ConceptsTerminologieLocalEtSystemEnLoinc.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());
			 
		 }
		 //}
		 for(BindingSet e: ListeRDF){
			 if(!ConceptsTerminologieLocalEtSystemEnLoinc.containsKey(e.getValue("s").stringValue())){
					
				 ConceptsTerminologieLocalEtSystemEnLoinc.put(e.getValue("s").stringValue(), new ArrayList<String>());
				};
				ConceptsTerminologieLocalEtSystemEnLoinc.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());
			 
		 }
		 
		 System.out.println(" deux ");
		 String queryLOINCconceptandSystem="prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> \n" + 
					"  SELECT distinct ?s ?p ?o \n" + 
					"  	where { \n" + 
					"  	 GRAPH<http://erias.org/FrenchLoinc> { \n" + 
					"  		  ?s <http://erias.org/loincTotal#has_system> ?o.   \n" + 
					"     } \n" + 
					"           \n" + 
					"  }  ";
		 
		 HashMap<String,List<String>> codeLOINCAndSystem= new HashMap<String, List<String>>();
		 
		 ArrayList<BindingSet> ListeRDF2= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, queryLOINCconceptandSystem));
			
		 //}
		 System.out.println(" trois ");
		 for(BindingSet e: ListeRDF2){
			 if(!codeLOINCAndSystem.containsKey(e.getValue("s").stringValue())){
					
				 codeLOINCAndSystem.put(e.getValue("s").stringValue(), new ArrayList<String>());
				};
				codeLOINCAndSystem.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());
			 
		 }
			HashMap<String,Set<String>> codesystemandcodeLOINC=getSystemListeCodeInversion(codeLOINCAndSystem);

			String querygetTerminoAndLOINCconcept="select ?s ?p ?o where "
			 		+ "{"
			 		+ "graph<http://erias.org/integration/AnchoringFilteringClassTOTALCORRIGE>{"
			 		+ " ?s ?p ?o "
			 		+ "} "
			 		+ " }";
			//http://erias.org/integration/AnchoringFilteringClassTOTAL
			//http://erias.org/integration/AnchoringFilteringClassTOTALCORRIGE
			System.out.println(" quatre ");
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
			//System.out.println("codeTerm +"+codeTerm+"*");
			Set<String> Systems = new HashSet<String>();
			if(ConceptsTerminologieLocalEtSystemEnLoinc.containsKey(codeTerm)){
				
				Systems.addAll(ConceptsTerminologieLocalEtSystemEnLoinc.get(codeTerm));
//				if(codeTerm.equals("http://chu-bordeaux.fr/synergie#syn-ana-c0azu")) {
//					System.out.println("Systems "+Systems);
//				}
				//System.out.println("Hierarchie "+Hierarchie);
				
				
				int uv=0;
				for(String sys:Systems){
					//System.out.println(sys);
					
					if(codesystemandcodeLOINC.containsKey(sys)){
						
						for(String codeLoinc : CodeTerminoListeCodeLoincAssocie.get(codeTerm)){
//							if(codeTerm.equals("http://chu-bordeaux.fr/synergie#syn-ana-c0azu")) {
//								System.out.println("codeLoinc "+codeLoinc);
//							}
														
							if(codesystemandcodeLOINC.get(sys).contains(codeLoinc)){
								Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
										vf.createURI("http://erias.org/integration/TLAB/model#ValidateAnchorsBySystem"),
										vf.createURI(codeLoinc));
								if(v<8000000){
										lesRelationSystem.add(hty10);
										v++;
								}else {
									lesRelationSystem2.add(hty10);
								}
										uv++;
								
							}
						}
					}
					else {
						System.out.println("qd56s4sq654q65d4q56q6d5qf4q65f46q5d4a564fq6s5dq564");
					}
				}
				if(uv==0) {
					//System.out.println(codeTerm+" ");
					codePerdu.add(codeTerm);
					
					for(String codloin: CodeTerminoListeCodeLoincAssocie.get(codeTerm)){
						

						
//						if(!codeLOINCAndSystem.containsKey(codloin)){
//							//System.out.println(codloin);
//							Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
//									vf.createURI("http://erias.org/integration/TLAB/model#ValidateAnchorsBySystem"),
//									vf.createURI(codloin));
//							if(v<8000000){
//								
//								lesRelationSystem.add(hty10);
//								v++;
//							}else{
//								lesRelationSystem2.add(hty10);
//							}
//						}
//						else {
//
//							if(codeLOINCAndSystem.get(codloin).size()==0) {
//							Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
//									vf.createURI("http://erias.org/integration/TLAB/model#ValidateAnchorsBySystem"),
//									vf.createURI(codloin));
//							if(v<8000000){
//								
//								lesRelationSystem.add(hty10);
//								v++;
//							}else{
//								lesRelationSystem2.add(hty10);
//							}
//						}
//						
//						}
					
						
						
						Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
								vf.createURI("http://erias.org/integration/TLAB/model#ValidateAnchorsBySystem"),
								vf.createURI(codloin));
						if(i<4000000){
								lesRelationSystem2.add(hty10);
								i++;
						}else{
							lesRelationSystem2.add(hty10);
						}
					}
				//}
//					for(String codloinc: CodeTerminoListeCodeLoincAssocie.get(codeTerm)){
//						//System.out.println(codeTerm+" "+codloinc);
//						Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
//								vf.createURI("http://erias.org/integration/TLAB/model#ValidateAnchorsBySystem"),
//								vf.createURI(codloinc));
//						if(i<8000000){
//							
//							lesRelationSystem.add(hty10);
//								i++;
//						}else{
//							lesRelationSystem2.add(hty10);
//						}
//					}
					
				}
			}
			else{
				for(String codloin: CodeTerminoListeCodeLoincAssocie.get(codeTerm)){
					

					
//					if(!codeLOINCAndSystem.containsKey(codloin)){
//						//System.out.println(codloin);
//						Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
//								vf.createURI("http://erias.org/integration/TLAB/model#ValidateAnchorsBySystem"),
//								vf.createURI(codloin));
//						if(v<8000000){
//							
//							lesRelationSystem.add(hty10);
//							v++;
//						}else{
//							lesRelationSystem2.add(hty10);
//						}
//					}
//					else {
//
//						if(codeLOINCAndSystem.get(codloin).size()==0) {
//						Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
//								vf.createURI("http://erias.org/integration/TLAB/model#ValidateAnchorsBySystem"),
//								vf.createURI(codloin));
//						if(v<8000000){
//							
//							lesRelationSystem.add(hty10);
//							v++;
//						}else{
//							lesRelationSystem2.add(hty10);
//						}
//					}
//					
//					}
				
					
					
					Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
							vf.createURI("http://erias.org/integration/TLAB/model#ValidateAnchorsBySystem"),
							vf.createURI(codloin));
					if(i<4000000){
							lesRelationSystem2.add(hty10);
							i++;
					}else{
						lesRelationSystem2.add(hty10);
					}
				}
				
			
			}
		}
		
		
		
		//http://erias.org/integration/AnchoringFilteringBySystemFull contains all the filtering step that include the limitation to LOINC concepts without system for TLAB concepts without system
		//http://erias.org/integration/AnchoringFilteringBySystem contains the filterign step only in cases of differences in attributes.
		String graphAncrageFiltre=	"http://erias.org/integration/AnchoringFilteringBySystemTOTALCORRIGE";
		
		URI AncrageURI = conn.getValueFactory().createURI(graphAncrageFiltre);
		List<Statement> liste= new ArrayList<Statement>(lesRelationSystem);
		System.out.println("filtrage hierarchir "+liste.size());
		List<Statement> liste2= new ArrayList<Statement>(lesRelationSystem2);
		System.out.println("filtrage hierarchir "+liste2.size());
		System.out.println("codePerdu "+codePerdu);
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
