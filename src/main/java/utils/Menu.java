package utils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import store.Store;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    public static Scanner sc = new Scanner(System.in);
    /* Running menu */
    public static void menu() {
        boolean continueExecution=true;
        int option;
        while(continueExecution) {
            menuHeader();
            option = Integer.parseInt(sc.nextLine());
            switch (option) {
                case 1: {
                    addProductMenu();
                    break;
                }
                case 2: {
                    removeProductMenu();
                    break;
                }
                case 3: {
                    Store.getStore().printingBriefStock();
                    break;
                }
                case 4: {
                    Store.getStore().printingFullStock();
                    break;
                }
                case 5: {
                    Store.getStore().printingStockValue();
                    break;
                }
                case 6: {
                    addTicketMenu();
                    break;
                }
                case 7: {
                    Store.getStore().printTickets();
                    break;
                }
                case 8: {
                    printingPurchasesValue();
                    break;
                }
                case 0: {
                    continueExecution=false;
                    terminateProgram();
                }
            }
        }
    }
    /* Menu header */
    public static void menuHeader() {
        System.out.println("Choose and option: ");
        System.out.println("1: Adding a Product");
        System.out.println("2: Remove a product");
        System.out.println("3: Printing stock - brief");
        System.out.println("4: Printing stock - detailed");
        System.out.println("5: Printing total stock value");
        System.out.println("6: Generating purchase ticket");
        System.out.println("7: Listing purchase tickets");
        System.out.println("8: Printing purchases incoming");
        System.out.println("0: Program terminating");
    }
    /* Adding product from the menu */
    public static void addProductMenu() {
        System.out.println("Type product type: tree (T), flower (F) or decoration (D)");
        String type = Menu.sc.nextLine();
        System.out.println("Type product name");
        String name = Menu.sc.nextLine();
        System.out.println("Type product price");
        double price = Double.parseDouble(Menu.sc.nextLine());
        System.out.println("Type product quantity");
        int quantity = Integer.parseInt(Menu.sc.nextLine());
        Store.getStore().addProduct(type, name, price, quantity, sc);
    }
    public static void addTicketMenu() {
        List<Document> productsListInTicket = new ArrayList<>();
        double ticketAmount = 0;
        boolean ticketGenerating = true;
        EXTERNALDO:do {
            Store.getStore().printingFullStock();
            System.out.println("Choose product id: ");
            int id = Integer.parseInt(sc.nextLine());
            System.out.println("Choose product demandedQuantity: ");
            int demandedQuantity = Integer.parseInt(sc.nextLine());
            Bson filter = Filters.eq("_id", id);
            FindIterable<Document> queryResults = Store.getStore().getProducts().find(filter);
            for (Document documentQueried : queryResults) {
                int initialQuantity = documentQueried.getInteger("quantity");
                if(demandedQuantity>initialQuantity) {
                    System.out.println("There's no stock enough");
                    continue EXTERNALDO;
                }
                String type = documentQueried.getString("type");
                String name = documentQueried.getString("name");
                double price = documentQueried.getDouble("price");
                Document documentToBeUpdated = new Document();
                documentToBeUpdated.put("_id", Store.getStore().setID(Store.getStore().getProducts()));
                documentToBeUpdated.put("type", type);
                documentToBeUpdated.put("name", name);
                documentToBeUpdated.put("price", price);
                documentToBeUpdated.put("quantity", demandedQuantity);
                productsListInTicket.add(documentToBeUpdated);
                /* Updating products list */
                Store.getStore().getProducts().updateOne(Filters.eq("_id", id), Updates.set("quantity",initialQuantity-demandedQuantity));
            }
            System.out.println("Do you want to add more products? Y/N");
            String adding = Menu.sc.nextLine();
            if(adding.equals("N")) {
                ticketGenerating = false;
            }
        } while (ticketGenerating) ;
        /* closing ticket and updating stocks */
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        LocalDateTime now = LocalDateTime.now();
        String date = df.format(Timestamp.valueOf(now));
        Store.getStore().generateTicket(date, productsListInTicket);
    }
    /* Removing product from the menu */
    public static void removeProductMenu() {
        System.out.println("Which product do you want to remove? Type its id");
        Store.getStore().printingFullStock();
        int id = Integer.parseInt(Menu.sc.nextLine());
        Store.getStore().removeProduct(id);
    }
    /* Program ends */
    public static void terminateProgram() {
        sc.close();
        Store.getStore().getMongoClient().close();
    }

    public static void printingPurchasesValue() {
        System.out.println("Total purchases are worth "+Store.getStore().calculatingPurchasesValue());
    }
}
