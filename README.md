# Modul 323 Projekt — Fussballdaten (Imperativ vs. Funktional)

Dieses Repo enthält ein Java-Konsolenprojekt zur Auswertung von Fussballdaten. 
Es gibt zwei Versionen:

- v1-imperative: klassische imperative Implementierung (Schleifen, Mutable State)
- v2-functional: funktionale Implementierung mit Java Streams und Lambdas

Beide Versionen verwenden die gleichen Beispieldaten (CSV) und generieren den gleichen Output.

## Ausführen

Voraussetzung: JDK 17+ im `PATH` (`javac`, `java`).

### Windows (PowerShell)

- Imperativ: `./v1-imperative/run.ps1`
- Funktional: `./v2-functional/run.ps1`

### Windows (CMD)

- Imperativ: `v1-imperative\\run.bat`
- Funktional: `v2-functional\\run.bat`

### macOS/Linux (bash)

- Imperativ: `bash v1-imperative/run.sh`
- Funktional: `bash v2-functional/run.sh`

## Datenquellen (Beispiele/Ideen)

- https://opendata.swiss/de
- https://www.pxweb.bfs.admin.ch/pxweb/de/
- https://data.bs.ch/pages/home/
- https://www.football-data.co.uk/
- Kaggle-Datasets (Fussball)

In diesem Projekt liegen kleine Beispieldatensätze in CSV-Form.

## Aufgaben/Operationen

- Filter: z. B. nur Spiele einer Liga/Saison
- Map: z. B. Tordifferenz je Spiel
- Reduce: z. B. Gesamttore einer Mannschaft, Durchschnittstore pro Spiel
- Vergleich: Heimsiege vs. Auswärtssiege vs. Unentschieden
- Top-Scorer: Top-Spieler einer Liga/Saison aus `players.csv`

## Dokumentation

Siehe `docs/DOCUMENTATION.md` (kann zu PDF exportiert werden). Enthält:

- Wahl der Sprache (Java) und funktionale Elemente (Streams, Lambdas, Collector)
- Projektantrag (gemäss Vorgabe)
- Beispiel-Outputs (identisch für V1 und V2)
- Fazit zum Nutzen funktionaler Elemente

