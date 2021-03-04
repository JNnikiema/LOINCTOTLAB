package dataDrivenFiltering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.InitialContext;

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

public class TLABconceptsAndUCUMunits {

	public static void main(String[] args) throws RepositoryException, QueryEvaluationException, MalformedQueryException {
		// TODO Auto-generated method stub
		String namespace = "TLABANDLOINC";
		Repository repo = RepositoryFactory.getRepository(namespace);
		RepositoryConnection conn = repo.getConnection();
		ValueFactory vf = conn.getValueFactory();
		Set<DataStructure> intial =InstanciationOfUsingLoincConcepts.getResultInstanciate();
		HashMap<String,Set<String>> CodeAndAnnotation=GetTLABUsed(conn);
		HashMap<String,Set<String>> unitOfMeasure =GetUnitOfmeasure(conn);
		HashMap<String, Set<String>> getUCUMunit=GetUnitOfmeasureUCUM(conn);
		for(String cod:getUCUMunit.keySet()) {
			if(!unitOfMeasure.containsKey(cod)) {
				unitOfMeasure.put(cod, new HashSet<String>());
			}
			unitOfMeasure.get(cod).addAll(getUCUMunit.get(cod));
		}
		
		Set<Statement> result=new HashSet<Statement>();
		//Set<Statement> result2=new HashSet<Statement>();
		//String TLABproperty = "http://erias.org/TLABWITHUCUM/Property";
		String TLABproperty = "http://erias.org/TLABWITHUCUM/PropertyTOTALESCorrigesArticle";
		String prefixe="http://erias.org/TLAB/Property#";
		
		String prefix="http://erias.org/UCUM#";
		String skos="http://www.w3.org/2004/02/skos/core#";
		System.out.println("initial : "+intial.size());
		for(DataStructure anotUnit:intial) {
			String annotation=anotUnit.Annotation.replace("SYN|ANA:", "SYN:ANA-");
			String unitofCode=anotUnit.Units.trim();
			//System.out.println("unitofCode "+unitofCode);
			String codeUn="";
			for(String code :unitOfMeasure.keySet()) {
				
				//System.out.println(code);
				Set<String> abel=unitOfMeasure.get(code);
				//System.out.println("abel "+abel);
				if(abel.contains(unitofCode)) {
					codeUn=code;
				}
			}
	//System.out.println(" unitofCode "+unitofCode+" codeUn  "+codeUn);
			if(codeUn.equals("")) {
				System.out.println("unitofCode "+unitofCode);
				System.out.println("6+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//				System.out.println(unitofCode);
//				Statement stm7=vf.createStatement(vf.createURI(prefix+unitofCode.hashCode()), 
//						vf.createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
//						vf.createURI(skos+"Concept"));
//				result2.add(stm7);
//				Statement stm8=vf.createStatement(vf.createURI(prefix+unitofCode.hashCode()),
//						vf.createURI(skos + "altLabel"),
//						vf.createLiteral(unitofCode));
//				result2.add(stm8);
//				if(unitofCode.equals("NTU")) {
//					String king="Arbitrary Units";
//					String kind= king.replace(" ", "");
//					Statement stm4=vf.createStatement(vf.createURI(prefixe+unitofCode.hashCode()), 
//							vf.createURI(prefixe+"kind_of_Quantity"),
//							vf.createURI(prefixe+kind));
//					result2.add(stm4);
//				}
//				else if(unitofCode.equals("cycles")) {
//					String king="frequence";
//					String kind= king.replace(" ", "");
//					Statement stm4=vf.createStatement(vf.createURI(prefixe+unitofCode.hashCode()), 
//							vf.createURI(prefixe+"kind_of_Quantity"),
//							vf.createURI(prefixe+kind));
//					result2.add(stm4);
//					
//				}
//				else if(unitofCode.equals("UB/ml")) {
//					String king="Arbitrary Concentration Units";
//					String kind= king.replace(" ", "");
//					Statement stm4=vf.createStatement(vf.createURI(prefixe+unitofCode.hashCode()), 
//							vf.createURI(prefixe+"kind_of_Quantity"),
//							vf.createURI(prefixe+kind));
//					result2.add(stm4);
//				}
//				else if(unitofCode.equals("ng/gcu")) {
//					String king="Mass Ratio Or Mass Fraction Or Mass Content Units";
//					String kind= king.replace(" ", "");
//					Statement stm4=vf.createStatement(vf.createURI(prefixe+unitofCode.hashCode()), 
//							vf.createURI(prefixe+"kind_of_Quantity"),
//							vf.createURI(prefixe+kind));
//					result2.add(stm4);
//				}
//				else if(unitofCode.equals("µ3/mm3")) {
//					String king="Volume Fraction Units";
//					String kind= king.replace(" ", "");
//					Statement stm4=vf.createStatement(vf.createURI(prefixe+unitofCode.hashCode()), 
//							vf.createURI(prefixe+"kind_of_Quantity"),
//							vf.createURI(prefixe+kind));
//					result2.add(stm4);
//				}
//				else {
//					System.out.println("unitofCode : "+unitofCode);
//				}
	}
			//System.out.println("anno "+annotation);
			for(String code:CodeAndAnnotation.keySet() ) {
				
				Set<String> anno= CodeAndAnnotation.get(code);
				//System.out.println("code "+anno);
				if(anno.contains(annotation)) {
					if(!codeUn.equals("")) {
					Statement stm=vf.createStatement(vf.createURI(code), 
							vf.createURI(prefixe + "Has_Unit"),
							vf.createURI(codeUn));
					result.add(stm);
					}
				}
			}
			
		}
		
		
//		String Tlabunit = "http://erias.org/TLABunitsInstanciated";
//		
//		URI TLABunitStructure = conn.getValueFactory().createURI(Tlabunit);
//		List<Statement> liste2= new ArrayList<Statement>(result2);
//		
//		System.out.println(liste2.size());
//
//		try {	
//
//			conn.add(liste2,TLABunitStructure);
//		} catch (RepositoryException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		System.out.println("final terminé");
		
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
	
	public static HashMap<String,Set<String>> GetTLABUsed(RepositoryConnection conn) throws QueryEvaluationException, RepositoryException, MalformedQueryException{

		String query= "prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"				select distinct ?s ?p ?o where \n" + 
				"		 		{ \n" + 
				"		 		graph<http://erias.org/InterfaceTerminology/TLAB/UsedCode>{\n" + 
				"		 		 ?s ?p ?o.\n" + 
				"                 \n" + 
				"		 		}\n" + 
				"		 		 }";
		
		ArrayList<BindingSet> ListeRDF3= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query));
		HashMap<String,Set<String>> CodeTerminoListeCodeLoincAssocie= new HashMap<String, Set<String>>();
		 for(BindingSet e: ListeRDF3){
			 if(!CodeTerminoListeCodeLoincAssocie.containsKey(e.getValue("s").stringValue())){
					
				 CodeTerminoListeCodeLoincAssocie.put(e.getValue("s").stringValue(), new HashSet<String>());
				};
				CodeTerminoListeCodeLoincAssocie.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());
			// System.out.println("e.getValue(\"o\").stringValue() "+e.getValue("o").stringValue());
		 }
		 
