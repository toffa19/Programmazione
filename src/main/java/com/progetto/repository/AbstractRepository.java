package com.progetto.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;

public abstract class AbstractRepository<T> {
    protected ObjectMapper objectMapper;

    public AbstractRepository() {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    // Metodo astratto per ottenere il percorso del file JSON
    protected abstract String getFilePath();

    // Carica i dati dal file JSON, dato il tipo di classe
    protected T loadData(Class<T> clazz) {
        String path = getFilePath();
        System.out.println("[LOG] Loading data from: " + path);
        try {
            File file = new File(path);
            if (file.exists()) {
                T data = objectMapper.readValue(file, clazz);
                System.out.println("[LOG] Data loaded successfully.");
                return data;
            } else {
                System.out.println("[LOG] File " + path + " does not exist.");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Error while loading data from " + path);
            e.printStackTrace();
        }
        return null;
    }

    // Salva i dati nel file JSON
    protected void saveData(T data) {
        String path = getFilePath();
        System.out.println("[LOG] Saving data to: " + path);
        try {
            objectMapper.writeValue(new File(path), data);
            System.out.println("[LOG] Data saved successfully.");
        } catch (Exception e) {
            System.out.println("[ERROR] Error while saving data to " + path);
            e.printStackTrace();
        }
    }
}
