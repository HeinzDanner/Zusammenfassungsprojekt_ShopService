package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrderMapRepo implements OrderRepo {

    private final Map<String, Order> orders = new HashMap<>();

    @Override
    public void add(Order order) {
        orders.put(order.id(), order);
    }

    @Override
    public void remove(Order order) {
        orders.remove(order.id(), order);
    }

    @Override
    public Optional<Order> getById(String id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public List<Order> getAll() {
        return List.copyOf(orders.values());
    }
}
