package com.fuzzyga.utils;

import com.fuzzyga.ga.GaConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * A utility class for loading GA configuration from a properties file.
 */
public final class ConfigLoader {

    private ConfigLoader() {
    }

    /**
     * Loads GA configuration from a specified resource file.
     *
     * @param resourceName The name of the properties file on the classpath (e.g., "config.properties").
     * @return A {@link GaConfig} record populated with the values from the file.
     * @throws IOException If the resource file cannot be found or read.
     */
    public static GaConfig loadConfig(String resourceName) throws IOException {
        Properties props = new Properties();
        try (InputStream is = ConfigLoader.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourceName);
            }
            props.load(is);
        }

        return new GaConfig(
            Integer.parseInt(props.getProperty("populationSize")),
            Integer.parseInt(props.getProperty("maxGenerations")),
            Integer.parseInt(props.getProperty("elitismCount")),
            Double.parseDouble(props.getProperty("crossoverRate")),
            Double.parseDouble(props.getProperty("mutationRate")),
            Double.parseDouble(props.getProperty("mutationStrength")),
            Integer.parseInt(props.getProperty("tournamentSize")),
            Double.parseDouble(props.getProperty("fitnessThreshold"))
        );
    }
}
