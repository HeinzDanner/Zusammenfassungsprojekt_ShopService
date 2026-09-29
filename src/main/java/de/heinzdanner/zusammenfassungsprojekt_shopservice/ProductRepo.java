package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepo {

    private final List<Product> products = new ArrayList<>();

    public void add(Product product) {
        products.add(product);
    }

    public void remove(Product product) {
        products.remove(product);
    }

    public Optional<Product> getById(String id) {
        return products.stream()
                .filter(product -> product.id().equals(id))
                .findFirst();
    }

    public List<Product> getAll() {
        return List.copyOf(products);
    }
}
