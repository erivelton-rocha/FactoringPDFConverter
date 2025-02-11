package com.factoring.pdf2csv.model;

public enum BankEnum {
	FIDC_RG_165("FIDC R&G - 165"),
	SB("SB");
	
	private final String displayName;
	
	BankEnum(String displayName){
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	@Override
	public String toString() {
		return displayName;
	}
}
