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

                        default -> System.out.println("알 수 없는 메뉴입니다.\n");
                    }
                } else {
                    System.out.println("오류: 잘못된 메뉴입니다.\n");
                }
            } while (line.charAt(0) >= '3' || line.length() != 1);
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("입력이 없어 프로그램을 종료합니다.");
        }
    }

    /**
     * Method that lets the user view by email_ID.
     */
    private void viewResName() {
        System.out.println("고객 이메일을 입력하세요.");
        Map<String, Reservation> viewResID;
        final Scanner scanner = new Scanner(System.in);
        String line;
        line = scanner.nextLine();
        String email = line.trim();

        try {
            if (email.isEmpty()) {
                throw new IllegalArgumentException("입력값을 작성해 주세요.");
            }
            bookingResource.checkUser(email);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getLocalizedMessage());
            addDelUser();
        }
        try {
            viewResID = bookingResource.viewResName(email);
            for (Map.Entry<String, Reservation> entry : viewResID.entrySet()) {
                System.out.println("예약 번호: " + entry.getValue().getBookingID() + "\n고객 이메일: " + entry.getValue().getEmail() + "\n지점명: " + entry.getValue().getBuildingName() + "\n테마명: " + entry.getValue().getRoom() + "\n시작 시간: " + entry.getValue().getCheckInDate() + ":" + entry.getValue().getCheckInTime() + "\n종료 시간: " + entry.getValue().getCheckOutDate() + ":" + entry.getValue().getCheckOutTime() + "\n예약 인원: " + entry.getValue().getPlayerCount() + "명\n총 가격: " + entry.getValue().getTotalPrice() + "원\n예약 상태: " + entry.getValue().getStatusDisplayName() + "\n");
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
                1. 고객 추가
                2. 고객 삭제
                3. 전체 고객 조회
                4. 메인 메뉴
                """);
        line = scanner.nextLine();
        if (line.length() == 1) {
            switch (line.charAt(0)) {
                case '1' -> {
                    System.out.print("고객을 추가합니다.\n");
                    System.out.println("이메일을 입력하세요. 예: name@domain.com");
                    line = scanner.nextLine();
                    final String email = line.trim();
                    System.out.println("이름을 입력하세요:");
                    line = scanner.nextLine();
                    final String name = line.trim();
                    try {
                        bookingResource.addUser(email, name);
                        System.out.println("고객이 등록되었습니다.\n");
                        commandLine();
                    } catch (IllegalArgumentException | IOException e) {
                        System.out.println(e.getLocalizedMessage());
                        addDelUser();
                    }
                }
                case '2' -> {
                    System.out.println("고객을 삭제합니다.");
                    System.out.println("고객 이메일을 입력하세요.");
                    line = scanner.nextLine();
                    final String del_email = line.trim();
                    try {
                        bookingResource.delUser(del_email);
                        System.out.println("고객이 삭제되었습니다.\n");
                        commandLine();
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getLocalizedMessage());
                        addDelUser();
                    }
                }
                case '3' -> {
                    System.out.print("전체 고객을 조회합니다.\n");
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
                default -> System.out.println("알 수 없는 메뉴입니다.\n");
            }

        } else {
            System.out.println("잘못된 입력입니다. 다시 시도해 주세요.");
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
                1. 지점 추가
                2. 지점 삭제
                3. 전체 지점 조회
                4. 메인 메뉴
                """);
        line = scanner.nextLine();
        if (line.length() == 1) {
            switch (line.charAt(0)) {
                case '1' -> {
                    try {
                        requireAdminEmail(scanner);

                        System.out.print("지점을 추가합니다.\n");
                        System.out.println("지점명을 입력하세요.");
                        line = scanner.nextLine();
                        final String buildingName = line.trim();
                        System.out.println("주소를 입력하세요:");
                        line = scanner.nextLine();
                        final String address = line.trim();
                        try {
                            if (buildingName.isEmpty() | address.isEmpty()) {
                                throw new IllegalArgumentException("입력값이 비어 있습니다. 다시 시도해 주세요.");
                            }
                            bookingResource.addBuilding(buildingName, address, getCurrentAdminEmail());
                            System.out.println("지점이 등록되었습니다.\n");
                            commandLine();
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getLocalizedMessage());
                            addDelBuild();
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getLocalizedMessage());
                        addDelBuild();
                    }
                }
                case '2' -> {
                    try {
                        requireAdminEmail(scanner);
                        System.out.print("지점을 삭제합니다.\n");
                            System.out.println("지점명을 입력하세요.");
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
                                System.out.println("지점이 삭제되었습니다.\n");
                                commandLine();
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getLocalizedMessage());
                                addDelBuild();
                            }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getLocalizedMessage());
                        addDelBuild();
                    }
                }
                case '3' -> {
                    System.out.print("전체 지점을 조회합니다.\n");
                    try {
                        Map<String, Building> viewBuildings = bookingResource.viewBuildings();
                        for (Map.Entry<String, Building> entry : viewBuildings.entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue().getBuildingName();
                            Object address = entry.getValue().getAddress();
                            System.out.println("지점명: " + value + "\n주소: " + address + "\n");
                        }
                        commandLine();
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getLocalizedMessage());
                        addDelBuild();
                    }
                }
                case '4' -> commandLine();
                default -> System.out.println("알 수 없는 메뉴입니다.\n");
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
                1. 방탈출 테마 추가
                2. 방탈출 테마 삭제
                3. 지점별 방탈출 테마 조회
                4. 메인 메뉴
                """);
        line = scanner.nextLine();
        if (line.length() == 1) {
            switch (line.charAt(0)) {
                case '1' -> {
                    try {
                        requireAdminEmail(scanner);
                        System.out.print("방탈출 테마를 추가합니다.\n");
                        System.out.println("\n테마를 추가할 지점명을 입력하세요.");
                        line = scanner.nextLine();
                        final String buildingName = line.trim();
                        bookingResource.checkBuilding(buildingName);
                        System.out.println("방탈출 테마명을 입력하세요.");
                        line = scanner.nextLine();
                        String roomName = line.trim();
                        System.out.println("장르를 입력하세요. 예: 공포, SF, 추리");
                        line = scanner.nextLine();
                        String genre = line.trim();
                        System.out.println("난이도를 입력하세요. EASY, NORMAL, HARD 중 하나를 권장합니다.");
                        line = scanner.nextLine();
                        String difficulty = line.trim();
                        System.out.println("플레이 시간(분)을 입력하세요.");
                        line = scanner.nextLine();
                        int durationMinutes = parseCliInt(line.trim(), "플레이 시간");
                        System.out.println("최소 인원을 입력하세요.");
                        line = scanner.nextLine();
                        int minPlayers = parseCliInt(line.trim(), "최소 인원");
                        System.out.println("최대 인원을 입력하세요.");
                        line = scanner.nextLine();
                        int maxPlayers = parseCliInt(line.trim(), "최대 인원");
                        System.out.println("1인 가격을 입력하세요.");
                        line = scanner.nextLine();
                        int pricePerPerson = parseCliInt(line.trim(), "1인 가격");
                        boolean isBooked = false;
                        if (buildingName.isEmpty() | roomName.isEmpty() | genre.isEmpty() | difficulty.isEmpty()) {
                            throw new IllegalArgumentException("입력값이 비어 있습니다. 다시 시도해 주세요.");
                        }
                        bookingResource.addRoom(buildingName, roomName, isBooked, genre, difficulty, durationMinutes, minPlayers, maxPlayers, pricePerPerson);
                        System.out.println("방탈출 테마가 등록되었습니다.\n");
                        commandLine();
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getLocalizedMessage());
                        addDelRoom();
                    }
                }
                case '2' -> {
                    try {
                        requireAdminEmail(scanner);
                        System.out.print("방탈출 테마를 삭제합니다.\n");
                        System.out.println("지점명을 입력하세요.");
                        line = scanner.nextLine();
                        final String buildingName = line.trim();
                        bookingResource.checkBuilding(buildingName);
                        System.out.println("\n삭제할 방탈출 테마명을 입력하세요:");
                        line = scanner.nextLine();
                        final String roomName = line.trim();
                        bookingResource.checkRoom(roomName);
                        bookingResource.delRoom(buildingName, roomName);
                        System.out.println("\n방탈출 테마가 삭제되었습니다.");
                        commandLine();
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getLocalizedMessage());
                        addDelRoom();
                    }
                }
                case '3' -> {
                    System.out.println("지점별 방탈출 테마를 조회합니다.");
                    try {
                        Map<String, Room> viewRooms = (Map<String, Room>) bookingResource.viewRooms();
                        for (Map.Entry<String, Room> entry : viewRooms.entrySet()) {
                            String key = entry.getKey();
                            System.out.println(entry.getValue().toDisplayString() + "\n");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                    commandLine();
                }
                case '4' -> commandLine();

                default -> System.out.println("알 수 없는 메뉴입니다.\n");
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
                1. 테마 예약
                2. 예약 취소/삭제
                3. 메인 메뉴
                """);
        line = scanner.nextLine();
        if (line.length() == 1) {
            switch (line.charAt(0)) {
                case '1' -> {
                    System.out.println("\n테마 예약을 진행합니다.");
                    System.out.println("예약 번호를 입력하세요. 이름, 숫자, 기호를 사용할 수 있습니다.");
                    line = scanner.nextLine();
                    final String bookingID = line.trim();
                    try {
                        bookingResource.checkID(bookingID);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getLocalizedMessage());
                        addDelRes();
                    }
                    System.out.println("고객 이메일을 입력하세요.\n");
                    line = scanner.nextLine();
                    final String email = line.trim();
                    try {
                        bookingResource.checkUser(email);
                        System.out.println("지점명을 입력하세요.");
                        line = scanner.nextLine();
                        final String buildingName = line.trim();
                        try {
                            bookingResource.checkBuilding(buildingName);
                            System.out.println("방탈출 테마명을 입력하세요.");
                            line = scanner.nextLine();
                            final String roomName = line.trim();
                            try {
                                bookingResource.checkRoom(roomName); // To check if a Room exists or not.
                                System.out.println("예약 시작 날짜를 입력하세요. 예: 02/12/2022");
                                line = scanner.nextLine();
                                String checkInDate = line.trim();
                                try {
                                    bookingResource.isDateValid(checkInDate);
                                } catch (IllegalArgumentException e) {
                                    System.out.println(e.getLocalizedMessage());
                                    addDelRes();
                                }
//
                                System.out.println("예약 종료 날짜를 입력하세요. 예: 03/12/2022");
                                line = scanner.nextLine();
                                String checkOutDate = line.trim();
                                try {
                                    bookingResource.isDateValid(checkOutDate);
                                } catch (IllegalArgumentException e) {
                                    System.out.println(e.getLocalizedMessage());
                                    addDelRes();
                                }

                                System.out.println("시작 시간을 24시간 형식(HH:mm)으로 입력하세요.");
                                line = scanner.nextLine();
                                String checkInTime = line.trim();
                                try {
                                    bookingResource.validateTime(checkInTime);
                                } catch (IllegalArgumentException e) {
                                    System.out.println(e.getLocalizedMessage());
                                    addDelRes();
                                }
                                System.out.println("종료 시간을 24시간 형식(HH:mm)으로 입력하세요.");
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
                                System.out.println("예약 인원을 입력하세요.");
                                line = scanner.nextLine();
                                int playerCount = parseCliInt(line.trim(), "예약 인원");
                                try {
                                    bookingResource.checkOverlap(checkInTime, checkOutTime, roomName, checkInDate);
                                } catch (IllegalArgumentException e) {
                                    System.out.println(e.getLocalizedMessage());
                                    addDelRes();
                                }
                                try {
                                    boolean isBooked = true;
                                    bookingResource.addReservation(bookingID, email, buildingName, roomName, checkInDate, checkOutDate, checkInTime, checkOutTime, isBooked, playerCount);
                                    bookingResource.updateRoom(roomName, buildingName, isBooked);
                                    System.out.println("예약이 완료되었습니다.");
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
                    System.out.println("예약을 취소합니다.");
                    System.out.println("예약 번호를 입력하세요. 이름, 숫자, 기호를 사용할 수 있습니다.");
                    line = scanner.nextLine();
                    final String bookingID = line.trim();
                    try {
                        bookingResource.cancelReservation(bookingID);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getLocalizedMessage());
                        addDelRes();
                    }
                    System.out.println("예약이 취소되었습니다.");
                    commandLine();
                }
                case '3' -> commandLine();
                default -> System.out.println("알 수 없는 메뉴입니다.\n");
            }
        }
    }

    /**
     * Method that allows the user to view their reservations using Booking-ID.
     */
    private void viewMyRes() {
        System.out.println("예약 번호를 입력하세요.");
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
                System.out.println("예약 번호: " + entry.getValue().getBookingID() + "\n고객 이메일: " + entry.getValue().getEmail() + "\n지점명: " + entry.getValue().getBuildingName() + "\n테마명: " + entry.getValue().getRoom() + "\n시작 시간: " + entry.getValue().getCheckInDate() + ":" + entry.getValue().getCheckInTime() + "\n종료 시간: " + entry.getValue().getCheckOutDate() + ":" + entry.getValue().getCheckOutTime() + "\n예약 인원: " + entry.getValue().getPlayerCount() + "명\n총 가격: " + entry.getValue().getTotalPrice() + "원\n예약 상태: " + entry.getValue().getStatusDisplayName() + "\n");


            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());
            addDelRes();
        }
        System.out.println("예약 조회가 완료되었습니다.");
        commandLine();
    }


    private void roomsFB() {
        System.out.println("전체 예약 현황을 조회합니다.");
        try {
            requireAdminEmail(new Scanner(System.in));
            Map<String, Reservation> allRooms = bookingResource.bookedRooms();
            for (Map.Entry<String, Reservation> entry : allRooms.entrySet()) {
                System.out.println("예약된 테마 :\n" + "예약 번호: " + entry.getValue().getBookingID() + "\n테마명: " + entry.getValue().getRoom() + "\n고객 이메일: " + entry.getValue().getEmail() + "\n예약 인원: " + entry.getValue().getPlayerCount() + "명\n총 가격: " + entry.getValue().getTotalPrice() + "원\n예약 상태: " + entry.getValue().getStatusDisplayName() + "\n이용 시간: " + entry.getValue().getCheckInDate() + ":" + entry.getValue().getCheckInTime() + " ~ " + entry.getValue().getCheckOutDate() + ":" + entry.getValue().getCheckOutTime());
            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        commandLine();

    }

    private void saveData() {
        try {
            System.out.println(bookingResource.saveAllData());
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        commandLine();
    }

    private void loadData() {
        try {
            System.out.println(bookingResource.loadAllData());
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        commandLine();
    }


    private String currentAdminEmail;

    private void requireAdminEmail(Scanner scanner) {
        System.out.println("관리자 이메일을 입력하세요.");
        String adminEmail = scanner.nextLine().trim();
        bookingResource.requireAdmin(adminEmail);
        currentAdminEmail = adminEmail;
    }

    private String getCurrentAdminEmail() {
        return currentAdminEmail == null ? "" : currentAdminEmail;
    }

    private int parseCliInt(String value, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + "은(는) 숫자로 입력해주세요.");
        }
    }


    private void exit() {
        System.out.println("종료합니다.");
        this.setContinueCli(false);
        if (ViewHandler.isGUIView) {
            System.exit(0);
        } else {
            ViewHandler.isCommandView = true;
        }

    }


    public void printMainMenu() {
        String Welcome = "방탈출 예약 관리 시스템에 오신 것을 환영합니다.";
        String End = "*--------------계속하려면 메뉴 번호를 선택하세요--------------*";
        for (int i = 0; i < Welcome.length(); i++) {
            System.out.print("-");
        }
        String Options = """
                1. 고객 추가/삭제/조회
                2. 지점 추가/삭제/조회
                3. 방탈출 테마 추가/삭제/조회
                4. 테마 예약/예약 취소
                5. 예약 번호로 예약 조회
                6. 전체 예약 현황 조회
                7. 고객 이메일로 예약 조회
                8. 데이터 저장
                9. 데이터 불러오기
                0. 종료
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
