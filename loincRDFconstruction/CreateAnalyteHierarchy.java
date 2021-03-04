package loincRDFconstruction;

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
import utilitaries.Couple;

public class CreateAnalyteHierarchy {

	public static void main(String[] args) throws RepositoryException, QueryEvaluationException, MalformedQueryException {
		// TODO Auto-generated method stub
		//String namespace="LOINCTotal";
		//String namespace = "TLABANDLOINC";
		String namespace="FrenchLoincAnalysis";
		//String namespace="FrenchLOINC";
		
		Repository repo = RepositoryFactory.getRepository(namespace);
		RepositoryConnection conn = repo.getConnection();
		String queryAll=getAnalyteLabels();
		HashMap<String, Set<String>> codeAndLabels= new HashMap<String, Set<String>>();
		ArrayList<BindingSet> ListeWithLabels= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, queryAll));
		for(BindingSet e: ListeWithLabels){
			if(!codeAndLabels.containsKey(e.getValue("p").stringValue())) {
				codeAndLabels.put(e.getValue("p").stringValue(), new HashSet<String>());
			}
			codeAndLabels.get(e.getValue("p").stringValue()).add(e.getValue("d").stringValue());
		}
		HashMap<String, Set<Couple>> analyteHierarchy= new HashMap<String, Set<Couple>>();
		for(String code: codeAndLabels.keySet()) {
			if(codeAndLabels.get(code).size()==1) {
				for(String libelleOriginal:codeAndLabels.get(code)) {
					analyteHierarchy=InsertAHasmap(analyteHierarchy,CreateAnalyteComponentwithoutChallenge.parseHyerarchyLabels(libelleOriginal));
				
				}
			}
			else if(codeAndLabels.get(code).size()>1){
				String libe=chooseAstring(codeAndLabels.get(code));
				System.out.println("toi toi toi toi toi toi toi toi"+libe);
				analyteHierarchy=InsertAHasmap(analyteHierarchy,CreateAnalyteComponentwithoutChallenge.parseHyerarchyLabels(libe));
				
			}
		}
		
		Set<String> AnalytesLabel= new HashSet<String>();
		System.out.println("AnalytesLabel "+AnalytesLabel.size());
		for(String lab:analyteHierarchy.keySet()) {
			
			AnalytesLabel.add(lab);
			for(Couple cp:analyteHierarchy.get(lab)) {
				if(cp.x.equals("ISa")) {
				AnalytesLabel.add(cp.y);
				AnalytesLabel.add(lab);
				}
				else {
					AnalytesLabel.add(lab+"-");
					AnalytesLabel.add(cp.y);
					System.out.println("lab "+lab);
					System.out.println(lab+"-      lab          +++++++++++++++ "+cp.y);
				}
				
			}
		}
		System.out.println("AnalytesLabel "+AnalytesLabel.size());
		String prefixe="http://erias.org/loincTotal#";
		ValueFactory vf = conn.getValueFactory();
		
		
//		for(String label:analyteHierarchy.keySet() ) {
//			if(getStringEspaceCorrection(label).equals(getStringEspaceCorrection("cells.cd13+hla-dr+/100 cells"))) {
//				System.out.println(label+" label");
//				for(Couple cod:analyteHierarchy.get(label) ) {
//				System.out.println("link "+cod.y);
//				}
//			}
//			
//		}
		System.out.println("voila begin description ");
		HashMap<String, Set<String>> analytesConstruct=getDescription(prefixe, AnalytesLabel, codeAndLabels, "AN");
		System.out.println("analytesConstruct "+analytesConstruct.size());
		
		HashMap<String, Set<String>> analytesTotal=InsertAHasmapString(codeAndLabels, analytesConstruct);
		Set<Statement>stms= getRdfformat(vf, analyteHierarchy, analytesTotal);
		
