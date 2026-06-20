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
    private final JTextField playerCountField;

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
        playerCountField = new JTextField(24);

        setContentPane(this.addReservation);
        setTitle("테마 예약");
        setSize(660, 620);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        buildForm(bookingResource);
        setVisible(true);
    }

    private void buildForm(BookingResource bookingResource) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("테마 예약 정보 입력");
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        addReservation.add(title, gbc);
        gbc.gridwidth = 1;

        addRow("예약 번호", bookingIdField, 1, gbc);
        addRow("고객 이메일", emailField, 2, gbc);
        addRow("지점명", branchField, 3, gbc);
        addRow("테마명", themeField, 4, gbc);
        addRow("예약 시작 날짜(dd/mm/yyyy)", checkInDateField, 5, gbc);
        addRow("예약 종료 날짜(dd/mm/yyyy)", checkOutDateField, 6, gbc);
        addRow("시작 시간(HH:mm)", checkInTimeField, 7, gbc);
        addRow("종료 시간(HH:mm)", checkOutTimeField, 8, gbc);
        addRow("예약 인원", playerCountField, 9, gbc);

        JButton okButton = new JButton("예약 저장");
        okButton.addActionListener(e -> saveReservation(bookingResource));
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        addReservation.add(okButton, gbc);

        JButton backButton = new JButton("예약 메뉴로 돌아가기");
        backButton.addActionListener(e -> {
            dispose();
            new Reservation(bookingResource);
        });
        gbc.gridy = 11;
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
        String playerCountText = playerCountField.getText().trim();
        try {
            int playerCount = parsePlayerCount(playerCountText);
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
            bookingResource.addReservation(bookingID, emailID, buildingName, roomName, checkInDate, checkOutDate, checkInTime, checkOutTime, isBooked, playerCount);
            bookingResource.updateRoom(roomName, buildingName, isBooked);
            JOptionPane.showMessageDialog(addReservation, "예약이 완료되었습니다.");
        } catch (IllegalArgumentException | ParseException ex) {
            JOptionPane.showMessageDialog(addReservation, ex.getLocalizedMessage());
        }
    }

    private int parsePlayerCount(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("예약 인원은 숫자로 입력해주세요.");
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
