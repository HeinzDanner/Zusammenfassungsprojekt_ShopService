package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @Test
    void totalPrice_shouldSumAllOrderItems() {
        Product apple = new Product("P-001", "Apfel", 10);
        Product banana = new Product("P-002", "Banane", 20);

        Order order = new Order(
                "O-001",
                List.of(
                        new OrderItem(apple, 2, new BigDecimal("1.50")),
                        new OrderItem(banana, 3, new BigDecimal("2.00"))
                )
        );

        assertThat(order.totalPrice()).isEqualByComparingTo("9.00");
    }

    @Test
    void setQuantityForProduct_shouldUpdateQuantityAndTotalPrice() {
        Product apple = new Product("P-001", "Apfel", 10);
        Product banana = new Product("P-002", "Banane", 20);

        Order order = new Order(
                "O-001",
                List.of(
                        new OrderItem(apple, 2, new BigDecimal("1.50")),
                        new OrderItem(banana, 3, new BigDecimal("2.00"))
                )
        );

        Order updatedOrder = order.setQuantityForProduct("P-001", 5);

        assertThat(updatedOrder.items())
                .extracting(OrderItem::quantity)
                .containsExactly(5, 3);

        assertThat(updatedOrder.totalPrice()).isEqualByComparingTo("13.50");
    }

    @Test
    void constructor_shouldRejectBlankOrderId() {
        Product apple = new Product("P-001", "Apfel", 10);
        OrderItem item = new OrderItem(
                apple,
                1,
                new BigDecimal("1.50")
        );

        assertThatThrownBy(() -> new Order("   ", List.of(item)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Order ID");
    }

    @Test
    void constructor_shouldRejectNullItems() {
        assertThatThrownBy(() -> new Order("O-001", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("items");
    }

    @Test
    void setQuantityForProduct_shouldRejectUnknownProduct() {
        Product apple = new Product("P-001", "Apfel", 10);
        OrderItem item = new OrderItem(
                apple,
                1,
                new BigDecimal("1.50")
        );

        Order order = new Order("O-001", List.of(item));

        assertThatThrownBy(() ->
                order.setQuantityForProduct("P-999", 2)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("P-999");
    }

    @Test
    void setQuantityForProduct_shouldRejectInvalidQuantity() {
        Product apple = new Product("P-001", "Apfel", 10);
        OrderItem item = new OrderItem(
                apple,
                1,
                new BigDecimal("1.50")
        );

        Order order = new Order("O-001", List.of(item));

        assertThatThrownBy(() ->
                order.setQuantityForProduct("P-001", 0)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("greater than zero");
    }
}