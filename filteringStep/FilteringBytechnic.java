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

public class FilteringBytechnic {
	public FilteringBytechnic(RepositoryConnection conn) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		
		
		ValueFactory vf = conn.getValueFactory();
		Set<Statement> lesRelationTech= new HashSet<Statement>();
		Set<Statement> lesRelationTech2= new HashSet<Statement>();
		Set<Statement> lesRelationTech3= new HashSet<Statement>();
		Set<Statement> lesRelationTech4= new HashSet<Statement>();		
		Set<Statement> lesRelationTech5= new HashSet<Statement>();
		System.out.println("débutons");
		//String graphAncrageHierarchie=	"http://erias.org/integration/AnchoringCandidats/newHierarchy";	
		String queryGetConceptterminologyandSystem="prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> \n" + 
				"prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"select distinct ?s ?p ?o\n" + 
				"where{\n" + 
				"  graph<http://erias.org/InterfaceTerminology/TLAB>{\n" + 
				"    ?s skos:prefLabel ?p\n" + 
				"    }\n" + 
				" graph<http://erias.org/integration/AnchoringFromWordReducedEnrichiTOTALCORRIGE>{"+
				"       ?s <http://erias.org/loincTotal#has_method> ?o. \n" + 
				"  } "
				+ " } ";
		
		String queryGetConceptterminologyandSystem2="prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> \n" + 
				"prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"select distinct ?s ?p ?o\n" + 
				"where{\n" + 
				"  graph<http://erias.org/InterfaceTerminology/TLAB>{\n" + 
				"    ?s skos:prefLabel ?p\n" + 
				"    }\n" + 
				" graph<http://erias.org/integration/AnchoringFromWordReducedTOTALCORRIGE>{"+
				"       ?s <http://erias.org/loincTotal#has_method> ?o. \n" + 
				"  } "
				+ " } ";
		//RecuperationGraphRDF lesConceptsTerminologieLOcalEtsystemENLOINC = new RecuperationGraphRDF(conn, "hierarchie", graphAncrageHierarchie);
		
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
		 System.out.println("step 1");
		 String queryLOINCconceptandSystem="prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> \n" + 
					"  SELECT distinct ?s ?p ?o \n" + 
					"  	where { \n" + 
					"  	 GRAPH<http://erias.org/FrenchLoinc> { \n" + 
					"  		  ?s <http://erias.org/loincTotal#has_method> ?o.   \n" + 
					"     } \n" + 
					"           \n" + 
					"  }  ";
		 
		 HashMap<String,List<String>> codeLOINCAndSystem= new HashMap<String, List<String>>();
		 
		 ArrayList<BindingSet> ListeRDF2= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, queryLOINCconceptandSystem));
			
		 //}
		 for(BindingSet e: ListeRDF2){
			 if(!codeLOINCAndSystem.containsKey(e.getValue("s").stringValue())){
					
				 codeLOINCAndSystem.put(e.getValue("s").stringValue(), new ArrayList<String>());
				};
				codeLOINCAndSystem.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());
			 
		 }
			HashMap<String,Set<String>> codesystemandcodeLOINC=getSystemListeCodeInversion(codeLOINCAndSystem);
			System.out.println("step 2");
//			String querygetTerminoAndLOINCconcept="select ?s ?p ?o where "
//			 		+ "{"
//			 		+ "graph<http://erias.org/integration/AnchoringFilteringBySystemReduced>{"
//			 		+ " ?s ?p ?o "
//			 		+ "} "
//			 		+ " }";
			//http://erias.org/integration/AnchoringFilteringBySystemTOTALCORRIGE
			String querygetTerminoAndLOINCconcept="select ?s ?p ?o where "
			 		+ "{"
			 		+ "graph<http://erias.org/integration/AnchoringFilteringBySystemTOTALCORRIGE>{"
			 		+ " ?s ?p ?o "
			 		+ "} "
			 		+ " }";
