package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShopServiceTest {

    private ProductRepo productRepo;
    private OrderRepo orderRepo;
    private ShopService shopService;

    @BeforeEach
    void setUp() {
        productRepo = new ProductRepo();
        orderRepo = new OrderListRepo();
        shopService = new ShopService(productRepo, orderRepo);

        productRepo.add(new Product("P-001", "Apfel", 10));
        productRepo.add(new Product("P-002", "Banane", 20));
    }

    @Test
    void addOrder_shouldStoreOrder_whenAllProductsExist() {
        shopService.addOrder("O-001", List.of("P-001", "P-002"));

        Order order = orderRepo.getById("O-001").orElseThrow();

        assertThat(order.id()).isEqualTo("O-001");
        assertThat(order.items())
                .extracting(OrderItem::product)
                .containsExactly(
                        new Product("P-001", "Apfel", 10),
                        new Product("P-002", "Banane", 20)
                );
        assertThat(order.items())
                .extracting(OrderItem::quantity)
                .containsOnly(1);
    }

    @Test
    void addOrder_shouldStoreOnlyExistingProducts_whenOneProductDoesNotExist() {
        shopService.addOrder("O-002", List.of("P-001", "P-999"));

        Order order = orderRepo.getById("O-002").orElseThrow();

        assertThat(order.items())
                .extracting(OrderItem::product)
                .containsExactly(new Product("P-001", "Apfel", 10));
    }

    @Test
    void addOrder_shouldNotStoreOrder_whenNoProductExists() {
        shopService.addOrder(
                "O-003",
                List.of("P-999")
        );

        assertThat(orderRepo.getById("O-003"))
                .isEmpty();
    }

    @Test
    void addOrder_shouldPrintMessage_whenProductDoesNotExist() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output));

            shopService.addOrder(
                    "O-004",
                    List.of("P-999")
            );

            assertThat(output.toString())
                    .contains("Produkt mit ID P-999 existiert nicht.");
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void addOrder_shouldRejectBlankOrderId() {
        assertThatThrownBy(() -> shopService.addOrder("   ", List.of("P-001")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Order ID");
    }

    @Test
    void addOrder_shouldNotStoreOrder_whenProductListIsEmpty() {
        shopService.addOrder("O-005", List.of());

        assertThat(orderRepo.getById("O-005"))
                .isEmpty();
    }

    @Test
    void addOrder_shouldRejectDuplicateOrderId() {
        shopService.addOrder("O-006", List.of("P-001"));

        assertThatThrownBy(() -> shopService.addOrder("O-006", List.of("P-002")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void receiveGoods_shouldIncreaseStock() {
        Product before = productRepo.getById("P-001").orElseThrow();
        assertThat(before.stock()).isEqualTo(10);

        shopService.receiveGoods("P-001", 5);

        Product after = productRepo.getById("P-001").orElseThrow();
        assertThat(after.stock()).isEqualTo(15);
    }

    @Test
    void removeGoods_shouldDecreaseStock() {
        Product before = productRepo.getById("P-001").orElseThrow();
        assertThat(before.stock()).isEqualTo(10);

        shopService.removeGoods("P-001", 4);

        Product after = productRepo.getById("P-001").orElseThrow();
        assertThat(after.stock()).isEqualTo(6);
    }

    @Test
    void removeGoods_shouldRejectWhenStockIsTooLow() {
        assertThatThrownBy(() -> shopService.removeGoods("P-001", 999))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Not enough stock");
    }
}
