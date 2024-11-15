import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Sale {
    private int saleId;
    private LocalDateTime saleDateTime;
    private int customerId;
    private int productId;

    public Sale(int saleId, LocalDateTime saleDateTime, int customerId, int productId) {
        this.saleId = saleId;
        this.saleDateTime = saleDateTime;
        this.customerId = customerId;
        this.productId = productId;
    }

    public int getSaleId() {
        return saleId;
    }

    public LocalDateTime getSaleDateTime() {
        return saleDateTime;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getProductId() {
        return productId;
    }

    public static Sale fromCSV(String csvLine) {
        String[] fields = csvLine.split(",");
        int saleId = Integer.parseInt(fields[0].trim());
        LocalDateTime saleDateTime = LocalDateTime.parse(fields[1].trim(), DateTimeFormatter.ISO_DATE_TIME);
        int customerId = Integer.parseInt(fields[2].trim());
        int productId = Integer.parseInt(fields[3].trim());
        return new Sale(saleId, saleDateTime, customerId, productId);
    }
}