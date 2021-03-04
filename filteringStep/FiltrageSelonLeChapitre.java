package filteringStep; 

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


import preTraitement.RecuperationGraphRDF;
import utilitaries.Couple;


public class FiltrageSelonLeChapitre {
	
	public FiltrageSelonLeChapitre(RepositoryConnection conn) {
		// TODO Auto-generated constructor stub
		ValueFactory vf = conn.getValueFactory();
		Set<Statement> lesNouvellesHierarchie= new HashSet<Statement>();
		Set<Statement> lesNouvellesHierarchie2= new HashSet<Statement>();
		String graphAncrageHierarchie=	"http://erias.org/integration/AnchoringCandidats/newHierarchy";		
		RecuperationGraphRDF lesConceptsTerminologieLOcalEtLeurClassENLOINC = new RecuperationGraphRDF(conn, "hierarchie", graphAncrageHierarchie);
		
		HashMap<String,List<String>> ConceptsTerminologieLocalEtChapitreLoinc= lesConceptsTerminologieLOcalEtLeurClassENLOINC.getRDFlisteLibellé();
		//String ancreFiltrageEtape1="http://erias.org/integration/AnchoringCandidats";
		//String ancreFiltrageEtape1="http://erias.org/integration/AnchoringCandidatsTOTAL";
		String ancreFiltrageEtape1="http://erias.org/integration/AnchoringCandidatsTOTALCORRIGE";
		
		
		String graphLoinc=	"http://erias.org/FrenchLoinc";
		RecuperationGraphRDF LoincHierarchieAndClass = new RecuperationGraphRDF(conn, "hierarchie2", graphLoinc);
		
		HashMap<String,List<String>> codeLOINCEtChapitr= LoincHierarchieAndClass.getRDFlisteLibellé();
		HashMap<String,Set<String>> codeChapitreandcodeLOINC=getChapListeCodeInversion(codeLOINCEtChapitr);

		Set<String>codePerdu= new HashSet<String>();
		RecuperationGraphRDF lescodetermEtecodeLoincLiet = new RecuperationGraphRDF(conn, "anchorRest", ancreFiltrageEtape1);
		
		HashMap<String,List<String>> CodeTerminoListeCodeLoincAssocie= lescodetermEtecodeLoincLiet.getRDFlisteLibellé();
		System.out.println("CodeTerminoListeCodeLoincAssocie "+CodeTerminoListeCodeLoincAssocie.size());
		
		
		int i=1;
		int v=1;
		for(String codeTerm :CodeTerminoListeCodeLoincAssocie.keySet()){
			//System.out.println("codeTerm "+codeTerm);
			Set<String> Hierarchie = new HashSet<String>();
			if(ConceptsTerminologieLocalEtChapitreLoinc.containsKey(codeTerm)){
				
				Hierarchie.addAll(ConceptsTerminologieLocalEtChapitreLoinc.get(codeTerm));
				//System.out.println("Hierarchie "+Hierarchie);
				
				
				int uv=0;
				for(String hierar:Hierarchie){
					
					
					if(codeChapitreandcodeLOINC.containsKey(hierar)){
						
						for(String codeLoinc : CodeTerminoListeCodeLoincAssocie.get(codeTerm)){
														
							if(codeChapitreandcodeLOINC.get(hierar).contains(codeLoinc)){
								Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
										vf.createURI("http://erias.org/integration/TLAB/model#ValidateAnchorsByClass"),
										vf.createURI(codeLoinc));
								if(v<8000000){
										lesNouvellesHierarchie.add(hty10);
										v++;
								}else {
									lesNouvellesHierarchie2.add(hty10);
								}
										uv++;
								
							}
						}
					}
				}
				if(uv==0) {
					//System.out.println(codeTerm+" ");
					codePerdu.add(codeTerm);
				}
//					for(String codloinc: CodeTerminoListeCodeLoincAssocie.get(codeTerm)){
//						//System.out.println(codeTerm+" "+codloinc);
//						Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
//								vf.createURI("http://erias.org/integration/TLAB/model#ValidateAnchorsByClass"),
//								vf.createURI(codloinc));
//						if(i<4000000){
//							
//								lesNouvellesHierarchie2.add(hty10);
//								i++;
//						}else{
//							lesNouvellesHierarchie2.add(hty10);
//						}
//					}
//					
//				}
			}
			else{
				for(String codloinc: CodeTerminoListeCodeLoincAssocie.get(codeTerm)){
					Statement hty10=vf.createStatement(vf.createURI(codeTerm), 
							vf.createURI("http://erias.org/integration/TLAB/model#ValidateAnchorsByClass"),
							vf.createURI(codloinc));
					if(i<4000000){
							lesNouvellesHierarchie2.add(hty10);
							i++;
					}else{
						lesNouvellesHierarchie2.add(hty10);
					}
				}
				
			
			}
		}
		
		
		String graphAncrageFiltre=	"http://erias.org/integration/AnchoringFilteringClassTOTALCORRIGE";
		
		URI AncrageURI = conn.getValueFactory().createURI(graphAncrageFiltre);
		List<Statement> liste= new ArrayList<Statement>(lesNouvellesHierarchie);
		System.out.println("filtrage hierarchir "+liste.size());
		List<Statement> liste2= new ArrayList<Statement>(lesNouvellesHierarchie2);
		System.out.println("filtrage hierarchir "+liste2.size());
		
		System.out.println("codePerdu "+codePerdu);
		System.out.println("debut d'insertion");
		try {
			
			conn.add(liste,AncrageURI);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("deuxième parti debut ");
		try {
			
			conn.add(liste2,AncrageURI);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	}
		System.out.println("fin d'insertion");
	
		
		
	}
	
	public HashMap<String, Set<String>> getCodeListeChap(HashMap<String,Set<Couple>> codeEtChapitre){
		HashMap<String, Set<String>> resultat= new HashMap<String, Set<String>>();
		for(String code: codeEtChapitre.keySet()){
			for(Couple chap: codeEtChapitre.get(code)){
				if(!resultat.containsKey(code)){
					resultat.put(code, new HashSet<String>());
				}
				resultat.get(code).add(chap.x);
				resultat.get(code).add(chap.y);
			}
		}
		return resultat;
	}
	public HashMap<String, Set<String>> getChapListeCode(HashMap<String,Set<Couple>> codeEtChapitre){
		HashMap<String, Set<String>> resultat= new HashMap<String, Set<String>>();
		for(String code: codeEtChapitre.keySet()){
			for(Couple chap: codeEtChapitre.get(code)){
				if(!resultat.containsKey(chap.y)){
					resultat.put(chap.y, new HashSet<String>());
				}
				resultat.get(chap.y).add(code);
				if(!chap.x.isEmpty()){
					if(!resultat.containsKey(chap.x)){
						resultat.put(chap.x, new HashSet<String>());
					}
					resultat.get(chap.x).add(code);
				}
			}
		}
		return resultat;
	}
	
	public HashMap<String, Set<String>> getChapListeCodeInversion(HashMap<String,List<String>> codeEtChapitre){
		HashMap<String, Set<String>> resultat= new HashMap<String, Set<String>>();
		for(String code: codeEtChapitre.keySet()){
			for(String chap: codeEtChapitre.get(code)){
				if(!resultat.containsKey(chap)){
					resultat.put(chap, new HashSet<String>());
				}
				resultat.get(chap).add(code);
				
			}
		}
		return resultat;
	}

}
