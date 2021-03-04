package loincRDFconstruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import interactionTripleStore.InteractionBlazgraph;
import interactionTripleStore.RepositoryFactory;
import utilitaries.Couple;

public class CreateAnalyteComponentwithoutChallenge {

	public static void main(String[] args) throws RepositoryException, QueryEvaluationException, MalformedQueryException {
		// TODO Auto-generated method stub
		//String namespace="LOINCTotal";
		//String namespace = "TLABANDLOINC";
		String namespace="FrenchLoincAnalysis";
		//String namespace="FrenchLOINC";
		
		Repository repo = RepositoryFactory.getRepository(namespace);
		RepositoryConnection conn = repo.getConnection();
		String queryAll=getAllcomponent();
		String queryWAnalytes=getAllcomponentwithAnalytes();
		//System.out.println("queryWAnalytes "+queryWAnalytes);
				
		Set<String> allcomponent= new HashSet<String>();
		Set<String> ComponentWithAnalyte= new HashSet<String>();
		Set<String> ComponentAnalyteToCreate= new HashSet<String>();
		
		 ArrayList<BindingSet> ListeRDFTotal= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, queryAll));
			for(BindingSet e: ListeRDFTotal){
				allcomponent.add(e.getValue("o").stringValue());
			}
			System.out.println("allcomponent "+allcomponent.size());
			ArrayList<BindingSet> ListeWithAna= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, queryWAnalytes));
			for(BindingSet e: ListeWithAna){
				ComponentWithAnalyte.add(e.getValue("o").stringValue());
			}
			System.out.println("ComponentWithAnalyte "+ComponentWithAnalyte.size());
			for(String analyt:allcomponent) {
				if(!ComponentWithAnalyte.contains(analyt)) {
					ComponentAnalyteToCreate.add(analyt);
				}
			}
			String querylabel=getqueryComponentAndlabels();
			HashMap<String, Set<String>> codeAndLabels= new HashMap<String, Set<String>>();
			ArrayList<BindingSet> ListeWithLabels= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, querylabel));
			for(BindingSet e: ListeWithLabels){
				if(!codeAndLabels.containsKey(e.getValue("o").stringValue())) {
					codeAndLabels.put(e.getValue("o").stringValue(), new HashSet<String>());
				}
				codeAndLabels.get(e.getValue("o").stringValue()).add(e.getValue("p").stringValue());
			}
			HashMap<String, Set<String>> codeAndLabelsTocreate= new HashMap<String, Set<String>>();
			System.out.println("codeAndLabels "+codeAndLabels.size());
			System.out.println("ComponentAnalyteToCreate "+ComponentAnalyteToCreate.size());
			for(String code:codeAndLabels.keySet()) {
				if(ComponentAnalyteToCreate.contains(code)) {
					if(!codeAndLabelsTocreate.containsKey(code)) {
						codeAndLabelsTocreate.put(code, new HashSet<String>());
					}
					codeAndLabelsTocreate.get(code).addAll(codeAndLabels.get(code));
				
				}
				
			}
			//System.out.println("codeAndLabelsTocreate "+ codeAndLabelsTocreate);
			String prefixe="http://erias.org/loincTotal#";
			ValueFactory vf = conn.getValueFactory();
			System.out.println();
			
			HashMap<Couple, Set<String>> result=getAnalyteFromComponent(prefixe,codeAndLabelsTocreate,"AN");
		
			
			Set<Statement> mesStatement=getrdfStructure(prefixe, vf, result);
			
			String GraphLoincFrancais = "http://erias.org/FrenchLoinc";
			
			URI graphLoincFrancais = conn.getValueFactory().createURI(GraphLoincFrancais);
			List<Statement> liste= new ArrayList<Statement>(mesStatement);
			

			try {	

				conn.add(liste,graphLoincFrancais);
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("final terminé");

	}
	
	public static Set<Statement> getrdfStructure(String prefixe,ValueFactory vf,HashMap<Couple, Set<String>> result){
		
		Set<Statement> lesstatem= new HashSet<Statement>();
		String Prefix_rdfs="http://www.w3.org/2000/01/rdf-schema#";	
		for(Couple codeComponAndAnalyte: result.keySet()){
			//System.out.println(prefixe+"Has_Analyte");
			
			Statement stm = vf.createStatement( vf.createURI(codeComponAndAnalyte.x),
					vf.createURI(prefixe+"has_analyte"),
					vf.createURI(codeComponAndAnalyte.y));
			lesstatem.add(stm);
			
			for(String lib: result.get(codeComponAndAnalyte)){
				Statement stm1 = vf.createStatement( vf.createURI(codeComponAndAnalyte.y),
						vf.createURI(Prefix_rdfs + "label"),
						vf.createLiteral (lib));
				lesstatem.add(stm1);
			}
		}
		return lesstatem;
		
	}
	
	public static HashMap<Couple, Set<String>> getAnalyteFromComponent(String prefixe,HashMap<String, Set<String>>componentCodeAndLabel,String description){
		int i=50000;
		HashMap<Couple, Set<String>> descriptionresult= new HashMap<Couple, Set<String>>();
		for(String codeCo:componentCodeAndLabel.keySet()) {
			i++;
			Couple cp= new Couple(codeCo, prefixe+description+i);
			if(!descriptionresult.containsKey(cp)){
				descriptionresult.put(cp, new HashSet<String>());
			}
			descriptionresult.get(cp).addAll(componentCodeAndLabel.get(codeCo));
		}
		
		return descriptionresult;
		
	}
	
	
	
	public static String getqueryComponentAndlabels() {
		String query="prefix skos: <http://www.w3.org/2004/02/skos/core#>  " + 
				"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>  " + 
				"				 Select distinct?o ?p  WHERE { " + 
				"                 GRAPH <http://erias.org/FrenchLoinc> {  " + 
				"				?s <http://erias.org/loincTotal#has_component> ?o.  " + 
				"                   ?o rdfs:label ?p. " +
				"				 } " + 
				"				 } ";
		return query;
	}
	public static String getquery(String a) {
		String query="prefix skos: <http://www.w3.org/2004/02/skos/core#> "+
			" prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>  "+
			" 	 Select distinct ?o ?p ?d WHERE { "+				
			"    GRAPH <http://erias.org/FrenchLoinc> { "+
			" 	?s <http://erias.org/loincTotal#has_component> ?o. "+
			"      ?o <http://erias.org/loincTotal#has_analyte> ?p."+
			"       ?p rdfs:label ?d "+                  
			" 	 } "+ 
			" 	 } ";
		return query;
		
	}
	public static String getAllcomponent() {
		String query="prefix skos: <http://www.w3.org/2004/02/skos/core#>      " + 
				"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>   " + 
				"				 Select distinct?o  WHERE {  " + 
				"                 GRAPH <http://erias.org/FrenchLoinc> {  " + 
				"				?s <http://erias.org/loincTotal#has_component> ?o. " +
				"				 } " + 
				"				 } ";
		return query;
	}
	public static String getAllcomponentwithAnalytes() {
		String query="prefix skos: <http://www.w3.org/2004/02/skos/core#>  " + 
				" prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ " Select distinct ?o  WHERE {                " + 
				" GRAPH <http://erias.org/FrenchLoinc> {   " + 
				" ?s <http://erias.org/loincTotal#has_component> ?o. " + 
				"                   ?o <http://erias.org/loincTotal#has_analyte> ?p.  " +
				"				 }   " + 
				"				 } ";
		System.out.println(query);
		return query;
	}
	
	public static HashMap<String, Set<Couple>> parseHyerarchyLabels(String libelleOriginal){



		

		//String libelleOriginal= "Human papilloma virus retrovirus 16 & 18 & 31+33+35+39+45+51+52+56+58+59+66+68 ADN franck";
		//String libelleOriginal="anti Cortisol.libre/Creatinine";
		//String libelleOriginal="(Anacardium occidentale+Carya illinoinensis+Juglans regia) , IgE";
		//String libelleOriginal="Borrelia afzelii+burgdorferi+garinii Ac IgG et IgM panel";
		//String libelleOriginal="Arachide (Arachis hypogaea) recombinant (rAra h) 1 , IgE.RAST.film.cine.dysney";
		//String libelleOriginal="(Acer negundo+Betula verrucosa+Fagus grandifolia+Quercus alba+Juglans californica)-made change to common term - pls confirm , IgE";
		//String libelleOriginal="(artemisia vulgaris+betula verrucosa+squame de chat+cladosporium herbarum+dermatophagoides pteronyssinus+squame de chien+phleum pratense+secale cereale) anticorps.ige";
		//String libelleOriginal="(armoise commune+bouleau+chat (squames)+cladosporium herbarum+dermatophagoides pteronyssinus+chien (squames)+phléole des prés+seigle) ac ige";
		//String libelleOriginal="(artemisia dracunculus+levisticum officinale+origanum majorana+thymus vulgaris) anticorps. ige.rast";
		//String libelleOriginal="herpes simplex virus 1 & 2+3 ab.igg & igm panel";
		//String libelleOriginal="s wave amplitude.lead v3";
		//String libelleOriginal="collagène.injectable , ac";
		Set<String> lesAnalytes= new HashSet<String>();
		
		HashMap<String, Set<String>> lesComposantAnalyste= new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> lesComposantNonAnalyste= new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> lesComposantChallenge= new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> lesComposantAjustement= new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> lesComposant= new HashMap<String, Set<String>>();
		HashMap<String, Set<Couple>> lesAnalytesHierarchie= new HashMap<String, Set<Couple>>();
		HashMap<String, String>echelleSubclass= new HashMap<String, String>();
		//Set<Couple> echelleSubclass=new HashSet<Couple>();
		
		String ExpressionDecoupe="(\\+|\\/|&|\\.)";
		Pattern patternExpressionDecoupe= Pattern.compile(ExpressionDecoupe);
		String Expressionpoint="\\.";
		Pattern patternExpressionPoint= Pattern.compile(Expressionpoint);
		
		String ExpressionNegation="-";
		Pattern patternExpressionNegation= Pattern.compile(ExpressionNegation);
		
		
		String ExpressionMotGraceAuxEspaces="([^\\s]+\\.*)";
		Pattern patternMotdulibbeleGraceAespace= Pattern.compile(ExpressionMotGraceAuxEspaces);
		Set<String> motfinissantParPlusoumoins=new HashSet<String>();
		
		Set<String> LesQuotients= new HashSet<String>();
		String[] libelleL=libelleOriginal.split("/");
		if(libelleL.length>1) {
			System.out.println("test"+libelleL.length);
		LesQuotients.add(libelleL[1]);
		}
		String libelle=libelleL[0];
		System.out.println("libelle "+libelle+" LesQuotients "+LesQuotients);
		if(!LesQuotients.isEmpty()) {
			System.out.println(LesQuotients+" sddddddddddddddddddd");
			lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(libelleOriginal), getStringEspaceCorrection(libelle), "a"));
			
		}
		int i=0;
		for(String composants:libelle.split("\\^")){
			//System.out.println("composants "+composants);
			i++;
			if(i==1){
				int u=0;
				Matcher matcheurExpressionDecoupe= patternExpressionDecoupe.matcher(composants);
				if(!lesComposantAnalyste.containsKey(Integer.toString(i))){
					lesComposantAnalyste.put(Integer.toString(i), new HashSet<String>());
				}
				if(!lesComposantNonAnalyste.containsKey(Integer.toString(i))){
					lesComposantNonAnalyste.put(Integer.toString(i), new HashSet<String>());
				}
				while(matcheurExpressionDecoupe.find()){
					u++;
				}
				Set<String> caract1= new HashSet<String>();
				Set<String> caract2= new HashSet<String>();
				
				if(u>0){
					System.out.println(composants+" test ");
					int v=0;
					//for(String LesTroisCaracteristiques:composants.split("(\\)[\\s]*,*[\\s]|,[\\s])")){
					//for(String LesTroisCaracteristiques:composants.split("(,[\\s])")){
						String LesTroisCaracteristiques=composants;
						v++;
						String LesTroisCaracteristique="";
						//String parenthese
						if(LesTroisCaracteristiques.startsWith("(")){
							if(LesTroisCaracteristiques.startsWith("(view")) {
								
							}else {

							
							StringTokenizer stOuvrante = new StringTokenizer(composants, "(");
							//StringTokenizer stFermante = new StringTokenizer(composants, ")");
							System.out.println(stOuvrante.countTokens());
							
							String prefixe="";
							String prefixeAlpha="";
							String suffice="";
							for(int j=0;  j<=stOuvrante.countTokens();j++) {
								if(j==0) {
									System.out.println(stOuvrante.countTokens());
									for(int tyy=0;  tyy<=stOuvrante.countTokens()-1;tyy++) {
										if(tyy==0) {
											prefixe=composants.split("\\)")[tyy];
										}
										else {
										prefixe=prefixe+") "+composants.split("\\)")[tyy];
										}
										
									}
									
									//prefixe= stFermante.nextToken();
									
									
								System.out.println("soit "+j+" "+prefixe);
								String alpha=prefixe.substring(1);
								prefixeAlpha=alpha;
								System.out.println("alpha ++++++++++++++++++"+alpha);
								lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(composants), getStringEspaceCorrection(alpha), "a"));
								
								
								
								}
								else {
									
									// suffice=stFermante.nextToken();
									if(composants.startsWith("(kiwi+ananas+melon+banane+pèche")) {
										if(!composants.startsWith("(kiwi+ananas+melon+banane+pèche)")) {
										composants=composants.replace("(kiwi+ananas+melon+banane+pèche", "(kiwi+ananas+melon+banane+pèche)");
										}
										}
									 suffice=composants.split("\\)")[stOuvrante.countTokens()];
									// suffice=composants.split("\\)")[stOuvrante.countTokens()];
										
									 for(String elet: prefixeAlpha.split("\\+|[\\s]&[\\s]")) {
										 lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(composants), getStringEspaceCorrection(elet+" "+suffice), "a"));
										 lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(elet+" "+suffice), getStringEspaceCorrection(elet), "a"));
										 lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(prefixeAlpha), getStringEspaceCorrection(elet), "a"));
											
										 
										 
									 }
									System.out.println("test suffice "+suffice);
									for(String code:lesAnalytesHierarchie.keySet()) {
										for(Couple ds: lesAnalytesHierarchie.get(code)) {
											System.out.println("code "+code+" "+ds.x+" "+ds.y);
										}
									}
									
									
								}
							}
						
					
						//	System.out.println(LesTroisCaracteristiques+" a voir ");
							
							//LesTroisCaracteristique=LesTroisCaracteristiques.substring(1);
							
							
							//System.out.println(LesTroisCaracteristique+"LesTroisCaracteristique");
						}
				}
						else{
							LesTroisCaracteristique=LesTroisCaracteristiques;
						}
						//System.out.println(LesTroisCaracteristique);
						
						int w=0;
						Set<String> composantsplit= new HashSet<String>();
						
						for(String carrat: LesTroisCaracteristique.split("\\/")){
							System.out.println("carrat carrat "+carrat);
							w++;
							Matcher matcherpoint=patternExpressionPoint.matcher(carrat);
							int njd=0;
							while(matcherpoint.find()){
								njd++;
							}
							if(carrat.endsWith("+")|carrat.endsWith("-")){
								motfinissantParPlusoumoins.add(carrat);
							}
					
							 if(njd==0){
								
								int visi=0;
								Set<String> resultat= new HashSet<String>();
								String preficecommun="";
								String suffixeCommun=getSuffixes(carrat);
								String LastsuffixeCommun=getLastSuffixes(carrat);
								//System.out.println(LastsuffixeCommun+" LastsuffixeCommun");
								System.out.println(LastsuffixeCommun+" LastsuffixeCommun");
								for(String carract:carrat.split("\\+|[\\s]&[\\s]")){
									visi++;
									HashMap<String, Set<String>> numeroCorection= new HashMap<String, Set<String>>();
									System.out.println("carract importnat "+carract);
									if(visi==1){
										resultat.add(carract);
										composantsplit.add(carract);
										//System.out.println("je veux savoir "+carract);
										
										Matcher espac=patternMotdulibbeleGraceAespace.matcher(carract);
										int kl=0;
										String codeee="";
										while(espac.find()){
											kl++;
											
											codeee=codeee+" "+espac.group(1);
											
											if(!numeroCorection.containsKey(Integer.toString(kl))){
												numeroCorection.put(Integer.toString(kl), new HashSet<String>());
											}
											numeroCorection.get(Integer.toString(kl)).add(codeee);
										}
								
										if(kl>1){
										for(String ft:numeroCorection.get(Integer.toString(kl-1))){
											//test
											
											preficecommun=preficecommun+" "+ft;
											
											
										}
										System.out.println("preficecommun "+preficecommun);
										//tocorrect
										String con=LastsuffixeCommun.split(" ")[0];
								lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(preficecommun+" "+LastsuffixeCommun), getStringEspaceCorrection(preficecommun+" "+con), "a"));
								lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(preficecommun+" "+con), getStringEspaceCorrection(preficecommun), "a"));
										
										}
										if(!carrat.equals(carract)) {
											System.out.println("carract +++"+carract);
										System.out.println("carrat "+carrat);
										
										lesAnalytes.add(getStringEspaceCorrection(carrat));
										lesAnalytes.add(getStringEspaceCorrection(carract));
										if(!getStringEspaceCorrection(carract).equals(getStringEspaceCorrection(carrat))) {
										lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrat), getStringEspaceCorrection(carract+" "+suffixeCommun), "a"));
										
										lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carract+" "+suffixeCommun), getStringEspaceCorrection(carract), "a"));
										
										System.out.println("test "+carrat+" double test "+carract);
										}
									}
									}
									else{
										if (!carract.equals(LastsuffixeCommun)) {
											//System.out.println("########preficecommun*"+preficecommun+" carract*"+carract+" suffixeCommun*"+suffixeCommun);
										resultat.contains(preficecommun+" "+carract+" "+suffixeCommun);
										composantsplit.add(preficecommun+" "+carract+" "+suffixeCommun);
										lesAnalytes.add(getStringEspaceCorrection(carrat));
										lesAnalytes.add(getStringEspaceCorrection(preficecommun+" "+carract+" "+suffixeCommun));
										lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrat), getStringEspaceCorrection(preficecommun+" "+carract+" "+suffixeCommun), "a"));
										lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(preficecommun+" "+carract+" "+suffixeCommun), getStringEspaceCorrection(preficecommun+" "+carract), "a"));
										lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(preficecommun+" "+carract), getStringEspaceCorrection(preficecommun), "a"));
										
										//System.out.println("resutl : "+resultat);
										}
										else{
											resultat.contains(preficecommun+" "+carract);
											composantsplit.add(preficecommun+" "+carract);
											lesAnalytes.add(getStringEspaceCorrection(carrat));
											lesAnalytes.add(getStringEspaceCorrection(preficecommun+" "+carract));
											lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrat), getStringEspaceCorrection(preficecommun+" "+carract), "a"));
											lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(preficecommun+" "+carract), getStringEspaceCorrection(preficecommun), "a"));
											
										}
									}
									
									
									
								}
							}
							else{
								
								int visi=0;
								Set<String> ccaer1= new HashSet<String>();
								Set<String> ccaer1Mot= new HashSet<String>();
								Set<String> ccaer2= new HashSet<String>();
								String echelle="";
								int ech=1;
								for(String carrate:carrat.split("\\.")){
									visi++;
									System.out.println("carrate point "+carrate);
									int vs=0;
									String preficecommun="";
									String suffixeCommunDansPOint=getSuffixes(carrate);
									
									System.out.println("*++"+suffixeCommunDansPOint+"*");
									String lastSuffuxeCommun=getLastSuffixes(carrate);
								
									for(String carract:carrate.split("\\+|[\\s]&[\\s]")){
										vs++;
										

										HashMap<String, Set<String>> numeroCorection= new HashMap<String, Set<String>>();
										
										if(vs==1){
											//System.out.println("je veux savoir "+carract);
											Matcher espac=patternMotdulibbeleGraceAespace.matcher(carract);
											int kl=0;
											String codeee="";
											while(espac.find()){
												kl++;
												
												codeee=codeee+" "+espac.group(1);
												
												
												if(!numeroCorection.containsKey(Integer.toString(kl))){
													numeroCorection.put(Integer.toString(kl), new HashSet<String>());
												}
												numeroCorection.get(Integer.toString(kl)).add(codeee);
											}
											
											
											if(kl>1){
												for(String ft:numeroCorection.get(Integer.toString(kl-1))){
													preficecommun=preficecommun+" "+ft;
													
												}
												
											}
											//System.out.println("preficecommun "+preficecommun);
											if(visi==1){
												System.out.println("pourquoi "+carrate);
												ccaer1.add(carrate);
												ccaer1Mot.add(carrate);
												
										System.out.println("je veux savoir + "+carrate);
										System.out.println("je veux savoir + carract "+carract);
										
												lesAnalytes.add(getStringEspaceCorrection(carrate));
												lesAnalytes.add(getStringEspaceCorrection(carract));
												
											if(!getStringEspaceCorrection(carract).equals(getStringEspaceCorrection(carrate))){
												System.out.println("alleluia "+suffixeCommunDansPOint);
												if(suffixeCommunDansPOint != null && !suffixeCommunDansPOint.isEmpty()) {
												lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrate), getStringEspaceCorrection(carract+" "+suffixeCommunDansPOint), "a"));
												lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carract+" "+suffixeCommunDansPOint), getStringEspaceCorrection(carract), "a"));
												lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carract), getStringEspaceCorrection(preficecommun), "a"));
												
												}else {
													lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrate), getStringEspaceCorrection(carract), "a"));
													lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carract), getStringEspaceCorrection(preficecommun), "a"));
													
												}
												//System.out.println(carrate+"ùùùùùùùùùùùùùùùùùùùù");
											}
											}
											else {
												//test
												System.out.println("quoiooooooooooooooooooooooooooooo "+carract);
												System.out.println("quoiooooooooooooooooooooooooooooo "+carrate);
												System.out.println("quoiooooooooooooooooooooooooooooo "+suffixeCommunDansPOint);
													if(suffixeCommunDansPOint != null && !suffixeCommunDansPOint.isEmpty()) {
														if(echelle.isEmpty()) {
															
															
															echelle=carract+" "+suffixeCommunDansPOint;
															
														}
														else if (!getStringEspaceCorrection(echelle).equals(getStringEspaceCorrection(carract+" "+suffixeCommunDansPOint))){
															//System.out.println("pour voir echelle "+echelle+" carract "+carract+" suffixeCommunDansPOint "+suffixeCommunDansPOint);
															
															echelle=echelle+" "+carract+" "+suffixeCommunDansPOint;
															}
														//echelle=echelle+carract+" "+suffixeCommunDansPOint;
														//ccaer2.add(carract+" "+suffixeCommunDansPOint);
													
														System.out.println("echelle :" +ech+" : "+echelle);
														String echel=Integer.toString(ech);
														if(!echelleSubclass.containsKey(echel)) {
															echelleSubclass.put(echel, echelle);
														}
														ech++;
														ccaer2.add(echelle);
														System.out.println("jn' "+echelle);
														System.out.println("*"+suffixeCommunDansPOint+"*");
													}
													else {
														System.out.println("jn "+carract);
														//ccaer2.add(carract);
														if(echelle.isEmpty()) {
															echelle=carract;
														}
														else {
															echelle=echelle+" "+carract;
															//System.out.println(echelle);
														}
														System.out.println("echelle :" +ech+" : "+echelle);
														String echel=Integer.toString(ech);
														if(!echelleSubclass.containsKey(echel)) {
															echelleSubclass.put(echel, echelle);
														}
														
														ech++;
														ccaer2.add(echelle);
														System.out.println("jn' "+echelle);
													}
												
												//ccaer2.add(carract+" "+suffixeCommunDansPOint);
												//System.out.println("test +++ "+carract+suffixeCommun+ "ffffffffffffffffffffffffffff");
//												for(String atd: ccaer1Mot) {
//													System.out.println(atd+" "+preficecommun+" "+carract+" "+suffixeCommun);
//													lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(atd+" "+preficecommun+" "+carract+" "+suffixeCommun), getStringEspaceCorrection(atd+" "+preficecommun+" "+carract), "a"));
//													
//												}
												
											
											}
											
										}
										else{
											if(visi==1){
												System.out.println(carrate+" a voir ---------------------");
												System.out.println(carract+"****gdhhhhhhhhhhhhhhhhhhhhh"+suffixeCommunDansPOint);
												System.out.println(lastSuffuxeCommun);
												
												ccaer1.add(carrate);
												if(carract.equals(lastSuffuxeCommun)) {
													
												lesAnalytes.add(getStringEspaceCorrection(carrate));
												lesAnalytes.add(getStringEspaceCorrection(preficecommun+" "+carract));
												lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrate), getStringEspaceCorrection(preficecommun+" "+carract), "a"));
												
												ccaer1Mot.add(carrate);
													if(getStringEspaceCorrection(carract).endsWith(getStringEspaceCorrection(suffixeCommunDansPOint))){
														System.out.println("---------------------------------------------------------");
														String lov=carract.replace(getStringEspaceCorrection(suffixeCommunDansPOint), "");
														System.out.println(suffixeCommunDansPOint+" "+carract+" lov "+lov);
														String lov2=getStringEspaceCorrection(lov);
														if(lov2!=null && !lov2.isEmpty()) {
															lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(preficecommun+" "+carract), getStringEspaceCorrection(preficecommun+" "+lov2), "a"));
															lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(preficecommun+" "+lov2), getStringEspaceCorrection(preficecommun), "a"));
															
														}
														
													}
												}else {
													System.out.println(carract+" sfdddddddd5555555555555555555555555555555555555555555555555");
													lesAnalytes.add(getStringEspaceCorrection(carrate));
													if(suffixeCommunDansPOint != null && !suffixeCommunDansPOint.isEmpty()) {
														lesAnalytes.add(getStringEspaceCorrection(preficecommun+" "+carract+" "+suffixeCommunDansPOint));
														lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrate), getStringEspaceCorrection(preficecommun+" "+carract+" "+suffixeCommunDansPOint), "a"));
														lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(preficecommun+" "+carract+" "+suffixeCommunDansPOint), getStringEspaceCorrection(preficecommun+" "+carract), "a"));
														lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(preficecommun+" "+carract), getStringEspaceCorrection(preficecommun), "a"));
														System.out.println(carrate+"$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"+preficecommun+" "+carract+" "+suffixeCommunDansPOint);
													}
													else {
														lesAnalytes.add(getStringEspaceCorrection(preficecommun+" "+carract));
														lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrate), getStringEspaceCorrection(preficecommun+" "+carract), "a"));
														lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(preficecommun+" "+carract), getStringEspaceCorrection(preficecommun), "a"));
														
													}
													//lesAnalytes.add(getStringEspaceCorrection(preficecommun+" "+carract+" "+suffixeCommun));
													//lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrate), getStringEspaceCorrection(preficecommun+" "+carract+" "+suffixeCommun), "a"));
													
													ccaer1Mot.add(carrate);
												
													
												}
											}
											else {
												System.out.println(carract+" gggggggggggggggggggggg"+preficecommun);
												
												if(!getStringEspaceCorrection(carract).equals(getStringEspaceCorrection(lastSuffuxeCommun))) {
													if(suffixeCommunDansPOint!=null && !suffixeCommunDansPOint.isEmpty()) {
														ccaer2.add(preficecommun+" "+carract+" "+suffixeCommunDansPOint);
//											lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrate), getStringEspaceCorrection(preficecommun+" "+carract+" "+suffixeCommunDansPOint), "a"));
//											lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(preficecommun+" "+carract+" "+suffixeCommunDansPOint), getStringEspaceCorrection(preficecommun+" "+carract), "a"));	
//
//											lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(preficecommun+" "+carract), getStringEspaceCorrection(preficecommun), "a"));	
													}
													else {
														ccaer2.add(preficecommun+" "+carract);
//														lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(preficecommun+" "+carract), getStringEspaceCorrection(preficecommun), "a"));	
//														lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrate), getStringEspaceCorrection(preficecommun+" "+carract), "a"));	
//														
													}
												
												}
												else {
													ccaer2.add(preficecommun+" "+carract);
//													lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(preficecommun+" "+carract), getStringEspaceCorrection(preficecommun), "a"));	
//													lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrate), getStringEspaceCorrection(preficecommun+" "+carract), "a"));	
//													
													
												}
											}
										}
										
										
										
									
									}
								
								}
								
								for(String a:ccaer1){
									System.out.println("ccaer2 "+ccaer2);
									for(String b:ccaer2){
										String ab=a+" "+b;
										composantsplit.add(ab);
										
										
										lesAnalytes.add(getStringEspaceCorrection(ab));
										lesAnalytes.addAll(getStringespaceCorrectionListe(ccaer1Mot));
										
										lesAnalytes.add(getStringEspaceCorrection(ab));
										lesAnalytes.add(getStringEspaceCorrection(carrat));
										System.out.println(" trois entités 1 carrat "+carrat);
										System.out.println("trois entités  2 ab " +ab);
										System.out.println("trois entités  3 ccaer1Mot " +ccaer1Mot);
										System.out.println("++++++++++++++++++");
										if(!getStringEspaceCorrection(carrat.replace(".", " ")).equals(ab)) {
											//System.out.println("dddddddddddddd "+carrat.replace(".", " "));
										lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrat), getStringEspaceCorrection(ab), "a"));
										System.out.println(" trois entités 1 "+carrat);
										System.out.println("trois entités  2 " +ab);
										System.out.println("trois entités  3 " +ccaer1Mot);
										System.out.println(ccaer2);
										}
										int ap=1;
										for(String ky:echelleSubclass.keySet()) {
											if(getStringEspaceCorrection(b).equals(getStringEspaceCorrection(echelleSubclass.get(ky)))) {
												ap=Integer.parseInt(ky);
											}
										}
										for(String ky:echelleSubclass.keySet()) {
											int aps=Integer.parseInt(ky);
											if(aps<ap) {
												String abf=a+" "+echelleSubclass.get(ky);
												lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(ab), getStringEspaceCorrection(abf), "a"));
												
												
											}
										}
										String resultatSufixe=getSuffixes(carrat);
										String abd =ab.split(resultatSufixe)[0];
										//System.out.println(resultatSufixe+"resultatSufixe");
										
										if(!resultatSufixe.isEmpty()) {
											//System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
										lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(abd), getStringespaceCorrectionListe(ccaer1Mot), "a"));
										lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(ab), getStringEspaceCorrection(abd), "a"));
										}
										else {
											//System.out.println("try");
											lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(ab), getStringespaceCorrectionListe(ccaer1Mot), "a"));
											//System.out.println("ccaer1Mot"+ab);
										}
										//System.out.println("ab ab "+ab.substring(0, ab.length()-lastSuffuxeCommun.length()));
										
										
									}
								}
								if(ccaer2.isEmpty()){
									if(!ccaer1.isEmpty()){
										composantsplit.addAll(ccaer1);
										
							lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringespaceCorrectionListe(ccaer1), getStringespaceCorrectionListe(ccaer1Mot), "a"));
									
										lesAnalytes.addAll(getStringespaceCorrectionListe(ccaer1));
										lesAnalytes.addAll(getStringespaceCorrectionListe(ccaer1Mot));
								
							lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrat), getStringespaceCorrectionListe(ccaer1), "a"));
										
										lesAnalytes.add(getStringEspaceCorrection(carrat));
										
										}
								}
								else if(ccaer1.isEmpty()){
									if(!ccaer2.isEmpty()){
										composantsplit.addAll(ccaer2);
										lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringespaceCorrectionListe(ccaer2), getStringespaceCorrectionListe(ccaer1Mot), "a"));
										
										
										lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrat), getStringespaceCorrectionListe(ccaer2), "a"));
										
										
										lesAnalytes.addAll(getStringespaceCorrectionListe(ccaer2));
										lesAnalytes.addAll(getStringespaceCorrectionListe(ccaer1Mot));
										lesAnalytes.add(getStringEspaceCorrection(carrat));
									}
								}
								
								
							}
						
							if(!motfinissantParPlusoumoins.isEmpty()){
							
								for(String code: composantsplit){
									int vuio =0;
									Matcher matchnegation=patternExpressionNegation.matcher(code);
									int iuo=0;
									while(matchnegation.find()){
										iuo++;
									}
									if(iuo>0){
										String preficeNegation="";
										
									for(String carrrt:code.split("-")){
										vuio++;
										HashMap<String, Set<String>> numeroCorectionNegation= new HashMap<String, Set<String>>();
																				
										if(vuio<=iuo&&vuio==1){
											Matcher espac=patternMotdulibbeleGraceAespace.matcher(carrrt);
											int kl=0;
											String codeee="";
											while(espac.find()){
												kl++;
												codeee=codeee+" "+espac.group(1);
											
												
												if(!numeroCorectionNegation.containsKey(Integer.toString(kl))){
													numeroCorectionNegation.put(Integer.toString(kl), new HashSet<String>());
												}
												numeroCorectionNegation.get(Integer.toString(kl)).add(codeee);
											}
										
											if(kl>1){
											for(String ft:numeroCorectionNegation.get(Integer.toString(kl-1))){
												preficeNegation=preficeNegation+" "+ft;
												
											}
											}
											
										
											lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrrt), getStringEspaceCorrection(code), "b"));
											
											
											lesAnalytes.add(getStringEspaceCorrection(carrrt));
											lesAnalytes.add(getStringEspaceCorrection(code));
										}
										
										else if(vuio<=iuo){
											
											
											lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(preficeNegation+" "+carrrt), getStringEspaceCorrection(code), "b"));
											
											lesAnalytes.add(getStringEspaceCorrection(code));
											lesAnalytes.add(getStringEspaceCorrection(preficeNegation+" "+carrrt));
										}
										else{
											lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(code),getStringEspaceCorrection(preficeNegation+" "+carrrt), "a"));
											
											lesAnalytes.add(getStringEspaceCorrection(code));
											lesAnalytes.add(getStringEspaceCorrection(preficeNegation+" "+carrrt));
										}
										
									}
								}
								}
							}
					
						}
						
						if(v==1){
							caract1.addAll(composantsplit);
							//System.out.println("composantsplit "+composantsplit);
						}
						else{
							caract2.addAll(composantsplit);
						}
						
					//look for this ==>  and open the angle for(String LesTroisCaracteristiques:composants.split("(,[\\s])")){ //}
					//
					//System.out.println("caract1 "+caract1);
					//System.out.println("caract2 "+caract2);
					for(String a:caract1){
						for(String b:caract2){
							String ab=a+" "+b;
							if(v==1){
								lesComposantAnalyste.get(Integer.toString(i)).add(ab);
								
								lesAnalytes.add(getStringEspaceCorrection(composants));
			lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(composants), getStringEspaceCorrection(ab), "a"));
								
								lesAnalytes.add(getStringEspaceCorrection(ab));
							}
							else{
								lesComposantAnalyste.get(Integer.toString(i)).add(ab);
								
								lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(composants), getStringEspaceCorrection(ab), "a"));
								
								lesAnalytes.add(getStringEspaceCorrection(composants));
								lesAnalytes.add(getStringEspaceCorrection(ab));
							}
							
							
							
						}
					}
					if(caract2.isEmpty()){
						if(!caract1.isEmpty()){
							if(v==1){
								lesComposantAnalyste.get(Integer.toString(i)).addAll(caract1);
								lesAnalytes.addAll(getStringespaceCorrectionListe(caract2));
								
							}
							else{
								lesComposantAnalyste.get(Integer.toString(i)).addAll(caract1);
								lesAnalytes.addAll(getStringespaceCorrectionListe(caract2));
								
							}
							}
					}
					else if(caract1.isEmpty()){
						if(!caract2.isEmpty()){
							if(v==1){
								lesComposantAnalyste.get(Integer.toString(i)).addAll(caract2);
								lesAnalytes.addAll(getStringespaceCorrectionListe(caract2));
								
							}
							else{
								lesComposantAnalyste.get(Integer.toString(i)).addAll(caract2);
								lesAnalytes.addAll(getStringespaceCorrectionListe(caract2));
								
							}
						}
					}
					
				}
				else{
					if(!lesComposantAnalyste.containsKey(Integer.toString(i))){
						lesComposantAnalyste.put(Integer.toString(i), new HashSet<String>());
					}
					lesComposantAnalyste.get(Integer.toString(i)).add(composants);
					lesAnalytes.add(getStringEspaceCorrection(composants));
				}
				
				
				lesAnalytes.add(getStringEspaceCorrection(composants));
				
				
			}
			else if(i==2){
				if(!lesComposantChallenge.containsKey(Integer.toString(i))){
					lesComposantChallenge.put(Integer.toString(i), new HashSet<String>());
				}
				lesComposantChallenge.get(Integer.toString(i)).add(composants);
		
			}
			else if(i==3){
				if(!lesComposantAjustement.containsKey(Integer.toString(i))){
					lesComposantAjustement.put(Integer.toString(i), new HashSet<String>());
				}
				lesComposantAjustement.get(Integer.toString(i)).add(composants);
			}
			else{
				System.out.println("erreur");
			}
		
		}
	
		
		for(String a:lesComposantAnalyste.keySet()){
			if(!lesComposant.containsKey(a)){
				lesComposant.put(a, new HashSet<String>());
				
			}
			lesComposant.get(a).addAll(lesComposantAnalyste.get(a));
			//System.out.println("a +++"+lesComposantAnalyste.get(a));
		}
		for(String a:lesComposantAjustement.keySet()){
			if(!lesComposant.containsKey(a)){
				lesComposant.put(a, new HashSet<String>());
				
			}
			lesComposant.get(a).addAll(lesComposantAjustement.get(a));
			
		}
		for(String a:lesComposantChallenge.keySet()){
			if(!lesComposant.containsKey(a)){
				lesComposant.put(a, new HashSet<String>());
				
			}
			lesComposant.get(a).addAll(lesComposantChallenge.get(a));
			
		}
		
		for( String cp:lesAnalytesHierarchie.keySet()) {
			System.out.println(cp);
			for(Couple cpt:lesAnalytesHierarchie.get(cp)) {
				System.out.println(cpt.x+"   hdhd  "+cpt.y);
			}
		};
	
	
	return lesAnalytesHierarchie;
