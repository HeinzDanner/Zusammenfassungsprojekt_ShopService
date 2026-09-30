package de.heinzdanner.zusammenfassungsprojekt_shopservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRepoTest {

    private ProductRepo productRepo;

    @BeforeEach
    void setUp() {
        productRepo = new ProductRepo();
    }

    @Test
    void add_shouldAddProduct() {
        Product product = new Product("P-001", "Apfel", 10);

        productRepo.add(product);

        assertThat(productRepo.getAll()).contains(product);
    }

    @Test
    void getById_shouldReturnProduct_whenProductExists() {
        Product product = new Product("P-001", "Apfel", 10);
        productRepo.add(product);

        assertThat(productRepo.getById("P-001")).contains(product);
    }

    @Test
    void getById_shouldBeEmpty_whenProductDoesNotExist() {
        assertThat(productRepo.getById("P-999")).isEmpty();
    }

    @Test
    void remove_shouldDeleteProduct() {
        Product product = new Product("P-001", "Apfel", 10);
        productRepo.add(product);

        productRepo.remove(product);

        assertThat(productRepo.getAll()).doesNotContain(product);
    }
}
