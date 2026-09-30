package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        ProductRepo productRepo = new ProductRepo();
        
        productRepo.add(new Product("P-001", "Apfel", 10));
        productRepo.add(new Product("P-002", "Banane", 20));

        OrderRepo orderRepo = new OrderMapRepo();
        ShopService shopService = new ShopService(productRepo, orderRepo);

        shopService.addOrder("O-001", List.of("P-001", "P-002"));
        shopService.addOrder("O-002", List.of("P-999"));

        System.out.println("Alle Bestellungen:");
        System.out.println(orderRepo.getAll());
    }
}
