import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Shop {
    private ConcurrentHashMap<Product,Integer> warehouse;
    private List<Order> allOrders = new CopyOnWriteArrayList<>();
    //private ConcurrentHashMap<Product, Object> productLocks;
    public Shop() {
        this.warehouse = new ConcurrentHashMap<>();
    }

    public void addProduct(Product product,Integer quantity){
        warehouse.put(product,quantity);
    }


    public boolean orderProduct(Product product, Integer quantity) {
        // use the product object itself as the lock!!!!!!!!!!!!!!!!!!!!!!!!!
        // avoid example where both clients want to buy last item
        synchronized (product) {
            if (quantity > warehouse.get(product)){
                return false;
            }
            warehouse.compute(product,(key,oldvalue) -> oldvalue-quantity);
            return true;
        }
    }

    public void addOrder(Order order) {
        allOrders.add(order);
    }

    public List<Order> getAllOrders() {
        return allOrders;
    }

    public int getProductQuantity(Product product){return warehouse.get(product);}

    public long getTotalOrders() {
        return allOrders.parallelStream().count();
    }
    
    public double getTotalProfit() {
        return allOrders.parallelStream()
            .filter(Order::isSuccess)
            .mapToDouble(order -> order.getProduct().getPrice() * order.getQuantity())
            .sum();
    }
    
    public Map<Product, Long> getTopSellingProducts(int limit) {
        return allOrders.parallelStream()
            .filter(Order::isSuccess)
            .collect(Collectors.groupingByConcurrent(
                Order::getProduct,
                Collectors.summingLong(Order::getQuantity)
            ))
            .entrySet()
            .stream()
            .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
            .limit(limit)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
            ));
    }

   
}