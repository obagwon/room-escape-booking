package service;

import model.PlayResult.PlayResult;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * PlayResult 데이터를 저장, 조회, 파일 저장/불러오기 하는 Service 클래스입니다.
 *
 * <p>힌트 수와 남은 시간은 0 이상이어야 하므로 이 클래스에서 입력값 검증을 수행합니다.
 * GUI는 BookingResource를 통해 이 서비스를 호출합니다.
 */
public class PlayResultService implements Serializable {
    private Map<String, PlayResult> playResults = new HashMap<>();

    public void addPlayResult(String bookingId, boolean success, int hintCount, int remainingMinutes, String staffMemo) {
        // 발표 포인트: 잘못된 입력을 Service 계층에서 한 번 더 검증하여 데이터 안정성을 유지합니다.
        if (bookingId == null || bookingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Booking ID cannot be empty.");
        }
        if (hintCount < 0) {
            throw new IllegalArgumentException("Hint count must be 0 or greater.");
        }
        if (remainingMinutes < 0) {
            throw new IllegalArgumentException("Remaining time must be 0 or greater.");
        }

        playResults.put(bookingId, new PlayResult(bookingId, success, hintCount, remainingMinutes, staffMemo));
    }

    public Map<String, PlayResult> viewPlayResults() {
        if (playResults.isEmpty()) {
            throw new IllegalArgumentException("No play results are registered in the system.");
        }
        return new HashMap<>(playResults);
    }

    public Map<String, PlayResult> getPlayResults() {
        return new HashMap<>(playResults);
    }

    public void playResultSave() throws IOException {
        FileOutputStream f = new FileOutputStream(new File("playResultData.txt"));
        ObjectOutputStream o = new ObjectOutputStream(f);
        o.writeObject(playResults);
        o.close();
        f.close();
    }

    public void playResultLoad() {
        try {
            FileInputStream fi = new FileInputStream(new File("playResultData.txt"));
            ObjectInputStream oi = new ObjectInputStream(fi);
            playResults = (Map<String, PlayResult>) oi.readObject();
            oi.close();
            fi.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
