package dev.lpa;

import java.util.*;

public class Store {

    private NavigableSet<InventoryItem> inventory;
    private List<Cart> carts;
    private NavigableMap<String, List<InventoryItem>> aisleInventory;

    public Comparator<InventoryItem> sortByName = Comparator.comparing(o -> o.getProduct().name());
    public Comparator<InventoryItem> sortByCategory = Comparator.comparing(o -> o.getProduct().category());

    public Store() {
        this.inventory = new TreeSet<>();
        this.carts = new ArrayList<>(20);
        this.aisleInventory = new TreeMap<>();
    }

    public void manageStoreCarts() {
        carts.add(new Cart("virtual"));
    }

    public void addItemToInventoryList(InventoryItem item) {

        List<InventoryItem> list = this.aisleInventory.get(item.getProduct().category());

        if (list == null) {
            list = new ArrayList<>();
            this.getAisleInventory().put(item.getProduct().category(), list);
        }

        list.add(item);
    }

    public void checkOutCarts(Cart cart) {

        if (cart == null) {
            System.out.println("Unable to identify Cart");
            return;
        }

        cart.printSalesSlip();
        cart.getProducts().forEach(InventoryItem::sellItem);
    }

    public void abandonCarts() {

        carts.forEach(c -> {
            if (c.getDate().compareTo(c.getLocalDate()) != 0) {
                carts.remove(c);
            }
        });
    }

    public void listProductsByCategory() {

        aisleInventory.forEach((k ,v) -> {

            v.forEach(item -> System.out.printf("%8s $%.2f%n", item.getProduct(), item.getSalesPrice()));
        });
    }

    public NavigableSet<InventoryItem> getInventory() {
        return inventory;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public NavigableMap<String, List<InventoryItem>> getAisleInventory() {
        return aisleInventory;
    }
}
