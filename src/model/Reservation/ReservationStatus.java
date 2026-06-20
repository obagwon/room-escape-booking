package model.Reservation;

/**
 * 예약 상태를 표현하는 Enum입니다.
 * 발표에서는 고급 Java 기법 중 Enum 적용 예시로 설명할 수 있습니다.
 */
public enum ReservationStatus {
    RESERVED("예약됨"),
    CANCELLED("취소됨"),
    COMPLETED("이용 완료"),
    NO_SHOW("노쇼");

    private final String displayName;

    ReservationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
