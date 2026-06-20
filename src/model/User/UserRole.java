package model.User;

/**
 * 사용자 권한을 표현하는 Enum입니다.
 */
public enum UserRole {
    CUSTOMER("고객"),
    ADMIN("관리자");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
