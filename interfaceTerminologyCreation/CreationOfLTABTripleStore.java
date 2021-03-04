package interfaceTerminologyCreation;

import java.io.IOException;

import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFParseException;

import interactionTripleStore.ChargementFichierOntologie;

public class CreationOfLTABTripleStore {

	public static void main(String[] args) throws RepositoryException, RDFParseException, IOException {
		// TODO Auto-generated method stub
		//String namespace = "TLAB";
		String namespace = "TLABANDLOINC";
		String cheminFichier ="./TLAB.rdf";
		//String cheminFichier ="/home/erias/Bureau/TerminologieLocale/synergie.skos" ;
		String URIdelaterminologie = "http://erias.org/InterfaceTerminology/TLAB";
		//ChargementFichierOntologie.ChargementFichier(namespace, cheminFichier, URIdelaterminologie,"text/turtle");
		ChargementFichierOntologie.ChargementFichier(namespace, cheminFichier, URIdelaterminologie,"application/xml");
		
	}

}