//			String querygetTerminoAndLOINCconcept="select ?s ?p ?o where "
//			 		+ "{"
//			 		+ "graph<http://erias.org/integration/AnchoringFilteringSystem>{"
//			 		+ " ?s ?p ?o "
//			 		+ "} "
//			 		+ " }";
			//http://erias.org/integration/AnchoringFilteringBySystemTOTAL
			System.out.println("a revoir ");
			
			ArrayList<BindingSet> ListeRDF3= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, querygetTerminoAndLOINCconcept));
			HashMap<String,List<String>> CodeTerminoListeCodeLoincAssocie= new HashMap<String, List<String>>();
			System.out.println("step 3 final");
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
			Set<String> Technique = new HashSet<String>();
			if(ConceptsTerminologieLocalEtSystemEnLoinc.containsKey(codeTerm)){
				
				Technique.addAll(ConceptsTerminologieLocalEtSystemEnLoinc.get(codeTerm));
				//System.out.println("Hierarchie "+Hierarchie);
				
				
				int uv=0;
				for(String tec:Technique){
					//System.out.println(sys);
					
					if(codesystemandcodeLOINC.containsKey(tec)){
						
						for(String codeLoinc : CodeTerminoListeCodeLoincAssocie.get(codeTerm)){
														
							if(codesystemandcodeLOINC.get(tec).contains(codeLoinc)){
								Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
										vf.createURI("http://erias.org/integration/TLAB/model#ValidateAnchorsByMethod"),
										vf.createURI(codeLoinc));
								if(v<200000){
									lesRelationTech.add(hty10);
										v++;
								}else if(v<400000){
									lesRelationTech2.add(hty10);
									v++;
								}
								else if(v<600000) {
									lesRelationTech3.add(hty10);
									v++;
								}
								else if(v<800000) {
									lesRelationTech4.add(hty10);
									v++;
								}
								else {
									lesRelationTech5.add(hty10);
									v++;
								}
										uv++;
								
							}
						}
					}
				}
				if(uv==0) {
					//System.out.println(codeTerm+" ");
					if(!Technique.isEmpty()) {
					codePerdu.add(codeTerm);
					
				//}
					
					for(String codloin: CodeTerminoListeCodeLoincAssocie.get(codeTerm)){
						
						if(!codeLOINCAndSystem.containsKey(codloin)){
							//System.out.println(codloin);
							Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
									vf.createURI("http://erias.org/integration/TLAB/model#ValidateAnchorsByMethod"),
									vf.createURI(codloin));
							if(v<200000){
								lesRelationTech.add(hty10);
									v++;
							}else if(v<400000){
								lesRelationTech2.add(hty10);
								v++;
							}
							else if(v<600000) {
								lesRelationTech3.add(hty10);
								v++;
							}
							else if(v<800000) {
								lesRelationTech4.add(hty10);
								v++;
							}
							else {
								lesRelationTech5.add(hty10);
								v++;
							}
						}
						else {

							if(codeLOINCAndSystem.get(codloin).size()==0) {
							Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
									vf.createURI("http://erias.org/integration/TLAB/model#ValidateAnchorsByMethod"),
									vf.createURI(codloin));
							if(v<200000){
								lesRelationTech.add(hty10);
									v++;
							}else if(v<400000){
								lesRelationTech2.add(hty10);
								v++;
							}
							else if(v<600000) {
								lesRelationTech3.add(hty10);
								v++;
							}
							else if(v<800000) {
								lesRelationTech4.add(hty10);
								v++;
							}
							else {
								lesRelationTech5.add(hty10);
								v++;
							}
						}
						
						}
					}
					//pour tout remettre
