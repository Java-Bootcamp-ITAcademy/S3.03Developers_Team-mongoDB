package Products;

import store.Store;

import java.util.Comparator;

public final class  Flower extends Product {
    private String colour;

    public Flower(String name, double price, int quantity, String colour) {
        super(name, price, quantity);
        this.colour = colour;
    }

    public Flower() {

    }
    /* Id is set getting the max id in the set and adding +1 */
    public void setNewId() {
        if(!Store.getStore().getFlowers().isEmpty()) {
            this.id = Store.getStore().getFlowers().stream().max(Comparator.comparing(s -> s.getId())).get().getId() + 1;
        }
        else {
            this.id=Product.INITIALID;
        }
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    @Override
    public String toString() {
        return "ProductID: "+this.getId()+" | "+super.toString()+" | Product colour: "+this.colour;
    }
}

