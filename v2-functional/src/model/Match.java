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
        return new Match(
                cols[0].trim(),
                Integer.parseInt(cols[1].trim()),
                cols[2].trim(),
                cols[3].trim(),
                cols[4].trim(),
                Integer.parseInt(cols[5].trim()),
                Integer.parseInt(cols[6].trim()),
                Integer.parseInt(cols[7].trim()),
                Integer.parseInt(cols[8].trim()),
                Integer.parseInt(cols[9].trim()),
                Integer.parseInt(cols[10].trim())
        );
    }
}
