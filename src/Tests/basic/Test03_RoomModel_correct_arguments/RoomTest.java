package Tests.basic.Test03_RoomModel_correct_arguments;

import model.Room.Room;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class RoomTest {
    Room room = new Room("J.11", "jack cole", false, "공포", "HARD", 60, 2, 4, 28000);

    @Test
    void getRoomName() {
        assertEquals("J.11", room.getRoomName());
    }

    @Test
    void isBooked() {
        assertFalse(room.isBooked());
    }

    @Test
    void getBuildingName() {
        assertEquals("jack cole", room.getBuildingName());
    }

    @Test
    void getThemeDetails() {
        assertEquals("공포", room.getGenre());
        assertEquals("HARD", room.getDifficulty());
        assertEquals(60, room.getDurationMinutes());
        assertEquals(2, room.getMinPlayers());
        assertEquals(4, room.getMaxPlayers());
        assertEquals(28000, room.getPricePerPerson());
    }
}
