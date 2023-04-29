package store;

import utils.Menu;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import static com.mongodb.client.model.Sorts.descending;

public class Store {
    private final String nameStore;
    private final MongoClient mongoClient;
    private MongoDatabase shopDatabase;
    private MongoCollection<Document> products;
    private MongoCollection<Document> tickets;
    private double stockValue;
    public static final Store store;
    public static final Properties properties;

    static {
        /* Loading properties and instantiating new store */
        final String PROPERTIESPATH="config.properties";
        properties=new Properties();
        try {
            properties.load(new FileInputStream(PROPERTIESPATH));
            store=new Store();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /* Setting connection and getting collections */
    {
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(properties.getProperty("connection")))
                .serverApi(serverApi)
                .build();
        this.mongoClient = MongoClients.create(settings);
        this.shopDatabase = mongoClient.getDatabase("shop");
        this.products = shopDatabase.getCollection("products");
        this.tickets = shopDatabase.getCollection("tickets");
        this.settingStockValue();
        /* Connection succeed */
        System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
    }

    public Store() {
        this.nameStore = "flowerWorld";
        this.settingStockValue();
    }

    public double getStockValue() {
        return stockValue;
    }
    public static Store getStore() {
        return store;
    }
    public MongoDatabase getShopDatabase() {
        return shopDatabase;
    }
    public MongoClient getMongoClient() {
        return mongoClient;
    }
    public MongoCollection<Document> getProducts() {
        return products;
    }
    public MongoCollection<Document> getTickets() {
        return tickets;
    }

    public void setTickets(MongoCollection<Document> tickets) {
        this.tickets = tickets;
    }

    public void setStockValue(double stockValue) {
        this.stockValue = stockValue;
    }
    /* Setting id both for products and tickets. Custom version of autoincrement */
    public int setID(MongoCollection<Document> list) {
        if(list.countDocuments()==0) {
            return 1;
        }
        else {
            return Objects.requireNonNull(list.find().sort(descending("_id")).limit(1).first()).getInteger("_id") + 1;
        }
    }
    public void setProducts(MongoCollection<Document> products) {
        this.products = products;
    }


    public void setShopDatabase(MongoDatabase shopDatabase) {
        this.shopDatabase = shopDatabase;
    }

    /* Adds a new product to the store */
    public void addProduct(String type, String name, double price, int quantity, Scanner sc) {
        Document document = new Document();
        document.put("_id", setID(this.getProducts()));
        document.put("type", type);
        document.put("name", name);
        document.put("price", price);
        document.put("quantity", quantity);
        switch (type) {
            case "T" -> {
                System.out.println("Type tree height");
                double height = Double.parseDouble(sc.nextLine());
                document.put("height", height);
            }
            case "F" -> {
                System.out.println("Type flower colour");
                String colour = sc.nextLine();
                document.put("colour", colour);
            }
            case "D" -> {
                System.out.println("Type tree material: wood (wood) or plastic (plastic)");
                String material = sc.nextLine();
                document.put("material", material);
            }
        }
        products.insertOne(document);
        this.settingStockValue();
    }

    /* Remove a product from the store */
    public void removeProduct(int id) {
        products.deleteOne(Filters.eq("_id",id));
        this.settingStockValue();
    }
    /* Generating new ticket */
    public void generateTicket(String date, List<Document> productsListInTicket) {
        double ticketAmount = 0;
        for(Document product: productsListInTicket) {
            ticketAmount+=product.getDouble("price")*product.getInteger("quantity");
        }
        Document ticketDocument = new Document();
        ticketDocument.put("_id",this.setID(this.getTickets()));
        ticketDocument.put("date",date);
        ticketDocument.put("products",productsListInTicket);
        ticketDocument.put("ticketAmount",ticketAmount);
        this.getTickets().insertOne(ticketDocument);
        this.settingStockValue();
    }

    /* Prints all the tickets from the set */
    public void printTickets() {
        for (Document ticket : this.getTickets().find()) {
            System.out.println("******** Ticket ********");
            System.out.println("Ticket reference: "+ticket.getInteger("_id"));
            System.out.println("Date: " + ticket.getString("date"));
            List<Document> productsList = (List<Document>) ticket.get("products");
            for (Document product : productsList) {
                System.out.println("Product name: " + product.get("name") + " | Product type: " + product.get("type") + " | Product quantity: " + product.get("quantity") + " | Product price: " + product.get("price"));
            }
            System.out.printf("Total Amount: %.2f\n", ticket.getDouble("ticketAmount"));
            System.out.println("************************ ");
            System.out.println("");
        }
    }


    /* Prints the whole stock */
    public void printingFullStock() {
        System.out.println("****** Trees ******");
        MongoCursor<Document> treeList = products.find(Filters.eq("type","T")).iterator();
        while(treeList.hasNext()) {
            System.out.println(treeList.next());
        }
        System.out.println("");
        System.out.println("****** Flowers ******");
        FindIterable<Document> flowerList = products.find(Filters.eq("type","F"));
        for(Document flower:flowerList) {
            System.out.println(flower);
        }
        System.out.println("");
        System.out.println("****** Decoration ******");
        FindIterable<Document> decorationList = products.find(Filters.eq("type","D"));
        for(Document decoration:decorationList) {
            System.out.println(decoration);
        }
    }
    /* Prints a brief version of the stock */
    public void printingBriefStock() {
        int totalTrees=0;
        int totalFlowers=0;
        int totalDecorations=0;
        FindIterable<Document> treeList = products.find(Filters.eq("type","T"));
        for(Document tree:treeList) {
            totalTrees+=tree.getInteger("quantity");
        }
        System.out.println("Total trees stock is: "+totalTrees);

        FindIterable<Document> flowerList = products.find(Filters.eq("type","F"));
        for(Document flower:flowerList) {
            totalFlowers+=flower.getInteger("quantity");
        }
        System.out.println("Total flowers stock is: "+totalFlowers);

        FindIterable<Document> decorationList = products.find(Filters.eq("type","D"));
        for(Document decoration:decorationList) {
            totalDecorations+=decoration.getInteger("quantity");
        }
        System.out.println("Total decorations stock is: "+totalDecorations);
    }
    /* Calculates the whole stock value */
    public void settingStockValue() {
        double price;
        int quantity;
        this.stockValue=0;
        FindIterable<Document> products = getProducts().find();
        for(Document document:products) {
            price = document.getDouble("price");
            quantity = document.getInteger("quantity");
            this.stockValue+=price*quantity;
        }
    }
    /* Prints the total stock value */
    public void printingStockValue() {
        System.out.printf("Total stock is worth %.2f\n",Store.getStore().getStockValue());
    }
    /* Prints the total tickets value */
    public double calculatingPurchasesValue() {
        double purchasetotalValue=0;
        MongoCursor<Document> tickets = this.getTickets().find().iterator();
        while(tickets.hasNext()) {
            purchasetotalValue+=tickets.next().getDouble("ticketAmount");
        }
        return purchasetotalValue;
    }

}