//	
//
//		//String libelleOriginal= "Human papilloma virus retrovirus 16 & 18 & 31+33+35+39+45+51+52+56+58+59+66+68 ADN franck";
//		//String libelleOriginal="anti Cortisol.libre/Creatinine";
//		//String libelleOriginal="(Anacardium occidentale+Carya illinoinensis+Juglans regia) , IgE";
//		
//		//String libelleOriginal="Arachide (Arachis hypogaea) recombinant (rAra h) 1 , IgE.RAST.film.cine.dysney";
//		//String libelleOriginal="(Acer negundo+Betula verrucosa+Fagus grandifolia+Quercus alba+Juglans californica)-made change to common term - pls confirm , IgE";
//		Set<String> lesAnalytes= new HashSet<String>();
//		
//		HashMap<String, Set<String>> lesComposantAnalyste= new HashMap<String, Set<String>>();
//		HashMap<String, Set<String>> lesComposantNonAnalyste= new HashMap<String, Set<String>>();
//		HashMap<String, Set<String>> lesComposantChallenge= new HashMap<String, Set<String>>();
//		HashMap<String, Set<String>> lesComposantAjustement= new HashMap<String, Set<String>>();
//		HashMap<String, Set<String>> lesComposant= new HashMap<String, Set<String>>();
//		HashMap<String, Set<Couple>> lesAnalytesHierarchie= new HashMap<String, Set<Couple>>();
//		HashMap<String, String>echelleSubclass= new HashMap<String, String>();
//		//Set<Couple> echelleSubclass=new HashSet<Couple>();
//		
//		String ExpressionDecoupe="(\\+|\\/|&|\\.)";
//		Pattern patternExpressionDecoupe= Pattern.compile(ExpressionDecoupe);
//		String Expressionpoint="\\.";
//		Pattern patternExpressionPoint= Pattern.compile(Expressionpoint);
//		
//		String ExpressionNegation="-";
//		Pattern patternExpressionNegation= Pattern.compile(ExpressionNegation);
//		
//		
//		String ExpressionMotGraceAuxEspaces="([^\\s]+\\.*)";
//		Pattern patternMotdulibbeleGraceAespace= Pattern.compile(ExpressionMotGraceAuxEspaces);
//		Set<String> motfinissantParPlusoumoins=new HashSet<String>();
//		
//		Set<String> LesQuotients= new HashSet<String>();
//		String[] libelleL=libelleOriginal.split("/");
//		if(libelleL.length>1) {
//			System.out.println("test"+libelleL.length);
//		LesQuotients.add(libelleL[1]);
//		}
//		String libelle=libelleL[0];
//		System.out.println("libelle "+libelle+" LesQuotients "+LesQuotients);
//		int i=0;
//		for(String composants:libelle.split("\\^")){
//			//System.out.println("composants "+composants);
//			i++;
//			if(i==1){
//				int u=0;
//				Matcher matcheurExpressionDecoupe= patternExpressionDecoupe.matcher(composants);
//				if(!lesComposantAnalyste.containsKey(Integer.toString(i))){
//					lesComposantAnalyste.put(Integer.toString(i), new HashSet<String>());
//				}
//				if(!lesComposantNonAnalyste.containsKey(Integer.toString(i))){
//					lesComposantNonAnalyste.put(Integer.toString(i), new HashSet<String>());
//				}
//				while(matcheurExpressionDecoupe.find()){
//					u++;
//				}
//				Set<String> caract1= new HashSet<String>();
//				Set<String> caract2= new HashSet<String>();
//				
//				if(u>0){
//					System.out.println(composants+" test ");
//					int v=0;
//					//for(String LesTroisCaracteristiques:composants.split("(\\)[\\s]*,*[\\s]|,[\\s])")){
//					//for(String LesTroisCaracteristiques:composants.split("(,[\\s])")){
//						String LesTroisCaracteristiques=composants;
//						v++;
//						String LesTroisCaracteristique="";
//						//String parenthese
//						if(LesTroisCaracteristiques.startsWith("(")){
//							if(LesTroisCaracteristiques.startsWith("(view")) {
//								
//							}else {
//							
//
//							
//							StringTokenizer stOuvrante = new StringTokenizer(composants, "(");
//							StringTokenizer stFermante = new StringTokenizer(composants, ")");
//							System.out.println(stOuvrante.countTokens());
//							
//							String prefixe="";
//							String prefixeAlpha="";
//							String suffice="";
//							for(int j=0;  j<=stOuvrante.countTokens();j++) {
//								if(j==0) {
//									
//									prefixe= stFermante.nextToken();
//									
//									
//								System.out.println("soit "+j+" "+prefixe);
//								String alpha=prefixe.substring(1);
//								prefixeAlpha=alpha;
//								System.out.println("alpha ++++++++++++++++++"+alpha);
//								lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(composants), getStringEspaceCorrection(alpha), "a"));
//								
//								
//								
//								}
//								else {
//									
//									// suffice=stFermante.nextToken();
//									if(composants.startsWith("(kiwi+ananas+melon+banane+pèche")) {
//										if(!composants.startsWith("(kiwi+ananas+melon+banane+pèche)")) {
//										composants=composants.replace("(kiwi+ananas+melon+banane+pèche", "(kiwi+ananas+melon+banane+pèche)");
//										}
//										}
//									 suffice=composants.split("\\)")[stOuvrante.countTokens()];
//										
//									 System.out.println("suffice "+suffice);
//									 HashMap<String,String> todo= new HashMap<String, String>();
//									 int vege=1;
//									 String suf="";
//									 for(String carrate:suffice.split("\\.")){
//										 if(vege==1) {
//											 suf=carrate;
//										 }
//										 else {
//											 suf=suf+" "+carrate;
//										 }
//										 String alp=Integer.toString(vege);
//										 if(!todo.containsKey(alp)) {
//											 todo.put(alp, suf);
//										 }
//										 vege++;
//										 
//										 System.out.println("découpage "+carrate);
//									 }
//									 System.out.println("todo "+todo);
//									 for(String elet: prefixeAlpha.split("\\+|[\\s]&[\\s]")) {
//										 lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(composants), getStringEspaceCorrection(elet+" "+suffice), "a"));
//										 lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(elet+" "+suffice), getStringEspaceCorrection(elet), "a"));
//										 lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(prefixeAlpha), getStringEspaceCorrection(elet), "a"));
//											
//										 int ap=1;
//										 for(String ky:todo.keySet()) {
//												int aps=Integer.parseInt(ky);
//												System.out.println("aps "+aps);
//												if(aps>ap) {
//													int adre=aps-1;
//													String abf=todo.get(ky);
//													String prec=todo.get(Integer.toString(adre));
//													System.out.println(" 66666666666666666 "+abf);
//													System.out.println("prefixeAlpha "+elet+" suffixe"+abf);
//				lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(elet+" "+abf), getStringEspaceCorrection(elet+" "+prec), "a"));
//													
//													
//												}
//												if(aps==1) {
//													String abf=todo.get(ky);
//													System.out.println(" 1111111111111 "+abf);
//				lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(elet+" "+abf), getStringEspaceCorrection(elet), "a"));
//													
//												}
//											}
//										 
//									 }
//									System.out.println("test suffice "+suffice);
//									for(String code:lesAnalytesHierarchie.keySet()) {
//										for(Couple ds: lesAnalytesHierarchie.get(code)) {
//											System.out.println("code "+code+" "+ds.x+" "+ds.y);
//										}
//									}
//									
//									
//								}
//							}
//						
//					
//						//	System.out.println(LesTroisCaracteristiques+" a voir ");
//							
//							//LesTroisCaracteristique=LesTroisCaracteristiques.substring(1);
//							
//							
//							//System.out.println(LesTroisCaracteristique+"LesTroisCaracteristique");
//						}
//						}
//						else{
//							LesTroisCaracteristique=LesTroisCaracteristiques;
//						}
//						//System.out.println(LesTroisCaracteristique);
//						
//						int w=0;
//						Set<String> composantsplit= new HashSet<String>();
//						
//						for(String carrat: LesTroisCaracteristique.split("\\/")){
//							System.out.println("carrat carrat "+carrat);
//							w++;
//							System.out.println(w);
//							Matcher matcherpoint=patternExpressionPoint.matcher(carrat);
//							int njd=0;
//							while(matcherpoint.find()){
//								njd++;
//							}
//							if(carrat.endsWith("+")|carrat.endsWith("-")){
//								motfinissantParPlusoumoins.add(carrat);
//							}
//					
//							 if(njd==0){
//								
//								int visi=0;
//								Set<String> resultat= new HashSet<String>();
//								String preficecommun="";
//								String suffixeCommun=getSuffixes(carrat);
//								String LastsuffixeCommun=getLastSuffixes(carrat);
//								for(String carract:carrat.split("\\+|[\\s]&[\\s]")){
//									visi++;
//									HashMap<String, Set<String>> numeroCorection= new HashMap<String, Set<String>>();
//									//System.out.println("carract importnat "+carract);
//									if(visi==1){
//										resultat.add(carract);
//										composantsplit.add(carract);
//										//System.out.println("je veux savoir "+carract);
//										
//										Matcher espac=patternMotdulibbeleGraceAespace.matcher(carract);
//										int kl=0;
//										String codeee="";
//										while(espac.find()){
//											kl++;
//											
//											codeee=codeee+" "+espac.group(1);
//											
//											if(!numeroCorection.containsKey(Integer.toString(kl))){
//												numeroCorection.put(Integer.toString(kl), new HashSet<String>());
//											}
//											numeroCorection.get(Integer.toString(kl)).add(codeee);
//										}
//								
//										if(kl>1){
//										for(String ft:numeroCorection.get(Integer.toString(kl-1))){
//											//test
//											
//											preficecommun=preficecommun+" "+ft;
//											
//											
//										}
//										//System.out.println("preficecommun "+preficecommun);
//										}
//										if(!carrat.equals(carract)) {
//											System.out.println("carract +++"+carract);
//										System.out.println("carrat "+carrat);
//										
//										lesAnalytes.add(getStringEspaceCorrection(carrat));
//										lesAnalytes.add(getStringEspaceCorrection(carract));
//										if(!getStringEspaceCorrection(carract).equals(getStringEspaceCorrection(carrat))) {
//										lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrat), getStringEspaceCorrection(carract+" "+suffixeCommun), "a"));
//										
//										lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carract+" "+suffixeCommun), getStringEspaceCorrection(carract), "a"));
//										
//										System.out.println("test "+carrat+" double test "+carract);
//										}
//									}
//									}
//									else{
//										if (!carract.equals(LastsuffixeCommun)) {
//											//System.out.println("########preficecommun*"+preficecommun+" carract*"+carract+" suffixeCommun*"+suffixeCommun);
//										resultat.contains(preficecommun+" "+carract+" "+suffixeCommun);
//										composantsplit.add(preficecommun+" "+carract+" "+suffixeCommun);
//										lesAnalytes.add(getStringEspaceCorrection(carrat));
//										lesAnalytes.add(getStringEspaceCorrection(preficecommun+" "+carract+" "+suffixeCommun));
//										lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrat), getStringEspaceCorrection(preficecommun+" "+carract+" "+suffixeCommun), "a"));
//										lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(preficecommun+" "+carract+" "+suffixeCommun), getStringEspaceCorrection(preficecommun+" "+carract), "a"));
//										lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(preficecommun+" "+carract), getStringEspaceCorrection(preficecommun), "a"));
//										
//										//System.out.println("resutl : "+resultat);
//										}
//										else{
//											resultat.contains(preficecommun+" "+carract);
//											composantsplit.add(preficecommun+" "+carract);
//											lesAnalytes.add(getStringEspaceCorrection(carrat));
//											lesAnalytes.add(getStringEspaceCorrection(preficecommun+" "+carract));
//											lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrat), getStringEspaceCorrection(preficecommun+" "+carract), "a"));
//										
//										}
//									}
//									
//									
//									
//								}
//							}
//							else{
//								
//								int visi=0;
//								Set<String> ccaer1= new HashSet<String>();
//								Set<String> ccaer1Mot= new HashSet<String>();
//								Set<String> ccaer2= new HashSet<String>();
//								String echelle="";
//								int ech=1;
//								for(String carrate:carrat.split("\\.")){
//									visi++;
//									System.out.println("carrate point "+carrate);
//									int vs=0;
//									String preficecommun="";
//									String suffixeCommunDansPOint=getSuffixes(carrate);
//									
//									//System.out.println("*"+suffixeCommun+"*");
//									String lastSuffuxeCommun=getLastSuffixes(carrate);
//								
//									for(String carract:carrate.split("\\+|[\\s]&[\\s]")){
//										vs++;
//										
//
//										HashMap<String, Set<String>> numeroCorection= new HashMap<String, Set<String>>();
//										
//										if(vs==1){
//											//System.out.println("je veux savoir "+carract);
//											Matcher espac=patternMotdulibbeleGraceAespace.matcher(carract);
//											int kl=0;
//											String codeee="";
//											while(espac.find()){
//												kl++;
//												
//												codeee=codeee+" "+espac.group(1);
//												
//												
//												if(!numeroCorection.containsKey(Integer.toString(kl))){
//													numeroCorection.put(Integer.toString(kl), new HashSet<String>());
//												}
//												numeroCorection.get(Integer.toString(kl)).add(codeee);
//											}
//											
//											
//											if(kl>1){
//												for(String ft:numeroCorection.get(Integer.toString(kl-1))){
//													preficecommun=preficecommun+" "+ft;
//													
//												}
//												
//											}
//											//System.out.println("preficecommun "+preficecommun);
//											if(visi==1){
//												System.out.println("pourquoi "+carrate);
//												ccaer1.add(carrate);
//												ccaer1Mot.add(carrate);
//												if(!carrate.equals(carract))
//										//System.out.println("je veux savoir + "+carrate);
//										//System.out.println("je veux savoir + carract "+carract);
//										
//												lesAnalytes.add(getStringEspaceCorrection(carrate));
//												lesAnalytes.add(getStringEspaceCorrection(carract));
//												
//											if(!getStringEspaceCorrection(carract).equals(getStringEspaceCorrection(carract))){
//												lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrate), getStringEspaceCorrection(carract), "a"));
//												
//												//System.out.println(carrate+"ùùùùùùùùùùùùùùùùùùùù");
//											}
//											}
//											else {
//												//test
//												
//													if(suffixeCommunDansPOint != null && !suffixeCommunDansPOint.isEmpty()) {
//														if(echelle.isEmpty()) {
//															echelle=carract+" "+suffixeCommunDansPOint;
//														}
//														else {
//															echelle=echelle+" "+carract+" "+suffixeCommunDansPOint;
//														}
//														echelle=echelle+carract+" "+suffixeCommunDansPOint;
//														//ccaer2.add(carract+" "+suffixeCommunDansPOint);
//														System.out.println("echelle :" +ech+" : "+echelle);
//														String echel=Integer.toString(ech);
//														if(!echelleSubclass.containsKey(echel)) {
//															echelleSubclass.put(echel, echelle);
//														}
//														ech++;
//														ccaer2.add(echelle);
//														System.out.println("jn' "+echelle);
//														System.out.println("*"+suffixeCommunDansPOint+"*");
//													}
//													else {
//														System.out.println("jn "+carract);
//														//ccaer2.add(carract);
//														if(echelle.isEmpty()) {
//															echelle=carract;
//														}
//														else {
//															echelle=echelle+" "+carract;
//															//System.out.println(echelle);
//														}
//														System.out.println("echelle :" +ech+" : "+echelle);
//														String echel=Integer.toString(ech);
//														if(!echelleSubclass.containsKey(echel)) {
//															echelleSubclass.put(echel, echelle);
//														}
//														
//														ech++;
//														ccaer2.add(echelle);
//														System.out.println("jn' "+echelle);
//													}
//												
//												//ccaer2.add(carract+" "+suffixeCommunDansPOint);
//												//System.out.println("test +++ "+carract+suffixeCommun+ "ffffffffffffffffffffffffffff");
////												for(String atd: ccaer1Mot) {
////													System.out.println(atd+" "+preficecommun+" "+carract+" "+suffixeCommun);
////													lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(atd+" "+preficecommun+" "+carract+" "+suffixeCommun), getStringEspaceCorrection(atd+" "+preficecommun+" "+carract), "a"));
////													
////												}
//												
//											
//											}
//											
//										}
//										else{
//											if(visi==1){
//												//System.out.println(carrate+" a voir ");
//												
//												ccaer1.add(carrate);
//												if(carract.equals(lastSuffuxeCommun)) {
//													
//												lesAnalytes.add(getStringEspaceCorrection(carrate));
//												lesAnalytes.add(getStringEspaceCorrection(preficecommun+" "+carract));
//												lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrate), getStringEspaceCorrection(preficecommun+" "+carract), "a"));
//												
//												ccaer1Mot.add(carrate);
//												}else {
//													
//													lesAnalytes.add(getStringEspaceCorrection(carrate));
//													if(suffixeCommunDansPOint != null&&!suffixeCommunDansPOint.isEmpty()) {
//														lesAnalytes.add(getStringEspaceCorrection(preficecommun+" "+carract+" "+suffixeCommunDansPOint));
//														lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrate), getStringEspaceCorrection(preficecommun+" "+carract+" "+suffixeCommunDansPOint), "a"));
//														
//													}
//													else {
//														lesAnalytes.add(getStringEspaceCorrection(preficecommun+" "+carract));
//														lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrate), getStringEspaceCorrection(preficecommun+" "+carract), "a"));
//														
//													}
//													//lesAnalytes.add(getStringEspaceCorrection(preficecommun+" "+carract+" "+suffixeCommun));
//													//lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrate), getStringEspaceCorrection(preficecommun+" "+carract+" "+suffixeCommun), "a"));
//													
//													ccaer1Mot.add(carrate);
//												
//													
//												}
//											}
//											else {
//												if(!carract.equals(lastSuffuxeCommun)) {
//													if(suffixeCommunDansPOint!=null && !suffixeCommunDansPOint.isEmpty()) {
//														ccaer2.add(preficecommun+" "+carract+" "+suffixeCommunDansPOint);
//													}
//													else {
//														ccaer2.add(preficecommun+" "+carract);
//													}
//												
//												}
//												else {
//													ccaer2.add(preficecommun+" "+carract);
//													
//												}
//											}
//										}
//										
//										
//										
//									
//									}
//								
//								}
//								
//								for(String a:ccaer1){
//									System.out.println("ccaer2 "+ccaer2);
//									for(String b:ccaer2){
//										String ab=a+" "+b;
//										composantsplit.add(ab);
//										
//										
//										lesAnalytes.add(getStringEspaceCorrection(ab));
//										lesAnalytes.addAll(getStringespaceCorrectionListe(ccaer1Mot));
//										
//										lesAnalytes.add(getStringEspaceCorrection(ab));
//										lesAnalytes.add(getStringEspaceCorrection(carrat));
//										System.out.println(" trois entités 1 carrat "+carrat);
//										System.out.println("trois entités  2 ab " +ab);
//										System.out.println("trois entités  3 ccaer1Mot " +ccaer1Mot);
//										System.out.println("++++++++++++++++++");
//										if(!getStringEspaceCorrection(carrat.replace(".", " ")).equals(ab)) {
//											//System.out.println("dddddddddddddd "+carrat.replace(".", " "));
//										lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrat), getStringEspaceCorrection(ab), "a"));
//										System.out.println(" trois entités 1 "+carrat);
//										System.out.println("trois entités  2 " +ab);
//										System.out.println("trois entités  3 " +ccaer1Mot);
//										System.out.println(ccaer2);
//										}
//										int ap=1;
//										for(String ky:echelleSubclass.keySet()) {
//											if(getStringEspaceCorrection(b).equals(getStringEspaceCorrection(echelleSubclass.get(ky)))) {
//												ap=Integer.parseInt(ky);
//											}
//										}
//										for(String ky:echelleSubclass.keySet()) {
//											int aps=Integer.parseInt(ky);
//											if(aps<ap) {
//												String abf=a+" "+echelleSubclass.get(ky);
//												lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(ab), getStringEspaceCorrection(abf), "a"));
//												
//												
//											}
//										}
//										String resultatSufixe=getSuffixes(carrat);
//										String abd =ab.split(resultatSufixe)[0];
//										//System.out.println(resultatSufixe+"resultatSufixe");
//										
//										if(!resultatSufixe.isEmpty()) {
//											//System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//										lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(abd), getStringespaceCorrectionListe(ccaer1Mot), "a"));
//										lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(ab), getStringEspaceCorrection(abd), "a"));
//										}
//										else {
//											//System.out.println("try");
//											lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(ab), getStringespaceCorrectionListe(ccaer1Mot), "a"));
//											//System.out.println("ccaer1Mot"+ab);
//										}
//										//System.out.println("ab ab "+ab.substring(0, ab.length()-lastSuffuxeCommun.length()));
//										
//										
//									}
//								}
//								if(ccaer2.isEmpty()){
//									if(!ccaer1.isEmpty()){
//										composantsplit.addAll(ccaer1);
//										
//							lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringespaceCorrectionListe(ccaer1), getStringespaceCorrectionListe(ccaer1Mot), "a"));
//									
//										lesAnalytes.addAll(getStringespaceCorrectionListe(ccaer1));
//										lesAnalytes.addAll(getStringespaceCorrectionListe(ccaer1Mot));
//								
//							lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrat), getStringespaceCorrectionListe(ccaer1), "a"));
//										
//										lesAnalytes.add(getStringEspaceCorrection(carrat));
//										
//										}
//								}
//								else if(ccaer1.isEmpty()){
//									if(!ccaer2.isEmpty()){
//										composantsplit.addAll(ccaer2);
//										lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringespaceCorrectionListe(ccaer2), getStringespaceCorrectionListe(ccaer1Mot), "a"));
//										
//										
//										lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrat), getStringespaceCorrectionListe(ccaer2), "a"));
//										
//										
//										lesAnalytes.addAll(getStringespaceCorrectionListe(ccaer2));
//										lesAnalytes.addAll(getStringespaceCorrectionListe(ccaer1Mot));
//										lesAnalytes.add(getStringEspaceCorrection(carrat));
//									}
//								}
//								
//								
//							}
//						
//							if(!motfinissantParPlusoumoins.isEmpty()){
//							
//								for(String code: composantsplit){
//									int vuio =0;
//									Matcher matchnegation=patternExpressionNegation.matcher(code);
//									int iuo=0;
//									while(matchnegation.find()){
//										iuo++;
//									}
//									if(iuo>0){
//										String preficeNegation="";
//										
//									for(String carrrt:code.split("-")){
//										vuio++;
//										HashMap<String, Set<String>> numeroCorectionNegation= new HashMap<String, Set<String>>();
//																				
//										if(vuio<=iuo&&vuio==1){
//											Matcher espac=patternMotdulibbeleGraceAespace.matcher(carrrt);
//											int kl=0;
//											String codeee="";
//											while(espac.find()){
//												kl++;
//												codeee=codeee+" "+espac.group(1);
//											
//												
//												if(!numeroCorectionNegation.containsKey(Integer.toString(kl))){
//													numeroCorectionNegation.put(Integer.toString(kl), new HashSet<String>());
//												}
//												numeroCorectionNegation.get(Integer.toString(kl)).add(codeee);
//											}
//										
//											if(kl>1){
//											for(String ft:numeroCorectionNegation.get(Integer.toString(kl-1))){
//												preficeNegation=preficeNegation+" "+ft;
//												
//											}
//											}
//											
//										
//											lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(carrrt), getStringEspaceCorrection(code), "b"));
//											
//											
//											lesAnalytes.add(getStringEspaceCorrection(carrrt));
//											lesAnalytes.add(getStringEspaceCorrection(code));
//										}
//										
//										else if(vuio<=iuo){
//											
//											
//											lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(preficeNegation+" "+carrrt), getStringEspaceCorrection(code), "b"));
//											
//											lesAnalytes.add(getStringEspaceCorrection(code));
//											lesAnalytes.add(getStringEspaceCorrection(preficeNegation+" "+carrrt));
//										}
//										else{
//											lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(code),getStringEspaceCorrection(preficeNegation+" "+carrrt), "a"));
//											
//											lesAnalytes.add(getStringEspaceCorrection(code));
//											lesAnalytes.add(getStringEspaceCorrection(preficeNegation+" "+carrrt));
//										}
//										
//									}
//								}
//								}
//							}
//					
//						}
//						
//						if(v==1){
//							caract1.addAll(composantsplit);
//							//System.out.println("composantsplit "+composantsplit);
//						}
//						else{
//							caract2.addAll(composantsplit);
//						}
//						
//					//look for this ==>  and open the angle for(String LesTroisCaracteristiques:composants.split("(,[\\s])")){ //}
//					//
//					//System.out.println("caract1 "+caract1);
//					//System.out.println("caract2 "+caract2);
//					for(String a:caract1){
//						for(String b:caract2){
//							String ab=a+" "+b;
//							if(v==1){
//								lesComposantAnalyste.get(Integer.toString(i)).add(ab);
//								
//								lesAnalytes.add(getStringEspaceCorrection(composants));
//			lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(composants), getStringEspaceCorrection(ab), "a"));
//								
//								lesAnalytes.add(getStringEspaceCorrection(ab));
//							}
//							else{
//								lesComposantAnalyste.get(Integer.toString(i)).add(ab);
//								
//								lesAnalytesHierarchie=getConcatenationdeHasmap(lesAnalytesHierarchie, getRelation(getStringEspaceCorrection(composants), getStringEspaceCorrection(ab), "a"));
//								
//								lesAnalytes.add(getStringEspaceCorrection(composants));
//								lesAnalytes.add(getStringEspaceCorrection(ab));
//							}
//							
//							
//							
//						}
//					}
//					if(caract2.isEmpty()){
//						if(!caract1.isEmpty()){
//							if(v==1){
//								lesComposantAnalyste.get(Integer.toString(i)).addAll(caract1);
//								lesAnalytes.addAll(getStringespaceCorrectionListe(caract2));
//								
//							}
//							else{
//								lesComposantAnalyste.get(Integer.toString(i)).addAll(caract1);
//								lesAnalytes.addAll(getStringespaceCorrectionListe(caract2));
//								
//							}
//							}
//					}
//					else if(caract1.isEmpty()){
//						if(!caract2.isEmpty()){
//							if(v==1){
//								lesComposantAnalyste.get(Integer.toString(i)).addAll(caract2);
//								lesAnalytes.addAll(getStringespaceCorrectionListe(caract2));
//								
//							}
//							else{
//								lesComposantAnalyste.get(Integer.toString(i)).addAll(caract2);
//								lesAnalytes.addAll(getStringespaceCorrectionListe(caract2));
//								
//							}
//						}
//					}
//					
//				}
//				else{
//					if(!lesComposantAnalyste.containsKey(Integer.toString(i))){
//						lesComposantAnalyste.put(Integer.toString(i), new HashSet<String>());
//					}
//					lesComposantAnalyste.get(Integer.toString(i)).add(composants);
//					lesAnalytes.add(getStringEspaceCorrection(composants));
//				}
//				
//				
//				lesAnalytes.add(getStringEspaceCorrection(composants));
//				
//				
//			}
//			else if(i==2){
//				if(!lesComposantChallenge.containsKey(Integer.toString(i))){
//					lesComposantChallenge.put(Integer.toString(i), new HashSet<String>());
//				}
//				lesComposantChallenge.get(Integer.toString(i)).add(composants);
//		
//			}
//			else if(i==3){
//				if(!lesComposantAjustement.containsKey(Integer.toString(i))){
//					lesComposantAjustement.put(Integer.toString(i), new HashSet<String>());
//				}
//				lesComposantAjustement.get(Integer.toString(i)).add(composants);
//			}
//			else{
//				System.out.println("erreur");
//			}
//		
//		}
//	
//		
//		for(String a:lesComposantAnalyste.keySet()){
//			if(!lesComposant.containsKey(a)){
//				lesComposant.put(a, new HashSet<String>());
//				
//			}
//			lesComposant.get(a).addAll(lesComposantAnalyste.get(a));
//			//System.out.println("a +++"+lesComposantAnalyste.get(a));
//		}
//		for(String a:lesComposantAjustement.keySet()){
//			if(!lesComposant.containsKey(a)){
//				lesComposant.put(a, new HashSet<String>());
//				
//			}
//			lesComposant.get(a).addAll(lesComposantAjustement.get(a));
//			
//		}
//		for(String a:lesComposantChallenge.keySet()){
//			if(!lesComposant.containsKey(a)){
//				lesComposant.put(a, new HashSet<String>());
//				
//			}
//			lesComposant.get(a).addAll(lesComposantChallenge.get(a));
//			
//		}
//		
//		for( String cp:lesAnalytesHierarchie.keySet()) {
//			System.out.println(cp);
//			for(Couple cpt:lesAnalytesHierarchie.get(cp)) {
//				System.out.println(cpt.x+"   hdhd  "+cpt.y);
//			}
//		};
//	
//	
//	 return lesAnalytesHierarchie;
	}

	public static String getStringEspaceCorrection(String carract){
		String ExpressionMotGraceAuxEspaces="([^\\s]+\\.*)";
		Pattern patternMotdulibbeleGraceAespace= Pattern.compile(ExpressionMotGraceAuxEspaces);
		Matcher espac=patternMotdulibbeleGraceAespace.matcher(carract);
		int kl=0;
		String codeee="";
		while(espac.find()){
			kl++;
			if(kl==1){
				codeee=espac.group(1);
			}
			else{
				codeee=codeee+" "+espac.group(1);
			}
			
		}
		return codeee;
	}
	

	public static Set<String> getStringespaceCorrectionListe(Set<String> liste){
		Set<String> dree= new HashSet<String>();
		String ExpressionMotGraceAuxEspaces="([^\\s]+\\.*)";
		Pattern patternMotdulibbeleGraceAespace= Pattern.compile(ExpressionMotGraceAuxEspaces);
		for(String carract:liste){
			Matcher espac=patternMotdulibbeleGraceAespace.matcher(carract);
			int kl=0;
			String codeee="";
			while(espac.find()){
				kl++;
				if(kl==1){
					codeee=espac.group(1);
				}
				else{
					codeee=codeee+" "+espac.group(1);
				}
				
			}
			dree.add(codeee);
		}
		return dree;
		
	}
	public static HashMap<String, Set<Couple>> getRelation(String codeA, String codeB, String relation){
		//System.out.println("voici a "+codeA+" et voici b "+codeB);
		HashMap<String, Set<Couple>> description= new HashMap<String, Set<Couple>>();
		if(!description.containsKey(codeA)){
			description.put(codeA, new HashSet<Couple>());
		}
		if(relation.equals("a")){
			Couple ghj=new Couple("ISa",codeB);
			description.get(codeA).add(ghj);
		}else{
			description.get(codeA).add(new Couple("ISNota",codeB));
		}
		
		return description;
	}
	public static HashMap<String, Set<Couple>> getRelation(Set<String> Liste, String codeB, String relation){
		HashMap<String, Set<Couple>> description= new HashMap<String, Set<Couple>>();
		for(String codeA:Liste){
			if(!description.containsKey(codeA)){
				description.put(codeA, new HashSet<Couple>());
			}
			if(relation.equals("a")){
				description.get(codeA).add(new Couple("ISa",codeB));
			}else{
				description.get(codeA).add(new Couple("ISNota",codeB));
			}
		}
		
		
		return description;
	}
	public static HashMap<String, Set<Couple>> getRelation(Set<String> Liste, Set<String> liste2, String relation){
		HashMap<String, Set<Couple>> description= new HashMap<String, Set<Couple>>();
		for(String codeA:Liste){
			if(!description.containsKey(codeA)){
				description.put(codeA, new HashSet<Couple>());
			}
			if(relation.equals("a")){
				for(String codeB: liste2){
					description.get(codeA).add(new Couple("ISa",codeB));
				}
				
			}else{
				for(String codeB: liste2){
					description.get(codeA).add(new Couple("ISNota",codeB));
				}
				
			}
		}
		
		
		return description;
	}
	
	
	
	
	public static HashMap<String, Set<Couple>> getRelation(String codeA, Set<String> Liste, String relation){
		HashMap<String, Set<Couple>> description= new HashMap<String, Set<Couple>>();
		if(!description.containsKey(codeA)){
			description.put(codeA, new HashSet<Couple>());
		}
		if(relation.equals("a")){
			for(String codeB:Liste){
				description.get(codeA).add(new Couple("ISa",codeB));
			}
			
		}else{
			for(String codeB:Liste){
				description.get(codeA).add(new Couple("ISNota",codeB));
			}
		}
		
		return description;
	}
	public static HashMap<String,Set<Couple>> getConcatenationdeHasmap(HashMap<String, Set<Couple>> a, HashMap<String, Set<Couple>> b){
		HashMap<String, Set<Couple>> ab= new HashMap<>();
		for(String lib:a.keySet()){
			Set<Couple> coup= new HashSet<Couple>();
			coup.addAll(a.get(lib));
			if(b.containsKey(lib)){
				coup.addAll(b.get(lib));
			}
			if(!ab.containsKey(lib)){
				ab.put(lib, new HashSet<Couple>());
			}
			ab.get(lib).addAll(coup);
		}
		
		for(String lib:b.keySet()){
			Set<Couple> coup= new HashSet<Couple>();
			coup.addAll(b.get(lib));
			if(a.containsKey(lib)){
				coup.addAll(a.get(lib));
			}
			if(!ab.containsKey(lib)){
				ab.put(lib, new HashSet<Couple>());
			}
			ab.get(lib).addAll(coup);
		}
		return ab;
	}
	
	public static String  getLastSuffixes(String libelle) {

		Map<String, Set<String>> suffixeTestt= new HashMap<String, Set<String>>();
		
		
		
		//System.out.println("la chaine avant le split : " + libelle);
		String[] tabChaines = libelle.split("\\+|[\\s]&[\\s]");
		int longueur = tabChaines.length;
		if (longueur > 0)
		{
			//System.out.println("les sous-chaines après le split : ");
			for (int i=0 ; i < longueur ; i++)
			{
			//	System.out.println("tab[" + i + "] = "+ tabChaines[i]);
				if(!suffixeTestt.containsKey(Integer.toString(i))) {
					suffixeTestt.put(Integer.toString(i), new HashSet<String>());
				}
				suffixeTestt.get(Integer.toString(i)).add(tabChaines[i]);
			}
		}
		//System.out.println(suffixeTestt);
		String alpha="";
		int voir=0;
		for(String je :suffixeTestt.keySet()) {
			int jev=Integer.valueOf(je);
			//System.out.println(jev);
			if(jev>=voir){
				for(String lib:suffixeTestt.get(je) ) {
					alpha=lib;
				}
				voir=jev;
				
			}
			
		}
		//System.out.println("voir "+voir+" alpha "+alpha);
		
		
		//System.out.println("resut *"+resut+"*");
		return alpha;
		
	
	}
	
	
	public static String  getSuffixes(String libelle) {

		Map<String, Set<String>> suffixeTestt= new HashMap<String, Set<String>>();
		
		
		
		//System.out.println("la chaine avant le split : " + libelle);
		String[] tabChaines = libelle.split("\\+|[\\s]&[\\s]");
		int longueur = tabChaines.length;
		//System.out.println(longueur);
		if (longueur > 1)
		{
			//System.out.println("les sous-chaines après le split : ");
			for (int i=0 ; i < longueur ; i++)
			{
				//System.out.println("tab[" + i + "] = "+ tabChaines[i]);
				if(!suffixeTestt.containsKey(Integer.toString(i))) {
					suffixeTestt.put(Integer.toString(i), new HashSet<String>());
				}
				suffixeTestt.get(Integer.toString(i)).add(tabChaines[i]);
			}
		}
		//System.out.println(suffixeTestt);
		String alpha="";
		int voir=0;
		for(String je :suffixeTestt.keySet()) {
			int jev=Integer.valueOf(je);
			//System.out.println(jev);
			if(jev>=voir){
				for(String lib:suffixeTestt.get(je) ) {
					alpha=lib;
				}
				voir=jev;
				
			}
			
		}
	//	System.out.println("voir "+voir+" alpha "+alpha);
		
		String resut="";
		String[] tabSufuxe = alpha.split(" ");
		int longueurSplit = tabSufuxe.length;
		//System.out.println();
		if (longueurSplit > 0)
		{
			for (int i=0 ; i < longueurSplit ; i++)
			{
				if(i>0) {
					resut=resut+tabSufuxe[i]+" ";
				}
			}
		}
		//System.out.println("resut *"+resut+"*");
		return resut;
		
	
	}
}
