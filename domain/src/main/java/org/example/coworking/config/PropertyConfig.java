package org.example.coworking.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

/**
 * Utility class for loading application properties from a configuration file.
 */
public class PropertyConfig {

    private static final Properties PROPERTIES = new Properties();

    /**
     * Loads properties from the "config.properties" file if not already loaded.
     *
     * @return The loaded properties.
     * @throws RuntimeException if an error occurs while reading the properties file.
     */
    public static Properties getProperties() {
        Reader reader;
        if (PROPERTIES.isEmpty()) {
            String fileName = "config.properties";
            try (InputStream inputStream = PropertyConfig.class.getClassLoader().getResourceAsStream(fileName)) {
                reader = new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8);
                PROPERTIES.load(reader);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        return PROPERTIES;
    }
}

