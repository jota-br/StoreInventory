package dev.lpa;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Store store = new Store();
        store.manageStoreCarts();

        store.getInventory().add(new InventoryItem("Carpet", "FineStamps", "Decoration", 10, 5, 3, 55.10));
        store.getInventory().forEach(store::addItemToInventoryList);
        store.getCarts().forEach(c -> c.addItem(store.getInventory().getFirst(), 2));

        store.listProductsByCategory();
        store.getCarts().getFirst().printSalesSlip();

        store.checkOutCarts(store.getCarts().getFirst());
    }
}
