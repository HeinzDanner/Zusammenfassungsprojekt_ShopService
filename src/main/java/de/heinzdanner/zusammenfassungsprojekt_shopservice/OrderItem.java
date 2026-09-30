package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import java.math.BigDecimal;

public record OrderItem(
        Product product,
        int quantity,
        BigDecimal price
) {

    public OrderItem {
        if (product == null) {
            throw new IllegalArgumentException("Product must not be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (price == null) {
            throw new IllegalArgumentException("Price must not be null.");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must not be negative.");
        }
    }

    public BigDecimal totalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public OrderItem withQuantity(int newQuantity) {
        return new OrderItem(product, newQuantity, price);
    }
}