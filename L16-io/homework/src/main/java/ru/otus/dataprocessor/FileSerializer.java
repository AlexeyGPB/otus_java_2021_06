package ru.otus.dataprocessor;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {
    private final String fileName;
    private final Gson gson;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
        this.gson = new Gson();
    }

    @Override
    public void serialize(Map<String, Double> data) {
        //формирует результирующий json и сохраняет его в файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String json = gson.toJson(data);
            writer.write(json);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }


    }
}
