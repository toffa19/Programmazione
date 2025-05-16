// src/main/java/com/progetto/repository/AbstractRepository.java
package com.progetto.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;

/**
 * Base per tutti i repository JSON.
 */
public abstract class AbstractRepository<T> {
    protected final ObjectMapper objectMapper;

    public AbstractRepository() {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /** Il path (assoluto) del file JSON su disco */
    protected abstract String getFilePath();

    /** Carica i dati dal JSON in un oggetto di tipo T */
    protected T loadData(Class<T> clazz) {
        String path = getFilePath();
        System.out.println("[LOG] Loading data from: " + path);
        try {
            File file = new File(path);
            if (file.exists()) {
                return objectMapper.readValue(file, clazz);
            } else {
                System.out.println("[LOG] File " + path + " does not exist.");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Error loading data from " + path);
            e.printStackTrace();
        }
        return null;
    }

    /** Salva l’oggetto T sul JSON */
    protected void saveData(T data) {
        String path = getFilePath();
        System.out.println("[LOG] Saving data to: " + path);
        try {
            objectMapper.writeValue(new File(path), data);
        } catch (Exception e) {
            System.out.println("[ERROR] Error saving data to " + path);
            e.printStackTrace();
        }
    }
}
