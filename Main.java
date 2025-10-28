import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        Product toy = new Product("Toy Car", 15.99);
        Product book = new Product("Java Book", 29.99);

        Shop shop = new Shop();
        shop.addProduct(toy, 11);
        shop.addProduct(book, 5);

        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        // Create 3 customers all trying to buy the LAST toy
        for (int i = 1; i <= 3; i++) {
            int customerId = i;
            Runnable customerTask = () -> {  
                String customerName = "Customer-" + customerId;
                boolean success = shop.orderProduct(toy, 1); 
                int stockafter = shop.getProductQuantity(toy);
                System.out.println(customerName + " tried to buy Toy Car: " + (success ? "SUCCESS" : "FAILED") + " - Stock after: " + stockafter);
            };
            executor.execute(customerTask);  
        }
    }
}
