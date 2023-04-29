package store;
import utils.*;
import Products.*;

import java.util.*;

public class Store {
    /* Since we are managing only one shop, name is final */
    private final String nameStore;
    /* Sets containing every class of product */
    private final HashSet<Flower> flowers;
    private final HashSet<Tree> trees;
    private final HashSet<Decoration> decorations;
    /* List containing every generated ticket */
    private final ArrayList<Ticket> tickets;
    /* Total stock value */
    private double stockValue;
    public static final Store store;

    static {
        /* Instantiating store field and setting value stock */
        store=new Store();
        Store.getStore().setStockValue(Store.getStore().calculatingStockValue());
    }

    private Store() {
        /* Loading sets from file. If file is empty, an empty list is initialized */
        this.nameStore="flowerWorld";
        this.flowers = utils.fromFileProductToFlowerSet();
        this.trees = utils.fromFileProductToTreeSet();
        this.decorations = utils.fromFileProductToDecorationSet();
        this.tickets = utils.fromFileToTicketList();
    }
    public HashSet<Flower> getFlowers() {
        return flowers;
    }

    public HashSet<Tree> getTrees() {
        return trees;
    }

    public HashSet<Decoration> getDecorations() {
        return decorations;
    }

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    public double getStockValue() {
        return stockValue;
    }

    public static Store getStore() {
        return store;
    }

