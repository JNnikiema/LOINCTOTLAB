package preTraitement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.QueryResults;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import interactionTripleStore.InteractionBlazgraph;
import utilitaries.Couple;


/**
 * 
 * @author erias
 *cette classe permet d'exécuter des requêtes sparql sur la base de données graph 
 *et d'en récupérer les resultats si necessaire
 */
public class RecuperationGraphRDF {
	private Set<String> codeRdf = new HashSet<String>();
	private HashMap<String , List<String>> codeIdentifiant = new HashMap<String, List<String>>();
	private HashMap<String, Set<Couple>> tripletRDf= new HashMap<>();
	private Set<Couple> coupleRelation = new HashSet<Couple>();
	/**
	 * différentes requetes permettant d'avoir un resultat sur la base de donnée blazgraph
	 * @param conn
	 * @param ChoixRequete
	 * @param URIduGraph
	 */
	public RecuperationGraphRDF(RepositoryConnection conn, String ChoixRequete, String URIduGraph){
		String query= new String();
		if(ChoixRequete.equals("a")){
			 query="prefix skos:<http://www.w3.org/2004/02/skos/core#>"+
				"SELECT ?s ?p ?o WHERE {"+
			  "GRAPH <"+URIduGraph+"> {"+
			    "?s ?p ?o."+
			  "}"+
			        "}";
			
		}
		else if(ChoixRequete.equals("anchorRest")){
			 query="prefix skos:<http://www.w3.org/2004/02/skos/core#>"+
				"SELECT ?s ?p ?o WHERE {"+
			  "GRAPH <"+URIduGraph+"> {"+
			    "?s ?p ?o."+
			  "}"+
			        "}";
			
		}
		else if(ChoixRequete.equals("b")){
			query="prefix skos: <http://www.w3.org/2004/02/skos/core#>"+
"SELECT DISTINCT ?s ?o WHERE {"+
"GRAPH <"+URIduGraph+"> {"+
    "?s skos:prefLabel ?o."+
  "}"+
       "}";
		}
		else if(ChoixRequete.equals("c")){
			query="prefix skos: <http://www.w3.org/2004/02/skos/core#>"+
					"prefix syn: <http://chu-bordeaux.fr/synergie#>"+
					"SELECT DISTINCT ?s ?o WHERE {"+
					"GRAPH <"+URIduGraph+"> {"+
					"?s a skos:ConceptScheme."+
					"?s skos:prefLabel ?o."+
					"filter not exists{"+
			      "?s skos:broader syn:syn-codeorph."+
			         
			          "}"+
					"}"+
       "}"
       ;
			
		}
		else if(ChoixRequete.equals("SansOrphelin")){
			query=" prefix skos: <http://www.w3.org/2004/02/skos/core#> "+
					" prefix syn: <http://chu-bordeaux.fr/synergie#> "+
					" SELECT ?s ?p ?o WHERE { "+
					" GRAPH <"+URIduGraph+"> { "+
					" ?s ?p ?o. "+
					" filter not exists{ "+
			      " ?s skos:broader syn:syn-codeorph. "+
			         
			        "  } "+
					" } "+
					" }";
		}
		else if(ChoixRequete.equals("CodeEtSesDescendant")){
			query="prefix skos: <http://www.w3.org/2004/02/skos/core#> "+
			"	select ?s ?p ?o "+
			"	where{ "+
			"	  graph<http://erias.org/InterfaceTerminology/TLAB>{ "+
			" 	    ?o skos:broader+ ?s "+
				"   } "+
				 " } ";
		}
		else if(ChoixRequete.equals("CodeEtSesAscendant")){
			query="prefix skos: <http://www.w3.org/2004/02/skos/core#> "+
			"	select ?s ?p ?o "+
			"	where{ "+
			"	  graph<http://erias.org/InterfaceTerminology/TLAB>{ "+
			" 	    ?s skos:broader+ ?o "+
				"   } "+
				 " } ";
		}
		else if(ChoixRequete.equals("d")){
			query="prefix skos: <http://www.w3.org/2004/02/skos/core#>"+
					"prefix syn: <http://chu-bordeaux.fr/synergie#>"+
					"SELECT (<http://chu-bordeaux.fr/synergie#syn-9acpn> as ?s) ?o WHERE {"+
					"GRAPH <"+URIduGraph+"> {"+
					"<http://chu-bordeaux.fr/synergie#syn-9acpn> skos:prefLabel ?o."+
					
					
					"}"+
       "}";
			
		}
		else if(ChoixRequete.equals("Modele")){
			query="prefix lnc:<http://erias.org/integration/ontologySupport/loincCanada#> "+
					" prefix skos: <http://www.w3.org/2004/02/skos/core#> "+
					" prefix MOD:<http://erias.org/integration/terminologieLocale/model#> "+
					" prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> "+
					" select ?s ?p ?o "+
					" where{ "+ 
					" ?s a MOD:AnalyseBiologique. "+
					" ?M1 a MOD:Mot1. "+
					" ?s <http://erias.org/integration/terminologieLocale/relation#:ApourMot> ?M1. "+
					" ?M1 MOD:EstMappeA ?M2. "+
					"?M2 a MOD:Mot2. "+
					" ?o lnc:ApourMot ?M2. "+
					" ?g ?p ?o."
					+ "} ";
		}
		else if(ChoixRequete.equals("Modele2")){
			query="prefix lnc:<http://erias.org/integration/ontologySupport/loincCanada#> "+
					" prefix skos: <http://www.w3.org/2004/02/skos/core#> "+
					" prefix MOD:<http://erias.org/integration/terminologieLocale/model#> "+
					" prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> "+
					" select ?s ?p ?o "+
					" where{ "+ 
					" ?s a MOD:AnalyseBiologique. "+
					" ?M1 a MOD:Mot1. "+
					" ?s <http://erias.org/integration/terminologieLocale/relation#:ApourMot> ?M1. "+
					" ?M1 MOD:EstMappeA ?p. "+
					"?p a MOD:Mot2. "+
					" ?o lnc:ApourMot ?p. "
					+ "} ";
		}
		else if(ChoixRequete.equals("Modele3")){
			query=" prefix lnc:<http://erias.org/integration/ontologySupport/loincCanada#>  "+
					"  prefix skos: <http://www.w3.org/2004/02/skos/core#>   "+
					"  prefix MOD:<http://erias.org/integration/terminologieLocale/model#>   "+
					"  prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#>   "+
					"  select distinct ?p ?o  "+
					"  where{   "+
					"  ?s a MOD:AnalyseBiologique.   "+
					"  ?M1 a MOD:Mot1.   "+
					"  ?s <http://erias.org/integration/terminologieLocale/relation#:ApourMot> ?M1.   "+
					"  ?M1 MOD:EstMappeA ?M2.   "+
					" ?M2 a MOD:Mot2.   "+
					"  ?o lnc:ApourMot ?M2.   "+
					"   {  "+
					"      ?g a MOD:AnalyseBiologique.  "+
					"     ?g ?p ?o  "+
					"     }  "+
					"   union{  "+
					"     ?g a MOD:AnalyseBiologique.  "+
					"    ?g ?j ?h.  "+
					"       ?h ?p ?o  "+
					"    }  "+
					" }   ";
		}
		else if(ChoixRequete.equals("VisiteLoincGraph")){
			query=URIduGraph;
			
		}
		else if(ChoixRequete.equals("Ancor")){
			query=" SELECT ?s ?p ?o "+
					"where { "
					+ " GRAPH<"+URIduGraph+"> { "
					+ " ?s ?p ?o. "
					+ " } "
					+ " } ";
		}
		else if(ChoixRequete.equals("Ancor2")){
			query="SELECT ?s ?p ?o "
					+" where {  "
					+"  GRAPH<http://erias.org/integration/AnchoringFiltrageChapitre> {  "
					+"  ?o ?j ?p.  "
					+"  }  "
					+"       ?o <http://erias.org/integration/ontologySupport/loincCanada#ApourMilieu> ?s. "
					+"       ?p <http://erias.org/integration/ontologySupport/loincCanada#ApourMilieu>  ?s "
					+"  }  ";
		}
		else if(ChoixRequete.equals("Ancor2bis")){
			query="SELECT ?s ?p ?o "
					+" where {  "
					+"  GRAPH<"+URIduGraph+"> {  "
					+"  ?o ?j ?p.  "
					+"  }  "
					+"       ?o <http://erias.org/integration/ontologySupport/loincCanada#ApourTechnique> ?s. "
					+"       ?p <http://erias.org/integration/ontologySupport/loincCanada#ApourTechnique>  ?s "
					+"  }  ";
		}
		
		
		
		else if(ChoixRequete.equals("Ancor3")){
			query=" SELECT ?s  "
					+"  where { "
					+"   GRAPH<http://erias.org/integration/AnchoringFiltrageChapitre> { "
					+"   ?s ?p ?j. "
					+"   } "
				    +"        ?s <http://erias.org/integration/ontologySupport/loincCanada#ApourMilieu> ?o."
				    +"   } ";
		}
		else if(ChoixRequete.equals("Ancor3bis")){
			query=" SELECT ?s  "
					+"  where { "
					+"   GRAPH<"+URIduGraph+"> { "
					+"   ?s ?p ?j. "
					+"   } "
				    +"        ?s <http://erias.org/integration/ontologySupport/loincCanada#ApourTechnique> ?o."
				    +"   } ";
			
		}
		else if(ChoixRequete.equals("Ancor4")){
			query=" SELECT ?s ?p ?o "
					+"  where { "
					+"   GRAPH<"+URIduGraph+"> { "
					+"   ?s ?p ?o. "
					+"   } "
					+"   } ";
		}
		
		else if(ChoixRequete.equals("hierarchie")){
			query="prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
+"  SELECT distinct ?s ?p ?o "
	+" where { "
	+"  GRAPH<http://erias.org/integration/AnchoringCandidats/newHierarchy> { "
	+" 	 ?s <http://erias.org/integration/model#APourNouvelleHierarchie> ?o. "
	+"    } "
    +"    OPTIONAL { {\n" + 
    "  graph<http://erias.org/integration/AnchoringFromWordReducedEnrichiTOTALCORRIGE>{\n" + 
    "    ?s <http://erias.org/loincTotal#has_class> ?p.\n" + 
    "    \n" + 
    "       \n" + 
    "  	}\n" + 
    "       \n" + 
    "       }\n" + 
    "  union{\n" + 
    "    graph<http://erias.org/integration/AnchoringFromWordReducedTOTALCORRIGE>{\n" + 
    "    ?s <http://erias.org/loincTotal#has_class> ?p.\n" + 
    "  	}\n" + 
    "           } } "
    +" } ";
		}
		else if(ChoixRequete.equals("hierarchie2")){
			query="prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> \n" + 
					"  SELECT distinct ?s ?p ?o \n" + 
					"  	where { \n" + 
					"  	 GRAPH<http://erias.org/FrenchLoinc> { \n" + 
					"  		  ?s <http://erias.org/loincTotal#has_class> ?o.   \n" + 
					"     } \n" + 
					"           \n" + 
					"  }  ";
		}
		else if(ChoixRequete.equals("e")){

			 query="prefix skos: <http://www.w3.org/2004/02/skos/core#>"+
					 "prefix owl:<http://www.w3.org/2002/07/owl#>"+
						"prefix xsd:<http://www.w3.org/2001/XMLSchema#>"+
						 "prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
				"SELECT ?s ?p ?o WHERE {"+
			  "GRAPH <http://erias.org/integration/LNC-fr/> {"+
			    "?s a owl:Class."+
			  "}"+
			        "}";
			
		
		}
		else if(ChoixRequete.equals("f")){
			query="prefix skos: <http://www.w3.org/2004/02/skos/core#>"+
					 "prefix owl:<http://www.w3.org/2002/07/owl#>"+
						"prefix xsd:<http://www.w3.org/2001/XMLSchema#>"+
						 "prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
				"SELECT ?s ?p ?o WHERE {"+
			  "GRAPH <http://erias.org/integration/LNC-fr/> {"+
			    "?s a owl:Class. "+
			  "FILTER NOT EXISTS {?s rdfs:label ?o}"+
			  "}"+
			        "}";
			
		}
		else if(ChoixRequete.equals("g")){
			query="prefix skos: <http://www.w3.org/2004/02/skos/core#> "+
				"prefix owl:<http://www.w3.org/2002/07/owl#> "+
				"	prefix xsd:<http://www.w3.org/2001/XMLSchema#> "+
				"	prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> "+
				"	SELECT ?s ?p ?o WHERE { "+
				"	 GRAPH <http://erias.org/integration/LNC-fr/> { "+
					"	?s a owl:Class. "+
					  "FILTER EXISTS{ "+
					   " ?s ?p ?x. "+
					   "?x a owl:Class. "+
					  "?p a owl:ObjectProperty "+
					  "} "+
					        "} "+
							"	        } ";
					
			
		}
		else if(ChoixRequete.equals("h")){
			
			query="prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
				"prefix owl:<http://www.w3.org/2002/07/owl#> "+
				"prefix xsd:<http://www.w3.org/2001/XMLSchema#> "+
				"prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> "+
				"prefix skos: <http://www.w3.org/2004/02/skos/core#> "+
					"select ?s ?p ?o "+
				"where "+
					"{  graph <http://erias.org/integration/LNC-fr/>{ "+
				      "?s ?p ?o. "+
				      "?s a owl:Class. "+
				      "?p a owl:ObjectProperty. "+
				      "?o a owl:Class "+
				      "} "+
				    " } ";
			//
		}
		else if(ChoixRequete.equals("i")){
			query="prefix skos: <http://www.w3.org/2004/02/skos/core#>  "+
				"prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#>  "+
				"SELECT DISTINCT ?s ?p ?o WHERE {  "+
				"GRAPH <http://erias.org/integration/LNC-fr/> {  "+
				 "  ?s rdfs:label ?o.  "+
				  "}"+
				   "    } ";
			//
		}
		else if(ChoixRequete.equals("j")){
			query="prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
				"prefix owl:<http://www.w3.org/2002/07/owl#> "+
				"prefix xsd:<http://www.w3.org/2001/XMLSchema#> "+
				"prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> "+
				"prefix skos: <http://www.w3.org/2004/02/skos/core#> "+
					" "+
				"select ?s ?p ?o "+
				"where "+
				"	{  graph <http://erias.org/integration/LNC-fr/>{ "+
				 "  "+   
				    "  ?s a owl:Class. "+
				     " ?s rdfs:subClassOf ?o "+
				      " "+
				      "} "+
				    " "+
				    "} ";
			//
		}
		else if(ChoixRequete.equals("N")){
			query="prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>	"+
					"prefix owl:<http://www.w3.org/2002/07/owl#>	"+
					"prefix xsd:<http://www.w3.org/2001/XMLSchema#>	"+
					"prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#>	"+
					"prefix skos: <http://www.w3.org/2004/02/skos/core#>	"+
					" "+
					"select ?s ?p ?o	"+
					"where	"+
					"{  graph <"+URIduGraph+">{	"+
					"?s ?p ?o.	"+
      "?s a skos:ConceptScheme.	"+
     " "+      
      "}	"+
    " "+
    "}  ";
					
					//
			
		}
		else if(ChoixRequete.equals("M")){
			query="prefix skos: <http://www.w3.org/2004/02/skos/core#> "+
					"prefix owl:<http://www.w3.org/2002/07/owl#> "+
					"prefix xsd:<http://www.w3.org/2001/XMLSchema#> "+
					"prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> "+
				"SELECT ?s ?p ?o WHERE { "+
			  "GRAPH <http://erias.org/integration/LNC-fr/> { "+
			 	"?s a owl:Class.  "+
			  " "+
                "?s rdfs:label \"CodesansLibelle\"  "+
			 " } "+
			 " }";
		}
		else if(ChoixRequete.equals("J")){
			query="prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>	"+
					"prefix owl:<http://www.w3.org/2002/07/owl#>	"+
					"prefix xsd:<http://www.w3.org/2001/XMLSchema#>	"+
					"prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#>	"+
					"prefix skos: <http://www.w3.org/2004/02/skos/core#>	"+
					" "+
					"select ?s ?p ?o	"+
					"where	"+
					"{  graph <"+URIduGraph+">{	"+
					"?s ?p ?o.	"+
      "?s a owl:Class.	"+
     " "+      
      "}	"+
    " "+
    "}  ";
		}
		else if(ChoixRequete.equals("JN")){
			query="prefix xmlns:<http://knowledgeweb.semanticweb.org/heterogeneity/alignment> "+
					" SELECT  ?s ?p ?o WHERE {  "+
				" GRAPH <"+URIduGraph+"> {  "+
				"	?s xmlns:entity1 ?o."+
				"			?s xmlns:entity2 ?p "+
				"  }"+
				     " } ";
		}
		else if(ChoixRequete.equals("JD")){
			query="prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>	 "+
					"prefix owl:<http://www.w3.org/2002/07/owl#>	 "+
					"prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#>	 "+
					"select ?s ?p ?o	 "+
					"where	 "+
					"{  graph <"+URIduGraph+">{  "+
     				"		 ?s a 	owl:Class.  "+
                     "         ?s rdfs:label ?o  "+
                     "}	 "+
                     "} ";
		}
		else if(ChoixRequete.equals("graphLibelleCorrige")){
			query="prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>	 "+
					"prefix owl:<http://www.w3.org/2002/07/owl#>	 "+
					"prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#>	 "+
					"select ?s ?p ?o	 "+
					"where	 "+
					"{  graph <"+URIduGraph+">{  "+
     			
                     "         ?s rdfs:label ?o  "+
                     "}	 "+
                     "} ";
		}
		else if(ChoixRequete.equals("JDK")){
			query="prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>	 "+
				"prefix owl:<http://www.w3.org/2002/07/owl#>	 "+
				"prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#>	 "+
				"prefix lnc:<http://erias.org/ontology/LoincCorrige-PanCanadian#> "+
				"prefix syn:<http://chu-bordeaux.fr/synergie#> "+
				"prefix xmlns:<http://knowledgeweb.semanticweb.org/heterogeneity/alignment> "+
					"select  ?s ?p ?o  "+
				"where	 "+
				"{ "+
                 "   graph <http://erias.org/ontology/terminologieLocalePretraiteFinal#>{	 "+
				"  ?o syn:ApourMot ?s. "+
				"} "
				+ " "
				+ "}";
			
		}
		else if(ChoixRequete.equals("JDKL")){
			query="prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>	 "+
				"prefix owl:<http://www.w3.org/2002/07/owl#>	 	 "+
				"prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#>		 "+ 
				"prefix lnc:<http://erias.org/ontology/LoincCorrige-PanCanadian#> 	 "+
				"prefix syn:<http://chu-bordeaux.fr/synergie#> 	 "+
				"prefix xmlns:<http://knowledgeweb.semanticweb.org/heterogeneity/alignment> 	 "+
				"select  ?s ?p ?o  	 "+
				"where	 	 "+
				"{ 	 "+
                "   graph <http://erias.org/ontology/LoincCorrige-PanCanadian#>{	 	 "+
				"  ?o lnc:ApourMot ?s. 	 "+
				"} 	 "+
                     "    }" ;
			
			
		}
		
		 try {
			 System.out.println("système de recuperation du RDF enclanché");
			 System.out.println(ChoixRequete+" "+query);
//			 List<BindingSet>ListeRDF= new ArrayList<BindingSet>();
//				
//			 if(ChoixRequete.equals("Modele2")||ChoixRequete.equals("Modele")){
//				System.out.println("requpete a exécuté sur place");
//				 TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, query);
//					
//				  TupleQueryResult t = tupleQuery.evaluate();
//			
//				  ListeRDF = QueryResults.asList(t);
//				  System.out.println("fini");
//			 }
//			 else{
				 ArrayList<BindingSet> ListeRDF= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query));
					
