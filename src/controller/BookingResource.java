package controller;


import model.Building.Building;
import model.PlayResult.PlayResult;
import model.Reservation.Reservation;
import model.Room.Room;
import model.User.User;
import service.BuildingService;
import service.PlayResultService;
import service.ReservationService;
import service.RoomService;
import service.StatisticsService;
import service.UserService;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * View(GUI/CLI)와 Service 계층 사이를 연결하는 Controller 클래스입니다.
 *
 * <p>발표 시 코드 흐름은 View → BookingResource → Service → Model 순서로 설명하면 됩니다.
 * 이 클래스는 화면에서 받은 요청을 각 서비스로 전달하고, 예약/플레이 결과/통계 기능의
 * 진입점 역할을 합니다.
 *
 * @author 220031985.
 */
public class BookingResource implements Runnable {

    private final UserService userService;
    private final RoomService roomService;
    private final ReservationService reservationService;
    private final BuildingService buildingService;
    private final PlayResultService playResultService;
    private final StatisticsService statisticsService;

    public BookingResource(UserService userService, RoomService roomService, ReservationService reservationService, BuildingService buildingService) {
        this.userService = userService;
        this.roomService = roomService;
        this.reservationService = reservationService;
        this.buildingService = buildingService;
        this.playResultService = new PlayResultService();
        this.statisticsService = new StatisticsService();
    }


    /**
     * Passes value to the service layer.
     *
     * @param email Email is Unique.
     * @param name  Name of the user.
     * @throws IOException if empty or Invalid.
     */
    public void addUser(String email, String name) throws IOException {
        userService.addUser(email, name);

    }

    public void addAdmin(String email, String name) {
        userService.addAdmin(email, name);
    }

    public boolean checkAdmin(String email) {
        return userService.checkAdmin(email);
    }

    public void requireAdmin(String email) {
        userService.checkAdmin(email);
    }

    /**
     * Updates the value to the service layer.
     *
     * @param email Updates the user email to service layer.
     */
    public void delUser(String email) {
        userService.delUser(email);
    }

    /**
     * Calling all users in the model by asking the service layer.
     */
    public Map<String, User> viewUsers() {
        return userService.viewUsers();
    }


    /**
     * The Controller passes the params to the service layer.
     *
     * @param buildingName Add the building name.
     * @param address      Add the Building's address.
     * @param email        User that is adding the building.
     */
    public void addBuilding(String buildingName, String address, String email) {
        buildingService.addBuilding(buildingName, address, email);
    }

    /**
     * THe Controller updates the params to the service layer.
     *
     * @param buildingName Updating the building parameter.
     */
    public void delBuilding(String buildingName) {
        buildingService.delBuilding(buildingName);
    }

    /**
     * Calling the service layer to view all Buildings.
     *
     * @return
     */
    public Map<String, Building> viewBuildings() {
        return buildingService.viewBuildings();

    }


    /**
     * Calling the service layer -> Model from the controller.
     *
     * @param buildingName Adding building name.
     * @param roomName     Adding room name.
     */
    public void addRoom(String buildingName, String roomName, boolean isBooked) {
        roomService.addRoom(buildingName, roomName, isBooked);
    }

    public void addRoom(String buildingName, String roomName, boolean isBooked, String genre, String difficulty,
                        int durationMinutes, int minPlayers, int maxPlayers, int pricePerPerson) {
        roomService.addRoom(buildingName, roomName, isBooked, genre, difficulty, durationMinutes, minPlayers, maxPlayers, pricePerPerson);
    }

    /**
     * Updating the model by invoking service layer from the controller.
     *
     * @param buildingName Updating the buildingName.
     * @param roomName     Updating the roomName.
     */
    public void delRoom(String buildingName, String roomName) {
        roomService.delRoom(buildingName, roomName);
    }

    /**
     * Generating all rooms present in the System
     */
    public Map<String, Room> viewRooms() {
        return roomService.viewRooms();
    }


    /**
     * Checks if the user does exist or not.
     *
     * @param email checking if the user is present in the system.
     * @return If true, User exists.
     */
    public boolean checkUser(String email) {
        return userService.checkUser(email);
    }

    /**
     * Checks if the building exists or not
     *
     * @param buildingName checking if a Building is present in the system.
     * @return if true, Building exists.
     */
    public boolean checkBuilding(String buildingName) {
        return buildingService.checkBuilding(buildingName);
    }

    /**
     * Checks if the room does exist in the System.
     *
     * @param roomName Passes the room to the service layer.
     * @return if true, room Exists.
     */
    public boolean checkRoom(String roomName) {

        return roomService.checkRoom(roomName);
    }

    public Room getRoom(String roomName) {
        return roomService.getRoom(roomName);
    }

