import java.util.concurrent.ConcurrentHashMap;

public class Shop {
    private ConcurrentHashMap<Product,Integer> warehouse;
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


    public int getProductQuantity(Product product){return warehouse.get(product);}

   
}