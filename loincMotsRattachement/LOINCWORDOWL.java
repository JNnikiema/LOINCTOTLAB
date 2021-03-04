package loincMotsRattachement;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import interactionTripleStore.RepositoryFactory;

public class LOINCWORDOWL {

	public static void main(String[] args) throws QueryEvaluationException, RepositoryException, MalformedQueryException, OWLOntologyCreationException, OWLOntologyStorageException {
		// TODO Auto-generated method stub
		//String namespace="FrenchLOINC";
		String namespace = "TLABANDLOINC";
		Repository repo = RepositoryFactory.getRepository(namespace);
		RepositoryConnection conn = repo.getConnection();
		
		
		String query="prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
				"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
				"				 Select distinct ?s ?o  WHERE { \n" + 
				"				\n" + 
				"                 GRAPH <http://erias.org/FrenchLoinc> {\n" + 
				"           \n" + 
				"				\n" + 
				"                 ?s rdf:type<http://erias.org/LOINC#Mot2>.\n" + 
				"                   ?s rdfs:label ?o\n" + 
				"                      }\n" + 
				"                  \n" + 
				"				 }";
		HashMap<String,Set<String>> wordandlabels=	LoincMotsCreation.getresultLabels(conn, query);
		
		System.out.println("creation du format owl des mots");
		OWLOntologyManager manager= OWLManager.createOWLOntologyManager();
		OWLOntology ontologyLOINCMot = manager.createOntology(IRI.create("http://erias.org/loincTotal"));
		
		
		OWLDataFactory factory=manager.getOWLDataFactory();
		Set<OWLAxiom> lesaxiomMot= new HashSet<OWLAxiom>();
		for(String motDecrit: wordandlabels.keySet()){
			
			OWLClass CodeMotLoinc= factory.getOWLClass(IRI.create(motDecrit));
			System.out.println("CodeMotLoinc "+CodeMotLoinc);
			
			OWLDeclarationAxiom declarati=factory.getOWLDeclarationAxiom(CodeMotLoinc);
			lesaxiomMot.add(declarati);
			for(String libelleMot: wordandlabels.get(motDecrit)){
				OWLAnnotation Annotation = factory.getOWLAnnotation(factory.getRDFSLabel(),
						factory.getOWLLiteral(libelleMot));
				lesaxiomMot.add(factory.getOWLAnnotationAssertionAxiom(CodeMotLoinc.getIRI(), Annotation));
				System.out.println("lesaxiomMot "+factory.getOWLAnnotationAssertionAxiom(CodeMotLoinc.getIRI(), Annotation));
			}
		}
		manager.addAxioms(ontologyLOINCMot, lesaxiomMot);
		File fichierchemin= new File("/home/erias/Bureau/graph/LOINC/LOINCWORDTOTAL.owl");
		//LOINCWORDTOTAL.owl last update previous LOINCWORD.owl
		RDFXMLOntologyFormat Format= new RDFXMLOntologyFormat();
		manager.saveOntology(ontologyLOINCMot, Format,IRI.create(fichierchemin));
		
		
		
		System.out.println("fini terminé");
		
		
		
		
		
		
	}
	

}
