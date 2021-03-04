package tlabMotConstruction;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryParser.ParseException;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
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
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import interactionRelationalDatabase.ConnexionBD;
import interactionRelationalDatabase.RequeteSQL;
import interactionTripleStore.InteractionBlazgraph;
import loincMotsRattachement.LoincMotsCreation;
import preTraitement.RecuperationGraphRDF;
import utilitaries.Couple;
import utilitaries.LibelleAvecEspaceCorrecte;

public class TlabWords {

	public static void main(String[] args) throws OWLOntologyCreationException, ParseException, OWLOntologyStorageException {
		// TODO Auto-generated method stubpublic FormatOWLIntegration(Connection connex,String namespace) throws OWLOntologyCreationException, OWLOntologyStorageException, ParseException{
		ConnexionBD jnnn= new ConnexionBD(11);
		Connection connRelationalBase=jnnn.getconn();
		Set<String> StopWordFrancais= new HashSet<String>();
		HashMap<String, Set<String>> codeLibelle= new HashMap<String, Set<String>>();
		
	//	String namespace="TLAB";
		String namespace = "TLABANDLOINC";
		
		RepositoryConnection connnex= InteractionBlazgraph.connex(namespace);
		String interfaceTerminoCorrection = "http://erias.org/InterfaceTerminology/TLAB/Correction";
		String interfaceTerminoOriginal = "http://erias.org/InterfaceTerminology/TLAB";
		Set<Statement> lesSatement= new HashSet<Statement>();
		Set<Statement> lesSatementModel= new HashSet<Statement>();
		
		RecuperationGraphRDF terminologie= new RecuperationGraphRDF(connnex, "b", interfaceTerminoOriginal);
		HashMap<String, List<String>> TerminologieOriginal=terminologie.getRDFlisteLibellé();
		
		RecuperationGraphRDF terminologie2= new RecuperationGraphRDF(connnex, "graphLibelleCorrige", interfaceTerminoCorrection);
		HashMap<String, List<String>> TerminologiLibellecorrige=terminologie2.getRDFlisteLibellé();
		
		OWLOntologyManager manager= OWLManager.createOWLOntologyManager();
		OWLOntology ontologyLocalMot = manager.createOntology(IRI.create("http://erias.org/TLAB/OWL/WORD#"));
		
		OWLDataFactory factory=manager.getOWLDataFactory();
		Set<String> motTerminologie=new HashSet<String>();
		String ExpressionMotGraceAuxEspaces="([^\\s]+\\.*)";
		Pattern patternMotdulibbeleGraceAespace= Pattern.compile(ExpressionMotGraceAuxEspaces);
		Set<OWLAxiom> lesaxiomMot= new HashSet<OWLAxiom>();
		
		PrefixManager prefixeMot = new DefaultPrefixManager("http://erias.org/TLAB/OWL/WORD#");
		String prefixeModeleRelation = "http://erias.org/TLAB/OWL/Relation#";
		
		HashMap<String,Set<Couple>> TerminologieDescription= new HashMap<String,Set<Couple>>();
		
		
		
		RequeteSQL stopWO = new RequeteSQL("stop");
		
		try {
			PreparedStatement stowww = connRelationalBase.prepareStatement(stopWO.getrequ());
			ResultSet stopworde = stowww.executeQuery();
			while(stopworde.next()){
				StopWordFrancais.add(stopworde.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		for(String Codeterm:TerminologiLibellecorrige.keySet()){
			Set<String> libellePourlecode= new HashSet<String>();
			libellePourlecode.addAll(TerminologiLibellecorrige.get(Codeterm));
			if(!codeLibelle.containsKey(Codeterm)){
				codeLibelle.put(Codeterm, new HashSet<String>());
			}
			codeLibelle.get(Codeterm).addAll(libellePourlecode);
			for(String libe :libellePourlecode){
				String libelles = StringUtils.stripAccents(libe).toLowerCase();
				String libelle =LoincMotsCreation.libelleSansLescaracteristiquesDeRegex(libelles);
						
				Matcher matcheurMotgraceAespaces= patternMotdulibbeleGraceAespace.matcher(libelle);
					while(matcheurMotgraceAespaces.find()){
//						String motNiveau3=matcheurMotgraceAespaces.group(1);
//						String motNiveau2="";
//						for(String motss:motNiveau3.split(":")){
//							motNiveau2=motNiveau2+" "+motss;
//						}
//						String motNiveau1 = LibelleAvecEspaceCorrecte.getLibelleCorrect(motNiveau2);
//						String mots ="";
						
						String motNiveau3=matcheurMotgraceAespaces.group(1);
						
						String motNiveau2="";
						for(String motss:motNiveau3.split("(:|--->)")){
							motNiveau2=motNiveau2+" "+motss;
						}
						String motNiveau1 =LibelleAvecEspaceCorrecte.getLibelleCorrect(motNiveau2);
						String mots ="";
						if(motNiveau1.length()>1){
							System.out.println("motNiveau1 *"+motNiveau1+"*");
							if(!motNiveau1.equals("!!!")) {
								if(!motNiveau1.equals("----")) {
									if(!motNiveau1.equals("-->")) {
						 mots =LoincMotsCreation.stemTerm(motNiveau1);
									}
								}
							}
						}
						else{
							 mots =motNiveau1;
						}
						Matcher matcheurMotgraceAespac= patternMotdulibbeleGraceAespace.matcher(mots);
						while(matcheurMotgraceAespac.find()){
							String mot= matcheurMotgraceAespac.group(1);
							String mot1= matcheurMotgraceAespac.group(1);
							if(mot1.startsWith(":")&&mot1.length()>1){
								motTerminologie.add(mot1.substring(1));
								
							}
							else{
								motTerminologie.add(mot1);
							}
						}
						//codeComposant.add(mot);
						
						
					}
					
						
				
				
			}
			
			
		}
		
		for(String Codeterm:TerminologieOriginal.keySet()){
			Set<String> libelleOPourlecode= new HashSet<String>();
			libelleOPourlecode.addAll(TerminologiLibellecorrige.get(Codeterm));
			if(!codeLibelle.containsKey(Codeterm)){
				codeLibelle.put(Codeterm, new HashSet<String>());
			}
			codeLibelle.get(Codeterm).addAll(libelleOPourlecode);
			for(String libe:libelleOPourlecode){
				String libelleOrs = StringUtils.stripAccents(libe).toLowerCase();
				
				String libelleOr =LoincMotsCreation.libelleSansLescaracteristiquesDeRegex(libelleOrs);
				
					Matcher matcheurMotgraceAespaces= patternMotdulibbeleGraceAespace.matcher(libelleOr);
					while(matcheurMotgraceAespaces.find()){
						
//						String motNiveau1=matcheurMotgraceAespaces.group(1);
//						String mots ="";
						String motNiveau3=matcheurMotgraceAespaces.group(1);
						//System.out.println("motNiveau3 "+motNiveau3);
						String motNiveau2="";
						for(String motss:motNiveau3.split("(:|--->)")){
							motNiveau2=motNiveau2+" "+motss;
						}
						//System.out.println("motNiveau2 "+motNiveau2);
						String motNiveau1 = LibelleAvecEspaceCorrecte.getLibelleCorrect(motNiveau2);
						String mots ="";
						if(motNiveau1.length()>1){
							if(!motNiveau1.equals("!!!")) {
								if(!motNiveau1.equals("----")) {
									if(!motNiveau1.equals("-->")) {
						 mots =LoincMotsCreation.stemTerm(motNiveau1);
									}
								}
							}
						}
						else{
							 mots =motNiveau1;
						}
						Matcher matcheurMotgraceAespac= patternMotdulibbeleGraceAespace.matcher(mots);
						while(matcheurMotgraceAespac.find()){
							String mot1= matcheurMotgraceAespac.group(1);
							if(mot1.startsWith(":")&&mot1.length()>1){
								motTerminologie.add(mot1.substring(1));
							}
							else{
								motTerminologie.add(mot1);
							}
							
						}
						//codeComposant.add(mot);
						
						
					
						
						
					}
					
						
				
				
			}
			
			
		}
			Set<String> lesMotsansMotVideTerminologi= new HashSet<String>();
		for(String motstrouve:motTerminologie){
			if(!StopWordFrancais.contains(motstrouve)){
				lesMotsansMotVideTerminologi.add(motstrouve);
			}
		}
		
		String Leschiffre="([0-9]+)";
		Pattern patternLesChiffres= Pattern.compile(Leschiffre);
		
		
		Set<String> lesMotsansChiffreniLettreDeMoinsDuneLettreTerminologi= new HashSet<String>();
		
		
		for(String motssansMotvide:lesMotsansMotVideTerminologi){
			//if(motssansMotvide.length()>1&&!patternLesChiffres.matcher(motssansMotvide).matches()){
				lesMotsansChiffreniLettreDeMoinsDuneLettreTerminologi.add(motssansMotvide);
			//}
			
		}
		
		
		HashMap<String, Set<String>> MotAssocieCode= new HashMap<String, Set<String>>();
		int gj=0;
		for(String motDecrit: lesMotsansChiffreniLettreDeMoinsDuneLettreTerminologi){
			gj++;
			OWLClass codeTerminolog= factory.getOWLClass("TermWord"+gj,prefixeMot);
			
			OWLDeclarationAxiom declarati=factory.getOWLDeclarationAxiom(codeTerminolog);
			lesaxiomMot.add(declarati);
			OWLAnnotation Annotation = factory.getOWLAnnotation(factory.getRDFSLabel(),
					factory.getOWLLiteral(motDecrit));
			lesaxiomMot.add(factory.getOWLAnnotationAssertionAxiom(codeTerminolog.getIRI(), Annotation));
			
			if(!MotAssocieCode.containsKey(motDecrit)){
				MotAssocieCode.put(motDecrit, new HashSet<String>());
			}
			MotAssocieCode.get(motDecrit).add("TermWord"+gj);
		}
	//	OWLObjectProperty has_word = factory.getOWLObjectProperty(":has_word", prefixeModeleRelation);
		ValueFactory vf = connnex.getValueFactory();
		
		String Prefix_rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		for(String Codeterm:codeLibelle.keySet()){
			System.out.println("code du terme : "+Codeterm);
			
			Statement stm2 = vf.createStatement( vf.createURI(Codeterm),
					vf.createURI(Prefix_rdf + "type"),
					vf.createURI("http://erias.org/integration/TLAB/model#BiologicalAnalysis"));
			lesSatementModel.add(stm2);
			
			for(String libelleCre:codeLibelle.get(Codeterm)){
				String Prefix_rdfs="http://www.w3.org/2000/01/rdf-schema#";	
				String libelleJN = StringUtils.stripAccents(libelleCre).toLowerCase();
				String libelleCree =LoincMotsCreation.libelleSansLescaracteristiquesDeRegex(libelleJN);
				
				Matcher matcheurMotgraceAespaces= patternMotdulibbeleGraceAespace.matcher(libelleCree);
				
					while(matcheurMotgraceAespaces.find()){
					//	String mot=matcheurMotgraceAespaces.group(1);
						
						

						
//						String motNiveau1=matcheurMotgraceAespaces.group(1);
//						String mots ="";
						String motNiveau3=matcheurMotgraceAespaces.group(1);
						//System.out.println("motNiveau3 "+motNiveau3);
						String motNiveau2="";
						for(String motss:motNiveau3.split("(:|--->)")){
							motNiveau2=motNiveau2+" "+motss;
						}
						//System.out.println("motNiveau2 "+motNiveau2);
						String motNiveau1 = LibelleAvecEspaceCorrecte.getLibelleCorrect(motNiveau2);
						String mots ="";
						if(motNiveau1.length()>1){
							if(!motNiveau1.equals("!!!")) {
								if(!motNiveau1.equals("----")) {
									if(!motNiveau1.equals("-->")) {
						 mots =LoincMotsCreation.stemTerm(motNiveau1);
									}
								}
							}
						}
						else{
							 mots =motNiveau1;
						}
						Matcher matcheurMotgraceAespac= patternMotdulibbeleGraceAespace.matcher(mots);
						while(matcheurMotgraceAespac.find()){
							String mottt= matcheurMotgraceAespac.group(1);
							String mot="";
							if(mottt.startsWith(":")&&mottt.length()>1){
								mot=mottt.substring(1);
							}
							else{
								mot=mottt;
							}

							if(MotAssocieCode.containsKey(mot)){
								for(String codeMot:MotAssocieCode.get(mot)){
									System.out.println("code du mot"+codeMot);
									Statement stm = vf.createStatement( vf.createURI(Codeterm),
											vf.createURI(prefixeModeleRelation+":has_word"),
											vf.createURI("http://erias.org/TLAB/OWL/WORD#"+codeMot));
									lesSatement.add(stm);
									Statement stmlabel = vf.createStatement(vf.createURI("http://erias.org/TLAB/OWL/WORD#"+codeMot),
											vf.createURI(Prefix_rdfs + "label"),
											vf.createLiteral (mot));
									lesSatement.add(stmlabel);
									Statement stm1 = vf.createStatement( vf.createURI("http://erias.org/TLAB/OWL/WORD#"+codeMot),
											vf.createURI(Prefix_rdf + "type"),
											vf.createURI("http://erias.org/integration/TLAB/model#Mot1"));
									lesSatementModel.add(stm1);
									
									
								}
							}
						}
						//codeComposant.add(mot);
						
						
					
						
						
					
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
					}
				
			}
			
			
		}
		String interfaceGaphdecomposition = "http://erias.org/TLAB/OWL/WORDTOTAL#";
		String graphModel= "http://erias.org/integration/TLAB/model#";
		
		URI graphTerm_URI = connnex.getValueFactory().createURI(interfaceGaphdecomposition);
		List<Statement> liste= new ArrayList<Statement>(lesSatement);
		System.out.println(liste.size());
		
		URI Model_URI = connnex.getValueFactory().createURI(graphModel);
		List<Statement> liste2= new ArrayList<Statement>(lesSatementModel);
		System.out.println(liste2.size());
		
		System.out.println("nombre de mot dans la termiologie d'interface : "+motTerminologie.size());
		
		System.out.println("la liste des mots vide stopword: "+StopWordFrancais.size());
	
		System.out.println("la liste des mots sans stopword dans la terminologie d'interface "+lesMotsansMotVideTerminologi.size());
		
		
		System.out.println("debut d'insertion");
		try {
			
			connnex.add(liste2, Model_URI);
			connnex.add(liste,graphTerm_URI);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("fin d'insertion");
		
		
		
		
		
		
		
		
		
		
		
		manager.addAxioms(ontologyLocalMot, lesaxiomMot);
		File fichierchemin= new File("/home/erias/Bureau/graph/TLABCORRECTIONS/TLABWORDTOTAL.owl");
		RDFXMLOntologyFormat Format= new RDFXMLOntologyFormat();
		manager.saveOntology(ontologyLocalMot, Format,IRI.create(fichierchemin));
		
		
	
	
		
	

	}
	

}
