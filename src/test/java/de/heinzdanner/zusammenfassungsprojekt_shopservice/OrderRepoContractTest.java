package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

abstract class OrderRepoContractTest {

    protected abstract OrderRepo createRepository();

    @Test
    void add_shouldAddOrder() {
        OrderRepo orderRepo = createRepository();
        Order order = order("O-001");

        orderRepo.add(order);

        assertThat(orderRepo.getAll()).containsExactly(order);
    }

    @Test
    void getById_shouldReturnOrder_whenExists() {
        OrderRepo orderRepo = createRepository();
        Order order = order("O-001");
        orderRepo.add(order);

        assertThat(orderRepo.getById("O-001")).contains(order);
    }

    @Test
    void getById_shouldBeEmpty_whenOrderDoesNotExist() {
        assertThat(createRepository().getById("O-999")).isEmpty();
    }

    @Test
    void remove_shouldDeleteOrder() {
        OrderRepo orderRepo = createRepository();
        Order order = order("O-001");
        orderRepo.add(order);

        orderRepo.remove(order);

        assertThat(orderRepo.getAll()).isEmpty();
        assertThat(orderRepo.getById("O-001")).isEmpty();
    }

    @Test
    void add_shouldReplaceOrderWithSameId() {
        OrderRepo orderRepo = createRepository();
        Order first = order("O-001");
        Order replacement = new Order("O-001", List.of(new Product("P-002", "Banane")));

        orderRepo.add(first);
        orderRepo.add(replacement);

        assertThat(orderRepo.getById("O-001")).contains(replacement);
        assertThat(orderRepo.getAll()).containsExactly(replacement);
    }

    private Order order(String id) {
        return new Order(id, List.of(new Product("P-001", "Apfel")));
    }
}

