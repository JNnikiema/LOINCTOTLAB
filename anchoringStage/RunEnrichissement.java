package anchoringStage;

import org.openrdf.repository.RepositoryConnection;

import interactionTripleStore.InteractionBlazgraph;

public class RunEnrichissement {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String namespace = "TLABANDLOINC";
		RepositoryConnection conn= InteractionBlazgraph.connex(namespace);
		
		EnrichissementGlobaleDesDescendant enr= new EnrichissementGlobaleDesDescendant(conn);
		System.out.println("erichissement fini");

	}

}
