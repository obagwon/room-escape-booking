package Tests.basic.Test09_RoomService;

import org.junit.Test;
import service.RoomService;

import static org.junit.Assert.*;


public class RoomServiceTest {
    RoomService roomService = new RoomService();

    @Test
    public void addRoom() {
        roomService.addRoom("jack cole", "J10", true, "공포", "HARD", 60, 2, 4, 28000);
        roomService.addRoom("Powell Hall", "C10", true, "추리", "NORMAL", 50, 2, 5, 26000);
        assertEquals(2, roomService.roomCount());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            roomService.addRoom("jack cole", "J10", true, "공포", "HARD", 60, 2, 4, 28000);
        });
        String expectedMessage = "이미 등록된 테마명입니다.\n";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void addRoomRejectsInvalidThemeDetails() {
        Exception durationException = assertThrows(IllegalArgumentException.class, () ->
                roomService.addRoom("jack cole", "BAD1", false, "공포", "HARD", 0, 2, 4, 28000));
        assertTrue(durationException.getMessage().contains("플레이 시간은 1분 이상이어야 합니다."));

        Exception maxPlayersException = assertThrows(IllegalArgumentException.class, () ->
                roomService.addRoom("jack cole", "BAD2", false, "공포", "HARD", 60, 5, 4, 28000));
        assertTrue(maxPlayersException.getMessage().contains("최대 인원은 최소 인원 이상이어야 합니다."));

        Exception difficultyException = assertThrows(IllegalArgumentException.class, () ->
                roomService.addRoom("jack cole", "BAD3", false, "공포", "EXTREME", 60, 2, 4, 28000));
        assertTrue(difficultyException.getMessage().contains("난이도는 EASY, NORMAL, HARD 중 하나를 입력해주세요."));
    }

    @Test
    public void addRoomStoresThemeDetails() {
        RoomService localRoomService = new RoomService();
        localRoomService.addRoom("branch", "DETAIL_UNIQUE", false, "추리", "EASY", 45, 2, 6, 23000);

        assertEquals("추리", localRoomService.getRoom("DETAIL_UNIQUE").getGenre());
        assertEquals("EASY", localRoomService.getRoom("DETAIL_UNIQUE").getDifficulty());
        assertEquals(23000, localRoomService.getRoom("DETAIL_UNIQUE").getPricePerPerson());
    }

    @Test
    public void delRoomRemovesTheme() {
        RoomService localRoomService = new RoomService();
        localRoomService.addRoom("branch", "DELETE_UNIQUE", false, "공포", "NORMAL", 60, 2, 4, 25000);
        localRoomService.delRoom("branch", "DELETE_UNIQUE");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> localRoomService.checkRoom("DELETE_UNIQUE"));
        assertTrue(exception.getMessage().contains("등록되지 않은 테마입니다."));
    }

    @Test
    public void addRoomRejectsNegativePrice() {
        RoomService localRoomService = new RoomService();
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                localRoomService.addRoom("branch", "PRICE_BAD_UNIQUE", false, "공포", "HARD", 60, 2, 4, -1));
        assertTrue(exception.getMessage().contains("1인 가격은 0원 이상이어야 합니다."));
    }

    @Test
    public void delRoom() {
        roomService.addRoom("jack coles", "J101", true);
        assertEquals(3, roomService.roomCount());
        roomService.delRoom("jack coles", "J101");
        assertEquals(2, roomService.roomCount());


    }

    @Test
    public void checkRoom() {
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            roomService.checkRoom("");
        });
        String expectedMessage1 = "등록된 테마가 없습니다.\n";
        String actualMessage1 = exception1.getMessage();

        assertTrue(actualMessage1.contains(expectedMessage1));

    }
}
