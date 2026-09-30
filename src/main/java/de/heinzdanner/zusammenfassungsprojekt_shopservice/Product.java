package de.heinzdanner.zusammenfassungsprojekt_shopservice;

public record Product(
        String id,
        String name
) {
    public Product {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Product ID must not be null or blank.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name must not be null or blank.");
        }
    }
}

