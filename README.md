# Zusammenfassungsprojekt ShopService

Ein kleines Java-Projekt zur Verwaltung von Produkten, Bestellungen und Lagerbestand.

## Funktionen

- Bestellungen über den `ShopService` anlegen
- Prüfung, ob bestellte Produkte existieren
- `OrderRepo`-Interface mit zwei Implementierungen:
    - `OrderListRepo` (Speicherung in einer Liste)
    - `OrderMapRepo` (Speicherung in einer HashMap)
- Produkte mit Lagerbestand
- Eine Bestellung reduziert den Lagerbestand
- Bestellungen ohne ausreichenden Bestand werden abgelehnt
- Wareneingang (`receiveGoods`) und Warenausgang (`removeGoods`)
- Lagerprotokoll (`StockMovement`) für jede Bestandsänderung
- Bestellmenge einer Position ändern
- Prüfung auf ungültige Eingaben (leere IDs, negative Mengen usw.)
- Bestellungen nach dem Prinzip „alles oder nichts“: Ist ein Produkt ungültig, wird nichts gespeichert

## Grenzen

- Die Daten werden nur im Arbeitsspeicher gehalten, es gibt keine Datenbank
- `addOrder` bestellt jedes Produkt mit der Menge 1
- Preise werden beim Anlegen einer Bestellung noch nicht aus dem Produkt übernommen
- Keine Befehlszeilenschnittstelle, keine farbige Ausgabe, kein CSV-/EAN-Import

## Voraussetzungen

- Java 21 (bzw. die im `pom.xml` angegebene Version)
- Maven

## Starten und Testen

```bash
mvn test
mvn compile exec:java -Dexec.mainClass="de.heinzdanner.zusammenfassungsprojekt_shopservice.Main"
```

Alternativ `Main` direkt in IntelliJ starten.

## Tests

Die Tests verwenden JUnit 5 und AssertJ. Abgedeckt sind:

- Anlegen von Bestellungen, auch Fehlerfälle
- Validierung von `Order` und `OrderItem`
- Lagerbestand, Wareneingang und Warenausgang
- Einträge im Lagerprotokoll