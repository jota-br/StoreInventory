package dev.lpa;

import java.util.Comparator;
import java.util.Random;

public class InventoryItem implements Comparable<InventoryItem> {

    private Product product;
    private int qtyTotal;
    private int qtyReserved;
    private int qtyReorder;
    private int qtyLow;
    private double salesPrice;

    public InventoryItem(String name, String manufacturer, String category, int qtyTotal, int qtyReorder, int qtyLow, double salesPrice) {
        this.product = new Product(getSKU(name, manufacturer, category), name, manufacturer, category);
        this.qtyTotal = qtyTotal;
        this.qtyReorder = qtyReorder;
        this.qtyLow = qtyLow;
        this.salesPrice = salesPrice;
    }

    private String getSKU(String name, String manufacturer, String category) {

        String categoryIdentifier = category.substring(0, 3);
        String productIdentifier = name.substring(0, 3);
        String manufacturerIdentifier = manufacturer.substring(0, 3);
        String sequenceValue = name.charAt(0) + String.valueOf(new Random().nextInt(100, 1000));
        return (categoryIdentifier + productIdentifier + manufacturerIdentifier + sequenceValue).toUpperCase();
    }

    public void reserveItem(int qty) {

        int available = this.qtyTotal - this.qtyReserved;
        if (available >= qty) {
            this.qtyReserved += qty;
            System.out.printf("%-15s %-8d units reserved%n", this.product, qty);
        } else {
            System.out.printf("Required quantity not available, in stock: %d%n", available);
        }
    }

    public void releaseItem(int qty) {

        this.qtyReserved -= qty;
        System.out.printf("%-15s %-8d units released%n", this.product, qty);
    }

    public void sellItem(int qty) {

        this.qtyReserved -= qty;
        this.qtyTotal -= qty;

        if (this.qtyTotal <= qtyLow) {
            System.out.printf("Product %s Order placed%n", this.product.name());
            placeInventoryOrder(qtyReorder);
        }
    }

    public void placeInventoryOrder(int qty) {

        if (qty <= 0) {
            System.out.println("Quantity must be greater than zero");
            return;
        }

        this.qtyTotal += qty;
        System.out.printf("%-15s %-8d units placed in inventory%n", this.product, qty);
    }

    public Product getProduct() {
        return product;
    }

    public int getQtyTotal() {
        return qtyTotal;
    }

    public int getQtyReserved() {
        return qtyReserved;
    }

    public int getQtyReorder() {
        return qtyReorder;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    @Override
    public int compareTo(InventoryItem o) {
        int result = this.getProduct().category.compareTo(o.getProduct().category);
        return (result == 0) ? this.getProduct().name().compareTo(o.getProduct().name()) : result;
    }

    record Product(String sku, String name, String manufacturer, String category) implements Comparable<Product> {

        static Comparator<Product> SortByCategoryThenName = Comparator.comparing(Product::category).thenComparing(Product::name);
        static Comparator<Product> SortByNameThenCategory = Comparator.comparing(Product::name).thenComparing(Product::category);
        static Comparator<Product> sortByName = Comparator.comparing(Product::name);
        static Comparator<Product> sortByCategory = Comparator.comparing(Product::category);

        @Override
        public String toString() {
            return "%-15s %-15s %-10s %-10s".formatted(name, category, manufacturer, sku);
        }

        @Override
        public int compareTo(Product o) {
            int result = this.category.compareTo(o.category);
            return (result == 0) ? this.name.compareTo(o.name) : result;
        }
    }
}
