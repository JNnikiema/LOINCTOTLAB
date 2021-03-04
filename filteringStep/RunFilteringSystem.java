package filteringStep;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import interactionTripleStore.InteractionBlazgraph;

public class RunFilteringSystem {

	public static void main(String[] args) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		// TODO Auto-generated method stub
		String namespace = "TLABANDLOINC";
		System.out.println("début");
		RepositoryConnection conn= InteractionBlazgraph.connex(namespace);
		System.out.println("ok");
		//FiltrageSelonLeMilieuBiologique df= new FiltrageSelonLeMilieuBiologique(conn);
		FilteringBytechnic df= new FilteringBytechnic(conn);
		//FilteringByTime df= new FilteringByTime(conn);
		//FilteringByChallenge df = new FilteringByChallenge(conn);
		System.out.println("filtering over ");
		
	}

}
