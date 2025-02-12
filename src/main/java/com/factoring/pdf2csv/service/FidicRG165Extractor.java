package com.factoring.pdf2csv.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FidicRG165Extractor implements DataExtractor {

	
	
	@Override
	public void reprocessTxt(File txtFile, File outputFile) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processarDocumentos(File inputFile, File outputFile) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String[]> extractData(String text, double totalValue, double totalPaid, double iofRate, double fee) {

		List<String[]> data = new ArrayList<>();
		
		String[] lines = text.split("\n");
		// Imprimir o conteúdo do PDF linha por linha
        System.out.println("Conteúdo do PDF extraído:");
        for (String line : lines) {
            System.out.println(line);
        }

        // Retornar a lista de dados extraídos (ainda vazia neste exemplo)
        return data;
	}

	@Override
	public List<String[]> reprocessData(String text) {
		// TODO Auto-generated method stub
		return null;
	}

}
