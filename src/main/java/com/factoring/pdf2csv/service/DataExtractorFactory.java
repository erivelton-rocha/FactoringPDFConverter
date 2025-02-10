package com.factoring.pdf2csv.service;

import java.util.HashMap;
import java.util.Map;

import com.factoring.pdf2csv.model.BankEnum;

public class DataExtractorFactory {
	private static final Map<BankEnum, DataExtractor> BANKTOEXTRACTOR_MAP = new HashMap<>();

	static {
	    BANKTOEXTRACTOR_MAP.put(BankEnum.FIDC_RG_165, new FidicRG165Extractor());
	    BANKTOEXTRACTOR_MAP.put(BankEnum.SB, new SBExtractor());
	   
	}
	
	/**
	 * Retorna um objeto extrator de dados baseado no nome do banco
	 * 
	 * @param bankName O nome do banco selecionado.
	 * @return Uma instância de DataExtrator correspondete.
	 * @throws IllegalArgumentException Se o banco ão for suportado.
	 */

	public static DataExtractor getExtractor(BankEnum bank) {
		DataExtractor extractor = BANKTOEXTRACTOR_MAP.get(bank);
		if (extractor == null) {
			throw new IllegalArgumentException("Banco não suportado: " + bank);
		}
		return extractor;
	}

}
