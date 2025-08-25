package com.fuzzyga.core.data;

import com.fuzzyga.fuzzy.InputVariable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a dataset for training or testing a fuzzy system.
 *
 * @param dataPoints An unmodifiable list of {@link DataPoint}s.
 */
public record Dataset(List<DataPoint> dataPoints) implements Serializable {

    public Dataset(List<DataPoint> dataPoints) {
        this.dataPoints = List.copyOf(dataPoints);
    }

    public int size() {
        return dataPoints.size();
    }

    /**
     * Loads a dataset from a text file.
     * The file is expected to have a header row with column names (e.g., "x y z").
     * The last column is assumed to be the output value.
     *
     * @param path The path to the resource file.
     * @return A new {@link Dataset} instance.
     * @throws IOException If there is an error reading the file.
     */
    public static Dataset fromResource(String path) throws IOException {
        List<DataPoint> points = new ArrayList<>();
        try (InputStream is = Dataset.class.getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                throw new IOException("Resource not found: " + path);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            String header = reader.readLine();
            if (header == null) {
                throw new IOException("Dataset file is empty or has no header.");
            }
            String[] columnNames = header.trim().split("\\s+");
            List<InputVariable> inputVariables = new ArrayList<>();
            for (int i = 0; i < columnNames.length - 1; i++) {
                inputVariables.add(new InputVariable(columnNames[i]));
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] values = line.trim().split("\\s+");
                Map<InputVariable, Double> inputs = new HashMap<>();
                for (int i = 0; i < inputVariables.size(); i++) {
                    inputs.put(inputVariables.get(i), Double.parseDouble(values[i]));
                }
                double output = Double.parseDouble(values[values.length - 1]);
                points.add(new DataPoint(inputs, output));
            }
        }
        return new Dataset(points);
    }
}
