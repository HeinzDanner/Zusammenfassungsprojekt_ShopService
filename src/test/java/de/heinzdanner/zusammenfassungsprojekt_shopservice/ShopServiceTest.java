package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShopServiceTest {
    private StockMovementRepo stockMovementRepo;
    private ProductRepo productRepo;
    private OrderRepo orderRepo;
    private ShopService shopService;

    @BeforeEach
    void setUp() {
        productRepo = new ProductRepo();
        orderRepo = new OrderListRepo();
        stockMovementRepo = new StockMovementRepo();
        shopService = new ShopService(productRepo, orderRepo, stockMovementRepo);

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
    void addOrder_shouldThrow_whenProductDoesNotExist() {
        assertThatThrownBy(() -> shopService.addOrder("O-004", List.of("P-999")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("does not exist");

        assertThat(orderRepo.getById("O-004")).isEmpty();
    }

    @Test
    void addOrder_shouldRejectBlankOrderId() {
        assertThatThrownBy(() -> shopService.addOrder("   ", List.of("P-001")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Order ID");
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

    @Test
    void addOrder_shouldRejectUnknownProduct_andNotStoreOrder() {
        Product beforeApple = productRepo.getById("P-001").orElseThrow();

        assertThatThrownBy(() -> shopService.addOrder("O-002", List.of("P-001", "P-999")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("does not exist");

        assertThat(orderRepo.getById("O-002")).isEmpty();

        Product afterApple = productRepo.getById("P-001").orElseThrow();
        assertThat(afterApple.stock()).isEqualTo(beforeApple.stock());
    }


    @Test
    void addOrder_shouldDecreaseStock_whenOrderIsStored() {
        Product beforeApple = productRepo.getById("P-001").orElseThrow();
        Product beforeBanana = productRepo.getById("P-002").orElseThrow();

        shopService.addOrder("O-100", List.of("P-001", "P-002"));

        Product afterApple = productRepo.getById("P-001").orElseThrow();
        Product afterBanana = productRepo.getById("P-002").orElseThrow();

        assertThat(afterApple.stock()).isEqualTo(beforeApple.stock() - 1);
        assertThat(afterBanana.stock()).isEqualTo(beforeBanana.stock() - 1);
        assertThat(orderRepo.getById("O-100")).isPresent();
    }

    @Test
    void addOrder_shouldRejectWhenStockIsTooLow_andNotStoreOrder() {
        // Bestand auf 0 setzen
        Product apple = productRepo.getById("P-001").orElseThrow();
        productRepo.update(apple.withStock(0));

        assertThatThrownBy(() -> shopService.addOrder("O-101", List.of("P-001")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Not enough stock");

        assertThat(orderRepo.getById("O-101")).isEmpty();

        Product unchanged = productRepo.getById("P-001").orElseThrow();
        assertThat(unchanged.stock()).isEqualTo(0);
    }

    @Test
    void addOrder_shouldRejectUnknownProduct_andNotChangeAnyStock() {
        Product beforeApple = productRepo.getById("P-001").orElseThrow();

        assertThatThrownBy(() -> shopService.addOrder("O-102", List.of("P-001", "P-999")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("does not exist");

        assertThat(orderRepo.getById("O-102")).isEmpty();

        Product afterApple = productRepo.getById("P-001").orElseThrow();
        assertThat(afterApple.stock()).isEqualTo(beforeApple.stock());
    }

    @Test
    void addOrder_shouldCreateStockMovement() {
        shopService.addOrder("O-200", List.of("P-001"));

        List<StockMovement> movements = stockMovementRepo.getAll();

        assertThat(movements)
                .isNotEmpty();

        assertThat(movements.get(0).movementType()).isEqualTo("BESTELLUNG");
        assertThat(movements.get(0).productId()).isEqualTo("P-001");
    }

    @Test
    void receiveGoods_shouldCreateStockMovement() {
        shopService.receiveGoods("P-001", 5);

        assertThat(stockMovementRepo.getAll())
                .anySatisfy(movement -> {
                    assertThat(movement.productId()).isEqualTo("P-001");
                    assertThat(movement.movementType()).isEqualTo("WARENEINGANG");
                    assertThat(movement.quantityBefore()).isEqualTo(10);
                    assertThat(movement.quantityAfter()).isEqualTo(15);
                    assertThat(movement.reason()).isEqualTo("Wareneingang");
                });
    }

    @Test
    void removeGoods_shouldCreateStockMovement() {
        shopService.removeGoods("P-001", 4);

        assertThat(stockMovementRepo.getAll())
                .anySatisfy(movement -> {
                    assertThat(movement.productId()).isEqualTo("P-001");
                    assertThat(movement.movementType()).isEqualTo("WARENAUSGANG");
                    assertThat(movement.quantityBefore()).isEqualTo(10);
                    assertThat(movement.quantityAfter()).isEqualTo(6);
                    assertThat(movement.reason()).isEqualTo("Warenausgang");
                });
    }

}
