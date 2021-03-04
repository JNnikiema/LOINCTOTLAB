package interactionTripleStore;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.openrdf.model.URI;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;

/**
 * Cette classe permet de charger un fichier dans blazgraph
 * @author erias
 *
 */
public class ChargementFichierOntologie {
	public static final Map<String, RDFFormat> transcodeMime2RDFFormat;
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
	public static void ChargementFichier(String namespace, String Chemin, String uriGraph,String formatDecriture) throws RepositoryException, RDFParseException, IOException{
		
		Repository repo = RepositoryFactory.getRepository(namespace);
		RepositoryConnection conn = repo.getConnection();
		final File fichierOntologie = new File(Chemin);
		URI graphLOINC_Termi = conn.getValueFactory().createURI(uriGraph);
		System.out.println("debut");
		System.out.println("c'est bon");
		conn.add(fichierOntologie, "", transcodeMime2RDFFormat.get(formatDecriture), graphLOINC_Termi);
		
		System.out.println("c'est fini");
		try {
			conn.close(); 
		} catch (final RepositoryException ignore) { 
			System.out.println("problème de fermeture de la connexion");
		}


	}

}
