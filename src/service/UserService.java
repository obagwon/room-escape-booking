package service;

import model.User.User;
import model.User.UserRole;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * User Service Layer interacts when called by the Controller from the View.
 *
 * @author 220031985
 */
public class UserService implements Serializable {
    private static final String DATA_FILE = "userData.txt";

    private Map<String, User> user = new HashMap<>();


    /**
     * Constructor UserService.
     */
    public UserService() {
    }

    /**
     * Saving the User Data.
     *
     * @throws IOException throws IOException.
     */
    public void userSave() throws IOException {
        try (ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            o.writeObject(user);
        }
    }

    /**
     * Load the User Data.
     */
    public boolean userLoad() throws IOException, ClassNotFoundException {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return false;
        }
        try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(file))) {
            user = (Map<String, User>) oi.readObject();
            return true;
        }
    }


    /**
     * Adding the User to the System.
     *
     * @param email EmailID.
     * @param name  Name.
     */
    public void addUser(String email, String name) {
        addUser(email, name, UserRole.CUSTOMER);
    }

    public void addAdmin(String email, String name) {
        addUser(email, name, UserRole.ADMIN);
    }

    public void addUser(String email, String name, UserRole role) {
        if (user.containsKey(email)) {
            throw new IllegalArgumentException("이미 등록된 고객입니다.\n");
        } else if (name.isEmpty()) {
            throw new IllegalArgumentException("고객 이름을 입력해주세요.\n");
        } else {
            user.put(email, new User(name, email, role));
        }
    }


    /**
     * Deleting the User.
     *
     * @param email Email_ID.
     */
    public void delUser(String emailID) {
        String email = emailID.trim();
        if (email.isEmpty()) {
            throw new IllegalArgumentException("고객 이메일을 입력해주세요.");
        } else if (user.isEmpty()) {
            throw new IllegalArgumentException("등록된 고객이 없습니다.\n");
        } else if (user.containsKey(email)) {
            user.remove(email);
        } else {
            throw new IllegalArgumentException("등록되지 않은 고객입니다.\n");
        }
    }


    /**
     * For test purposes
     *
     * @return Size of the User Map.
     */
    public int getUsersCount() {
        return user.size();

    }

    /**
     * Viewing all users.
     */
    public Map<String, User> viewUsers() {
        Map<String, User> viewUsers = new HashMap<>();
        if (user.isEmpty()) {
            throw new IllegalArgumentException("등록된 고객이 없습니다.\n");
        } else {
            viewUsers.putAll(user);
            return viewUsers;
            // System.out.println(user.keySet() +"\n");

        }
    }

    /**
     * Check if user exists or not.
     *
     * @param email Email ID.
     * @return true if exists.
     */
    public boolean checkUser(String email) {
        if (user.containsKey(email)) {
            return true;
        } else {
            throw new IllegalArgumentException("등록되지 않은 고객입니다.\n");
        }
    }

    public boolean checkAdmin(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("관리자 이메일을 입력해주세요.");
        }
        User target = user.get(email.trim());
        if (target == null) {
            throw new IllegalArgumentException("등록되지 않은 관리자 계정입니다.");
        } else if (!target.isAdmin()) {
            throw new IllegalArgumentException("관리자 권한이 필요한 기능입니다.");
        }
        return true;
    }


}




