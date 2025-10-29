import java.util.concurrent.BlockingQueue;

public class WarehouseWorker implements Runnable {
    private BlockingQueue<Order> orderQueue;
    private Shop shop;
    private String workerName;
    
    public WarehouseWorker(String name, BlockingQueue<Order> queue, Shop shop) {
        this.workerName = name;
        this.orderQueue = queue;
        this.shop = shop;
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                Order order = orderQueue.take();
                System.out.println(workerName + " processing: " + order.getCustomerName() + " - " + order.getQuantity() + "x " + order.getProduct().getName());
                boolean success = shop.orderProduct(order.getProduct(), order.getQuantity());
                order.setProcessed(true);
                order.setSuccess(success);
                shop.addOrder(order);
                System.out.println(workerName + " completed: " + order.getCustomerName() + " - " + (success ? "SUCCESS" : "FAILED"));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}