package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import java.math.BigDecimal;
import java.util.List;

public record Order(
        String id,
        List<OrderItem> items
        // List<Product> products
) {
    public BigDecimal totalPrice() {
        return items.stream()
                .map(OrderItem::totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Order setQuantityForProduct(String productId, int newQuantity) {
        List<OrderItem> updatedItems = items.stream()
                .map(item -> item.product().id().equals(productId)
                        ? item.withQuantity(newQuantity)
                        : item)
                .toList();

        return new Order(id, updatedItems);
    }
}
