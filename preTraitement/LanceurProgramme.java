package preTraitement;

import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import interactionTripleStore.InteractionBlazgraph;



public class LanceurProgramme {

	public static void main(String[] args) throws RepositoryException {
		// TODO Auto-generated method stub
		System.out.println("debut");
		System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
		
		
		System.out.println("connexion a la base de données my sql effectué avec succès effectué avec succès");
		// la base de predilection est BaseAnchoring
		//String namespace = "BaseAnchoring";	
		//String namespace ="TLAB";
		String namespace = "TLABANDLOINC";
		
		RepositoryConnection conn = InteractionBlazgraph.connex(namespace);
		System.out.println("etape de la connexion au blazgraph franchis");
		
		RecuperationGraphRDF inter = new RecuperationGraphRDF(conn, "b","http://erias.org/InterfaceTerminology/TLAB");
		
		DemarcheDeSegmentation hhhh=new DemarcheDeSegmentation(inter.getRDFlisteLibellé());
		ProcessusDeCorrectionParWikipedia util= new ProcessusDeCorrectionParWikipedia(inter.getRDFlisteLibellé());
		CorrectionLibelle interfaceTT= new CorrectionLibelle(util.getresultatCorrection(),namespace);
		
		System.out.println("debut export ........");
		ExportGraph gh= new ExportGraph("http://erias.org/InterfaceTerminology/TLAB/Correction", namespace,"/home/erias/Bureau/graph/TLABCORRECTIONS/abbreviationsCorrections.ttl");
		
			try {
				conn.close(); 
			} catch (final RepositoryException ignore) { 
				System.out.println("problème de fermeture de la connexion");
			}
		
		System.out.println("interface final termineeeeeeeeeeee");
		
		

	}

}
