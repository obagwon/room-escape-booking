package service;

import model.PlayResult.PlayResult;
import model.Reservation.Reservation;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 방탈출 카페 운영 통계를 계산하는 Service 클래스입니다.
 *
 * <p>예약 데이터와 플레이 결과 데이터를 입력받아 전체 예약 수, 완료된 플레이 수,
 * 전체 성공률, 평균 힌트 수, 테마별 예약 수, 테마별 성공률을 계산합니다.
 * 발표에서는 Java Stream API를 활용한 집계 로직의 핵심 클래스로 설명하면 됩니다.
 */
public class StatisticsService implements Serializable {

    public String buildStatisticsReport(Map<String, Reservation> reservations, Map<String, PlayResult> playResults) {
        // 전체 예약 수와 완료된 플레이 수는 Map 크기로 바로 계산합니다.
        int totalReservations = reservations.size();
        int completedPlays = playResults.size();
        double overallSuccessRate = successRate(playResults);

        // Stream API로 모든 플레이 결과의 hintCount를 int 스트림으로 바꾼 뒤 평균을 계산합니다.
        // 데이터가 하나도 없으면 orElse(0.0)으로 0을 사용하여 예외를 방지합니다.
        double averageHints = playResults.values().stream()
                .mapToInt(PlayResult::getHintCount)
                .average()
                .orElse(0.0);

        StringBuilder report = new StringBuilder();
        report.append("Escape Room Operation Statistics\n")
                .append("--------------------------------\n")
                .append("Total Reservations: ").append(totalReservations).append("\n")
                .append("Completed Plays: ").append(completedPlays).append("\n")
                .append("Overall Success Rate: ").append(formatPercent(overallSuccessRate)).append("\n")
                .append("Average Hints Used: ").append(String.format("%.2f", averageHints)).append("\n\n")
                .append("Reservations by Theme\n")
                .append("---------------------\n");

        // 테마 이름(Reservation.getRoom)을 기준으로 예약 수를 그룹화합니다.
        // Collectors.groupingBy + counting 조합이 "테마별 예약 수" 계산의 핵심입니다.
        Map<String, Long> reservationsByTheme = reservations.values().stream()
                .collect(Collectors.groupingBy(Reservation::getRoom, Collectors.counting()));

        if (reservationsByTheme.isEmpty()) {
            report.append("No reservation data.\n");
        } else {
            reservationsByTheme.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> report.append(entry.getKey())
                            .append(": ")
                            .append(entry.getValue())
                            .append("\n"));
        }

        report.append("\nSuccess Rate by Theme\n")
                .append("---------------------\n");

        // 테마별 성공률 계산 흐름:
        // 1) 예약을 테마별로 묶고
        // 2) 각 예약의 bookingId로 PlayResult를 찾고
        // 3) 성공이면 1, 실패면 0으로 변환한 뒤 평균을 냅니다.
        // 아직 플레이 결과가 없는 예약은 filter(result != null)로 제외합니다.
        Map<String, Double> successRateByTheme = reservations.values().stream()
                .collect(Collectors.groupingBy(
                        Reservation::getRoom,
                        Collectors.collectingAndThen(Collectors.toList(), themeReservations ->
                                themeReservations.stream()
                                        .map(Reservation::getBookingID)
                                        .map(playResults::get)
                                        .filter(result -> result != null)
                                        .mapToInt(result -> result.isSuccess() ? 1 : 0)
                                        .average()
                                        .orElse(0.0)
                        )
                ));

        if (successRateByTheme.isEmpty()) {
            report.append("No theme success data.\n");
        } else {
            successRateByTheme.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> report.append(entry.getKey())
                            .append(": ")
                            .append(formatPercent(entry.getValue()))
                            .append("\n"));
        }

        return report.toString();
    }

    private double successRate(Map<String, PlayResult> playResults) {
        // 전체 성공률도 성공=1, 실패=0으로 변환한 뒤 평균을 내는 방식입니다.
        return playResults.values().stream()
                .mapToInt(result -> result.isSuccess() ? 1 : 0)
                .average()
                .orElse(0.0);
    }

    private String formatPercent(double rate) {
        return String.format("%.2f%%", rate * 100);
    }
}
