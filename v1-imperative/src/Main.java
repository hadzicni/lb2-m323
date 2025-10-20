import model.Match;
import model.PlayerStat;
import util.CsvUtil;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String baseDir = Paths.get(".").toAbsolutePath().normalize().toString();
        String matchesPath = Paths.get("data", "matches.csv").toString();
        String playersPath = Paths.get("data", "players.csv").toString();

        List<Match> matches = new ArrayList<>();
        for (String[] row : CsvUtil.readCsv(matchesPath)) {
            matches.add(Match.fromCsv(row));
        }

        List<PlayerStat> players = new ArrayList<>();
        for (String[] row : CsvUtil.readCsv(playersPath)) {
            players.add(PlayerStat.fromCsv(row));
        }

        String league = "Super League";
        int season = 2024;
        String team = "FC Basel";

        System.out.println("=== Fussballdaten Analyse (Imperativ) ===\n");

        // Filter: nur Spiele der Liga/Saison
        System.out.println("-- Filter: Spiele " + league + " " + season + " --");
        List<Match> filtered = new ArrayList<>();
        for (Match m : matches) {
            if (m.league.equals(league) && m.season == season) {
                filtered.add(m);
            }
        }
        for (Match m : filtered) {
            System.out.printf(Locale.ROOT, "%s – %s vs. %s – Ergebnis: %d:%d%n",
                    m.date, m.homeTeam, m.awayTeam, m.homeGoals, m.awayGoals);
        }
        System.out.println();

        // Map: Tordifferenz je Spiel (alle Spiele)
        System.out.println("-- Map: Tordifferenz pro Spiel (alle) --");
        for (Match m : matches) {
            int diff = m.homeGoals - m.awayGoals;
            System.out.printf(Locale.ROOT, "%s – %s: %d%n", m.homeTeam, m.awayTeam, diff);
        }
        System.out.println();

        // Reduce: Gesamttore Team in Saison
        System.out.println("-- Reduce: Gesamttore " + team + " in Saison " + season + " --");
        int totalGoals = 0;
        for (Match m : matches) {
            if (m.season == season) {
                if (m.homeTeam.equals(team)) totalGoals += m.homeGoals;
                if (m.awayTeam.equals(team)) totalGoals += m.awayGoals;
            }
        }
        System.out.println(totalGoals + " Tore\n");

        // Top-Scorer: Top 5 in Liga/Saison
        System.out.println("-- Top 5 Torschützen (" + league + " " + season + ") --");
        List<PlayerStat> scoped = new ArrayList<>();
        for (PlayerStat p : players) {
            if (p.league.equals(league) && p.season == season) scoped.add(p);
        }
        scoped.sort((a,b) -> Integer.compare(b.goals, a.goals));
        int limit = Math.min(5, scoped.size());
        for (int i=0; i<limit; i++) {
            PlayerStat p = scoped.get(i);
            System.out.printf(Locale.ROOT, "%d. %s – %d Tore (%s)%n", i+1, p.player, p.goals, p.team);
        }
        System.out.println();

        // Reduce: Durchschnittstore pro Spiel
        System.out.println("-- Reduce: Durchschnittstore pro Spiel --");
        int goalsSum = 0;
        for (Match m : matches) goalsSum += (m.homeGoals + m.awayGoals);
        double avgGoals = matches.isEmpty() ? 0.0 : (goalsSum * 1.0) / matches.size();
        System.out.printf(Locale.ROOT, "%.1f Tore%n%n", avgGoals);

        // Vergleich: Heimsiege/Auswärtssiege/Unentschieden
        System.out.println("-- Vergleich: Heimsiege vs. Auswärtssiege vs. Unentschieden --");
        int homeWins=0, awayWins=0, draws=0;
        for (Match m : matches) {
            if (m.homeGoals > m.awayGoals) homeWins++;
            else if (m.homeGoals < m.awayGoals) awayWins++;
            else draws++;
        }
        int total = matches.size();
        double homePct = total==0 ? 0 : (homeWins*100.0)/total;
        double awayPct = total==0 ? 0 : (awayWins*100.0)/total;
        double drawPct = total==0 ? 0 : (draws*100.0)/total;
        System.out.printf(Locale.ROOT, "Heimsiege: %.0f%%%nAuswärtssiege: %.0f%%%nUnentschieden: %.0f%%%n",
                homePct, awayPct, drawPct);
    }
}

