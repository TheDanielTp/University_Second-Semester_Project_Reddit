package org.project.reddit.classes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Subreddit
{
    //region Objects

    private static final ArrayList <Subreddit> subredditList = new ArrayList <> ();

    private final ArrayList <User> memberList = new ArrayList <> ();
    private final ArrayList <Post> postList   = new ArrayList <> ();
    private final ArrayList <User> adminList  = new ArrayList <> ();

    private final String dateTime;
    private final String description;
    private final String topic;

    private int memberCount;

    //endregion

    //region Initialize Functions

    public Subreddit (String topic, String description, User admin)
    {
        this.dateTime = getDateTime (LocalDateTime.now ());

        this.description = description;
        this.memberCount = 0;
        this.topic       = topic;

        this.adminList.add (admin);
        subredditList.add (this);
    }

    private String getDateTime (LocalDateTime dateTime)
    {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern ("yyyy-MM-dd HH:mm");
        return dateTime.format (myFormatObj);
    }

    //endregion

    //region Subreddit Functions

    public static Subreddit findSubreddit (String topic)
    {
        for (Subreddit subreddit : subredditList)
        {
            if (subreddit.topic.equals (topic))
            {
                System.out.println ("> subreddit found");
                return subreddit;
            }
        }
        System.out.println ("> subreddit not found");
        return null;
    }

    public void addMember (User user)
    {
        this.memberList.add (user);
        this.memberCount++;
        System.out.println ("> member added successfully");
    }

    public void removeMember (User user)
    {
        this.memberList.remove (user);
        this.adminList.remove (user);
        this.memberCount--;
        System.out.println ("> member removed successfully");
    }

    public void addPost (Post post)
    {
        this.postList.add (post);
        for (User user : this.memberList)
        {
            user.addPostToTimeline (post);
        }
        System.out.println ("> post created successfully");
    }

    public void removePost (Post post)
    {
        this.postList.remove (post);
        for (User user : this.memberList)
        {
            user.removePostFromTimeline (post);
        }
        System.out.println ("> post removed successfully");
    }

    public void addAdmin (User admin)
    {
        if (adminList.contains (admin))
        {
            System.out.println ("> user is already an admin");
            return;
        }
        this.adminList.add (admin);
        System.out.println ("> admin added successfully");
    }

    //endregion

    //region Get-Info Functions

    public static ArrayList <Subreddit> getSubredditList ()
    {
        return new ArrayList <> (subredditList);
    }

    public ArrayList <User> getMemberList ()
    {
        return new ArrayList <> (this.memberList);
    }

    public ArrayList <User> getAdminList ()
    {
        return new ArrayList <> (this.adminList);
    }

    public ArrayList <Post> getPostList ()
    {
        return new ArrayList <> (this.postList);
    }

    public String getDescription ()
    {
        return description;
    }

    public int getMemberCount ()
    {
        return this.memberCount;
    }

    public String getDateTime ()
    {
        return this.dateTime;
    }

    public String getTopic ()
    {
        return this.topic;
    }

    //endregion
}
