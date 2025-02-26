Semantik- und Typ-Check - Fabian Holzwarth

## Aufgabenbeschreibung

Der zweite Teil des Compilers besteht aus einer Überprüfung des vom Parser generierten AST auf korrekte Semantik und 
der verwendeten Datentypen. Dabei wird unter anderem syntactic-sugar durch die explizite Darstellungsweise ersetzt, der 
AST mit den erkannten Datentypen angereichert und auf Constraint-Verletzungen überprüft. Ziel des Semantik- und Typ-Checks ist es,
dem Schritt der Bytecode-Generierung eine umfassend getypte Darstellung eines gültigen Programms bereitzustellen.   

Die wichtigsten Aspekte waren:
- Iteration über den AST und kapselung von Statements und Expressions in getypte Container
- Evaluierung von Klassen-Namen anhand der importierten Klassen/Wildcards und des aktuellen package Namens
- Nahtloses Einbinden der Verbindungen zu vordefinierten Klassen (z.B. `java.lang.Object`)
- Ersetzen von syntaktischem Zucker durch die explizite Darstellung (z.B. Object-parent oder implizite returns)
- Vereinigung und überprüfung von Datentypen in Expressions, Statements, Methoden-Argumenten oder Rückgabe-Typen 
- Zusicherung der Einhaltung von Methoden- und Feld-Sichtbarkeiten sowie valider Variablen-Nutzung

Abseits des Semantik-Checks war Teil meines Beitrags:
- Aufsetzen der sbt Umgebung
- Implementierung eines simplen ArgumentParsers und konfigurierbarem Logger
- Erstellen der Bedienungsanleitung (in Form einer readme Datei)