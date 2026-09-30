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
}
