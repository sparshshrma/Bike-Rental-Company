/**
 * Ass3 - Name: Jessica Stewart, Sparsh Sharma, Khushi Patel
 * Date: 24/04/2022
 * Program description: The program is designed to handle bike rentals.
 * The user is initially prompted to enter the bike data filename name, then the option number they want to select from rent, return, charge battery and exit.
 * After selecting the option, the user is prompted to enter a bike Id that they want to select.
 * If the bike is available, the user has to enter the email address..
 * 
 * @authoR-Sparsh sharma, jessica stewart, Khushi
 * @version (a version number or a date)
 */
// Standard import for the Scanner class
import java.util.*;
import java.io.*;

public class Ass3 {
    public static void main (String [] args) throws IOException {
        // Create a Scanner object attached to the keyboard
        Scanner in = new Scanner (System.in);
        // Write your code here!!!!
        ArrayList<Bike> myBikes = new ArrayList<>();
        Bike bike;

        System.out.println("*** Welcome to R&R E-Bike Rentals ***");
        System.out.println("");
        System.out.print("Enter bike data filename: ");
        String filename = in.nextLine();

        loadBikes (filename, myBikes);

        // loop
        while (true) {
            int option = showMenu(myBikes, in);

            switch (option) {
                case 1:
                    rentBike(myBikes, in);
                    break;

                case 2:
                    returnBike(myBikes, in);
                    break;

                case 4:
                    System.out.println("Good bye! ");
                    System.exit(0);
                    break;

                default:
                    break;
            }

            System.out.println();
            System.out.print("Press [Enter] to continue...");
            in.nextLine();
        }
    }

    //loadBikes
    public static void loadBikes(String filename, ArrayList<Bike> myBikes) throws IOException {
        File myFile = new File (filename);
        Scanner inFile = new Scanner (myFile);
        inFile.useDelimiter(",|\r\n");

        while (inFile.hasNextLine()) {

            String[] data = inFile.nextLine().split(",", -1);

            String bikeId = data[0];
            Double batteryCharge = Double.parseDouble(data[1]);
            Boolean inUse = Boolean.parseBoolean(data[2]);
            Long rentalStart = Long.parseLong(data[3]);
            String userName = data[4];
            Bike bike = new Bike(bikeId, batteryCharge, inUse, rentalStart, userName);

            //agli line hatani hai
            myBikes.add(bike);
        }
    }

    //saveBikes
    public static void saveBikes(String filename, ArrayList<Bike> myBikes) throws IOException {
        File file = new File(filename);
        PrintWriter writer = new PrintWriter(file);

        for (Bike bike : myBikes) {
            String line = String.format("%s,%.2f,%b,%d,%s", bike.getBikeId(), bike.getBatteryCharge(), bike.isInUse(),bike.getRentalStart(), bike.getUserName());
            writer.println(line);
        }

        writer.close();

        System.out.println("Good bye!");
    }

    //showMenu
    public static int showMenu(ArrayList<Bike> myBikes, Scanner in) {
        System.out.println();
        System.out.printf("%s%9s%15s\n","Bike ID","Battery","Rental Status");
        System.out.println("-------  -------  ----------------------------------------");

        for (Bike bike : myBikes) {
            System.out.println(bike.toString());
        }
        System.out.println();
        System.out.println("What would you like to do (1=Rent, 2=Return, 3=Charge Battery, 4=Exit)?");
        System.out.print("Enter your option number: ");

        int option = in.nextInt();
        in.nextLine();

        return option;
    }

    //rentBike
    public static void rentBike(ArrayList<Bike> myBikes, Scanner in) {

        System.out.println();
        System.out.print("Rent a bike. Enter bike ID: ");
        String id = in.nextLine();

        for (Bike bike : myBikes) {
            if (bike.getBikeId().equalsIgnoreCase(id)) {

                if (bike.isBatteryLow()) {
                    System.out.printf("Bike %s has a low battery and cannot be rented.", bike.getBikeId());
                    System.out.println();
                    break;
                }

                if (bike.isInUse()) {
                    System.out.printf("Bike %s is in use and cannot be rented.", bike.getBikeId());
                    System.out.println();
                }

                System.out.print("Enter customer name or email: ");
                String userName = in.nextLine();

                bike.rent(userName);
                System.out.printf("Bike %s rented to %s.", id, bike.getUserName());

            }

            else if (bike.getBikeId() == null) {
                System.out.printf("Sorry. Bike %s is not found.", id);
                System.out.println();

            }

        }

    }

    //returnBike
    public static void returnBike(ArrayList<Bike> myBikes, Scanner in) {

        System.out.println();
        System.out.print("Return a bike. Enter bike ID: ");
        String id = in.next();
        in.nextLine();

        for (Bike bike : myBikes) {
            if (bike.getBikeId().equalsIgnoreCase(id)) {

                if (!bike.isInUse()) {
                    System.out.printf("Bike %s does not need to be returned.", bike.getBikeId());
                    return;
                }

                double currentBattery = bike.getBatteryCharge();

                long minutesElapsed = bike.unrent() / (1000 * 60);
                double cost = 1.00 + minutesElapsed * 0.25;

                bike.setBatteryCharge(currentBattery - 0.002 * minutesElapsed);

                System.out.printf("Bike %s returned. Minutes used: %d Cost: $%.2f",bike.getBikeId(),minutesElapsed, cost);
            }
        }

        //chargeBike

        
    }
}