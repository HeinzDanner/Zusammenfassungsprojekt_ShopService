package de.heinzdanner.zusammenfassungsprojekt_shopservice;

public record Product(
        String id,
        String name,
        int stock
) {
    public Product {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Product ID must not be null or blank.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name must not be null or blank.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Stock must not be negative.");
        }
    }

    public Product withStock(int newStock) {
        return new Product(id, name, newStock);
    }
}

