package com.onaio.steps.helper;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileBuilder {

    private String[] headers;
    private List<String[]> data = new ArrayList<String[]>();
    public FileBuilder withHeader(String[] headers){
        this.headers = headers;
        return this;
    }

    public FileBuilder withData(String[] data){
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

}
