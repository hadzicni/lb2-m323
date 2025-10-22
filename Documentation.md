### Dokumentation – Modul 323: Fussballdaten analysieren

### Projektübersicht
- Titel: Analyse von Fussballdaten – Spieler- und Teamstatistiken
- Ziel: Aus CSV-Rohdaten strukturierte Statistiken für Ligen und Saisons ableiten (Spiele, Teams, Tore).
- Varianten:
  - V1 Imperativ: klassische Schleifen, mutable Maps/Listen.
  - V2 Funktional: Java-Streams, Lambdas und Collector-APIs.
- Output: Einheitlich formatiert, konsolentauglich, ASCII-sicher.

### Technologien
- Programmiersprache: Java 17
- Build/Run: `javac`, `java`, Skripte (`run.ps1`, `run.sh`, `run.bat`)
- Funktionale Elemente in V2:
  - Lambda Expressions: `x -> x + 1`
  - Streams: `filter`, `map`, `flatMap`, `collect`, `reduce`
  - Method References: `Integer::sum`
  - Immutability: bevorzugt `final` für lokale Variablen

Beispiel:

```java
List<Integer> xs = List.of(1, 2, 3, 4);
int sumSquares = xs.stream()
    .map(x -> x * x)
    .reduce(0, Integer::sum);
```

### Datenbasis
- Dateien je Version: `v*/data/matches.csv`, `v*/data/players.csv`
- `matches.csv` (genutzte Felder): `League`, `Season` (z. B. `2012/2013`, Startjahr 2012), `Date`, `Home`, `Away`, `HG` (Home Goals), `AG` (Away Goals)
  - Nicht benötigte Quoten-/Zusatzfelder werden ignoriert.
- `players.csv` (Schema):

```text
player,team,league,season,goals
```

Hinweise:
- Datensätze bis 2025 (Super League erweitert), zusätzlich ausgewählte Einträge 2024.
- Automatische Ermittlung der neuesten Saison im Datensatz und einer Standardliga (präferiert „Super League“, sonst meistvertretene Liga).

### Fachliche Auswertungen
- Filter: Spiele nach Liga und Saison
- Map: Tordifferenz je Spiel (`HG - AG`)
- Reduce/Aggregationen:
  - Gesamttore pro Team (alle Teams, sortiert)
  - Durchschnittstore pro Spiel (gesamt)
  - Top-Scorer: Top 5 Spieler der gewählten Liga/Saison
  - Teamvergleich: Tabelle je Team mit `W/D/L`, `GF/GA/GD`, `Pkt` (Sortierung: Punkte, GD, GF)

### Outputformat
- Spiele: `Datum | Home vs Away | X:Y`
- Tordifferenz: `Home - Away | Diff: N`
- Gesamttore pro Team: `Team | N Tore`
- Top-Scorer: `# Name | N Tore (Team)`
- Teamvergleich (Kopfzeile): `Team | W D L | GF GA GD | Pkt`
- Hinweis: ASCII-Separators (`|`, `:`) für konsistente Darstellung in verschiedenen Konsolen.

### Code- und Projektstruktur
- `v*/src/Main.java`: Einstieg, Laden der CSVs, Analysen, Ausgabe
- `v*/src/model/Match.java`: CSV-Mapping einer Spielzeile (`matches.csv`)
- `v*/src/model/PlayerStat.java`: Spielerstatistiken aus `players.csv`
- `v*/src/util/CsvUtil.java`: CSV-Reader (Header wird übersprungen)

### Imperativ vs. Funktional
- V1 (imperativ):
  - Aufbau von Listen/Maps über Schleifen
  - Zustandsänderungen (Mutation) und explizite Iteration
  - Sortierung via Comparatoren
- V2 (funktional mit Streams):
  - Deklarative Pipelines mit `stream()`, `filter`, `map`, `collect`, `reduce`
  - Geringerer mutable State, kompaktere Aggregationen (Gruppieren, Summieren, Sortieren)
  - Gleicher fachlicher Output, besser lesbarer Analysefluss

Beispielvergleich (vereinfachtes Zählen der Tore pro Team):

Imperativ:
```java
Map<String, Integer> goals = new HashMap<>();
for (Match m : matches) {
  goals.put(m.home(), goals.getOrDefault(m.home(), 0) + m.hg());
  goals.put(m.away(), goals.getOrDefault(m.away(), 0) + m.ag());
}
```

Funktional:
```java
Map<String, Integer> goals = Stream.concat(
    matches.stream().map(m -> Map.entry(m.home(), m.hg())),
    matches.stream().map(m -> Map.entry(m.away(), m.ag()))
  )
  .collect(Collectors.toMap(
      Map.Entry::getKey,
      Map.Entry::getValue,
      Integer::sum
  ));
```

### Wahl der imperativen Programmiersprache
- Entscheidung für Java:
  - Weit verbreitet, stabile Toolchain, gute Bibliotheken für I/O und Collections
  - Statische Typisierung und IDE-Unterstützung erhöhen Sicherheit und Wartbarkeit
  - Einfache Ausführung auf unterschiedlichen Plattformen (JRE/JDK vorhanden)
- Relevante funktionale Sprachelemente in Java 17 (für V2):
  - Lambdas und Method References zur kompakten Funktionsübergabe
  - Streams mit `map`, `filter`, `flatMap`, `collect`, `groupingBy`, `mapping`, `summingInt`
  - `Collectors` für Gruppieren/Aggregieren, konfliktfreie Zusammenführung über Merge-Funktionen

Beispiel (Gruppieren der Gesamttore pro Team):
```java
Map<String, Integer> goalsByTeam = matches.stream()
    .flatMap(m -> Stream.of(
        new AbstractMap.SimpleEntry<>(m.home(), m.hg()),
        new AbstractMap.SimpleEntry<>(m.away(), m.ag())
    ))
    .collect(Collectors.groupingBy(Map.Entry::getKey,
        Collectors.summingInt(Map.Entry::getValue)));
```

### Ausführen
- Imperativ: `v1-imperative/run.ps1` (Windows), `v1-imperative/run.sh` (Unix), `v1-imperative/run.bat`
- Funktional: `v2-functional/run.ps1` (Windows), `v2-functional/run.sh` (Unix), `v2-functional/run.bat`
- Voraussetzung: Java 17 im `PATH`

### Beispielauszug (gekürzt)
```text
Spiele: 25/07/2025 | Zürich vs Sion | 2:1
Tordifferenz: Basel - Luzern | Diff: 0
Gesamttore pro Team: Basel | 19 Tore
Top-Scorer: #1 Aiyegun Tosin | 20 Tore (Zürich)
Team | W D L | GF GA GD | Pkt
```

### Fazit
- Nutzen für dieses Projekt: Kürzere, gut lesbare Datenpipelines, weniger Fehler durch Reduktion von mutablem Zustand und klarere Aggregationslogik.
- Unterschied zur imperativen Lösung: Funktionale Pipelines beschreiben „was“ statt „wie“; weniger Boilerplate, stabilere Sortier-/Gruppierlogik bei identischem Ergebnis.
- Erneuter Einsatz funktionaler Elemente: Geeignet für weitere CSV-/Log-Verarbeitung, ETL-Schritte, Reporting-Pipelines und Auswertungen mit gruppierenden Kennzahlen.
- Anwendungsfälle im Betrieb: Regelmäßige Berichte (Team-/Spielerstatistiken), Monitoring von Spieltagsdaten, Datenbereinigung/Enrichment vor dem Import in BI-Systeme.

Siehe auch `README.md` für den Kurzstart.

