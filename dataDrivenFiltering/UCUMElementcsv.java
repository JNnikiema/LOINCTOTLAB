package dataDrivenFiltering;


public class UCUMElementcsv {
	
	public final String Code;
	public final String Descriptive_Name;
	public final String Synonym;
	public final String Kind_of_Quantity;
	public final String ConceptID;
	public UCUMElementcsv(String code,
	String descriptive_Name,
	String synonym,
	String kind_of_Quantity,
	String conceptID) {

		
		this.Code=code;
		this.Descriptive_Name=descriptive_Name;
		this.Synonym=synonym;
		this.Kind_of_Quantity=kind_of_Quantity;
		this.ConceptID=conceptID;
		
	
	}
	 public boolean equals(Object o_) {
		 UCUMElementcsv o = (UCUMElementcsv) o_;

	      return (o.Code.equals(Code) && o.Descriptive_Name.equals(Descriptive_Name)&&o.Synonym.equals(Synonym)
	    		  && o.Kind_of_Quantity.equals(Kind_of_Quantity) &&o.ConceptID.equals(ConceptID));
	   }

	   	@Override
	   public int hashCode()
	   {
	      return 31 * Code.hashCode()+Synonym.hashCode() + 21* Kind_of_Quantity.hashCode()+12*ConceptID.hashCode()+Descriptive_Name.hashCode()+178;
	   }
	


}
