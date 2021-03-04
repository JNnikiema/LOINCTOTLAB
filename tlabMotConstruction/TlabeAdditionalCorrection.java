package tlabMotConstruction;

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

public class TlabeAdditionalCorrection {

	public static void main(String[] args) throws RepositoryException, QueryEvaluationException, MalformedQueryException {
		// TODO Auto-generated method stub
		String namespace = "TLABANDLOINC";
		//String namespace="FrenchLOINC";
		
		
		Repository repo = RepositoryFactory.getRepository(namespace);
		RepositoryConnection conn = repo.getConnection();
		ValueFactory vf = conn.getValueFactory();
		String query="prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" + 
				"select distinct ?s ?o \n" + 
				"where {\n" + 
				"  \n" + 
				"  graph<http://erias.org/InterfaceTerminology/TLAB/Correction>{\n" + 
				"   ?s rdfs:label ?o\n" + 
				"      \n" + 
				"    \n" + 
				"    }\n" + 
				"      \n" + 
				"       }";
		
		
		ArrayList<BindingSet> ListeRDFTotal= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query));
		 String Prefix_rdfs="http://www.w3.org/2000/01/rdf-schema#";
		 Set<Statement> lesSatement = new HashSet<Statement>();
		 for(BindingSet e: ListeRDFTotal){
			 String codeLoinc=e.getValue("s").stringValue();
			 //System.out.println(codeLoinc);
			 String label=e.getValue("o").stringValue();
			 String labels=label.replace("hematies", "globules rouges, gr");
			 labels=labels.replace("hématies", "globules rouges, gr");
			 labels=labels.replace("Hématies", "globules rouges, gr");
			 Statement stm = vf.createStatement( vf.createURI(codeLoinc),
						vf.createURI(Prefix_rdfs + "label"),
						vf.createLiteral(labels));
				lesSatement.add(stm);
				String labelss=label.replace("hematies", "érythrocytes");
				 labelss=labelss.replace("hématies", "érythrocytes");
				 labelss=labelss.replace("Hématies", "érythrocytes");
				 Statement stm2 = vf.createStatement( vf.createURI(codeLoinc),
							vf.createURI(Prefix_rdfs + "label"),
							vf.createLiteral(labelss));
					lesSatement.add(stm2);
					
		 }
		 String GraphLoincFrancais = "http://erias.org/InterfaceTerminology/TLAB/Correction";
			System.out.println("debit");
			URI graphLoincFrancais = conn.getValueFactory().createURI(GraphLoincFrancais);
			List<Statement> liste= new ArrayList<Statement>(lesSatement);
			System.out.println(liste.size());

			try {	

				conn.add(liste,graphLoincFrancais);
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("terminé ");
	}

	//public static 
}
