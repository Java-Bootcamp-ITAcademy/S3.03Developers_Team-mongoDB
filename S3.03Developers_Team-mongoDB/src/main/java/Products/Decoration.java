package Products;

import store.Store;

import java.util.Comparator;

public final class Decoration extends Product {
    private Materials material;
    public Decoration(String name, double price, int quantity, Materials material) {
        super(name, price, quantity);
        this.material=material;
    }

    public void setMaterial(Materials material) {
        this.material = material;
    }

    public Decoration() {

    }
    /* Id is set getting the max id in the set and adding +1 */
    public void setNewId() {
        if(!Store.getStore().getDecorations().isEmpty()) {
            this.id = Store.getStore().getDecorations().stream().max(Comparator.comparing(s -> s.getId())).get().getId() + 1;
        }
        else {
            this.id=Product.INITIALID;
        }
    }

    public Materials getMaterial() {
        return material;
    }

    @Override
    public String toString() {
        return "ProductID: "+this.getId()+" | "+super.toString()+" | Product type: "+this.material;
    }
}
