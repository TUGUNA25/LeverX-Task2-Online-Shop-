import java.util.concurrent.*;
import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Create more products
        Product toy = new Product("Toy Car", 15.99);
        Product book = new Product("Java Book", 29.99);
        Product laptop = new Product("Laptop", 899.99);
        Product phone = new Product("Smartphone", 499.99);
        Product headphone = new Product("Headphones", 79.99);
        
        Shop shop = new Shop();
        shop.addProduct(toy, 3);
        shop.addProduct(book, 5);
        shop.addProduct(laptop, 2);
        shop.addProduct(phone, 4);
        shop.addProduct(headphone, 6);
        
        BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();
        
        System.out.println("/////// STARTING SIMULATION ///////");
        System.out.println("Initial stock:");
        System.out.println("  Toy Cars: " + shop.getProductQuantity(toy));
        System.out.println("  Java Books: " + shop.getProductQuantity(book));
        System.out.println("  Laptops: " + shop.getProductQuantity(laptop));
        System.out.println("  Smartphones: " + shop.getProductQuantity(phone));
        System.out.println("  Headphones: " + shop.getProductQuantity(headphone));
        
        // Add one more worker (total 3 now)
        ExecutorService workerExecutor = Executors.newFixedThreadPool(3);
        workerExecutor.execute(new WarehouseWorker("worker-1", orderQueue, shop));
        workerExecutor.execute(new WarehouseWorker("worker-2", orderQueue, shop));
        workerExecutor.execute(new WarehouseWorker("worker-3", orderQueue, shop));
        
        ExecutorService customerExecutor = Executors.newFixedThreadPool(8);
        
        for (int i = 1; i <= 5; i++) {
            int customerId = i;
            Runnable customerTask = () -> {
                String customerName = "Customer-" + customerId;
                try {
                    Order order = new Order(toy, 1, customerName);
                    orderQueue.put(order);
                    System.out.println(customerName + " placed order for 1 Toy Car");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            };
            customerExecutor.execute(customerTask);
        }
        
        for (int i = 6; i <= 8; i++) {
            int customerId = i;
            Runnable customerTask = () -> {
                String customerName = "Customer-" + customerId;
                try {
                    Order order = new Order(book, 1, customerName);
                    orderQueue.put(order);
                    System.out.println(customerName + " placed order for 1 Java Book");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            };
            customerExecutor.execute(customerTask);
        }
        
        for (int i = 9; i <= 10; i++) {
            int customerId = i;
            Runnable customerTask = () -> {
                String customerName = "Customer-" + customerId;
                try {
                    Order order = new Order(laptop, 1, customerName);
                    orderQueue.put(order);
                    System.out.println(customerName + " placed order for 1 Laptop");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            };
            customerExecutor.execute(customerTask);
        }
        
        for (int i = 11; i <= 14; i++) {
            int customerId = i;
            Runnable customerTask = () -> {
                String customerName = "Customer-" + customerId;
                try {
                    Order order = new Order(phone, 1, customerName);
                    orderQueue.put(order);
                    System.out.println(customerName + " placed order for 1 Smartphone");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            };
            customerExecutor.execute(customerTask);
        }
        
        for (int i = 15; i <= 18; i++) {
            int customerId = i;
            Runnable customerTask = () -> {
                String customerName = "Customer-" + customerId;
                try {
                    Order order = new Order(headphone, 1, customerName);
                    orderQueue.put(order);
                    System.out.println(customerName + " placed order for 1 Headphones");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            };
            customerExecutor.execute(customerTask);
        }
        
        customerExecutor.shutdown();
        customerExecutor.awaitTermination(3, TimeUnit.SECONDS);
        
        System.out.println("\nWaiting for orders to process...");
        Thread.sleep(3000);
        
        workerExecutor.shutdownNow();
        
        System.out.println("\n/////// FINAL RESULT ///////");
        System.out.println("Toy Cars left: " + shop.getProductQuantity(toy) + " (started with 3)");
        System.out.println("Java Books left: " + shop.getProductQuantity(book) + " (started with 5)");
        System.out.println("Laptops left: " + shop.getProductQuantity(laptop) + " (started with 2)");
        System.out.println("Smartphones left: " + shop.getProductQuantity(phone) + " (started with 4)");
        System.out.println("Headphones left: " + shop.getProductQuantity(headphone) + " (started with 6)");
        System.out.println("Orders in queue: " + orderQueue.size());
        
        System.out.println("\n/////// ANALYTICS ///////");
        
        List<Order> allOrders = shop.getAllOrders();
        
        // 1. Total number of orders
        long totalOrders = allOrders.parallelStream().count();
        System.out.println("Total number of orders: " + totalOrders);
        
        // 2. Total profit from successful orders
        double totalProfit = allOrders.parallelStream()
            .filter(Order::isSuccess)
            .mapToDouble(order -> order.getProduct().getPrice() * order.getQuantity())
            .sum();
        System.out.println("Total profit: $" + String.format("%.2f", totalProfit));
        
        // 3. Top 3 best-selling products
        System.out.println("Top 3 best-selling products:");
        allOrders.parallelStream()
            .filter(Order::isSuccess)
            .collect(Collectors.groupingByConcurrent(
                Order::getProduct,
                Collectors.summingLong(Order::getQuantity)
            ))
            .entrySet()
            .stream()
            .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
            .limit(3)
            .forEach(entry -> 
                System.out.println("  - " + entry.getKey().getName() + 
                    ": " + entry.getValue() + " units ($" + 
                    String.format("%.2f", entry.getKey().getPrice() * entry.getValue()) + " revenue)")
            );
        
    }
}