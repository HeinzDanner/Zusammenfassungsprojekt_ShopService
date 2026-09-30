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
            throw new IllegalArgumentException("Order with ID " + orderId + " already exists.");
        }
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Product IDs must not be null or empty.");
        }

        List<OrderItem> orderedItems = new ArrayList<>();
        List<Product> productsToUpdate = new ArrayList<>();

        // 1) Alles vorab prüfen + neue Bestände vorbereiten
        for (String productId : productIds) {
            if (productId == null || productId.isBlank()) {
                throw new IllegalArgumentException("Product ID must not be null or blank.");
            }

            Product product = productRepo.getById(productId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Product with ID " + productId + " does not exist."
                    ));

            if (product.stock() < 1) {
                throw new IllegalArgumentException(
                        "Not enough stock for product ID " + productId + "."
                );
            }

            orderedItems.add(new OrderItem(product, 1, BigDecimal.ZERO));
            productsToUpdate.add(product.withStock(product.stock() - 1));
        }

        // 2) Erst nach erfolgreicher Komplettprüfung schreiben
        for (Product updatedProduct : productsToUpdate) {
            productRepo.update(updatedProduct);
        }

        Order order = new Order(orderId, orderedItems);
        orderRepo.add(order);
        System.out.println("Bestellung " + orderId + " wurde angelegt.");
    }

    public void receiveGoods(String productId, int quantity) {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("Product ID must not be null or blank.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        Product product = productRepo.getById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product with ID " + productId + " does not exist."
                ));

        Product updatedProduct = product.withStock(product.stock() + quantity);
        productRepo.update(updatedProduct);
    }

    public void removeGoods(String productId, int quantity) {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("Product ID must not be null or blank.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        Product product = productRepo.getById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product with ID " + productId + " does not exist."
                ));

        if (product.stock() < quantity) {
            throw new IllegalArgumentException(
                    "Not enough stock for product ID " + productId + "."
            );
        }

        Product updatedProduct = product.withStock(product.stock() - quantity);
        productRepo.update(updatedProduct);
    }
}
