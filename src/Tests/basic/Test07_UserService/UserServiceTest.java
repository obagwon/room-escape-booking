package Tests.basic.Test07_UserService;

import model.User.User;
import model.User.UserRole;
import org.junit.Test;
import service.UserService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class UserServiceTest {

    UserService userService = new UserService();

    User user = new User("alpha", "a@gmail.com");

    @Test
    public void addUser() {
        String expected = "alpha";
        String actual = user.getName();
        assertEquals(expected, actual);
        userService.addUser("a@gmail.com", "alpha");
        assertEquals(1, userService.getUsersCount());

    }

    @Test
    public void delUser() {
        userService.addUser("a@gmail.com", "alpha");
        userService.addUser("b@gmail.com", "beta");
        assertEquals(2, userService.getUsersCount());
        userService.delUser("a@gmail.com");
        assertEquals(1, userService.getUsersCount());

    }


    @Test
    public void checkUser() {
        userService.addUser("a@gmail.com", "alpha");
        boolean check = userService.checkUser("a@gmail.com");
        assertTrue(check);

    }

    @Test
    public void addUserRejectsInvalidEmail() {
        UserService localUserService = new UserService();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> localUserService.addUser("invalid-email", "invalid"));
        assertTrue(exception.getMessage().contains("이메일 형식이 올바르지 않습니다."));
    }

    @Test
    public void addUserRejectsDuplicateEmail() {
        UserService localUserService = new UserService();
        localUserService.addUser("duplicate@gmail.com", "alpha");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> localUserService.addUser("duplicate@gmail.com", "beta"));
        assertTrue(exception.getMessage().contains("이미 등록된 고객입니다."));
    }

    @Test
    public void addAdminAndCheckAdmin() {
        userService.addAdmin("manager@escaperoom.local", "manager");
        assertEquals(UserRole.ADMIN, userService.viewUsers().get("manager@escaperoom.local").getRole());
        assertTrue(userService.checkAdmin("manager@escaperoom.local"));
    }

    @Test
    public void checkAdminRejectsCustomer() {
        userService.addUser("customer@gmail.com", "customer");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.checkAdmin("customer@gmail.com"));
        assertTrue(exception.getMessage().contains("관리자 권한이 필요한 기능입니다."));
    }
}
