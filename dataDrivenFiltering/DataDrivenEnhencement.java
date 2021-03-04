package dataDrivenFiltering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import interactionTripleStore.InteractionBlazgraph;
import interactionTripleStore.RepositoryFactory;
import utilitaries.Couple;

public class DataDrivenEnhencement {

	public static void main(String[] args) throws RepositoryException, QueryEvaluationException, MalformedQueryException {
		// TODO Auto-generated method stub
		String namespace = "TLABANDLOINC";
		Repository repo = RepositoryFactory.getRepository(namespace);
		RepositoryConnection conn = repo.getConnection();
		GetmappingsDebut(conn);

	}
	
	public static void GetmappingsDebut(RepositoryConnection conn) throws QueryEvaluationException, RepositoryException, MalformedQueryException{
//		String querygetTerminoAndLOINCconcept= " prefix skos: <http://www.w3.org/2004/02/skos/core#> "
//				+ "select ?s ?p ?o where "
//		 		+ "{ "
//		 		+ "graph<http://erias.org/integration/AnchoringFilteringMethod>{"
//		 		+ " ?s ?p ?o "
//		 		+ "} "
//		 		+ " }";
		String querygetTerminoAndLOINCconcept= "prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> \n" + 
				"					  SELECT distinct ?s ?o \n" + 
				"					  	where { \n" + 
				"					  	 GRAPH<http://erias.org/integration/DataDrivenTOTALCORRIGEARTICLE> {  \n" + 
				"									       ?s ?p ?o. \n" + 
				"					     }\n" + 
				"					  }\n";
		//http://erias.org/integration/DataDrivenTOTALCORRIGEARTICLE
		//http://erias.org/integration/DataDriven
		//http://erias.org/integration/AnchoringCandidats
		//http://erias.org/integration/AnchoringFilteringBySystem
		//http://erias.org/integration/AnchoringFilteringByMethodCorrectedArticleEncoreReduit
		// http://erias.org/integration/AnchoringFilteringClass
		
		System.out.println("setp");
		ArrayList<BindingSet> ListeRDF3= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, querygetTerminoAndLOINCconcept));
		HashMap<String,Set<String>> CodeTerminoListeCodeLoinc= new HashMap<String, Set<String>>();
		System.out.println("step 3 final");
		 //}
		int iu=0;
		 for(BindingSet e: ListeRDF3){
			 iu++;
			 //Couple a= new Couple(e.getValue("s").stringValue(), Integer.toString(i));
			 if(!CodeTerminoListeCodeLoinc.containsKey(e.getValue("s").stringValue())){
				 
				 CodeTerminoListeCodeLoinc.put(e.getValue("s").stringValue(), new HashSet<String>());
				};
				CodeTerminoListeCodeLoinc.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());
				//CodeTerminoListeCodeLoincAssocie.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());
			// System.out.println("e.getValue(\"o\").stringValue() "+e.getValue("o").stringValue());
		 }
		 Set<String>loinccodeUsed= new HashSet<String>();
		 Set<String>Tlaccardinalite1= new HashSet<String>();
		 
		 for(String tlab :CodeTerminoListeCodeLoinc.keySet()) {
			 if(CodeTerminoListeCodeLoinc.get(tlab).size()==1) {
				 loinccodeUsed.addAll(CodeTerminoListeCodeLoinc.get(tlab));
				 Tlaccardinalite1.add(tlab);
			 }
		 }
		 HashMap<String, Set<String>> resultFinal=getOneToOnemapping(CodeTerminoListeCodeLoinc, loinccodeUsed);
		 HashMap<String, Set<String>> resultatFinalmethod=GetmappingsMethod(conn);
		 for(String code: resultatFinalmethod.keySet()) {
			 if(!resultFinal.containsKey(code)) {
				 resultFinal.put(code, new HashSet<String>());
				 resultFinal.get(code).addAll(resultatFinalmethod.get(code));
			 }
		 }
		 
		 Set<String> Loincconcept= new HashSet<String>();
			for(String Concepts: resultFinal.keySet()) {
				Loincconcept.addAll(resultFinal.get(Concepts));
			}
		 HashMap<Couple,String> result= GetmappingsOrdonne(resultFinal);
		 
			
			int ab= result.size();
			//int ab=5;
			int a=ab/2;
			int medianePlace=0;
			if(a*2<ab) {
				medianePlace=a+1;
			}
			
			int i=0;
					int mediane=0;
					int medianeCal=0;
					int mediane1=0;
					int mediane2=0;
					int maximum=0;
					int pair=0;
					int cardinalite1=0;
					//Set<String> Loincconcept= new HashSet<String>();
					for(Couple Concepts: result.keySet()) {
						//Loincconcept.add(result.get(Concepts));
						i++;
						int max=Integer.parseInt(result.get(Concepts));
						if(max==1) {
							cardinalite1++;
						}
						if(maximum<max) {
							maximum=max;
						}
						if(medianePlace>0) {
							if(Integer.parseInt(Concepts.y)==medianePlace) {
								mediane=Integer.parseInt(result.get(Concepts));
								
							}
						}
						else {
							pair=2;
							if(Integer.parseInt(Concepts.y)==a) {
								mediane1=Integer.parseInt(result.get(Concepts));
							}
							if(Integer.parseInt(Concepts.y)==a+1) {
								mediane2=Integer.parseInt(result.get(Concepts));
							}
						}
					}
					//System.out.println(mediane1);
					//System.out.println(mediane2);
					medianeCal=(mediane1+mediane2)/2;
			if(pair==2) {
				System.out.println("mediane "+medianeCal);
			}
			else {
				System.out.println("mediane "+mediane);
			}
			System.out.println("maximum "+maximum);
			System.out.println("cardinalite1 "+cardinalite1);
			System.out.println("total non 1-0 "+result.keySet().size());
		 System.out.println("resultFinal "+resultFinal.size());
		 System.out.println("Loincconcept "+Loincconcept.size());
		
	}
	
	public static HashMap<String, Set<String>> GetmappingsMethod(RepositoryConnection conn) throws QueryEvaluationException, RepositoryException, MalformedQueryException{
//		String querygetTerminoAndLOINCconcept= " prefix skos: <http://www.w3.org/2004/02/skos/core#> "
//				+ "select ?s ?p ?o where "
//		 		+ "{ "
//		 		+ "graph<http://erias.org/integration/AnchoringFilteringMethod>{"
//		 		+ " ?s ?p ?o "
//		 		+ "} "
//		 		+ " }";
		String querygetTerminoAndLOINCconcept= "prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> \n" + 
				"					  SELECT distinct ?s ?o \n" + 
				"					  	where { \n" + 
				"    GRAPH<http://erias.org/TLABWITHLOINCProperty> {  \n" + 
					"									       ?s ?d ?j. \n" + 
					"					     }\n" +
				"					  	 GRAPH<http://erias.org/integration/AnchoringFilteringByMethodCorrectedArticleEncoreReduit> {  \n" + 
				"									       ?s ?p ?o. \n" + 
				"					     }\n" + 
				"					  }\n";
		
		//http://erias.org/integration/DataDriven
		//http://erias.org/integration/AnchoringCandidats
		//http://erias.org/integration/AnchoringFilteringBySystem
		//http://erias.org/integration/AnchoringFilteringByMethodCorrectedArticleEncoreReduit
		// http://erias.org/integration/AnchoringFilteringClass
		
		System.out.println("setp");
		ArrayList<BindingSet> ListeRDF3= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, querygetTerminoAndLOINCconcept));
		HashMap<String,Set<String>> CodeTerminoListeCodeLoinc= new HashMap<String, Set<String>>();
		System.out.println("step 3 final");
		 //}
		int iu=0;
		 for(BindingSet e: ListeRDF3){
			 iu++;
			 //Couple a= new Couple(e.getValue("s").stringValue(), Integer.toString(i));
			 if(!CodeTerminoListeCodeLoinc.containsKey(e.getValue("s").stringValue())){
				 
				 CodeTerminoListeCodeLoinc.put(e.getValue("s").stringValue(), new HashSet<String>());
				};
				CodeTerminoListeCodeLoinc.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());
				//CodeTerminoListeCodeLoincAssocie.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());
			// System.out.println("e.getValue(\"o\").stringValue() "+e.getValue("o").stringValue());
		 }
		 Set<String>loinccodeUsed= new HashSet<String>();
		 Set<String>Tlaccardinalite1= new HashSet<String>();
		 HashMap<String, Set<String>> resultFinal=new HashMap<String, Set<String>>();
		 for(String tlab :CodeTerminoListeCodeLoinc.keySet()) {
			 if(CodeTerminoListeCodeLoinc.get(tlab).size()==1) {
				 if(!resultFinal.containsKey(tlab)) {
					 resultFinal.put(tlab, new HashSet<String>());
				 }
				 resultFinal.get(tlab).addAll(CodeTerminoListeCodeLoinc.get(tlab));
			 }
		 }
			 return resultFinal;
		
	}
	public static HashMap<String, Set<String>> getOneToOnemapping(HashMap<String, Set<String>> resullt, Set<String> loinc){
		HashMap<String, Set<String>> resul= new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> resulat= new HashMap<String, Set<String>>();
		
		for(String tlab :resullt.keySet()) {
			 if(resullt.get(tlab).size()==1) {
				 if(!resul.containsKey(tlab)){
					 
					 resul.put(tlab, new HashSet<String>() );
					 }
					 
					for(String ae:resullt.get(tlab)) {
						resul.get(tlab).add(ae);
					};
					loinc.addAll(resullt.get(tlab));
			 }
		 }
		for (String tla:resullt.keySet()) {
			for(String aze:resullt.get(tla)) {
				if(!loinc.contains(aze)) {
					if(!resulat.containsKey(tla)){
					
						resulat.put(tla, new HashSet<String>());
					}
					
						resulat.get(tla).add(aze);
				}
					
				
			}
		}
		Set<String> Tlaccardinalite1= new HashSet<String>();
		for(String at:resulat.keySet()) {
			if(resulat.get(at).size()==1) {
				Tlaccardinalite1.add(at);
			}
		}
		if(Tlaccardinalite1.size()>0) {
		HashMap<String, Set<String>> resultatvalidé=getOneToOnemapping(resulat, loinc);
		for(String ar:resultatvalidé.keySet()) {
			if(!resul.containsKey(ar)) {
				resul.put(ar, resultatvalidé.get(ar));
			}
		}
		}
		else {
			for(String at:resulat.keySet()) {
				if(!resul.containsKey(at)) {
					resul.put(at, new HashSet<String>());
				}
				resul.get(at).addAll(resulat.get(at));
			}
		}
		
		return resul;
	}
	public static HashMap<Couple,String> GetmappingsOrdonne(HashMap<String,Set<String>> CodeTerminoListeCodeLoinc) throws QueryEvaluationException, RepositoryException, MalformedQueryException{

		
		
		HashMap<Couple,String> CodeTerminoListeCodeLoincAssocie= new HashMap<Couple, String>();
		
		 //}
		int i=0;
		
		 Set<String> listNumber= new HashSet<String>();
		 for(String cod:CodeTerminoListeCodeLoinc.keySet()) {
			 String a=Integer.toString(CodeTerminoListeCodeLoinc.get(cod).size());
			 listNumber.add(a);
		 }
		 String[] array = listNumber.stream().toArray(String[]::new);
		 Integer[] intarray=new Integer[array.length];
		    int at=0;
		    for(String str:array){
		        intarray[at]=Integer.parseInt(str);//Exception in this line
		        at++;
		    }
		   // Integer[] Arrays=new Integer[intarray.length];
		    System.out.println(Arrays.toString(intarray));
		    
		 Arrays.sort(intarray);
		 System.out.println(Arrays.toString(intarray));
		 int vft=1;
		 for(int a :intarray) {
			 
			 for(String cod:CodeTerminoListeCodeLoinc.keySet()) {
				 int result = CodeTerminoListeCodeLoinc.get(cod).size();
				 if(result==a) {
					 Couple bv= new Couple(cod, Integer.toString(vft));
							 vft++;
							 if(!CodeTerminoListeCodeLoincAssocie.containsKey(bv)){
								 
								 CodeTerminoListeCodeLoincAssocie.put(bv, Integer.toString(result));
								};
				 }
			 }
		 }
		// String[] array = listNumber.toArray(String[]::new);
		 return CodeTerminoListeCodeLoincAssocie;
		
	}

}
