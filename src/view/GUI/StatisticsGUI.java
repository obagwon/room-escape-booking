package view.GUI;

import controller.BookingResource;

import javax.swing.*;
import java.awt.*;

public class StatisticsGUI extends JFrame {
    private final JPanel statisticsPanel;
    private final JTextArea statisticsArea;

    public StatisticsGUI(BookingResource bookingResource) {
        statisticsPanel = new JPanel(new GridBagLayout());
        statisticsArea = new JTextArea(18, 44);
        statisticsArea.setEditable(false);

        setContentPane(statisticsPanel);
        setTitle("OPERATION STATISTICS");
        setSize(700, 560);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        buildView(bookingResource);
        statisticsArea.setText(bookingResource.viewStatistics());
        setVisible(true);
    }

    private void buildView(BookingResource bookingResource) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("ESCAPE ROOM OPERATION STATISTICS");
        gbc.gridx = 0;
        gbc.gridy = 0;
        statisticsPanel.add(title, gbc);

        JButton refreshButton = new JButton("REFRESH STATISTICS");
        refreshButton.addActionListener(e -> statisticsArea.setText(bookingResource.viewStatistics()));
        gbc.gridy = 1;
        statisticsPanel.add(refreshButton, gbc);

        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        statisticsPanel.add(new JScrollPane(statisticsArea), gbc);

        JButton backButton = new JButton("BACK");
        backButton.addActionListener(e -> {
            dispose();
            new Main_GUI(bookingResource);
        });
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        statisticsPanel.add(backButton, gbc);
    }
}
