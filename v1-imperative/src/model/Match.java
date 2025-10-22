package model;

/**
 * Stellt ein Fussballspiel dar mit allen wichtigen Daten
 * wie Tore, Karten und Ballbesitz.
 *
 * @author Nikola Hadzic & Emilio Jordan
 * @version 1.0
 */
public class Match {

    // Datum des Spiels
    public final String date;

    // Saison (z. B. 2024)
    public final int season;

    // Liga (z. B. Super League)
    public final String league;

    // Heimmannschaft
    public final String homeTeam;

    // Auswärtsmannschaft
    public final String awayTeam;

    // Tore der Heimmannschaft
    public final int homeGoals;

    // Tore der Auswärtsmannschaft
    public final int awayGoals;

    // Gelbe Karten der Heimmannschaft
    public final int homeYellow;

    // Gelbe Karten der Auswärtsmannschaft
    public final int awayYellow;

    // Ballbesitz der Heimmannschaft (in %)
    public final int homePossession;

    // Ballbesitz der Auswärtsmannschaft (in %)
    public final int awayPossession;

    /**
     * Erstellt ein neues Match-Objekt mit allen Spielwerten.
     */
    public Match(String date, int season, String league, String homeTeam, String awayTeam,
                 int homeGoals, int awayGoals, int homeYellow, int awayYellow,
                 int homePossession, int awayPossession) {
        this.date = date;
        this.season = season;
        this.league = league;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.homeYellow = homeYellow;
        this.awayYellow = awayYellow;
        this.homePossession = homePossession;
        this.awayPossession = awayPossession;
    }

    /**
     * Erstellt ein Match-Objekt aus einer CSV-Zeile.
     * @param cols Die einzelnen Werte aus der CSV-Zeile
     * @return Ein neues Match-Objekt
     */
    public static Match fromCsv(String[] cols) {
        // Erwartete Struktur des neuen Datensatzes "matches.csv":
        // 0: Country, 1: League, 2: Season (z.B. 2012/2013), 3: Date, 4: Time,
        // 5: Home, 6: Away, 7: HG, 8: AG, 9: Res, ... (weitere Quotenfelder)
        String date = cols[3].trim();
        int season = parseSeason(cols[2].trim());
        String league = cols[1].trim();
        String home = cols[5].trim();
        String away = cols[6].trim();
        int hg = parseIntSafe(cols[7]);
        int ag = parseIntSafe(cols[8]);

        // Der neue Datensatz enthält keine Karten/Ballbesitz -> als 0 annehmen
        int homeYellow = 0;
        int awayYellow = 0;
        int homePossession = 0;
        int awayPossession = 0;

        return new Match(
                date,
                season,
                league,
                home,
                away,
                hg,
                ag,
                homeYellow,
                awayYellow,
                homePossession,
                awayPossession
        );
    }

    private static int parseSeason(String value) {
        // Unterstützt Formate wie "2012/2013" oder "2024"
        String v = value.trim();
        int slash = v.indexOf('/');
        String year = (slash > 0) ? v.substring(0, slash) : v;
        try {
            return Integer.parseInt(year);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }
}
