package org.project.reddit.controllers;

import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import org.project.reddit.classes.Post;
import org.project.reddit.classes.Subreddit;
import org.project.reddit.classes.User;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.fxml.FXML;

import java.util.Arrays;
import java.io.IOException;
import java.util.ArrayList;

public class SubredditViewController
{
    //region Objects

    public Subreddit subReddit;

    @FXML
    public VBox subredditPane;

    @FXML
    public Label topicText;

    @FXML
    public Label memberCount;

    @FXML
    public Label dateText;

    @FXML
    public Label invalidAlert;

    @FXML
    public Label descriptionText;

    @FXML
    public ListView <String> memberList;

    @FXML
    public AnchorPane createPostPane;

    @FXML
    public Button joinButton;

    @FXML
    public Button leaveButton;

    @FXML
    public TextField postTitleText;

    @FXML
    public TextField tagsText;

    @FXML
    public TextArea postContentsText;

    //endregion

    //region Layout Functions

    @FXML
    void refreshAll ()
    {
        this.descriptionText.setText (subReddit.getDescription ());
        this.memberCount.setText ("Members: " + subReddit.getMemberCount ());

        if (MainViewController.user == null)
        {
            this.subredditPane.getChildren ().remove (2, this.subredditPane.getChildren ().size ());
        }
        else
        {
            this.subredditPane.getChildren ().remove (3, this.subredditPane.getChildren ().size ());
        }
        for (Post post : this.subReddit.getPostList ())
        {
            this.subredditPane.getChildren ().add (getPostLayout (post));
        }
        refreshMembers ();
    }

    void refreshMembers ()
    {
        this.memberList.getItems ().clear ();
        for (User user : this.subReddit.getMemberList ())
        {
            String userText = user.getUsername ();
            if (subReddit.getAdminList ().contains (user))
            {
                userText += " - Admin";
            }
            this.memberList.getItems ().add (userText);
        }
        ContextMenu contextMenu = new ContextMenu ();

        MenuItem promoteToAdmin = new MenuItem ("Promote to Admin");
        MenuItem removeMember   = new MenuItem ("Remove Member");

        contextMenu.getItems ().add (promoteToAdmin);
        contextMenu.getItems ().add (removeMember);

        memberList.setContextMenu (contextMenu);
        memberList.getSelectionModel ().selectedItemProperty ().addListener ((observableValue, s, t1) ->
        {
            String username = memberList.getSelectionModel ().getSelectedItem ();
            removeMember.setOnAction (e -> deleteItem (username));
            promoteToAdmin.setOnAction (e -> setAdmin (username));
        });
    }

    Node getPostLayout (Post post)
    {
        FXMLLoader loader = new FXMLLoader (getClass ().getResource ("/org/project/reddit/PostView.fxml"));
        try
        {
            Node               node       = loader.load ();

            PostViewController controller = loader.getController ();
            controller.post = post;

            controller.usernameText.setText (post.getUser ().getUsername ());
            controller.karmaCount.setText (String.valueOf (post.getKarma ()));
            controller.dateTimeText.setText (post.getDateTime ());
            controller.topicText.setText (post.getTitle ());
            controller.textBody.setText (post.getText ());

            if (! post.getTagList ().isEmpty ())
            {
                StringBuilder tags = new StringBuilder ();
                for (String tag : post.getTagList ())
                {
                    tags.append ("#").append (tag).append (" ");
                }
                controller.tagsText.setText (tags.toString ());
            }
            else
            {
                controller.tagsText.setVisible (false);
            }
            return node;
        }
        catch (IOException e)
        {
            throw new RuntimeException ();
        }
    }

    //endregion

    //region Buttons Functions

    private void setAdmin (String username)
    {
        if (username.endsWith (" - Admin"))
        {
            System.out.println ("> selected user is already admin");
            return;
        }
        User user = User.findUserViaUsername (username);
        MainViewController.user.addAdmin (user, this.subReddit);
        refreshAll ();
    }

    @FXML
    void createPost ()
    {
        this.invalidAlert.setText (null);
        if (this.postTitleText.getText () == null)
        {
            this.invalidAlert.setText ("Please enter a title");
            return;
        }
        if (this.postContentsText.getText () == null)
        {
            this.invalidAlert.setText ("Please enter contents");
            return;
        }
        if (this.tagsText.getText () == null)
        {
            MainViewController.user.createPost (this.postTitleText.getText (), this.postContentsText.getText (), this.subReddit);
        }
        else
        {
            ArrayList <String> tagList = new ArrayList <> (Arrays.asList (tagsText.getText ().split (" ")));
            MainViewController.user.createPost (tagList, this.postTitleText.getText (), this.postContentsText.getText (), this.subReddit);
            this.tagsText.clear ();
        }
        this.postTitleText.clear ();
        this.postContentsText.clear ();

        refreshAll ();
    }

    @FXML
    void joinSubreddit ()
    {
        MainViewController.user.joinSubReddit (this.subReddit);
        this.joinButton.setVisible (false);
        this.leaveButton.setVisible (true);
        refreshAll ();
    }

    @FXML
    void leaveSubreddit ()
    {
        MainViewController.user.leaveSubReddit (this.subReddit);
        this.joinButton.setVisible (true);
        this.leaveButton.setVisible (false);
        refreshAll ();
    }

    private void deleteItem (String item)
    {
        if (item.endsWith (" - Admin"))
        {
            item = item.replaceAll (" - Admin", "");
        }
        User user = User.findUserViaUsername (item);

        MainViewController.user.removeMember (user, this.subReddit);
        refreshAll ();
    }

    //endregion
}