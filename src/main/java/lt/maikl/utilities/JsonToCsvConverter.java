package lt.maikl.utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JsonToCsvConverter {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java JsonToCsvConverter <input_json_file_path> <output_csv_file_path> <property_names_file_path>");
            return;
        }

        String jsonFilePath = args[0];
        String csvFilePath = args[1];
        String propertyNamesFilePath = args[2];

        // String jsonFilePath = "input.json";
        // String csvFilePath = "output.csv";
        // String propertyNamesFilePath = "json-properties.txt";

        List<String> propertyNames = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(propertyNamesFilePath))) {
            while (scanner.hasNextLine()) {
                propertyNames.add(scanner.nextLine().trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonNode = objectMapper.readTree(new FileReader(new File(jsonFilePath)));
            JsonNode features = jsonNode.get("features");

            CSVWriter csvWriter = new CSVWriter(new FileWriter(new File(csvFilePath)));

            csvWriter.writeNext(propertyNames.toArray(new String[0]));

            // Process each feature and write to CSV
            for (JsonNode feature : features) {
                JsonNode properties = feature.get("properties");
                List<String> propertyValues = new ArrayList<>();

                // Extract property values based on the property names
                for (String propertyName : propertyNames) {
                    JsonNode propertyValue = properties.get(propertyName);
                    if (propertyValue != null) {
                        propertyValues.add(propertyValue.asText());
                    } else {
                        // If the property name is not found in the JSON, use an empty string as the
                        // value
                        propertyValues.add("");
                    }
                }

                // Write data to CSV
                csvWriter.writeNext(propertyValues.toArray(new String[0]));
            }

            csvWriter.close();

            System.out.println("CSV file generated successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
