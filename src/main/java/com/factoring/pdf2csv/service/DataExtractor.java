package com.factoring.pdf2csv.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface DataExtractor {
    List<String[]> extractData(String text, double totalValue, double totalPaid, double iofRate, double fee);
    List<String[]> reprocessData(String text);
    void reprocessTxt(File txtFile, File outputFile) throws IOException;
    void processarDocumentos(File inputFile, File outputFile) throws IOException;
}
