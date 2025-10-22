package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Hilfsklasse zum Einlesen von CSV-Dateien.
 * Jede Zeile wird als String-Array zurückgegeben.
 *
 * @author Nikola
 * @version 1.0
 */
public class CsvUtil {

    /**
     * Liest eine CSV-Datei ein und gibt alle Datenzeilen zurück.
     * Die erste Zeile (Header) wird dabei übersprungen.
     *
     * @param path Pfad zur CSV-Datei
     * @return Liste mit String-Arrays (jede Zeile als Array)
     * @throws IOException falls die Datei nicht gelesen werden kann
     */
    public static List<String[]> readCsv(String path) throws IOException {
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
            String line;
            boolean headerSkipped = false;

            while ((line = br.readLine()) != null) {
                // Header überspringen
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                // Leere Zeilen ignorieren
                if (line.isBlank()) continue;

                // Zeile in Spalten aufteilen und speichern
                String[] cols = line.split(",");
                rows.add(cols);
            }
        }

        return rows;
    }
}
