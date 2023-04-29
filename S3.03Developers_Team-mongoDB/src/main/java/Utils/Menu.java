package utils;
import store.Store;
import store.*;

import java.util.Scanner;

public class Menu {
    public static Scanner sc = new Scanner(System.in);

    public static void menu() {
        boolean continueExecution=true;
        int option;
        while(continueExecution) {
            menuHeader();
            option = Integer.parseInt(sc.nextLine());
            switch (option) {
                case 1: {
                    Store.getStore().addTree();
                    break;
                }
                case 2: {
                    Store.getStore().addFlower();
                    break;
                }
                case 3: {
                    Store.getStore().addDecoration();
                    break;
                }
                case 4: {
                    Store.getStore().removeTree();
                    break;
                }
                case 5: {
                    Store.getStore().removeFlower();
                    break;
                }
                case 6: {
                    Store.getStore().removeDecoration();
                    break;
                }
                case 7: {
                    Store.getStore().printingBriefStock();
                    break;
                }
                case 8: {
                    Store.getStore().printingFullStock();
                    break;
                }
                case 9: {
                    Store.getStore().printingStockValue();
                    break;
                }
                case 10: {
                    Ticket ticket = new Ticket();
                    ticket.generateTicket();
                    System.out.println(ticket);
                    break;
                }
                case 11: {
                    Store.store.printTickets();
                    break;
                }
                case 12: {
                    Store.getStore().printingPurchasesValue();
                    break;
                }
                case 0: {
                    continueExecution=false;
                    terminateProgram();
                }
            }
        }
    }
    public static void menuHeader() {
        System.out.println("Choose and option: ");
        System.out.println("1: Adding tree");
        System.out.println("2: Adding flower");
        System.out.println("3: Adding decoration");
        System.out.println("4: Remove tree");
        System.out.println("5: Remove flower");
        System.out.println("6: Remove decoration");
        System.out.println("7: Printing stock - brief");
        System.out.println("8: Printing stock - detailed");
        System.out.println("9: Printing total stock value");
        System.out.println("10: Generating purchase ticket");
        System.out.println("11: Listing purchase tickets");
        System.out.println("12: Printing purchases incoming");
        System.out.println("0: Program terminating");
    }
    public static void terminateProgram() {
        utils.updatingFiles(Store.store);
        sc.close();
    }
}