    public void setStockValue(double stockValue) {
        this.stockValue = stockValue;
    }
    public void addFlowerToSet(Flower flower) {
        flowers.add(flower);}
    public void addTreeToSet(Tree tree) {
        trees.add(tree);
    }
    public void addDecorationToSet(Decoration decoration) {
        decorations.add(decoration);
    }
    /* Adds a new tree to the store */
    public  void addTree() {
        try {
            System.out.println("Type tree name");
            String name = Menu.sc.nextLine();
            System.out.println("Type tree price");
            double price = Double.parseDouble(Menu.sc.nextLine());
            System.out.println("Type trees quantity");
            int quantity = Integer.parseInt(Menu.sc.nextLine());
            System.out.println("Type tree height");
            double height = Double.parseDouble(Menu.sc.nextLine());
            Tree tree = new Tree(name, price, quantity, height);
            tree.setNewId();
            this.addTreeToSet(tree);
            this.setStockValue(getStore().calculatingStockValue());
            utils.fromTreeSetToFile(this.getTrees());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    /* Adds a new flower to the store */
    public void addFlower() {
        System.out.println("Type flower name");
        String name= Menu.sc.nextLine();
        System.out.println("Type flower price");
        double price=Double.parseDouble(Menu.sc.nextLine());
        System.out.println("Type flowers quantity");
        int quantity=Integer.parseInt(Menu.sc.nextLine());
        System.out.println("Type flower colour");
        String colour= Menu.sc.nextLine();
        Flower flower = new Flower(name, price, quantity, colour);
        flower.setNewId();
        getStore().addFlowerToSet(flower);
        getStore().setStockValue(getStore().calculatingStockValue());
        utils.fromFlowerSetToFile(getStore().getFlowers());
    }
    /* Adds a new decoration to the store */
    public void addDecoration() {
        System.out.println("Type decoration name");
        String name= Menu.sc.nextLine();
        System.out.println("Type decoration price");
        double price=Double.parseDouble(Menu.sc.nextLine());
        System.out.println("Type decoration quantity");
        int quantity=Integer.parseInt(Menu.sc.nextLine());
        System.out.println("Type tree material: wood (W) or plastic (P)");
        String material= Menu.sc.nextLine();
        Materials auxMaterial=null;
        if(material.equals("W")) {
            auxMaterial=Materials.WOOD;
        }
        else if(material.equals("P")){
            auxMaterial=Materials.PLASTIC;
        }
        Decoration decoration = new Decoration(name, price, quantity, auxMaterial);
        decoration.setNewId();
        getStore().addDecorationToSet(decoration);
        getStore().setStockValue(getStore().calculatingStockValue());
        utils.fromDecorationSetToFile(getStore().getDecorations());
    }
    /* Remove a class of tree from the store */
    public void removeTree() {
        System.out.println("Which tree do you want to remove? Type its id");
        utils.printingSet(getStore().getTrees());
        int id= Integer.parseInt(Menu.sc.nextLine());
        boolean treeNotFound=true;
        Iterator<Tree> it = getStore().getTrees().iterator();
        while(treeNotFound && it.hasNext()) {
            Tree tree = it.next();
            if(tree.getId()==id) {
                it.remove();
                treeNotFound = false;
            }
        }
        this.setStockValue(getStore().calculatingStockValue());
        utils.fromTreeSetToFile(getStore().getTrees());
    }
    /* Remove a class of flower from the store */
    public void removeFlower() {
        System.out.println("Which flower do you want to remove? Type its id");
        utils.printingSet(getStore().getFlowers());
        int id= Integer.parseInt(Menu.sc.nextLine());
        boolean flowerNotFound=true;
        Iterator<Flower> it = getStore().getFlowers().iterator();
        while(flowerNotFound && it.hasNext()) {
            Flower flower = it.next();
            if(flower.getId()==id) {
                it.remove();
                flowerNotFound = false;
            }
        }
        this.setStockValue(getStore().calculatingStockValue());
        utils.fromFlowerSetToFile(getStore().getFlowers());
    }
    /* Remove a class of decoration from the store */
    public void removeDecoration() {
        System.out.println("Which decoration do you want to remove? Type its id");
        utils.printingSet(getStore().getDecorations());
        int id= Integer.parseInt(Menu.sc.nextLine());
        boolean decorationNotFound=true;
        Iterator<Decoration> it = getStore().getDecorations().iterator();
        while(decorationNotFound && it.hasNext()) {
            Decoration decoration = it.next();
            if(decoration.getId()==id) {
                it.remove();
                decorationNotFound = false;
            }
        }
        this.setStockValue(getStore().calculatingStockValue());
        utils.fromDecorationSetToFile(getStore().getDecorations());
    }
    /* Prints all the tickets from the set */
    public void printTickets() {
        for(Ticket ticket: getStore().getTickets()) {
            System.out.println(ticket);
        }
    }
    /* Prints the whole stock */
    public void printingFullStock() {
        System.out.println("*** Tree Stock ***");
        utils.printingSet(getStore().getTrees());
        System.out.println();
        System.out.println("*** Flower Stock ***");
        utils.printingSet(getStore().getFlowers());
        System.out.println();
        System.out.println("*** Decoration Stock ***");
        utils.printingSet(getStore().getDecorations());
        System.out.println();
    }
    /* Prints a brief version of the stock */
    public void printingBriefStock() {
        int trees = 0;
        int flowers = 0;
        int decorations = 0;

        for(Tree tree:getStore().getTrees()) {
            trees+=tree.getQuantity();
        }
        for(Flower flower:getStore().getFlowers()) {
            flowers+=flower.getQuantity();
        }
        for(Decoration decoration:getStore().getDecorations()) {
            decorations+=decoration.getQuantity();
        }
        System.out.println("Total trees in stock: "+trees);
        System.out.println("Total flowers in stock: "+flowers);
        System.out.println("Total decorations in stock: "+decorations);
    }
    /* Calculates the whole stock value */
    public double calculatingStockValue() {
        double stockValue=0;
        for(Tree tree:store.getTrees()) {
           stockValue+=tree.getPrice()*tree.getQuantity();
        }
        for(Flower flower:Store.getStore().getFlowers()) {
            stockValue+=flower.getPrice()*flower.getQuantity();
        }
        for(Decoration decoration:store.getDecorations()) {
            stockValue+=decoration.getPrice()*decoration.getQuantity();
        }
        return stockValue;
    }
    /* Prints the total stock value */
    public void printingStockValue() {
        String s="Total Stock is worth "+ getStore().getStockValue();
        System.out.println(s);
    }
    /* Prints the total tickets value */
    public void printingPurchasesValue() {
        double purchasesValue=0;
        for(Ticket ticket: getStore().getTickets()) {
            purchasesValue+=ticket.getTotalTicketAmount();
        }
        String s="Total Purchases are worth "+purchasesValue;
        System.out.println(s);
    }

}





