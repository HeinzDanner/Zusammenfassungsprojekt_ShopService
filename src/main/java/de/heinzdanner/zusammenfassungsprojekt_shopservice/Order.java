package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import java.util.List;

public record Order(
        String id,
        List<OrderItem> items
        // List<Product> products
) {
}
