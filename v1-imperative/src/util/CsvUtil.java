package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {
    public static List<String[]> readCsv(String path) throws IOException {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
            String line;
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) { // skip header
                    headerSkipped = true;
                    continue;
                }
                if (line.isBlank()) continue;
                // naive split: datasets avoid quoted commas
                String[] cols = line.split(",");
                rows.add(cols);
            }
        }
        return rows;
    }
}

