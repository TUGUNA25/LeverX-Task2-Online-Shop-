import java.util.HashMap;

public class Shop {
    private HashMap<Product,Integer> warehouse;
    public Shop() {
        this.warehouse = new HashMap<>();
    }

    public void addProduct(Product product,Integer quantity){
        warehouse.put(product,quantity);
    }

    public void addOneProduct(Product product){
        warehouse.compute(product,(key,oldvalue) -> oldvalue+1);
        System.out.println();
    }

    public boolean orderProduct(Product product, Integer quantity){
        if (quantity > warehouse.get(product)){
            return false;
        }
        warehouse.compute(product,(key,oldvalue) -> oldvalue-quantity);
        return true;
    }

    public int getProductQuantity(Product product){
        return warehouse.get(product);
    }


   
}