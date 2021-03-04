package anchoringStage;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import interactionTripleStore.InteractionBlazgraph;

public class RunAnchoringCandidates {

	public static void main(String[] args) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		// TODO Auto-generated method stub
		String namespace = "TLABANDLOINC";
		RepositoryConnection conn= InteractionBlazgraph.connex(namespace);
		
		AncrageAuCodeLoinc gf= new AncrageAuCodeLoinc(conn);
		System.out.println("finish anchoring corrections");
	}

}
