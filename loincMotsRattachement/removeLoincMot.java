package loincMotsRattachement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import interactionTripleStore.InteractionBlazgraph;

public class removeLoincMot {

	public static void main(String[] args) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		// TODO Auto-generated method stub
		String namespace = "TLABANDLOINC";
		//String namespace = "ENGLISHLOINC";
		//String namespace="FrenchLoincAnalysis";
		RepositoryConnection conn= InteractionBlazgraph.connex(namespace);
	ValueFactory vf = conn.getValueFactory();
	//getTripletSujet(conn, vf);
	getTripletObjet(conn, vf);
	System.out.println("fini tout ");
	

	}
	public static void getTripletSujet(RepositoryConnection conn,ValueFactory vf ) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		String query="prefix lnc:<http://erias.org/loincTotal#> \n" + 
				"									 prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"									 prefix MOD:<http://erias.org/integration/TLAB/model#>  \n" + 
				"									 prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> \n" + 
				"									  select ?s ?p ?o \n" + 
				"									 where{ \n" + 
				"									\n" + 
				"									?s a <http://erias.org/LOINC#Mot2>. \n" + 
				"									 ?s ?p ?o. \n" + 
				"                                       ?d ?j ?s.\n" + 
				"									} ";
		
 ArrayList<BindingSet> ListeRDFTotal= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query));
		 Set<Statement> lesSatement= new HashSet<Statement>();
		 for(BindingSet e: ListeRDFTotal){
			 if(e.getValue("o").stringValue().startsWith("http")) {
			Statement stm = vf.createStatement( vf.createURI(e.getValue("s").stringValue()),
						vf.createURI(e.getValue("p").stringValue()),
						vf.createURI(e.getValue("o").stringValue()));
				lesSatement.add(stm);
			 }
			 else {
				 Statement stm = vf.createStatement( vf.createURI(e.getValue("s").stringValue()),
							vf.createURI(e.getValue("p").stringValue()),
							vf.createLiteral(e.getValue("o").stringValue()));
					lesSatement.add(stm);
			 }
			
		 }
		 conn.remove(lesSatement);
		
	}
	public static void getTripletObjet(RepositoryConnection conn,ValueFactory vf ) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		String query="prefix lnc:<http://erias.org/loincTotal#> \n" + 
				"									 prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"									 prefix MOD:<http://erias.org/integration/TLAB/model#>  \n" + 
				"									 prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> \n" + 
				"									  select ?s ?d ?j \n" + 
				"									 where{ \n" + 
				"					  	 GRAPH<http://erias.org/FrenchLoinc> { "+
				"									?o lnc:has_word ?s.\n" + 
				"                                       \n" + 
				"                                      \n" + 
				"                                       ?d ?j ?s\n" + 
				"									}"
				+ " } ";
		System.out.println("ouf ");
 ArrayList<BindingSet> ListeRDFTotal= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query));
		 Set<Statement> lesSatement= new HashSet<Statement>();
		 int i=0;
		 System.out.println("les débuts ");
		 for(BindingSet e: ListeRDFTotal){
			 
			Statement stm = vf.createStatement( vf.createURI(e.getValue("d").stringValue()),
						vf.createURI(e.getValue("j").stringValue()),
						vf.createURI(e.getValue("s").stringValue()));
				lesSatement.add(stm);
				int u=50000;
			 if(i==u) {
				 conn.remove(lesSatement);
				 lesSatement= new HashSet<Statement>();
				 System.out.println(lesSatement);
				 u=u+50000;
			 }
			
			
		 }
		 conn.remove(lesSatement);
		 System.out.println("fini");
		
	}

}
