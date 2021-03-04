package anchoringStage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

public class getMainanchoringbeforedatadriven {

	public static void main(String[] args) throws RepositoryException, QueryEvaluationException, MalformedQueryException, FileNotFoundException {
		// TODO Auto-generated method stub
		
		
		
		
		
//		InputStream flux=new FileInputStream("./TLABLOINCMAPPINGS2.csv"); 
//		InputStreamReader lecture=new InputStreamReader(flux,StandardCharsets.UTF_8);
//		BufferedReader buff=new BufferedReader(lecture);
//		Set<String> tlabresul= new HashSet<String>();
//		Set<String> loincresul= new HashSet<String>();
//		String ligne;
//		try {
//			while ((ligne=buff.readLine())!=null){
//				String[]rest=ligne.split(";");
//				//System.out.println(rest);
//				if(rest.length==4) {
//					
//					String Tlablcode=rest[0].trim();
//					tlabresul.add(Tlablcode);
//					String Loinccode=rest[3];
//					loincresul.add(Loinccode);
//					
//					}
//			
//			}
//			buff.close(); 
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		
				
		
		
		
		
		
		
		
		
		String namespace = "TLABANDLOINC";
		Repository repo = RepositoryFactory.getRepository(namespace);
		RepositoryConnection conn = repo.getConnection();
		ValueFactory vf = conn.getValueFactory();
		
		
		
		
		String queryGetConcept="prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"				select distinct ?s ?o\n" + 
				"where \n" + 
				"		 		{ \n" + 
				"                  GRAPH<http://erias.org/TLABWITHLOINCPropertyTOTAL> { \n" + 
				"						?s ?vv ?j. \n" + 
				"								}\n" + 
			
				"		 		graph<http://erias.org/integration/AnchoringFilteringByMethodTOTALCORRIGE>{\n" + 
				"		 		 ?s ?p ?o. \n" + 
				"		 		} \n" + 
				"		 	?s skos:prefLabel ?l. \n" + 
				"		 		\n" + 
				"		 		graph<http://erias.org/LOINCLabel>{ \n" + 
				"		 		?o skos:prefLabel ?i \n" + 
				"		 		 } \n" + 
				"		 		 }";
		
		String queryquery="prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"				select distinct ?s ?o \n" + 
				"where \n" + 
				"		 		{ \n" + 
				"                  GRAPH<http://erias.org/TLABWITHLOINCPropertyTOTAL> { \n" + 
				"						?s ?vv ?j. \n" + 
				"								}\n" + 
			
				"		 		graph<http://erias.org/integration/AnchoringFilteringBySystemTOTALCORRIGE>{\n" + 
				"		 		 ?s ?p ?o. \n" + 
				"		 		} \n" + 
				"		 	?s skos:prefLabel ?l. \n" + 
				"		 		\n" + 
				"		 		graph<http://erias.org/LOINCLabel>{ \n" + 
				"		 		?o skos:prefLabel ?i \n" + 
				"		 		 } \n" + 
				"		 		 }";
		
		
		
		
	
		
		
		
		
		
		ArrayList<BindingSet> ListeRDFinitialsystem= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, queryquery));
		
		
		
		
		Map<String, Set<String>> initile = new HashMap<String, Set<String>>();
		 for(BindingSet e: ListeRDFinitialsystem){
			 if(!initile.containsKey(e.getValue("s").stringValue())) {
				 initile.put(e.getValue("s").stringValue(), new HashSet<String>());
			
			 }
			 initile.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());	 
		 }
		 ArrayList<BindingSet> ListeRDFinitial= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, queryGetConcept));
			
		 Map<String, Set<String>> initil = new HashMap<String, Set<String>>();
		 for(BindingSet e: ListeRDFinitial){
				 if(!initil.containsKey(e.getValue("s").stringValue())) {
					 initil.put(e.getValue("s").stringValue(), new HashSet<String>());
				
				 }
				 initil.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());	 
			 }
		
		 Map<String, String> initialunique = new HashMap<String, String>();
		System.out.println(initil.size());
		System.out.println(initile.size());
	
		
		
		
		 initil.forEach((a,b)->{
			 if(b.size()==1) {
				 for(String bc :b) {
					 if(!initialunique.containsKey(a)) {
					 
						 initialunique.put(a, bc);
					 }
				
				 }
				
			 }
		 });
		
		 initile.forEach((a,b)->{
			 if(b.size()==1) {
				 for(String bc :b) {
					 if(!initialunique.containsKey(a)) {
					 
						 initialunique.put(a, bc);
					 }
				
				 }
				
			 }
		 });
		
		
		
		
		
		
		String queryGetConceptterminologyandPropery="prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"				select distinct ?s ?o \n" + 
				"where \n" + 
				"		 		{ \n" + 
				"                  GRAPH<http://erias.org/TLABWITHLOINCPropertyTOTAL> { \n" + 
				"						?s ?vv ?j. \n" + 
				"								}\n" + 
			
				"		 		graph<http://erias.org/integration/DataDrivenTOTALCORRIGEARTICLE>{\n" + 
				"		 		 ?s ?p ?o. \n" + 
				"		 		} \n" + 
				"		 	?s skos:prefLabel ?l. \n" + 
				"		 		\n" + 
				"		 		graph<http://erias.org/LOINCLabel>{ \n" + 
				"		 		?o skos:prefLabel ?i \n" + 
				"		 		 } \n" + 
				"		 		 }";
		
		
		 ArrayList<BindingSet> ListeRDF= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, queryGetConceptterminologyandPropery));
			
		 //}
		 Set<String> codeTLAB = new HashSet<String>();
		 Set<String> codeTLABtotal = new HashSet<String>();
		 Set<String> codeLOINC = new HashSet<String>();
		 Set<String> codeLOINCtotal = new HashSet<String>();
		 Set<String> TlabUn = new HashSet<String>();
		 Map<String, Set<String>> initilee = new HashMap<String, Set<String>>();
		 for(BindingSet e: ListeRDF){
			// if(tlabresul.contains(e.getValue("s").stringValue())) {
			 codeTLAB.add(e.getValue("s").stringValue());
			 codeLOINC.add(e.getValue("o").stringValue());
			 
			 if(!initilee.containsKey(e.getValue("s").stringValue())) {
				 initilee.put(e.getValue("s").stringValue(), new HashSet<String>());
			
			 }
			 initilee.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());	
			// }
					 
		 }
		 int sup =1;
		for (String a: initilee.keySet()){
			Set<String> b = initilee.get(a);
			 if(b.size()==1) {
				 TlabUn.add(a);
				
			 }
			 else {
				 if(sup<b.size()) {
					 sup=b.size();
				 }
			 }
		 }
		 System.out.println("b "+sup);
		 for(String a : initialunique.keySet()) {
			 if(!codeTLAB.contains(a)) {
				 codeTLABtotal.add(a);
				 String b = initialunique.get(a);
				 codeLOINCtotal.add(b);
			 }
			 
		 }
		 codeLOINCtotal.addAll(codeLOINC);
		 codeTLABtotal.addAll(codeTLAB);
		 Set<String> codeLOINCinital = new HashSet<String>();
		 for(String code : codeTLABtotal) {
			 if(initil.containsKey(code)) {
				 codeLOINCinital.addAll(initil.get(code));
			 }
		 }
		// System.out.println(" tlabresul "+tlabresul.size());
		  System.out.println(" codeLOINCinital "+codeLOINCinital.size());
		 System.out.println(" method codeTLAB "+codeTLABtotal.size());
		 System.out.println(" method codeLOINC "+codeLOINCtotal.size());
		 System.out.println(" TlabUn "+TlabUn.size());
		 
	
	}

}
