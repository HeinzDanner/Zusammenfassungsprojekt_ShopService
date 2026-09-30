package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StockMovementRepo {

    private final List<StockMovement> movements = new ArrayList<>();

    public void add(StockMovement movement) {
        if (movement == null) {
            throw new IllegalArgumentException("Movement must not be null.");
        }
        movements.add(movement);
    }

    public Optional<StockMovement> getById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }

        return movements.stream()
                .filter(movement -> movement.id().equals(id))
                .findFirst();
    }

    public List<StockMovement> getAll() {
        return List.copyOf(movements);
    }
}