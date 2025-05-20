package com.progetto.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Config {
    public static final String USERS_JSON_PATH = "src/data/users.json";
    public static final String EXERCISES_JSON_PATH = "src/data/exercises.json";

    public static final ObjectMapper MAPPER = new ObjectMapper();

}
