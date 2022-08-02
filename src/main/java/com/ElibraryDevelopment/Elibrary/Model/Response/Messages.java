package com.ElibraryDevelopment.Elibrary.Model.Response;

public enum Messages {
    MISSING_REQUIRED_FIELD("Required fields missing"),
    RECORD_ALREADY_EXISTS("Verified account already exists for this email, please use a different email."),
    NO_RECORD_FOUND("Account with provided id not found"),
    FAILED_DB_SAVE("Saving to Database failed! please try again."),
    TOKEN_NOT_FOUND("Given token is corrupted"),
    EMAIL_TOKEN_EXPIRED("Token expired! Try to generate a new email verification link."),
    PASSWORD_TOKEN_EXPIRED("Token expired! Try to generate new passwword reset link."),
    DELETE_SUCCESS("Account deleted successfully."),
    DEACTIVATE_SUCCESS("Account Deactivated successfully."),
    ACTIVATE_SUCCESS("Account activated successfully."),
    COULD_NOT_ACTIVATE("Account activation failed."),
    COULD_NOT_DEACTIVATE_RECORD("Account Deactivation failed."),
    COULD_NOT_DELETE_RECORD("Could not delete account"),
    EMAIL_NOT_FOUND("Account with given Email not found,Please check email again!"),
    EMAIL_SENT("Email successfully sent,Please check your email account."),
    EMAIL_NOT_SENT("Email not sent."),
    EMAIL_ADDRESS_NOT_VERIFIED("Email address could not be verified"),
    EMAIL_ADDRESS_VERIFIED("Email Authentication Successfull"),
    EMAIL_VERIFICATION_INCOMPLETE("Please log in to your email to complete registration."),
    PASSWORD_NOT_MATCHING("Entered passwords are different!"),
    PASSWORD_NOT_CHANGED("Password could not be changed."),
    PASSWORD_CHANGED("Password Successfully changed."),
    EMAIL_SUCCESS("email sent succesfully");


    private String Message;

    Messages(String Message) {
        this.Message = Message;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
