package dataDrivenFiltering;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import interactionTripleStore.RepositoryFactory;

public class UCUMstructure {

	public static void main(String[] args) throws RepositoryException {
		// TODO Auto-generated method stub
		String namespace = "TLABANDLOINC";
		Repository repo = RepositoryFactory.getRepository(namespace);
		RepositoryConnection conn = repo.getConnection();
		ValueFactory vf = conn.getValueFactory();
		
		Set<UCUMElementcsv> result=Ucumresult();
		Set<Statement> stms=CreateRDFTriplet(result, vf);
		

		String ucumGraph = "http://erias.org/ucum";
		
		URI Ucum = conn.getValueFactory().createURI(ucumGraph);
		List<Statement> liste= new ArrayList<Statement>(stms);
		
		System.out.println(liste.size());

		try {	

			conn.add(liste,Ucum);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("final terminé");
	

	}
	
	public static Set<Statement> CreateRDFTriplet(Set<UCUMElementcsv> results,ValueFactory vf) {
		Set<Statement> result= new HashSet<Statement>();
		String Prefix_rdfs="http://www.w3.org/2000/01/rdf-schema#";
		String prefixe="http://erias.org/UCUM#";
		String skos="http://www.w3.org/2004/02/skos/core#";
		
		for(UCUMElementcsv elt:results) {
			//System.out.println(prefixe+elt.Kind_of_Quantity);
			Statement stm1=vf.createStatement(vf.createURI(prefixe+elt.ConceptID), 
					vf.createURI(skos + "prefLabel"),
					vf.createLiteral(elt.Code));
			result.add(stm1);
			
			Statement stm2=vf.createStatement(vf.createURI(prefixe+elt.ConceptID), 
					vf.createURI(skos + "altLabel"),
					vf.createLiteral(elt.Synonym));
			result.add(stm2);
			
			Statement stm3=vf.createStatement(vf.createURI(prefixe+elt.ConceptID), 
					vf.createURI(Prefix_rdfs + "label"),
					vf.createLiteral(elt.Descriptive_Name));
			result.add(stm3);
			String kind= elt.Kind_of_Quantity.replace(" ", "");
			//System.out.println(kind+"++|++"+elt.Kind_of_Quantity);
			Statement stm4=vf.createStatement(vf.createURI(prefixe+elt.ConceptID), 
					vf.createURI(prefixe+"kind_of_Quantity"),
					vf.createURI(prefixe+kind));
			result.add(stm4);
			Statement stm5=vf.createStatement(vf.createURI(prefixe+kind), 
					vf.createURI(Prefix_rdfs + "label"),
					vf.createLiteral(elt.Kind_of_Quantity));
			result.add(stm5);
			
			Statement stm6=vf.createStatement(vf.createURI(prefixe+elt.ConceptID), 
					vf.createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
					vf.createURI(skos+"Concept"));
			result.add(stm6);
			Statement stm7=vf.createStatement(vf.createURI(prefixe+kind), 
					vf.createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
					vf.createURI(skos+"Concept"));
			result.add(stm7);
			
			
		}
		
		return result;
		
	}
	public static Set<UCUMElementcsv>  Ucumresult() {

		//Set<String> Results= new HashSet<String>();
		Set<UCUMElementcsv> Resultats= new HashSet<UCUMElementcsv>();
		System.out.println("ok");
		try{
			InputStream flux=new FileInputStream("./concepts.tsv"); 
			InputStreamReader lecture=new InputStreamReader(flux,StandardCharsets.ISO_8859_1);
			BufferedReader buff=new BufferedReader(lecture);
			String ligne;
			System.out.println("ok");
			while ((ligne=buff.readLine())!=null){
				//System.out.println(ligne);
				String[]rest=ligne.split("\t");
				String code=rest[0];
				String descriptive_Name=rest[1];
				String synonym=rest[5];
				String kind_of_Quantity=rest[7];
				String conceptID=rest[9];
				if(!code.equals("Code")) {
					UCUMElementcsv elemnt = new UCUMElementcsv(code.trim(), descriptive_Name.trim(), synonym.trim(), kind_of_Quantity.trim(), conceptID);
					Resultats.add(elemnt);
				}			
				//System.out.println(rest[0]+" | "+rest[1]+" | "+rest[5]+" | "+rest[7]+" | "+rest[9]);
				//System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
				

			
			}
			
			buff.close(); 
			}		
			catch (Exception e){
			System.out.println(e.toString());
			}
	return Resultats;
	
	}

}
