package service;


import model.Reservation.Reservation;
import model.Reservation.ReservationStatus;

import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;


/**
 * Reservation Service Layer interacts when called by the controller from the view.
 *
 * @author 220031985.
 */
// Bug Fix: If user is the key, they cannot make more than one booking (Addressed).
public class ReservationService implements Serializable {
    private static final String DATA_FILE = "resData.txt";

    private Map<String, Reservation> reservation = new HashMap<>();


    /**
     * Constructor of the class (Declaration).
     */
    public ReservationService() {
    }


    /**
     * @param bookingID    BookingID.
     * @param email        Email of the User.
     * @param buildingName Building Name.
     * @param room         Room Name.
     * @param checkInDate  Check In Date.
     * @param checkOutDate Check Out Date.
     * @param checkInTime  Check In TIme.
     * @param checkOutTime Check Out Time.
     * @param isBooked     state of the room (redudant).
     */
    // Bug Fix: Rewrite the method as keeping bookingID as the key. (Addressed)
    public void addReservation(String bookingID, String email, String buildingName, String room, String checkInDate, String checkOutDate, String checkInTime, String checkOutTime, boolean isBooked) {
        addReservation(bookingID, email, buildingName, room, checkInDate, checkOutDate, checkInTime, checkOutTime, isBooked, 0, 0);
    }

    public void addReservation(String bookingID, String email, String buildingName, String room, String checkInDate, String checkOutDate,
                               String checkInTime, String checkOutTime, boolean isBooked, int playerCount, int totalPrice) {
        if (bookingID.trim().isEmpty() || email.trim().isEmpty() || buildingName.trim().isEmpty() || checkInDate.trim().isEmpty() || checkInDate.trim().isEmpty() || checkOutDate.trim().isEmpty() || checkInTime.trim().isEmpty() || checkOutTime.trim().isEmpty()) {
            throw new IllegalArgumentException("입력값을 모두 입력해주세요.");
        }
        if (playerCount < 0) {
            throw new IllegalArgumentException("예약 인원은 1명 이상이어야 합니다.");
        }
        if (totalPrice < 0) {
            throw new IllegalArgumentException("총 가격은 0원 이상이어야 합니다.");
        }
        reservation.put(bookingID, new Reservation(bookingID, email, buildingName, room, checkInDate, checkOutDate, checkInTime, checkOutTime, isBooked, playerCount, totalPrice));

    }

    /**
     * Deleting a Reservation of a User.
     *
     * @param bookingID Booking ID.
     */
    public void delReservation(String bookingID) {
        cancelReservation(bookingID);
    }

    public void cancelReservation(String bookingID) {
        Reservation target = reservation.get(bookingID);
        if (target != null) {
            target.setStatus(ReservationStatus.CANCELLED);
        } else if (reservation.isEmpty()) {
            throw new IllegalArgumentException("등록된 예약이 없습니다.");
        } else {
            throw new IllegalArgumentException("해당 예약 번호로 등록된 예약이 없습니다.");
        }

    }

    public void completeReservation(String bookingID) {
        updateReservationStatus(bookingID, ReservationStatus.COMPLETED);
    }

    public void markNoShow(String bookingID) {
        updateReservationStatus(bookingID, ReservationStatus.NO_SHOW);
    }

    public void updateReservationStatus(String bookingID, ReservationStatus status) {
        Reservation target = reservation.get(bookingID);
        if (target != null) {
            target.setStatus(status);
        } else if (reservation.isEmpty()) {
            throw new IllegalArgumentException("등록된 예약이 없습니다.");
        } else {
            throw new IllegalArgumentException("해당 예약 번호로 등록된 예약이 없습니다.");
        }
    }


    /**
     * Viewing Reservation of an individual User (Booking ID).
     *
     * @param bookingID Booking ID.
     */
    // Bug Fix: Rewrite the method as keeping BookingID as the key or email address as value. (Addressed)
    public HashMap<String, Reservation> viewMyRes(String bookingID) {
        HashMap<String, Reservation> bookingIDRes = new HashMap<String, Reservation>();
        if (reservation.isEmpty()) {
            throw new IllegalArgumentException("해당 예약 번호로 등록된 예약이 없습니다: " + bookingID);
        } else if (reservation.containsKey(bookingID)) {
            for (Map.Entry<String, Reservation> m : reservation.entrySet()) {
                if (m.getKey().equals(bookingID)) {
                    bookingIDRes.put(m.getKey(), m.getValue());
                }
            }
        } else {
            throw new IllegalArgumentException("해당 고객의 예약 내역이 없습니다.");
        }
        return bookingIDRes;
    }

