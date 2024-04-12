package org.project.reddit.classes;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.UUID;

public class User
{
    //region Objects

    private static ArrayList <User> userList = new ArrayList <> ();

    private final ArrayList <Comment> commentList          = new ArrayList <> ();
    private final ArrayList <Comment> upVotedCommentList   = new ArrayList <> ();
    private final ArrayList <Comment> downVotedCommentList = new ArrayList <> ();

    private final ArrayList <Subreddit> subredditList = new ArrayList <> ();

    private final ArrayList <Post> downVotedPostList = new ArrayList <> ();
    private final ArrayList <Post> timelinePostList  = new ArrayList <> ();
    private final ArrayList <Post> upVotedPostList   = new ArrayList <> ();
    private final ArrayList <Post> savedPostList     = new ArrayList <> ();
    private final ArrayList <Post> postList          = new ArrayList <> ();

    private final UUID id;

    private ArrayList <Message> messageList;

    private String username;
    private String password;
    private String email;
    private int    karma;

    //endregion

    //region Initialize Functions

    public User (String email, String username, String password)
    {
        this.id = generateId ();

        this.email    = email;
        this.username = username;
        this.karma    = 0;

        this.password    = DigestUtils.sha256Hex (password);
        this.messageList = new ArrayList <> ();
    }

    private static UUID generateId ()
    {
        UUID id = UUID.randomUUID ();
        while (findUserViaId (id) != null)
        {
            id = UUID.randomUUID ();
        }
        System.out.println ("> uuid generated successfully");
        return id;
    }

    public static void setUserList (ArrayList <User> userList)
    {
        User.userList = userList;
    }

    //endregion

    //region User Functions

    public void increaseKarma ()
    {
        this.karma++;
        System.out.println ("> karma increased");
    }

    public void decreaseKarma ()
    {
        this.karma--;
        System.out.println ("> karma decreased");
    }

    public static User findUserViaUsername (String username)
    {
        for (User user : userList)
        {
            if (user.username.equals (username))
            {
                System.out.println ("> username found");
                return user;
            }
        }
        System.out.println ("> username not found");
        return null;
    }

    public static User findUserViaId (UUID id)
    {
        for (User user : userList)
        {
            if (user.id.equals (id))
            {
                System.out.println ("> user id found");
                return user;
            }
        }
        System.out.println ("> user id not found");
        return null;
    }

    public static User findUserViaEmail (String email)
    {
        for (User user : userList)
        {
            if (user.email.equals (email))
            {
                System.out.println ("> email found");
                return user;
            }
        }
        System.out.println ("> email not found");
        return null;
    }

    public void addMessage (Message message)
    {
        messageList.add (message);
    }

    //endregion

    //region Authorization Functions

    public static void signUp (String email, String username, String password)
    {
        if (validateEmail (email) == 0)
        {
            System.out.println ("> invalid email");
            return;
        }
        if (validateEmail (email) == 2)
        {
            System.out.println ("> email already taken");
            return;
        }
        if (validateUsername (username) == 0)
        {
            System.out.println ("> invalid username");
            return;
        }
        if (validateUsername (username) == 2)
        {
            System.out.println ("> username already taken");
            return;
        }
        if (validatePassword (password) == 0)
        {
            System.out.println ("> invalid password");
            return;
        }
        User user = new User (email, username, password);
        userList.add (user);
        System.out.println ("> sign up successful");
    }

    public static int validateEmail (String email)
    {
        String regex = "^[a-zA-Z0-9_+.&*-]+(?:\\.[a-zA-Z0-9_+.&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile (regex);
        Matcher matcher = pattern.matcher (email);

        if (! matcher.find ())
        {
            return 0;
        }

        if (userList.isEmpty ())
        {
            return 1;
        }
        else
        {
            for (User user : userList)
            {
                if (email.equalsIgnoreCase (user.email))
                {
                    return 2;
                }
            }
        }
        return 1;
    }

    public static int validateUsername (String username)
    {
        if (username.length () < 3 || username.length () > 20)
        {
            return 0;
        }

        if (User.userList.isEmpty ())
        {
            return 1;
        }

        for (User user : userList)
        {
            if (username.equalsIgnoreCase (user.username))
            {
                return 2;
            }
        }
        return 1;
    }

