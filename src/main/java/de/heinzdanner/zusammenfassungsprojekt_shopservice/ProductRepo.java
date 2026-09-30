package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepo {

    private final List<Product> products = new ArrayList<>();

    public void add(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product must not be null.");
        }
        if (getById(product.id()).isPresent()) {
            throw new IllegalArgumentException("Product with ID " + product.id() + " already exists.");
        }
        products.add(product);
    }

    public void update(Product updatedProduct) {
        if (updatedProduct == null) {
            throw new IllegalArgumentException("Product must not be null.");
        }

        for (int i = 0; i < products.size(); i++) {
            Product current = products.get(i);
            if (current.id().equals(updatedProduct.id())) {
                products.set(i, updatedProduct);
                return;
            }
        }

        throw new IllegalArgumentException(
                "Product with ID " + updatedProduct.id() + " does not exist."
        );
    }

    public void remove(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product must not be null.");
        }
        products.remove(product);
    }

    public Optional<Product> getById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return products.stream()
                .filter(product -> product.id().equals(id))
                .findFirst();
    }

    public List<Product> getAll() {
        return List.copyOf(products);
    }
}