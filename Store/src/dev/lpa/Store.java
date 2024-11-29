package dev.lpa;

import java.util.*;

public class Store {

    private Map<String, InventoryItem> inventory;
    private NavigableSet<Cart> carts;
    private NavigableMap<String, TreeSet<InventoryItem>> aisleInventory;

    public Comparator<InventoryItem> sortByName = Comparator.comparing(o -> o.getProduct().name());
    public Comparator<InventoryItem> sortByCategory = Comparator.comparing(o -> o.getProduct().category());

    public Store() {
        this.inventory = new HashMap<>();
        this.carts = new TreeSet<>(Comparator.comparing(Cart::getId));
        this.aisleInventory = new TreeMap<>();
    }

    public void manageStoreCarts() {
        carts.add(new Cart(Cart.Type.VIRTUAL));

        listInventory();

        InventoryItem box = new InventoryItem("Steel Box", "Japan Steel IT", "Furniture", 1000, 950, 50, 125.99);
        InventoryItem table = new InventoryItem("Mahogany Executive Table", "Made HandBY", "Furniture", 1000, 950, 50, 5675.99);
        InventoryItem woodBox = new InventoryItem("Wood Box", "Made HandBY", "Furniture", 1000, 950, 50, 110.00);
        InventoryItem cement = new InventoryItem("Cement", "Made HandBY", "Construction", 1000, 950, 50, 15.00);

        inventory.put(box.getProductSKU(), box);
        inventory.put(table.getProductSKU(), table);
        inventory.put(woodBox.getProductSKU(), woodBox);
        inventory.put(cement.getProductSKU(), cement);

        listInventory();

        inventory.forEach((k, v) -> {

            addItemToInventory(v);
            carts.getFirst().addItem(v, 10);
        });

        listInventory();

        carts.getFirst().removeItem(woodBox, 2);
        carts.getFirst().removeItem(cement, 299);

        listProductsByCategory();
        if (!checkOutCarts(carts.getFirst())) System.out.println("Cart CheckOut failed");

        listInventory();
    }

    public boolean checkOutCarts(Cart cart) {

        for (var cartItem : cart.getProducts().entrySet()) {
            InventoryItem item = getInventory().get(cartItem.getKey());
            int qty = cartItem.getValue();

            if(!item.sellItem(qty)) return false;
        }

        cart.printSalesSlip(inventory);
        carts.remove(cart);
        return true;
    }

    public void addItemToInventory(InventoryItem item) {

        TreeSet<InventoryItem> list = this.aisleInventory
                .computeIfAbsent(item.getProduct().category(), k -> new TreeSet<>(Comparator.comparing(InventoryItem::getProductName)));
        list.add(item);
    }

    public void abandonCarts() {

    }

    public void listInventory() {
        inventory.forEach((k, v) -> {

            System.out.println("InventoryItem{" +
                    "product=" + v.getProduct() +
                    ", qtyTotal=" + v.getQtyTotal() +
                    ", qtyReserved=" + v.getQtyReserved() +
                    '}');
        });
    }

    public void listProductsByCategory() {

        String separate = "-".repeat(20);
        aisleInventory.forEach((k ,v) -> {

            System.out.printf("%1$s %2$s %1$s%n", separate, k);
            v.forEach(item -> System.out.printf("%8s $%.2f%n", item.getProduct(), item.getSalesPrice()));
        });
        System.out.println("-".repeat(50));
    }

    public Map<String, InventoryItem> getInventory() {
        return inventory;
    }

    public NavigableSet<Cart> getCarts() {
        return carts;
    }

    public NavigableMap<String, TreeSet<InventoryItem>> getAisleInventory() {
        return aisleInventory;
    }
}
