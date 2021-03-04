package TranslationComparison;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import interactionTripleStore.InteractionBlazgraph;
import interactionTripleStore.RepositoryFactory;

public class ClassConstructionOfComparison {

	public static void main(String[] args) throws RepositoryException, QueryEvaluationException, MalformedQueryException {
		// TODO Auto-generated method stub
		String relationhas_System="has_system";
		String relationhas_method="has_method";
		String relationhas_time_Aspect="has_time";
		String relationhas_scale="has_scale";
		String relationhas_class="has_class";
		
		String relationhas_component="has_component";
		String relationhas_property="has_property";
		
		String relationhas_modifier_time="has_modifier_time";
		String relationhas_super_system="has_super_system";
		String relationhas_challenge="has_challenge";
		String relationhas_adjustment="has_adjustment";
	
		String GraphLoincFrancais = "http://erias.org/FrenchLoinc";
		//String GraphLoincFrancais = "http://erias.org/constructed/englishLoinc";
		String Queryhas_System=getquery(GraphLoincFrancais,relationhas_System);
		String Queryhas_method=getquery(GraphLoincFrancais,relationhas_method);
		String Queryhas_time_Aspect=getquery(GraphLoincFrancais,relationhas_time_Aspect);
		String Queryhas_scale=getquery(GraphLoincFrancais,relationhas_scale);
		String Queryhas_class=getquery(GraphLoincFrancais,relationhas_class);
		
		String Queryhas_component=getquery(GraphLoincFrancais,relationhas_component);
		String Queryhas_property=getquery(GraphLoincFrancais,relationhas_property);
		
		String Queryhas_SystemSimple=getquerySimple(GraphLoincFrancais,relationhas_System);
		String Queryhas_methodSimple=getquerySimple(GraphLoincFrancais,relationhas_method);
		String Queryhas_time_AspectSimple=getquerySimple(GraphLoincFrancais,relationhas_time_Aspect);
		String Queryhas_scaleSimple=getquerySimple(GraphLoincFrancais,relationhas_scale);
		String Queryhas_classSimple=getquerySimple(GraphLoincFrancais,relationhas_class);
		
		String Queryhas_componentSimple=getquerySimple(GraphLoincFrancais,relationhas_component);
		String Queryhas_propertySimple=getquerySimple(GraphLoincFrancais,relationhas_property);
		
		String Queryhas_modifier_time=getquerySupart(GraphLoincFrancais,relationhas_modifier_time);
		String Queryhas_super_system=getquerySupart(GraphLoincFrancais,relationhas_super_system);
		String Queryhas_challenge=getquerySupart(GraphLoincFrancais,relationhas_challenge);
		String Queryhas_adjustment=getquerySupart(GraphLoincFrancais,relationhas_adjustment);
		
		//String namespace="LOINCTotal";
		//String namespace = "ENGLISHLOINC";
		String namespace="FrenchLoincAnalysis";
		
		Repository repo = RepositoryFactory.getRepository(namespace);
		RepositoryConnection conn = repo.getConnection();
		
	//	ValueFactory vf = conn.getValueFactory();
		
		Map<String, Set<String>> has_System=getresults(Queryhas_System,conn);
		Map<String, Set<String>> has_method=getresults(Queryhas_method,conn);
		Map<String, Set<String>> has_time_Aspect=getresults(Queryhas_time_Aspect,conn);
		Map<String, Set<String>> has_scale=getresults(Queryhas_scale,conn);
		Map<String, Set<String>> has_class=getresults(Queryhas_class,conn);
		
		Map<String, Set<String>> has_component=getresults(Queryhas_component,conn);
		Map<String, Set<String>> has_property=getresults(Queryhas_property,conn);
		
		
		Set<String> has_SystemSimple=getresultsSimple(Queryhas_SystemSimple,conn);
		Set<String> has_methodSimple=getresultsSimple(Queryhas_methodSimple,conn);
		Set<String> has_time_AspectSimple=getresultsSimple(Queryhas_time_AspectSimple,conn);
		Set<String> has_scaleSimple=getresultsSimple(Queryhas_scaleSimple,conn);
		Set<String> has_classSimple=getresultsSimple(Queryhas_classSimple,conn);
		
		Set<String> has_componentSimple=getresultsSimple(Queryhas_componentSimple,conn);
		Set<String> has_propertySimple=getresultsSimple(Queryhas_propertySimple,conn);
		
		
		Map<String, Set<String>> has_modifier_time=getresults(Queryhas_modifier_time,conn);
		Map<String, Set<String>> has_super_system=getresults(Queryhas_super_system,conn);
		Map<String, Set<String>> has_challenge=getresults(Queryhas_challenge,conn);
		Map<String, Set<String>> has_adjustment=getresults(Queryhas_adjustment,conn);
		
		
		System.out.println("getcardinalitytotal(has_component).size() "+has_componentSimple.size());
		System.out.println("getcardinalitytotal(has_property).size() "+has_propertySimple.size());
		System.out.println("getcardinalitytotal(has_time_Aspect).size()"+has_time_AspectSimple.size());
		System.out.println("getcardinalitytotal(has_System).size() "+has_SystemSimple.size());
		System.out.println("getcardinalitytotal(has_scale).size() "+has_scaleSimple.size());
		System.out.println("getcardinalitytotal(has_method).size() "+has_methodSimple.size());
		System.out.println("getcardinalitytotal(has_class).size() "+has_classSimple.size());
		int uuii =0;
		Set<String> resss =getcardinality0(has_component,has_componentSimple);
		for(String a :resss) {
			if(uuii<=4) {
				System.out.println("==> "+a);
				uuii++;
			}
		}
				
		
		//System.out.println("getcardinalityt0(has_component).size() "+getcardinality0(has_component,has_componentSimple).size());
		System.out.println("getcardinalityt0(has_property).size() "+getcardinality0(has_property,has_propertySimple).size());
		System.out.println("getcardinalityt0(has_time_Aspect).size()"+getcardinality0(has_time_Aspect,has_time_AspectSimple).size());
		System.out.println("getcardinalityt0(has_System).size() "+getcardinality0(has_System,has_SystemSimple));
		System.out.println("getcardinalityt0(has_scale).size() "+getcardinality0(has_scale,has_scaleSimple).size());
		System.out.println("getcardinalityt0(has_method).size() "+getcardinality0(has_method,has_methodSimple).size());
		System.out.println("getcardinalityt0(has_class).size() "+getcardinality0(has_class,has_classSimple).size());
		
		System.out.println("getcardinality0(has_challenge).size() "+getcardinality0(has_challenge).size());
		System.out.println("getcardinalityt0(has_modifier_time).size() "+getcardinality0(has_modifier_time).size());
		System.out.println("getcardinalityt0(has_adjustment).size() "+getcardinality0(has_adjustment).size());
		System.out.println("getcardinalityt0(has_super_system).size() "+getcardinality0(has_super_system).size());
		
		
		System.out.println("getcardinality1(has_component).size() "+getcardinality1(has_component).size());
		System.out.println("getcardinality1(has_property).size() "+getcardinality1(has_property).size());
		System.out.println("getcardinality1(has_time_Aspect).size()"+getcardinality1(has_time_Aspect).size());
		System.out.println("getcardinality1(has_System).size() "+getcardinality1(has_System));
		System.out.println("getcardinality1(has_scale).size() "+getcardinality1(has_scale).size());
		System.out.println("getcardinality1(has_method).size() "+getcardinality1(has_method).size());
		System.out.println("getcardinality1(has_class).size() "+getcardinality1(has_class).size());
		
		System.out.println("getcardinality1(has_challenge).size() "+getcardinality1(has_challenge).size());
		System.out.println("getcardinality1(has_modifier_time).size() "+getcardinality1(has_modifier_time).size());
		System.out.println("getcardinality1(has_adjustment).size() "+getcardinality1(has_adjustment).size());
		System.out.println("getcardinality1(has_super_system).size() "+getcardinality1(has_super_system).size());
		
		
		
		System.out.println("getcardinalityN(has_component).size() "+getcardinalityN(has_component).size());
		System.out.println("getcardinalityN(has_property).size() "+getcardinalityN(has_property).size());
		System.out.println("getcardinalityN(has_time_Aspect).size()"+getcardinalityN(has_time_Aspect).size());
		System.out.println("getcardinalityN(has_System).size() "+getcardinalityN(has_System));
		System.out.println("getcardinalityN(has_scale).size() "+getcardinalityN(has_scale).size());
		System.out.println("getcardinalityN(has_method).size() "+getcardinalityN(has_method).size());
		System.out.println("getcardinalityN(has_class).size() "+getcardinalityN(has_class).size());
		
		System.out.println("getcardinalityN(has_challenge).size() "+getcardinalityN(has_challenge).size());
		System.out.println("getcardinalityN(has_modifier_time).size() "+getcardinalityN(has_modifier_time).size());
		System.out.println("getcardinalityN(has_adjustment).size() "+getcardinalityN(has_adjustment).size());
		System.out.println("getcardinalityN(has_super_system).size() "+getcardinalityN(has_super_system).size());
		
		
		
		System.out.println("getcardinalityN(has_component).size() "+getRDFtoconstructcardinalityN(has_component));
		System.out.println("getcardinalityN(has_property).size() "+getRDFtoconstructcardinalityN(has_property));
		System.out.println("getcardinalityN(has_time_Aspect).size()"+getRDFtoconstructcardinalityN(has_time_Aspect).size());
		System.out.println("getcardinalityN(has_System).size() "+getRDFtoconstructcardinalityN(has_System).size());
		System.out.println("getcardinalityN(has_scale).size() "+getRDFtoconstructcardinalityN(has_scale));
		System.out.println("getcardinalityN(has_method).size() "+getRDFtoconstructcardinalityN(has_method).size());
		System.out.println("getcardinalityN(has_class).size() "+getRDFtoconstructcardinalityN(has_class).size());
		
		
		System.out.println("getcardinalityN(has_challenge).size() "+getRDFtoconstructcardinalityN(has_challenge));
		System.out.println("getcardinalityN(has_modifier_time).size() "+getRDFtoconstructcardinalityN(has_modifier_time).size());
		System.out.println("getcardinalityN(has_adjustment).size() "+getRDFtoconstructcardinalityN(has_adjustment).size());
		System.out.println("getcardinalityN(has_super_system).size() "+getRDFtoconstructcardinalityN(has_super_system).size());
	
	
	
		
		

	}
	public static Map<String, Set<String>> getresults(String query, RepositoryConnection conn) throws QueryEvaluationException, RepositoryException, MalformedQueryException{
		
		Map<String, Set<String>> result = new HashMap<String, Set<String>>();
			
			 ArrayList<BindingSet> ListeRDFTotal= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query));
				for(BindingSet e: ListeRDFTotal){
				if(!result.containsKey(e.getValue("p").stringValue())){
					result.put(e.getValue("p").stringValue(), new HashSet<String>());
				}
				result.get(e.getValue("p").stringValue()).add(e.getValue("o").stringValue());
				
				
					
			//System.out.println("*"+e.getValue("s").stringValue()+"*");
			}System.out.println("couple create for each concepts");
			
			return result;
			
	}
