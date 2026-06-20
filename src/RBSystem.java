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
 * Escape Room Manager의 실행 시작점입니다.
 *
 * <p>발표 시에는 이 클래스에서 프로그램 실행 흐름을 설명하면 됩니다.
 * 1) Service 객체 생성 → 2) BookingResource 컨트롤러 연결 →
 * 3) 샘플 방탈출 테마 등록 → 4) GUI/CLI 실행 모드 선택 순서로 동작합니다.
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
        // 발표 시연용 기본 데이터입니다. 저장된 파일이 없어도 바로 테마 목록을 확인할 수 있습니다.
        final String branchName = "Escape Room Cafe";
        final String sampleAdminEmail = "manager@escaperoom.local";

        try {
            bookingResource.addAdmin(sampleAdminEmail, "Escape Room Manager");
        } catch (Exception ignored) {
            // Sample manager already exists.
        }

        try {
            bookingResource.addBuilding(branchName, "Sample branch for escape room themes", sampleAdminEmail);
        } catch (IllegalArgumentException ignored) {
            // Sample branch already exists.
        }

        addSampleTheme(bookingResource, branchName, "저택의 비밀", "공포", "HARD", 60, 2, 4, 28000);
        addSampleTheme(bookingResource, branchName, "연구소 탈출", "SF", "NORMAL", 60, 2, 5, 26000);
        addSampleTheme(bookingResource, branchName, "사라진 탐정", "추리", "EASY", 50, 2, 4, 24000);
    }

    private static void addSampleTheme(BookingResource bookingResource, String branchName, String themeName, String genre,
                                       String difficulty, int durationMinutes, int minPlayers, int maxPlayers, int pricePerPerson) {
        try {
            bookingResource.addRoom(branchName, themeName, false, genre, difficulty, durationMinutes, minPlayers, maxPlayers, pricePerPerson);
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
