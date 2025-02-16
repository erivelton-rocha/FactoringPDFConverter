package com.factoring.pdf2csv.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfToTextUtil {

	public static String convertPdfToText(File pdfFile) throws IOException {

		try (PDDocument document = PDDocument.load(pdfFile)) {
			PDFTextStripper pdfStripper = new PDFTextStripper();
			return pdfStripper.getText(document);
		}

	}

	public static void saveToTextFile(File pdfFile, File outputFile) throws IOException {
		String extractedText = convertPdfToText(pdfFile);
		Files.write(outputFile.toPath(), extractedText.getBytes(), StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
	}

}
