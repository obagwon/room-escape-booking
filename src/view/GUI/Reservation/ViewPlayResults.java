package view.GUI.Reservation;

import controller.BookingResource;
import model.PlayResult.PlayResult;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ViewPlayResults extends JFrame {
    private final JPanel viewPlayResults;
    private final JTextArea resultsArea;

    public ViewPlayResults(BookingResource bookingResource) {
        viewPlayResults = new JPanel(new GridBagLayout());
        resultsArea = new JTextArea(15, 36);
        resultsArea.setEditable(false);

        setContentPane(viewPlayResults);
        setTitle("플레이 결과 조회");
        setSize(680, 560);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        buildView(bookingResource);
        setVisible(true);
    }

    private void buildView(BookingResource bookingResource) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("플레이 결과 목록");
        gbc.gridx = 0;
        gbc.gridy = 0;
        viewPlayResults.add(title, gbc);

        JButton loadButton = new JButton("결과 새로고침");
        loadButton.addActionListener(e -> loadResults(bookingResource));
        gbc.gridy = 1;
        viewPlayResults.add(loadButton, gbc);

        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        viewPlayResults.add(new JScrollPane(resultsArea), gbc);

        JButton backButton = new JButton("예약 메뉴로 돌아가기");
        backButton.addActionListener(e -> {
            dispose();
            new Reservation(bookingResource);
        });
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        viewPlayResults.add(backButton, gbc);
    }

    private void loadResults(BookingResource bookingResource) {
        try {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, PlayResult> entry : bookingResource.viewPlayResults().entrySet()) {
                PlayResult result = entry.getValue();
                builder.append("예약 번호: ").append(result.getBookingId()).append("\n")
                        .append("성공 여부: ").append(result.isSuccess() ? "예" : "아니오").append("\n")
                        .append("힌트 사용 횟수: ").append(result.getHintCount()).append("\n")
                        .append("남은 시간: ").append(result.getRemainingMinutes()).append("분\n")
                        .append("직원 메모: ").append(result.getStaffMemo()).append("\n\n");
            }
            resultsArea.setText(builder.toString());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(viewPlayResults, ex.getLocalizedMessage());
        }
    }
}
