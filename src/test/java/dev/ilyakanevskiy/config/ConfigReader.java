package dev.ilyakanevskiy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

public class ConfigReader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);
    private static final Properties properties = new Properties();

    private static final String CONFIG_FILENAME = "config.properties";
    private static final String LOCAL_CONFIG_FILENAME = "config.local.properties";

    static {
        loadProperties(CONFIG_FILENAME, true);
        loadProperties(LOCAL_CONFIG_FILENAME, false);
    }

    private static void loadProperties(String filename, boolean required) {
        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(filename)) {
            if (inputStream == null) {
                if (required) {
                    throw new RuntimeException("File '" + filename + "' is not found in resources.");
                }
                logger.warn("File '{}' is not found in resources.", filename);
                return;
            }

            properties.load(inputStream);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load file '" + filename + "'", e);
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Property '" + key + "' is not set. Add it to '" + LOCAL_CONFIG_FILENAME + "'.");
        }

        return value;
    }

    public static String getBrowser() {
        return properties.getProperty("browser", "chrome");
    }
}
