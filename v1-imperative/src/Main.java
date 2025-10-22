import model.Match;
import model.PlayerStat;
import util.CsvUtil;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Hauptklasse des Projekts "Analyse von Fussballdaten".
 * Führt verschiedene Auswertungen mit Fussballdaten durch:
 * - Filter (nach Liga/Saison)
 * - Map (Tordifferenz je Spiel)
 * - Reduce (Gesamttore, Durchschnittstore)
 * - Top-Scorer-Liste
 * - Vergleich von Heimsiegen, Auswärtssiegen und Unentschieden
 *
 * @author Nikola
 * @version 1.0
 */
public class Main {

    /**
     * Startpunkt des Programms.
     * Liest CSV-Dateien ein und führt alle Analysen aus.
     *
     * @param args keine Kommandozeilenparameter nötig
     * @throws IOException falls CSV-Dateien nicht gelesen werden können
     */
    public static void main(String[] args) throws IOException {
        // Basisverzeichnis und Datenpfade
        String baseDir = Paths.get(".").toAbsolutePath().normalize().toString();
        String matchesPath = Paths.get("data", "matches.csv").toString();
        String playersPath = Paths.get("data", "players.csv").toString();

        // Spiele laden
        List<Match> matches = new ArrayList<>();
        for (String[] row : CsvUtil.readCsv(matchesPath)) {
            matches.add(Match.fromCsv(row));
        }

        // Spielerstatistiken laden
        List<PlayerStat> players = new ArrayList<>();
        for (String[] row : CsvUtil.readCsv(playersPath)) {
            players.add(PlayerStat.fromCsv(row));
        }

        // Beispielwerte für Analyse
        String league = inferLeague(matches);
        int season = inferLatestSeason(matches, league);
        String team = inferTeam(matches, league, season);

        System.out.println("=== Fussballdaten Analyse ===\n");

        // --- Filter: nur Spiele der Liga/Saison ---
        System.out.println("-- Filter: Spiele " + league + " " + season + " (letzte 10) --");
        List<Match> filtered = new ArrayList<>();
        for (Match m : matches) {
            if (m.league.equals(league) && m.season == season) {
                filtered.add(m);
            }
        }
        int startIdx = Math.max(0, filtered.size() - 10);
        List<Match> last = filtered.subList(startIdx, filtered.size());
        for (Match m : last) {
            System.out.printf(Locale.ROOT, "%s | %s vs %s | %d:%d%n",
                    m.date, m.homeTeam, m.awayTeam, m.homeGoals, m.awayGoals);
        }
        System.out.println();

        // --- Map: Tordifferenz je Spiel ---
        System.out.println("-- Map: Tordifferenz pro Spiel Super League 2025 (letzte 10) --");
        int startIdxMap = Math.max(0, filtered.size() - 10);
        List<Match> lastMap = filtered.subList(startIdxMap, filtered.size());
        for (Match m : lastMap) {
            int diff = m.homeGoals - m.awayGoals;
            System.out.printf(Locale.ROOT, "%s - %s | Diff: %d%n", m.homeTeam, m.awayTeam, diff);
        }
        System.out.println();

        // --- Reduce: Gesamttore pro Team (alle) ---
        System.out.println("-- Reduce: Gesamttore pro Team (" + league + " " + season + ") --");
        Map<String,Integer> goalsFor = new HashMap<>();
        for (Match m : filtered) {
            goalsFor.merge(m.homeTeam, m.homeGoals, Integer::sum);
            goalsFor.merge(m.awayTeam, m.awayGoals, Integer::sum);
        }
        List<Map.Entry<String,Integer>> goalsList = new ArrayList<>(goalsFor.entrySet());
        goalsList.sort((a,b) -> Integer.compare(b.getValue(), a.getValue()));
        for (Map.Entry<String,Integer> e : goalsList) {
            System.out.printf(Locale.ROOT, "%-14s | %3d Tore%n", e.getKey(), e.getValue());
        }
        System.out.println();

        // --- Top-Scorer-Liste ---
        System.out.println("-- Top 5 Torschützen (" + league + " " + season + ") --");
        List<PlayerStat> scoped = new ArrayList<>();
        for (PlayerStat p : players) {
            if (p.league.equals(league) && p.season == season) scoped.add(p);
        }
        scoped.sort((a, b) -> Integer.compare(b.goals, a.goals));
        int limit = Math.min(5, scoped.size());
        for (int i = 0; i < limit; i++) {
            PlayerStat p = scoped.get(i);
            System.out.printf(Locale.ROOT, "%d. %s | %d Tore (%s)%n",
                    i + 1, p.player, p.goals, p.team);
        }
        System.out.println();

        // --- Reduce: Durchschnittstore pro Spiel ---
        System.out.println("-- Reduce: Durchschnittstore pro Spiel --");
        int goalsSum = 0;
        for (Match m : matches) goalsSum += (m.homeGoals + m.awayGoals);
        double avgGoals = matches.isEmpty() ? 0.0 : (goalsSum * 1.0) / matches.size();
        System.out.printf(Locale.ROOT, "%.1f Tore%n%n", avgGoals);

        // --- Vergleich: Team-Statistiken (alle Teams) ---
        System.out.println("-- Vergleich: Team-Statistiken (W/D/L/GF/GA/GD/Pkt) --");
        class Agg { int w,d,l,gf,ga; }
        Map<String, Agg> table = new HashMap<>();
        for (Match m : filtered) {
            // home
            Agg ah = table.computeIfAbsent(m.homeTeam, k -> new Agg());
            ah.gf += m.homeGoals; ah.ga += m.awayGoals;
            if (m.homeGoals > m.awayGoals) ah.w++; else if (m.homeGoals == m.awayGoals) ah.d++; else ah.l++;
            // away
            Agg aa = table.computeIfAbsent(m.awayTeam, k -> new Agg());
            aa.gf += m.awayGoals; aa.ga += m.homeGoals;
            if (m.awayGoals > m.homeGoals) aa.w++; else if (m.awayGoals == m.homeGoals) aa.d++; else aa.l++;
        }
        class Row { String team; int w,d,l,gf,ga,gd,pts; Row(String t,int w,int d,int l,int gf,int ga){team=t;this.w=w;this.d=d;this.l=l;this.gf=gf;this.ga=ga;this.gd=gf-ga;this.pts=w*3+d;} }
        List<Row> rows = new ArrayList<>();
        for (Map.Entry<String,Agg> e : table.entrySet()) {
            Agg a = e.getValue(); rows.add(new Row(e.getKey(), a.w,a.d,a.l,a.gf,a.ga));
        }
        rows.sort((r1,r2) -> {
            if (r2.pts != r1.pts) return Integer.compare(r2.pts, r1.pts);
            if (r2.gd != r1.gd) return Integer.compare(r2.gd, r1.gd);
            if (r2.gf != r1.gf) return Integer.compare(r2.gf, r1.gf);
            return r1.team.compareTo(r2.team);
        });
        System.out.printf(Locale.ROOT, "%-14s | %2s %2s %2s | %3s %3s %3s | %3s%n",
                "Team","W","D","L","GF","GA","GD","Pkt");
        for (Row r : rows) {
            System.out.printf(Locale.ROOT, "%-14s | %2d %2d %2d | %3d %3d %3d | %3d%n",
                    r.team, r.w, r.d, r.l, r.gf, r.ga, r.gd, r.pts);
        }
        System.out.println();

        // --- Vergleich: Heimsiege / Auswärtssiege / Unentschieden ---
        System.out.println("-- Vergleich: Heimsiege vs. Auswärtssiege vs. Unentschieden --");
        int homeWins = 0, awayWins = 0, draws = 0;
        for (Match m : matches) {
            if (m.homeGoals > m.awayGoals) homeWins++;
            else if (m.homeGoals < m.awayGoals) awayWins++;
            else draws++;
        }

        int total = matches.size();
        double homePct = total == 0 ? 0 : (homeWins * 100.0) / total;
        double awayPct = total == 0 ? 0 : (awayWins * 100.0) / total;
        double drawPct = total == 0 ? 0 : (draws * 100.0) / total;

        System.out.printf(Locale.ROOT,
                "Heimsiege: %.0f%%%nAuswärtssiege: %.0f%%%nUnentschieden: %.0f%%%n",
                homePct, awayPct, drawPct);
    }

    private static String inferLeague(List<Match> matches) {
        Map<String, Long> counts = new HashMap<>();
        for (Match m : matches) counts.merge(m.league, 1L, Long::sum);
        if (counts.containsKey("Super League")) return "Super League";
        return counts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");
    }

    private static int inferLatestSeason(List<Match> matches, String league) {
        int max = 0;
        boolean present = false;
        for (Match m : matches) {
            if (!league.isEmpty() && !m.league.equals(league)) continue;
            if (!present || m.season > max) { max = m.season; present = true; }
        }
        return present ? max : 0;
    }

    private static String inferTeam(List<Match> matches, String league, int season) {
        Map<String, Integer> counts = new HashMap<>();
        for (Match m : matches) {
            if ((!league.isEmpty() && !m.league.equals(league)) || (season != 0 && m.season != season)) continue;
            counts.merge(m.homeTeam, 1, Integer::sum);
            counts.merge(m.awayTeam, 1, Integer::sum);
        }
        if (counts.containsKey("Basel")) return "Basel";
        return counts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(counts.keySet().stream().findFirst().orElse(""));
    }
}
