package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShopService {

    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;

    public ShopService(ProductRepo productRepo, OrderRepo orderRepo) {
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
    }

    public void addOrder(String orderId, List<String> productIds) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID must not be null or blank.");
        }
        if (orderRepo.getById(orderId).isPresent()) {
            throw new IllegalArgumentException(
                    "Order with ID " + orderId + " already exists."
            );
        }
        if (productIds == null || productIds.isEmpty()) {
            System.out.println("Keine Produkte für Bestellung " + orderId + " vorhanden.");
            return;
        }

        List<OrderItem> orderedItems = new ArrayList<>();

        for (String productId : productIds) {
            if (productId == null || productId.isBlank()) {
                System.out.println("Produkt-ID ist ungültig.");
                continue;
            }

            Optional<Product> foundProduct = productRepo.getById(productId);
            if (foundProduct.isPresent()) {
                orderedItems.add(
                        new OrderItem(foundProduct.get(), 1, BigDecimal.ZERO)
                );
            } else {
                System.out.println("Produkt mit ID " + productId + " existiert nicht.");
            }
        }

        if (!orderedItems.isEmpty()) {
            Order order = new Order(orderId, orderedItems);
            orderRepo.add(order);
            System.out.println("Bestellung " + orderId + " wurde angelegt.");
        } else {
            System.out.println(
                    "Keine gültigen Produkte für Bestellung "
                            + orderId
                            + " vorhanden."
            );
        }
    }
}
