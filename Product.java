public class Product {
    private int productId;
    private String productName;
    private double price;

    public Product(int productId, String productName, double price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public static Product fromCSV(String csvLine) {
        String[] fields = csvLine.split(",");
        int productId = Integer.parseInt(fields[0].trim());
        String productName = fields[1].trim();
        double price = Double.parseDouble(fields[2].trim());
        return new Product(productId, productName, price);
    }
}