    public static int validatePassword (String password)
    {

        String regex = "^(.{0,7}|[^0-9]*|[^A-Z]*|[^a-z]*|[a-zA-Z0-9]*)$";

        Pattern pattern = Pattern.compile (regex);
        Matcher matcher = pattern.matcher (password);

        if (matcher.find ())
        {
            return 0;
        }
        return 1;
    }

    public boolean checkPassword (String password)
    {
        password = DigestUtils.sha256Hex (password);
        return password.equals (this.password);
    }

    //endregion

    //region Account Functions

    public void changeEmail (String newEmail)
    {
        if (validateEmail (newEmail) == 1)
        {
            this.email = newEmail;
            System.out.println ("> email updated successfully");
        }
    }

    public void changeUsername (String newUsername)
    {
        if (validateUsername (newUsername) == 1)
        {
            this.username = newUsername;
            System.out.println ("> username updated successfully");
        }
    }

    public void changePassword (String newPassword)
    {
        if (validatePassword (newPassword) == 1)
        {
            this.password = DigestUtils.sha256Hex (newPassword);
            System.out.println ("> password updated successfully");
        }
    }

    //endregion

    //region Subreddit Functions

    public void joinSubReddit (Subreddit subReddit)
    {
        this.subredditList.add (subReddit);
        subReddit.addMember (this);
        System.out.println ("> user joined subreddit");
    }

    public void leaveSubReddit (Subreddit subReddit)
    {
        this.subredditList.remove (subReddit);
        subReddit.removeMember (this);
        System.out.println ("> user left subreddit");
    }

    public void createSubReddit (String topic, String description)
    {
        Subreddit subReddit = new Subreddit (topic, description, this);
        this.subredditList.add (subReddit);
        subReddit.addMember (this);
        subReddit.addAdmin (this);
        System.out.println ("> subreddit created successfully");
    }

    public void removeMember (User user, Subreddit subreddit)
    {
        if (user == this)
        {
            System.out.println ("> you can't remove yourself");
            return;
        }
        if (! subreddit.getAdminList ().contains (this))
        {
            System.out.println ("> you don't have permisson to remove a user");
            return;
        }
        if (! subreddit.getMemberList ().contains (user))
        {
            System.out.println ("> selected user isn't a member of this subreddit");
            return;
        }
        user.leaveSubReddit (subreddit);
        System.out.println ("> user removed successfully");
    }

    public void addAdmin (User user, Subreddit subReddit)
    {
        if (user == this)
        {
            System.out.println ("> you can't promote yourself to admin");
            return;
        }
        if (! subReddit.getMemberList ().contains (user))
        {
            System.out.println ("> selected user isn't a member of this subreddit");
            return;
        }
        if (! subReddit.getAdminList ().contains (this))
        {
            System.out.println ("> you don't have permisson to promote a user to admin");
            return;
        }
        if (subReddit.getAdminList ().contains (user))
        {
            System.out.println ("> selected user is already admin");
            return;
        }
        subReddit.addAdmin (user);
        System.out.println ("> user promoted to admin successfully");
    }

    //endregion

    //region Post Functions

    public void createPost (ArrayList <String> tags, String title, String text, Subreddit subReddit)
    {
        Post post = new Post (tags, title, text, subReddit, this);
        this.postList.add (post);
        subReddit.addPost (post);
    }

    public void createPost (String title, String text, Subreddit subReddit)
    {
        Post post = new Post (title, text, subReddit, this);
        this.postList.add (post);
        subReddit.addPost (post);
    }

    public void upVote (Post post)
    {
        if (this.upVotedPostList.contains (post))
        {
            System.out.println ("> user already up voted this post");
            return;
        }
        if (this.downVotedPostList.contains (post))
        {
            this.downVotedPostList.remove (post);
            post.increaseKarma ();
            post.getUser ().increaseKarma ();
        }
        this.upVotedPostList.add (post);
        post.increaseKarma ();
        post.getUser ().increaseKarma ();
    }

    public void downVote (Post post)
    {
        if (this.downVotedPostList.contains (post))
        {
            System.out.println ("> user already down voted this post");
            return;
        }
        if (this.upVotedPostList.contains (post))
        {
            this.upVotedPostList.remove (post);
            post.decreaseKarma ();
            post.getUser ().decreaseKarma ();
        }
        this.downVotedPostList.add (post);
        post.decreaseKarma ();
        post.getUser ().decreaseKarma ();
    }

