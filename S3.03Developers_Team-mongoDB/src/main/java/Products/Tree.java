package Products;

import store.Store;

import java.util.Comparator;

public final class Tree extends Product{
    private double height;
    public Tree(String name, double price, int quantity, double height) {
        super(name, price, quantity);
        this.height=height;
    }
    public Tree() {

    }
    /* Id is set getting the max id in the set and adding +1 */
    public void setNewId() {
        if(!Store.getStore().getTrees().isEmpty()) {
            this.id = Store.getStore().getTrees().stream().max(Comparator.comparing(s -> s.getId())).get().getId() + 1;
        }
        else {
            this.id=Product.INITIALID;
        }
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public String toString() {

        return "ProductID: "+this.getId()+" | "+super.toString()+" | Product height: "+this.height;
    }

}
