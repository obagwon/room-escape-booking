package model.PlayResult;

import java.io.Serializable;

/**
 * Play result for a completed escape-room reservation.
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
