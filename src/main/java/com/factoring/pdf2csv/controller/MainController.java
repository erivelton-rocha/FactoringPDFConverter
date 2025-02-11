package com.factoring.pdf2csv.controller;

import java.io.File;
import java.io.IOException;
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
	// Campos do FXML
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
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos pdf", "*.pdf"));
		selectedFile = fileChooser.showOpenDialog(null);
		
		if(selectedFile != null) {
			filePathField.setText(selectedFile.getAbsolutePath());
		}
	}
	
	
	@FXML
	private void convertToCsv() {
		if(selectedFile == null || bankComboBox.getValue()== null) {
			statusLabel.setText("Selecione um arquivo e um banco");
			return;
		}
		try {
			String extractedText = extractTextFromPdf(selectedFile);
			
			DataExtractor extractor = DataExtractorFactory.getExtractor(bankComboBox.getValue());
			
			
//			  // Passar os novos valores para o extrator
//            double totalValue = Double.parseDouble(totalValueField.getText());
//            double totalPaid = Double.parseDouble(totalPaidField.getText());
//            double iofRate = Double.parseDouble(iofRateField.getText());
//            double fee = Double.parseDouble(feeField.getText());
//            
           // List<String[]> extractedData = extractor.extractData(extractedText, totalValue, 
            //		totalPaid, iofRate, fee);
			
			List<String[]> extracData = extractor.extractData(extractedText,0, 0, 0, 0);
			statusLabel.setText("Arquivo gerado com sucesso");
			
		}catch(Exception e){
			e.printStackTrace();
			statusLabel.setText("Erro ao gerar CSV");
		}
		
	}
	
	private String extractTextFromPdf(File pdfFile)throws IOException{
		try (PDDocument document = PDDocument.load(pdfFile)){
			PDFTextStripper pdfStripper = new PDFTextStripper();
			return pdfStripper.getText(document);
		}
	}

}
