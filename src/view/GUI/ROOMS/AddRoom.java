package view.GUI.ROOMS;

import controller.BookingResource;

import javax.swing.*;
import java.awt.*;

public class AddRoom extends JFrame {
    private JPanel addRoom;
    private JTextField emailField;
    private JTextField branchField;
    private JTextField themeNameField;
    private JTextField genreField;
    private JTextField difficultyField;
    private JTextField durationField;
    private JTextField minPlayersField;
    private JTextField maxPlayersField;
    private JTextField priceField;

    public AddRoom(BookingResource bookingResource) {
        addRoom = new JPanel(new GridBagLayout());
        emailField = new JTextField(24);
        branchField = new JTextField(24);
        themeNameField = new JTextField(24);
        genreField = new JTextField(24);
        difficultyField = new JTextField(24);
        durationField = new JTextField(24);
        minPlayersField = new JTextField(24);
        maxPlayersField = new JTextField(24);
        priceField = new JTextField(24);

        setContentPane(addRoom);
        setTitle("테마 추가");
        setSize(620, 620);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        buildForm(bookingResource);
        setVisible(true);
    }

    private void buildForm(BookingResource bookingResource) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("방탈출 테마 추가");
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        addRoom.add(title, gbc);
        gbc.gridwidth = 1;

        addRow("고객 이메일", emailField, 1, gbc);
        addRow("지점명", branchField, 2, gbc);
        addRow("테마명", themeNameField, 3, gbc);
        addRow("장르", genreField, 4, gbc);
        addRow("난이도(EASY/NORMAL/HARD)", difficultyField, 5, gbc);
        addRow("플레이 시간(분)", durationField, 6, gbc);
        addRow("최소 인원", minPlayersField, 7, gbc);
        addRow("최대 인원", maxPlayersField, 8, gbc);
        addRow("1인 가격", priceField, 9, gbc);

        JButton okButton = new JButton("확인");
        okButton.addActionListener(e -> saveTheme(bookingResource));
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        addRoom.add(okButton, gbc);

        JButton backButton = new JButton("뒤로");
        backButton.addActionListener(e -> {
            dispose();
            new Rooms(bookingResource);
        });
        gbc.gridy = 11;
        addRoom.add(backButton, gbc);
    }

    private void addRow(String labelText, JComponent field, int row, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        addRoom.add(label, gbc);
        gbc.gridx = 1;
        addRoom.add(field, gbc);
    }

    private void saveTheme(BookingResource bookingResource) {
        try {
            String emailID = emailField.getText().trim();
            String buildingName = branchField.getText().trim();
            String roomName = themeNameField.getText().trim();
            String genre = genreField.getText().trim();
            String difficulty = difficultyField.getText().trim();
            int durationMinutes = parseInt(durationField.getText().trim(), "플레이 시간");
            int minPlayers = parseInt(minPlayersField.getText().trim(), "최소 인원");
            int maxPlayers = parseInt(maxPlayersField.getText().trim(), "최대 인원");
            int pricePerPerson = parseInt(priceField.getText().trim(), "1인 가격");

            bookingResource.checkUser(emailID);
            bookingResource.checkBuilding(buildingName);
            bookingResource.addRoom(buildingName, roomName, false, genre, difficulty, durationMinutes, minPlayers, maxPlayers, pricePerPerson);
            JOptionPane.showMessageDialog(addRoom, "테마가 성공적으로 저장되었습니다.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(addRoom, ex.getLocalizedMessage());
        }
    }

    private int parseInt(String value, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + "은(는) 숫자로 입력해주세요.");
        }
    }

    public JComponent $$$getRootComponent$$$() {
        return addRoom;
    }
}
