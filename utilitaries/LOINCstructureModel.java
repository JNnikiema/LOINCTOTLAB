package utilitaries;

public class LOINCstructureModel {
	
	private String CodeLoinc;
	private String LoincName;
	private String PartCode;
	private String PartName;
	private String PartType;
	private String LinkType;
	public LOINCstructureModel(
			String Loinc_NUM,
			String LoinLongName,
			String LoincPartCode,
			String LoincPartName,
			String LoincPartType,
			String LoincLinkType) {
		CodeLoinc=Loinc_NUM;
		LoincName=LoinLongName;
		PartCode=LoincPartCode;
		PartName=LoincPartName;
		PartType=LoincPartType;
		LinkType=LoincLinkType;
		
	}
	
	public String getCodeLoinc() {
		return CodeLoinc;
	}
	public String getLoincName() {
		return LoincName;
	}
	public String getPartCode() {
		return PartCode;
	}
	public String getPartName() {
		return PartName;
	}
	public String getPartType() {
		return PartType;
	}
	public String getLinkType() {
		return LinkType;
	}
	public boolean equals(Object o_) {
		LOINCstructureModel o = (LOINCstructureModel) o_;
	      return (o.getCodeLoinc().equals(CodeLoinc) && o.getLoincName().equals(LoincName)&&o.getPartCode().equals(PartCode)&&o.getPartName().equals(PartName)&&o.getPartType().equals(PartType)&&o.getLinkType().equals(LinkType));
	   }
	
	
	

}
