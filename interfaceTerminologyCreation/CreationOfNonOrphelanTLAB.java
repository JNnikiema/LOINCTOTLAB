package interfaceTerminologyCreation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.BindingSet;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFHandlerException;

import interactionTripleStore.InteractionBlazgraph;
import interactionTripleStore.RepositoryFactory;

public class CreationOfNonOrphelanTLAB {

	public static void main(String[] args) throws RepositoryException, QueryEvaluationException, MalformedQueryException {
		// TODO Auto-generated method stub
		String namespace="TLAB";
		Repository repo = RepositoryFactory.getRepository(namespace);
		RepositoryConnection conn = repo.getConnection();
	
		String query="prefix skos: <http://www.w3.org/2004/02/skos/core#>   "+ 
				"	prefix syn: <http://chu-bordeaux.fr/synergie#>   "+ 
				"					 construct{?s ?p ?o} WHERE {   "+ 
				"					 GRAPH <http://erias.org/InterfaceTerminology/TLAB> {   "+ 
				" 	                   ?s ?p ?o.				  "+ 
					"                ?s skos:topConceptOf <http://chu-bordeaux.fr/synergie#synergie>.  "+ 
					  "                 filter (str(?s)!=str(syn:syn-codeorph))  "+ 
						" 				 }  "+ 
							" 		 } " ;
		Set<Statement> stemtsMainClass= new HashSet<Statement>();
		GraphQueryResult graphResult = conn.prepareGraphQuery(QueryLanguage.SPARQL, query).evaluate();
		while (graphResult.hasNext()) {
			   Statement st = graphResult.next();
			   stemtsMainClass.add(st);
			  
			}
		
		String query1=" prefix skos: <http://www.w3.org/2004/02/skos/core#> "+
				" prefix syn: <http://chu-bordeaux.fr/synergie#> "+
				" Select ?s ?p ?o WHERE { "+
				" GRAPH <http://erias.org/InterfaceTerminology/TLAB> { "+
				" ?s ?p ?o. "+
					" } "+
				" }";
		
		String query2=" prefix skos: <http://www.w3.org/2004/02/skos/core#> "+
				" prefix syn: <http://chu-bordeaux.fr/synergie#> "+
				" select ?s ?p ?o WHERE { "+
				" GRAPH <http://erias.org/InterfaceTerminology/TLAB> { "+
				" ?s a skos:Concept. "+
					"  ?s skos:broader+ ?p. "+
               " ?p skos:topConceptOf <http://chu-bordeaux.fr/synergie#synergie>."
					+ " } "+
				" }";
		ValueFactory vf = conn.getValueFactory();
		
		 ArrayList<BindingSet> ListeRDFTotal= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query1));
		System.out.println("ok"); 
		ArrayList<BindingSet> ListeRDFCoupleCodeAscendant= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query2));
		HashMap<String, Set<String>> codeListeAscendant= new HashMap<String, Set<String>>();
		Set<String> codeTotal= new HashSet<String>();
		Set<String> codeSansOrphelin= new HashSet<String>();
		Set<String> codeAvecOrphelinUniquement= new HashSet<String>();
		Set<String> codeSansEtAvecOrphelin= new HashSet<String>();
		System.out.println("liste of couple concepts and the main domain");
		
		for(BindingSet e: ListeRDFCoupleCodeAscendant){
			if(!codeListeAscendant.containsKey(e.getValue("s").stringValue())){
				codeListeAscendant.put(e.getValue("s").stringValue(), new HashSet<String>());
			};
			codeListeAscendant.get(e.getValue("s").stringValue()).add(e.getValue("p").stringValue());		
			
		}System.out.println("couple create for each concepts");
		
		for(String code : codeListeAscendant.keySet()){
			codeTotal.add(code);
			//System.out.println(codeListeAscendant.get(code));
			if(codeListeAscendant.get(code).size()==1){
				if(!codeListeAscendant.get(code).contains("http://chu-bordeaux.fr/synergie#syn-codeorph")){
					codeSansOrphelin.add(code);
				}
				else{
					codeAvecOrphelinUniquement.add(code);
				}
				
			
				
			}
			
			else {
				if(codeListeAscendant.get(code).contains("http://chu-bordeaux.fr/synergie#syn-codeorph")){
					codeSansEtAvecOrphelin.add(code);
					
				}
				codeSansOrphelin.add(code);
					
				
				
			}
			
			
		}	
		for(BindingSet e: ListeRDFTotal){
			if(codeSansOrphelin.contains(e.getValue("s").stringValue())){
				
				if(e.getValue("p").stringValue().equals("http://www.w3.org/2004/02/skos/core#notation")||e.getValue("p").stringValue().equals("http://www.w3.org/2004/02/skos/core#prefLabel")){
					Statement tgrd= vf.createStatement(vf.createURI(e.getValue("s").stringValue()), vf.createURI(e.getValue("p").stringValue()), e.getValue("o"));
					stemtsMainClass.add(tgrd);
					
				
				}
				else {
					
					Statement tgrd= vf.createStatement(vf.createURI(e.getValue("s").stringValue()), vf.createURI(e.getValue("p").stringValue()), vf.createURI(e.getValue("o").stringValue()));
					stemtsMainClass.add(tgrd);
					
				
				}
			}
		}
		

		
		List<Statement> liste= new ArrayList<Statement>(stemtsMainClass);
		System.out.println(liste.size());
		String interfaceCorrection = "http://erias.org/InterfaceTerminology/TLAB/MainUnits";
		
		URI graphTerm_URI = conn.getValueFactory().createURI(interfaceCorrection);
		System.out.println("debut d'insertion");
		try {
			

			conn.add(liste,graphTerm_URI);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		System.out.println("fin d'insertion");
		ExportGraph(interfaceCorrection, namespace, "/home/erias/Bureau/TerminologieLocale/TLAB/synergieMainConcepts.rdf");
		System.out.println(" codeTotal "+codeTotal.size());
		System.out.println(" codeSansOrphelin "+codeSansOrphelin.size());
		System.out.println(" codeAvecOrphelinUniquement "+codeAvecOrphelinUniquement.size());
		System.out.println(" codeSansEtAvecOrphelin "+codeSansEtAvecOrphelin.size());
	
		
		
		
		

	}
	public static void ExportGraph(String Grahoque, String namespace, String chemin) throws RepositoryException{
		String filepath= chemin;
		File file = new File(filepath);
		Repository repo=RepositoryFactory.getRepository(namespace);
		try {
			InteractionBlazgraph.getGraphByURI(repo, Grahoque, file);
		} catch (FileNotFoundException | RDFHandlerException | QueryEvaluationException | MalformedQueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
