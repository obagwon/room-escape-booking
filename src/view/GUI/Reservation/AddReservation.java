package view.GUI.Reservation;

import controller.BookingResource;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;

/**
 * 방탈출 테마 예약을 입력하는 Swing 화면입니다.
 *
 * <p>발표 시에는 이 화면에서 입력값을 받은 뒤 BookingResource를 통해
 * 사용자/지점/테마 존재 여부, 날짜/시간 형식, 예약 중복 여부를 검증하는 흐름을 설명하면 됩니다.
 */
public class AddReservation extends JFrame {
    private final JPanel addReservation;
    private final JTextField bookingIdField;
    private final JTextField emailField;
    private final JTextField branchField;
    private final JTextField themeField;
    private final JTextField checkInDateField;
    private final JTextField checkOutDateField;
    private final JTextField checkInTimeField;
    private final JTextField checkOutTimeField;

    public AddReservation(BookingResource bookingResource) {
        addReservation = new JPanel(new GridBagLayout());
        bookingIdField = new JTextField(24);
        emailField = new JTextField(24);
        branchField = new JTextField(24);
        themeField = new JTextField(24);
        checkInDateField = new JTextField(24);
        checkOutDateField = new JTextField(24);
        checkInTimeField = new JTextField(24);
        checkOutTimeField = new JTextField(24);

        setContentPane(this.addReservation);
        setTitle("Escape Room Manager - Book Theme");
        setSize(660, 620);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        buildForm(bookingResource);
        setVisible(true);
    }

    private void buildForm(BookingResource bookingResource) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Theme Reservation Entry");
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        addReservation.add(title, gbc);
        gbc.gridwidth = 1;

        addRow("Booking ID", bookingIdField, 1, gbc);
        addRow("Customer Email", emailField, 2, gbc);
        addRow("Cafe Branch Name", branchField, 3, gbc);
        addRow("Escape Theme Name", themeField, 4, gbc);
        addRow("Book-In Date (dd/mm/yyyy)", checkInDateField, 5, gbc);
        addRow("Book-Out Date (dd/mm/yyyy)", checkOutDateField, 6, gbc);
        addRow("Start Time (HH:mm)", checkInTimeField, 7, gbc);
        addRow("End Time (HH:mm)", checkOutTimeField, 8, gbc);

        JButton okButton = new JButton("Save Reservation");
        okButton.addActionListener(e -> saveReservation(bookingResource));
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        addReservation.add(okButton, gbc);

        JButton backButton = new JButton("Back to Reservation Menu");
        backButton.addActionListener(e -> {
            dispose();
            new Reservation(bookingResource);
        });
        gbc.gridy = 10;
        addReservation.add(backButton, gbc);
    }

    private void saveReservation(BookingResource bookingResource) {
        // 화면 입력값을 읽어 Controller에 전달하기 전 앞뒤 공백을 제거합니다.
        String bookingID = bookingIdField.getText().trim();
        String emailID = emailField.getText().trim();
        String buildingName = branchField.getText().trim();
        String roomName = themeField.getText().trim();
        String checkInDate = checkInDateField.getText().trim();
        String checkOutDate = checkOutDateField.getText().trim();
        String checkInTime = checkInTimeField.getText().trim();
        String checkOutTime = checkOutTimeField.getText().trim();
        try {
            // 검증 순서가 발표 포인트입니다: ID 중복 → 고객/지점/테마 존재 → 날짜/시간 → 중복 예약.
            bookingResource.checkID(bookingID);
            bookingResource.checkUser(emailID);
            bookingResource.checkBuilding(buildingName);
            bookingResource.checkRoom(roomName);
            bookingResource.isDateValid(checkInDate);
            bookingResource.isDateValid(checkOutDate);
            bookingResource.validateTime(checkInTime);
            bookingResource.validateTime(checkOutTime);
            bookingResource.isBeforeTime(checkInTime, checkOutTime);
            bookingResource.LeastBy5(checkInTime, checkOutTime);
            bookingResource.checkDay(checkInDate, checkOutDate);
            bookingResource.checkOverlap(checkInTime, checkOutTime, roomName, checkInDate);
            boolean isBooked = true;
            bookingResource.updateRoom(roomName, buildingName, isBooked);
            bookingResource.addReservation(bookingID, emailID, buildingName, roomName, checkInDate, checkOutDate, checkInTime, checkOutTime, isBooked);
            JOptionPane.showMessageDialog(addReservation, "Reservation completed successfully.");
        } catch (IllegalArgumentException | ParseException ex) {
            JOptionPane.showMessageDialog(addReservation, ex.getLocalizedMessage());
        }
    }

    private void addRow(String labelText, Component component, int row, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.25;
        addReservation.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.75;
        addReservation.add(component, gbc);
    }
}
