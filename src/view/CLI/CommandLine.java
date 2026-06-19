package view.CLI;


import controller.BookingResource;
import model.Building.Building;
import model.Reservation.Reservation;
import model.Room.Room;
import model.User.User;
import view.ViewHandler;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * CommandLine Interface
 *
 * @author 220031985
 */
public class CommandLine implements Serializable, Runnable {

    private final BookingResource bookingResource;
    private Scanner scanner;

    private boolean continueCli;


    public CommandLine(BookingResource bookingResource) {
        this.bookingResource = bookingResource;
        this.continueCli = true;
    }

    public boolean isContinueCli() {
        return continueCli;
    }

    public void setContinueCli(boolean continueCli) {
        this.continueCli = continueCli;
    }

    /**
     * Main Method calling all other methods
     */
    public void commandLine() {
        String line;

        scanner = new Scanner(System.in);
        printMainMenu();
        try {
            do {
                line = scanner.nextLine();
                if (line.length() == 1 && continueCli) {
                    switch (line.charAt(0)) {
                        case '1' -> addDelUser();
                        case '2' -> addDelBuild();
                        case '3' -> addDelRoom();
                        case '4' -> addDelRes();
                        case '5' -> viewMyRes();
                        case '6' -> roomsFB();
                        case '7' -> viewResName();
                        case '8' -> saveData();
                        case '9' -> loadData();
                        case '0' -> exit();

                        default -> System.out.println("Unknown Action \n");
                    }
                } else {
                    System.out.println("Error: Invalid action\n");
                }
            } while (line.charAt(0) >= '3' || line.length() != 1);
        } catch (StringIndexOutOfBoundsException | IOException e) {
            System.out.println("Empty Input Received. Exiting Program...");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method that lets the user view by email_ID.
     */
    private void viewResName() {
        System.out.println("Enter your EMAIL - ID");
        Map<String, Reservation> viewResID;
        final Scanner scanner = new Scanner(System.in);
        String line;
        line = scanner.nextLine();
        String email = line.trim();

        try {
            if (email.isEmpty()) {
                throw new IllegalArgumentException("Text Field Cannot be Empty");
            }
            bookingResource.checkUser(email);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getLocalizedMessage());
            addDelUser();
        }
        try {
            viewResID = bookingResource.viewResName(email);
            for (Map.Entry<String, Reservation> entry : viewResID.entrySet()) {
                System.out.println("Booking ID: " + entry.getValue().getBookingID() + ", " + entry.getValue().getEmail() + " has a reservation in " + entry.getValue().getBuildingName() + " and has reserved the escape theme: " + entry.getValue().getRoom() + " @ " + entry.getValue().getCheckInDate() + ":" + entry.getValue().getCheckInTime() + ".The customer exits @ " + entry.getValue().getCheckOutDate() + ":" + entry.getValue().getCheckOutTime() + "." + "\n");
                commandLine();
            }
        } catch (IllegalArgumentException ex) {
            String error = ex.getLocalizedMessage();
            System.out.println(error);
        }
    }

    /**
     * Method that lets the user add,delete or view all users.
     */
    private void addDelUser() {
        final Scanner scanner = new Scanner(System.in);
        String line;
        System.out.println("""
                1.Do you want to add a Customer\s
                2.Do you want to delete a Customer?
                3.View All Customers\s
                4.Main Menu\s
                """);
        line = scanner.nextLine();
        if (line.length() == 1) {
            switch (line.charAt(0)) {
                case '1' -> {
                    System.out.print("Adding a Customer\n");
                    System.out.println("Enter Email format: name@domain.com");
                    line = scanner.nextLine();
                    final String email = line.trim();
                    System.out.println("Enter your Name:");
                    line = scanner.nextLine();
                    final String name = line.trim();
                    try {
                        bookingResource.addUser(email, name);
                        System.out.println("Customer added successfully \n");
                        commandLine();
                    } catch (IllegalArgumentException | IOException e) {
                        System.out.println(e.getLocalizedMessage());
                        addDelUser();
                    }
                }
                case '2' -> {
                    System.out.println("Deleting a Customer");
                    System.out.println("Enter the email");
                    line = scanner.nextLine();
                    final String del_email = line.trim();
                    try {
                        bookingResource.delUser(del_email);
                        System.out.println("Customer deleted successfully \n");
                        commandLine();
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getLocalizedMessage());
                        addDelUser();
                    }
                }
                case '3' -> {
                    System.out.print("Viewing All Customers\n");
                    try {
                        Map<String, User> viewUsers = bookingResource.viewUsers();
                        for (Map.Entry<String, User> entry : viewUsers.entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            System.out.println(value + "\n");
                        }
                        commandLine();
                    } catch (IllegalArgumentException ex) {
                        System.out.println(ex.getLocalizedMessage());
                        addDelUser();
                    }
                }
                case '4' -> commandLine();
                default -> System.out.println("Unknown action\n");
            }

        } else {
            System.out.println("Invalid Input. Try Again !");
            commandLine();
        }

    }

