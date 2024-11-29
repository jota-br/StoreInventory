package dev.lpa;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BinaryOperator;

public class Cart {

    enum Type {VIRTUAL, PHYSICAL}

    private static int LAST_ID = 1;
    private int id;
    private Map<String, Integer> products;
    private final String date;
    private final Type type;

    public Cart(Type type) {
        this.id = LAST_ID++;
        this.products = new LinkedHashMap<>(50);
        this.date = getLocalDate();
        this.type = type;
    }

    public void addItem(InventoryItem item, int qty) {

        if (item.reserveItem(qty)) this.products.merge(item.getProductSKU(), qty, Integer::sum);
    }

    public void removeItem(InventoryItem item, int qty) {

        BinaryOperator<Integer> subtract = (a, b) -> a - b;
        if (products.get(item.getProductSKU()) != null) {
            products.merge(item.getProductSKU(), qty, subtract);
        } else {
            System.out.println("Item not in the Cart");
            return;
        }

        item.releaseItem(qty);
        if (products.get(item.getProductSKU()) <= 0) products.remove(item.getProductSKU());
    }

    public void printSalesSlip(Map<String, InventoryItem> inventory) {

        System.out.printf("%1$s %2$s %1$s%n", "-".repeat(20), "RECEIPT");
        products.forEach((sku, qty) -> {
            InventoryItem item = inventory.get(sku);
            double totalPrice = qty * item.getSalesPrice();

            System.out.printf("\t%s %s %dx $%.2f $%.2f%n", item.getProduct().name(), sku, qty, item.getSalesPrice(), totalPrice);
        });
        System.out.println("-".repeat(50));
    }

    public String getLocalDate() {
        return LocalDate.now().getDayOfMonth() + "-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getYear();
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public Map<String, Integer> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", products=" + products +
                ", date='" + date + '\'' +
                ", type=" + type +
                '}';
    }
}
