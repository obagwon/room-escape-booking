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
        setTitle("Escape Room Manager - Play Results");
        setSize(680, 560);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        buildView(bookingResource);
        setVisible(true);
    }

    private void buildView(BookingResource bookingResource) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Play Result List");
        gbc.gridx = 0;
        gbc.gridy = 0;
        viewPlayResults.add(title, gbc);

        JButton loadButton = new JButton("Refresh Results");
        loadButton.addActionListener(e -> loadResults(bookingResource));
        gbc.gridy = 1;
        viewPlayResults.add(loadButton, gbc);

        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        viewPlayResults.add(new JScrollPane(resultsArea), gbc);

        JButton backButton = new JButton("Back to Reservation Menu");
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
                builder.append("Booking ID: ").append(result.getBookingId()).append("\n")
                        .append("Success: ").append(result.isSuccess() ? "YES" : "NO").append("\n")
                        .append("Hints Used: ").append(result.getHintCount()).append("\n")
                        .append("Remaining Time: ").append(result.getRemainingMinutes()).append(" minutes\n")
                        .append("Staff Memo: ").append(result.getStaffMemo()).append("\n\n");
            }
            resultsArea.setText(builder.toString());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(viewPlayResults, ex.getLocalizedMessage());
        }
    }
}
