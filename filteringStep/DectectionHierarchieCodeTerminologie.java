package filteringStep;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import preTraitement.RecuperationGraphRDF;



public class DectectionHierarchieCodeTerminologie {
	
	public DectectionHierarchieCodeTerminologie(RepositoryConnection conn){
		Set<String>lescodesAyantUneNouvelleHierarchie= new HashSet<String>();
		
		String cytologieCHAP1546	 = "http://erias.org/loincTotal#ClAS1544";
	//	String bactériologieSSCHAP1506	 = "http://erias.org/loincTotal#SSCHAP1506";
		//String ObstetriqueGynecoCLAS1529="http://erias.org/loincTotal#CLAS1529";
		//String mycobactériologieSSCHAP1509	 = "http://erias.org/loincTotal#SSCHAP1509";
		String microbiologieCHAP1533	 = "http://erias.org/loincTotal#ClAS1540";
		String sensibilitéAntiBiotiqueClas1540="http://erias.org/loincTotal#ClAS1540";
		String AnalyseUrinaireClas1555="http://erias.org/loincTotal#ClAS1555";
		
	//	String vaccinClas1546= "http://erias.org/loincTotal#ClAS1546";
		String panelCoagulationClas1553="http://erias.org/loincTotal#ClAS1553";

		String panelmicrobiologieCHAP1558	 = "http://erias.org/loincTotal#ClAS1524";

		String testCHAP1548	 = "http://erias.org/loincTotal#ClAS1584";
		String PanelanalysesUrinaire="http://erias.org/loincTotal#ClAS1511";

		String panelBiochimieCHAP1567	 = "http://erias.org/loincTotal#ClAS1532";
		String PanelTestCHAP1518	 = "http://erias.org/loincTotal#ClAS1516";
		String BiochimieCHAP1583	 = "http://erias.org/loincTotal#ClAS1510";
		String testRoutineCHAP1566	 = "http://erias.org/loincTotal#ClAS1561";
		String ImmunoHematoCHAP1532	 = "http://erias.org/loincTotal#ClAS1531";
		String panelImmunoPhenotypaageClAS1521="http://erias.org/loincTotal#ClAS1521";
		String PanelImmunoHematoCHAP1580	 = "http://erias.org/loincTotal#ClAS1598";
		String PanelHNACHAP1503	 = "http://erias.org/loincTotal#ClAS1502";
		String HNACHAP1519	 = "http://erias.org/loincTotal#ClAS1517";
		
	String PatholoMoleculaireCHAP1502	 = "http://erias.org/loincTotal#ClAS1505";
	String BiologiMoleculaireMutationClas1537="http://erias.org/loincTotal#ClAS1537";
	String testCutanéeClas1541="http://erias.org/loincTotal#ClAS1541";

	String BioMolDeletionCHAP1504	 = "http://erias.org/loincTotal#ClAS1542";

	String BioMoleDeletionCHAP1542	 = "http://erias.org/loincTotal#ClAS1562";
		
	String panelHLAClAS1550 ="http://erias.org/loincTotal#ClAS1550";
	String hlaClas1595="http://erias.org/loincTotal#ClAS1595";
	String BiologieMoleculaireRenseignementClas1552="http://erias.org/loincTotal#ClAS1552";
	String BioMolRearCHAP1562	 = "http://erias.org/loincTotal#ClAS1600";
	String anomalieTrisomieCHAP1526	 = "http://erias.org/loincTotal#ClAS1523";
	String genetiqueMoleculCHAP1506	 = "http://erias.org/loincTotal#ClAS1505";
	String biologieMoleculairePharmacogenetiqueClas1579="http://erias.org/loincTotal#ClAS1579";

	String PathoMolecTranslationCHAP1550	 = "http://erias.org/loincTotal#ClAS1593";
//	String BIoloMMCHAP1540	 = "http://erias.org/loincTotal#CHAP1540";
	String BiologieMoleculaireInversionClAS1560="http://erias.org/loincTotal#ClAS1560";
	String BiologieMoleculaireRepetititionsClas1586="http://erias.org/loincTotal#ClAS1586";

	//String PathoMTRCHAP1535	 = "http://erias.org/loincTotal#CHAP1535";
	//String PathologieClAS1573="http://erias.org/loincTotal#ClAS1573";
	//String BiologieMICHAP1522	 = "http://erias.org/loincTotal#CHAP1522";

	//String BiologieMRCHAP1520	 = "http://erias.org/loincTotal#CHAP1520";


	String HematologieCHAP1544	 = "http://erias.org/loincTotal#ClAS1543";
	String coagulationCHAP1510	 = "http://erias.org/loincTotal#ClAS1508";

	String hemostaseCHAP1573	 = "http://erias.org/loincTotal#ClAS1508";

	String PanelHematologieCHAP1549	 = "http://erias.org/loincTotal#ClAS1587";
	String PanelMarqueurCHAP1556	 = "http://erias.org/loincTotal#ClAS1521";

	String PanelSerologieCHAP1521	 = "http://erias.org/loincTotal#ClAS1575";
	String SerologieClAS1540="http://erias.org/loincTotal#ClAS1540";;

	String AutoImmuniteCHAP1565	 = "http://erias.org/loincTotal#ClAS1540";
	String panelAutoImmuniteClAS1575="http://erias.org/loincTotal#ClAS1575";

	String immunoPhenotypageCHAP1529	 = "http://erias.org/loincTotal#ClAS1581";

	String HPACHAP1553	 = "http://erias.org/loincTotal#ClAS1548";
	String panelHPAClAS1603	 = "http://erias.org/loincTotal#ClAS1603";
	String PanelAllergieCHAP1574	 = "http://erias.org/loincTotal#ClAS1571";
	String allergieCHAP1575	 = "http://erias.org/loincTotal#ClAS1556";
	String PanelsAllerCHAP1516	 = "http://erias.org/loincTotal#ClAS1571";
	

	//String MycologieSSCHAP1502	 = "http://erias.org/loincTotal#SSCHAP1502";
	//String ParasitologiSSCHAP1504	 = "http://erias.org/loincTotal#SSCHAP1504";
	
	
	String fertilityCHAP1577	 = "http://erias.org/loincTotal#ClAS1602";	
	String PanelFertilityCHAP1534	 = "http://erias.org/loincTotal#ClAS1585";	
	//String AntimicrobienSSCHAP1510	 = "http://erias.org/loincTotal#SSCHAP1510";
	String antibiogramCHAP1523	 ="http://erias.org/loincTotal#ClAS1540";

	//String genotypageSSCHAP1508	 = "http://erias.org/loincTotal#SSCHAP1508";
	String PanelAntibiogrammCHAP1563	 = "http://erias.org/loincTotal#ClAS1568";

	//String PanelMedicamentCHAP1554	 = "http://erias.org/loincTotal#CHAP1554";
	String PanelToxicoCHAP1551	 = "http://erias.org/loincTotal#ClAS1504";

	String MedicamentToxicoCHAP1552	 = "http://erias.org/loincTotal#ClAS1559";
	String MedicamentetToxicoClAS1591	 = "http://erias.org/loincTotal#ClAS1591";


	String TraitementRenseCHAP1530	 = "http://erias.org/loincTotal#ClAS1527";

	String DoseMedicamentCHAP1514	 = "http://erias.org/loincTotal#ClAS1512";

	//String virologieSSCHAP1507	 = "http://erias.org/loincTotal#SSCHAP1507";
	
	
	
	String URIduGraph="http://erias.org/integration/OntologieLocale";
	Set<Statement> lesNouvellesHierarchie= new HashSet<Statement>();
	RecuperationGraphRDF codeEtSesAscendant = new RecuperationGraphRDF(conn, "CodeEtSesAscendant", URIduGraph);
	ValueFactory vf = conn.getValueFactory();
	
	System.out.println("codeEtSesAscendant "+codeEtSesAscendant.getRDFlisteLibellé().size());
	for(String codeTerminologie: codeEtSesAscendant.getRDFlisteLibellé().keySet()){
		Set<String> asecendant= new HashSet<String>();
		asecendant.addAll(codeEtSesAscendant.getRDFlisteLibellé().get(codeTerminologie));
		System.out.println("asecendant "+asecendant);
		if(asecendant.contains("http://chu-bordeaux.fr/synergie#syn-fam-a")){
			
			Set<String> lesCorrespondantLoinc= new HashSet<String>();
			lesCorrespondantLoinc.add(	AnalyseUrinaireClas1555);
			lesCorrespondantLoinc.add(PanelanalysesUrinaire);
		lesCorrespondantLoinc.add(cytologieCHAP1546);
		lesNouvellesHierarchie.addAll(getStamentRelation(codeTerminologie, lesCorrespondantLoinc, vf));
		lescodesAyantUneNouvelleHierarchie.add(codeTerminologie);
		}
		
		if(asecendant.contains("http://chu-bordeaux.fr/synergie#syn-fam-b")){
			
			Set<String> lesCorrespondantLoinc= new HashSet<String>();
		//lesCorrespondantLoinc.add(bactériologieSSCHAP1506);
		//lesCorrespondantLoinc.add(genotypageSSCHAP1508);
			lesCorrespondantLoinc.add(	AnalyseUrinaireClas1555);
			lesCorrespondantLoinc.add(PanelanalysesUrinaire);
		//lesCorrespondantLoinc.add(sensibilitéAntiBiotiqueClas1540);
			
		lesCorrespondantLoinc.add(microbiologieCHAP1533);
		lesCorrespondantLoinc.add(panelmicrobiologieCHAP1558);
		lescodesAyantUneNouvelleHierarchie.add(codeTerminologie);
		
		lesNouvellesHierarchie.addAll(getStamentRelation(codeTerminologie, lesCorrespondantLoinc, vf));
		}
		
		if(asecendant.contains("http://chu-bordeaux.fr/synergie#syn-fam-c")){
			
		Set<String> lesCorrespondantLoinc= new HashSet<String>();
		lesCorrespondantLoinc.add(testCHAP1548);
		lesCorrespondantLoinc.add(AnalyseUrinaireClas1555);
		lesCorrespondantLoinc.add(testCutanéeClas1541);
		lesCorrespondantLoinc.add(PanelanalysesUrinaire);
		lesCorrespondantLoinc.add(panelBiochimieCHAP1567);
		lesCorrespondantLoinc.add(PanelTestCHAP1518);
		lesCorrespondantLoinc.add(BiochimieCHAP1583);
		lesCorrespondantLoinc.add(testRoutineCHAP1566);
		lescodesAyantUneNouvelleHierarchie.add(codeTerminologie);
		
		lesNouvellesHierarchie.addAll(getStamentRelation(codeTerminologie, lesCorrespondantLoinc, vf));
		}
		
		if(asecendant.contains("http://chu-bordeaux.fr/synergie#syn-fam-f")){
			
			Set<String> lesCorrespondantLoinc= new HashSet<String>();
			lesCorrespondantLoinc.add(ImmunoHematoCHAP1532);
			lesCorrespondantLoinc.add(panelHLAClAS1550);
			lesCorrespondantLoinc.add(hlaClas1595);
			lesCorrespondantLoinc.add(PanelImmunoHematoCHAP1580);
			lesCorrespondantLoinc.add(PanelHNACHAP1503);
			lesCorrespondantLoinc.add(HNACHAP1519);
			lescodesAyantUneNouvelleHierarchie.add(codeTerminologie);
			
			lesNouvellesHierarchie.addAll(getStamentRelation(codeTerminologie, lesCorrespondantLoinc, vf));
		}
		
		
		if(asecendant.contains("http://chu-bordeaux.fr/synergie#syn-fam-g")){
			
			Set<String> lesCorrespondantLoinc= new HashSet<String>();
			lesCorrespondantLoinc.add(PatholoMoleculaireCHAP1502);
			lesCorrespondantLoinc.add(BioMolDeletionCHAP1504);
			lesCorrespondantLoinc.add(BioMoleDeletionCHAP1542);
			lesCorrespondantLoinc.add(BioMolRearCHAP1562);
			lesCorrespondantLoinc.add(anomalieTrisomieCHAP1526);
			lesCorrespondantLoinc.add(genetiqueMoleculCHAP1506);
			lesCorrespondantLoinc.add(PathoMolecTranslationCHAP1550);
			lesCorrespondantLoinc.add(BiologiMoleculaireMutationClas1537);
			lesCorrespondantLoinc.add(BiologieMoleculaireRenseignementClas1552);
			lesCorrespondantLoinc.add(biologieMoleculairePharmacogenetiqueClas1579);
			lesCorrespondantLoinc.add(BiologieMoleculaireInversionClAS1560);
			lesCorrespondantLoinc.add(BiologieMoleculaireRepetititionsClas1586);
			lesCorrespondantLoinc.add(panelHLAClAS1550);
			lesCorrespondantLoinc.add(hlaClas1595);
			//lesCorrespondantLoinc.add(BIoloMMCHAP1540);
			//lesCorrespondantLoinc.add(PathoMTRCHAP1535);
			//lesCorrespondantLoinc.add(BiologieMICHAP1522);
			//lesCorrespondantLoinc.add(BiologieMRCHAP1520);
			lescodesAyantUneNouvelleHierarchie.add(codeTerminologie);
			
			lesNouvellesHierarchie.addAll(getStamentRelation(codeTerminologie, lesCorrespondantLoinc, vf));
		}
		if(asecendant.contains("http://chu-bordeaux.fr/synergie#syn-fam-h")){
			
			Set<String> lesCorrespondantLoinc= new HashSet<String>();
			lesCorrespondantLoinc.add(HematologieCHAP1544);
			lesCorrespondantLoinc.add(coagulationCHAP1510);
			lesCorrespondantLoinc.add(hemostaseCHAP1573);
			lesCorrespondantLoinc.add(panelCoagulationClas1553);
			lesCorrespondantLoinc.add(PanelHematologieCHAP1549);
			lescodesAyantUneNouvelleHierarchie.add(codeTerminologie);
			
			lesNouvellesHierarchie.addAll(getStamentRelation(codeTerminologie, lesCorrespondantLoinc, vf));
		}
		
		if(asecendant.contains("http://chu-bordeaux.fr/synergie#syn-fam-i")){
			
			Set<String> lesCorrespondantLoinc= new HashSet<String>();
				lesCorrespondantLoinc.add(HematologieCHAP1544);
			lesCorrespondantLoinc.add(PanelHematologieCHAP1549);
			lesCorrespondantLoinc.add(hemostaseCHAP1573);
			lesCorrespondantLoinc.add(coagulationCHAP1510);
			
			lesCorrespondantLoinc.add(PanelMarqueurCHAP1556);
			lesCorrespondantLoinc.add(PanelSerologieCHAP1521);
			lesCorrespondantLoinc.add(SerologieClAS1540);
			lesCorrespondantLoinc.add(testCutanéeClas1541);
			lesCorrespondantLoinc.add(AutoImmuniteCHAP1565);
			lesCorrespondantLoinc.add(immunoPhenotypageCHAP1529);
			lesCorrespondantLoinc.add(panelImmunoPhenotypaageClAS1521);
			lesCorrespondantLoinc.add(HPACHAP1553);
			lesCorrespondantLoinc.add(panelHPAClAS1603);
			lesCorrespondantLoinc.add(panelAutoImmuniteClAS1575);
			lesCorrespondantLoinc.add(PanelAllergieCHAP1574);
			lesCorrespondantLoinc.add(allergieCHAP1575);
			lesCorrespondantLoinc.add(PanelsAllerCHAP1516);
			lesCorrespondantLoinc.add(PanelHNACHAP1503);
			lesCorrespondantLoinc.add(HNACHAP1519);
			lesCorrespondantLoinc.add(panelHLAClAS1550);
			lesCorrespondantLoinc.add(hlaClas1595);
			lesCorrespondantLoinc.add(panelBiochimieCHAP1567);
			lesCorrespondantLoinc.add(BiochimieCHAP1583);
			lescodesAyantUneNouvelleHierarchie.add(codeTerminologie);
			
			lesNouvellesHierarchie.addAll(getStamentRelation(codeTerminologie, lesCorrespondantLoinc, vf));
		}
		
		if(asecendant.contains("http://chu-bordeaux.fr/synergie#syn-fam-m")){
			
			Set<String> lesCorrespondantLoinc= new HashSet<String>();
			//lesCorrespondantLoinc.add(MycologieSSCHAP1502);
			lesCorrespondantLoinc.add(microbiologieCHAP1533);
			//lesCorrespondantLoinc.add(ParasitologiSSCHAP1504);
			lesCorrespondantLoinc.add(panelmicrobiologieCHAP1558);
			lescodesAyantUneNouvelleHierarchie.add(codeTerminologie);
			
			lesNouvellesHierarchie.addAll(getStamentRelation(codeTerminologie, lesCorrespondantLoinc, vf));
		}
		if(asecendant.contains("http://chu-bordeaux.fr/synergie#syn-fam-n")) {
			Set<String> lesCorrespondantLoinc= new HashSet<String>();
			lesCorrespondantLoinc.add(panelHLAClAS1550);
			lesCorrespondantLoinc.add(hlaClas1595);
			lesCorrespondantLoinc.add(panelBiochimieCHAP1567);
			lesCorrespondantLoinc.add(BiochimieCHAP1583);
			lescodesAyantUneNouvelleHierarchie.add(codeTerminologie);
			
			lesNouvellesHierarchie.addAll(getStamentRelation(codeTerminologie, lesCorrespondantLoinc, vf));
		}
		if(asecendant.contains("http://chu-bordeaux.fr/synergie#syn-fam-t")) {
			Set<String> lesCorrespondantLoinc= new HashSet<String>();
			lesCorrespondantLoinc.add(panelHLAClAS1550);
			lesCorrespondantLoinc.add(hlaClas1595);
			lescodesAyantUneNouvelleHierarchie.add(codeTerminologie);
			
			lesNouvellesHierarchie.addAll(getStamentRelation(codeTerminologie, lesCorrespondantLoinc, vf));
		}
		if(asecendant.contains("http://chu-bordeaux.fr/synergie#syn-fam-o")){
			
			Set<String> lesCorrespondantLoinc= new HashSet<String>();
			lesCorrespondantLoinc.add(microbiologieCHAP1533);
			lesCorrespondantLoinc.add(panelmicrobiologieCHAP1558);
			lesCorrespondantLoinc.add(panelBiochimieCHAP1567);
			lesCorrespondantLoinc.add(BiochimieCHAP1583);
			lesCorrespondantLoinc.add(fertilityCHAP1577);
			lesCorrespondantLoinc.add(PanelFertilityCHAP1534);
			lescodesAyantUneNouvelleHierarchie.add(codeTerminologie);
			lesNouvellesHierarchie.addAll(getStamentRelation(codeTerminologie, lesCorrespondantLoinc, vf));
		}
		
		if(asecendant.contains("http://chu-bordeaux.fr/synergie#syn-fam-p")){
			
			Set<String> lesCorrespondantLoinc= new HashSet<String>();
			//lesCorrespondantLoinc.add(AntimicrobienSSCHAP1510);
			lesCorrespondantLoinc.add(antibiogramCHAP1523);
			//lesCorrespondantLoinc.add(genotypageSSCHAP1508);
			lesCorrespondantLoinc.add(PanelAntibiogrammCHAP1563);
			lesCorrespondantLoinc.add(sensibilitéAntiBiotiqueClas1540);
			lesCorrespondantLoinc.add(PanelToxicoCHAP1551);
			lesCorrespondantLoinc.add(MedicamentToxicoCHAP1552);
			lesCorrespondantLoinc.add(MedicamentetToxicoClAS1591);
			lesCorrespondantLoinc.add(TraitementRenseCHAP1530);
			lesCorrespondantLoinc.add(DoseMedicamentCHAP1514);
			lescodesAyantUneNouvelleHierarchie.add(codeTerminologie);
			
			lesNouvellesHierarchie.addAll(getStamentRelation(codeTerminologie, lesCorrespondantLoinc, vf));
		}
		
		if(asecendant.contains("http://chu-bordeaux.fr/synergie#syn-fam-v")){
			
			Set<String> lesCorrespondantLoinc= new HashSet<String>();
			//lesCorrespondantLoinc.add(virologieSSCHAP1507);
			lesCorrespondantLoinc.add(microbiologieCHAP1533);
			lesCorrespondantLoinc.add(panelmicrobiologieCHAP1558);
			lescodesAyantUneNouvelleHierarchie.add(codeTerminologie);
			
			lesNouvellesHierarchie.addAll(getStamentRelation(codeTerminologie, lesCorrespondantLoinc, vf));
		}
		
		
	
	
	}
	
	System.out.println("lescodesAyantUneNouvelleHierarchie "+lescodesAyantUneNouvelleHierarchie.size());
	
	String graphAncrageHierarchie=	"http://erias.org/integration/AnchoringCandidats/newHierarchy";
	
	URI AncrageURI = conn.getValueFactory().createURI(graphAncrageHierarchie);
	List<Statement> liste= new ArrayList<Statement>(lesNouvellesHierarchie);
	System.out.println("filtrage hierarchir "+liste.size());
	
	System.out.println("debut d'insertion");
	try {
		
		conn.add(liste,AncrageURI);
	} catch (RepositoryException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	System.out.println("fin d'insertion");
	
	
	

	}
	public Set<Statement> getStamentRelation(String codeTerm, Set<String>NouveauChapitre,ValueFactory vf){
		Set<Statement> lesChapitresLonc= new HashSet<Statement>();
		
		for(String chap: NouveauChapitre){
			Statement hty=vf.createStatement(vf.createURI(codeTerm), 
					vf.createURI("http://erias.org/integration/model#APourNouvelleHierarchie"),
					vf.createURI(chap));
			lesChapitresLonc.add(hty);
		}
		return lesChapitresLonc;
	}

}