//		Repository repo = RepositoryFactory.getRepository(namespace);
//		RepositoryConnection conn = repo.getConnection();
//		String queryAll=getAnalyteLabels();
//		HashMap<String, Set<String>> codeAndLabels= new HashMap<String, Set<String>>();
//		ArrayList<BindingSet> ListeWithLabels= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, queryAll));
//		for(BindingSet e: ListeWithLabels){
//			if(!codeAndLabels.containsKey(e.getValue("p").stringValue())) {
//				codeAndLabels.put(e.getValue("p").stringValue(), new HashSet<String>());
//			}
//			codeAndLabels.get(e.getValue("p").stringValue()).add(e.getValue("d").stringValue());
//		}
//		HashMap<String, Set<Couple>> analyteHierarchy= new HashMap<String, Set<Couple>>();
//		for(String code: codeAndLabels.keySet()) {
//			if(codeAndLabels.get(code).size()==1) {
//				for(String libelleOriginal:codeAndLabels.get(code)) {
//					analyteHierarchy=InsertAHasmap(analyteHierarchy,CreateAnalyteComponentwithoutChallenge.parseHyerarchyLabels(libelleOriginal));
//				
//				}
//			}
//			else if(codeAndLabels.get(code).size()>1){
//				String libe=chooseAstring(codeAndLabels.get(code));
//				System.out.println("toi toi toi toi toi toi toi toi"+libe);
//				analyteHierarchy=InsertAHasmap(analyteHierarchy,CreateAnalyteComponentwithoutChallenge.parseHyerarchyLabels(libe));
//				
//			}
//		}
//		
//		Set<String> AnalytesLabel= new HashSet<String>();
//		System.out.println("AnalytesLabel ");
//		for(String lab:analyteHierarchy.keySet()) {
//			AnalytesLabel.add(lab);
//			for(Couple cp:analyteHierarchy.get(lab)) {
//				AnalytesLabel.add(cp.y);
//			}
//		}
//		System.out.println("AnalytesLabel "+AnalytesLabel.size());
//		String prefixe="http://erias.org/loincTotal#";
//		ValueFactory vf = conn.getValueFactory();
//		
//		HashMap<String, Set<String>> analytesConstruct=getDescription(prefixe, AnalytesLabel, codeAndLabels, "AN");
//		
//		HashMap<String, Set<String>> analytesTotal=InsertAHasmapString(codeAndLabels, analytesConstruct);
//		Set<Statement>stms= getRdfformat(vf, analyteHierarchy, analytesTotal);
		
		String GraphLoincFrancais = "http://erias.org/FrenchLoinc";
		
		URI graphLoincFrancais = conn.getValueFactory().createURI(GraphLoincFrancais);
		List<Statement> liste= new ArrayList<Statement>(stms);
		

		try {	

			conn.add(liste,graphLoincFrancais);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("final terminé");
	
	}
	
	public static Set<Statement> getRdfformat(ValueFactory vf, HashMap<String, Set<Couple>> hyerarchy,HashMap<String, Set<String>> analytestotalAndLabel ){
		Set<Statement> result= new HashSet<Statement>();
		String Prefix_rdfs="http://www.w3.org/2000/01/rdf-schema#";
		for(String labeli:hyerarchy.keySet()) {
			for(Couple cop:hyerarchy.get(labeli)) {
				if(cop.x.equals("ISa")) {
					String labelin=labeli;
					
					
					String codeInitial="";
					for(String code :analytestotalAndLabel.keySet()) {

						for(String libe:analytestotalAndLabel.get(code)) {
							if(CreateAnalyteComponentwithoutChallenge.getStringEspaceCorrection(libe).equals(CreateAnalyteComponentwithoutChallenge.getStringEspaceCorrection(labelin))) {
								codeInitial=code;
								Statement stm2=vf.createStatement(vf.createURI(codeInitial), vf.createURI(Prefix_rdfs + "label"),vf.createLiteral(labelin));
								result.add(stm2);
								
							}
							
						}
						
					}
					
					if(codeInitial.equals("")) {
						System.out.println("we have some serious problemes ");
					}
					
					String codetarger="";
					String libtarg=cop.y;
					if(!CreateAnalyteComponentwithoutChallenge.getStringEspaceCorrection(libtarg).equals(CreateAnalyteComponentwithoutChallenge.getStringEspaceCorrection(labelin))) {
						for(String codel :analytestotalAndLabel.keySet()) {
							
								for(String libs :analytestotalAndLabel.get(codel) ) {
									if(CreateAnalyteComponentwithoutChallenge.getStringEspaceCorrection(libtarg).equals(CreateAnalyteComponentwithoutChallenge.getStringEspaceCorrection(libs))) {
										codetarger=codel;
										Statement stm3=vf.createStatement(vf.createURI(codetarger), vf.createURI(Prefix_rdfs + "label"),vf.createLiteral(libtarg));
										result.add(stm3);
									}
								}
								
								
							
						}
					}
					
					if(codeInitial!=null &&!codeInitial.isEmpty()) {
						if(codetarger!=null&&!codetarger.isEmpty()) {
							if(!codetarger.equals(codeInitial)) {
								
					Statement stm = vf.createStatement( vf.createURI(codeInitial),
							vf.createURI("http://www.w3.org/2004/02/skos/core#broader"),
							vf.createURI(codetarger));
					result.add(stm);
								}
							}
					}
					
					
					
				}
				else {
					String labelin=labeli+"-";
					
					
					String codeInitial="";
					for(String code :analytestotalAndLabel.keySet()) {

						for(String libe:analytestotalAndLabel.get(code)) {
							if(CreateAnalyteComponentwithoutChallenge.getStringEspaceCorrection(libe).equals(CreateAnalyteComponentwithoutChallenge.getStringEspaceCorrection(labelin))) {
								codeInitial=code;
								Statement stm2=vf.createStatement(vf.createURI(codeInitial), vf.createURI(Prefix_rdfs + "label"),vf.createLiteral(labelin));
								result.add(stm2);
								
							}
							
						}
						
					}
					
					if(codeInitial.equals("")) {
						System.out.println("we have some serious problemes ");
					}
					
					String codetarger="";
					String libtarg=cop.y;
					if(!CreateAnalyteComponentwithoutChallenge.getStringEspaceCorrection(libtarg).equals(CreateAnalyteComponentwithoutChallenge.getStringEspaceCorrection(labelin))) {
						for(String codel :analytestotalAndLabel.keySet()) {
							
							for(String libs :analytestotalAndLabel.get(codel)) {	
								if(CreateAnalyteComponentwithoutChallenge.getStringEspaceCorrection(libtarg).equals(CreateAnalyteComponentwithoutChallenge.getStringEspaceCorrection(libs))) {
								codetarger=codel;
								Statement stm3=vf.createStatement(vf.createURI(codetarger), vf.createURI(Prefix_rdfs + "label"),vf.createLiteral(libtarg));
								result.add(stm3);
								}
							}
								
								
							
						}
					}
					
					if(codeInitial!=null &&!codeInitial.isEmpty()) {
						if(codetarger!=null&&!codetarger.isEmpty()) {
							if(!codetarger.equals(codeInitial)) {
								
					Statement stm = vf.createStatement( vf.createURI(codetarger),
							vf.createURI("http://www.w3.org/2004/02/skos/core#broader"),
							vf.createURI(codeInitial));
					result.add(stm);
								}
							}
					}
					
				}
			}
			
		}
		
		
		
		return result;
		
		
	
	
	}
	public static  HashMap<String, Set<String>> getDescription(String prefixe, Set<String> listecaracteristiques, HashMap<String,Set<String>> analytes, String description){
		int i=1500000;
		
		HashMap<String, Set<String>> descriptionresult= new HashMap<String, Set<String>>();
		
		for(String lib: listecaracteristiques){
			//System.out.println(prefix+i);
			i++;
			String tdf="";
			if(lib.length()>0){
				for(String code:analytes.keySet()) {
					if(analytes.get(code).contains(lib)) {
						tdf=code;
					}
				}
				if(tdf.equals("")) {
					if(!descriptionresult.containsKey(prefixe+description+i)){
					descriptionresult.put(prefixe+description+i, new HashSet<String>());
					}
				descriptionresult.get(prefixe+description+i).add(lib);
				}
				
			
			}
			
			
			
		
	}
	
		
		return descriptionresult;
		
	}
	public static String chooseAstring(Set<String>libell) {
		HashMap<String, String> result = new HashMap<String, String>();
		for(String lib : libell) {
			int i=0;
			for(String token:lib.split("(\\+|&|\\.)")) {
				System.out.println("token "+token);
				i++;
				
			}
			String key=Integer.toString(i);
			if(!result.containsKey(key)) {
				result.put(key, lib);
			}
		}
		
		System.out.println("result "+result);
		int v=0;
		String Final="";
		for(String ky:result.keySet()) {
			int aps=Integer.valueOf(ky);
			if(aps>v) {
				Final=result.get(ky);
				v=aps;
			}
			
		}
		return Final;
		
	}
	public static HashMap<String, Set<Couple>> InsertAHasmap(HashMap<String, Set<Couple>> first,HashMap<String, Set<Couple>>toadd){
		for(String code: toadd.keySet()) {
			if(!first.containsKey(code)) {
				first.put(code, new HashSet<Couple>());
			}
			first.get(code).addAll(toadd.get(code));
		}
		return first;
	}
	public static HashMap<String, Set<String>> InsertAHasmapString(HashMap<String, Set<String>> first,HashMap<String, Set<String>>toadd){
		for(String code: toadd.keySet()) {
			if(!first.containsKey(code)) {
				first.put(code, new HashSet<String>());
			}
			first.get(code).addAll(toadd.get(code));
		}
		return first;
	}
	public static String getAnalyteLabels() {
		String query="prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" + 
				"				 Select distinct ?p ?d WHERE { \n" + 
				"				\n" + 
				"                 GRAPH <http://erias.org/FrenchLoinc> { \n" + 
				"				?s <http://erias.org/loincTotal#has_component> ?o.\n" + 
				"                   ?o <http://erias.org/loincTotal#has_analyte> ?p.\n" + 
				"                   ?p rdfs:label ?d\n" + 
				"                   \n" + 
				"				 }\n" + 
				"                       \n" + 
				"				 } ";
		return query;
	}

}