    /**
     * Adding the Reservation of a room in the Building by a User.
     *
     * @param bookingID    Booking Reference ID
     * @param email        User's unique ID.
     * @param buildingName Building that contains the room.
     * @param room         Room that is to be booked.
     * @param checkInDate  The date that is to be booked.
     * @param checkOutDate The date the booking ends.
     * @param checkInTime  Time the user needs to book the room.
     * @param checkOutTime Time the user exits the room.
     * @param isBooked     Boolean value to check if the room is available.
     */
    public void addReservation(String bookingID, String email, String buildingName, String room, String checkInDate, String checkOutDate, String checkInTime, String checkOutTime, boolean isBooked) {
        reservationService.addReservation(bookingID, email, buildingName, room, checkInDate, checkOutDate, checkInTime, checkOutTime, isBooked);
    }

    public void addReservation(String bookingID, String email, String buildingName, String room, String checkInDate, String checkOutDate,
                               String checkInTime, String checkOutTime, boolean isBooked, int playerCount) {
        Room theme = roomService.getRoom(room);
        validatePlayerCount(theme, playerCount);
        int totalPrice = playerCount * theme.getPricePerPerson();
        reservationService.addReservation(bookingID, email, buildingName, room, checkInDate, checkOutDate, checkInTime, checkOutTime, isBooked, playerCount, totalPrice);
    }

    private void validatePlayerCount(Room theme, int playerCount) {
        if (playerCount < 1) {
            throw new IllegalArgumentException("예약 인원은 1명 이상이어야 합니다.");
        }
        if (playerCount < theme.getMinPlayers() || playerCount > theme.getMaxPlayers()) {
            throw new IllegalArgumentException("예약 인원은 테마 권장 인원(" + theme.getMinPlayers() + "~" + theme.getMaxPlayers() + "명) 범위 안에서 입력해주세요.");
        }
    }

    /**
     * Deleting the booking done by User.
     *
     * @param bookingID Booking reference ID.
     */
    public void delReservation(String bookingID) {
        reservationService.delReservation(bookingID);
    }

    public void cancelReservation(String bookingID) {
        reservationService.cancelReservation(bookingID);
    }

    public void completeReservation(String bookingID) {
        reservationService.completeReservation(bookingID);
    }


    /**
     * To delete Rooms if the Building is deleted.
     *
     * @param buildingName Checks if the building is does exist and deletes the rooms present.
     */
    public void delRoomFromBuild(String buildingName) {
        roomService.delRoomFromBuild(buildingName);
    }

    /**
     * View a particular bookings.
     *
     * @param bookingID if the user has a reservation.
     * @return
     */
    public HashMap<String, Reservation> viewMyRes(String bookingID) {
        return reservationService.viewMyRes(bookingID);
    }

    /**
     * View Bookings by User_email_ID.
     *
     * @param email email_ID.
     * @return
     */
    public Map<String, Reservation> viewResName(String email) {
        return reservationService.viewResName(email);
    }


    /**
     * To see if BookingID exists in the system.
     *
     * @param bookingID makes each booking referenced and unique.
     */
    public void checkID(String bookingID) {
        reservationService.checkID(bookingID);
    }

    /**
     * @param checkInTime  Check In Time of the User.
     * @param checkOutTime Check out Time of the User.
     * @param roomName     Room booked by the User.
     * @param checkInDate  Check In date of the User.
     */
    public void checkOverlap(String checkInTime, String checkOutTime, String roomName, String checkInDate) {
        reservationService.checkOverlap(checkInTime, checkOutTime, roomName, checkInDate);
    }

    /**
     * @param roomName     Room booked by the User
     * @param buildingName Building which holds the Room.
     * @param isBooked     State of the Room (redundant).
     */
    public void updateRoom(String roomName, String buildingName, boolean isBooked) {
        roomService.updateRoom(roomName, buildingName, isBooked);
    }

    /**
     * Calling all Booked Rooms to the View.
     *
     * @return
     */
    public Map<String, Reservation> bookedRooms() {
        return reservationService.roomsBooked();

    }

    /**
     * Adds or updates a play result for an existing booking ID.
     *
     * <p>PlayResult는 반드시 기존 Reservation의 bookingId와 연결되어야 하므로,
     * 저장 전에 reservationService.viewMyRes(bookingId)로 예약 존재 여부를 확인합니다.
     */
    public void addPlayResult(String bookingId, boolean success, int hintCount, int remainingMinutes, String staffMemo) {
        reservationService.viewMyRes(bookingId);
        playResultService.addPlayResult(bookingId, success, hintCount, remainingMinutes, staffMemo);
        reservationService.completeReservation(bookingId);
    }

    /**
     * Views all saved escape-room play results.
     */
    public Map<String, PlayResult> viewPlayResults() {
        return playResultService.viewPlayResults();
    }

    /**
     * Builds escape-room operation statistics without throwing on empty data.
     *
     * <p>StatisticsService는 Reservation과 PlayResult의 복사본을 받아 Stream API로 통계를 계산합니다.
     */
    public String viewStatistics() {
        return statisticsService.buildStatisticsReport(reservationService.getReservations(), playResultService.getPlayResults());
    }


    /**
     * Saving User Data.
     *
     * @throws IOException Exception Handling.
     */
    public void userSave() throws IOException {
        userService.userSave();
    }

    /**
     * Loading User Data.
     *
     * @throws IOException            throws IOException.
     * @throws ClassNotFoundException throws ClassNotFoundException.
     */
    public boolean userLoad() throws IOException, ClassNotFoundException {
        return userService.userLoad();
    }

