package dataDrivenFiltering;



public class DataStructure {
	
	public final String Annotation;
	  public final String Units;

	  
	  public DataStructure(String annotation, String units) {
	   					this.Annotation = annotation;
	   						this.Units = units;
	   					}

	   @Override
	   public boolean equals(Object o_) {
		   DataStructure o = (DataStructure) o_;

	      return (o.Annotation.equals(Annotation) && o.Units.equals(Units));
	   }

	   	@Override
	   public int hashCode()
	   {
	      return 31 * Annotation.hashCode() + Units.hashCode()+178;
	   }
	
	

}
