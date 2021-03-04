package TranslationComparison;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

public class Comparison {

	public static void main(String[] args) throws RepositoryException, QueryEvaluationException, MalformedQueryException {
		// TODO Auto-generated method stub
		String  namespace="EnglishLoinc";
		String  namespaceF="FrenchLOINC";
		
		
		
		String relationhas_System="has_System";
		String relationhas_method="has_method";
		String relationhas_time_Aspect="has_time_Aspect";
		String relationhas_scale="has_scale";
		String relationhas_class="has_class";
		
		String relationhas_component="has_component";
		String relationhas_property="has_property";
		//String relation="has_System";
		
		
		System.out.println("resultMapping");
		Map<String,Set<String>> Comp=resultMapping(namespace, relationhas_component);
		Map<String,Set<String>> Comp2=resultMapping(namespaceF, relationhas_component);
		
		Map<String,Set<String>> Sys=resultMapping(namespace, relationhas_System);
		Map<String,Set<String>> Sys2=resultMapping(namespaceF, relationhas_System);
		
		Map<String,Set<String>> Meth=resultMapping(namespace, relationhas_method);
		Map<String,Set<String>> Meth2=resultMapping(namespaceF, relationhas_method);
		
		Map<String,Set<String>> Time=resultMapping(namespace, relationhas_time_Aspect);
		Map<String,Set<String>> Time2=resultMapping(namespaceF, relationhas_time_Aspect);
		
		Map<String,Set<String>> Scale=resultMapping(namespace, relationhas_scale);
		Map<String,Set<String>> Scale2=resultMapping(namespaceF, relationhas_scale);
		
		Map<String,Set<String>> Cals=resultMapping(namespace, relationhas_class);
		Map<String,Set<String>> Cals2=resultMapping(namespaceF, relationhas_class);
		
		Map<String,Set<String>> Prop=resultMapping(namespace, relationhas_property);
		Map<String,Set<String>> Prop2=resultMapping(namespaceF, relationhas_property);
		
		Set<Statement> resut=new  HashSet<Statement>();
		
		String  namespaceT="CommonLOINC";
		System.out.println("triple store beguinning");
		Repository repo = RepositoryFactory.getRepository(namespaceT);
		repo.initialize();
		RepositoryConnection connnex = repo.getConnection();
		System.out.println("connexion realised");
		
		ValueFactory vf = connnex.getValueFactory();
		
		resut.addAll(getMappingsOfParts(Comp, Comp2, vf));
		//resut.addAll(getMappingsOfParts(Sys, Sys2, vf));
		//resut.addAll(getMappingsOfParts(Meth, Meth2, vf));
		//resut.addAll(getMappingsOfParts(Time, Time2, vf));
		//resut.addAll(getMappingsOfParts(Scale, Scale2, vf));
		//resut.addAll(getMappingsOfParts(Cals, Cals2, vf));
		//resut.addAll(getMappingsOfParts(Prop, Prop2, vf));
		
		
		
		System.out.println("debut de l'insertion");
		
		String GraphLoinc = "http://erias.org/TotalLoincF";
		
		URI graphLoincTotal = connnex.getValueFactory().createURI(GraphLoinc);
		List<Statement> liste= new ArrayList<Statement>(resut);
		System.out.println(liste.size());
	//	System.out.println("connnex "+connnex.getNamespace(GraphLoinc));

		try {	

			connnex.add(liste,graphLoincTotal);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("final terminé");
		
	
	}
	public static Set<Statement> getMappingsOfParts(Map<String,Set<String>> rest1,Map<String,Set<String>>rest2,ValueFactory vf){
		Set<Statement> lesSatement= new HashSet<Statement>();
		for(String codeL:rest1.keySet()) {
			if(rest2.containsKey(codeL)) {
				for(String part: rest1.get(codeL)) {
					for(String part2: rest2.get(codeL)) {
						Statement stm = vf.createStatement(vf.createURI(part),
								vf.createURI("http://erias.org/loincTotal#"+"has_equivalence"),
								vf.createURI(part2));
							lesSatement.add(stm);
						
					}
				}
				
			}
	
		}
		return lesSatement;
		
	}
	
	public static Map<String,Set<String>>resultMappingFrench(String relation) throws RepositoryException, QueryEvaluationException, MalformedQueryException {
		String  namespaceF="FrenchLOINC";
					
		System.out.println("resultMapping");
		Map<String,Set<String>> Comp=resultMapping(namespaceF, relation);
				return Comp;
	
	
	}
	public static Map<String,Set<String>>resultMappingEnglish(String relation) throws RepositoryException, QueryEvaluationException, MalformedQueryException {
		String  namespaceF="EnglishLoinc";
					
		System.out.println("resultMapping");
		Map<String,Set<String>> Comp=resultMapping(namespaceF, relation);
				return Comp;
	
	
	}
	public static Map<String,Set<String>>resultMapping(String namespace, String relation) throws RepositoryException, QueryEvaluationException, MalformedQueryException {
		
		Repository repo = RepositoryFactory.getRepository(namespace);
		RepositoryConnection conn = repo.getConnection();
		Map<String,Set<String>> codeAndPart= new HashMap<String,Set<String>>();
	
		String query="Select ?s ?p ?o WHERE { "
				+" ?s ?p ?o.  "+
				 "  } ";
		ValueFactory vf = conn.getValueFactory();
		
		String prefixe="http://erias.org/loincTotal#";
	//	System.out.println(repo.initialize());
		
		 ArrayList<BindingSet> ListeRDFTotal= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query));
		 System.out.println(relation+" "+namespace+" nombre de triplet : "+ListeRDFTotal.size());
		 System.out.println("ok"); 
		for(BindingSet e: ListeRDFTotal){
			if(!codeAndPart.containsKey(e.getValue("s").stringValue())){
				codeAndPart.put(e.getValue("s").stringValue(), new HashSet<String>());
			};
			
			if(e.getValue("p").stringValue().equals(prefixe+relation)) {
				codeAndPart.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());		
			}
				
		//System.out.println("*"+e.getValue("s").stringValue()+"*");
		}System.out.println("couple create for each concepts");
		
		
		
		return codeAndPart;
		
	}

}
