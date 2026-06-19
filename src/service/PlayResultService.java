package service;

import model.PlayResult.PlayResult;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores play results linked to reservation booking IDs.
 */
public class PlayResultService implements Serializable {
    private Map<String, PlayResult> playResults = new HashMap<>();

    public void addPlayResult(String bookingId, boolean success, int hintCount, int remainingMinutes, String staffMemo) {
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
