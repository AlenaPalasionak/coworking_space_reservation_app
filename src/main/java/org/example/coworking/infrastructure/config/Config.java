package org.example.coworking.infrastructure.config;

import org.example.coworking.infrastructure.logger.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

public class Config {
    private static final Properties PROPERTIES = new Properties();

    public static Properties getProperties() {
        Reader reader;
        if (PROPERTIES.isEmpty()) {
            String fileName = "file.properties";
            try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(fileName)) {
                reader = new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8);
                PROPERTIES.load(reader);
            } catch (IOException e) {
                Log.info("** Config **  Exception while getting properties object" + e);
                throw new RuntimeException("Exception while getting properties object from file: " + fileName + "PROPERTIES: " + PROPERTIES);
            }
        }

        return PROPERTIES;
    }
}



