import model.Match;
import model.PlayerStat;
import util.CsvUtil;

import java.io.IOException;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
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

        // Werte aus dem Datensatz ableiten: bevorzugt "Super League",
        // dann neueste Saison; Team bevorzugt "Basel" oder meist-auftretend
        String league = inferLeague(matches);
        int season = inferLatestSeason(matches, league);
        String team = inferTeam(matches, league, season);

        System.out.println("=== Fussballdaten Analyse ===\n");

        // --- Filter: nur Spiele der Liga/Saison ---
        System.out.println("-- Filter: Spiele " + league + " " + season + " --");
        List<Match> seasonMatches = matches.stream()
                .filter(m -> m.league.equals(league) && m.season == season)
                .collect(Collectors.toList());
        seasonMatches.forEach(m -> System.out.printf(Locale.ROOT, "%s | %s vs %s | %d:%d%n",
                m.date, m.homeTeam, m.awayTeam, m.homeGoals, m.awayGoals));
        System.out.println();

        // --- Map: Tordifferenz je Spiel ---
        System.out.println("-- Map: Tordifferenz pro Spiel (alle) --");
        matches.stream()
                .map(m -> new Object[]{m.homeTeam + " - " + m.awayTeam, m.homeGoals - m.awayGoals})
                .forEach(arr -> System.out.printf(Locale.ROOT, "%s | Diff: %d%n", arr[0], (int) arr[1]));
        System.out.println();

        // --- Reduce: Gesamttore pro Team (alle) ---
        System.out.println("-- Reduce: Gesamttore pro Team (" + league + " " + season + ") --");
        Map<String, Integer> goalsFor = seasonMatches.stream().flatMap(m -> Stream.of(
                        new Object[]{m.homeTeam, m.homeGoals},
                        new Object[]{m.awayTeam, m.awayGoals}
                ))
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (Integer) arr[1],
                        Integer::sum
                ));
        goalsFor.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(e -> System.out.printf(Locale.ROOT, "%-14s | %3d Tore%n", e.getKey(), e.getValue()));
        System.out.println();

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
                        System.out.printf(Locale.ROOT, "%d. %s | %d Tore (%s)%n",
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

        // --- Vergleich: Team-Statistiken (alle Teams) ---
        System.out.println("-- Vergleich: Team-Statistiken (W/D/L/GF/GA/GD/Pkt) --");
        class Agg { int w,d,l,gf,ga; }
        Map<String, Agg> table = seasonMatches.stream()
                .flatMap(m -> Stream.of(
                        new Object[]{m.homeTeam, m.homeGoals, m.awayGoals},
                        new Object[]{m.awayTeam, m.awayGoals, m.homeGoals}
                ))
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> {
                            Agg a = new Agg(); a.gf = (Integer) arr[1]; a.ga = (Integer) arr[2];
                            if (a.gf > a.ga) a.w = 1; else if (a.gf == a.ga) a.d = 1; else a.l = 1; return a;
                        },
                        (a,b) -> { a.gf+=b.gf; a.ga+=b.ga; a.w+=b.w; a.d+=b.d; a.l+=b.l; return a; }
                ));
        class Row { String team; int w,d,l,gf,ga,gd,pts; Row(String t,int w,int d,int l,int gf,int ga){this.team=t;this.w=w;this.d=d;this.l=l;this.gf=gf;this.ga=ga;this.gd=gf-ga;this.pts=w*3+d;} }
        List<Row> rows = table.entrySet().stream()
                .map(e -> new Row(e.getKey(), e.getValue().w, e.getValue().d, e.getValue().l, e.getValue().gf, e.getValue().ga))
                .sorted(
                        Comparator.comparingInt((Row r) -> r.pts).reversed()
                                .thenComparing(Comparator.comparingInt((Row r) -> r.gd).reversed())
                                .thenComparing(Comparator.comparingInt((Row r) -> r.gf).reversed())
                                .thenComparing(r -> r.team)
                )
                .collect(Collectors.toList());
        System.out.printf(Locale.ROOT, "%-14s | %2s %2s %2s | %3s %3s %3s | %3s%n",
                "Team","W","D","L","GF","GA","GD","Pkt");
        rows.forEach(r -> System.out.printf(Locale.ROOT,
                "%-14s | %2d %2d %2d | %3d %3d %3d | %3d%n",
                r.team, r.w, r.d, r.l, r.gf, r.ga, r.gd, r.pts));
        System.out.println();

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

    private static String inferLeague(List<Match> matches) {
        Map<String, Long> counts = matches.stream()
                .collect(Collectors.groupingBy(m -> m.league, Collectors.counting()));
        if (counts.containsKey("Super League")) return "Super League";
        return counts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");
    }

    private static int inferLatestSeason(List<Match> matches, String league) {
        OptionalIntWrapper max = new OptionalIntWrapper();
        matches.stream()
                .filter(m -> league.equals("") || m.league.equals(league))
                .mapToInt(m -> m.season)
                .forEach(v -> { if (!max.present || v > max.value) { max.present = true; max.value = v; } });
        return max.present ? max.value : 0;
    }

    private static String inferTeam(List<Match> matches, String league, int season) {
        // Bevorzugt "Basel" falls vorhanden, sonst meist-auftretendes Team
        List<String> teams = matches.stream()
                .filter(m -> (league.isEmpty() || m.league.equals(league)) && (season == 0 || m.season == season))
                .flatMap(m -> Stream.of(m.homeTeam, m.awayTeam))
                .collect(Collectors.toList());
        Map<String, Long> counts = teams.stream()
                .collect(Collectors.groupingBy(t -> t, Collectors.counting()));
        if (counts.containsKey("Basel")) return "Basel";
        return counts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(teams.isEmpty() ? "" : teams.get(0));
    }

    // Kleiner Hilfstyp, um ohne OptionalInt auszukommen (keine zusätzlichen Importe nötig)
    private static class OptionalIntWrapper { boolean present = false; int value = 0; }
}