    /**
     * Saving Building Data.
     *
     * @throws IOException throws IOException.
     */
    public void buildSave() throws IOException {
        buildingService.buildSave();
    }

    /**
     * Loading Building Data.
     *
     * @throws IOException throws IOException.
     */
    public boolean buildLoad() throws IOException, ClassNotFoundException {
        return buildingService.buildLoad();

    }

    /**
     * Saving Room Data.
     *
     * @throws IOException throws IOException.
     */
    public void roomSave() throws IOException {
        roomService.roomSave();

    }

    /**
     * Loading Room Data.
     *
     * @throws IOException            throws IOException
     * @throws ClassNotFoundException throws ClassNotFoundException.
     */
    public boolean roomLoad() throws IOException, ClassNotFoundException {
        return roomService.roomLoad();

    }


    /**
     * Saving the Reservation Data
     *
     * @throws IOException throws IOException.
     */
    public void resSave() throws IOException {
        reservationService.resSave();

    }

    /**
     * Loading the Reservation Data.
     *
     * @throws IOException throws IOException.
     */
    public boolean resLoad() throws IOException, ClassNotFoundException {
        return reservationService.resLoad();
    }

    public void playResultSave() throws IOException {
        playResultService.playResultSave();
    }

    public boolean playResultLoad() throws IOException, ClassNotFoundException {
        return playResultService.playResultLoad();
    }

    public String saveAllData() {
        try {
            userSave();
            buildSave();
            roomSave();
            resSave();
            playResultSave();
            return "데이터가 성공적으로 저장되었습니다.";
        } catch (IOException ex) {
            throw new IllegalArgumentException("데이터 저장에 실패했습니다: " + ex.getMessage());
        }
    }

    public String loadAllData() {
        try {
            boolean loaded = false;
            loaded |= userLoad();
            loaded |= buildLoad();
            loaded |= roomLoad();
            loaded |= resLoad();
            loaded |= playResultLoad();
            if (!loaded) {
                return "저장된 데이터가 없어 새로 시작합니다.";
            }
            return "데이터를 성공적으로 불러왔습니다.";
        } catch (IOException | ClassNotFoundException | ClassCastException ex) {
            throw new IllegalArgumentException("데이터 불러오기에 실패했습니다: " + ex.getMessage());
        }
    }

    //To implement the following parameters for these types of functions.(Addressed)

    public void isBeforeTime(String checkInTime, String checkOutTime) {
        int cmp = checkInTime.compareTo(checkOutTime);
        if (cmp > 0) {
            throw new IllegalArgumentException("시작 시간은 종료 시간보다 늦을 수 없습니다.");
        } else if (cmp == 0) {
            throw new IllegalArgumentException("예약은 최소 5분 이상이어야 합니다.");
        }

    }

    public void checkDay(String checkInDate, String checkOutDate) throws ParseException {
        DateTimeFormatter fIn = DateTimeFormatter.ofPattern("dd/MM/uuuu", Locale.UK);  // As a habit, specify the desired/expected locale, though in this case the locale is irrelevant.

        LocalDate d1 = LocalDate.parse(checkInDate, fIn);
        LocalDate d2 = LocalDate.parse(checkOutDate, fIn);

        long difference = ChronoUnit.DAYS.between(d1, d2);
        if (difference != 0) {
            throw new IllegalArgumentException("하루를 초과하는 예약은 할 수 없습니다.");
        }
    }

    public void LeastBy5(String checkInTime, String checkOutTime) {
        String minIn = checkInTime.substring(3);
        String minOut = checkOutTime.substring(3);
        int minIn1 = Integer.parseInt(minIn);
        int minOut1 = Integer.parseInt(minOut);
        String hrIn = checkInTime.substring(0, checkInTime.length() - 3);
        String hrOut = checkOutTime.substring(0, checkOutTime.length() - 3);
        int hrIn1 = Integer.parseInt(hrIn);
        int hrOut1 = Integer.parseInt(hrOut);
        int dif = hrOut1 - hrIn1;
        int dif60 = dif * 60;
        //Bug-Fix: On lower numbers, the value adds (Addressed).
        int totalMin = minOut1 - minIn1;
        int totalTime = dif60 + totalMin;
        if (totalTime < 5) {
            throw new IllegalArgumentException("예약은 최소 5분 이상이어야 합니다.");
        } else if ((totalTime % 5) != 0) {
            throw new IllegalArgumentException("예약 시간은 5분 단위로 입력해야 합니다.");
        }

    }


    public void isDateValid(String date) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        {
            try {
                LocalDate.parse(date, f);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("오류가 발생했습니다: " + e);
            }
        }

    }


    /**
     * To check if input is of the valid time format.
     *
     * @param input Time.
     */
    public void validateTime(String input) {
        if (input.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
        } else {
            throw new IllegalArgumentException("시간 형식이 올바르지 않습니다. 예: 14:30");
        }
    }


    /**
     *
     */
    @Override
    public void run() {

    }
}
