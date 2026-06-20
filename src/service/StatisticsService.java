package service;

import model.PlayResult.PlayResult;
import model.Reservation.Reservation;
import model.Reservation.ReservationStatus;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 방탈출 카페 운영 통계를 계산하는 Service 클래스입니다.
 *
 * <p>예약 데이터와 플레이 결과 데이터를 입력받아 예약 상태, 매출, 테마별 예약/성공률,
 * 인기 테마, 힌트 최다 사용 예약을 Stream API로 계산합니다.
 */
public class StatisticsService implements Serializable {

    public String buildStatisticsReport(Map<String, Reservation> reservations, Map<String, PlayResult> playResults) {
        int totalReservations = reservations.size();
        long reservedReservations = countByStatus(reservations, ReservationStatus.RESERVED);
        long cancelledReservations = countByStatus(reservations, ReservationStatus.CANCELLED);
        long completedReservations = countByStatus(reservations, ReservationStatus.COMPLETED);
        int completedPlays = playResults.size();
        double overallSuccessRate = successRate(playResults);
        double averageHints = averageHints(playResults);
        int expectedRevenue = expectedRevenue(reservations);
        Map<String, Long> reservationsByTheme = reservationsByTheme(reservations);
        Map<String, Double> successRateByTheme = successRateByTheme(reservations, playResults);
        String mostPopularTheme = mostPopularTheme(reservationsByTheme);
        String mostHintedReservation = mostHintedReservation(playResults);

        StringBuilder report = new StringBuilder();
        report.append("방탈출 운영 통계\n")
                .append("====================\n")
                .append("전체 예약 수: ").append(formatNumber(totalReservations)).append("건\n")
                .append("예약 완료 수: ").append(formatNumber(completedReservations)).append("건\n")
                .append("예약 대기/확정 수: ").append(formatNumber(reservedReservations)).append("건\n")
                .append("취소 예약 수: ").append(formatNumber(cancelledReservations)).append("건\n")
                .append("완료된 플레이 수: ").append(formatNumber(completedPlays)).append("건\n")
                .append("전체 성공률: ").append(formatPercent(overallSuccessRate)).append("\n")
                .append("평균 힌트 사용 횟수: ").append(String.format(Locale.KOREA, "%.2f회", averageHints)).append("\n")
                .append("총 예상 매출: ").append(formatCurrency(expectedRevenue)).append("\n")
                .append("가장 인기 있는 테마: ").append(mostPopularTheme).append("\n")
                .append("힌트를 가장 많이 사용한 예약: ").append(mostHintedReservation).append("\n\n")
                .append("테마별 예약 수\n")
                .append("--------------------\n");

        if (reservationsByTheme.isEmpty()) {
            report.append("예약 데이터가 없습니다.\n");
        } else {
            reservationsByTheme.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> report.append(entry.getKey())
                            .append(": ")
                            .append(formatNumber(entry.getValue()))
                            .append("건\n"));
        }

        report.append("\n테마별 성공률\n")
                .append("--------------------\n");

        if (successRateByTheme.isEmpty()) {
            report.append("플레이 결과 데이터가 없습니다.\n");
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

    private long countByStatus(Map<String, Reservation> reservations, ReservationStatus status) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getStatus() == status)
                .count();
    }

    private double successRate(Map<String, PlayResult> playResults) {
        return playResults.values().stream()
                .mapToInt(result -> result.isSuccess() ? 1 : 0)
                .average()
                .orElse(0.0);
    }

    private double averageHints(Map<String, PlayResult> playResults) {
        return playResults.values().stream()
                .mapToInt(PlayResult::getHintCount)
                .average()
                .orElse(0.0);
    }

    private int expectedRevenue(Map<String, Reservation> reservations) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getStatus() != ReservationStatus.CANCELLED)
                .mapToInt(Reservation::getTotalPrice)
                .sum();
    }

    private Map<String, Long> reservationsByTheme(Map<String, Reservation> reservations) {
        return reservations.values().stream()
                .collect(Collectors.groupingBy(Reservation::getRoom, Collectors.counting()));
    }

    private Map<String, Double> successRateByTheme(Map<String, Reservation> reservations, Map<String, PlayResult> playResults) {
        return reservations.values().stream()
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
    }

    private String mostPopularTheme(Map<String, Long> reservationsByTheme) {
        return reservationsByTheme.entrySet().stream()
                .max(Map.Entry.<String, Long>comparingByValue().thenComparing(Map.Entry.comparingByKey()))
                .map(entry -> entry.getKey() + " (" + formatNumber(entry.getValue()) + "건)")
                .orElse("예약 데이터가 없습니다.");
    }

    private String mostHintedReservation(Map<String, PlayResult> playResults) {
        Optional<PlayResult> mostHinted = playResults.values().stream()
                .max(Comparator.comparingInt(PlayResult::getHintCount));
        return mostHinted
                .map(result -> result.getBookingId() + " (" + formatNumber(result.getHintCount()) + "회)")
                .orElse("플레이 결과 데이터가 없습니다.");
    }

    private String formatNumber(long number) {
        return NumberFormat.getNumberInstance(Locale.KOREA).format(number);
    }

    private String formatCurrency(int amount) {
        return NumberFormat.getNumberInstance(Locale.KOREA).format(amount) + "원";
    }

    private String formatPercent(double rate) {
        return String.format(Locale.KOREA, "%.2f%%", rate * 100);
    }
}
