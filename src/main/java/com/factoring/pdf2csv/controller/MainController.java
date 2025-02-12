package com.factoring.pdf2csv.controller;

import java.io.File;
import java.util.List;
import com.factoring.pdf2csv.model.BankEnum;
import com.factoring.pdf2csv.service.DataExtractor;
import com.factoring.pdf2csv.service.DataExtractorFactory;
import com.factoring.pdf2csv.util.PdfToTextUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class MainController {
    @FXML
    private TextField filePathField;
    @FXML
    private ComboBox<BankEnum> bankComboBox;
    @FXML
    private TextField totalValueField;
    @FXML
    private TextField totalPaidField;
    @FXML
    private TextField iofRateField;
    @FXML
    private TextField feeField;
    @FXML
    private Label statusLabel;
    private File selectedFile;

    @FXML
    private void initialize() {
        bankComboBox.getItems().addAll(BankEnum.values());
    }

    @FXML
    private void selectPdfFile() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos PDF", "*.pdf"));
            selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                System.out.println("Arquivo selecionado: " + selectedFile.getAbsolutePath());
                filePathField.setText(selectedFile.getAbsolutePath());
            } else {
                System.out.println("Nenhum arquivo foi selecionado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Erro ao selecionar arquivo.");
        }
    }

    @FXML
    private void convertToCsv() {
        if (selectedFile == null || bankComboBox.getValue() == null) {
            statusLabel.setText("Selecione um arquivo e um banco.");
            return;
        }

        try {
            System.out.println("Iniciando a conversão do PDF para texto...");
            
            // Converter o PDF para texto e salvar em um arquivo TXT
            File txtFile = new File(selectedFile.getParent(), "dados_extraidos.txt");
            PdfToTextUtil.saveToTextFile(selectedFile, txtFile);
            statusLabel.setText("Gerado arquivo txt: " + txtFile.getAbsolutePath());
            System.out.println("Arquivo TXT gerado: " + txtFile.getAbsolutePath());

            // Obter o extrator correspondente ao banco selecionado
            DataExtractor extractor = DataExtractorFactory.getExtractor(bankComboBox.getValue());
            System.out.println("Extrator obtido para o banco: " + bankComboBox.getValue());

            // Ler os valores dos campos de texto
            double totalValue = parseDoubleOrDefault(totalValueField.getText(), 0.0);
            double totalPaid = parseDoubleOrDefault(totalPaidField.getText(), 0.0);
            double iofRate = parseDoubleOrDefault(iofRateField.getText(), 0.0);
            double fee = parseDoubleOrDefault(feeField.getText(), 0.0);
            System.out.println("Valores lidos: Total Value=" + totalValue + ", Total Paid=" + totalPaid + ", IOF Rate=" + iofRate + ", Fee=" + fee);

            statusLabel.setText("Finalizado por enquanto...");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Erro ao gerar TXT.");
        }
    }

    private double parseDoubleOrDefault(String text, double defaultValue) {
        try {
            return text.isEmpty() ? defaultValue : Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
