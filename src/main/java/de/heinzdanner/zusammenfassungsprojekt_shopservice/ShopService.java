package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import java.util.ArrayList;
import java.util.List;

public class ShopService {

    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;

    public ShopService(ProductRepo productRepo, OrderRepo orderRepo) {
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
    }

    public void addOrder(String orderId, List<String> productIds) {
        List<Product> orderedProducts = new ArrayList<>();

        for (String productId : productIds) {
            productRepo.getById(productId)
                    .ifPresentOrElse(
                            orderedProducts::add,
                            () -> System.out.println("Produkt mit ID " + productId + " existiert nicht.")
                    );
        }

        if (!orderedProducts.isEmpty()) {
            Order order = new Order(orderId, orderedProducts);
            orderRepo.add(order);
            System.out.println("Bestellung " + orderId + " wurde angelegt.");
        } else {
            System.out.println("Keine gültigen Produkte für Bestellung " + orderId + " vorhanden.");
        }
    }
}