//					for(String codloinc: CodeTerminoListeCodeLoincAssocie.get(codeTerm)){
//						//System.out.println(codeTerm+" "+codloinc);
//						Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
//								vf.createURI("http://erias.org/integration/TLAB/model#ValidateAnchorsByMethod"),
//								vf.createURI(codloinc));
//						if(i<100000){
//							
//							lesRelationTech.add(hty10);
//								i++;
//						}else if(i<200000){
//							lesRelationTech2.add(hty10);
//							i++;
//						}
//						
//						else if(i<400000) {
//							lesRelationTech3.add(hty10);
//							i++;
//						}
//						else if(i<600000) {
//							lesRelationTech4.add(hty10);
//							i++;
//						}
//						else {
//							lesRelationTech5.add(hty10);
//							i++;
//						}
//					}
					}
				
				}
			}
			else{
				

				
				for(String codloin: CodeTerminoListeCodeLoincAssocie.get(codeTerm)){
					
					if(!codeLOINCAndSystem.containsKey(codloin)){
						//System.out.println(codloin);
						Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
								vf.createURI("http://erias.org/integration/TLAB/model#ValidateAnchorsByMethod"),
								vf.createURI(codloin));
						if(v<200000){
							lesRelationTech.add(hty10);
								v++;
						}else if(v<400000){
							lesRelationTech2.add(hty10);
							v++;
						}
						else if(v<600000) {
							lesRelationTech3.add(hty10);
							v++;
						}
						else if(v<800000) {
							lesRelationTech4.add(hty10);
							v++;
						}
						else {
							lesRelationTech5.add(hty10);
							v++;
						}
					}
					else {

						if(codeLOINCAndSystem.get(codloin).size()==0) {
						Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
								vf.createURI("http://erias.org/integration/TLAB/model#ValidateAnchorsByMethod"),
								vf.createURI(codloin));
						if(v<200000){
							lesRelationTech.add(hty10);
								v++;
						}else if(v<400000){
							lesRelationTech2.add(hty10);
							v++;
						}
						else if(v<600000) {
							lesRelationTech3.add(hty10);
							v++;
						}
						else if(v<800000) {
							lesRelationTech4.add(hty10);
							v++;
						}
						else {
							lesRelationTech5.add(hty10);
							v++;
						}
					}
					
					}
				}
				
				
			
//				for(String codloinc: CodeTerminoListeCodeLoincAssocie.get(codeTerm)){
//					Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
//							vf.createURI("http://erias.org/integration/TLAB/model#ValidateAnchorsByMethod"),
//							vf.createURI(codloinc));
//					if(i<100000){
//						lesRelationTech2.add(hty10);
//							i++;
//					}
//					else if(i<200000) {
//						lesRelationTech3.add(hty10);
//						i++;
//					}
//					else if(i<300000) {
//						lesRelationTech4.add(hty10);
//						i++;
//					}
//					else {
//						lesRelationTech5.add(hty10);
//						i++;
//					}
//				}
				
			
			}
		}
		
		
		// full for the full filtering in biological systems
		//String graphAncrageFiltre=	"http://erias.org/integration/AnchoringFilteringByMethodFull";		
//String graphAncrageFiltre=	"http://erias.org/integration/AnchoringFilteringByMethod";
		//String graphAncrageFiltre=	"http://erias.org/integration/AnchoringFilteringByMethodCorrectedArticleEncoreReduitAvecSystemReduit";
		
		String graphAncrageFiltre=	"http://erias.org/integration/AnchoringFilteringByMethodTOTALCORRIGE";
		URI AncrageURI = conn.getValueFactory().createURI(graphAncrageFiltre);
		List<Statement> liste= new ArrayList<Statement>(lesRelationTech);
		System.out.println("filtrage hierarchir "+liste.size());
		List<Statement> liste2= new ArrayList<Statement>(lesRelationTech2);
		System.out.println("filtrage hierarchir "+liste2.size());
		List<Statement> liste3= new ArrayList<Statement>(lesRelationTech3);
		System.out.println("filtrage hierarchir "+liste3.size());
		List<Statement> liste4= new ArrayList<Statement>(lesRelationTech4);
		System.out.println("filtrage hierarchir "+liste4.size());
		List<Statement> liste5= new ArrayList<Statement>(lesRelationTech5);
		System.out.println("filtrage hierarchir "+liste5.size());
		
		
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
		System.out.println("troisieme parti debut ");
		try {
			
			conn.add(liste3,AncrageURI);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("quatrieme parti debut ");
		try {
			
			conn.add(liste4,AncrageURI);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("cinquieme parti debut ");
		try {
			
			conn.add(liste5,AncrageURI);
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

