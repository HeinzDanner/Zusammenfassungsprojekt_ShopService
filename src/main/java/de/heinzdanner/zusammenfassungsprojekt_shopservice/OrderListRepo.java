package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderListRepo implements OrderRepo {

    private final List<Order> orders = new ArrayList<>();

    @Override
    public void add(Order order) {
        orders.add(order);
    }

    @Override
    public void remove(Order order) {
        orders.remove(order);
    }

    @Override
    public Optional<Order> getById(String id) {
        return orders.stream()
                .filter(order -> order.id().equals(id))
                .findFirst();
    }

    @Override
    public List<Order> getAll() {
        return List.copyOf(orders);
    }
}