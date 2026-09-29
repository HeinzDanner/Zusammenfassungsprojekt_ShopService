package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import java.util.List;

public record Order(
        String id,
        List<Product> products
) {
}
