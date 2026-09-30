package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import java.time.LocalDateTime;

public record StockMovement(
        String id,
        String productId,
        String movementType,
        int quantityBefore,
        int quantityAfter,
        String reason,
        LocalDateTime timestamp
) {
    public StockMovement {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Movement ID must not be null or blank.");
        }
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("Product ID must not be null or blank.");
        }
        if (movementType == null || movementType.isBlank()) {
            throw new IllegalArgumentException("Movement type must not be null or blank.");
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Reason must not be null or blank.");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp must not be null.");
        }
    }
}