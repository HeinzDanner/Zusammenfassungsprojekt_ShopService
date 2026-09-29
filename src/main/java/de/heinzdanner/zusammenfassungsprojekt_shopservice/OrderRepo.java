package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import java.util.List;
import java.util.Optional;

public interface OrderRepo {

    void add(Order order);

    void remove(Order order);

    Optional<Order> getById(String id);

    List<Order> getAll();
}
