package com.factoring.pdf2csv.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
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
            String[] parts = line.split("\\|");

            if (parts.length == 2) {
                String documento = parts[0].trim();
                String vencimentoValorSacado = parts[1].trim();

                // Expressão regular para separar vencimento, valor e sacado
                String[] vencimentoValorSacadoParts = vencimentoValorSacado.split("(?<=\\d{2}/\\d{2}/\\d{4})\\s+");

                if (vencimentoValorSacadoParts.length == 2) {
                    String vencimento = vencimentoValorSacadoParts[0];
                    String valorSacado = vencimentoValorSacadoParts[1];

                    // Separar valor do sacado
                    String valor = valorSacado.replaceAll("^(\\d{1,3}(?:\\.\\d{3})*,\\d{2}).*", "$1");
                    String sacado = valorSacado.replaceFirst("^\\d{1,3}(?:\\.\\d{3})*,\\d{2}", "").trim();

                    // Adicionar a linha processada à lista de dados
                    data.add(new String[] { documento, vencimento, valor, sacado });
                }
            }
        }

        // Retornar a lista de dados reprocessados
        return data;
    }

    @Override
    public void reprocessTxt(File txtFile, File outputFile) throws IOException {
        String extractedText = new String(Files.readAllBytes(txtFile.toPath()));
        List<String[]> reprocessedData = reprocessData(extractedText);
        saveDataToTxt(outputFile, reprocessedData);
    }

    @Override
    public void processarDocumentos(File inputFile, File outputFile) throws IOException {
        List<String[]> data = new ArrayList<>();
        
        // Ler o conteúdo do arquivo de entrada
        String content = new String(Files.readAllBytes(inputFile.toPath()));
        String[] lines = content.split("\\r?\\n");

        // Processar cada linha para separar título e parcela
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 5) { // Ajuste para garantir que todas as colunas estão presentes
                String documento = parts[0].trim();
                String[] docInfo = parseDocumento(documento);
                String titulo = docInfo[0];
                String parcela = docInfo[1];
                String vencimento = parts[2].trim();
                String valor = parts[3].trim();
                String sacado = parts[4].trim();

                // Adicionar a linha processada à lista de dados
                data.add(new String[] { titulo, parcela, vencimento, valor, sacado });
            } else {
                System.err.println("Linha inválida: " + line);
            }
        }

        // Salvar os dados processados em um novo arquivo
        saveDataToTxt(outputFile, data);
    }

    private String[] parseDocumento(String documento) {
        // Separar o título e a parcela
        String[] parts = documento.split("-");
        String titulo = parts[0];
        String parcela = parts[1].substring(0, 2);

        return new String[] { titulo, parcela };
    }

    private void saveDataToTxt(File file, List<String[]> data) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (String[] row : data) {
                writer.println(String.join(" | ", row));
            }
        }
    }
}
