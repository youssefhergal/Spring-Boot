package org.youssefhergal.my_app_ws.responses;

public enum ErrorMessages {

    MISSING_REQUIRED_FIELDS("Missing required field."),
    RECORD_ALREADY_EXISTS("Record already exists"),
    INTERNAL_SERVER_ERROR("Internal server error"),
    RECORD_NOT_FOUND("Record not found");


    private String message;

    private ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
