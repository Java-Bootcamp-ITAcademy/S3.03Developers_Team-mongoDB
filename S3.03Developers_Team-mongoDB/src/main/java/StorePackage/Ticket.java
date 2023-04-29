package store;
import utils.*;
import Products.Decoration;
import Products.Flower;
import Products.Product;
import Products.Tree;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

public class Ticket {
    public final static int INITIALID=1;
    private int id;
    private String products;
    private double totalTicketAmount;
    private Date date;
    {
        products = "";
    }
    public Ticket() {
    }
    public int getId() {
        return id;
    }
    /* Id is set getting the max id in the set and adding +1 */
    public void setNewId() {
        if(!Store.getStore().getTickets().isEmpty()) {
            this.id = Store.getStore().getTickets().stream().max(Comparator.comparing(s -> s.getId())).get().getId() + 1;
        }
        else {
            this.id= Product.INITIALID;
        }
    }

    public String getProducts() {
        return products;
    }

    public double getTotalTicketAmount() {
        return totalTicketAmount;
    }

    public Date getDate() {
        return date;
    }
    /* Generating new ticket */
    public void generateTicket()  {
            boolean ticketGenerating = true;
            while (ticketGenerating) {
                Store.getStore().printingFullStock();
                System.out.println("Choose the list: decorations, flowers, trees");
                String option = Menu.sc.nextLine();
                System.out.println("Choose product id: ");
                int id = Integer.parseInt(Menu.sc.nextLine());
                System.out.println("Choose product quantity: ");
                int quantity = Integer.parseInt(Menu.sc.nextLine());
                switch (option) {
                    /* adding a decor to ticket */
                    case "decorations" -> {
                        Decoration decoration = (Decoration) utils.findProduct(Store.store.getDecorations(), id);
                        if (decoration.getQuantity() < quantity) {
                            System.out.println("There is not stock enough");
                            continue;
                        }
                        utils.updateProductQuantity(Store.store.getDecorations(), id, quantity);
                        Decoration decorAux = new Decoration(decoration.getName(), decoration.getPrice(), quantity, decoration.getMaterial());
                        decorAux.setId(decoration.getId());
                        this.products += decorAux + "\n";
                        this.totalTicketAmount += decorAux.getPrice() * decorAux.getQuantity();
                        utils.fromDecorationSetToFile(Store.getStore().getDecorations());
                    }
                    /* adding a flower to ticket */
                    case "flowers" -> {
                        Flower flower = (Flower) utils.findProduct(Store.store.getFlowers(), id);
                        if (flower.getQuantity() < quantity) {
                            System.out.println("There is not stock enough");
                            continue;
                        }
                        utils.updateProductQuantity(Store.store.getFlowers(), id, quantity);
                        Flower flowerAux = new Flower(flower.getName(), flower.getPrice(), quantity, flower.getColour());
                        flowerAux.setId(flower.getId());
                        this.products += flowerAux + "\n";
                        this.totalTicketAmount += flowerAux.getPrice() * flowerAux.getQuantity();
                        utils.fromFlowerSetToFile(Store.getStore().getFlowers());
                    }
                    /* adding tree to ticket */
                    case "trees" -> {
                        Tree tree = (Tree) utils.findProduct(Store.store.getTrees(), id);
                        if (tree.getQuantity() < quantity) {
                            System.out.println("There is not stock enough");
                            continue;
                        }
                        utils.updateProductQuantity(Store.store.getTrees(), id, quantity);
                        Tree treeAux = new Tree(tree.getName(), tree.getPrice(), quantity, tree.getHeight());
                        treeAux.setId(tree.getId());
                        this.products += treeAux + "\n";
                        this.totalTicketAmount += treeAux.getPrice() * treeAux.getQuantity();
                        utils.fromTreeSetToFile(Store.getStore().getTrees());
                    }
                }
                System.out.println("Do you want to add more products? Y/N");
                String yn= Menu.sc.nextLine();
                if(yn.equals("N")) {
                    ticketGenerating=false;
                }
            }
            /* closing ticket and updating stocks */
            LocalDateTime now = LocalDateTime.now();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            this.date= java.sql.Timestamp.valueOf(now);
            this.setNewId();
        Store.store.getTickets().add(this);
        utils.fromTicketsListToFile(Store.store.getTickets());
        Store.store.setStockValue(Store.getStore().calculatingStockValue());
    }

    @Override
    public String toString() {
        String toReturn="******* TICKET  *******"+"\n";
        toReturn+="Ticket ID: "+this.getId()+"\n";
        toReturn+=this.getProducts()+"\n";
        toReturn+="Total amount: "+this.getTotalTicketAmount()+"\n";
        toReturn+="Date: "+this.getDate()+"\n";
        toReturn+="*******************"+"\n";
        return toReturn;
    }
}


