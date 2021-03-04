package interactionTripleStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.query.BindingSet;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;

import com.bigdata.rdf.sail.remote.BigdataSailRemoteRepository;
import com.bigdata.rdf.sail.remote.BigdataSailRemoteRepositoryConnection;


/**
	Cette classe repertorie des méthodes permettant d'interagir avec la base de données graph
*/
public class InteractionBlazgraph {
	/**
	 chargeons  dans transcodeMime2RDFFormat le format choisi de stockage ou de sauvegarde de la terminologie
	 */

	public static final Map<String, RDFFormat> transcodeMime2RDFFormat;
	/**
	 les différents format RDF décrit
	 */
	static {
		Map<String, RDFFormat> tmp = new HashMap<String, RDFFormat>();
		tmp.put("application/rdf+xml", RDFFormat.RDFXML);
		tmp.put("application/xml", RDFFormat.RDFXML);
		tmp.put("text/plain", RDFFormat.NTRIPLES);

		tmp.put("text/turtle", RDFFormat.TURTLE);
		tmp.put("application/x-turtle", RDFFormat.TURTLE);

		tmp.put("text/n3", RDFFormat.N3);
		tmp.put("text/rdf+n3", RDFFormat.N3);

		tmp.put("application/trix", RDFFormat.TRIX);
		tmp.put("application/x-trig", RDFFormat.TRIG);

		tmp.put("application/x-binary-rdf", RDFFormat.BINARY);

		tmp.put("text/x-nquads", RDFFormat.NQUADS);

		tmp.put("application/ld+json", RDFFormat.JSONLD);
		tmp.put("application/rdf+json", RDFFormat.RDFJSON);
		tmp.put("application.xhtml+xml", RDFFormat.RDFA);

		transcodeMime2RDFFormat = Collections.unmodifiableMap(tmp);
	}
	/**
	 triplet rechercher par la requete
	 * @param conn
	 * @param query
	 * @throws QueryEvaluationException
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 */

	public static ArrayList<BindingSet> selectQuery(RepositoryConnection conn, String query) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		//System.out.println(query);
		TupleQueryResult t = conn.prepareTupleQuery(QueryLanguage.SPARQL, query).evaluate();
		
		
		try {
			ArrayList<BindingSet> liste= new ArrayList<BindingSet>();
		//	int i=1;
			while (t.hasNext()) {
				BindingSet bs = t.next();
				liste.add(bs);

				//Value s = bs.getValue("s");
				//Value p = bs.getValue("p");
				//Value o = bs.getValue("o");
			//	System.out.println("nombre d'occurence : "+i);
			//	i++;

			//	if (s != null && p != null && o != null)
					//System.out.println(s.stringValue()+" --"+p.stringValue()+"--> "+o.stringValue());
					
				
			}
			return liste;
		} finally {
			t.close();
			conn.close();
		}
	}

	public static void insertQuery(Repository repo, String query) throws RepositoryException, UpdateExecutionException, MalformedQueryException {
		RepositoryConnection conn = repo.getConnection();
		try{
		conn.prepareUpdate(QueryLanguage.SPARQL, query).execute();
		}
		finally {
			if (conn != null) {
				try {
					conn.close(); 
				} catch (final RepositoryException ignore) { 
					System.out.println("problème de fermeture de la connexion");
				}
			}
		}
		
	}

	public static void getGraphByURI(Repository repo, String context, File f) throws FileNotFoundException, RDFHandlerException, RepositoryException, QueryEvaluationException, MalformedQueryException {
		RepositoryConnection conn = repo.getConnection();
		String query = /* get all prefixes */
				"CONSTRUCT {?s ?p ?o}"
				+ "WHERE {"
				+ "		GRAPH <"+context+"> {"
				+ "			?s ?p ?o"
				+ "		}"
				+ "}";

		GraphQueryResult gq = conn.prepareGraphQuery(QueryLanguage.SPARQL, query).evaluate();
		OutputStream out = null;			

		try {
			out = new FileOutputStream(f);

			RDFWriter writer = Rio.createWriter(RDFFormat.RDFXML, out);	
			Map<String, String> namespaces = gq.getNamespaces();

			for(Entry<String, String> e: namespaces.entrySet())
				writer.handleNamespace(e.getKey(), e.getValue());

			writer.startRDF();
			while(gq.hasNext()) {
				Statement s = gq.next();
				writer.handleStatement(s);
				//System.out.println(s);
			}
			writer.endRDF();
			System.out.println("endRDF");
		} finally {
			conn.close();
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void importISrdf(BigdataSailRemoteRepository repo,
			String baseURI,
			InputStream is,
			String mimeType,
			URI uri) throws IOException, RDFParseException, RepositoryException {

		BigdataSailRemoteRepositoryConnection conn = repo.getConnection();

		try {
			conn.add(is, baseURI, transcodeMime2RDFFormat.get(mimeType) , uri);
		} finally {
			if (conn != null) {
				try {
					conn.close(); 
				} catch (final RepositoryException ignore) { 

				}
			}
		}
	}
	public static void listFilesForFolder(final File folder, RepositoryConnection conn) throws RDFParseException, RepositoryException, IOException {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				System.out.println("Folder : " + fileEntry.getAbsolutePath());
				listFilesForFolder(fileEntry, conn);
			} else {
				System.out.println("Adding : " + fileEntry.getAbsolutePath());
				conn.add(fileEntry, "", transcodeMime2RDFFormat.get("text/turtle"));
			}
		}
	}
	
public static void insertGraph(String namespace, String filepath, String URIchoice) throws RDFParseException, RepositoryException, MalformedURLException, IOException, org.openrdf.repository.RepositoryException, org.openrdf.rio.RDFParseException, QueryEvaluationException, MalformedQueryException {
		

		
		RepositoryConnection conn = InteractionBlazgraph.connex(namespace);
	
		
		final File filettl = new File(filepath);
		URI graph_URI=  ChoixURISpecific(namespace, URIchoice);
		
		
		
		try{
		conn.add(filettl, "", transcodeMime2RDFFormat.get("text/turtle"), graph_URI);
		
		}
		finally{
			if (conn != null) {
				try {
					conn.close(); 
				} catch (final RepositoryException ignore) { 
					System.out.println("problème de fermeture de la connexion");
				}
			}
		}
		
	}
	public static RepositoryConnection connex(String namespace){
		Repository repo;
		try {
			repo = RepositoryFactory.getRepository(namespace);
			RepositoryConnection conn = repo.getConnection();
			return conn;
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	public static URI ChoixURISpecific(String ChoixNamespace, String Choixgraph){
		RepositoryConnection connt= InteractionBlazgraph.connex(ChoixNamespace);
		
		String loincJN = "http://erias.org/integration/LOINC";
		URI graphLOINC_URI = connt.getValueFactory().createURI(loincJN);
		
		String interfaceJN = "http://erias.org/integration/CHU-terminologie/InterfaceTerminologie";
		URI graphTerm_URI = connt.getValueFactory().createURI(interfaceJN);
		
		if (Choixgraph.equals("a")){
			return graphLOINC_URI;
			
		}
		else if(Choixgraph.equals("b")){
			return graphTerm_URI;
			
		}
		if (connt != null) {
			try {
				connt.close(); 
			} catch (final RepositoryException ignore) { 
				System.out.println("problème de fermeture de la connexion");
			}
		}
		return graphLOINC_URI;
		
	}

}
