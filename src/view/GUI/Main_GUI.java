package view.GUI;

import controller.BookingResource;
import model.Reservation.Reservation;
import view.GUI.BUILDINGS.Buildings;
import view.GUI.ROOMS.Rooms;
import view.GUI.Reservation.ReservationBID;
import view.GUI.Reservation.ViewResID;
import view.GUI.USER.User;
import view.ViewHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class Main_GUI extends JFrame implements Runnable {

    private boolean isGUIView;
    private JButton ADDDELVIEWUsersButton;
    private JButton BUILDINGSButton;
    private JButton RESERVATIONSButton;
    private JPanel main_GUI;
    private JButton VIEWRESERVATIONSBYBOOKINGButton;
    private JButton VIEWBOOKEDROOMSButton;
    private JButton VIEWRESERVATIONSBYEMAILButton;
    private JButton SAVEDATAButton;
    private JButton LOADDATAButton;
    private JButton EXITButton;
    private JButton ROOMSButton1;
    private JButton STATISTICSButton;

    public Main_GUI(BookingResource bookingResource) {
        this.isGUIView = true;
        setContentPane(this.main_GUI);
        setTitle("방탈출 예약 관리 시스템");
        setSize(620, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        ADDDELVIEWUsersButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new User(bookingResource);
            }
        });
        SAVEDATAButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JOptionPane.showMessageDialog(main_GUI, bookingResource.saveAllData());
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(main_GUI, ex.getLocalizedMessage());
                }
            }
        });
        LOADDATAButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JOptionPane.showMessageDialog(main_GUI, bookingResource.loadAllData());
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(main_GUI, ex.getLocalizedMessage());
                }
            }
        });
        EXITButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                setGUIView(false);
                if (ViewHandler.isCommandView) {
                    System.exit(0);
                } else {
                    ViewHandler.isGUIView = true;
                    dispose();
                }
                JOptionPane.showMessageDialog(main_GUI, "프로그램을 종료합니다.");
            }

        });

        BUILDINGSButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (promptAdmin(bookingResource, "지점 관리는 관리자 권한이 필요합니다.")) {
                    dispose();
                    new Buildings(bookingResource);
                }
            }
        });
        ROOMSButton1.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (promptAdmin(bookingResource, "테마 관리는 관리자 권한이 필요합니다.")) {
                    dispose();
                    new Rooms(bookingResource);
                }
            }
        });
        RESERVATIONSButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new view.GUI.Reservation.Reservation(bookingResource);
            }
        });
        VIEWRESERVATIONSBYBOOKINGButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ReservationBID(bookingResource);
            }
        });
        VIEWBOOKEDROOMSButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!promptAdmin(bookingResource, "전체 예약 현황 조회는 관리자 권한이 필요합니다.")) {
                    return;
                }
                try {
                    Map<String, Reservation> allRooms = bookingResource.bookedRooms();
                    for (Map.Entry<String, Reservation> entry : allRooms.entrySet()) {
                        JOptionPane.showMessageDialog(main_GUI, "예약된 테마 :\n" + "예약 번호: " + entry.getValue().getBookingID() + "\n테마명: " + entry.getValue().getRoom() + "\n고객 이메일: " + entry.getValue().getEmail() + "\n예약 인원: " + entry.getValue().getPlayerCount() + "명\n총 가격: " + entry.getValue().getTotalPrice() + "원\n예약 상태: " + entry.getValue().getStatusDisplayName() + "\n이용 시간: " + entry.getValue().getCheckInDate() + ":" + entry.getValue().getCheckInTime() + " ~ " + entry.getValue().getCheckOutDate() + ":" + entry.getValue().getCheckOutTime());
                    }
                } catch (IllegalArgumentException ex) {
                    String error = ex.getLocalizedMessage();
                    JOptionPane.showMessageDialog(main_GUI, error);
                }
            }
        });
        VIEWRESERVATIONSBYEMAILButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ViewResID(bookingResource);
                try {

                } catch (IllegalArgumentException ex) {
                    String error = ex.getLocalizedMessage();
                    JOptionPane.showMessageDialog(main_GUI, error);
                }
            }
        });
        STATISTICSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (promptAdmin(bookingResource, "운영 통계 조회는 관리자 권한이 필요합니다.")) {
                    dispose();
                    new StatisticsGUI(bookingResource);
                }
            }
        });
    }


    private boolean promptAdmin(BookingResource bookingResource, String message) {
        String email = JOptionPane.showInputDialog(main_GUI, message + "\n관리자 이메일을 입력하세요.");
        if (email == null) {
            return false;
        }
        try {
            bookingResource.requireAdmin(email.trim());
            return true;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(main_GUI, ex.getLocalizedMessage());
            return false;
        }
    }

    public boolean isGUIView() {
        return isGUIView;
    }

    public void setGUIView(boolean GUIView) {
        isGUIView = GUIView;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        main_GUI = new JPanel();
        main_GUI.setLayout(new GridBagLayout());
        ADDDELVIEWUsersButton = new JButton();
        ADDDELVIEWUsersButton.setText("1. 고객 관리");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        main_GUI.add(ADDDELVIEWUsersButton, gbc);
        BUILDINGSButton = new JButton();
        BUILDINGSButton.setText("2. 지점 관리");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        main_GUI.add(BUILDINGSButton, gbc);
        RESERVATIONSButton = new JButton();
        RESERVATIONSButton.setText("4. 예약 및 플레이 결과 관리");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        main_GUI.add(RESERVATIONSButton, gbc);
        final JLabel label1 = new JLabel();
        label1.setFont(new Font(label1.getFont().getName(), Font.BOLD, 20));
        label1.setText("방탈출 예약 관리 시스템");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        main_GUI.add(label1, gbc);
        VIEWRESERVATIONSBYBOOKINGButton = new JButton();
        VIEWRESERVATIONSBYBOOKINGButton.setText("5. 예약 번호로 예약 조회");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        main_GUI.add(VIEWRESERVATIONSBYBOOKINGButton, gbc);
        VIEWBOOKEDROOMSButton = new JButton();
        VIEWBOOKEDROOMSButton.setText("6. 예약된 테마 조회");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        main_GUI.add(VIEWBOOKEDROOMSButton, gbc);
        VIEWRESERVATIONSBYEMAILButton = new JButton();
        VIEWRESERVATIONSBYEMAILButton.setText("7. 고객 이메일로 예약 조회");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        main_GUI.add(VIEWRESERVATIONSBYEMAILButton, gbc);
        SAVEDATAButton = new JButton();
        SAVEDATAButton.setText("8. 데이터 저장");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        main_GUI.add(SAVEDATAButton, gbc);
        LOADDATAButton = new JButton();
        LOADDATAButton.setText("9. 데이터 불러오기");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        main_GUI.add(LOADDATAButton, gbc);
        EXITButton = new JButton();
        EXITButton.setText("종료");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        main_GUI.add(EXITButton, gbc);
        ROOMSButton1 = new JButton();
        ROOMSButton1.setText("3. 테마 관리");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        main_GUI.add(ROOMSButton1, gbc);
        STATISTICSButton = new JButton();
        STATISTICSButton.setText("10. 운영 통계");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        main_GUI.add(STATISTICSButton, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return main_GUI;
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

    }
}
