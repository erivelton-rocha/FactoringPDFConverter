package com.factoring.pdf2csv.service;

import java.util.ArrayList;
import java.util.List;

public class SBExtractor implements DataExtractor {

    @Override
    public List<String[]> extractData(String text, double totalValue, double totalPaid, double iofRate, double fee) {
        List<String[]> data = new ArrayList<>();
        
        // Dividir o texto extraído em linhas usando o caractere de nova linha (\n)
        String[] lines = text.split("\\r?\\n");

        // Processar cada linha para separar os campos corretamente
        for (String line : lines) {
            // Expressão regular para separar os campos
            String[] columns = line.split("(?<=\\d{4}-\\d{4})\\s+(?=\\d{2}/\\d{2}/\\d{4})");

            // Adicionar a linha processada à lista de dados, se tiver pelo menos 2 colunas
            if (columns.length == 2) {
                String documento = columns[0];
                String vencimentoValorSacado = columns[1];

                // Adicionar a linha processada à lista de dados
                data.add(new String[] { documento, vencimentoValorSacado });
            }
        }

        // Retornar a lista de dados extraídos
        return data;
    }

    @Override
    public List<String[]> reprocessData(String text) {
        List<String[]> data = new ArrayList<>();
        
        // Dividir o texto extraído em linhas usando o caractere de nova linha (\n)
        String[] lines = text.split("\\r?\\n");

        // Processar cada linha para separar os campos corretamente
        for (String line : lines) {
            // Separar documento e vencimento/valor/sacado
            String[] parts = line.split("\\s+(?=\\d{2}/\\d{2}/\\d{4}\\s+\\d{1,3},\\d{2})");

            if (parts.length == 2) {
                String documento = parts[0];
                String vencimentoValorSacado = parts[1];

                // Expressão regular para separar vencimento, valor e sacado
                String[] vencimentoValorSacadoParts = vencimentoValorSacado.split("(?<=\\d{2}/\\d{2}/\\d{4})\\s+(?=\\d{1,3},\\d{2})");

                if (vencimentoValorSacadoParts.length == 2) {
                    String vencimento = vencimentoValorSacadoParts[0];
                    String valorSacado = vencimentoValorSacadoParts[1];

                    // Separar valor do sacado
                    String valor = valorSacado.replaceAll("^(\\d{1,3},\\d{2}).*", "$1");
                    String sacado = valorSacado.replaceFirst("^\\d{1,3},\\d{2}", "").trim();

                    // Adicionar a linha processada à lista de dados
                    data.add(new String[] { documento, vencimento, valor, sacado });
                }
            }
        }

        // Retornar a lista de dados reprocessados
        return data;
    }
}
