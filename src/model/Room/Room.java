package model.Room;

import java.io.Serializable;

/**
 * 방탈출 테마 정보를 저장하는 모델입니다.
 *
 * <p>기존 클래스명 Room은 유지하지만, 화면과 문서에서는 방탈출 "테마"로 표현합니다.
 */
public class Room implements Serializable {
    private static final long serialVersionUID = 7052591309734494766L;

    private final boolean isBooked;
    private final String roomName;
    private final String buildingName;
    private final String genre;
    private final String difficulty;
    private final int durationMinutes;
    private final int minPlayers;
    private final int maxPlayers;
    private final int pricePerPerson;

    /**
     * 기존 테마 추가 흐름과 저장된 호출부 호환을 위한 생성자입니다.
     */
    public Room(String roomName, String buildingName, boolean isBooked) {
        this(roomName, buildingName, isBooked, "미지정", "NORMAL", 60, 2, 4, 0);
    }

    public Room(String roomName, String buildingName, boolean isBooked, String genre, String difficulty,
                int durationMinutes, int minPlayers, int maxPlayers, int pricePerPerson) {
        this.roomName = roomName;
        this.buildingName = buildingName;
        this.isBooked = isBooked;
        this.genre = genre;
        this.difficulty = difficulty;
        this.durationMinutes = durationMinutes;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.pricePerPerson = pricePerPerson;
    }

    public String getRoomName() {
        return roomName;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getGenre() {
        return genre;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getPricePerPerson() {
        return pricePerPerson;
    }

    public Room withBookingStatus(String buildingName, boolean isBooked) {
        return new Room(roomName, buildingName, isBooked, genre, difficulty, durationMinutes, minPlayers, maxPlayers, pricePerPerson);
    }

    public String toDisplayString() {
        return "테마명: " + roomName + "\n"
                + "지점명: " + buildingName + "\n"
                + "장르: " + genre + "\n"
                + "난이도: " + difficulty + "\n"
                + "플레이 시간: " + durationMinutes + "분\n"
                + "권장 인원: " + minPlayers + "~" + maxPlayers + "명\n"
                + "1인 가격: " + pricePerPerson + "원\n"
                + "예약 여부: " + (isBooked ? "예약됨" : "예약 가능");
    }
}