		 return CodeTerminoListeCodeLoincAssocie;
	}
	public static HashMap<String,Set<String>> GetUnitOfmeasure(RepositoryConnection conn) throws QueryEvaluationException, RepositoryException, MalformedQueryException{

		String query= "prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"				select distinct ?s ?o where \n" + 
				"		 		{ \n" + 
				"		 		graph<http://erias.org/InstanciatedUNitsTOTALESCorriges>{\n" + 
				"		 		 ?s skos:altLabel ?o.\n" + 
				"                 \n" + 
				"		 		}\n" + 
				"		 		 }";
		//http://erias.org/InstanciatedUNitsTOTAL
		//http://erias.org/InstanciatedUNits
		
		ArrayList<BindingSet> ListeRDF3= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query));
		HashMap<String,Set<String>> CodeTerminoListeCodeLoincAssocie= new HashMap<String,Set<String>>();
		 for(BindingSet e: ListeRDF3){
			 if(!CodeTerminoListeCodeLoincAssocie.containsKey(e.getValue("s").stringValue())){
					
				 CodeTerminoListeCodeLoincAssocie.put(e.getValue("s").stringValue(), new HashSet<String>());
				};
				CodeTerminoListeCodeLoincAssocie.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());
			// System.out.println("e.getValue(\"o\").stringValue() "+e.getValue("o").stringValue());
		 }
		 
		 return CodeTerminoListeCodeLoincAssocie;
	}
	
	public static HashMap<String,Set<String>> GetUnitOfmeasureUCUM(RepositoryConnection conn) throws QueryEvaluationException, RepositoryException, MalformedQueryException{

		String query= "prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"				select distinct ?s ?o where \n" + 
				"		 		{ \n" + 
				"		 		graph<http://erias.org/ucum>{\n" + 
				"		 		 ?s skos:altLabel ?o.\n" + 
				"                 \n" + 
				"		 		}\n" + 
				"		 		 }";
		//http://erias.org/InstanciatedUNitsTOTAL
		//http://erias.org/InstanciatedUNits
		
		ArrayList<BindingSet> ListeRDF3= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query));
		HashMap<String,Set<String>> CodeTerminoListeCodeLoincAssocie= new HashMap<String,Set<String>>();
		 for(BindingSet e: ListeRDF3){
			 if(!CodeTerminoListeCodeLoincAssocie.containsKey(e.getValue("s").stringValue())){
					
				 CodeTerminoListeCodeLoincAssocie.put(e.getValue("s").stringValue(), new HashSet<String>());
				};
				CodeTerminoListeCodeLoincAssocie.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());
			// System.out.println("e.getValue(\"o\").stringValue() "+e.getValue("o").stringValue());
		 }
		 
		 return CodeTerminoListeCodeLoincAssocie;
	}
	

}
