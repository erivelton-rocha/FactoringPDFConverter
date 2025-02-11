package com.factoring.pdf2csv.service;

import java.util.List;

public interface DataExtractor {
	/**
	 * Extrair os dados do pdf e os estrutura em uma lsita de arrays de strings
	 * 
	 * @param texto extraido do pdf
	 * @return Lista de arrays de string representando os dados
	 */
	List<String[]> extractData(String text, double totalValue, double totalPaid, double iofRate, double fee);
	List<String[]> reprocessData(String text); // Adicionando o método reprocessData
}
