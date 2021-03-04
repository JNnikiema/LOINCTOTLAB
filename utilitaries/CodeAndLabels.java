package utilitaries;

import java.util.Set;

public class CodeAndLabels {
	
	
	public final String Code;
	  public final Set<String> Labels;

		  
		  public CodeAndLabels(String code, Set<String> labels) {
		   			this.Code= code;
		   			this.Labels = labels;
		   					}

		   @Override
		   public boolean equals(Object o_) {
			   CodeAndLabels o = (CodeAndLabels) o_;

		      return (o.Code.equals(Code));
		   }

		   	@Override
		   public int hashCode()
		   {
		      return 31 * Code.hashCode() + Code.hashCode()+178;
		   }
		

	

}
