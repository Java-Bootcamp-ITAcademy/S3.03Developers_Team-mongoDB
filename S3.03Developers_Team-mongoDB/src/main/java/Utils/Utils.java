package utils;

import store.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import Products.*;

import java.io.*;
import java.util.*;

public class Utils {
    /* properties file contains paths to products and ticket files */
    private static final String PROPERTIESPATH="config.properties";
    private static Properties properties=new Properties();

    static {
        try {
            properties.load(new FileInputStream(PROPERTIESPATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Properties getProperties() {
        return properties;
    }

    public static void setProperties(Properties properties) {
        Utils.properties = properties;
    }
    /* Find a product by id in a set */
    public static Product findProduct(Set<? extends Product> set, int id) {
        Product product=null;
        boolean productNotfound=true;
        int i=0;
        for(Product productAux:set) {
            if(productAux.getId()==id) {
                product = productAux;
                break;
            }
        }
        return product;
    }
    /* Updates product quantity in a set */
    public static void updateProductQuantity(Set<? extends Product> set, int id, int quantityToRemove) {
        Product product = findProduct(set, id);
        product.setQuantity(product.getQuantity()-quantityToRemove);
    }
    /* Saves decorations set to a file */
    public static void fromDecorationSetToFile(Set<Decoration> set) {
        try {
            if(!set.isEmpty()) {
                PrintWriter writer = new PrintWriter(properties.getProperty("decorations"));
                writer.print("");
                writer.close();
                ObjectMapper objectMapper = new ObjectMapper();
                String setToString = objectMapper.writeValueAsString(set);
                PrintWriter printWriter;
                printWriter = new PrintWriter(properties.getProperty("decorations"));
                printWriter.write(setToString);
                printWriter.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /* Saves trees set to a file */
    public static void fromTreeSetToFile(Set<Tree> set) {
        try {
            if(!set.isEmpty()) {
                PrintWriter writer = new PrintWriter(properties.getProperty("trees"));
                writer.print("");
                writer.close();
                ObjectMapper objectMapper = new ObjectMapper();
                String setToString = objectMapper.writeValueAsString(set);
                PrintWriter printWriter;
                printWriter = new PrintWriter(properties.getProperty("trees"));
                printWriter.write(setToString);
                printWriter.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /* Saves flowers set to a file */
    public static void fromFlowerSetToFile(Set<Flower> set) {
        try {
            if(!set.isEmpty()) {
                PrintWriter writer = new PrintWriter(properties.getProperty("flowers"));
                writer.print("");
                writer.close();
                ObjectMapper objectMapper = new ObjectMapper();
                String setToString = objectMapper.writeValueAsString(set);
                PrintWriter printWriter;
                printWriter = new PrintWriter(properties.getProperty("flowers"));
                printWriter.write(setToString);
                printWriter.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /* Saves tickets list to a file */
    public static void fromTicketsListToFile(List<Ticket> list) {
        try {
            if(!list.isEmpty()) {
                PrintWriter writer = new PrintWriter(properties.getProperty("tickets"));
                writer.print("");
                writer.close();
                ObjectMapper objectMapper = new ObjectMapper();
                String setToString = objectMapper.writeValueAsString(list);
                PrintWriter printWriter;
                printWriter = new PrintWriter(properties.getProperty("tickets"));
                printWriter.write(setToString);
                printWriter.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /* loads decorations from file to set */
    public static HashSet<Decoration> fromFileProductToDecorationSet() {
        try {
            HashSet<Decoration> decorations = new HashSet<>();
            File file = new File(properties.getProperty("decorations"));
            if(file.length()!=0) {
                ObjectMapper mapper = new ObjectMapper();
                decorations = mapper.readValue(file, new TypeReference<>() {
                });
            }
            return decorations;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /* loads flowers from file to set */
    public static HashSet<Flower> fromFileProductToFlowerSet() {
        try {
            HashSet<Flower> flowers = new HashSet<>();
            File file=new File(properties.getProperty("flowers"));
            if(file.length()!=0) {
                ObjectMapper mapper = new ObjectMapper();
                flowers = mapper.readValue(file, new TypeReference<>() {
                });
            }
            return flowers;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /* loads trees from file to set */
    public static HashSet<Tree> fromFileProductToTreeSet() {
        try {
            HashSet<Tree> trees = new HashSet<>();
            File file = new File(properties.getProperty("trees"));
            if(file.length()!=0) {
                ObjectMapper mapper = new ObjectMapper();
                trees = mapper.readValue(file, new TypeReference<>() {
                });
            }
            return trees;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /* loads tickets from file to list */
    public static ArrayList<Ticket> fromFileToTicketList() {
        try {
            ArrayList<Ticket> tickets = new ArrayList<>();
            File file = new File("tickets.json");
            if(file.length()!=0) {
                ObjectMapper mapper = new ObjectMapper();
                tickets = mapper.readValue(file, new TypeReference<>() {
                });
            }
            return tickets;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /* Prints a given set */
    public static void printingSet(Set<? extends Product> set) {
        for(Product product:set) {
            System.out.println(product);
        }
    }
    /* Updates all files when necessary */
    public static void updatingFiles(Store store) {
        fromTreeSetToFile(store.getTrees());
        fromFlowerSetToFile(store.getFlowers());
        fromDecorationSetToFile(store.getDecorations());
        fromTicketsListToFile(Store.store.getTickets());
    }

}
