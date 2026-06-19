package model.PlayResult;

import java.io.Serializable;

/**
 * 방탈출 플레이 종료 후 기록하는 결과 Model 클래스입니다.
 *
 * <p>Reservation의 bookingId와 연결되며, 성공 여부, 힌트 사용 횟수,
 * 남은 시간, 직원 메모를 저장합니다. 발표에서는 "예약 이후 운영 데이터"를
 * 담당하는 클래스라고 설명하면 됩니다.
 */
public class PlayResult implements Serializable {
    private final String bookingId;
    private final boolean success;
    private final int hintCount;
    private final int remainingMinutes;
    private final String staffMemo;

    public PlayResult(String bookingId, boolean success, int hintCount, int remainingMinutes, String staffMemo) {
        this.bookingId = bookingId;
        this.success = success;
        this.hintCount = hintCount;
        this.remainingMinutes = remainingMinutes;
        this.staffMemo = staffMemo;
    }

    public String getBookingId() {
        return bookingId;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getHintCount() {
        return hintCount;
    }

    public int getRemainingMinutes() {
        return remainingMinutes;
    }

    public String getStaffMemo() {
        return staffMemo;
    }
}
