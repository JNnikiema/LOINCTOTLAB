package interactionTripleStore;


import java.util.Properties;

import org.openrdf.model.Statement;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import com.bigdata.rdf.sail.webapp.SD;

import com.bigdata.rdf.sail.webapp.client.RemoteRepositoryManager;
/**
 * classe permettant d'interagir avec un repertoire blazgraph
 * @author erias
 *
 */
public class RepositoryFactory {
	public static int port = 8889;
	public static String urlBlazegraph = "http://localhost:"+port+"/bigdata";
	
	
	public static Repository repoC;
	
	public static Repository getRepository(String namespace) throws RepositoryException {
		if (repoC != null) return repoC;
		System.out.println(urlBlazegraph);
		RemoteRepositoryManager repoo=new RemoteRepositoryManager(urlBlazegraph);
		final Properties properties = new Properties();
		properties.setProperty("com.bigdata.rdf.sail.namespace", namespace);
		try {
			if (!namespaceExists(repoo, namespace)) {
				repoo.createRepository(namespace, properties);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		RepositoryFactory.repoC = repoo.getRepositoryForNamespace(namespace).getBigdataSailRemoteRepository();
		return repoC;
	}
	/*
	 * Check namespace already exists.
	 */
	private static boolean namespaceExists(final RemoteRepositoryManager repo,
			final String namespace) throws Exception {
		
		final GraphQueryResult res = repo.getRepositoryDescriptions();
		try {
			while (res.hasNext()) {
				final Statement stmt = res.next();
				if (stmt.getPredicate()
						.toString()
						.equals(SD.KB_NAMESPACE.stringValue())) {
					if (namespace.equals(stmt.getObject().stringValue())) {
						return true;
					}
				}
			}
		} finally {
			res.close();
		}
		return false;
	}
}