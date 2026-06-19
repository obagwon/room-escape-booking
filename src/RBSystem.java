import controller.BookingResource;
import service.BuildingService;
import service.ReservationService;
import service.RoomService;
import service.UserService;
import view.CLI.CommandLine;
import view.GUI.Main_GUI;

import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;


/**
 * This is the main class of the entire Program.
 * Calling view. GUI AND COMMAND LINE - TWO UI VIEWS.
 *
 * @author 220031985
 */

public class RBSystem extends Thread {


    /**
     * Creating instances and calling the views.
     *
     * @param args optional mode: --gui for Swing only, --cli for command line only.
     */
    public static void main(String[] args) {

        // Instances of the Service Layer.
        UserService userService = new UserService();
        RoomService roomService = new RoomService();
        ReservationService reservationService = new ReservationService();
        BuildingService buildingService = new BuildingService();

        // Instance of the Controller Layer
        BookingResource bookingResource = new BookingResource(userService, roomService, reservationService, buildingService);
        loadSampleEscapeThemes(bookingResource);

        String mode = args.length > 0 ? args[0].trim().toLowerCase() : "--gui";

        if ("--cli".equals(mode) || GraphicsEnvironment.isHeadless()) {
            if (GraphicsEnvironment.isHeadless() && !"--cli".equals(mode)) {
                System.out.println("No graphical display was detected. Starting the command-line interface instead.");
            }
            startCommandLine(bookingResource);
            return;
        }

        if ("--gui".equals(mode)) {
            SwingUtilities.invokeLater(() -> new Main_GUI(bookingResource));
            return;
        }

        System.out.println("Unknown mode: " + args[0]);
        System.out.println("Usage: java -cp out RBSystem [--gui|--cli]");
    }

    private static void startCommandLine(BookingResource bookingResource) {
        System.out.println("\nCommand Line Interface is Called: ");
        CommandLine cmdLine = new CommandLine(bookingResource);
        cmdLine.commandLine();
    }

    private static void loadSampleEscapeThemes(BookingResource bookingResource) {
        final String branchName = "Escape Room Cafe";
        final String sampleAdminEmail = "manager@escaperoom.local";

        try {
            bookingResource.addUser(sampleAdminEmail, "Escape Room Manager");
        } catch (Exception ignored) {
            // Sample manager already exists.
        }

        try {
            bookingResource.addBuilding(branchName, "Sample branch for escape room themes", sampleAdminEmail);
        } catch (IllegalArgumentException ignored) {
            // Sample branch already exists.
        }

        addSampleTheme(bookingResource, branchName, "저택의 비밀 | 공포 | HARD | 60분 | 2~4명");
        addSampleTheme(bookingResource, branchName, "연구소 탈출 | SF | NORMAL | 60분 | 2~5명");
        addSampleTheme(bookingResource, branchName, "사라진 탐정 | 추리 | EASY | 50분 | 2~4명");
    }

    private static void addSampleTheme(BookingResource bookingResource, String branchName, String themeName) {
        try {
            bookingResource.addRoom(branchName, themeName, false);
        } catch (IllegalArgumentException ignored) {
            // Sample theme already exists.
        }
    }

    /**
     * Implementing Thread Function.
     */
    public void run() {
        System.out.println("This code is running in a thread");
    }
}
