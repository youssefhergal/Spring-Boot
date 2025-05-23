package org.youssefhergal.my_app_ws.responses;

import java.util.Date;

public class ErrorMessage {

    private Date timestamp;
    private String message;


    public ErrorMessage(Date date, String message) {
        super();
        this.timestamp = date;
        this.message = message;
    }
}
