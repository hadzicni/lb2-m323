package model;

public class Match {
    public final String date;
    public final int season;
    public final String league;
    public final String homeTeam;
    public final String awayTeam;
    public final int homeGoals;
    public final int awayGoals;
    public final int homeYellow;
    public final int awayYellow;
    public final int homePossession;
    public final int awayPossession;

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

