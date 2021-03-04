package interactionTripleStore;

import java.io.IOException;

import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFParseException;

public class ImportOntolo {

	public static void main(String[] args) throws RepositoryException, RDFParseException, IOException {
		// TODO Auto-generated method stub
		String namespace = "ENSC";
		String cheminFichier ="/home/erias/Bureau/alignementLoincInterface/human.rdfs";
		//String cheminFichier ="/home/erias/Bureau/TerminologieLocale/synergie.skos" ;
		String URIdelaterminologie = "http://erias.org/InterfaceTerminology/TLAB";
		//ChargementFichierOntologie.ChargementFichier(namespace, cheminFichier, URIdelaterminologie,"text/turtle");
		ChargementFichierOntologie.ChargementFichier(namespace, cheminFichier, URIdelaterminologie,"application/rdf+xml");
	
		System.out.println("ok jn");

	}

}
