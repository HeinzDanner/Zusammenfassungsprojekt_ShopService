package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @Test
    void totalPrice_shouldSumAllOrderItems() {
        Product apple = new Product("P-001", "Apfel");
        Product banana = new Product("P-002", "Banane");

        Order order = new Order(
                "O-001",
                List.of(
                        new OrderItem(apple, 2, new BigDecimal("1.50")),
                        new OrderItem(banana, 3, new BigDecimal("2.00"))
                )
        );

        assertThat(order.totalPrice()).isEqualByComparingTo("9.00");
    }
}