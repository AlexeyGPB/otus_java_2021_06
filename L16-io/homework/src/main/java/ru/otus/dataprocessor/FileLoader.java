package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.otus.model.Measurement;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;

public class FileLoader implements Loader {
    private final String fileName;

    public FileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() throws URISyntaxException, IOException {
        //читает файл, парсит и возвращает результат
        Gson gson = new Gson();
        var resource = getClass().getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new FileProcessException("No file!");
        }
        return gson.fromJson(Files.readString(new File(resource.toURI()).toPath()), new TypeToken<List<Measurement>>() {
        }.getType());
    }
}
