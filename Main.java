public class Main {
    public static void main(String[] args) {
        Product pro1 = new Product("cowboy-toy", 12.1, 10);
        Product pro2 = new Product("toy-car", 9.3, 12);
        Shop xdshop = new Shop();
        xdshop.addProduct(pro1, 10);
        xdshop.orderProduct(pro1, 3);
        System.out.println(xdshop.getProductQuantity(pro1));
    }
}
