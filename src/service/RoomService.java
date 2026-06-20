package service;

import model.Room.Room;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * RoomService interacts when called by the controller from view.
 *
 * @author 220031985
 */
public class RoomService implements Serializable {
    private static final String DATA_FILE = "roomData.txt";
    private static Map<String, Room> rooms = new HashMap<>();

    /**
     * Constructor of the class.
     */
    public RoomService() {
    }

    /**
     * Adding the room.
     *
     * @param buildingName building name.
     * @param roomName     room name.
     * @param isBooked     state of the room (Redudant).
     */
    //Bug Fix: Only one room is storing for one value in the Terminal???? (Addressed)
    // Bug Fix: If Key is the same, the building is getting replaced with the latest update. (Addressed)
    public void addRoom(String buildingName, String roomName, boolean isBooked) {
        addRoom(buildingName, roomName, isBooked, "미지정", "NORMAL", 60, 2, 4, 0);
    }

    public void addRoom(String buildingName, String roomName, boolean isBooked, String genre, String difficulty,
                        int durationMinutes, int minPlayers, int maxPlayers, int pricePerPerson) {
        validateThemeInput(buildingName, roomName, genre, difficulty, durationMinutes, minPlayers, maxPlayers, pricePerPerson);
        String normalizedRoomName = roomName.trim().toUpperCase();
        if (rooms.containsKey(normalizedRoomName)) {
            throw new IllegalArgumentException("이미 등록된 테마명입니다.\n");
        }
        rooms.put(normalizedRoomName, new Room(normalizedRoomName, buildingName.trim().toLowerCase(), isBooked,
                genre.trim(), difficulty.trim().toUpperCase(), durationMinutes, minPlayers, maxPlayers, pricePerPerson));
    }

    private void validateThemeInput(String buildingName, String roomName, String genre, String difficulty,
                                    int durationMinutes, int minPlayers, int maxPlayers, int pricePerPerson) {
        if (buildingName.isEmpty() || roomName.isEmpty() || genre.isEmpty() || difficulty.isEmpty()) {
            throw new IllegalArgumentException("입력값을 모두 입력해주세요.");
        }
        if (durationMinutes < 1) {
            throw new IllegalArgumentException("플레이 시간은 1분 이상이어야 합니다.");
        }
        if (minPlayers < 1) {
            throw new IllegalArgumentException("최소 인원은 1명 이상이어야 합니다.");
        }
        if (maxPlayers < minPlayers) {
            throw new IllegalArgumentException("최대 인원은 최소 인원 이상이어야 합니다.");
        }
        if (pricePerPerson < 0) {
            throw new IllegalArgumentException("1인 가격은 0원 이상이어야 합니다.");
        }
        String normalizedDifficulty = difficulty.trim().toUpperCase();
        if (!normalizedDifficulty.equals("EASY") && !normalizedDifficulty.equals("NORMAL") && !normalizedDifficulty.equals("HARD")) {
            throw new IllegalArgumentException("난이도는 EASY, NORMAL, HARD 중 하나를 입력해주세요.");
        }
    }

    /**
     * Deleting the room.
     *
     * @param buildingName1 Building Name.
     * @param roomName1     Room Name.
     */
    public void delRoom(String buildingName1, String roomName1) {
        String buildingName = buildingName1;
        String roomName = roomName1;
        if (roomName.isEmpty() || buildingName.isEmpty()) {
            throw new IllegalArgumentException("입력값을 모두 입력해주세요.");
        } else if (rooms.isEmpty()) {
            throw new IllegalArgumentException("등록된 테마가 없습니다.\n");
        } else if (!rooms.isEmpty()) {
            Set<Entry<String, Room>> setOfEntries = rooms.entrySet();
            Iterator<Entry<String, Room>> iterator = setOfEntries.iterator();
            while (iterator.hasNext()) {
                Entry<String, Room> entry = iterator.next();
                String value = entry.getKey();
                if (value.contains(roomName.toUpperCase())) {
                    iterator.remove();
                }
            }
        } else {
            throw new IllegalArgumentException("등록되지 않은 테마입니다.\n");
        }
    }


