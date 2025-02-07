package com.factoring.pdf2csv.updater;

public class VersionInfo {
	private String verstion;
	private String downloadUrl;
	
	public VersionInfo(String verstion, String downloadUrl) {
		super();
		this.verstion = verstion;
		this.downloadUrl = downloadUrl;
	}
	
	
	
	public String getVerstion() {
		return verstion;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	

}
