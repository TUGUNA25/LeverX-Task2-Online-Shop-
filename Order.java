public class Order {
    private static int nextId = 1;
    private int orderId;
    private Product product;
    private int quantity;
    private String customerName;
    private boolean processed;
    private boolean success;

    public Order(Product product, int quantity, String customerName) {
        this.orderId = nextId++;
        this.product = product;
        this.quantity = quantity;
        this.customerName = customerName;
        this.processed = false;
        this.success = false;
    }

    
    public int getOrderId() { return orderId; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public String getCustomerName() { return customerName; }
    public boolean isProcessed() { return processed; }
    public boolean isSuccess() { return success; }
    
    
    public void setProcessed(boolean processed) { this.processed = processed; }
    public void setSuccess(boolean success) { this.success = success; }

    @Override
    public String toString() {
        return "Order#" + orderId + " " + customerName + " - " + quantity + "x " + product.getName() + 
               " - Status" + (processed ? (success ? "SUCCESS" : "FAILED") : "PENDING");
    }
}
