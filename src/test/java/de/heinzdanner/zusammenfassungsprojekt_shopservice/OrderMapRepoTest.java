package de.heinzdanner.zusammenfassungsprojekt_shopservice;

class OrderMapRepoTest extends OrderRepoContractTest {

    @Override
    protected OrderRepo createRepository() {
        return new OrderMapRepo();
    }
}

