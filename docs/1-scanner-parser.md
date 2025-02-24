# Scanner und Parser - Jakub Schwenkbeck

## Aufgabenbeschreibung

Meine Hauptaufgabe bestand darin, die Grammatik für das Java-Subset mithilfe von ANTLR v4 korrekt zu definieren und zu implementieren. ANTLR übernahm das Tokenisieren (Lexing) und das Erstellen des Parse-Trees. Diese Implementierung ist in den Dateien `Parser.scala` und `Java.g4` zu finden.

Ein wesentlicher Bestandteil meiner Arbeit war die Verarbeitung des Parse-Trees mithilfe des Visitor-Systems. In `ASTBuilder.scala` traversierte ich den Parse-Tree und wandelte ihn in eine abstrakte Syntaxdarstellung (AST) um. Dies stellte den umfangreichsten Teil meiner Arbeit dar. Der AST, implementiert in `AST.scala`, war die Schnittstelle für den Semantic-Check und die Bytecode-Generierung. Daher war eine enge Abstimmung im Team wichtig, um sicherzustellen, dass alle Änderungen konsistent in allen Komponenten reflektiert wurden.

Zusätzlich zu meiner Hauptaufgabe beteiligte ich mich bei:
- Implementierung der Input-Output-Utilities,
- Erstellung des UML-Diagramms,
- Erstellen des `Compiler-Tests`.

## Zusammenarbeit im Team

Die enge Zusammenarbeit mit dem Team war besonders bei der Verfeinerung des Parsers hilfreich. Während der Implementierung und beim Testen konnten meine Teammitglieder Testfälle identifizieren, die mir teilweise entgangen waren. Dies half uns, den Parser robuster und zuverlässiger zu gestalten.
___

# Scanner and Parser - Jakub Schwenkbeck

## Task Description

My primary task was to define and implement the grammar for a Java subset using ANTLR v4. ANTLR handled the tokenization (lexing) and the generation of the parse tree. This implementation can be found in the files `Parser.scala` and `java.g4`.

A major part of my work was processing the parse tree using the visitor system. In `ASTBuilder.scala`, I traversed the parse tree and converted it into an abstract syntax representation (AST). This was the most extensive part of my work. The AST, implemented in `AST.scala`, served as the interface for semantic analysis and bytecode generation. Therefore, close coordination within the team was essential to ensure that all modifications were consistently reflected across all components.

In addition to my main task, I was responsible for the following:
- Implementation of input-output utilities,
- Creation of the UML diagram,
- Development of compiler tests.

## Team Collaboration

Close collaboration with the team was especially valuable in refining the parser. During development and testing, my teammates identified test cases that I might have overlooked. This helped us make the parser more robust and reliable.
