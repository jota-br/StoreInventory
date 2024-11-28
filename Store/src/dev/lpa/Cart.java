package dev.lpa;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {

    private static int LAST_ID = 1;
    private int id;
    private Map<InventoryItem, Integer> products;
    private final String date;
    private final String type;

    public Cart(String type) {
        this.id = LAST_ID++;
        this.products = new LinkedHashMap<>(50);
        this.date = getLocalDate();
        this.type = type.toUpperCase();
    }

    public void addItem(InventoryItem item, int qty) {

        if (item == null) {
            System.out.println("Unable to identify item");
            return;
        }

        if (products.get(item) != null) {
            products.replace(item, qty);
        } else {
            products.put(item, qty);
        }
    }

    public void removeItem(InventoryItem item) {

        if (item == null) {
            System.out.println("Unable to identify item");
            return;
        }

        if (products.get(item) != null) {
            products.remove(item);
        } else {
            System.out.println("Item not in the Cart");
        }
    }

    public void printSalesSlip() {

        System.out.printf("%5$s%n%-10s %-10s %-10s %-10s%n", "Item", "Quantity", "Unit Price", "Total Price", "-".repeat(50));
        products.forEach((key, value) -> {
            System.out.printf("%-10s %-10d $%-10.2f $%-10.2f%n", key.getProduct().name(), value, key.getSalesPrice(), key.getSalesPrice() * value);
        });
        System.out.println("-".repeat(50));
    }

    public String getLocalDate() {
        return LocalDate.now().getDayOfMonth() + "-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getYear();
    }

    public String getDate() {
        return date;
    }

    public Map<InventoryItem, Integer> getProducts() {
        return products;
    }
}