    public void addPostToTimeline (Post post)
    {
        this.timelinePostList.add (post);
        System.out.println ("> post add to timeline successfully");
    }

    public void removePostFromTimeline (Post post)
    {
        this.timelinePostList.remove (post);
        System.out.println ("> post removed from timeline successfully");
    }

    public void removePost (Post post, Subreddit subreddit)
    {
        if (! subreddit.getPostList ().contains (post))
        {
            System.out.println ("> selected post does not exist");
            return;
        }
        if (! subreddit.getAdminList ().contains (this) && ! this.postList.contains (post))
        {
            System.out.println ("> you don't have permisson to remove a post");
            return;
        }
        this.postList.remove (post);
        this.savedPostList.remove (post);
        subreddit.removePost (post);
    }

    public void savePost (Post post)
    {
        if (this.savedPostList.contains (post))
        {
            System.out.println ("> post is already saved");
            return;
        }
        this.savedPostList.add (post);
        System.out.println ("> post saved successfully");
    }

    public void unsavePost (Post post)
    {
        if (! this.savedPostList.contains (post))
        {
            System.out.println ("> post is not saved");
            return;
        }
        this.savedPostList.remove (post);
        System.out.println ("> post unsaved successfully");
    }

    public void editPostText (Post post, String newText)
    {
        if (! this.postList.contains (post))
        {
            System.out.println ("> you don't have permisson to edit this post");
            return;
        }
        post.changeText (newText);
        System.out.println ("> post edited successfully");
    }

    //endregion

    //region Comment Functions

    public void createComment (String text, Post post)
    {
        Comment comment = new Comment (text, post, this);
        this.commentList.add (comment);
        post.addComment (comment);
        System.out.println ("> comment created successfully");
    }

    public void upVote (Comment comment)
    {
        if (this.upVotedCommentList.contains (comment))
        {
            System.out.println ("> user already up voted this comment");
            return;
        }
        if (this.downVotedCommentList.contains (comment))
        {
            this.downVotedCommentList.remove (comment);
            comment.increaseKarma ();
            comment.getUser ().increaseKarma ();
        }
        this.upVotedCommentList.add (comment);
        comment.increaseKarma ();
        comment.getUser ().increaseKarma ();
    }

    public void downVote (Comment comment)
    {
        if (this.downVotedCommentList.contains (comment))
        {
            System.out.println ("> user already down voted this comment");
            return;
        }
        if (this.upVotedCommentList.contains (comment))
        {
            this.upVotedCommentList.remove (comment);
            comment.decreaseKarma ();
            comment.getUser ().decreaseKarma ();
        }
        this.downVotedCommentList.add (comment);
        comment.decreaseKarma ();
        comment.getUser ().decreaseKarma ();
    }

    public void removeComment (Comment comment, Post post)
    {
        if (! post.getCommentList ().contains (comment))
        {
            System.out.println ("> selected comment doesn't exist");
            return;
        }
        if (! post.getSubreddit ().getAdminList ().contains (this) && ! commentList.contains (comment))
        {
            System.out.println ("> you don't have permisson to remove a comment");
            return;
        }
        this.commentList.remove (comment);
        post.removeComment (comment);
    }

    public void editCommentText (Comment comment, String newText)
    {
        if (! this.commentList.contains (comment))
        {
            System.out.println ("> you don't have permisson to edit a comment");
            return;
        }
        comment.changeText (newText);
    }

    //endregion

    //region Get-Info Functions

    public static ArrayList <User> getUserList ()
    {
        return userList;
    }

    public ArrayList <Subreddit> getSubRedditList ()
    {
        return new ArrayList <> (this.subredditList);
    }

    public ArrayList <Post> getTimelinePostList ()
    {
        return new ArrayList <> (this.timelinePostList);
    }

    public ArrayList <Message> getMessageList ()
    {
        return messageList;
    }

    public ArrayList <Post> getSavedPostList ()
    {
        return new ArrayList <> (savedPostList);
    }

    public ArrayList <Post> getPostList ()
    {
        return new ArrayList <> (this.postList);
    }

    public String getUsername ()
    {
        return this.username;
    }

    public String getEmail ()
    {
        return this.email;
    }

    public int getKarma ()
    {
        return this.karma;
    }

    //endregion
}
