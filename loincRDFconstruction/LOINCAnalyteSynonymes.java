package loincRDFconstruction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import interactionRelationalDatabase.ConnexionBD;
import interactionTripleStore.InteractionBlazgraph;
import interactionTripleStore.RepositoryFactory;

public class LOINCAnalyteSynonymes {

	public static void main(String[] args) throws SQLException, RepositoryException, QueryEvaluationException, MalformedQueryException {
		// TODO Auto-generated method stub
ConnexionBD loinc2019DataBase= new ConnexionBD(1);
		
		String query= "SELECT * FROM frenchLoinc";
		
		String prefixe="http://erias.org/loincTotal#";
			
		PreparedStatement LOINCFrenchLoinc = loinc2019DataBase.getconn().prepareStatement(query);
		ResultSet FrenchLoincresultat = LOINCFrenchLoinc.executeQuery();
		System.out.println("query obtain");
		String namespace = "TLABANDLOINC";
		Repository repo = RepositoryFactory.getRepository(namespace);
		RepositoryConnection connnex = repo.getConnection();
		
		ValueFactory vf = connnex.getValueFactory();
		Set<String> CodeLoinc = new HashSet<String>();
		HashMap<String, Set<String>> loincAndLabel= new HashMap<String, Set<String>>();
		while(FrenchLoincresultat.next()){
			
			String code=FrenchLoincresultat.getString(1).toLowerCase();
			String RelatedName=FrenchLoincresultat.getString(11).toLowerCase();
//			if(CodeLOINCFrench.contains(code)) {
//				Statement stm = vf.createStatement( vf.createURI(prefixe+code),
//						vf.createURI("http://www.w3.org/2004/02/skos/core#prefLabel"),
//						vf.createLiteral(ShortName));
//				lesSatement.add(stm);
//			}
			
			if(!RelatedName.isEmpty()) {
				if(!RelatedName.equals("")) {
					if(!RelatedName.equals("Null")) {
						if(!RelatedName.equals("`")) {
							CodeLoinc.add(prefixe+code);
							if(!loincAndLabel.containsKey(code)) {
								loincAndLabel.put(code, new HashSet<String>());
							}
							loincAndLabel.get(code).add(RelatedName);
						//	System.out.println("code "+code);
							//System.out.println("RelatedName "+RelatedName);
						}
					}
					
				}
			
		}

			
		}
		
		System.out.println("related.add(RelatedName); "+CodeLoinc.size());
		HashMap<String, Set<String>> loicAnalyte=loincAndAnalyteCode(connnex, CodeLoinc);
	//	System.out.println(loicAnalyte);
		String Prefix_rdfs="http://www.w3.org/2000/01/rdf-schema#";
		Set<Statement> lesSatement= new HashSet<Statement>();
		for(String code :loincAndLabel.keySet()){
			//System.out.println(code);
			Set<String>RelatedNames= loincAndLabel.get(code);
			//System.out.println(RelatedNames);
			//System.out.println(loicAnalyte);
			for(String RelatedName:RelatedNames) {
			
					if(loicAnalyte.containsKey(prefixe+code)) {
						System.out.println("alpha "+prefixe+code);
						
							
						for(String An : loicAnalyte.get(prefixe+code)) {
								System.out.println("code "+An);
								System.out.println("RelatedName "+RelatedName);
								Statement stm = vf.createStatement( vf.createURI(An),
										vf.createURI(Prefix_rdfs + "label"),
										vf.createLiteral(RelatedName));
								lesSatement.add(stm);
						}
					}
						
					
					
				
			
			
		}

			
		}
		System.out.println("ok fini");
		String GraphLoincFrancais = "http://erias.org/FrenchLoinc";
		
		URI graphLoincFrancais = connnex.getValueFactory().createURI(GraphLoincFrancais);
		List<Statement> liste= new ArrayList<Statement>(lesSatement);
		

		try {	

			connnex.add(liste,graphLoincFrancais);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static HashMap<String,Set<String>>  loincAndAnalyteCode(RepositoryConnection conn, Set<String> code) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		String query="prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" + 
				"				select distinct ?s ?o ?j where \n" + 
				"		 		{  \n" + 
				"                  \n" + 
				"                    \n" + 
				"		 			\n" + 
				"                     graph<http://erias.org/FrenchLoinc>{\n" + 
				" ?s <http://erias.org/loincTotal#has_component> ?h.\n" + 
				"                              ?h <http://erias.org/loincTotal#has_analyte> ?o."+ 
				"                       \n" + 
				"                	\n" + 
				"                 	}\n" + 
				"                         \n" + 
				"                          \n" + 
				"                \n" + 
				"               }";
		ValueFactory vf = conn.getValueFactory();
		
		HashMap<String,Set<String>> resultat= new HashMap<String, Set<String>>();
		 ArrayList<BindingSet> ListeRDFTotal= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query));
		 
		 for(BindingSet e: ListeRDFTotal){
			 String codeLoinc=e.getValue("s").stringValue();
			 System.out.println(codeLoinc);
			 String codeAnalyte=e.getValue("o").stringValue();
			 if(code.contains(codeLoinc)) {
				 System.out.println("aboslut"+codeLoinc);
				if(!resultat.containsKey(codeLoinc)) {
					resultat.put(codeLoinc, new HashSet<String>());
				}
				resultat.get(codeLoinc).add(codeAnalyte);
			 }
			}
		 System.out.println("ok"); 
		return resultat;
		  
		
	}

}
