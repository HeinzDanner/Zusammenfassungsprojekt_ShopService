package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public record Order(
        String id,
        List<OrderItem> items
) {
    public Order {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "Order ID must not be null or blank."
            );
        }

        if (items == null) {
            throw new IllegalArgumentException(
                    "Order items must not be null."
            );
        }

        for (OrderItem item : items) {
            if (item == null) {
                throw new IllegalArgumentException(
                        "Order items must not contain null."
                );
            }
        }

        // Verhindert, dass die ursprüngliche Liste später von außen verändert wird.
        items = List.copyOf(items);
    }

    public BigDecimal totalPrice() {
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem item : items) {
            total = total.add(item.totalPrice());
        }

        return total;
    }

    public Order setQuantityForProduct(String productId, int newQuantity) {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException(
                    "Product ID must not be null or blank."
            );
        }

        if (newQuantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero."
            );
        }

        List<OrderItem> updatedItems = new ArrayList<>();
        boolean productFound = false;

        for (OrderItem item : items) {
            if (item.product().id().equals(productId)) {
                updatedItems.add(item.withQuantity(newQuantity));
                productFound = true;
            } else {
                updatedItems.add(item);
            }
        }

        if (!productFound) {
            throw new IllegalArgumentException(
                    "Product with ID " + productId + " is not part of the order."
            );
        }

        return new Order(id, updatedItems);
    }
    
}