    /**
     * Method that lets the user add,delete or view all buildings.
     */
    //Bug Fix: Exemption handling in Delete Building (Addressed)
    private void addDelBuild() {
        final Scanner scanner = new Scanner(System.in);
        String line;
        System.out.println("""
                1.Do you want to add a Cafe Branch?\s
                2.Do you want to delete a Cafe Branch?
                3.View All Cafe Branches\s
                4.Main Menu\s
                """);
        line = scanner.nextLine();
        if (line.length() == 1) {
            switch (line.charAt(0)) {
                case '1' -> {
                    System.out.println("Enter customer email\n");
                    line = scanner.nextLine();
                    final String email = line.trim();
                    try {
                        bookingResource.checkUser(email);

                        System.out.print("Adding a Cafe Branch\n");
                        System.out.println("Enter Cafe Branch Name");
                        line = scanner.nextLine();
                        final String buildingName = line.trim();
                        System.out.println("Enter the address:");
                        line = scanner.nextLine();
                        final String address = line.trim();
                        try {
                            if (buildingName.isEmpty() | address.isEmpty()) {
                                throw new IllegalArgumentException("Inputs given are empty.Try Again.");
                            }
                            bookingResource.addBuilding(buildingName, address, email);
                            System.out.println("Cafe branch added successfully \n");
                            commandLine();
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getLocalizedMessage());
                            addDelBuild();
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getLocalizedMessage());
                        addDelUser();
                    }
                }
                case '2' -> {
                    System.out.println("Enter customer email\n");
                    line = scanner.nextLine();
                    final String email = line.trim();
                    try {
                        if (bookingResource.checkUser(email)) {
                            System.out.print("Deleting a Cafe Branch\n");
                            System.out.println("Enter Cafe Branch Name");
                            line = scanner.nextLine();
                            final String buildingName = line.trim();
                            try {
                                bookingResource.delBuilding(buildingName);
                                try {
                                    bookingResource.delRoomFromBuild(buildingName);
                                } catch (IllegalArgumentException e) {
                                    System.out.println(e.getLocalizedMessage());
                                    addDelBuild();
                                }
                                System.out.println("Cafe branch deleted successfully \n");
                                commandLine();
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getLocalizedMessage());
                                addDelBuild();
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getLocalizedMessage());
                        addDelUser();
                    }
                }
                case '3' -> {
                    System.out.print("Viewing All Cafe Branches\n");
                    try {
                        Map<String, Building> viewBuildings = bookingResource.viewBuildings();
                        for (Map.Entry<String, Building> entry : viewBuildings.entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue().getBuildingName();
                            Object address = entry.getValue().getAddress();
                            System.out.println("Cafe Branches :\n" + value + " and the address is " + address + "." + "\n");
                        }
                        commandLine();
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getLocalizedMessage());
                        addDelBuild();
                    }
                }
                case '4' -> commandLine();
                default -> System.out.println("Unknown action\n");
            }

        }

    }

    /**
     * Method that lets the user add,delete or view rooms in the buildings.
     */
    //Test Delete Room Method (Addressed)
    private void addDelRoom() {
        final Scanner scanner = new Scanner(System.in);
        String line;
        System.out.println("""
                1.Do you want to add an escape theme?\s
                2.Do you want to delete an escape theme?
                3.View All Escape Themes in Cafe Branches\s
                4.Main Menu\s
                """);
        line = scanner.nextLine();
        if (line.length() == 1) {
            switch (line.charAt(0)) {
                case '1' -> {
                    System.out.println("Enter customer email\n");
                    line = scanner.nextLine();
                    final String email = line.trim();
                    try {
                        if (bookingResource.checkUser(email)) {
                            System.out.print("Adding an Escape Theme\n");
                            System.out.println("\nEnter Cafe Branch Name to add the theme");
                            line = scanner.nextLine();
                            final String buildingName = line.trim();
                            try {
                                bookingResource.checkBuilding(buildingName);
                                System.out.println("Enter Escape Theme Name (example: 저택의 비밀 | 공포 | HARD | 60분 | 2~4명)");
                                line = scanner.nextLine();
                                String roomName = line.trim();
                                boolean isBooked = false;
                                if (buildingName.isEmpty() | roomName.isEmpty()) {
                                    throw new IllegalArgumentException("Empty Inputs given,Try Again.");
                                }
                                bookingResource.addRoom(buildingName, roomName, isBooked);
                                System.out.println("Escape theme added successfully \n");
                                commandLine();
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getLocalizedMessage());
                                addDelBuild();
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getLocalizedMessage());
                        addDelUser();
                    }
                }
                case '2' -> {
                    System.out.println("Enter customer email\n");
                    line = scanner.nextLine();
                    final String email = line.trim();
                    try {
                        if (bookingResource.checkUser(email)) {
                            System.out.print("Deleting an Escape Theme\n");
                            System.out.println("Enter Cafe Branch Name");
                            line = scanner.nextLine();
                            final String buildingName = line.trim();
                            try {
                                if (bookingResource.checkBuilding(buildingName)) {
                                    try {
                                        System.out.println("\nEnter the escape theme you wish to delete:");
                                        line = scanner.nextLine();
                                        final String roomName = line.trim();
                                        if (bookingResource.checkRoom(roomName)) {
                                            bookingResource.delRoom(buildingName, roomName);
                                            System.out.println("\n Escape theme deleted successfully");
                                        }
                                    } catch (IllegalArgumentException e) {
                                        System.out.println(e.getLocalizedMessage());
                                        addDelRoom();
                                    }
                                }
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getLocalizedMessage());
                                addDelBuild();
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getLocalizedMessage());
                        addDelUser();
                    }
                }

                case '3' -> {
                    System.out.println("Viewing all cafe branches with escape themes");
                    // Bug Fix: Building is not created, but it is passing (Overpassed and Addressed)
                    // System.out.println("Room Present in " + buildingName + " :\n");
                    try {
                        Map<String, Room> viewRooms = (Map<String, Room>) bookingResource.viewRooms();
                        for (Map.Entry<String, Room> entry : viewRooms.entrySet()) {
                            String key = entry.getKey();
                            Object buildingName = entry.getValue().getBuildingName();
                            Object roomName = entry.getValue().getRoomName();
                            System.out.println("Escape Themes :\n" + buildingName + " offers " + roomName + "\n");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                    commandLine();
                }
                case '4' -> commandLine();

                default -> System.out.println("Unknown Action \n");
            }
        }
    }

    /**
     * Method that allows the user to make or delete a reservation.
     */
    private void addDelRes() {
        final Scanner scanner = new Scanner(System.in);
        String line;
        System.out.println("""
                1.Book an Escape Theme\s
                2.Delete a Booking\s
                3.Main Menu\s
                """);
        line = scanner.nextLine();
        if (line.length() == 1) {
            switch (line.charAt(0)) {
                case '1' -> {
                    System.out.println("\nBooking an Escape Theme");
                    System.out.println("Enter Booking ID, it can be name,number,symbols.");
                    line = scanner.nextLine();
                    final String bookingID = line.trim();
                    try {
                        bookingResource.checkID(bookingID);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getLocalizedMessage());
                        addDelRes();
                    }
                    System.out.println("Enter customer email\n");
                    line = scanner.nextLine();
                    final String email = line.trim();
                    try {
                        bookingResource.checkUser(email);
                        System.out.println("Enter the Cafe Branch");
                        line = scanner.nextLine();
                        final String buildingName = line.trim();
                        try {
                            bookingResource.checkBuilding(buildingName);
                            System.out.println("Enter the Escape Theme");
                            line = scanner.nextLine();
                            final String roomName = line.trim();
                            try {
                                bookingResource.checkRoom(roomName); // To check if a Room exists or not.
                                System.out.println("Enter BOOK-IN Date dd/mm/yyyy example 02/12/2022");
                                line = scanner.nextLine();
                                String checkInDate = line.trim();
                                try {
                                    bookingResource.isDateValid(checkInDate);
                                } catch (IllegalArgumentException e) {
                                    System.out.println(e.getLocalizedMessage());
                                    addDelRes();
                                }
//
                                System.out.println("Enter BOOK-OUT Date dd/mm/yyyy example 03/12/2022");
                                line = scanner.nextLine();
                                String checkOutDate = line.trim();
                                try {
                                    bookingResource.isDateValid(checkOutDate);
                                } catch (IllegalArgumentException e) {
                                    System.out.println(e.getLocalizedMessage());
                                    addDelRes();
                                }

                                System.out.println("Enter the IN Time HH:mm in 24-hr format");
                                line = scanner.nextLine();
                                String checkInTime = line.trim();
                                try {
                                    bookingResource.validateTime(checkInTime);
                                } catch (IllegalArgumentException e) {
                                    System.out.println(e.getLocalizedMessage());
                                    addDelRes();
                                }
                                System.out.println("Enter the OUT Time HH:mm in 24-hr format");
                                line = scanner.nextLine();
                                String checkOutTime = line.trim();
                                try {
                                    bookingResource.validateTime(checkOutTime);
                                } catch (IllegalArgumentException e) {
                                    System.out.println(e.getLocalizedMessage());
                                    addDelRes();
                                }
                                try {
                                    bookingResource.isBeforeTime(checkInTime, checkOutTime);
                                } catch (IllegalArgumentException e) {
                                    System.out.println(e.getLocalizedMessage());
                                    addDelRes();
                                }
                                try {
                                    bookingResource.LeastBy5(checkInTime, checkOutTime);
                                } catch (IllegalArgumentException e) {
                                    System.out.println(e.getLocalizedMessage());
                                    addDelRes();
                                }
                                try {
                                    bookingResource.checkDay(checkInDate, checkOutDate);
                                } catch (IllegalArgumentException e) {
                                    System.out.println(e.getLocalizedMessage());
                                    addDelRes();
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                                try {
                                    bookingResource.checkOverlap(checkInTime, checkOutTime, roomName, checkInDate);
                                } catch (IllegalArgumentException e) {
                                    System.out.println(e.getLocalizedMessage());
                                    addDelRes();
                                }
                                try {
                                    boolean isBooked = true;
                                    bookingResource.updateRoom(roomName, buildingName, isBooked);
                                    bookingResource.addReservation(bookingID, email, buildingName, roomName, checkInDate, checkOutDate, checkInTime, checkOutTime, isBooked);
                                    System.out.println("Booking is Done");
                                    commandLine();
                                } catch (IllegalArgumentException e) {
                                    System.out.println(e.getLocalizedMessage());
                                    addDelRes();
                                }
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getLocalizedMessage());
                                addDelRoom();
                            }

                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getLocalizedMessage());
                            addDelBuild();
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getLocalizedMessage());
                        addDelUser();
                    }
                }
                case '2' -> {
                    System.out.println("Deleting a Booking");
                    System.out.println("Enter your Booking ID, it can be name,number,symbols.");
                    line = scanner.nextLine();
                    final String bookingID = line.trim();
                    try {
                        bookingResource.delReservation(bookingID);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getLocalizedMessage());
                        addDelRes();
                    }
                    System.out.println("Booking Deleted Successfully");
                    commandLine();
                }
                case '3' -> commandLine();
                default -> System.out.println("Unknown Action \n");
            }
        }
    }

    /**
     * Method that allows the user to view their reservations using Booking-ID.
     */
    private void viewMyRes() {
        System.out.println("Enter your Booking - ID");
        Map<String, Reservation> viewBookingIDRes = new HashMap<>();
        final Scanner scanner = new Scanner(System.in);
        String line;
        line = scanner.nextLine();
        String bookingID = line.trim();
        try {
            viewBookingIDRes = bookingResource.viewMyRes(bookingID);
            for (Map.Entry<String, Reservation> entry : viewBookingIDRes.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                System.out.println("Booking ID: " + entry.getValue().getBookingID() + ", " + entry.getValue().getEmail() + " has a reservation in " + entry.getValue().getBuildingName() + " and has reserved the escape theme: " + entry.getValue().getRoom() + " @ " + entry.getValue().getCheckInDate() + ":" + entry.getValue().getCheckInTime() + ".The customer exits @ " + entry.getValue().getCheckOutDate() + ":" + entry.getValue().getCheckOutTime() + "." + "\n");


            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());
            addDelRes();
        }
        System.out.println("Booking ID shown Successfully.");
        commandLine();
    }


    private void roomsFB() {
        System.out.println("Viewing All Reserved Escape Themes");
        try {
            Map<String, Reservation> allRooms = bookingResource.bookedRooms();
            for (Map.Entry<String, Reservation> entry : allRooms.entrySet()) {
                System.out.println("Reserved Escape Themes :\n" + "Booking Reference: " + entry.getValue().getBookingID() + "\n" + entry.getValue().getRoom() + " is reserved by " + entry.getValue().getEmail() + " from " + entry.getValue().getCheckInDate() + ":" + entry.getValue().getCheckInTime() + " to " + entry.getValue().getCheckOutDate() + ":" + entry.getValue().getCheckOutTime());
            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        commandLine();

    }

    private void saveData() throws IOException {
        bookingResource.userSave();
        bookingResource.buildSave();
        bookingResource.roomSave();
        bookingResource.resSave();
        bookingResource.playResultSave();
        System.out.print("File Saved Successfully\n");
        commandLine();
    }

    private void loadData() throws IOException, ClassNotFoundException {
        bookingResource.userLoad();
        bookingResource.buildLoad();
        bookingResource.roomLoad();
        bookingResource.resLoad();
        bookingResource.playResultLoad();
        System.out.print("File Loaded Successfully\n");
        commandLine();
    }


    private void exit() {
        System.out.println("Exit");
        this.setContinueCli(false);
        if (ViewHandler.isGUIView) {
            System.exit(0);
        } else {
            ViewHandler.isCommandView = true;
        }

    }


    public void printMainMenu() {
        String Welcome = "Welcome to Escape Room Manager - 방탈출 카페 예약 및 힌트 관리 프로그램 ";
        String End = "*--------------Choose the options above to continue--------------*";
        for (int i = 0; i < Welcome.length(); i++) {
            System.out.print("-");
        }
        String Options = """
                1.Add/Delete/View Customers.
                2.Add/Delete/View Cafe Branches.
                3.Add/Delete/View Escape Themes.
                4.Book or Delete an Escape Theme Reservation.
                5.View Your Reservations by Booking ID.
                6.View All Reserved Escape Themes.
                7.View Your Reservations by Customer Email.
                8.Save the Data.
                9.Load the Data.
                0.Exit.
                """;
        System.out.print("\n" + Welcome + "\n");
        for (int i = 0; i < Welcome.length(); i++) {
            System.out.print("-");
        }
        System.out.print("\n" + Options + "\n");
        System.out.print(End + "\n");
    }

    /**
     *
     */
    @Override
    public void run() {

    }
}
