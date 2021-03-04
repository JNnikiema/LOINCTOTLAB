package dataDrivenFiltering;

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

public class dataDrivenResult {

	public static void main(String[] args) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		// TODO Auto-generated method stub

		String namespace = "TLABANDLOINC";
		Repository repo = RepositoryFactory.getRepository(namespace);
		RepositoryConnection conn = repo.getConnection();
		ValueFactory vf = conn.getValueFactory();
		
		
		Set<Statement> lesRelationProperty= new HashSet<Statement>();
		Set<Statement> lesRelationProperty2= new HashSet<Statement>();
		//String graphAncrageHierarchie=	"http://erias.org/integration/AnchoringCandidats/newHierarchy";	
		String queryGetConceptterminologyandPropery="prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"				select distinct ?s ?o  where \n" + 
				"		 		{ \n" + 
				"		 		graph<http://erias.org/TLABWITHLOINCPropertyTOTALES>{\n" + 
				"		 		 ?s <http://erias.org/TLAB/Property#has_property> ?o.\n" + 
				"                 \n" + 
				"		 		}\n" + 
				"                \n" + 
				"                  \n" + 
				"		 		 }";
		//RecuperationGraphRDF lesConceptsTerminologieLOcalEtsystemENLOINC = new RecuperationGraphRDF(conn, "hierarchie", graphAncrageHierarchie);
		
		HashMap<String,List<String>> ConceptsTerminologieLocalEtPropertyEnLoinc= new HashMap<String, List<String>>();
		
		
		 ArrayList<BindingSet> ListeRDF= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, queryGetConceptterminologyandPropery));
			
		 //}
		 for(BindingSet e: ListeRDF){
			 if(!ConceptsTerminologieLocalEtPropertyEnLoinc.containsKey(e.getValue("s").stringValue())){
					
				 ConceptsTerminologieLocalEtPropertyEnLoinc.put(e.getValue("s").stringValue(), new ArrayList<String>());
				};
				ConceptsTerminologieLocalEtPropertyEnLoinc.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());
			 
		 }
		 
		 String queryLOINCconceptandProperty="prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> \n" + 
		 		"					  SELECT distinct ?s ?p ?o  \n" + 
		 		"					  	where { \n" + 
		 		"					  	 GRAPH<http://erias.org/FrenchLoinc> { \n" + 
		 		"									       ?s <http://erias.org/loincTotal#has_property> ?o. \n" + 
		 		"					     }\n" + 
		 		"					  } ";
		 
		 HashMap<String,List<String>> codeLOINCAndChallenge= new HashMap<String, List<String>>();
		 
		 ArrayList<BindingSet> ListeRDF2= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, queryLOINCconceptandProperty));
			
		 //}
		 for(BindingSet e: ListeRDF2){
			 if(!codeLOINCAndChallenge.containsKey(e.getValue("s").stringValue())){
					
				 codeLOINCAndChallenge.put(e.getValue("s").stringValue(), new ArrayList<String>());
				};
				codeLOINCAndChallenge.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());
			 
		 }
			HashMap<String,Set<String>> codePropertyandcodeLOINC=getSystemListeCodeInversion(codeLOINCAndChallenge);

			String querygetTerminoAndLOINCconcept="select ?s ?p ?o where "
			 		+ "{"
			 		+ "graph<http://erias.org/integration/AnchoringFilteringByMethodTOTALCORRIGE>{"
			 		+ " ?s ?p ?o "
			 		+ "} "
			 		+ " }";
			//http://erias.org/integration/AnchoringFilteringByMethodTOTAL
			//http://erias.org/integration/AnchoringFilteringByMethodCorrectedArticleEncoreReduit
			
				
				
				
				
				//http://erias.org/integration/AnchoringFilteringByMethodFull
			//http://erias.org/integration/AnchoringFilteringByMethodCorrected // for tech filtering full on system conserved
			//http://erias.org/integration/AnchoringFilteringByMethod
			// http://erias.org/integration/AnchoringFilteringByMethodCorrectedArticle
			//http://erias.org/integration/AnchoringFilteringByMethodCorrectedArticleEncoreReduit
			//http://erias.org/integration/AnchoringFilteringByMethodCorrectedArticleEncoreReduitAvecSystemReduit
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
			Set<String> Property = new HashSet<String>();
			if(ConceptsTerminologieLocalEtPropertyEnLoinc.containsKey(codeTerm)){
				
				Property.addAll(ConceptsTerminologieLocalEtPropertyEnLoinc.get(codeTerm));
				//System.out.println("Hierarchie "+Hierarchie);
				
				
				int uv=0;
				for(String pro:Property){
					System.out.println(pro);
					
					if(codePropertyandcodeLOINC.containsKey(pro)){
						
						for(String codeLoinc : CodeTerminoListeCodeLoincAssocie.get(codeTerm)){
														
							if(codePropertyandcodeLOINC.get(pro).contains(codeLoinc)){
								Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
										vf.createURI("http://erias.org/integration/TLAB/model#DataDriven"),
										vf.createURI(codeLoinc));
								if(v<8000000){
									lesRelationProperty.add(hty10);
										v++;
								}else {
									lesRelationProperty2.add(hty10);
								}
										uv++;
								
							}
						}
					}
				}
				if(uv==0) {
					//System.out.println(codeTerm+" ");
					codePerdu.add(codeTerm);
				//}
//					for(String codloinc: CodeTerminoListeCodeLoincAssocie.get(codeTerm)){
//						//System.out.println(codeTerm+" "+codloinc);
//						Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
//								vf.createURI("http://erias.org/integration/TLAB/model#ValidateAnchorsByChallenge"),
//								vf.createURI(codloinc));
//						if(i<8000000){
//							
//							lesRelationChallenge.add(hty10);
//								i++;
//						}else{
//							lesRelationChallenge2.add(hty10);
//						}
//					}
					
				}
			}
			else{
//				for(String codloinc: CodeTerminoListeCodeLoincAssocie.get(codeTerm)){
//					Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
//							vf.createURI("http://erias.org/integration/TLAB/model#DataDriven"),
//							vf.createURI(codloinc));
//					if(i<8000000){
//						lesRelationProperty.add(hty10);
//							i++;
//					}else{
//						lesRelationProperty2.add(hty10);
//					}
//				}
				
			
			}
		}
		
		
		
		
String graphAncrageFiltre=	"http://erias.org/integration/DataDrivenTOTALCORRIGEARTICLE";
		
		URI AncrageURI = conn.getValueFactory().createURI(graphAncrageFiltre);
		List<Statement> liste= new ArrayList<Statement>(lesRelationProperty);
		System.out.println("filtrage hierarchir "+liste.size());
		List<Statement> liste2= new ArrayList<Statement>(lesRelationProperty2);
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
	
	
	public static  HashMap<String, Set<String>> getSystemListeCodeInversion(HashMap<String,List<String>> codeEtChapitre){
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
