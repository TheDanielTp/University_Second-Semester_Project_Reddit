package org.project.reddit.classes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Post
{
    //region Objects

    private static final ArrayList <Post> postList = new ArrayList <> ();

    private final ArrayList <Comment> commentList = new ArrayList <> ();
    private final ArrayList <String>  tagList     = new ArrayList <> ();

    private final Subreddit subreddit;
    private final String    dateTime;
    private final String    title;
    private final User      user;

    private String text;
    private int    karma;

    //endregion

    //region Initialize Functions

    public Post (String title, String text, Subreddit subreddit, User user)
    {
        this.dateTime = getDateTime (LocalDateTime.now ());

        this.subreddit = subreddit;

        this.karma = 0;
        this.title = title;
        this.user  = user;
        this.text  = text;

        postList.add (this);
    }

    public Post (ArrayList <String> tagList, String title, String text, Subreddit subreddit, User user)
    {
        this.dateTime = getDateTime (LocalDateTime.now ());

        this.tagList.addAll (tagList);
        this.subreddit = subreddit;

        this.karma = 0;
        this.title = title;
        this.user  = user;
        this.text  = text;

        postList.add (this);
    }

    private String getDateTime (LocalDateTime dateTime)
    {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern ("yyyy-MM-dd HH:mm");
        return dateTime.format (myFormatObj);
    }

    //endregion

    //region Post Functions

    public void addComment (Comment comment)
    {
        this.commentList.add (comment);
        System.out.println ("> comment added successfully");
    }

    public void removeComment (Comment comment)
    {
        this.commentList.remove (comment);
        System.out.println ("> comment removed successfully");
    }

    public void changeText (String newText)
    {
        this.text = newText;
        System.out.println ("> text changed successfully");
    }

    public void increaseKarma ()
    {
        this.karma++;
        System.out.println ("> increased post's karma");
    }

    public void decreaseKarma ()
    {
        this.karma--;
        System.out.println ("> decreased post's karma");
    }

    //endregion

    //region Get-Info Functions

    public static ArrayList <Post> getPostList ()
    {
        return postList;
    }

    public ArrayList <Comment> getCommentList ()
    {
        return new ArrayList <> (commentList);
    }

    public ArrayList <String> getTagList ()
    {
        return new ArrayList <> (tagList);
    }

    public Subreddit getSubreddit ()
    {
        return subreddit;
    }

    public String getDateTime ()
    {
        return this.dateTime;
    }

    public String getTitle ()
    {
        return title;
    }

    public String getText ()
    {
        return text;
    }

    public User getUser ()
    {
        return user;
    }

    public int getKarma ()
    {
        return this.karma;
    }

    //endregion
}