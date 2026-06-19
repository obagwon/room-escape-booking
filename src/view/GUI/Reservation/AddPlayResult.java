package view.GUI.Reservation;

import controller.BookingResource;

import javax.swing.*;
import java.awt.*;

public class AddPlayResult extends JFrame {
    private final JPanel addPlayResult;
    private final JTextField bookingIdField;
    private final JCheckBox successCheckBox;
    private final JTextField hintCountField;
    private final JTextField remainingMinutesField;
    private final JTextArea staffMemoArea;

    public AddPlayResult(BookingResource bookingResource) {
        addPlayResult = new JPanel(new GridBagLayout());
        bookingIdField = new JTextField(20);
        successCheckBox = new JCheckBox("Success");
        hintCountField = new JTextField(20);
        remainingMinutesField = new JTextField(20);
        staffMemoArea = new JTextArea(5, 20);

        setContentPane(addPlayResult);
        setTitle("ADD PLAY RESULT");
        setSize(520, 520);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        buildForm(bookingResource);
        setVisible(true);
    }

    private void buildForm(BookingResource bookingResource) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addLabel("PLAY RESULT", 0, gbc);
        addLabel("BOOKING ID", 1, gbc);
        addField(bookingIdField, 2, gbc);
        addLabel("SUCCESS", 3, gbc);
        addField(successCheckBox, 4, gbc);
        addLabel("HINT COUNT (0 or greater)", 5, gbc);
        addField(hintCountField, 6, gbc);
        addLabel("REMAINING TIME IN MINUTES (0 or greater)", 7, gbc);
        addField(remainingMinutesField, 8, gbc);
        addLabel("STAFF MEMO", 9, gbc);
        addField(new JScrollPane(staffMemoArea), 10, gbc);

        JButton saveButton = new JButton("SAVE RESULT");
        saveButton.addActionListener(e -> savePlayResult(bookingResource));
        addField(saveButton, 11, gbc);

        JButton backButton = new JButton("BACK");
        backButton.addActionListener(e -> {
            dispose();
            new Reservation(bookingResource);
        });
        addField(backButton, 12, gbc);
    }

    private void savePlayResult(BookingResource bookingResource) {
        try {
            String bookingId = bookingIdField.getText().trim();
            int hintCount = parseNonNegativeInt(hintCountField.getText().trim(), "Hint count");
            int remainingMinutes = parseNonNegativeInt(remainingMinutesField.getText().trim(), "Remaining time");
            String staffMemo = staffMemoArea.getText().trim();
            bookingResource.addPlayResult(bookingId, successCheckBox.isSelected(), hintCount, remainingMinutes, staffMemo);
            JOptionPane.showMessageDialog(addPlayResult, "Play result saved successfully.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(addPlayResult, ex.getLocalizedMessage());
        }
    }

    private int parseNonNegativeInt(String value, String fieldName) {
        try {
            int number = Integer.parseInt(value);
            if (number < 0) {
                throw new IllegalArgumentException(fieldName + " must be 0 or greater.");
            }
            return number;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " must be a whole number.");
        }
    }

    private void addLabel(String text, int row, GridBagConstraints gbc) {
        JLabel label = new JLabel(text);
        gbc.gridx = 0;
        gbc.gridy = row;
        addPlayResult.add(label, gbc);
    }

    private void addField(Component component, int row, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = row;
        addPlayResult.add(component, gbc);
    }
}
