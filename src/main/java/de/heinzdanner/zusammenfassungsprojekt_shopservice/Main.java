package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        ProductRepo productRepo = new ProductRepo();
        OrderRepo orderRepo = new OrderListRepo(); // alternativ: new OrderMapRepo();
        StockMovementRepo stockMovementRepo = new StockMovementRepo();

        ShopService shopService = new ShopService(
                productRepo,
                orderRepo,
                stockMovementRepo
        );

        // Produkte anlegen
        productRepo.add(new Product("P-001", "Apfel", 10));
        productRepo.add(new Product("P-002", "Banane", 20));

        // Gültige Bestellung
        try {
            shopService.addOrder("O-001", List.of("P-001", "P-002"));
        } catch (IllegalArgumentException e) {
            System.out.println("Bestellung fehlgeschlagen: " + e.getMessage());
        }

        // Ungültige Bestellung (Produkt existiert nicht)
        try {
            shopService.addOrder("O-002", List.of("P-001", "P-999"));
        } catch (IllegalArgumentException e) {
            System.out.println("Bestellung fehlgeschlagen: " + e.getMessage());
        }

        // Wareneingang
        try {
            shopService.receiveGoods("P-001", 5);
            System.out.println("Wareneingang gebucht.");
        } catch (IllegalArgumentException e) {
            System.out.println("Wareneingang fehlgeschlagen: " + e.getMessage());
        }

        // Warenausgang
        try {
            shopService.removeGoods("P-002", 3);
            System.out.println("Warenausgang gebucht.");
        } catch (IllegalArgumentException e) {
            System.out.println("Warenausgang fehlgeschlagen: " + e.getMessage());
        }

        // Optional: kurze Ausgabe des Lagerprotokolls
        System.out.println("--- Lagerprotokoll ---");
        for (StockMovement movement : stockMovementRepo.getAll()) {
            System.out.println(
                    movement.timestamp() + " | " +
                            movement.movementType() + " | " +
                            movement.productId() + " | " +
                            movement.quantityBefore() + " -> " + movement.quantityAfter() + " | " +
                            movement.reason()
            );
        }
    }
}