			 //}
			 for(BindingSet e: ListeRDF){
				if(ChoixRequete.equals("a")){
					codeRdf.add(e.getValue("s").stringValue());
					//System.out.println(e.getValue("o").stringValue());
					
				}
				else if(ChoixRequete.equals("Ancor3bis")||ChoixRequete.equals("Ancor3")||ChoixRequete.equals("VisiteLoincGraph")){
					codeRdf.add(e.getValue("s").stringValue());
				}
				else if(ChoixRequete.equals("Ancor4")||ChoixRequete.equals("anchorRest")||ChoixRequete.equals("b")||ChoixRequete.equals("c")||ChoixRequete.equals("i")||ChoixRequete.equals("j")||ChoixRequete.equals("JD")||ChoixRequete.equals("JDK")||ChoixRequete.equals("JDKL")||ChoixRequete.equals("JDK")||ChoixRequete.equals("graphLibelleCorrige")||ChoixRequete.equals("CodeEtSesDescendant")||ChoixRequete.equals("CodeEtSesAscendant")){
					if(!codeIdentifiant.containsKey(e.getValue("s").stringValue())){
						codeIdentifiant.put(e.getValue("s").stringValue(), new ArrayList<String>());
					};
					codeIdentifiant.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());
				//System.out.println(e.getValue("s").stringValue());
				}
				else if(ChoixRequete.equals("e")||ChoixRequete.equals("f")||ChoixRequete.equals("g")||ChoixRequete.equals("M")){
					codeRdf.add(e.getValue("s").stringValue());
				}
				else if(ChoixRequete.equals("Ancor2bis")||ChoixRequete.equals("Ancor2")||ChoixRequete.equals("Ancor")||ChoixRequete.equals("Modele")||ChoixRequete.equals("Modele2")||ChoixRequete.equals("h")||ChoixRequete.equals("N")||ChoixRequete.equals("J")||ChoixRequete.equals("JN")||ChoixRequete.equals("SansOrphelin")){
					Couple relation= new Couple(e.getValue("p").stringValue(), e.getValue("o").stringValue());
					if(!tripletRDf.containsKey(e.getValue("s").stringValue())){
						tripletRDf.put(e.getValue("s").stringValue(), new HashSet<Couple>());
					};
					tripletRDf.get(e.getValue("s").stringValue()).add(relation);
					//System.out.println(e.getValue("s").stringValue()+" --"+e.getValue("p").stringValue()+"--> "+e.getValue("o").stringValue());
					
				
				}
				else if(ChoixRequete.equals("hierarchie2")||ChoixRequete.equals("hierarchie")){
					if(!codeIdentifiant.containsKey(e.getValue("s").stringValue())){
						
						codeIdentifiant.put(e.getValue("s").stringValue(), new ArrayList<String>());
					};
					codeIdentifiant.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());
					Value p = e.getValue("p");
					
					if( p != null){
						codeIdentifiant.get(e.getValue("s").stringValue()).add(e.getValue("p").stringValue());
						
					}
					
				}
				else if(ChoixRequete.equals("Modele3")){
					Couple relation= new Couple(e.getValue("p").stringValue(), e.getValue("o").stringValue());
					coupleRelation.add(relation);
				}
				
			}
			System.out.println("système de récupération du RDF anglais ou francais terminé");
		} catch (QueryEvaluationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * methode permettant de retourner une liste de code retrouver comme premier élément du triplet
	 * @return
	 */
	public Set<String> getRDFliste(){
		return codeRdf;
	}
	public Set<Couple> getCoupleRelation(){
		return coupleRelation;
	}
	/**
	 * methode permettant de retourner le premier et le dernier élément du triplet
	 * @return
	 */
	public HashMap<String , List<String>>  getRDFlisteLibellé(){
		return codeIdentifiant;
	}
	/**
	 * methode permettant d'obtenir tout le triplet rechercher
	 * @return
	 */
	public HashMap<String , Set<Couple>>  getTripletRDF(){
		return tripletRDf;
	}

}
