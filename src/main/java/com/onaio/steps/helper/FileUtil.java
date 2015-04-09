package com.onaio.steps.helper;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    private String[] headers;
    private List<String[]> data = new ArrayList<String[]>();
    public FileUtil withHeader(String[] headers){
        this.headers = headers;
        return this;
    }

    public FileUtil withData(String[] data){
        this.data.add(data);
        return this;
    }

    public File buildCSV(String fullFileName) throws IOException {
        File file = new File(fullFileName);
        CSVWriter csvWriter = new CSVWriter(new FileWriter(file), ',');
        csvWriter.writeNext(headers);
        for (String[] row:data){
            csvWriter.writeNext(row);
        }
        csvWriter.close();
        return file;
    }

    public List<String[]> readFile(String filePath) throws IOException {
        FileReader fileReader = new FileReader(new File(filePath));
        int dataEntryPosition = 2;
        CSVReader csvReader = new CSVReader(fileReader,CSVParser.DEFAULT_SEPARATOR,CSVParser.DEFAULT_QUOTE_CHARACTER, dataEntryPosition);
        String[] line;
        List<String []> lines = new ArrayList<String[]>();
        while ((line = csvReader.readNext())!=null){
            lines.add(line);
        }
        return lines;
    }

}
