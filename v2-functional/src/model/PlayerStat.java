package model;

public class PlayerStat {
    public final String player;
    public final String team;
    public final String league;
    public final int season;
    public final int goals;

    public PlayerStat(String player, String team, String league, int season, int goals) {
        this.player = player;
        this.team = team;
        this.league = league;
        this.season = season;
        this.goals = goals;
    }

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

