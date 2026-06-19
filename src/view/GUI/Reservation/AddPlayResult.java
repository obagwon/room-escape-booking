package view.GUI.Reservation;

import controller.BookingResource;

import javax.swing.*;
import java.awt.*;

/**
 * 예약 완료 후 플레이 결과를 입력하는 Swing 화면입니다.
 *
 * <p>bookingId를 기준으로 Reservation과 PlayResult를 연결합니다.
 * 성공 여부, 힌트 수, 남은 시간, 직원 메모를 입력받으며 결과 메시지는 JOptionPane으로 표시합니다.
 */
public class AddPlayResult extends JFrame {
    private final JPanel addPlayResult;
    private final JTextField bookingIdField;
    private final JCheckBox successCheckBox;
    private final JTextField hintCountField;
    private final JTextField remainingMinutesField;
    private final JTextArea staffMemoArea;

    public AddPlayResult(BookingResource bookingResource) {
        addPlayResult = new JPanel(new GridBagLayout());
        bookingIdField = new JTextField(24);
        successCheckBox = new JCheckBox("Escaped successfully");
        hintCountField = new JTextField(24);
        remainingMinutesField = new JTextField(24);
        staffMemoArea = new JTextArea(5, 24);

        setContentPane(addPlayResult);
        setTitle("Escape Room Manager - Add Play Result");
        setSize(620, 520);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        buildForm(bookingResource);
        setVisible(true);
    }

    private void buildForm(BookingResource bookingResource) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Play Result Entry");
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        addPlayResult.add(title, gbc);
        gbc.gridwidth = 1;

        addRow("Booking ID", bookingIdField, 1, gbc);
        addRow("Success", successCheckBox, 2, gbc);
        addRow("Hint Count (0 or greater)", hintCountField, 3, gbc);
        addRow("Remaining Time in Minutes (0 or greater)", remainingMinutesField, 4, gbc);
        addRow("Staff Memo", new JScrollPane(staffMemoArea), 5, gbc);

        JButton saveButton = new JButton("Save Play Result");
        saveButton.addActionListener(e -> savePlayResult(bookingResource));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        addPlayResult.add(saveButton, gbc);

        JButton backButton = new JButton("Back to Reservation Menu");
        backButton.addActionListener(e -> {
            dispose();
            new Reservation(bookingResource);
        });
        gbc.gridy = 7;
        addPlayResult.add(backButton, gbc);
    }

    private void savePlayResult(BookingResource bookingResource) {
        try {
            // bookingId는 기존 예약과 연결되는 값이며, 실제 존재 여부는 BookingResource에서 확인합니다.
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
        // 힌트 수와 남은 시간은 음수가 될 수 없으므로 공통 검증 메서드로 처리합니다.
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

    private void addRow(String labelText, Component component, int row, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.25;
        addPlayResult.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.75;
        addPlayResult.add(component, gbc);
    }
}
