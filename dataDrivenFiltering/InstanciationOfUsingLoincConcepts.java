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

import org.coode.owlapi.obo.parser.InverseHandler;
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
import utilitaries.Couple;
import utilitaries.LibelleAvecEspaceCorrecte;

public class InstanciationOfUsingLoincConcepts {

	public static void main(String[] args) throws SQLException, RepositoryException, QueryEvaluationException, MalformedQueryException {
		// TODO Auto-generated method stub
		String namespace = "TLABANDLOINC";
		Repository repo = RepositoryFactory.getRepository(namespace);
		RepositoryConnection conn = repo.getConnection();
		ValueFactory vf = conn.getValueFactory();
		
		Set<DataStructure> annotationInstanciated=getResultInstanciate();
		//Set<String> annotationTotal=getFirstResult();
		Set<String> Units = new HashSet<String>();
		Set<String> UnitsCorrect = new HashSet<String>();
		for(DataStructure annUnit:annotationInstanciated) {
				String unité=annUnit.Units.trim();
				if(!unité.equals("UNITS_CD")) {
				Units.add(annUnit.Units.trim());
				}
			
		}
		HashMap<String, String> unitAndCorrection= new HashMap<String, String>();
		HashMap<String, Set<String>> CorrectionandUnit= new HashMap<String, Set<String>>();
		System.out.println("Units "+Units.size());
		
		for(String unit: Units) {
			
			String UniteCOrrige=LibelleAvecEspaceCorrecte.getLibelleCorrect(unit);
			String libl=UniteCOrrige.replace(" / ", "/");
			String libell=libl.replace("/ ", "/");
			String libele=libell.replace("*", "^");
			//String libelle="";
			int i=0;
			//System.out.println("libele "+libele);
			
			if(libele.endsWith("Cr")) {
				//System.out.println(libele);
				String lib= libele.replace("Cr", " Cre");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				i++;
				//UnitsCorrect.add(libelle);
			}
			if(libele.endsWith("cre")) {
				//System.out.println(libele);
				String lib= libele.replace("cre", " Cre");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				i++;
				//UnitsCorrect.add(libelle);
			}
			if(libele.endsWith("cr")) {
				//System.out.println(libele);
				String lib= libele.replace("cr", " Cre");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				i++;
				//UnitsCorrect.add(libelle);
			}
			if(libele.contains("UI")) {
				String lib= libele.replace("UI", "IU");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				i++;
				//UnitsCorrect.add(libelle);
			}
			if(libele.contains("µ")) {
				//System.out.println(libele);
				String lib= libele.replace("µ", "u");
				 libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
					//UnitsCorrect.add(libelle);
			}
			if(libele.contains("mn")) {
				//System.out.println(libele);
				String lib= libele.replace("mn", "min");
				 libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			
					//UnitsCorrect.add(libelle);
			}
			if(libele.contains("mm3")) {
				//System.out.println(libele);
				String lib= libele.replace("mm3", "mm^3");
				 libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("°")) {
				//System.out.println(libele);
				String lib= libele.replace("°", "");
				 libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("cp")) {
				//System.out.println(libele);
				String lib= libele.replace("cp", "Copies");
				 libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("ml/dl")) {
				//System.out.println(libele);
				String lib= libele.replace("ml/", "cc/");
				 libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("Giga")) {
				//System.out.println(libele);
				String lib= libele.replace("Giga", "10^9");
				 libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("mcg")) {
				//System.out.println(libele);
				String lib= libele.replace("mcg", "ug");
				 libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("G/L")) {
				//System.out.println(libele);
				String lib= libele.replace("G/L", "10^9/L");
				 libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.equals("G/l")) {
				System.out.println(libele);
				String lib= libele.replace("G/l", "10^9/L");
				 libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("T/L")) {
				//System.out.println(libele);
				String lib= libele.replace("T/L", "10^6/L");
				 libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("ml/min/1,73m²")) {
				//System.out.println(libele);
				String lib=libele.replace("ml/min/1,73m²", "ml/min/1.73m2");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				
				 i++;
			}
			if(libele.contains("mmol/l ²")) {
				//System.out.println(libele);
				String lib=libele.replace("mmol/l ²", "mmol/l^2");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains(" /24h")) {
				//System.out.println(libele);
				String lib=libele.replace(" /24h", "/24h");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("mmol/24 h")) {
				//System.out.println(libele);
				String lib=libele.replace("mmol/24 h", "mmol/24h");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("mmol/mmolCR")) {
				//System.out.println(libele);
				String lib=libele.replace("mmol/mmolCR", "mmol/mmol Cre");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("giga/l")) {
				//System.out.println(libele);
				String lib=libele.replace("giga/l", "10^9/L");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("Copies/gr")) {
				//System.out.println("gggggggggggggggggggggggggggggggggggggggggggggggg"+libele);
				String lib=libele.replace("Copies/gr", "Copies/g");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("cop/ml")) {
				//System.out.println(libele);
				String lib=libele.replace("cop/ml", "Copies/mL");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("/l")) {
				//System.out.println("libele"+libele);
				String lib=libele.replace("/l", "/L");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("/ml")) {
				//System.out.println("libele"+libele);
				String lib=libele.replace("/ml", "/mL");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("ml")) {
				//System.out.println("libele :"+libele);
				String lib=libele.replace("ml", "mL");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("dl")) {
				//System.out.println("libele :"+libele);
				String lib=libele.replace("dl", "dL");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("fl")) {
				//System.out.println("libele :"+libele);
				String lib=libele.replace("fl", "fL");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("mg/Kg")) {
				//System.out.println("libele :"+libele);
				String lib=libele.replace("mg/Kg", "mg/kg");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("Kpa")) {
				//System.out.println("libele :"+libele);
				String lib=libele.replace("Kpa", "kPa");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("kpa")) {
				//System.out.println("libele :"+libele);
				String lib=libele.replace("kpa", "kPa");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("mosm/L")) {
				//System.out.println("libele :"+libele);
				String lib=libele.replace("mosm/L", "mOsm/L");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("mOsm/kg")) {
				//System.out.println("libele :"+libele);
				String lib=libele.replace("mOsm/kg", "mosm/kg");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("Kg")) {
				//System.out.println("libele :"+libele);
				String lib=libele.replace("Kg", "kg");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("uM")) {
				//System.out.println("libele :"+libele);
				String lib=libele.replace("uM", "um");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("UG/mL")) {
				//System.out.println("libele :"+libele);
				String lib=libele.replace("UG/mL", "ug/mL");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("nM")) {
				//System.out.println("libele :"+libele);
				String lib=libele.replace("nM", "nm");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.contains("copies/mL")) {
				//System.out.println("libele :"+libele);
				String lib=libele.replace("copies/mL", "Copies/mL");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			if(libele.equals("H")) {
				//System.out.println("libele :"+libele);
				String lib=libele.replace("H", "h");
				libele=LibelleAvecEspaceCorrecte.getLibelleCorrect(lib);
				 i++;
			}
			
			//System.out.println("libele : "+libele);
			
				UnitsCorrect.add(libele);
				
				if(!unitAndCorrection.containsKey(unit)) {
					unitAndCorrection.put(unit, libele);
				}
				if(!CorrectionandUnit.containsKey(libele)) {
					CorrectionandUnit.put(libele,new HashSet<String>());
				}
				CorrectionandUnit.get(libele).add(unit);
				if(!CorrectionandUnit.containsKey(unit)) {
					CorrectionandUnit.put(unit,new HashSet<String>());
				}
				CorrectionandUnit.get(unit).add(unit);
				//unit
				
			//System.out.println(unit+" "+libelle);
		}
		
			
		HashMap<String,String> getunitUCUM=GetUnitToMap(conn);
		Set<String> UcumUnit= new HashSet<String>();
		for(String a:getunitUCUM.keySet()) {
			UcumUnit.add(getunitUCUM.get(a));
			//UcumUnit.add(getunitUCUM.get(a).toUpperCase());
		}
		Set<String> UnitCommon= new HashSet<String>();
		Set<String> UnitDifferent= new HashSet<String>();
		for(String un:UnitsCorrect) {
			//System.out.println("un 1 "+ un);
			if(UcumUnit.contains(un)) {
				
				UnitCommon.add(un);
				
				//System.out.println(un);
			}
			
			else {
//				for(String uncf: UcumUnit) {
//					if(uncf.toUpperCase().equals(un.toUpperCase())) {
//						System.out.println("un 2 "+un);
//						
//						
//					}
//				}
				
				//System.out.println("un 3 "+un);
				
				UnitDifferent.add(un);
			}
			
		}
		System.out.println("UnitsCorrect "+UnitsCorrect.size());
		System.out.println("UnitDifferent "+UnitDifferent.size());
		System.out.println("UnitCommon "+UnitCommon.size());
		Set<Statement> result= new HashSet<Statement>();
		String Prefix_rdfs="http://www.w3.org/2000/01/rdf-schema#";
		String prefixe="http://erias.org/UCUM#";
		String skos="http://www.w3.org/2004/02/skos/core#";
		System.out.println("CorrectionandUnit "+CorrectionandUnit);
		
		for(String a:getunitUCUM.keySet()) {
			String unit=getunitUCUM.get(a);
			for(String uny:Units) {
				if(unit.equals(uny)) {
					//System.out.println("un : "+un);
					if(unitAndCorrection.containsKey(uny)) {
						String ATunit= unitAndCorrection.get(uny);
						
					Statement stm=vf.createStatement(vf.createURI(a), 
							vf.createURI(skos + "altLabel"),
							vf.createLiteral(ATunit));
					result.add(stm);

				Statement stmdd=vf.createStatement(vf.createURI(a), 
							vf.createURI(skos + "altLabel"),
							vf.createLiteral(uny));
					result.add(stmdd);
							
					}
					else {
						System.out.println("regarde        "+unit);
					}
			}
			}
			for(String un:UnitsCorrect) {
				if(unit.equals(un)) {
					//System.out.println("un : "+un);
					if(CorrectionandUnit.containsKey(un)) {
						Set<String> ATunit= CorrectionandUnit.get(un);
						for(String Tunit: ATunit) {
					Statement stm=vf.createStatement(vf.createURI(a), 
							vf.createURI(skos + "altLabel"),
							vf.createLiteral(Tunit));
					result.add(stm);

				Statement stmdd=vf.createStatement(vf.createURI(a), 
							vf.createURI(skos + "altLabel"),
							vf.createLiteral(un));
					result.add(stmdd);
							}
					}
					else {
						System.out.println("regarde        "+unit);
					}
//					if(CorrectionandUnit.containsKey(un.toUpperCase())) {
//						Set<String> ATunit= CorrectionandUnit.get(un.toUpperCase());
//						for(String Tunit: ATunit) {
//					Statement stm=vf.createStatement(vf.createURI(a), 
//							vf.createURI(skos + "altLabel"),
//							vf.createLiteral(Tunit));
//					result.add(stm);
//					Statement stmd=vf.createStatement(vf.createURI(a), 
//							vf.createURI(skos + "altLabel"),
//							vf.createLiteral(un.toUpperCase()));
//					result.add(stmd);
//					Statement stmdd=vf.createStatement(vf.createURI(a), 
//							vf.createURI(skos + "altLabel"),
//							vf.createLiteral(un));
//					result.add(stmdd);
//							}
//					}
				}
//				else if(unit.toUpperCase().equals(un.toUpperCase())) {
//					if(CorrectionandUnit.containsKey(un.toUpperCase())) {
//						Set<String> ATunit= CorrectionandUnit.get(un.toUpperCase());
//						for(String Tunit: ATunit) {
//					Statement stm=vf.createStatement(vf.createURI(a), 
//							vf.createURI(skos + "altLabel"),
//							vf.createLiteral(Tunit));
//					result.add(stm);
//					Statement stmd=vf.createStatement(vf.createURI(a), 
//							vf.createURI(skos + "altLabel"),
//							vf.createLiteral(un.toUpperCase()));
//					result.add(stmd);
//					Statement stmdd=vf.createStatement(vf.createURI(a), 
//							vf.createURI(skos + "altLabel"),
//							vf.createLiteral(un));
//					result.add(stmdd);
//							}
//					}
//				}
//				else if(unit.equals(un.toUpperCase())) {
//					if(CorrectionandUnit.containsKey(un.toUpperCase())) {
//						Set<String> ATunit= CorrectionandUnit.get(un.toUpperCase());
//						for(String Tunit: ATunit) {
//					Statement stm=vf.createStatement(vf.createURI(a), 
//							vf.createURI(skos + "altLabel"),
//							vf.createLiteral(Tunit));
//					result.add(stm);
//					Statement stmd=vf.createStatement(vf.createURI(a), 
//							vf.createURI(skos + "altLabel"),
//							vf.createLiteral(un.toUpperCase()));
//					result.add(stmd);
//					Statement stmdd=vf.createStatement(vf.createURI(a), 
//							vf.createURI(skos + "altLabel"),
//							vf.createLiteral(un));
//					result.add(stmdd);
//							}
//					}
//				}
//				else if(unit.toUpperCase().equals(un)) {
//					if(CorrectionandUnit.containsKey(un)) {
//						Set<String> ATunit= CorrectionandUnit.get(un);
//						for(String Tunit: ATunit) {
//					Statement stm=vf.createStatement(vf.createURI(a), 
//							vf.createURI(skos + "altLabel"),
//							vf.createLiteral(Tunit));
//					result.add(stm);
//					Statement stmd=vf.createStatement(vf.createURI(a), 
//							vf.createURI(skos + "altLabel"),
//							vf.createLiteral(un.toUpperCase()));
//					result.add(stmd);
//					Statement stmdd=vf.createStatement(vf.createURI(a), 
//							vf.createURI(skos + "altLabel"),
//							vf.createLiteral(un));
//					result.add(stmdd);
//							}
//					}
//				}
			}
		}
		System.out.println("statement "+result.size());
		Set<Couple> aditional=getPropetyForUnits();
		for(Couple ouple:aditional ) {
			System.out.println("ouple.x "+ouple.x);
			if(CorrectionandUnit.containsKey(ouple.x)) {
				Set<String> unitesP=new HashSet<String>();
				unitesP.addAll(CorrectionandUnit.get(ouple.x));
				if(unitAndCorrection.containsKey(ouple.x)) {
				unitesP.add(unitAndCorrection.get(ouple.x));
				}
				for(String unité:unitesP) {
					System.out.println("+++++ouple.y "+unité);
					String uritocreate=prefixe+unité.hashCode();
					Statement stm7=vf.createStatement(vf.createURI(uritocreate), 
							vf.createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
							vf.createURI(skos+"Concept"));
					result.add(stm7);
					Statement stm8=vf.createStatement(vf.createURI(uritocreate),
							vf.createURI(skos + "altLabel"),
							vf.createLiteral(unité));
					result.add(stm8);
//					Statement stm80=vf.createStatement(vf.createURI(uritocreate),
//							vf.createURI(skos + "altLabel"),
//							vf.createLiteral(unité.toUpperCase()));
//					result.add(stm80);
					Statement stm9=vf.createStatement(vf.createURI(uritocreate),
							vf.createURI(skos + "altLabel"),
							vf.createLiteral(ouple.x));
					result.add(stm9);
//					Statement stm90=vf.createStatement(vf.createURI(uritocreate),
//							vf.createURI(skos + "altLabel"),
//							vf.createLiteral(ouple.x.toUpperCase()));
//					result.add(stm90);
					String kind= ouple.y.replace(" ", "");
					Statement stm4=vf.createStatement(vf.createURI(uritocreate), 
							vf.createURI(prefixe+"kind_of_Quantity"),
							vf.createURI(prefixe+kind));
				result.add(stm4);
					Statement stm5=vf.createStatement(vf.createURI(prefixe+kind), 
							vf.createURI(Prefix_rdfs + "label"),
							vf.createLiteral(ouple.y));
					result.add(stm5);
				}
			}
			
		}
		
		//String Tlabunit = "http://erias.org/InstanciatedCorrectedUpdated";
		String Tlabunit = "http://erias.org/InstanciatedUNitsTOTALESCorriges";
		
		URI TLABunitStructure = conn.getValueFactory().createURI(Tlabunit);
		List<Statement> liste= new ArrayList<Statement>(result);
		
		System.out.println(liste.size());
		System.out.println("Units synergie "+Units.size());
		System.out.println("Units synergie "+UnitsCorrect.size());
		System.out.println("UnitCommon "+UnitCommon.size());
		System.out.println("UnitDifferent "+UnitDifferent.size());
		

		try {	

		conn.add(liste,TLABunitStructure);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("final terminé");
//		System.out.println(" annotationInstanciated "+annotationInstanciated.size());
//		System.out.println(" annotationTotal "+annotationTotal.size());
//		System.out.println(" Units "+Units.size());
//		System.out.println("UnitsCorrect "+UnitsCorrect.size());
		

	}
	public static HashMap<String,String> GetUnitToMap(RepositoryConnection conn) throws QueryEvaluationException, RepositoryException, MalformedQueryException{

		String query= " prefix skos: <http://www.w3.org/2004/02/skos/core#> "
				+ "select distinct ?s ?o where "
		 		+ "{ "
		 		+ "graph<http://erias.org/ucum>{"
		 		+ " ?s skos:altLabel ?o "
		 		+ "} "
		 		+ " }";
		
		ArrayList<BindingSet> ListeRDF3= new ArrayList<BindingSet>(InteractionBlazgraph.selectQuery(conn, query));
		HashMap<String,String> CodeTerminoListeCodeLoincAssocie= new HashMap<String, String>();
		 for(BindingSet e: ListeRDF3){
			 if(!CodeTerminoListeCodeLoincAssocie.containsKey(e.getValue("s").stringValue())){
					
				 CodeTerminoListeCodeLoincAssocie.put(e.getValue("s").stringValue(), e.getValue("o").stringValue());
				};
				//CodeTerminoListeCodeLoincAssocie.get(e.getValue("s").stringValue()).add(e.getValue("o").stringValue());
			// System.out.println("e.getValue(\"o\").stringValue() "+e.getValue("o").stringValue());
		 }
		 
		 return CodeTerminoListeCodeLoincAssocie;
	}
	public static Set<String> getFirstResult() throws SQLException{
		ConnexionBD df = new ConnexionBD(1);
		Set<String> Results= new HashSet<String>();
		String recuperationAnnotation = "select* from "
				+ "tlabinstanciatedconcepts ";
		PreparedStatement ps = df.getconn().prepareStatement(recuperationAnnotation);
		ResultSet resultat = ps.executeQuery();
		while(resultat.next()){
			String annotation=resultat.getString(1);
			Results.add(annotation);
			
		}
		
		return Results;
	}
	public static Set<DataStructure>getResultInstanciate() {
		Set<String> Results= new HashSet<String>();
		Set<DataStructure> Resultats= new HashSet<DataStructure>();
		try{
			//InputStream flux=new FileInputStream("./exampleSynInstance.csv"); 
			InputStream flux=new FileInputStream("./exampleSynInstance_all.csv");
			
			InputStreamReader lecture=new InputStreamReader(flux,StandardCharsets.ISO_8859_1);
			BufferedReader buff=new BufferedReader(lecture);
			String ligne;
			while ((ligne=buff.readLine())!=null){
				String[]rest=ligne.split(";");
				//System.out.println(rest);
				if(rest.length>=12) {
				
				String annot=rest[2].substring(1, rest[2].length()-1);
				Results.add(annot);
				String a=rest[12];
				//System.out.println(a);
					if(!a.isEmpty()&& a!="") {
						String unit =a.substring(1, rest[12].length()-1);
						DataStructure resum= new DataStructure(annot, unit);
						Resultats.add(resum);
					}
				}
				else {
					String annot=rest[2].substring(1, rest[2].length()-1);
					Results.add(annot);
					//System.out.println("annot "+annot);
					String unit ="Number";
					DataStructure resum= new DataStructure(annot, unit);
					Resultats.add(resum);
				}
			
			}
			
			buff.close(); 
			}		
			catch (Exception e){
			System.out.println(e.toString());
			}
		return Resultats;
	}
	public static Set<Couple>getPropetyForUnits() {
		Set<Couple> rsul= new HashSet<Couple>();
		try{
			//InputStream flux=new FileInputStream("./AditionalMainUnitAndProperty.txt"); 
			InputStream flux=new FileInputStream("./additional.csv"); 
			InputStreamReader lecture=new InputStreamReader(flux,StandardCharsets.UTF_8);
			BufferedReader buff=new BufferedReader(lecture);
			String ligne;
			while ((ligne=buff.readLine())!=null){
				String[]rest=ligne.split(";");
				//System.out.println(rest);
				if(rest.length>=2) {
				
				String UnitsCorrect=rest[0];
				UnitsCorrect=UnitsCorrect.substring(1,UnitsCorrect.length()-1);
				//System.out.println("UnitsCorrect :"+UnitsCorrect);
				String PropertyRatached=rest[1];
				PropertyRatached=PropertyRatached.substring(1,PropertyRatached.length()-1);
				//System.out.println("PropertyRatached 2 :"+PropertyRatached);
				
				Couple a= new Couple(UnitsCorrect, PropertyRatached);
				rsul.add(a);
				System.out.println(UnitsCorrect+" "+PropertyRatached);
				}
			
			}
			
			buff.close(); 
			}		
			catch (Exception e){
			System.out.println(e.toString());
			}
		return rsul;
	}

}
