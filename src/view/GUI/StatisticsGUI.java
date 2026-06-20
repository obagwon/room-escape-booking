package view.GUI;

import controller.BookingResource;

import javax.swing.*;
import java.awt.*;

/**
 * 운영 통계 결과를 보여주는 Swing 화면입니다.
 *
 * <p>StatisticsService가 만든 텍스트 리포트를 JTextArea에 표시합니다.
 * 발표 시에는 이 화면을 통해 Stream API로 계산한 통계 결과를 시연하면 됩니다.
 */
public class StatisticsGUI extends JFrame {
    private final JPanel statisticsPanel;
    private final JTextArea statisticsArea;

    public StatisticsGUI(BookingResource bookingResource) {
        statisticsPanel = new JPanel(new GridBagLayout());
        statisticsArea = new JTextArea(24, 56);
        statisticsArea.setEditable(false);

        setContentPane(statisticsPanel);
        setTitle("운영 통계");
        setSize(860, 720);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        buildView(bookingResource);
        statisticsArea.setText(bookingResource.viewStatistics());
        setVisible(true);
    }

    private void buildView(BookingResource bookingResource) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("방탈출 운영 통계");
        gbc.gridx = 0;
        gbc.gridy = 0;
        statisticsPanel.add(title, gbc);

        JButton refreshButton = new JButton("통계 새로고침");
        refreshButton.addActionListener(e -> statisticsArea.setText(bookingResource.viewStatistics()));
        gbc.gridy = 1;
        statisticsPanel.add(refreshButton, gbc);

        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        statisticsPanel.add(new JScrollPane(statisticsArea), gbc);

        JButton backButton = new JButton("메인 메뉴로 돌아가기");
        backButton.addActionListener(e -> {
            dispose();
            new Main_GUI(bookingResource);
        });
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        statisticsPanel.add(backButton, gbc);
    }
}
