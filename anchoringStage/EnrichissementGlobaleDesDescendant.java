package anchoringStage;

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
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import interactionTripleStore.InteractionBlazgraph;
import preTraitement.RecuperationGraphRDF;
import utilitaries.Couple;

public class EnrichissementGlobaleDesDescendant {
	Set<String> codeAvecAnalytefinal= new HashSet<String>();
	Set<String> codeAvecMilieuBiofinal= new HashSet<String>();
	Set<String> codeAvecGrandeurFinal= new HashSet<String>();
	Set<String> codeAvecTechniqueFinal= new HashSet<String>();
	
	HashMap<String,Set<String>> codeEtAnalytes= new HashMap<String,Set<String>>();
	HashMap<String,Set<String>> codeEtMilieuBio= new HashMap<String,Set<String>>();
	HashMap<String,Set<String>> codeEtGrandeur= new HashMap<String,Set<String>>();
	HashMap<String,Set<String>> codeEtTechnique= new HashMap<String,Set<String>>();
	
	
	
	public EnrichissementGlobaleDesDescendant(RepositoryConnection conn){
		
		String ChoixRequete="CodeEtSesDescendant";
		String URIduGraph="http://erias.org/InterfaceTerminology/TLAB";
	
		//String URIduGraph2="http://erias.org/integration/AnchoringFromWordReduced";
		//String URIduGraph2=	"http://erias.org/integration/AnchoringFromWordNormalTest";
		//String URIduGraph2=	"http://erias.org/integration/AnchoringFromWordReducedTestTOTAL";
		String URIduGraph2=	"http://erias.org/integration/AnchoringFromWordReducedTOTALCORRIGE";
		
		String ChoixRequete2="Ancor";
		
		RecuperationGraphRDF graphCodeEtSesDescendant = new RecuperationGraphRDF(conn, ChoixRequete, URIduGraph);
		//RecuperationGraphRDF graphCode = new RecuperationGraphRDF(conn, "a", URIduGraph);
		RecuperationGraphRDF graphCodeEtSesRelation = new RecuperationGraphRDF(conn, ChoixRequete2, URIduGraph2);
		
	//	Set<String> listecode=graphCode.getRDFliste();
		
		HashMap<String,List<String>> lescodesEtleurdescendant=graphCodeEtSesDescendant.getRDFlisteLibellé();
		ValueFactory vf = conn.getValueFactory();
		Set<Statement> lesNouvellesrelation= new HashSet<Statement>();
		Set<Statement> lesNouvellesrelation2= new HashSet<Statement>();
		String prefixe="http://erias.org/loincTotal#";
		
		String relationAnalyte=prefixe+"has_analyte";
		String relationTechnique=prefixe+"has_method";
		String relationMilieu=prefixe+"has_system";
		String relationGrandeur=prefixe+"has_property";
		for(String sr:graphCodeEtSesRelation.getTripletRDF().keySet()){
			
			Set<String> codeMilieuBiologique=new HashSet<String>();
			Set<String> codeTechnique=new HashSet<String>();
			Set<String> codeAnalyte=new HashSet<String>();
			Set<String> codeGrandeur=new HashSet<String>();
			for(Couple huiu:graphCodeEtSesRelation.getTripletRDF().get(sr)){
				if(huiu.x.equals(relationAnalyte)){
				
					codeAnalyte.add(huiu.y);
					if(!codeEtAnalytes.containsKey(sr)){
						codeEtAnalytes.put(sr, new HashSet<String>());
					}
					codeEtAnalytes.get(sr).add(huiu.y);
				
				}
				 if(huiu.x.equals(relationTechnique)){
					codeTechnique.add(huiu.y);
					if(!codeEtTechnique.containsKey(sr)){
						codeEtTechnique.put(sr, new HashSet<String>());
					}
					codeEtTechnique.get(sr).add(huiu.y);
					
				}
				 if(huiu.x.equals(relationMilieu)){
					codeMilieuBiologique.add(huiu.y);
					if(!codeEtMilieuBio.containsKey(sr)){
						codeEtMilieuBio.put(sr, new HashSet<String>());
					}
					codeEtMilieuBio.get(sr).add(huiu.y);
					
				}
				 if(huiu.x.equals(relationGrandeur)){
					codeGrandeur.add(huiu.y);
					if(!codeEtGrandeur.containsKey(sr)){
						codeEtGrandeur.put(sr, new HashSet<String>());
					}
					codeEtGrandeur.get(sr).add(huiu.y);
				}
				
			}
			
			
			
		}
		
		
		
		
		int i=1;
		for(String codeParent:graphCodeEtSesRelation.getTripletRDF().keySet()){
			if(lescodesEtleurdescendant.containsKey(codeParent)){
				
				for(String codeEnfant :lescodesEtleurdescendant.get(codeParent)){
					for(Couple relationduCodeParent:graphCodeEtSesRelation.getTripletRDF().get(codeParent) ){
						
						Statement hty=vf.createStatement(vf.createURI(codeEnfant), 
								vf.createURI(relationduCodeParent.x),
								vf.createURI(relationduCodeParent.y));
						Statement hty2=vf.createStatement(vf.createURI(codeParent), 
								vf.createURI(relationduCodeParent.x),
								vf.createURI(relationduCodeParent.y));
						if(i<6000000){
						lesNouvellesrelation.add(hty);
						lesNouvellesrelation.add(hty2);
						i++;
						System.out.println(i);
						}
						else{
							lesNouvellesrelation2.add(hty);
							lesNouvellesrelation.add(hty2);
						}
						
						
					}
					
				}
				
				
			}
		}
		
		
		//String graphAncrage=	"http://erias.org/integration/AnchoringFromWordReduced";
		//String graphAncrage=	"http://erias.org/integration/AnchoringFromWordNormalTestEnrichi";
		//String graphAncrage=	"http://erias.org/integration/AnchoringFromWordReducedTestEnrichiTOTAL";
		String graphAncrage=	"http://erias.org/integration/AnchoringFromWordReducedEnrichiTOTALCORRIGE";
		
		URI AncrageURI = conn.getValueFactory().createURI(graphAncrage);
		List<Statement> liste= new ArrayList<Statement>(lesNouvellesrelation);
		System.out.println(liste.size());
		List<Statement> liste2= new ArrayList<Statement>(lesNouvellesrelation2);
		System.out.println(liste2.size());
		
		System.out.println("debut d'insertion");
		try {
			
			conn.add(liste,AncrageURI);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("debut d'insertion");
		try {
			
			conn.add(liste2,AncrageURI);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("fin d'insertion ");
		
		RecuperationGraphRDF graphCodeEtSesRelationFinal = new RecuperationGraphRDF(conn, ChoixRequete2, graphAncrage);
		
		
for(String sr:graphCodeEtSesRelationFinal.getTripletRDF().keySet()){
	
	Set<String> codeMilieuBiologique=new HashSet<String>();
	Set<String> codeTechnique=new HashSet<String>();
	Set<String> codeAnalyte=new HashSet<String>();
	Set<String> codeGrandeur=new HashSet<String>();
	for(Couple huiu:graphCodeEtSesRelationFinal.getTripletRDF().get(sr)){
		if(huiu.x.equals(relationAnalyte)){
			codeAvecAnalytefinal.add(sr);
			
		}
		 if(huiu.x.equals(relationTechnique)){
			codeTechnique.add(huiu.y);
			
			codeAvecTechniqueFinal.add(sr);
			//codeAvecAnalyteMilieuBioTechnique.add(sr);
		}
		 if(huiu.x.equals(relationMilieu)){
			codeMilieuBiologique.add(huiu.y);
			
			codeAvecMilieuBiofinal.add(sr);
			//codeAvecAnalyteetMilieuBioFinal.add(sr);
			//codeAvecAnalyteMilieuBioTechnique.add(sr);
		}
		 if(huiu.x.equals(relationGrandeur)){
			codeGrandeur.add(huiu.y);
		
			codeAvecGrandeurFinal.add(sr);
		}
		
	}
	
	
	
}

System.out.println( "codeEtAnalytes : "+codeEtAnalytes.size());
System.out.println( "codeEtMilieuBio : "+codeEtMilieuBio.size());
System.out.println( "codeEtGrandeur : "+codeEtGrandeur.size());
System.out.println( "codeEtTechnique : "+codeEtTechnique.size());

System.out.println( "codeAvecAnalytefinal : "+codeAvecAnalytefinal.size());
System.out.println( "codeAvecMilieuBiofinal : "+codeAvecMilieuBiofinal.size());
System.out.println( "codeAvecGrandeurFinal : "+codeAvecGrandeurFinal.size());
System.out.println( "codeAvecTechniqueFinal : "+codeAvecTechniqueFinal.size());


		
		
		
		
	}
	public String getQueryAll(String URIduGraph) {
		String query=" SELECT ?s ?p ?o "+
				"where { "
				+ " GRAPH<"+URIduGraph+"> { "
				+ " ?s ?p ?o. "
				+ " } "
				+ " } ";
		return query;
		
	}
	public HashMap<String , Set<Couple>> getResult(RepositoryConnection conn,String query) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		
		HashMap<String , Set<Couple>>  tripletRDf = new HashMap<String, Set<Couple>>();
		
		ArrayList<BindingSet> ListeRDF= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query));
		 for(BindingSet e: ListeRDF){
		Couple relation= new Couple(e.getValue("p").stringValue(), e.getValue("o").stringValue());
		if(!tripletRDf.containsKey(e.getValue("s").stringValue())){
			tripletRDf.put(e.getValue("s").stringValue(), new HashSet<Couple>());
		};
		tripletRDf.get(e.getValue("s").stringValue()).add(relation);
		 }
		//System.out.println(e.getValue("s").stringValue()+" --"+e.getValue("p").stringValue()+"--> "+e.getValue("o").stringValue());
		return tripletRDf;
	}

}
