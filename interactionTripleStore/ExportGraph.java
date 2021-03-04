package interactionTripleStore;


import java.io.File;
import java.io.FileNotFoundException;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFHandlerException;

public class ExportGraph {
	public ExportGraph(String Grahoque, String namespace, String chemin) throws RepositoryException{
		String filepath= chemin;
		File file = new File(filepath);
		Repository repo=RepositoryFactory.getRepository(namespace);
		try {
			InteractionBlazgraph.getGraphByURI(repo, Grahoque, file);
		} catch (FileNotFoundException | RDFHandlerException | QueryEvaluationException | MalformedQueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
