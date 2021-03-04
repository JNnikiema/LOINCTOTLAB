package anchoringStage;

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
import utilitaries.Couple;

public class FirstStepsAnchoringAttributes {

	public static void main(String[] args) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		// TODO Auto-generated method stub
		HashMap<String, Set<Couple>> CodesTerminoLienCodeLoinc= new HashMap<String, Set<Couple>>();
		String namespace = "TLABANDLOINC";
		RepositoryConnection conn= InteractionBlazgraph.connex(namespace);
		
		String reductiondesMots="b";
		//String reductiondesMots="a";
//		String query="prefix lnc:<http://erias.org/loincTotal#> \n" + 
//				"					 prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
//				"					 prefix MOD:<http://erias.org/integration/TLAB/model#> \n" + 
//				"					 prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> \n" + 
//				"					\n" + 
//				"\n" + 
//				"					  select ?p ?o \n" + 
//				"					 where{ \n" + 
//				"					<http://chu-bordeaux.fr/synergie#syn-ana-vsha> a MOD:BiologicalAnalysis.\n" + 
//				"					 ?M1 a MOD:Mot1. \n" + 
//				"					<http://chu-bordeaux.fr/synergie#syn-ana-vsha> <http://erias.org/TLAB/OWL/Relation#:has_word> ?M1. \n" + 
//				"					 ?M1 MOD:isMappedTo ?p. \n" + 
//				"					?p a <http://erias.org/LOINC#Mot2>. \n" + 
//				"					 ?o lnc:has_word ?p. \n" + 
//				"					} ";
		String query="prefix lnc:<http://erias.org/loincTotal#> \n" + 
				"					 prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"					 prefix MOD:<http://erias.org/integration/TLAB/model#> \n" + 
				"					 prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> \n" + 
				"					\n" + 
			"\n" + 
				"					  select ?s ?p ?o \n" + 
				"					 where{ \n" + 
				"					 ?s a MOD:BiologicalAnalysis.\n" + 
				"					 ?M1 a MOD:Mot1. \n" + 
				"					 ?s <http://erias.org/TLAB/OWL/Relation#:has_word> ?M1. \n" + 
				"					 ?M1 MOD:isMappedToTOTALCORRIGE ?p. \n" + 
				"					?p a <http://erias.org/LOINC#Mot2>. \n" + 
				"					 ?o lnc:has_word ?p. \n" + 
				"					} ";
		
		
		String query2="prefix lnc:<http://erias.org/loincTotal#> \n" + 
				"					 prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"					 prefix MOD:<http://erias.org/integration/TLAB/model#> \n" + 
				"					 prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> \n" + 
				"					\n" + 
				"\n" + 
				"					  select distinct ?p ?o  \n" + 
				"					  where{   \n" + 
				"					 ?s a MOD:BiologicalAnalysis.   \n" + 
				"					  ?M1 a MOD:Mot1.   \n" + 
				"					  ?s <http://erias.org/TLAB/OWL/Relation#:has_word> ?M1.   \n" + 
				"					  ?M1 MOD:isMappedToTOTALCORRIGE ?M2.   \n" + 
				"					 ?M2 a <http://erias.org/LOINC#Mot2>.\n" + 
				"					  ?o lnc:has_word ?M2.  \n" + 
				"                        graph<http://erias.org/FrenchLoinc>{\n" + 
				"					   { \n" + 
				"					     ?g ?p ?o  \n" + 
				"					     }  \n" + 
				"					   union{  \n" + 
				"					    \n" + 
				"					    ?g ?j ?h.  \n" + 
				"					       ?h ?p ?o  \n" + 
				"					    }  \n" + 
				"                          }\n" + 
				"					 }  ";
		
		Set<Couple> RelationAvecAnalyseBiologique= new HashSet<Couple>();
		System.out.println(query2);
		HashMap<String, Set<String>> RelationCodeLoinc= new HashMap<String, Set<String>>();
		
		HashMap<String, Set<String>> CodeLoincRelation= new HashMap<String, Set<String>>();
		
