package org.example.coworking.infrastructure.config;

import org.example.coworking.infrastructure.logger.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

public class PropertyConfig {
    private static final Properties PROPERTIES = new Properties();

    public static Properties getProperties() {
        Reader reader;
        if (PROPERTIES.isEmpty()) {
            String fileName = "file.properties";
            try (InputStream inputStream = PropertyConfig.class.getClassLoader().getResourceAsStream(fileName)) {
                reader = new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8);
                PROPERTIES.load(reader);
            } catch (IOException e) {
                Log.error("Exception while getting properties object from file: " + fileName + "\n" + e.getStackTrace());
                throw new RuntimeException(e.getMessage());
            }
        }

        return PROPERTIES;
    }
}