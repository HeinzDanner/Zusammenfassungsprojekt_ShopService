package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ShopService {

    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;

    public ShopService(ProductRepo productRepo, OrderRepo orderRepo) {
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
    }

    public void addOrder(String orderId, List<String> productIds) {
        List<OrderItem> orderedItems = new ArrayList<>();

        for (String productId : productIds) {
            productRepo.getById(productId)
                    .ifPresentOrElse(
                            product -> orderedItems.add(
                                    new OrderItem(
                                            product,
                                            1,
                                            BigDecimal.ZERO
                                    )
                            ),
                            () -> System.out.println(
                                    "Produkt mit ID " + productId + " existiert nicht."
                            )
                    );
        }

        if (!orderedItems.isEmpty()) {
            Order order = new Order(orderId, orderedItems);
            orderRepo.add(order);
            System.out.println("Bestellung " + orderId + " wurde angelegt.");
        } else {
            System.out.println(
                    "Keine gültigen Produkte für Bestellung "
                            + orderId
                            + " vorhanden."
            );
        }
    }
}
