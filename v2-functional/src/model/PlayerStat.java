package model;

/**
 * Stellt die Statistik eines Spielers dar.
 * Enthält Informationen zu Spieler, Team, Liga, Saison und erzielten Toren.
 *
 * @author Nikola Hadzic & Emilio Jordan
 * @version 1.0
 */
public class PlayerStat {

    // Name des Spielers
    public final String player;

    // Name des Teams
    public final String team;

    // Liga (z. B. Super League)
    public final String league;

    // Saison (z. B. 2024)
    public final int season;

    // Anzahl erzielter Tore
    public final int goals;

    /**
     * Erstellt ein neues PlayerStat-Objekt mit den angegebenen Werten.
     */
    public PlayerStat(String player, String team, String league, int season, int goals) {
        this.player = player;
        this.team = team;
        this.league = league;
        this.season = season;
        this.goals = goals;
    }

    /**
     * Erstellt ein PlayerStat-Objekt aus einer CSV-Zeile.
     * @param cols Die einzelnen Werte aus der CSV-Zeile
     * @return Ein neues PlayerStat-Objekt
     */
    public static PlayerStat fromCsv(String[] cols) {
        return new PlayerStat(
                cols[0].trim(),
                cols[1].trim(),
                cols[2].trim(),
                Integer.parseInt(cols[3].trim()),
                Integer.parseInt(cols[4].trim())
        );
    }
}
