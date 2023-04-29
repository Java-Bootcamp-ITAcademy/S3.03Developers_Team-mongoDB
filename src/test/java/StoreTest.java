
import store.Store;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import org.bson.Document;
import org.junit.jupiter.api.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;


public class StoreTest {
    private static Scanner sc;
    private static final MongoClient mongoClient;
    private static MongoDatabase shopDatabase;
    public static Properties properties;
    private static MongoCollection<Document> productsTest;
    private static MongoCollection<Document> ticketsTest;
    private static Store store;

    static {
        /* Loading properties and instantiating new store */
        final String PROPERTIESPATH = "config.properties";
        properties = new Properties();
        try {
            properties.load(new FileInputStream(PROPERTIESPATH));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        /* Setting connection and getting collections */
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(properties.getProperty("connection")))
                .serverApi(serverApi)
                .build();
        mongoClient = MongoClients.create(settings);
        shopDatabase=mongoClient.getDatabase("shopTest");
        productsTest= shopDatabase.getCollection("productsTest");
        ticketsTest=shopDatabase.getCollection("ticketsTest");
        store = new Store();
        store.setProducts(productsTest);
        store.setTickets(ticketsTest);
        System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
    }

    @BeforeEach
    void insertElements() {
        sc = new Scanner(System.in);
        ticketsTest.drop();
        productsTest.drop();
        store.addProduct("tree", "Pine", 35.10, 25, sc);
        store.addProduct("tree", "Oak", 45.50, 30, sc);
        store.addProduct("flower", "Rose", 5.55, 50, sc);
        store.addProduct("flower", "Peonia", 8.52, 65, sc);
        store.addProduct("decoration", "Table", 159.99, 15, sc);
        store.addProduct("decoration", "Jar", 21.60, 80, sc);
    }

    @Test
    void productsAreAddedTest() {
        Assertions.assertEquals(6, store.getProducts().countDocuments());
    }
    @Test
    void productsAreRemovedTest() {
        store.removeProduct(1);
        Assertions.assertEquals(5, store.getProducts().countDocuments());
    }
    @Test
    void stockIsWorthTest() {
        Assertions.assertEquals(7201,(int)store.getStockValue());
    }
    @Test
    void ticketIsAddedTest() {
        List<Document> productsToBeAdded = new ArrayList<>();
        Document product = new Document();
        product.put("_id", 1);
        product.put("type", "tree");
        product.put("name", "Sequoia");
        product.put("price", 25.0);
        product.put("quantity", 5);
        productsToBeAdded.add(product);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        LocalDateTime now = LocalDateTime.now();
        String date = df.format(Timestamp.valueOf(now));
        store.generateTicket(date, productsToBeAdded);
        Assertions.assertEquals(1,ticketsTest.countDocuments());
    }

    @Test
    void ticketAmountTest(){
            List<Document> products = new ArrayList<>();
            Document product = new Document();
            product.put("_id", 1);
            product.put("type", "tree");
            product.put("name", "Sequoia");
            product.put("price", 25.0);
            product.put("quantity", 5);
            products.add(product);
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
            LocalDateTime now = LocalDateTime.now();
            String date = df.format(Timestamp.valueOf(now));
            store.generateTicket(date, products);
            FindIterable<Document> queryResults = ticketsTest.find();
            double ticketAmount=0;
            for(Document document:queryResults) {
                ticketAmount=document.getDouble("ticketAmount");
            }
            Assertions.assertEquals(125.0,ticketAmount);
    }
    @Test
    void purchasesAmountTest() {
        List<Document> products = new ArrayList<>();
        Document product1 = new Document();
        product1.put("_id", 1);
        product1.put("type", "tree");
        product1.put("name", "Sequoia");
        product1.put("price", 25.0);
        product1.put("quantity", 5);
        products.add(product1);
        Document product2 = new Document();
        product2.put("_id", 2);
        product2.put("type", "tree");
        product2.put("name", "Pine");
        product2.put("price", 20.0);
        product2.put("quantity", 5);
        products.add(product2);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        LocalDateTime now = LocalDateTime.now();
        String date = df.format(Timestamp.valueOf(now));
        store.generateTicket(date, products);
        FindIterable<Document> queryResults = ticketsTest.find();
        double purchasesAmount=0;
        for(Document document:queryResults) {
            purchasesAmount+=document.getDouble("ticketAmount");
        }
        Assertions.assertEquals(225,purchasesAmount);
    }
    @Test
    void stockAmountAfterRemovingTest() {
        store.removeProduct(1);
        Assertions.assertEquals((int)store.getStockValue(),6324);
    }
    @AfterAll
    static void closeResource() {
        mongoClient.close();
        sc.close();
    }

}