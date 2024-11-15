public class Customer {
    private int customerId;
    private String customerName;

    public Customer(int customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public static Customer fromCSV(String csvLine) {
        String[] fields = csvLine.split(",");
        int customerId = Integer.parseInt(fields[0].trim());
        String customerName = fields[1].trim();
        return new Customer(customerId, customerName);
    }
}