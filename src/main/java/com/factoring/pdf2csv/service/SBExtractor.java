package com.factoring.pdf2csv.service;

import java.util.ArrayList;
import java.util.List;

public class SBExtractor implements DataExtractor{

	@Override
	public List<String[]> extractData(String text, double totalValue, double totalPaid, double iofRate, double fee) {

		List<String[]> data = new ArrayList<>();
		
		String[] lines = text.split("\n");
		 // Imprimir o conteúdo do PDF linha por linha (para depuração)
        System.out.println("Conteúdo do PDF extraído:");
        for (String line : lines) {
            System.out.println(line);
        }

        // Processar cada linha para extrair a tabela
        System.out.println("\nTabela extraída:");
        boolean isTable = false;
        for (String line : lines) {
            if (line.startsWith("Documento")) {
                isTable = true; // Começar a processar a partir da linha do cabeçalho
                continue;
            }
            if (isTable) {
                // Supondo que as colunas da tabela são separadas por espaços inconsistentes
                // Ajuste o delimitador conforme necessário
                String[] columns = line.split("\\s{2,}|\\s(?=\\d{2}/\\d{2}/\\d{4})");  // Usa dois ou mais espaços ou um espaço seguido de uma data como delimitadores
                
                // Verificar se a linha parece ser uma linha da tabela (por exemplo, verificando o número de colunas)
                if (columns.length >= 4) {  // Ajuste o número mínimo de colunas conforme necessário
                    data.add(columns);
                    // Imprimir a linha da tabela
                    System.out.println(String.join(" | ", columns));
                }
            }
        }

        // Retornar a lista de dados extraídos
        return data;
    }

	
}