public static Set<String> getresultsSimple(String query, RepositoryConnection conn) throws QueryEvaluationException, RepositoryException, MalformedQueryException{
		
		Set<String> result = new HashSet<String>();
			
			 ArrayList<BindingSet> ListeRDFTotal= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query));
				for(BindingSet e: ListeRDFTotal){
				
					result.add(e.getValue("p").stringValue()); 
				
				
				
					
			//System.out.println("*"+e.getValue("s").stringValue()+"*");
			}System.out.println("couple create for each concepts");
			
			return result;
			
	}
	public static String getquerySimple(String constructedgraph, String a) {
		String query="prefix skos: <http://www.w3.org/2004/02/skos/core#> "
				+"	prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "	 Select distinct ?s ?p ?o   WHERE { "
				+ "	 GRAPH <"+constructedgraph+"> {  "
				+ "	?s <http://erias.org/loincTotal#"+a+"> ?p. "
				+ "			 } "
				+ "							 }";
		return query;
	}
	public static String getquery(String constructedgraph, String a) {
		String query="prefix skos: <http://www.w3.org/2004/02/skos/core#> "
				+"	prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "	 Select distinct ?s ?p ?o   WHERE { "
				+ "	 GRAPH <"+constructedgraph+"> {  "
				+ "	?s <http://erias.org/loincTotal#"+a+"> ?p. "
				+ "			 } "
				+ "       GRAPH <http://erias.org/EnglishLoinc> {  "
				+ "		?s <http://erias.org/loincTotal#"+a+"> ?o. "
				+ "				 }  "
				+ "							 }";
		return query;
	}
	public static String getquerySupart(String constructedgraph, String a) {
		String query="prefix skos: <http://www.w3.org/2004/02/skos/core#> "
				+"	prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "	 Select distinct ?s ?p ?o   WHERE { "
				+ "	 GRAPH <"+constructedgraph+"> {  "
				+ "	?s ?j ?f. "
				+ "	?f <http://erias.org/loincTotal#"+a+"> ?p. "
				+ "			 } "
				+ "       GRAPH <http://erias.org/EnglishLoinc> {  "
				+ "		?s <http://erias.org/loincTotal#"+a+"> ?o. "
				+ "				 }  "
				+ "							 }";
		return query;
	}
	public static Map<String, Set<String>>getRDFtoconstructcardinality1(Map<String, Set<String>> hastotest) {
		
		 Map<String, Set<String>> result= new  HashMap<String, Set<String>>();
		
		for(String code: hastotest.keySet()) {
			if(hastotest.get(code).size()==1) {
				if(!result.containsKey(code)) {
					result.put(code, new HashSet<String>());
				}
				result.get(code).addAll(hastotest.get(code));
			}
		}
		
		
		return result;
	}
	public static Map<String, Set<String>>getRDFtoconstructcardinalityN(Map<String, Set<String>> hastotest) {
		
		 Map<String, Set<String>> result= new  HashMap<String, Set<String>>();
		
		for(String code: hastotest.keySet()) {
			if(hastotest.get(code).size()>1) {
				if(!result.containsKey(code)) {
					result.put(code, new HashSet<String>());
				}
				result.get(code).addAll(hastotest.get(code));
			}
		}
		
		
		return result;
	}
	
	
public static Set<String>getcardinality1(Map<String, Set<String>> hastotest) {
		
		Set<String> result= new HashSet<String>();
		
		for(String code: hastotest.keySet()) {
			if(hastotest.get(code).size()==1) {
				result.add(code);
			}
		}
		
		
		return result;
	}	
	

public static Set<String>getcardinality0(Map<String, Set<String>> hastotest, Set<String> totalList) {
	
	Set<String> result= new HashSet<String>();
	for(String code :totalList) {
		if(!hastotest.containsKey(code)) {
			result.add(code);
		}
	}
	
	
	
	return result;
}

public static Set<String>getcardinality0(Map<String, Set<String>> hastotest) {
		
		Set<String> result= new HashSet<String>();
		
		for(String code: hastotest.keySet()) {
			if(hastotest.get(code).isEmpty()) {
				result.add(code);
			}
		}
		
		
		return result;
	}
public static Set<String>getcardinalityN(Map<String, Set<String>> hastotest) {
	
	Set<String> result= new HashSet<String>();
	
	for(String code: hastotest.keySet()) {
		if(hastotest.get(code).size()>1) {
			result.add(code);
		}
	}
	
	
	return result;
}


}
