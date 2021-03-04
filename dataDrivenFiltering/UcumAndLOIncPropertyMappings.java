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

public class UcumAndLOIncPropertyMappings {

	public static void main(String[] args) throws RepositoryException {
		// TODO Auto-generated method stub
		String namespace = "TLABANDLOINC";
		Repository repo = RepositoryFactory.getRepository(namespace);
		RepositoryConnection conn = repo.getConnection();
		ValueFactory vf = conn.getValueFactory();
		
		Set<Statement> result=GetUcumMapping(vf);
		String mappingGraph = "http://erias.org/UCUM/MappingsPropertyTotal";
		//String mappingGraph = "http://erias.org/UCUM/MappingsLoinc#";
		
		URI mappingGraphUcum = conn.getValueFactory().createURI(mappingGraph);
		List<Statement> liste= new ArrayList<Statement>(result);
		
		System.out.println(liste.size());

		try {	

			conn.add(liste,mappingGraphUcum);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("final terminé");

	}
	
	public static Set<Statement>  GetUcumMapping(ValueFactory vf) {

		Set<Statement> result= new HashSet<Statement>();
		
		String prefixe="http://erias.org/UCUM#";
		System.out.println("ok");
		try{
			InputStream flux=new FileInputStream("./MappingProperty.txt"); 
			InputStreamReader lecture=new InputStreamReader(flux,StandardCharsets.UTF_8);
			BufferedReader buff=new BufferedReader(lecture);
			String ligne;
			System.out.println("ok");
			while ((ligne=buff.readLine())!=null){
				//System.out.println(ligne);
				String[]rest=ligne.split(";");
				if(rest.length>1) {
				String PropertyLOIN=rest[0];
				String propertyUCU=rest[1];
				String PropertyLOINC=PropertyLOIN.substring(1, PropertyLOIN.length()-1);
				String propertyUCUM=propertyUCU.substring(1, propertyUCU.length()-1);
				Statement stm=vf.createStatement(vf.createURI(propertyUCUM), 
						vf.createURI(prefixe + "mappings"),
						vf.createURI(PropertyLOINC));
				result.add(stm);
							
				//System.out.println(PropertyLOINC.substring(1, PropertyLOINC.length()-1)+" | "+propertyUCUM.substring(1, propertyUCUM.length()-1));
				System.out.println(PropertyLOINC+" | "+propertyUCUM);
				}
				
				//System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
				

			
			}
			
			buff.close(); 
			}		
			catch (Exception e){
			System.out.println(e.toString());
			}
	return result;
	
	}

}
