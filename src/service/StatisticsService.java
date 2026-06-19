package service;

import model.PlayResult.PlayResult;
import model.Reservation.Reservation;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Calculates escape-room operation statistics with Java Stream API.
 */
public class StatisticsService implements Serializable {

    public String buildStatisticsReport(Map<String, Reservation> reservations, Map<String, PlayResult> playResults) {
        int totalReservations = reservations.size();
        int completedPlays = playResults.size();
        double overallSuccessRate = successRate(playResults);
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
        return playResults.values().stream()
                .mapToInt(result -> result.isSuccess() ? 1 : 0)
                .average()
                .orElse(0.0);
    }

    private String formatPercent(double rate) {
        return String.format("%.2f%%", rate * 100);
    }
}
