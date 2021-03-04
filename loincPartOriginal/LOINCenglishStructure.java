package loincPartOriginal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import interactionRelationalDatabase.ConnexionBD;
import interactionTripleStore.RepositoryFactory;
import utilitaries.LOINCstructureModel;

public class LOINCenglishStructure {

	public static void main(String[] args) throws SQLException, RepositoryException {
		// TODO Auto-generated method stub
		ConnexionBD loinc2019DataBase= new ConnexionBD(1);
		
		String query= "SELECT * FROM linkdescription";		
		
		PreparedStatement LOINCStructLoinc = loinc2019DataBase.getconn().prepareStatement(query);
		ResultSet StructureLoincresultat = LOINCStructLoinc.executeQuery();
		Set<LOINCstructureModel> ResultatLoinc= new HashSet<LOINCstructureModel>();

		while(StructureLoincresultat.next()){
			String CodeLoinc=StructureLoincresultat.getString(1);
			String LoincName=StructureLoincresultat.getString(2);
			String PartCode=StructureLoincresultat.getString(3);
			String PartName=StructureLoincresultat.getString(4);
			String PartType=StructureLoincresultat.getString(5);
			String LinkType=StructureLoincresultat.getString(6);
			LOINCstructureModel ligne = new LOINCstructureModel(CodeLoinc, LoincName, PartCode, PartName, PartType, LinkType);
			
			ResultatLoinc.add(ligne);
		}
		System.out.println(ResultatLoinc.size());
		HashMap<String, Set<String>> LoincAndComponent= new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> LoincAndProperty= new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> LoincAndTime_aspect= new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> LoincAndSystem= new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> LoincAndScale= new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> LoincAndmethod= new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> LoincAndClassName= new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> LoincAndChallenge= new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> LoincAndTimeModifier= new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> LoincAndAdjustment= new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> LoincAndSuperSystem= new HashMap<String, Set<String>>();
		
		
		
		Map<String, Set<String>> LoincCodeAndLabels= new HashMap<String, Set<String>>();
		
		for(LOINCstructureModel ligne :ResultatLoinc) {
			//System.out.println("*"+ligne.getPartType()+"*");
			//System.out.println("*"+ligne.getLinkType()+"*");
			if(!LoincCodeAndLabels.containsKey(ligne.getCodeLoinc())) {
				LoincCodeAndLabels.put(ligne.getCodeLoinc(), new HashSet<String>());
			}
			LoincCodeAndLabels.get(ligne.getCodeLoinc()).add(ligne.getLoincName());
			
			if(!LoincCodeAndLabels.containsKey(ligne.getPartCode())) {
				LoincCodeAndLabels.put(ligne.getPartCode(), new HashSet<String>());
			}
			LoincCodeAndLabels.get(ligne.getPartCode()).add(ligne.getPartName());
	
			if(ligne.getLinkType().equals("Primary")) {
				if(ligne.getPartType().equals("COMPONENT")) {
					if(!LoincAndComponent.containsKey(ligne.getCodeLoinc())) {
						LoincAndComponent.put(ligne.getCodeLoinc(), new HashSet<String>());
					}
					LoincAndComponent.get(ligne.getCodeLoinc()).add(ligne.getPartCode());
				}
				if(ligne.getPartType().equals("PROPERTY")) {
					if(!LoincAndProperty.containsKey(ligne.getCodeLoinc())) {
						LoincAndProperty.put(ligne.getCodeLoinc(), new HashSet<String>());
					}
					LoincAndProperty.get(ligne.getCodeLoinc()).add(ligne.getPartCode());
				}
				if(ligne.getPartType().equals("TIME")) {
					if(!LoincAndTime_aspect.containsKey(ligne.getCodeLoinc())) {
						LoincAndTime_aspect.put(ligne.getCodeLoinc(), new HashSet<String>());
					}
					LoincAndTime_aspect.get(ligne.getCodeLoinc()).add(ligne.getPartCode());
					
				}
				if(ligne.getPartType().equals("SYSTEM")) {
					if(!LoincAndSystem.containsKey(ligne.getCodeLoinc())) {
						LoincAndSystem.put(ligne.getCodeLoinc(), new HashSet<String>());
					}
					LoincAndSystem.get(ligne.getCodeLoinc()).add(ligne.getPartCode());
				}
				if(ligne.getPartType().equals("SCALE")) {
					if(!LoincAndScale.containsKey(ligne.getCodeLoinc())) {
						LoincAndScale.put(ligne.getCodeLoinc(), new HashSet<String>());
					}
					LoincAndScale.get(ligne.getCodeLoinc()).add(ligne.getPartCode());
				}
				if(ligne.getPartType().equals("METHOD")) {
					if(!LoincAndmethod.containsKey(ligne.getCodeLoinc())) {
						LoincAndmethod.put(ligne.getCodeLoinc(), new HashSet<String>());
					}
					LoincAndmethod.get(ligne.getCodeLoinc()).add(ligne.getPartCode());
				}
				if(ligne.getPartType().equals("CLASS")) {
					if(!LoincAndClassName.containsKey(ligne.getCodeLoinc())) {
						LoincAndClassName.put(ligne.getCodeLoinc(), new HashSet<String>());
					}
					LoincAndClassName.get(ligne.getCodeLoinc()).add(ligne.getPartCode());
				}
				if(ligne.getPartType().equals("CHALLENGE")) {
					if(!LoincAndChallenge.containsKey(ligne.getCodeLoinc())) {
						LoincAndChallenge.put(ligne.getCodeLoinc(), new HashSet<String>());
					}
					LoincAndChallenge.get(ligne.getCodeLoinc()).add(ligne.getPartCode());
				}
				if(ligne.getPartType().equals("ADJUSTMENT")) {
					if(!LoincAndAdjustment.containsKey(ligne.getCodeLoinc())) {
						LoincAndAdjustment.put(ligne.getCodeLoinc(), new HashSet<String>());
					}
					LoincAndAdjustment.get(ligne.getCodeLoinc()).add(ligne.getPartCode());
				}
				if(ligne.getPartType().equals("TIME MODIFIER")) {
					if(!LoincAndTimeModifier.containsKey(ligne.getCodeLoinc())) {
						LoincAndTimeModifier.put(ligne.getCodeLoinc(), new HashSet<String>());
					}
					LoincAndTimeModifier.get(ligne.getCodeLoinc()).add(ligne.getPartCode());
				}
				if(ligne.getPartType().equals("SUPER SYSTEM")) {
					if(!LoincAndSuperSystem.containsKey(ligne.getCodeLoinc())) {
						LoincAndSuperSystem.put(ligne.getCodeLoinc(), new HashSet<String>());
					}
					LoincAndSuperSystem.get(ligne.getCodeLoinc()).add(ligne.getPartCode());
				}
			
				
			}
			
		}
		//String namespace="EnglishLoinc";
		//String namespace="FrenchLOINCtest";
		//String namespace="LOINCTotal";
		String namespace = "ENGLISHLOINC";
		//String namespace="FrenchLoincAnalysis";
		Repository repo = RepositoryFactory.getRepository(namespace);
		RepositoryConnection connnex = repo.getConnection();
		System.out.println("connexion realised");
		
		//	Set<Statement> lesSatement= new HashSet<Statement>();
		ValueFactory vf = connnex.getValueFactory();
		
		String prefixe="http://erias.org/loincTotal#";
		Set<Statement> resultStatement= new HashSet<Statement>();
		
		Map<String, Set<String>> LoincAndComponentUnique= getRDFtoconstructcardinality1(LoincAndComponent);
		Map<String, Set<String>> LoincAndPropertyUnique= getRDFtoconstructcardinality1(LoincAndProperty);
		Map<String, Set<String>> LoincAndTime_aspectUnique= getRDFtoconstructcardinality1(LoincAndTime_aspect);
		Map<String, Set<String>> LoincAndSystemUnique= getRDFtoconstructcardinality1(LoincAndSystem);
		Map<String, Set<String>> LoincAndScaleUnique= getRDFtoconstructcardinality1(LoincAndScale);
		Map<String, Set<String>> LoincAndmethodUnique= getRDFtoconstructcardinality1(LoincAndmethod);
		Map<String, Set<String>> LoincAndClassNameUnique= getRDFtoconstructcardinality1(LoincAndClassName);
		Map<String, Set<String>> LoincAndChallengeUnique= getRDFtoconstructcardinality1(LoincAndChallenge);
		Map<String, Set<String>> LoincAndTimeModifierUnique= getRDFtoconstructcardinality1(LoincAndTimeModifier);
		Map<String, Set<String>> LoincAndAdjustmentUnique= getRDFtoconstructcardinality1(LoincAndAdjustment);
		Map<String, Set<String>> LoincAndSuperSystemUnique= getRDFtoconstructcardinality1(LoincAndSuperSystem);
		
		
		//resultStatement.addAll(getLoincRelationRDF(prefixe, LoincAndComponentUnique, LoincAndPropertyUnique, LoincAndClassNameUnique, LoincAndScaleUnique, LoincAndTime_aspectUnique, LoincAndmethodUnique, LoincAndSystemUnique,LoincCodeAndLabels, vf));
		resultStatement.addAll(getLoincRelationRDF(prefixe, LoincAndComponentUnique, LoincAndChallengeUnique, LoincAndAdjustmentUnique, LoincAndTimeModifierUnique, LoincAndSuperSystemUnique, LoincAndPropertyUnique, LoincAndClassNameUnique, LoincAndScaleUnique, LoincAndTime_aspectUnique, LoincAndmethodUnique, LoincAndSystemUnique,LoincCodeAndLabels, vf));
		
		System.out.println("debut de l'insertion");
		System.out.println("LoincAndComponentUnique "+LoincAndComponentUnique.size());
		
		
		String GraphLoincEnglish = "http://erias.org/EnglishLoinc";
		
		URI graphLoincStrucEnglish = connnex.getValueFactory().createURI(GraphLoincEnglish);
		List<Statement> liste= new ArrayList<Statement>(resultStatement);
		

//		try {	
//
//			connnex.add(liste,graphLoincStrucEnglish);
//		} catch (RepositoryException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		System.out.println("final terminé");
		
		
		System.out.println("getcardinality0(LoincAndComponent).size() "+getcardinality0(LoincAndComponent).size());
		System.out.println("getcardinality0(LoincAndProperty).size() "+getcardinality0(LoincAndProperty).size());
		System.out.println("getcardinality0(LoincAndTime_aspect).size()"+getcardinality0(LoincAndTime_aspect).size());
		System.out.println("getcardinality0(LoincAndSystem).size() "+getcardinality0(LoincAndSystem).size());
		System.out.println("getcardinality0(LoincAndScale).size() "+getcardinality0(LoincAndScale).size());
		System.out.println("getcardinality0(LoincAndmethod).size() "+getcardinality0(LoincAndmethod).size());
		System.out.println("getcardinality0(LoincAndClassName).size() "+getcardinality0(LoincAndClassName).size());
		System.out.println("getcardinality0(LoincAndChallenge).size() "+getcardinality0(LoincAndChallenge).size());
		System.out.println("getcardinality0(LoincAndTimeModifier).size() "+getcardinality0(LoincAndTimeModifier).size());
		System.out.println("getcardinality0(LoincAndAdjustment).size() "+getcardinality0(LoincAndAdjustment).size());
		System.out.println("getcardinality0(LoincAndSuperSystem).size() "+getcardinality0(LoincAndSuperSystem).size());
		
		
		System.out.println("getcardinality1(LoincAndComponent).size() "+getcardinality1(LoincAndComponent).size());
		System.out.println("getcardinality1(LoincAndProperty).size() "+getcardinality1(LoincAndProperty).size());
		System.out.println("getcardinality1(LoincAndTime_aspect).size()"+getcardinality1(LoincAndTime_aspect).size());
		System.out.println("getcardinality1(LoincAndSystem).size() "+getcardinality1(LoincAndSystem).size());
		System.out.println("getcardinality1(LoincAndScale).size() "+getcardinality1(LoincAndScale).size());
		System.out.println("getcardinality1(LoincAndmethod).size() "+getcardinality1(LoincAndmethod).size());
		System.out.println("getcardinality1(LoincAndClassName).size() "+getcardinality1(LoincAndClassName).size());
		System.out.println("getcardinality1(LoincAndChallenge).size() "+getcardinality1(LoincAndChallenge).size());
		System.out.println("getcardinality1(LoincAndTimeModifier).size() "+getcardinality1(LoincAndTimeModifier).size());
		System.out.println("getcardinality1(LoincAndAdjustment).size() "+getcardinality1(LoincAndAdjustment).size());
		System.out.println("getcardinality1(LoincAndSuperSystem).size() "+getcardinality1(LoincAndSuperSystem).size());
		
		
		
		System.out.println("getcardinalityN(LoincAndComponent).size() "+getcardinalityN(LoincAndComponent).size());
		System.out.println("getcardinalityN(LoincAndProperty).size() "+getcardinalityN(LoincAndProperty).size());
		System.out.println("getcardinalityN(LoincAndTime_aspect).size()"+getcardinalityN(LoincAndTime_aspect).size());
		System.out.println("getcardinalityN(LoincAndSystem).size() "+getcardinalityN(LoincAndSystem));
		System.out.println("getcardinalityN(LoincAndScale).size() "+getcardinalityN(LoincAndScale).size());
		System.out.println("getcardinalityN(LoincAndmethod).size() "+getcardinalityN(LoincAndmethod).size());
		System.out.println("getcardinalityN(LoincAndClassName).size() "+getcardinalityN(LoincAndClassName).size());
		System.out.println("getcardinalityN(LoincAndChallenge).size() "+getcardinalityN(LoincAndChallenge).size());
		System.out.println("getcardinalityN(LoincAndTimeModifier).size() "+getcardinalityN(LoincAndTimeModifier).size());
		System.out.println("getcardinalityN(LoincAndAdjustment).size() "+getcardinalityN(LoincAndAdjustment).size());
		System.out.println("getcardinalityN(LoincAndSuperSystem).size() "+getcardinalityN(LoincAndSuperSystem).size());
		
		
		
		

	}
	public static Set<String>getcardinality1(HashMap<String, Set<String>> hastotest) {
		
		Set<String> result= new HashSet<String>();
		
		for(String code: hastotest.keySet()) {
			if(hastotest.get(code).size()==1) {
				result.add(code);
			}
		}
		
		
		return result;
	}
	