		Set<Statement> lesSatement= new HashSet<Statement>();
		ValueFactory vf = conn.getValueFactory();
		ArrayList<BindingSet> ListeRDF2= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query2));
		 for(BindingSet e: ListeRDF2){
			 
		Couple relation= new Couple(e.getValue("p").stringValue(), e.getValue("o").stringValue());
		System.out.println("p "+e.getValue("p").stringValue()+" o "+ e.getValue("o").stringValue());
		RelationAvecAnalyseBiologique.add(relation);
		 }
		 System.out.println("finish");
		/**
		 * mise en evidence de la relation unissant la caractéristique et le code loinc principal
		 */
		for(Couple coup:RelationAvecAnalyseBiologique){
			if(!RelationCodeLoinc.containsKey(coup.x)){
				RelationCodeLoinc.put(coup.x, new HashSet<String>());
			}
			RelationCodeLoinc.get(coup.x).add(coup.y);
			if(!CodeLoincRelation.containsKey(coup.y)){
				CodeLoincRelation.put(coup.y, new HashSet<String>());
			}
			CodeLoincRelation.get(coup.y).add(coup.x);
		}
		HashMap<String, Set<Couple>> tripletRDfModel= new HashMap<String, Set<Couple>>();
		
		
		 ArrayList<BindingSet> ListeRDFModel= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query));

	
		 for(BindingSet e: ListeRDFModel){	
			
		Couple relation= new Couple(e.getValue("p").stringValue(), e.getValue("o").stringValue());
		if(!tripletRDfModel.containsKey(e.getValue("s").stringValue())){
			tripletRDfModel.put(e.getValue("s").stringValue(), new HashSet<Couple>());
		}
		tripletRDfModel.get(e.getValue("s").stringValue()).add(relation);
		System.out.println("s "+e.getValue("s").stringValue()+" p "+e.getValue("p").stringValue()+" o "+e.getValue("o").stringValue());
		 }
		 
		 // testing the process for one TLAB concept