    /**
     * Updating the Room Model if Building is Deleted(Rooms Deleted).
     *
     * @param buildingName building Name.
     */
    //  Exception-Handling: Concurrent Modification in the main thread (Addressed).
    public void delRoomFromBuild(String buildingName) {
        Set<Entry<String, Room>> setOfEntries = rooms.entrySet();
        Iterator<Entry<String, Room>> iterator = setOfEntries.iterator();
        while (iterator.hasNext()) {
            Entry<String, Room> entry = iterator.next();
            String value = entry.getValue().getBuildingName();
            if (value.contains(buildingName.toLowerCase())) {
                iterator.remove();
            }

        }
    }

    /**
     * Viewing all rooms in Buildings present in the system.
     */
    public Map<String, Room> viewRooms() {
        Map<String, Room> viewRooms = new HashMap<>();
        if (rooms.isEmpty()) {
            throw new IllegalArgumentException("등록된 테마가 없습니다.\n");
        } else {
            viewRooms.putAll(rooms);
            return rooms;
        }
    }


    /**
     * Check if room exists in the system.
     *
     * @param roomName Room Name.
     * @return true if exits.
     */
    public boolean checkRoom(String roomName) {
        if (roomName.trim().isEmpty()) {
            throw new IllegalArgumentException("등록된 테마가 없습니다.\n");
        }
        if (rooms.containsKey(roomName.toUpperCase())) {
            return true;
        } else {
            throw new IllegalArgumentException("등록되지 않은 테마입니다.\n");
        }
    }

    public Room getRoom(String roomName) {
        if (roomName.trim().isEmpty()) {
            throw new IllegalArgumentException("테마명을 입력해주세요.");
        }
        Room room = rooms.get(roomName.toUpperCase());
        if (room == null) {
            throw new IllegalArgumentException("등록되지 않은 테마입니다.\n");
        }
        return room;
    }


    /**
     * Updating the room status.
     *
     * @param roomName     Room Name.
     * @param buildingName Building Name.
     * @param isBooked     status of the room (redudant).
     */
    // Bug Fix : To Update the room in the building if a booking is done.
    public void updateRoom(String roomName, String buildingName, boolean isBooked) {
        roomName = roomName.toUpperCase();
        Set<Entry<String, Room>> setOfEntries = rooms.entrySet();
        Iterator<Entry<String, Room>> iterator = setOfEntries.iterator();
        while (iterator.hasNext()) {
            Entry<String, Room> entry = iterator.next();
            String key = entry.getKey();

            if (key.contains(roomName)) {
                rooms.replace(roomName, entry.getValue().withBookingStatus(buildingName.toLowerCase(), isBooked));
            }

        }

    }

    public void roomsBooked() {
        if (rooms.isEmpty()) {
            throw new IllegalArgumentException("등록된 테마가 없습니다.");
        } else if (!rooms.isEmpty()) {
            for (Map.Entry<String, Room> m : rooms.entrySet()) {
                if (m.getValue().isBooked()) {
                    System.out.println(m.getValue().toDisplayString());
                } else {
                    System.out.println("현재 예약된 테마가 없습니다.");
                }

            }
        }
    }

    /**
     * For test purposes.
     *
     * @return size of the room map.
     */
    public int roomCount() {
        return rooms.size();
    }

    public void roomsFree() {
        if (rooms.isEmpty()) {
            throw new IllegalArgumentException("등록된 테마가 없습니다.");
        } else {
            for (Map.Entry<String, Room> m : rooms.entrySet()) {
                if (!m.getValue().isBooked()) {
                    System.out.println(m.getValue().toDisplayString());
                }

            }
        }
    }

    /**
     * Saving the data of room Model.
     *
     * @throws IOException throws IOException.
     */
    public void roomSave() throws IOException {
        try (ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            o.writeObject(rooms);
        }
    }

    /**
     * Load the room data.
     *
     * @throws IOException            throws IOException
     * @throws ClassNotFoundException throws ClassNotFoundException.
     */
    public boolean roomLoad() throws IOException, ClassNotFoundException {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return false;
        }
        try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(file))) {
            rooms = (Map<String, Room>) oi.readObject();
            return true;
        }
    }


}
