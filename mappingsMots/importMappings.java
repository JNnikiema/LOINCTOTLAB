package mappingsMots;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFParseException;

import interactionTripleStore.ChargementFichierOntologie;
import interactionTripleStore.InteractionBlazgraph;
import preTraitement.RecuperationGraphRDF;
import utilitaries.Couple;

public class importMappings {
	public importMappings(String namespace) throws RepositoryException, RDFParseException, IOException{
		Set<Couple> lesMappingsCommun=new HashSet<Couple>();
		HashMap<String, Set<String>> resulatMapp=new HashMap<String, Set<String>>();
		
		String graph1=	"http://erias.org/integration/MappingServoMap/Mapping1TOTAL";
		String graph2=	"http://erias.org/integration/MappingServoMap/Mapping2TOTAL";
		RepositoryConnection connnex2= InteractionBlazgraph.connex(namespace);
		ChargementFichierOntologie.ChargementFichier(namespace, "./MAPPINGLOINCTOTLAB.rdf", graph1, "application/rdf+xml"); 
		ChargementFichierOntologie.ChargementFichier(namespace, "./MAPPINGTLABTOLOINC.rdf", graph2, "application/rdf+xml"); 
			
		RecuperationGraphRDF getMapping1= new RecuperationGraphRDF(connnex2, "JN", graph1);
		RecuperationGraphRDF getMapping2= new RecuperationGraphRDF(connnex2, "JN", graph2);
		
		for(String sr:getMapping1.getTripletRDF().keySet()){
			lesMappingsCommun.addAll(getMapping1.getTripletRDF().get(sr));
			for(Couple huiu:getMapping1.getTripletRDF().get(sr)){
				if(!resulatMapp.containsKey(huiu.y)){
					resulatMapp.put(huiu.x, new HashSet<String>());
				}
				resulatMapp.get(huiu.x).add(huiu.y);
				System.out.println("huiu x "+huiu.x);
			}
		}
		for(String sr:getMapping2.getTripletRDF().keySet()){
			for(Couple kio:getMapping2.getTripletRDF().get(sr)){
				Couple ju= new Couple(kio.y,kio.x);
				//System.out.println("triplet "+kio.y);
				lesMappingsCommun.add(ju);
				if(!resulatMapp.containsKey(kio.y)){
					resulatMapp.put(kio.y, new HashSet<String>());
				}
				resulatMapp.get(kio.y).add(kio.x);
				//System.out.println(" kio "+kio.y);
			}
		}
		String prefixee="http://erias.org/integration/TLAB/model#";
		ValueFactory vf = connnex2.getValueFactory();
		Set<Statement> lesSatement= new HashSet<Statement>();
		Set<String> codeterm= new HashSet<String>();
		Set<String> codeLoinc= new HashSet<String>();
		for(Couple Mappingd:lesMappingsCommun){
			Statement stm = vf.createStatement( vf.createURI(Mappingd.x),
					vf.createURI(prefixee+"isMappedToTOTALCORRIGE"),
					vf.createURI(Mappingd.y));
			lesSatement.add(stm);
			codeterm.add(Mappingd.x);
			codeLoinc.add(Mappingd.y);
			
			//System.out.println(Mappingd.x);
		}
		System.out.println("liste des statement : "+lesSatement.size());
		System.out.println("les mappings en commun "+lesMappingsCommun.size());
		System.out.println("code loinc "+codeLoinc.size());
		System.out.println("code terminologie "+codeterm.size());
		
		String graphModel= "http://erias.org/integration/TLAB/model#";
		
		
		
		URI Model_URI = connnex2.getValueFactory().createURI(graphModel);
		List<Statement> list= new ArrayList<Statement>(lesSatement);
		System.out.println(list.size());
	//	System.out.println(connnex2.getNamespaces().toString());
		connnex2.close();
		System.out.println("debut d'insertion");
		try {
			
			connnex2.add(list, Model_URI);
			
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("fin d'insertion");
		
		
		

	}
	
	
}