//		 for(BindingSet e: ListeRDFModel){	
//			
//			Couple relation= new Couple(e.getValue("p").stringValue(), e.getValue("o").stringValue());
//			if(!tripletRDfModel.containsKey("http://chu-bordeaux.fr/synergie#syn-ana-vsha")){
//				tripletRDfModel.put("http://chu-bordeaux.fr/synergie#syn-ana-vsha", new HashSet<Couple>());
//			}
//			tripletRDfModel.get("http://chu-bordeaux.fr/synergie#syn-ana-vsha").add(relation);
//			System.out.println("s "+"http://chu-bordeaux.fr/synergie#syn-ana-vsha"+" p "+e.getValue("p").stringValue()+" o "+e.getValue("o").stringValue());
//			 }
		 
		/**
		 * recuperation du triplet unissant chaque code de la terminologie locale à un mots
		 */
		
		for(String sr:tripletRDfModel.keySet()){
			
			if(!CodesTerminoLienCodeLoinc.containsKey(sr)){
				CodesTerminoLienCodeLoinc.put(sr,new HashSet<Couple>());
			}
			HashMap<String, Set<String>> CodeLoincDeRattacheemntEtMots= new HashMap<String, Set<String>>();
			
			/*
			 * pour chaque code Loinc rattaché regroupons le nombre de mots auquel il est rattaché
			 */
			for(Couple huiu:tripletRDfModel.get(sr)){
				if(!CodeLoincDeRattacheemntEtMots.containsKey(huiu.y)){
					CodeLoincDeRattacheemntEtMots.put(huiu.y,new HashSet<String>());
				}
				CodeLoincDeRattacheemntEtMots.get(huiu.y).add(huiu.x);
				
			}
			HashMap<String, Set<String>> LarelationEtLoincCode= new HashMap<String, Set<String>>();
			/*
			 * gardon en memoire la relation que ces codes loinc ont avec le code loinc principal
			 */
			for(String codeLoinc:CodeLoincDeRattacheemntEtMots.keySet()){
				System.out.println("codeLoinc "+codeLoinc);
				for(String realtion:CodeLoincRelation.get(codeLoinc)){
					if(!LarelationEtLoincCode.containsKey(realtion)){
						LarelationEtLoincCode.put(realtion, new HashSet<String>());
					}
					LarelationEtLoincCode.get(realtion).add(codeLoinc);
					
				}
			}
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ "+sr);
			System.out.println(LarelationEtLoincCode);
//			for(String lo:CodeLoincDeRattacheemntEtMots.keySet()) {
//				System.out.println("CodeLoincDeRattacheemntEtMots "+lo+" nombre de mots "+CodeLoincDeRattacheemntEtMots.get(lo).size());
//				
//			}
			for(String relation :LarelationEtLoincCode.keySet()){
				if( reductiondesMots.equals("a")){
				System.out.println("++ "+relation);
				Set<String> jend= new HashSet<String>();
				
				HashMap<String, Set<String>> NombreDeMotListeLoinc= new HashMap<String, Set<String>>();
				
				int i=0;
				int nombreMot=0;
				for(String loincRelier: LarelationEtLoincCode.get(relation)){
					//System.out.println("+++ "+loincRelier);
					jend.add(loincRelier);
					if(!NombreDeMotListeLoinc.containsKey(Integer.toString(CodeLoincDeRattacheemntEtMots.get(loincRelier).size()))){
						NombreDeMotListeLoinc.put(Integer.toString(CodeLoincDeRattacheemntEtMots.get(loincRelier).size()), new HashSet<String>());
					}
					NombreDeMotListeLoinc.get(Integer.toString(CodeLoincDeRattacheemntEtMots.get(loincRelier).size())).add(loincRelier);
					
					if(i==0){
						nombreMot=CodeLoincDeRattacheemntEtMots.get(loincRelier).size();
						i++;
						//System.out.println(sr+" primo loincRelier "+ loincRelier);
					}
					else{
						if(nombreMot<CodeLoincDeRattacheemntEtMots.get(loincRelier).size()){
							nombreMot=CodeLoincDeRattacheemntEtMots.get(loincRelier).size();
							i++;
							//System.out.println(sr+" la suite loincRelier "+ loincRelier);
							//System.out.println(NombreDeMotListeLoinc);
							
						}
						
					}
					
				}
				System.out.println(NombreDeMotListeLoinc);
				//System.out.println(nombreMot);
				Set<Couple> couplerelationCodeLoinc= new HashSet<Couple>();
				
				for(String loincCodeds:NombreDeMotListeLoinc.get(Integer.toString(nombreMot))){
					Couple ab= new Couple(relation, loincCodeds);
					Statement stm2 = vf.createStatement( vf.createURI(sr),
							vf.createURI(relation),
							vf.createURI(loincCodeds));
					lesSatement.add(stm2);
					couplerelationCodeLoinc.add(ab);
				}


				CodesTerminoLienCodeLoinc.get(sr).addAll(couplerelationCodeLoinc);
				if(nombreMot>1&&NombreDeMotListeLoinc.get(Integer.toString(nombreMot)).size()<=5){
				//System.out.println(" fin codeterminologie "+sr+" realtion "+relation+" code Loinc rattaché "+NombreDeMotListeLoinc.get(Integer.toString(nombreMot)) );
				//System.out.println(" debut  codeterminologie "+sr+" realtion "+relation+" code Loinc rattaché "+jend);
			}
				
				
			}
			else{
				for(String loincRelier: LarelationEtLoincCode.get(relation)){
					Statement stm2 = vf.createStatement( vf.createURI(sr),
							vf.createURI(relation),
							vf.createURI(loincRelier));
					lesSatement.add(stm2);
				}
			}
			}
			
			
			//System.out.println(" codeterminologie "+sr+" code Loinc rattaché "+CodeLoincDeRattacheemntEtMots.keySet() );
			
		}
		
		String graphAncrage=	"http://erias.org/integration/AnchoringFromWordReducedTOTALCORRIGEARTICLE";
		//String graphAncrage=	"http://erias.org/integration/AnchoringFromWordReducedTOTALCORRIGE";
		//String graphAncrage=	"http://erias.org/integration/AnchoringFromWordNormalTestTOTAL";
		
		//String graphAncrage=	"http://erias.org/integration/AnchoringFromWord";
		
		
		URI AncrageURI = conn.getValueFactory().createURI(graphAncrage);
		List<Statement> liste= new ArrayList<Statement>(lesSatement);
		System.out.println("mappings"+liste.size());
		
		System.out.println("debut d'insertion");
		try {
			
			conn.add(liste,AncrageURI);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("fin d'insertion");
				
		

	}

}