	public static Map<String, Set<String>>getRDFtoconstructcardinality1(HashMap<String, Set<String>> hastotest) {
		
		 Map<String, Set<String>> result= new  HashMap<String, Set<String>>();
		
		for(String code: hastotest.keySet()) {
			if(hastotest.get(code).size()==1) {
				if(!result.containsKey(code)) {
					result.put(code, new HashSet<String>());
				}
				result.get(code).addAll(hastotest.get(code));
			}
		}
		
		
		return result;
	}
	
	
	
	
	
public static Set<String>getcardinality0(HashMap<String, Set<String>> hastotest) {
		
		Set<String> result= new HashSet<String>();
		
		for(String code: hastotest.keySet()) {
			if(hastotest.get(code).isEmpty()) {
				result.add(code);
			}
		}
		
		
		return result;
	}
public static Set<String>getcardinalityN(HashMap<String, Set<String>> hastotest) {
	
	Set<String> result= new HashSet<String>();
	
	for(String code: hastotest.keySet()) {
		if(hastotest.get(code).size()>1) {
			result.add(code);
		}
	}
	
	
	return result;
}


public static Set<Statement> getLoincRelationRDF(String prefixe,
		Map<String, Set<String>> lesComposants,
		Map<String, Set<String>> lesChallenge,
		Map<String, Set<String>> lesadjustement,
		Map<String, Set<String>> lestimemodifier,
		Map<String, Set<String>> supersystem,
	Map<String, Set<String>> LesgrandeursMesurer,
Map<String, Set<String>> lesChapitres,
Map<String, Set<String>> Echelle,
Map<String, Set<String>> letemps,
Map<String, Set<String>> lesTechnique,
Map<String, Set<String>> leMilieuBiologique,
Map<String , Set<String>> codeAndLabel,ValueFactory vf
){
	Set<String> codeTodescribe= new HashSet<String>();
	Set<Statement> lesSatement= new HashSet<Statement>();
	
	System.out.println("begin adjustment");
	for(String codeLoinc :lesadjustement.keySet()){
		
		
		for (String codePart:lesadjustement.get(codeLoinc)) {
			codeTodescribe.add(codeLoinc);
			codeTodescribe.add(codePart);
			Statement stm = vf.createStatement(vf.createURI(prefixe+codeLoinc),
					vf.createURI(prefixe+"has_adjustment"),
					vf.createURI(prefixe+codePart));
			lesSatement.add(stm);
		}
		
	
}
	System.out.println("begin challenge");
	for(String codeLoinc :lesChallenge.keySet()){
		
		
		for (String codePart:lesChallenge.get(codeLoinc)) {
			codeTodescribe.add(codeLoinc);
			codeTodescribe.add(codePart);
			Statement stm = vf.createStatement(vf.createURI(prefixe+codeLoinc),
					vf.createURI(prefixe+"has_challenge"),
					vf.createURI(prefixe+codePart));
			lesSatement.add(stm);
		}
		
	
}
	System.out.println("begin time modifier");
	for(String codeLoinc :lestimemodifier.keySet()){
		
		
		for (String codePart:lestimemodifier.get(codeLoinc)) {
			codeTodescribe.add(codeLoinc);
			codeTodescribe.add(codePart);
			Statement stm = vf.createStatement(vf.createURI(prefixe+codeLoinc),
					vf.createURI(prefixe+"has_modifier_time"),
					vf.createURI(prefixe+codePart));
			lesSatement.add(stm);
		}
		
	
}
	System.out.println("begin super system ");
	for(String codeLoinc :supersystem.keySet()){
		
		
		for (String codePart:supersystem.get(codeLoinc)) {
			codeTodescribe.add(codeLoinc);
			codeTodescribe.add(codePart);
			Statement stm = vf.createStatement(vf.createURI(prefixe+codeLoinc),
					vf.createURI(prefixe+"has_super_system"),
					vf.createURI(prefixe+codePart));
			lesSatement.add(stm);
		}
		
	
}
	
//	System.out.println("begining systems");
	for(String codeLoinc :leMilieuBiologique.keySet()){
		
		
			for (String codePart:leMilieuBiologique.get(codeLoinc)) {
				codeTodescribe.add(codeLoinc);
				codeTodescribe.add(codePart);
				Statement stm = vf.createStatement(vf.createURI(prefixe+codeLoinc),
						vf.createURI(prefixe+"has_system"),
						vf.createURI(prefixe+codePart));
				lesSatement.add(stm);
			}
			
		
	}
//	System.out.println("begining lesTechnique");
	for(String codeLoinc :lesTechnique.keySet()){
		
		for (String codePart:lesTechnique.get(codeLoinc)) {
				
				codeTodescribe.add(codeLoinc);
				codeTodescribe.add(codePart);
				Statement stm = vf.createStatement(vf.createURI(prefixe+codeLoinc),
						vf.createURI(prefixe+"has_method"),
						vf.createURI(prefixe+codePart));
				lesSatement.add(stm);
			}
		
	}
	//System.out.println("begining letemps");
	for(String codeLoinc :letemps.keySet()){
		
		for (String codePart:letemps.get(codeLoinc)) {
			
			codeTodescribe.add(codeLoinc);
				codeTodescribe.add(codePart);
				Statement stm = vf.createStatement(vf.createURI(prefixe+codeLoinc),
					vf.createURI(prefixe+"has_time"),
						vf.createURI(prefixe+codePart));
				lesSatement.add(stm);
			}
		
	}
//	System.out.println("begining Echelle");
	
	for(String codeLoinc :Echelle.keySet()){
		
		for (String codePart:Echelle.get(codeLoinc)) {
			
			codeTodescribe.add(codeLoinc);
				codeTodescribe.add(codePart);
				Statement stm = vf.createStatement(vf.createURI(prefixe+codeLoinc),
					vf.createURI(prefixe+"has_scale"),
						vf.createURI(prefixe+codePart));
				lesSatement.add(stm);
			}
		
	}
	
//	System.out.println("begining lesChapitres");
	
	for(String codeLoinc :lesChapitres.keySet()){
		
		for (String codePart:lesChapitres.get(codeLoinc)) {
			
			codeTodescribe.add(codeLoinc);
				codeTodescribe.add(codePart);
				Statement stm = vf.createStatement(vf.createURI(prefixe+codeLoinc),
						vf.createURI(prefixe+"has_class"),
						vf.createURI(prefixe+codePart));
				lesSatement.add(stm);
			}
		
	}
	//System.out.println("begining lesComposants");
	
	for(String codeLoinc :lesComposants.keySet()){
		
		for (String codePart:lesComposants.get(codeLoinc)) {
			
			codeTodescribe.add(codeLoinc);
				codeTodescribe.add(codePart);
				Statement stm = vf.createStatement(vf.createURI(prefixe+codeLoinc),
					vf.createURI(prefixe+"has_component"),
					vf.createURI(prefixe+codePart));
				lesSatement.add(stm);
			}
		
	}
	//System.out.println("begining LesgrandeursMesurer");
	for(String codeLoinc :LesgrandeursMesurer.keySet()){
		
		for (String codePart:LesgrandeursMesurer.get(codeLoinc)) {
			
			codeTodescribe.add(codeLoinc);
				codeTodescribe.add(codePart);
				Statement stm = vf.createStatement(vf.createURI(prefixe+codeLoinc),
				vf.createURI(prefixe+"has_property"),
						vf.createURI(prefixe+codePart));
				lesSatement.add(stm);
			}
		
	}
	String Prefix_rdfs="http://www.w3.org/2000/01/rdf-schema#";	
	
	for(String code:codeTodescribe) {
		if(codeAndLabel.containsKey(code)) {
			for(String label:codeAndLabel.get(code)) {
				Statement stm = vf.createStatement( vf.createURI(prefixe+code),
						vf.createURI(Prefix_rdfs + "label"),
						vf.createLiteral (label));
				lesSatement.add(stm);
			
			}
		}
	}
	
	//System.out.println("next");
	return lesSatement;
	//System.out.println("quoi?");
	
}


public static Set<Statement> getLoincRelationRDF(String prefixe,
		Map<String, Set<String>> lesComposants,
	Map<String, Set<String>> LesgrandeursMesurer,
Map<String, Set<String>> lesChapitres,
Map<String, Set<String>> Echelle,
Map<String, Set<String>> letemps,
Map<String, Set<String>> lesTechnique,
Map<String, Set<String>> leMilieuBiologique,
Map<String , Set<String>> codeAndLabel,ValueFactory vf
){
	Set<String> codeTodescribe= new HashSet<String>();
	Set<Statement> lesSatement= new HashSet<Statement>();
	
//	System.out.println("begining systems");
	for(String codeLoinc :leMilieuBiologique.keySet()){
		
		
			for (String codePart:leMilieuBiologique.get(codeLoinc)) {
				codeTodescribe.add(codeLoinc);
				codeTodescribe.add(codePart);
				Statement stm = vf.createStatement(vf.createURI(prefixe+codeLoinc),
						vf.createURI(prefixe+"has_system"),
						vf.createURI(prefixe+codePart));
				lesSatement.add(stm);
			}
			
		
	}
//	System.out.println("begining lesTechnique");
	for(String codeLoinc :lesTechnique.keySet()){
		
		for (String codePart:lesTechnique.get(codeLoinc)) {
				
				codeTodescribe.add(codeLoinc);
				codeTodescribe.add(codePart);
				Statement stm = vf.createStatement(vf.createURI(prefixe+codeLoinc),
						vf.createURI(prefixe+"has_method"),
						vf.createURI(prefixe+codePart));
				lesSatement.add(stm);
			}
		
	}
	//System.out.println("begining letemps");
	for(String codeLoinc :letemps.keySet()){
		
		for (String codePart:letemps.get(codeLoinc)) {
			
			codeTodescribe.add(codeLoinc);
				codeTodescribe.add(codePart);
				Statement stm = vf.createStatement(vf.createURI(prefixe+codeLoinc),
					vf.createURI(prefixe+"has_time"),
						vf.createURI(prefixe+codePart));
				lesSatement.add(stm);
			}
		
	}
//	System.out.println("begining Echelle");
	
	for(String codeLoinc :Echelle.keySet()){
		
		for (String codePart:Echelle.get(codeLoinc)) {
			
			codeTodescribe.add(codeLoinc);
				codeTodescribe.add(codePart);
				Statement stm = vf.createStatement(vf.createURI(prefixe+codeLoinc),
					vf.createURI(prefixe+"has_scale"),
						vf.createURI(prefixe+codePart));
				lesSatement.add(stm);
			}
		
	}
	
//	System.out.println("begining lesChapitres");
	
	for(String codeLoinc :lesChapitres.keySet()){
		
		for (String codePart:lesChapitres.get(codeLoinc)) {
			
			codeTodescribe.add(codeLoinc);
				codeTodescribe.add(codePart);
				Statement stm = vf.createStatement(vf.createURI(prefixe+codeLoinc),
						vf.createURI(prefixe+"has_class"),
						vf.createURI(prefixe+codePart));
				lesSatement.add(stm);
			}
		
	}
	//System.out.println("begining lesComposants");
	
	for(String codeLoinc :lesComposants.keySet()){
		
		for (String codePart:lesComposants.get(codeLoinc)) {
			
			codeTodescribe.add(codeLoinc);
				codeTodescribe.add(codePart);
				Statement stm = vf.createStatement(vf.createURI(prefixe+codeLoinc),
					vf.createURI(prefixe+"has_component"),
					vf.createURI(prefixe+codePart));
				lesSatement.add(stm);
			}
		
	}
	//System.out.println("begining LesgrandeursMesurer");
	for(String codeLoinc :LesgrandeursMesurer.keySet()){
		
		for (String codePart:LesgrandeursMesurer.get(codeLoinc)) {
			
			codeTodescribe.add(codeLoinc);
				codeTodescribe.add(codePart);
				Statement stm = vf.createStatement(vf.createURI(prefixe+codeLoinc),
				vf.createURI(prefixe+"has_property"),
						vf.createURI(prefixe+codePart));
				lesSatement.add(stm);
			}
		
	}
	String Prefix_rdfs="http://www.w3.org/2000/01/rdf-schema#";	
	
	for(String code:codeTodescribe) {
		if(codeAndLabel.containsKey(code)) {
			for(String label:codeAndLabel.get(code)) {
				Statement stm = vf.createStatement( vf.createURI(prefixe+code),
						vf.createURI(Prefix_rdfs + "label"),
						vf.createLiteral (label));
				lesSatement.add(stm);
			
			}
		}
	}
	
	//System.out.println("next");
	return lesSatement;
	//System.out.println("quoi?");
	
}
public static Set<Statement> getStamentRDFFormat(HashMap<String, Set<String>> ListeCaracteristique,ValueFactory vf){

	String Prefix_rdfs="http://www.w3.org/2000/01/rdf-schema#";	
	
	Set<Statement> lesSatement= new HashSet<Statement>();
	
	for(String code: ListeCaracteristique.keySet()){
		for(String libelle: ListeCaracteristique.get(code)){
		
			//System.out.println("*"+code+"*");
			Statement stm = vf.createStatement( vf.createURI(code),
					vf.createURI(Prefix_rdfs + "label"),
					vf.createLiteral (libelle));
			lesSatement.add(stm);
		}
		
	}
	return lesSatement;
	
}

}
