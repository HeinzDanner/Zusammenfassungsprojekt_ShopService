package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;


public class ShopService {

    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final StockMovementRepo stockMovementRepo;

    public ShopService(
            ProductRepo productRepo,
            OrderRepo orderRepo,
            StockMovementRepo stockMovementRepo
    ) {
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.stockMovementRepo = stockMovementRepo;
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
        List<StockMovement> movementsToAdd = new ArrayList<>();
        

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

            int newStock = product.stock() - 1;
            Product updatedProduct = product.withStock(newStock);
            productsToUpdate.add(updatedProduct);

            String movementId = "MOV-" + productId + "-" + orderId;
            StockMovement movement = new StockMovement(
                    movementId,
                    productId,
                    "BESTELLUNG",
                    product.stock(),
                    newStock,
                    "Bestellung " + orderId,
                    LocalDateTime.now()
            );
            movementsToAdd.add(movement);
        }

        for (Product updatedProduct : productsToUpdate) {
            productRepo.update(updatedProduct);
        }

        for (StockMovement movement : movementsToAdd) {
            stockMovementRepo.add(movement);
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

        int newStock = product.stock() + quantity;
        Product updatedProduct = product.withStock(newStock);

        productRepo.update(updatedProduct);

        String movementId = "MOV-" + productId + "-EINGANG-" + System.nanoTime();
        StockMovement movement = new StockMovement(
                movementId,
                productId,
                "WARENEINGANG",
                product.stock(),
                newStock,
                "Wareneingang",
                LocalDateTime.now()
        );
        stockMovementRepo.add(movement);
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

        int newStock = product.stock() - quantity;
        Product updatedProduct = product.withStock(newStock);
        productRepo.update(updatedProduct);

        String movementId = "MOV-" + productId + "-AUSGANG-" + System.nanoTime();
        StockMovement movement = new StockMovement(
                movementId,
                productId,
                "WARENAUSGANG",
                product.stock(),
                newStock,
                "Warenausgang",
                LocalDateTime.now()
        );
        stockMovementRepo.add(movement);
    }
}