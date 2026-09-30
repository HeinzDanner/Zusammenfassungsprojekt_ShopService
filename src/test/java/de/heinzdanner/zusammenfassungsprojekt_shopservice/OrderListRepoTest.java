package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class OrderListRepoTest extends OrderRepoContractTest {

    @Override
    protected OrderRepo createRepository() {
        return new OrderListRepo();
    }


/*


class OrderListRepoTest {

    private OrderListRepo orderRepo;

    @BeforeEach
    void setUp() {
        orderRepo = new OrderListRepo();
    }

    @Test
    void add_shouldAddOrder() {
        Product product = new Product("P-001", "Apfel", 10);
        Order order = new Order("O-001", List.of(product));

        orderRepo.add(order);

        assertThat(orderRepo.getAll()).contains(order);
    }

    @Test
    void getById_shouldReturnOrder_whenExists() {
        Product product = new Product("P-001", "Apfel", 10);
        Order order = new Order("O-001", List.of(product));
        orderRepo.add(order);

        assertThat(orderRepo.getById("O-001")).contains(order);
    }

    @Test
    void getById_shouldBeEmpty_whenOrderDoesNotExist() {
        assertThat(orderRepo.getById("O-999")).isEmpty();
    }

    @Test
    void remove_shouldDeleteOrder() {
        Product product = new Product("P-001", "Apfel", 10);
        Order order = new Order("O-001", List.of(product));
        orderRepo.add(order);

        orderRepo.remove(order);

        assertThat(orderRepo.getAll()).doesNotContain(order);
    }

 */
}
