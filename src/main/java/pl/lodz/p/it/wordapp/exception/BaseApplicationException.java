package pl.lodz.p.it.wordapp.exception;

public abstract class BaseApplicationException extends Exception {
    public BaseApplicationException(String message) {
        super(message);
    }

    public static final String EMAIL_ADDRESS_ALREADY_TAKEN = "This email address is already taken";
    public static final String EMAIL_ADDRESS_NOT_DIFFERENT_FORM_OLD = "New email address must differ from the old email address";
    public static final String INVALID_EMAIL_ADDRESS = "Invalid email address";
    public static final String INVALID_PASSWORD = "Invalid password";
    public static final String LEARNING_SET_ACCESS_FORBIDDEN = "You have no access to this set";
    public static final String LEARNING_SET_DELETION_ACCESS_FORBIDDEN = "You have insufficient permissions to delete this set";
    public static final String LEARNING_SET_ITEM_MODIFICATION_ACCESS_FORBIDDEN = "You have insufficient permissions to edit items in this set";
    public static final String LEARNING_SET_ITEM_NOT_FOUND = "Item with id=%d does not exist in set with id=%d";
    public static final String LEARNING_SET_NOT_FOUND = "Learning set with id=%d does not exist";
    public static final String LEARNING_SET_USERS_PERMISSION_ACCESS_FORBIDDEN = "Only set owner can access user permissions";
    public static final String NEW_PASSWORD_NOT_DIFFERENT = "New password must differ from the old password!";
    public static final String LEARNING_SET_PERMISSION_MANAGEMENT_ACCESS_FORBIDDEN = "Only set owner can edit permissions";
    public static final String PERMISSIONS_SELF_MANAGEMENT_ERROR = "You cannot edit your own permissions";
    public static final String USERNAME_ALREADY_TAKEN = "This username is already taken";
    public static final String USER_NOT_FOUND = "User with id=%d could not be found";
}
