package mappingsMots;

import java.io.IOException;

import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFParseException;

public class RunImportMapping {

	public static void main(String[] args) throws RepositoryException, RDFParseException, IOException {
		// TODO Auto-generated method stub
		//String namespace="TLAB";
		String namespace = "TLABANDLOINC";
		importMappings mapping = new importMappings(namespace);
		System.out.println("model d'import terminé ");

	}

}
