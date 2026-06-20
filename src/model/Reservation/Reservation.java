package model.Reservation;


import java.io.Serializable;

/**
 * 방탈출 테마 예약 정보를 저장하는 Model 클래스입니다.
 *
 * <p>기존 프로젝트의 Room Booking 구조를 유지하기 위해 필드명은 buildingName, room 등을 사용하지만,
 * 발표에서는 buildingName = 카페 지점, room = 방탈출 테마로 설명하면 됩니다.
 * bookingID는 예약 조회와 PlayResult 연결에 사용되는 핵심 식별자입니다.
 */
public class Reservation implements Serializable {
    private static final long serialVersionUID = -8376204894422513685L;

    private final String bookingID;
    private final String buildingName;
    private final String email;
    private final String room;
    private final String checkInDate;
    private final String checkOutDate;
    private final String checkInTime;
    private final String checkOutTime;
    private final int playerCount;
    private final int totalPrice;

    private final boolean isBooked = false;
    private ReservationStatus status;


    /**
     * Reservation Model.
     * @param bookingID    Booking ID for Reservations (Unique).
     * @param email        Email ID for Booking a Reservation(Identifier).
     * @param buildingName Building Name.
     * @param room         Room Name.
     * @param checkInDate  Check in Date.
     * @param checkOutDate Check out Date
     * @param checkInTime  Check in time.
     * @param checkOutTime Check out time.
     * @param isBooked     Room's status if booked or not (redundant).
     */
    public Reservation(String bookingID, String email, String buildingName, String room, String checkInDate, String checkOutDate, String checkInTime, String checkOutTime, boolean isBooked) {
        this(bookingID, email, buildingName, room, checkInDate, checkOutDate, checkInTime, checkOutTime, isBooked, 0, 0);
    }

    public Reservation(String bookingID, String email, String buildingName, String room, String checkInDate, String checkOutDate,
                       String checkInTime, String checkOutTime, boolean isBooked, int playerCount, int totalPrice) {
        this.bookingID = bookingID;
        this.email = email;
        this.buildingName = buildingName;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.playerCount = playerCount;
        this.totalPrice = totalPrice;
        this.status = isBooked ? ReservationStatus.RESERVED : ReservationStatus.CANCELLED;
    }


    /**
     * Returns the Booking ID.
     *
     * @return BookingID.
     */
    public String getBookingID() {
        return bookingID;
    }

    /**
     * Returns the checkInDate.
     *
     * @return CheckInDate.
     */
    public String getCheckInDate() {
        return this.checkInDate;
    }

    /**
     * Returns the CheckOutDate.
     *
     * @return CheckOutDate.
     */
    public String getCheckOutDate() {
        return this.checkOutDate;
    }

    /**
     * Returns the email.
     *
     * @return email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the buildingName.
     *
     * @return buildingName.
     */
    public String getBuildingName() {
        return buildingName;
    }

    /**
     * Returns rooms.
     *
     * @return room.
     */
    public String getRoom() {
        return room;
    }

    /**
     * Returns the checkInTime.
     *
     * @return checkInTime.
     */
    public String getCheckInTime() {
        return checkInTime;
    }

    /**
     * Returns the checkOutTime.
     *
     * @return CheckOutTime
     */
    public String getCheckOutTime() {
        return checkOutTime;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    /**
     * Returns the reservation status enum used for presentation and future extension.
     *
     * @return reservation status.
     */
    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("예약 상태를 입력해주세요.");
        }
        this.status = status;
    }

    public String getStatusDisplayName() {
        return status.getDisplayName();
    }

    public boolean blocksReservationOverlap() {
        return status != ReservationStatus.CANCELLED;
    }
}
