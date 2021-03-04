package filteringStep;

import org.openrdf.repository.RepositoryConnection;

import interactionTripleStore.InteractionBlazgraph;

public class RunHierarchyOrganisation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String namespace = "TLABANDLOINC";
		RepositoryConnection conn= InteractionBlazgraph.connex(namespace);
		DectectionHierarchieCodeTerminologie df= new DectectionHierarchieCodeTerminologie(conn);
				
		
	}

}
