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

public class TLABconceptAndLOINCproperty {

	public static void main(String[] args) throws RepositoryException, QueryEvaluationException, MalformedQueryException {
		// TODO Auto-generated method stub
		
		String namespace = "TLABANDLOINC";
		Repository repo = RepositoryFactory.getRepository(namespace);
		RepositoryConnection conn = repo.getConnection();
		ValueFactory vf = conn.getValueFactory();
		HashMap<String,Set<String>> resultat1= getTLABandLOINCunits(conn);
		HashMap<String,Set<String>> resultat2= getTLABandLOINCunitsInstantiated(conn);
		String prefixe="http://erias.org/TLAB/Property#";
		Set<Statement> result=new HashSet<Statement>();
		
		for(String code:resultat1.keySet()) {
			Set<String> property= resultat1.get(code);
			for(String pro:property) {
				Statement stm=vf.createStatement(vf.createURI(code), 
						vf.createURI(prefixe + "has_property"),
						vf.createURI(pro));
				result.add(stm);
			}
			
		}
		for(String code:resultat2.keySet()) {
			Set<String> property= resultat2.get(code);
			for(String pro:property) {
				Statement stm=vf.createStatement(vf.createURI(code), 
						vf.createURI(prefixe + "has_property"),
						vf.createURI(pro));
				result.add(stm);
			}
			
		}
		
		//String TLABproperty = "http://erias.org/TLABToUCUM/Property#";
		//String TLABproperty = "http://erias.org/TLABWITHLOINCProperty";
		String TLABproperty = "http://erias.org/TLABWITHLOINCPropertyTOTALES";
		URI TLABpropertyInUCUM = conn.getValueFactory().createURI(TLABproperty);
		List<Statement> liste= new ArrayList<Statement>(result);
		
		System.out.println(liste.size());

		try {	

			conn.add(liste,TLABpropertyInUCUM);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("final terminé");

	}
	
	
	public static HashMap<String,Set<String>> getTLABandLOINCunits(RepositoryConnection conn) throws QueryEvaluationException, RepositoryException, MalformedQueryException{

		String query= "prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"				select distinct ?s ?o ?j ?f where \n" + 
				"		 		{ \n" + 
				"		 		graph<http://erias.org/TLABWITHUCUM/PropertyTOTALESCorrigesArticle>{\n" + 
				"		 		 ?s ?p ?o.\n" + 
				"                 \n" + 
				"		 		}\n" + 
				"                             graph<http://erias.org/ucum>{\n" + 
				"		 		 ?o <http://erias.org/UCUM#kind_of_Quantity>	?j.\n" + 
				"                 \n" + 
				"		 		}\n" + 
				"                                    graph<http://erias.org/UCUM/MappingsPropertyTotal>{\n" + 
				"		 		?j ?d	?f.\n" + 
				"                 \n" + 
				"		 		}\n" + 
				"                  \n" + 
				"		 		 }";
		//http://erias.org/UCUM/MappingsPropertyTotal
		//http://erias.org/UCUM/MappingsProperty
		ArrayList<BindingSet> ListeRDF3= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query));
		HashMap<String,Set<String>> CodeTerminoListeCodeLoincAssocie= new HashMap<String,Set<String>>();
		 for(BindingSet e: ListeRDF3){
			 if(!CodeTerminoListeCodeLoincAssocie.containsKey(e.getValue("s").stringValue())){
					
				 CodeTerminoListeCodeLoincAssocie.put(e.getValue("s").stringValue(), new HashSet<String>());
				};
				CodeTerminoListeCodeLoincAssocie.get(e.getValue("s").stringValue()).add(e.getValue("f").stringValue());
			// System.out.println("e.getValue(\"o\").stringValue() "+e.getValue("o").stringValue());
		 }
		 
		 return CodeTerminoListeCodeLoincAssocie;
	}
	public static HashMap<String,Set<String>> getTLABandLOINCunitsInstantiated(RepositoryConnection conn) throws QueryEvaluationException, RepositoryException, MalformedQueryException{

		String query= "prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"				select distinct ?s ?o ?j ?f where \n" + 
				"		 		{ \n" + 
				"		 		graph<http://erias.org/TLABWITHUCUM/PropertyTOTALESCorrigesArticle>{\n" + 
				"		 		 ?s ?p ?o.\n" + 
				"                 \n" + 
				"		 		}\n" + 
				"                             graph<http://erias.org/InstanciatedUNitsTOTALESCorriges>{\n" + 
				"		 		 ?o <http://erias.org/UCUM#kind_of_Quantity>	?j.\n" + 
				"                 \n" + 
				"		 		}\n" + 
				"                                    graph<http://erias.org/UCUM/MappingsPropertyTotal>{\n" + 
				"		 		?j ?d	?f.\n" + 
				"                 \n" + 
				"		 		}\n" + 
				"                  \n" + 
				"		 		 }";
		
		ArrayList<BindingSet> ListeRDF3= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query));
		HashMap<String,Set<String>> CodeTerminoListeCodeLoincAssocie= new HashMap<String,Set<String>>();
		 for(BindingSet e: ListeRDF3){
			 if(!CodeTerminoListeCodeLoincAssocie.containsKey(e.getValue("s").stringValue())){
					
				 CodeTerminoListeCodeLoincAssocie.put(e.getValue("s").stringValue(), new HashSet<String>());
				};
				CodeTerminoListeCodeLoincAssocie.get(e.getValue("s").stringValue()).add(e.getValue("f").stringValue());
			// System.out.println("e.getValue(\"o\").stringValue() "+e.getValue("o").stringValue());
		 }
		 
		 return CodeTerminoListeCodeLoincAssocie;
	}

}
