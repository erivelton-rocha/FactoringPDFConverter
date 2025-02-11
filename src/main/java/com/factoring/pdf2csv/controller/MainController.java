package com.factoring.pdf2csv.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.factoring.pdf2csv.model.BankEnum;
import com.factoring.pdf2csv.service.DataExtractor;
import com.factoring.pdf2csv.service.DataExtractorFactory;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class MainController {
    @FXML private TextField filePathField;
    @FXML private ComboBox<BankEnum> bankComboBox;
    @FXML private TextField totalValueField;
    @FXML private TextField totalPaidField;
    @FXML private TextField iofRateField;
    @FXML private TextField feeField;
    @FXML private Label statusLabel;
    private File selectedFile;

    @FXML
    private void initialize() {
        bankComboBox.getItems().addAll(BankEnum.values());
    }

    @FXML
    private void selectPdfFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos PDF", "*.pdf"));
        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void convertToCsv() {
        if (selectedFile == null || bankComboBox.getValue() == null) {
            statusLabel.setText("Selecione um arquivo e um banco.");
            return;
        }
        try {
            String extractedText = extractTextFromPdf(selectedFile);
            DataExtractor extractor = DataExtractorFactory.getExtractor(bankComboBox.getValue());
            double totalValue = Double.parseDouble(totalValueField.getText());
            double totalPaid = Double.parseDouble(totalPaidField.getText());
            double iofRate = Double.parseDouble(iofRateField.getText());
            double fee = Double.parseDouble(feeField.getText());
            List<String[]> extractedData = extractor.extractData(extractedText, totalValue, totalPaid, iofRate, fee);
            File txtFile = new File(selectedFile.getParent(), "dados_extraidos.txt");
            saveDataToTxt(txtFile, extractedData);
            statusLabel.setText("Arquivo TXT gerado com sucesso!");

            // Reprocessar o arquivo TXT
            reprocessTxt(txtFile, extractor);
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Erro ao gerar TXT.");
        }
    }

    private String extractTextFromPdf(File pdfFile) throws IOException {
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }

    private void saveDataToTxt(File file, List<String[]> data) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (String[] row : data) {
                writer.println(String.join(" | ", row));
            }
        }
    }

    private void reprocessTxt(File txtFile, DataExtractor extractor) {
        try {
            String extractedText = new String(Files.readAllBytes(txtFile.toPath()));
            List<String[]> reprocessedData = extractor.reprocessData(extractedText);
            File reprocessedFile = new File(txtFile.getParent(), "dados_reprocessados.txt");
            saveDataToTxt(reprocessedFile, reprocessedData);
            statusLabel.setText("Texto reprocessado com sucesso! Arquivo salvo como dados_reprocessados.txt.");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Erro ao reprocessar texto do TXT.");
        }
    }
}
