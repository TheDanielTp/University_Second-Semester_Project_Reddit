package org.project.reddit.classes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message
{
    //region Objects

    private final String message;
    private final String dateTime;
    private final User   user;

    //endregion

    //region Initialize Functions

    public Message (User user, String message)
    {
        this.message  = message;
        this.dateTime = getDateTime (LocalDateTime.now ());
        this.user     = user;
    }

    private String getDateTime (LocalDateTime dateTime)
    {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern ("yyyy-MM-dd HH:mm");
        return dateTime.format (myFormatObj);
    }

    //endregion

    //region Get-Info Functions

    public User getUser ()
    {
        return user;
    }

    public String getMessage ()
    {
        return message;
    }

    public String getDateTime ()
    {
        return dateTime;
    }

    //endregion
}