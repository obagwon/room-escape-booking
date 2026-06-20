package Tests.basic.Test11_StatisticsService;

import model.PlayResult.PlayResult;
import model.Reservation.Reservation;
import org.junit.Test;
import service.StatisticsService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class StatisticsServiceTest {

    @Test
    public void buildStatisticsReportIncludesRevenuePopularThemeAndMostHintedReservation() {
        Map<String, Reservation> reservations = new HashMap<>();
        reservations.put("R1", new Reservation("R1", "a@gmail.com", "Gangnam", "저택의 비밀", "12-02-2022", "12-02-2022", "10:00", "11:00", true, 3, 84000));
        reservations.put("R2", new Reservation("R2", "b@gmail.com", "Gangnam", "저택의 비밀", "12-02-2022", "12-02-2022", "11:00", "12:00", true, 2, 56000));
        reservations.put("R3", new Reservation("R3", "c@gmail.com", "Gangnam", "연구소 탈출", "12-02-2022", "12-02-2022", "12:00", "13:00", true, 2, 52000));
        reservations.get("R3").setStatus(model.Reservation.ReservationStatus.CANCELLED);

        Map<String, PlayResult> playResults = new HashMap<>();
        playResults.put("R1", new PlayResult("R1", true, 2, 10, "good"));
        playResults.put("R2", new PlayResult("R2", false, 5, 0, "hard"));

        String report = new StatisticsService().buildStatisticsReport(reservations, playResults);

        assertTrue(report.contains("총 예상 매출: 140,000원"));
        assertTrue(report.contains("가장 인기 있는 테마: 저택의 비밀 (2건)"));
        assertTrue(report.contains("힌트를 가장 많이 사용한 예약: R2 (5회)"));
        assertTrue(report.contains("취소 예약 수: 1건"));
    }
    @Test
    public void buildStatisticsReportWithoutDataDoesNotThrow() {
        String report = new StatisticsService().buildStatisticsReport(new HashMap<>(), new HashMap<>());

        assertTrue(report.contains("전체 예약 수: 0건"));
        assertTrue(report.contains("예약 데이터가 없습니다."));
        assertTrue(report.contains("플레이 결과 데이터가 없습니다."));
    }

    @Test
    public void buildStatisticsReportCalculatesSuccessRateAverageHintsAndThemeCount() {
        Map<String, Reservation> reservations = new HashMap<>();
        reservations.put("R10", new Reservation("R10", "a@gmail.com", "Gangnam", "연구소 탈출", "12-02-2022", "12-02-2022", "10:00", "11:00", true, 2, 52000));
        reservations.put("R11", new Reservation("R11", "b@gmail.com", "Gangnam", "연구소 탈출", "12-02-2022", "12-02-2022", "11:00", "12:00", true, 3, 78000));

        Map<String, PlayResult> playResults = new HashMap<>();
        playResults.put("R10", new PlayResult("R10", true, 1, 12, "success"));
        playResults.put("R11", new PlayResult("R11", false, 3, 0, "fail"));

        String report = new StatisticsService().buildStatisticsReport(reservations, playResults);

        assertTrue(report.contains("전체 성공률: 50.00%"));
        assertTrue(report.contains("평균 힌트 사용 횟수: 2.00회"));
        assertTrue(report.contains("연구소 탈출: 2건"));
        assertTrue(report.contains("연구소 탈출: 50.00%"));
    }

}
