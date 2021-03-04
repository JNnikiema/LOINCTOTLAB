package loincMotsRattachement;

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
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.util.Version;
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
import utilitaries.LibelleAvecEspaceCorrecte;

public class LoincMotsCreation {

	public static void main(String[] args) throws SQLException, ParseException, RepositoryException, QueryEvaluationException, MalformedQueryException {
		// TODO Auto-generated method stub
		Set<String> StopWordFrancais= new HashSet<String>();
		String requ="select* from stopword";
		ConnexionBD jnnn= new ConnexionBD(11);
		Connection connStopword=jnnn.getconn();
		PreparedStatement stowww = connStopword.prepareStatement(requ);
		ResultSet stopworde = stowww.executeQuery();
		while(stopworde.next()){
			StopWordFrancais.add(stopworde.getString(1));
		}
		//String namespace="LOINCTotal";
		String namespace = "TLABANDLOINC";
		//String namespace="FrenchLOINC";
		
		
		Repository repo = RepositoryFactory.getRepository(namespace);
		RepositoryConnection conn = repo.getConnection();
		String querylabels=getLabelInLOINC();
		Set<String>Labels=getresult(conn, querylabels);
		HashMap<String, Set<String>> codeandLabels= getresultLabels(conn, querylabels);
		String prefixe="http://erias.org/loincTotal#";
		ValueFactory vf = conn.getValueFactory();
		
		Set<String>MotTotal=getListeMotsLoinc(Labels, StopWordFrancais);
		HashMap<String, Set<String>> lesMotsDeLaLOINC=getDescription(prefixe, MotTotal, "LNCWORD");
		
		Set<Statement> getDescriptionrdf=getDescriptionMot(prefixe, lesMotsDeLaLOINC, codeandLabels, vf);
		String Prefix_rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		String Prefix_rdfs="http://www.w3.org/2000/01/rdf-schema#";	
		
		for(String codeMot:lesMotsDeLaLOINC.keySet()){
			for(String libelleMot:lesMotsDeLaLOINC.get(codeMot)){
				Statement stm1 = vf.createStatement( vf.createURI(codeMot),
						vf.createURI(Prefix_rdf + "type"),
						vf.createURI("http://erias.org/LOINC#Mot2"));
				getDescriptionrdf.add(stm1);
				Statement stmlabel = vf.createStatement(vf.createURI(codeMot),
						vf.createURI(Prefix_rdfs + "label"),
						vf.createLiteral (libelleMot));
				getDescriptionrdf.add(stmlabel);
			}
		}
		
		String GraphLoincFrancais = "http://erias.org/FrenchLoinc";
		
		URI graphLoincFrancais = conn.getValueFactory().createURI(GraphLoincFrancais);
		List<Statement> liste= new ArrayList<Statement>(getDescriptionrdf);
		

		try {	

			conn.add(liste,graphLoincFrancais);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("final terminé");

	}
	
	public static Set<Statement> getDescriptionMot(String prefixe,HashMap<String, Set<String>> Mots, HashMap<String, Set<String>> ListeLibelleCOde, ValueFactory vf) throws ParseException{
		Set<Statement> DescriptionMot= new HashSet<Statement>();
		String ExpressionMotGraceAuxEspaces="([^\\s]+\\.*)";
		Pattern patternMotdulibbeleGraceAespace= Pattern.compile(ExpressionMotGraceAuxEspaces);
	//	String Prefix_rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		for(String code : ListeLibelleCOde.keySet()){
			for(String libelle:ListeLibelleCOde.get(code)){
				String libeJN = StringUtils.stripAccents(libelle).toLowerCase();
				String libelles =libelleSansLescaracteristiquesDeRegex(libeJN);
				
				for(String codlib:libelles.split("(\\+|,|\\.|\\^|:)")){
					for(String libee:codlib.split("\\(|\\)|\\/")){
						
						Matcher matcheurMotgraceAes= patternMotdulibbeleGraceAespace.matcher(libee);
						
						while(matcheurMotgraceAes.find()){
							
							String motNiveau2=matcheurMotgraceAes.group(1);
							
							String motNiveau1 = LibelleAvecEspaceCorrecte.getLibelleCorrect(motNiveau2);
							String mots ="";
							if(motNiveau1.length()>1){
								motNiveau1=motNiveau1.replace("\"", "'");
							 mots =stemTerm(motNiveau1);
							}
							else{
								 mots =motNiveau1;
							}
							Matcher matcheurMotgraceAespac= patternMotdulibbeleGraceAespace.matcher(mots);
							while(matcheurMotgraceAespac.find()){
								String mottt= matcheurMotgraceAespac.group(1);
								String abreviationP="";
								if(mottt.startsWith(":")&&mottt.length()>1){
									abreviationP=mottt.substring(1);
									for(String codeMot:Mots.keySet()){
										if(Mots.get(codeMot).contains(abreviationP)){
											Statement stm = vf.createStatement( vf.createURI(code),
													vf.createURI(prefixe+"has_word"),
													vf.createURI(codeMot));
											DescriptionMot.add(stm);
											
										}
									
									
									}
								}
								else{
									abreviationP=mottt;
									for(String codeMot:Mots.keySet()){
										if(Mots.get(codeMot).contains(abreviationP)){
											Statement stm = vf.createStatement( vf.createURI(code),
													vf.createURI(prefixe+"has_word"),
													vf.createURI(codeMot));
											DescriptionMot.add(stm);
											
										}
									
									
									}
								}
							}
							
							//String abreviationP=matcheurMotgraceAes.group(1);
							
						}
						
						
					}
				}
				
			}
		}
			
		
		return DescriptionMot;
	}
	public static HashMap<String, Set<String>> getDescription(String prefixe, Set<String> listecaracteristiques, String description){
		int i =1500;
		
		HashMap<String, Set<String>> descriptionresult= new HashMap<String, Set<String>>();
		
		for(String lib: listecaracteristiques){
			//System.out.println(prefix+i);
			i++;
			if(lib.length()>0){
			if(!descriptionresult.containsKey(prefixe+description+i)){
				descriptionresult.put(prefixe+description+i, new HashSet<String>());
			}
			descriptionresult.get(prefixe+description+i).add(lib);
			}
			else{
				System.out.println("llllllllllllll +++++++++"+lib);
			}
			
			
			
		
	}
	
		
		return descriptionresult;
		
	}
	public static Set<String> getresult(RepositoryConnection conn, String query) throws QueryEvaluationException, RepositoryException, MalformedQueryException{
		Set<String> resultat= new HashSet<String>();
		ArrayList<BindingSet> ListeRDFTotal= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query));
		for(BindingSet e: ListeRDFTotal){
			resultat.add(e.getValue("o").stringValue());
		}
	return resultat;
	}
	
	public static HashMap<String,Set<String>> getresultLabels(RepositoryConnection conn, String query) throws QueryEvaluationException, RepositoryException, MalformedQueryException{
		HashMap<String,Set<String>> resultat= new HashMap<String, Set<String>>();
		ArrayList<BindingSet> ListeRDFTotal= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query));
		for(BindingSet e: ListeRDFTotal){
			if(!resultat.containsKey(e.getValue("s").stringValue())) {
			resultat.put(e.getValue("s").stringValue(), new HashSet<String>());
			}
			resultat.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());
		}
	return resultat;
	}
	
	public static String getLabelInLOINC() {
		String query="prefix skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" + 
				"				 Select distinct ?s ?o WHERE { \n" + 
				"				\n" + 
				"                 GRAPH <http://erias.org/FrenchLoinc> {\n" + 
				"                 \n" + 
				"				?s rdfs:label ?o.\n" + 
				"                   \n" + 
				"                   \n" + 
				"                   \n" + 
				"				 }\n" + 
				"                       \n" + 
				"				 } ";
		return query;
	}
	
	public static Set<String> getListeMotsLoinc(Set<String>listeDesParties, Set<String>StopWordFrancais) throws ParseException{

		String ExpressionMotGraceAuxEspaces="([^\\s]+\\.*)";
		Pattern patternMotdulibbeleGraceAespace= Pattern.compile(ExpressionMotGraceAuxEspaces);
		Set<String> motTerminologie= new HashSet<String>();
	
		System.out.println(listeDesParties.size());
		for(String libe: listeDesParties){
			String libs = StringUtils.stripAccents(libe).toLowerCase();
			String lib =libelleSansLescaracteristiquesDeRegex(libs);
			
			
			for(String codlib:lib.split("(\\+|,|\\.|\\^|:)")){
				//System.out.println("??????!!!!!!"+codlib+" "+lib);
				for(String libee:codlib.split("\\(|\\)|\\/")){
					//System.out.println(libee);
					Matcher matcheurMotgraceAes= patternMotdulibbeleGraceAespace.matcher(libee);
					
					while(matcheurMotgraceAes.find()){
						
						String motNiveau2=matcheurMotgraceAes.group(1);
						
						String motNiveau1 = LibelleAvecEspaceCorrecte.getLibelleCorrect(motNiveau2);
						String mots ="";
						if(motNiveau1.length()>1){
							System.out.println("motNiveau1 *"+motNiveau1);
							motNiveau1=motNiveau1.replace("\"", "'");
							System.out.println("motNiveau1 "+motNiveau1);
						 mots =stemTerm(motNiveau1);
						}
						else{
							 mots =motNiveau1;
						}
						Matcher matcheurMotgraceAespac= patternMotdulibbeleGraceAespace.matcher(mots);
						while(matcheurMotgraceAespac.find()){
							String mottt= matcheurMotgraceAespac.group(1);
							String abreviationP="";
							if(mottt.startsWith(":")&&mottt.length()>1){
								abreviationP=mottt.substring(1);
								motTerminologie.add(abreviationP);
								System.out.println(abreviationP);
							}
							else{
								abreviationP=mottt;
								motTerminologie.add(abreviationP);
								System.out.println(abreviationP);
							}
						}
						
							
					}
					
					
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
			if(motssansMotvide.length()>1&&!patternLesChiffres.matcher(motssansMotvide).matches()){
				lesMotsansChiffreniLettreDeMoinsDuneLettreTerminologi.add(motssansMotvide);
			}
			
		}
		
		return lesMotsansMotVideTerminologi;
		//return lesMotsansChiffreniLettreDeMoinsDuneLettreTerminologi;
	
	
	}
	
	public static String libelleSansLescaracteristiquesDeRegex(String term){
		String LibelleSansAc= term.replaceAll("\\(", "« ");
		String LibelleSansAct= LibelleSansAc.replaceAll("\\{", "& ");
		String LibelleSansAcs= LibelleSansAct.replaceAll("\\}", "; ");
		String LibelleSansAcsP= LibelleSansAcs.replaceAll("\\*", "§ ");
		String LibelleSansAcsPP= LibelleSansAcsP.replaceAll("\\?", " , ");
		String LibelleSansAcsPPP= LibelleSansAcsPP.replaceAll("\\[", " @ ");
		String LibelleSansAcsPPPP= LibelleSansAcsPPP.replaceAll("\\]", " % ");
		String LibelleSansAcsPPPPP= LibelleSansAcsPPPP.replaceAll("\\+", " £ ");
		String LibelleSansAccent= LibelleSansAcsPPPPP.replaceAll("\\)", " » ");
		return LibelleSansAccent;
	}
	public static String stemTerm(String MotARaciniser) throws ParseException{
		FrenchAnalyzer francais= new FrenchAnalyzer(Version.LUCENE_CURRENT);
						
		QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, " ", francais);
		//System.out.println("MotARaciniser "+MotARaciniser);
		 String hmots =parser.parse(MotARaciniser).toString();
		 //System.out.println(" qupi *"+hmots);
		 String mots="";
		 if(hmots.startsWith(" :\"")){
			 if(hmots.endsWith("\"")){
				 mots=hmots.substring(3,hmots.length()-1);
			 }
			 else{
				 mots=hmots.substring(3);
			 }
			  
			 
		 }
		 else if (hmots.startsWith(" :")){
			 if(hmots.endsWith("\"")){
				 mots=hmots.substring(2,hmots.length()-1);
			 }
			 else{
				 mots=hmots.substring(2);
			 }
		 }
		 else if(hmots.startsWith(":")){
			 if(hmots.endsWith("\"")){
				 mots=hmots.substring(1,hmots.length()-1);
			 }
			 else{
				 mots=hmots.substring(1);
			 }
		 }
		 
		 
	
		return mots; 
	}

}
