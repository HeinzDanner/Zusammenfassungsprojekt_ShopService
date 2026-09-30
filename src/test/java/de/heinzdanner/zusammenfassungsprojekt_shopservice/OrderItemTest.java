package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderItemTest {

    @Test
    void constructor_shouldCreateOrderItem_whenValuesAreValid() {
        Product product = new Product("P-001", "Apfel");

        OrderItem item = new OrderItem(product, 2, new BigDecimal("1.99"));

        assertThat(item.product()).isEqualTo(product);
        assertThat(item.quantity()).isEqualTo(2);
        assertThat(item.price()).isEqualByComparingTo("1.99");
    }

    @Test
    void constructor_shouldThrowException_whenProductIsNull() {
        assertThatThrownBy(() -> new OrderItem(null, 1, BigDecimal.ONE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product must not be null.");
    }

    @Test
    void constructor_shouldThrowException_whenQuantityIsZero() {
        Product product = new Product("P-001", "Apfel");

        assertThatThrownBy(() -> new OrderItem(product, 0, BigDecimal.ONE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quantity must be greater than zero.");
    }

    @Test
    void constructor_shouldThrowException_whenQuantityIsNegative() {
        Product product = new Product("P-001", "Apfel");

        assertThatThrownBy(() -> new OrderItem(product, -1, BigDecimal.ONE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quantity must be greater than zero.");
    }

    @Test
    void constructor_shouldThrowException_whenPriceIsNull() {
        Product product = new Product("P-001", "Apfel");

        assertThatThrownBy(() -> new OrderItem(product, 1, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Price must not be null.");
    }

    @Test
    void constructor_shouldThrowException_whenPriceIsNegative() {
        Product product = new Product("P-001", "Apfel");

        assertThatThrownBy(() -> new OrderItem(product, 1, new BigDecimal("-0.01")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Price must not be negative.");
    }

    @Test
    void totalPrice_shouldReturnPriceMultipliedByQuantity() {
        Product product = new Product("P-001", "Apfel");
        OrderItem item = new OrderItem(product, 3, new BigDecimal("2.50"));

        assertThat(item.totalPrice()).isEqualByComparingTo("7.50");
    }
}