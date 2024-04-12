package org.project.reddit.classes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Comment
{
    //region Objects

    private final Post   post;
    private final String dateTime;
    private final User   user;

    private String text;
    private int    karma;

    //endregion

    //region Initialize Functions

    public Comment (String text, Post post, User user)
    {
        this.dateTime = getDateTime (LocalDateTime.now ());

        this.text  = text;
        this.post  = post;
        this.user  = user;
        this.karma = 0;
    }

    private String getDateTime (LocalDateTime dateTime)
    {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern ("yyyy-MM-dd HH:mm");
        return dateTime.format (myFormatObj);
    }

    //endregion

    //region Comment Functions

    public void changeText (String newText)
    {
        this.text = newText;
        System.out.println ("> comment edited successfully");
    }

    public void increaseKarma ()
    {
        this.karma++;
        System.out.println (">  increased comment's karma");
    }

    public void decreaseKarma ()
    {
        this.karma--;
        System.out.println ("> decreased comment's karma");
    }

    //endregion

    //region Get-Info Functions

    public String getDateTime ()
    {
        return this.dateTime;
    }

    public String getText ()
    {
        return this.text;
    }

    public Post getPost ()
    {
        return this.post;
    }

    public User getUser ()
    {
        return this.user;
    }

    public int getKarma ()
    {
        return this.karma;
    }

    //endregion
}