import model.Match;
import model.PlayerStat;
import util.CsvUtil;

import java.io.IOException;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Funktionale Version (V2) des Projekts "Analyse von Fussballdaten".
 * Führt die gleichen Analysen wie die imperative Version aus,
 * verwendet jedoch Java-Streams und funktionale Sprachmittel.
 *
 * Analysen:
 * - Filter: Spiele nach Liga und Saison
 * - Map: Tordifferenz je Spiel
 * - Reduce: Gesamttore, Durchschnittstore
 * - Top-Scorer-Liste
 * - Vergleich Heimsiege/Auswärtssiege/Unentschieden
 *
 * @author Nikola Hadzic & Emilio Jordan
 * @version 2.0
 */
public class Main {

    /**
     * Startpunkt des Programms (funktionale Version).
     * Liest CSV-Dateien ein und verarbeitet die Daten mit Streams.
     *
     * @param args keine Kommandozeilenparameter nötig
     * @throws IOException falls CSV-Dateien nicht gelesen werden können
     */
    public static void main(String[] args) throws IOException {

        // Pfade zu den CSV-Dateien
        String matchesPath = "data/matches.csv";
        String playersPath = "data/players.csv";

        // CSV-Dateien einlesen und in Objekte umwandeln
        List<Match> matches = CsvUtil.readCsv(matchesPath).stream()
                .map(Match::fromCsv)
                .collect(Collectors.toList());

        List<PlayerStat> players = CsvUtil.readCsv(playersPath).stream()
                .map(PlayerStat::fromCsv)
                .collect(Collectors.toList());

        String league = "Super League";
        int season = 2024;
        String team = "FC Basel";

        System.out.println("=== Fussballdaten Analyse ===\n");

        // --- Filter: nur Spiele der Liga/Saison ---
        System.out.println("-- Filter: Spiele " + league + " " + season + " --");
        matches.stream()
                .filter(m -> m.league.equals(league) && m.season == season)
                .forEach(m -> System.out.printf(Locale.ROOT, "%s – %s vs. %s – Ergebnis: %d:%d%n",
                        m.date, m.homeTeam, m.awayTeam, m.homeGoals, m.awayGoals));
        System.out.println();

        // --- Map: Tordifferenz je Spiel ---
        System.out.println("-- Map: Tordifferenz pro Spiel (alle) --");
        matches.stream()
                .map(m -> new Object[]{m.homeTeam + " – " + m.awayTeam, m.homeGoals - m.awayGoals})
                .forEach(arr -> System.out.printf(Locale.ROOT, "%s: %d%n", arr[0], (int) arr[1]));
        System.out.println();

        // --- Reduce: Gesamttore Team ---
        System.out.println("-- Reduce: Gesamttore " + team + " in Saison " + season + " --");
        int totalGoals = matches.stream()
                .filter(m -> m.season == season)
                .mapToInt(m -> (team.equals(m.homeTeam) ? m.homeGoals : 0)
                           + (team.equals(m.awayTeam) ? m.awayGoals : 0))
                .sum();
        System.out.println(totalGoals + " Tore\n");

        // --- Top-Scorer: Top 5 Spieler ---
        System.out.println("-- Top 5 Torschützen (" + league + " " + season + ") --");
        players.stream()
                .filter(p -> p.league.equals(league) && p.season == season)
                .sorted(Comparator.comparingInt((PlayerStat p) -> p.goals).reversed())
                .limit(5)
                .forEachOrdered(new java.util.function.Consumer<>() {
                    int i = 0;
                    @Override public void accept(PlayerStat p) {
                        i++;
                        System.out.printf(Locale.ROOT, "%d. %s – %d Tore (%s)%n",
                                i, p.player, p.goals, p.team);
                    }
                });
        System.out.println();

        // --- Reduce: Durchschnittstore pro Spiel ---
        System.out.println("-- Reduce: Durchschnittstore pro Spiel --");
        DoubleSummaryStatistics stats = matches.stream()
                .mapToDouble(m -> m.homeGoals + m.awayGoals)
                .summaryStatistics();
        double avgGoals = stats.getCount() == 0 ? 0.0 : stats.getAverage();
        System.out.printf(Locale.ROOT, "%.1f Tore%n%n", avgGoals);

        // --- Vergleich: Heimsiege / Auswärtssiege / Unentschieden ---
        System.out.println("-- Vergleich: Heimsiege vs. Auswärtssiege vs. Unentschieden --");
        Map<String, Long> outcomeCounts = matches.stream()
                .collect(Collectors.groupingBy(m -> {
                    if (m.homeGoals > m.awayGoals) return "HOME";
                    else if (m.homeGoals < m.awayGoals) return "AWAY";
                    else return "DRAW";
                }, Collectors.counting()));

        long total = matches.size();
        double homePct = total == 0 ? 0 : 100.0 * outcomeCounts.getOrDefault("HOME", 0L) / total;
        double awayPct = total == 0 ? 0 : 100.0 * outcomeCounts.getOrDefault("AWAY", 0L) / total;
        double drawPct = total == 0 ? 0 : 100.0 * outcomeCounts.getOrDefault("DRAW", 0L) / total;

        System.out.printf(Locale.ROOT,
                "Heimsiege: %.0f%%%nAuswärtssiege: %.0f%%%nUnentschieden: %.0f%%%n",
                homePct, awayPct, drawPct);
    }
}
