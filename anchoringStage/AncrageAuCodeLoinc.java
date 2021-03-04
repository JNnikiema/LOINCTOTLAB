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
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import preTraitement.RecuperationGraphRDF;
import utilitaries.Couple;


public class AncrageAuCodeLoinc {
	
	HashMap<String, Set<String>>LesAnalytesEtlecodeLoinc= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>>LesMilieuBiologiquesEtlecodeLoinc= new HashMap<String, Set<String>>();
	HashMap<String, Set<String>>LesTechniquesEtlecodeLoinc= new HashMap<String, Set<String>>();
	
	
	public AncrageAuCodeLoinc(RepositoryConnection conn) throws QueryEvaluationException, RepositoryException, MalformedQueryException{
		Set<Statement> lesSatementModel= new HashSet<Statement>();
		Set<Statement> lesSatementModel2= new HashSet<Statement>();
		
		String prefixe="http://erias.org/loincTotal#";
		
		String relationAnalyte=prefixe+"has_analyte";
		String relationTechnique=prefixe+"has_method";
		String relationMilieu=prefixe+"has_system";
		String relationComposant=prefixe+"has_component";
		String relationGrandeur=prefixe+"has_property";
		
		String query1=" Select ?s ?g"
				+ " where{ "
				+ " GRAPH<http://erias.org/FrenchLoinc>{ "
				+" ?s 	<"+relationComposant+">  ?k."
				+ " ?k <"+relationAnalyte+">  ?g. "
				+ " } "
				+ " } ";
		String query2=" Select ?s ?o "
				+ " where{ "
				+ " GRAPH<http://erias.org/FrenchLoinc>{ "
				+ " ?s  <"+relationTechnique+"> ?o. "
				+ " } "
				+ " } ";
		String query3=" Select ?s ?p "
				+ " where{ "
				+ " GRAPH<http://erias.org/FrenchLoinc>{ "
				+ " ?s <"+relationMilieu+"> ?p. "
				+ " } "
				+ " } ";
		
		
		
		TupleQueryResult analytes = conn.prepareTupleQuery(QueryLanguage.SPARQL, query1).evaluate();
		TupleQueryResult techniques = conn.prepareTupleQuery(QueryLanguage.SPARQL, query2).evaluate();
		TupleQueryResult MilieuBiologique = conn.prepareTupleQuery(QueryLanguage.SPARQL, query3).evaluate();
		//Set<TripletLoincRDF> listeResultat= new HashSet<TripletLoincRDF>();
		//HashMap<TripletLoincRDF, Set<String>> RelationCodeLoincCorrespondant= new HashMap<TripletLoincRDF, Set<String>>();
	//	HashMap<Couple, Set<String>> RelationCoupleCodeLoincCorrespondant= new HashMap<Couple, Set<String>>();
		
		while (analytes.hasNext()) {
			BindingSet bs = analytes.next();
			
			
			if(!LesAnalytesEtlecodeLoinc.containsKey(bs.getValue("g").stringValue())){
				LesAnalytesEtlecodeLoinc.put(bs.getValue("g").stringValue(), new HashSet<String>());
			}
			LesAnalytesEtlecodeLoinc.get(bs.getValue("g").stringValue()).add(bs.getValue("s").stringValue());
		}	
			
		while (MilieuBiologique.hasNext()) {
			BindingSet bs = MilieuBiologique.next();
			if(!LesMilieuBiologiquesEtlecodeLoinc.containsKey(bs.getValue("p").stringValue())){
				LesMilieuBiologiquesEtlecodeLoinc.put(bs.getValue("p").stringValue(), new HashSet<String>());
			}
			LesMilieuBiologiquesEtlecodeLoinc.get(bs.getValue("p").stringValue()).add(bs.getValue("s").stringValue());
		}
		while (techniques.hasNext()) {
			BindingSet bs = techniques.next();
			if(!LesTechniquesEtlecodeLoinc.containsKey(bs.getValue("o").stringValue())){
				LesTechniquesEtlecodeLoinc.put(bs.getValue("o").stringValue(), new HashSet<String>());
			}
			LesTechniquesEtlecodeLoinc.get(bs.getValue("o").stringValue()).add(bs.getValue("s").stringValue());
			
		//	listeResultat.add(jn);
		}
		
		//String graphCode="http://erias.org/integration/AnchoringFromWordReduced";
		//String graphCode=	"http://erias.org/integration/AnchoringFromWordNormalTestEnrichi";
		//String graphCode=	"http://erias.org/integration/AnchoringFromWordReducedTestEnrichiTOTAL";
		String graphCode=	"http://erias.org/integration/AnchoringFromWordReducedEnrichiTOTALCORRIGE";
		String graphCode2=	"http://erias.org/integration/AnchoringFromWordReducedTOTALCORRIGE";
		
		ValueFactory vf = conn.getValueFactory();
		RecuperationGraphRDF graphAncrage= new RecuperationGraphRDF(conn, "Ancor", graphCode);
		RecuperationGraphRDF graphAncrage2= new RecuperationGraphRDF(conn, "Ancor", graphCode2);
		HashMap<String, Set<Couple>> anchorAtt= new HashMap<String, Set<Couple>>();
		for(String sr:graphAncrage2.getTripletRDF().keySet()) {
			if(!anchorAtt.containsKey(sr)) {
				anchorAtt.put(sr, new HashSet<Couple>());
			}
			anchorAtt.get(sr).addAll(graphAncrage2.getTripletRDF().get(sr));
		}
		for(String sr:graphAncrage.getTripletRDF().keySet()) {
			if(!anchorAtt.containsKey(sr)) {
				anchorAtt.put(sr, new HashSet<Couple>());
			}
			anchorAtt.get(sr).addAll(graphAncrage.getTripletRDF().get(sr));
		}
		
		
		int i=1;
		for(String sr:anchorAtt.keySet()){
			Set<String> CodeLoincMappe=new HashSet<String>();
			Set<String> codeMilieuBiologique=new HashSet<String>();
			Set<String> codeTechnique=new HashSet<String>();
			Set<String> codeAnalyte=new HashSet<String>();
			Set<String> codeGrandeur=new HashSet<String>();
			for(Couple huiu:anchorAtt.get(sr)){
				if(huiu.x.equals(relationAnalyte)){
					codeAnalyte.add(huiu.y);
				}
				else if(huiu.x.equals(relationTechnique)){
					codeTechnique.add(huiu.y);
				}
				else if(huiu.x.equals(relationMilieu)){
					codeMilieuBiologique.add(huiu.y);
				}
				else if(huiu.x.equals(relationGrandeur)){
					codeGrandeur.add(huiu.y);
				}
				
			}
			for(String codeA : codeAnalyte){
				if(LesAnalytesEtlecodeLoinc.containsKey(codeA)){
					for(String codeLoinc:LesAnalytesEtlecodeLoinc.get(codeA)){
						Statement stm2 = vf.createStatement( vf.createURI(sr),
								vf.createURI("http://erias.org/integration/TLAB/model#is_anchor_to"),
								vf.createURI(codeLoinc));
						if(i<8000000){
							lesSatementModel.add(stm2);
							i++;
						}
						else{
							lesSatementModel2.add(stm2);
						}
						
					}
				}
			}
		}
		//String graphAncrageLoinc="http://erias.org/integration/AnchoringCandidatsTOTAL";
		String graphAncrageLoinc="http://erias.org/integration/AnchoringCandidatsTOTALCORRIGE";
		//String graphAncrageLoinc="http://erias.org/integration/AnchoringCandidatsTotalMots";
		
		URI Anchor_URI = conn.getValueFactory().createURI(graphAncrageLoinc);
		List<Statement> liste= new ArrayList<Statement>(lesSatementModel);
		List<Statement> liste2= new ArrayList<Statement>(lesSatementModel2);
		System.out.println("debut d'insertion");
		System.out.println(liste.size());
		System.out.println(liste2.size());
		System.out.println("lancer ");
		try {
			conn.add(liste,Anchor_URI);
			//conn.add(liste2,Anchor_URI);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("premiere insertion finie ");
		try {
			//conn.add(liste,Anchor_URI);
			conn.add(liste2,Anchor_URI);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("fini l'ancrage des candidats");
		
		
	}

}
