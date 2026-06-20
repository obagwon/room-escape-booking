package Tests.basic.Test12_PlayResultService;

import org.junit.Test;
import service.PlayResultService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class PlayResultServiceTest {

    @Test
    public void addPlayResult() {
        PlayResultService playResultService = new PlayResultService();
        playResultService.addPlayResult("R1", true, 2, 10, "성공");

        assertEquals(1, playResultService.viewPlayResults().size());
        assertTrue(playResultService.viewPlayResults().get("R1").isSuccess());
    }

    @Test
    public void addPlayResultRejectsNegativeHintCount() {
        PlayResultService playResultService = new PlayResultService();
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                playResultService.addPlayResult("R1", true, -1, 10, "bad"));

        assertTrue(exception.getMessage().contains("힌트 사용 횟수는 0 이상이어야 합니다."));
    }

    @Test
    public void addPlayResultRejectsNegativeRemainingMinutes() {
        PlayResultService playResultService = new PlayResultService();
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                playResultService.addPlayResult("R1", true, 1, -1, "bad"));

        assertTrue(exception.getMessage().contains("남은 시간은 0 이상이어야 합니다."));
    }
}
