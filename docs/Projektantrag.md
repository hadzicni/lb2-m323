# Modul 323 - Funktional programmieren

Michael Salm  
M323 Projektantrag © BBZ BL  
Seite 1 von 2

## Projekttitel
Analyse von Fussballdaten – Spieler- und Mannschaftsstatistiken

## Ausgangssituation / Problembeschreibung
Fussballdaten sind weltweit verfügbar und enthalten viele Dimensionen (Spiele, Spieler, Tore, Karten, Vereine, Länder). Oft ist es schwierig, einen Überblick über Leistungen von Spielern oder Mannschaften zu gewinnen. Mit unserem Programm wollen wir Fussballdaten auswerten und strukturierte Statistiken ableiten.

## Datengrundlage
Wir nutzen offene Fussballdatensätze von Plattformen wie:
- opendata.swiss (Sport & Gesellschaft, teilweise Fussballdaten)
- Kaggle Fussball-Datensätze (z. B. FIFA World Cup, UEFA Champions League, Premier League)
- football-data.co.uk (Spielstatistiken aus verschiedenen Ligen)

### Beispiel-Dimensionen
- 1. Dimension: Spiele (Datum, Liga, Teams)
- 2. Dimension: Spielstatistiken (Tore, Karten, Ballbesitz, Torschüsse etc.)

Beispiele:

Spiele

| Datum      | Liga             | Heimteam    | Auswärtsteam    |
|------------|------------------|-------------|-----------------|
| 15.08.2024 | Super League     | FC Basel    | FC Zürich       |
| 20.08.2024 | Champions League | Real Madrid | Bayern München  |

Spielstatistiken

| Spiel                 | Tore Heim | Tore Auswärts | Gelbe Karten | Ballbesitz Heim |
|-----------------------|-----------|---------------|--------------|-----------------|
| Basel – Zürich        | 2         | 1             | 3            | 55%             |
| Real Madrid – Bayern  | 3         | 3             | 4            | 48%             |

## Produktfunktionen
- Filter: Eingabedaten nach Kriterien auswählen, z. B. nur Spiele einer bestimmten Liga oder Saison.
- Map: Daten transformieren, z. B. Tordifferenz je Spiel (Tore Heim – Tore Auswärts).
- Reduce: Werte aggregieren, z. B. Gesamttore einer Mannschaft in der Saison.
- Filter: Spieler mit mehr als X Toren finden (erst Spieler filtern, dann Tore extrahieren).
- Reduce: Durchschnittliche Anzahl Tore pro Spiel berechnen.
- Vergleich: Heim- vs. Auswärtsspiele analysieren (z. B. Punkte-/Siegverteilung).

## Technologien
- Programmiersprache: Java
- UI: Konsolenoutput (formatierte Listen)
- Funktionale Elemente: Java Streams API, Lambda Expressions

Seite 2 von 2

## Output (Beispiele)
- Top 5 Torschützen der Liga
- Durchschnittstore pro Spiel
- Mannschaft mit den meisten Punkten/Toren in einer Saison
- Filter (nur Spiele aus einer Liga/Saison)
- Map (Tordifferenz je Spiel)
- Reduce (Gesamttore eines Teams)
- Vergleich (Heim- vs. Auswärtssiege vs. Unentschieden)

Top 5 Torschützen der Liga (Beispiel):
1. Lionel Messi – 25 Tore
2. Cristiano Ronaldo – 22 Tore
3. Kylian Mbappé – 20 Tore
4. Erling Haaland – 18 Tore
5. Harry Kane – 17 Tore

Durchschnittstore pro Spiel: 3.1

Mannschaft mit den meisten Punkten: Bayern München (72 Punkte)

## Projektgruppe
Emilio Jordan und Nikola Hadzic

## Unterschrift / Abnahme
Lehrperson: Michael Salm

Teammitglieder: Emilio Jordan & Nikola Hadzic

