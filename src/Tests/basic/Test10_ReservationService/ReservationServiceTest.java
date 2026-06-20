package Tests.basic.Test10_ReservationService;

import model.Reservation.ReservationStatus;
import org.junit.Test;
import service.ReservationService;

import static org.junit.Assert.*;

public class ReservationServiceTest {

    @Test
    public void addReservation() {
        ReservationService reservationService = new ReservationService();
        reservationService.addReservation("abc", "alpha@gmail.com", "jack cole", "J10", "12-02-2022", "12-02-2022", "13:00", "14:00", true, 3, 84000);
        assertEquals(1, reservationService.resCount());
        assertEquals(3, reservationService.viewMyRes("abc").get("abc").getPlayerCount());
        assertEquals(84000, reservationService.viewMyRes("abc").get("abc").getTotalPrice());
    }

    @Test
    public void delReservation() {
        ReservationService reservationService = new ReservationService();
        reservationService.addReservation("abc", "alpha@gmail.com", "jack cole", "J10", "12-02-2022", "12-02-2022", "13:00", "14:00", true);
        reservationService.delReservation("abc");
        assertEquals(1, reservationService.resCount());
        assertEquals(ReservationStatus.CANCELLED, reservationService.viewMyRes("abc").get("abc").getStatus());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> reservationService.delReservation("abc1"));
        assertTrue(exception.getMessage().contains("해당 예약 번호로 등록된 예약이 없습니다."));
    }

    @Test
    public void viewMyRes() {
        ReservationService reservationService = new ReservationService();
        reservationService.addReservation("abc", "alpha@gmail.com", "jack cole", "J10", "12-02-2022", "12-02-2022", "13:00", "14:00", true);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> reservationService.viewMyRes("missing"));
        assertTrue(exception.getMessage().contains("해당 고객의 예약 내역이 없습니다."));
    }

    @Test
    public void checkID() {
        ReservationService reservationService = new ReservationService();
        reservationService.addReservation("abc", "alpha@gmail.com", "jack cole", "J10", "12-02-2022", "12-02-2022", "13:00", "14:00", true);
        assertFalse(reservationService.checkID("abc1"));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> reservationService.checkID("abc"));
        assertTrue(exception.getMessage().contains("같은 예약 번호를 중복 사용할 수 없습니다."));
    }

    @Test
    public void checkOverlapRejectsSameThemeSameDateOverlappingTime() {
        ReservationService reservationService = new ReservationService();
        reservationService.addReservation("abc", "alpha@gmail.com", "jack cole", "J10", "12-02-2022", "12-02-2022", "10:00", "11:00", true);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                reservationService.checkOverlap("10:30", "11:30", "j10", "12-02-2022"));

        assertTrue(exception.getMessage().contains("해당 테마는 이미 예약되어 있습니다. 예약 고객: alpha@gmail.com"));
        assertEquals(1, reservationService.resCount());
    }

    @Test
    public void checkOverlapAllowsAdjacentReservation() {
        ReservationService reservationService = new ReservationService();
        reservationService.addReservation("abc", "alpha@gmail.com", "jack cole", "J10", "12-02-2022", "12-02-2022", "10:00", "11:00", true);

        reservationService.checkOverlap("11:00", "12:00", "J10", "12-02-2022");
        reservationService.addReservation("def", "beta@gmail.com", "jack cole", "J10", "12-02-2022", "12-02-2022", "11:00", "12:00", true);

        assertEquals(2, reservationService.resCount());
    }

    @Test
    public void checkOverlapAllowsDifferentThemeAtSameTime() {
        ReservationService reservationService = new ReservationService();
        reservationService.addReservation("abc", "alpha@gmail.com", "jack cole", "J10", "12-02-2022", "12-02-2022", "10:00", "11:00", true);

        reservationService.checkOverlap("10:30", "11:30", "J11", "12-02-2022");
        reservationService.addReservation("def", "beta@gmail.com", "jack cole", "J11", "12-02-2022", "12-02-2022", "10:30", "11:30", true);

        assertEquals(2, reservationService.resCount());
    }

    @Test
    public void checkOverlapAllowsSameThemeSameTimeOnDifferentDate() {
        ReservationService reservationService = new ReservationService();
        reservationService.addReservation("abc", "alpha@gmail.com", "jack cole", "J10", "12-02-2022", "12-02-2022", "10:00", "11:00", true);

        reservationService.checkOverlap("10:00", "11:00", "J10", "13-02-2022");
        reservationService.addReservation("def", "beta@gmail.com", "jack cole", "J10", "13-02-2022", "13-02-2022", "10:00", "11:00", true);

        assertEquals(2, reservationService.resCount());
    }

    @Test
    public void checkOverlapRejectsInvalidTimeFormat() {
        ReservationService reservationService = new ReservationService();
        reservationService.addReservation("abc", "alpha@gmail.com", "jack cole", "J10", "12-02-2022", "12-02-2022", "10:00", "11:00", true);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                reservationService.checkOverlap("10:AA", "11:30", "J10", "12-02-2022"));

        assertTrue(exception.getMessage().contains("시간 형식이 올바르지 않습니다. 예: 14:30"));
    }

    @Test
    public void viewResName() {
        ReservationService reservationService = new ReservationService();
        reservationService.addReservation("abc", "alpha@gmail.com", "jack cole", "J10", "12-02-2022", "12-02-2022", "13:00", "14:00", true);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> reservationService.viewResName(""));
        assertTrue(exception.getMessage().contains("고객 이메일을 입력해주세요."));
        reservationService.delReservation("abc");
        assertEquals(1, reservationService.resCount());
        assertEquals(ReservationStatus.CANCELLED, reservationService.viewResName("alpha@gmail.com").get("abc").getStatus());
    }

    @Test
    public void checkOverlapIgnoresCancelledReservation() {
        ReservationService reservationService = new ReservationService();
        reservationService.addReservation("abc", "alpha@gmail.com", "jack cole", "J10", "12-02-2022", "12-02-2022", "10:00", "11:00", true);
        reservationService.cancelReservation("abc");

        reservationService.checkOverlap("10:30", "11:30", "j10", "12-02-2022");
        reservationService.addReservation("def", "beta@gmail.com", "jack cole", "J10", "12-02-2022", "12-02-2022", "10:30", "11:30", true);

        assertEquals(2, reservationService.resCount());
    }

    @Test
    public void completeReservationChangesStatus() {
        ReservationService reservationService = new ReservationService();
        reservationService.addReservation("abc", "alpha@gmail.com", "jack cole", "J10", "12-02-2022", "12-02-2022", "10:00", "11:00", true);
        reservationService.completeReservation("abc");

        assertEquals(ReservationStatus.COMPLETED, reservationService.viewMyRes("abc").get("abc").getStatus());
    }
}
