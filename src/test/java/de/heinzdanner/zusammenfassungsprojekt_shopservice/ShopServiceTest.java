package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShopServiceTest {

    private ProductRepo productRepo;
    private OrderRepo orderRepo;
    private ShopService shopService;

    @BeforeEach
    void setUp() {
        productRepo = new ProductRepo();
        orderRepo = new OrderListRepo();
        shopService = new ShopService(productRepo, orderRepo);

        productRepo.add(new Product("P-001", "Apfel"));
        productRepo.add(new Product("P-002", "Banane"));
    }

    @Test
    void addOrder_shouldStoreOrder_whenAllProductsExist() {
        shopService.addOrder(
                "O-001",
                List.of("P-001", "P-002")
        );

        assertThat(orderRepo.getById("O-001"))
                .isPresent()
                .get()
                .extracting(Order::id)
                .isEqualTo("O-001");

        assertThat(orderRepo.getById("O-001"))
                .get()
                .extracting(Order::products)
                .asList()
                .containsExactly(
                        new Product("P-001", "Apfel"),
                        new Product("P-002", "Banane")
                );
    }

    @Test
    void addOrder_shouldStoreOnlyExistingProducts_whenOneProductDoesNotExist() {
        shopService.addOrder(
                "O-002",
                List.of("P-001", "P-999")
        );

        assertThat(orderRepo.getById("O-002"))
                .isPresent()
                .get()
                .extracting(Order::products)
                .asList()
                .containsExactly(
                        new Product("P-001", "Apfel")
                );
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
}