    /**
     * To check out the Booking ID.
     *
     * @param bookingID Booking ID given by User.
     * @return
     */
    public boolean checkID(String bookingID) {
        if (bookingID.trim().isEmpty()) {
            throw new IllegalArgumentException("입력값을 모두 입력해주세요.");
        } else if (reservation.containsKey(bookingID)) {
            throw new IllegalArgumentException("같은 예약 번호를 중복 사용할 수 없습니다.");
        } else if (bookingID.isEmpty()) {
            throw new IllegalArgumentException(("예약 번호를 입력해주세요."));
        }
        return false;
    }

    /**
     * Checking for Overlap Conditions.
     *
     * @param checkInTime  Check In Time.
     * @param checkOutTime Check Out Time.
     * @param roomName     Room Name.
     * @param checkInDate  Check In Date.
     */
    public void checkOverlap(String checkInTime, String checkOutTime, String roomName, String checkInDate) {
        LocalTime newCheckInTime = parseReservationTime(checkInTime);
        LocalTime newCheckOutTime = parseReservationTime(checkOutTime);
        String normalizedRoomName = normalizeRoomName(roomName);
        String normalizedCheckInDate = checkInDate.trim();

        for (Map.Entry<String, Reservation> m : reservation.entrySet()) {
            Reservation existingReservation = m.getValue();
            boolean sameRoom = normalizeRoomName(existingReservation.getRoom()).equals(normalizedRoomName);
            boolean sameDate = existingReservation.getCheckInDate().trim().equals(normalizedCheckInDate);

            if (sameRoom && sameDate && existingReservation.blocksReservationOverlap()) {
                LocalTime existingCheckInTime = parseReservationTime(existingReservation.getCheckInTime());
                LocalTime existingCheckOutTime = parseReservationTime(existingReservation.getCheckOutTime());
                boolean overlaps = newCheckInTime.isBefore(existingCheckOutTime) && newCheckOutTime.isAfter(existingCheckInTime);

                if (overlaps) {
                    throw new IllegalArgumentException("해당 테마는 이미 예약되어 있습니다. 예약 고객: " + existingReservation.getEmail());
                }
            }
        }
    }

    private LocalTime parseReservationTime(String time) {
        try {
            return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException | NullPointerException e) {
            throw new IllegalArgumentException("시간 형식이 올바르지 않습니다. 예: 14:30");
        }
    }

    private String normalizeRoomName(String roomName) {
        return roomName == null ? "" : roomName.trim().toUpperCase();
    }

    /**
     * Viewing all Rooms Booked (Assertion : All other rooms that are not printed in this method is free to be booked).
     *
     * @return
     */
    public Map<String, Reservation> roomsBooked() {
        if (reservation.isEmpty()) {
            throw new IllegalArgumentException("예약된 테마가 없습니다.");
        } else {
            Map<String, Reservation> bookedRooms = new HashMap<>();
            bookedRooms.putAll(reservation);
            return bookedRooms;
        }
    }


    /**
     * Viewing Reservation of a user by email_ID.
     *
     * @param email email_ID.
     * @return
     */
    // Bug Fix : This for - loop is not working at the moment. (Addressed)
    public Map<String, Reservation> viewResName(String email) {
        HashMap<String, Reservation> viewResID = new HashMap<String, Reservation>();
        if (email.isEmpty()) {
            throw new IllegalArgumentException("고객 이메일을 입력해주세요.");
        } else if (reservation.isEmpty()) {
            throw new IllegalArgumentException("해당 이메일로 등록된 예약이 없습니다: " + email);
        } else if (!reservation.isEmpty()) {
            for (Map.Entry<String, Reservation> m : reservation.entrySet()) {
                if (m.getValue().getEmail().contains(email)) {
                    viewResID.put(m.getKey(), m.getValue());
                }
            }
        } else {
            throw new IllegalArgumentException("해당 고객의 예약 내역이 없습니다.");
        }
        return viewResID;

    }

    /**
     * Saving Reservation Data.
     *
     * @throws IOException
     */
    public void resSave() throws IOException {
        try (ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            o.writeObject(reservation);
        }
    }

    /**
     * Loading Reservation Data.
     */
    public boolean resLoad() throws IOException, ClassNotFoundException {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return false;
        }
        try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(file))) {
            reservation = (Map<String, Reservation>) oi.readObject();
            return true;
        }
    }

    /**
     * For testing purposes.
     *
     * @return size of the Reservation map.
     */
    public int resCount() {
        return reservation.size();


    }

    public Map<String, Reservation> getReservations() {
        return new HashMap<>(reservation);
    }
}
