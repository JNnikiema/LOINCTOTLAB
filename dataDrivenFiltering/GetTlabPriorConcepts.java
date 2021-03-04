package dataDrivenFiltering;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import interactionRelationalDatabase.ConnexionBD;
import interactionTripleStore.InteractionBlazgraph;
import interactionTripleStore.RepositoryFactory;

public class GetTlabPriorConcepts {

	public static void main(String[] args) throws RepositoryException, QueryEvaluationException, MalformedQueryException, SQLException {
		// TODO Auto-generated method stub
		

		HashMap<String, String> codeAnnotation=getCodeAndAnotation();
		Set<String> annotiona=getTheUsedAnnotation();
		HashMap<String, String> anotabNbre=getTheUsedAnnotationNbreLabo();
		
		Set<String> baseDeValidation = new HashSet<String>();
		for(String anot:anotabNbre.keySet() ) {
			if(annotiona.contains(anot)) {
				baseDeValidation.add(anot);
			}
		}
		Set<String> identifiedAnnotationCode= new HashSet<String>();
		Set<String> identifiedAnnotation= new HashSet<String>();
		Set<String> NonidentifiedAnnotationCode= new HashSet<String>();
		Set<String> NonidentifiedAnnotation= new HashSet<String>();
	for(String code: codeAnnotation.keySet()) {
			if(annotiona.contains(codeAnnotation.get(code))) {
				identifiedAnnotationCode.add(code);
				identifiedAnnotation.add(codeAnnotation.get(code));
			}
			else {
				NonidentifiedAnnotationCode.add(code);
				
				//System.out.println(code);
			}
		}
		
		for(String lab:annotiona) {
			if(!identifiedAnnotation.contains(lab)) {
				NonidentifiedAnnotation.add(lab);
			}
			
		}
		System.out.println("identifiedAnnotationCode "+identifiedAnnotationCode.size());
		System.out.println("NonidentifiedAnnotationCode "+NonidentifiedAnnotationCode.size());
		System.out.println("NonidentifiedAnnotation "+NonidentifiedAnnotation.size());
		System.out.println("baseDeValidation "+baseDeValidation.size());
		
		String namespace = "TLABANDLOINC";
		Repository repo = RepositoryFactory.getRepository(namespace);
		RepositoryConnection conn = repo.getConnection();
		
		ValueFactory vf = conn.getValueFactory();
		Set<Statement> statements= new HashSet<Statement>();
		
		for(String code:codeAnnotation.keySet()) {
			String annot=codeAnnotation.get(code);
			if(identifiedAnnotationCode.contains(code)) {
			Statement tgrd= vf.createStatement(vf.createURI(code), vf.createURI("http://www.w3.org/2004/02/skos/core#notation"), vf.createLiteral(annot));
				statements.add(tgrd);
				
			}
		}
		
		
		List<Statement> liste= new ArrayList<Statement>(statements);
		System.out.println(liste.size());
		String interfaceUsefullElements = "http://erias.org/InterfaceTerminology/TLAB/UsedCodeOneYear";
		
		URI graphTerm_URI = conn.getValueFactory().createURI(interfaceUsefullElements);
		System.out.println("debut d'insertion");
		try {
			

			conn.add(liste,graphTerm_URI);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(" fin d'insertion ");
		
		int nbreDeResultatIdentifie=0;
		int NonIdenfienbreDeResultat=0;
		int maximumResult=0;
		int maximumResultIdentifie=0;
		int maximumResultNonIdentifie=0;
		
		for(String ann:anotabNbre.keySet()) {
			String result= anotabNbre.get(ann);
			
			int NbreResultatLabo= Integer.parseInt(result);
			if(maximumResult <NbreResultatLabo) {
				maximumResult=NbreResultatLabo;
			}
		if(NonidentifiedAnnotation.contains(ann)) {
				
				NonIdenfienbreDeResultat=NonIdenfienbreDeResultat+NbreResultatLabo;
				if(maximumResultNonIdentifie <NbreResultatLabo) {
					maximumResultNonIdentifie=NbreResultatLabo;
				}
				
			}
			else {
				nbreDeResultatIdentifie=nbreDeResultatIdentifie+NbreResultatLabo;
				if(maximumResultIdentifie <NbreResultatLabo) {
					maximumResultIdentifie=NbreResultatLabo;
				}
			}
		}
//		
		System.out.println(" nbreDeResultatIdentifie "+nbreDeResultatIdentifie);
		System.out.println(" NonIdenfienbreDeResultat "+NonIdenfienbreDeResultat);
		System.out.println(" maximumResult "+maximumResult);
		System.out.println(" maximumResultIdentifie "+maximumResultIdentifie);
		System.out.println(" maximumResultNonIdentifie "+maximumResultNonIdentifie);
	}
	
	public static Set<String> getTheUsedAnnotation() throws SQLException {
		
		
		Set<String> AnnoationOriginal = new HashSet<String>();
		Set<String> AnnoationFinale = new HashSet<String>();
		
		try{
			InputStream flux=new FileInputStream("./listCodeSynergy.csv"); 
			InputStreamReader lecture=new InputStreamReader(flux,StandardCharsets.ISO_8859_1);
			BufferedReader buff=new BufferedReader(lecture);
			String ligne;
			while ((ligne=buff.readLine())!=null){
				String[]rest=ligne.split(";");
				//System.out.println(rest);
				if(rest.length>1) {
					
					String annot=rest[0];
					if(!annot.equals("CONCEPT_CD")) {
						AnnoationOriginal.add(annot);
					}
				}
			}
				for(String ann: AnnoationOriginal) {
					String annot=ann.replace("SYN|ANA:", "SYN:ANA-");
					
					AnnoationFinale.add(annot);
				}
			
			
			
			buff.close(); 
			}		
			catch (Exception e){
			System.out.println(e.toString());
			}
			System.out.println(AnnoationFinale.size());
			System.out.println(AnnoationOriginal.size());
			return AnnoationFinale;
		
		
	}
	
	public static HashMap<String, String> getTheUsedAnnotationNbreLabo() throws SQLException {
		

		
		
		HashMap<String, String> AnnotationNbreResultat = new HashMap<String, String>();
		
		
		try{
			InputStream flux=new FileInputStream("./listCodeSynergy.csv"); 
			// changer listCodeSynergy par la liste se trouvant dans le document jeannoel.xlx
			InputStreamReader lecture=new InputStreamReader(flux,StandardCharsets.ISO_8859_1);
			BufferedReader buff=new BufferedReader(lecture);
			String ligne;
			while ((ligne=buff.readLine())!=null){
				String[]rest=ligne.split(";");
				//System.out.println(rest);
				if(rest.length>1) {
					
					String annotation=rest[0].replace("SYN|ANA:", "SYN:ANA-");
					String result= rest[1];
					if(!annotation.equals("CONCEPT_CD")) {
						if(!AnnotationNbreResultat.containsKey(annotation)) {
							AnnotationNbreResultat.put(annotation, result);
						}
					}
				}
				else {
					System.out.println("probleme........................");
				}
			}
				
			
			
			
			buff.close(); 
			}		
			catch (Exception e){
			System.out.println(e.toString());
			}
		
		return AnnotationNbreResultat;
		
	}
	
	public static HashMap<String, String> getCodeAndAnotation() throws RepositoryException, QueryEvaluationException, MalformedQueryException{
		String Query=" prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n" + 
				"prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"select distinct ?s ?l\n" + 
				"where {\n" + 
				"  graph<http://erias.org/InterfaceTerminology/TLAB>{\n" + 
				"   ?s ?p ?o\n" + 
				"    }\n" + 
				"       ?s skos:notation ?l\n" + 
				"       }";
		String namespace = "TLABANDLOINC";
		Repository repo = RepositoryFactory.getRepository(namespace);
		RepositoryConnection conn = repo.getConnection();
		 ArrayList<BindingSet> ListeRDFresult= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, Query));
			System.out.println("ok"); 
			HashMap<String, String> codeAndAnnotation= new HashMap<String, String>();
			for(BindingSet e: ListeRDFresult){
				if(!codeAndAnnotation.containsKey(e.getValue("s").stringValue())){
					codeAndAnnotation.put(e.getValue("s").stringValue(), e.getValue("l").stringValue());
				};
				//System.out.println(e.getValue("s").stringValue()+" "+ e.getValue("l").stringValue());
				
			}
			return codeAndAnnotation; 
			
			
	
		
	}

}
