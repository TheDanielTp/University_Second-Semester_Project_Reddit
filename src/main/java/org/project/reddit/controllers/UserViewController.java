package org.project.reddit.controllers;

import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.control.ListView;
import org.project.reddit.classes.Post;
import org.project.reddit.classes.Message;
import org.project.reddit.classes.Subreddit;
import org.project.reddit.classes.User;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class UserViewController
{
    //region Objects

    public User user;

    @FXML
    public VBox userPane;

    @FXML
    private TextField messageText;

    @FXML
    public Label usernameText;

    @FXML
    public Label karmaCount;

    @FXML
    public ListView <String> subredditList;

    //endregion

    //region Layout Functions

    @FXML
    void refreshUser ()
    {
        this.karmaCount.setText (String.valueOf (this.user.getKarma ()));
        this.userPane.getChildren ().remove (2, this.userPane.getChildren ().size ());

        for (Post post : this.user.getPostList ())
        {
            this.userPane.getChildren ().add (getPostLayout (post));
        }

        this.subredditList.getItems ().clear ();
        for (Subreddit subReddit : this.user.getSubRedditList ())
        {
            this.subredditList.getItems ().add (subReddit.getTopic ());
        }
    }

    Node getPostLayout (Post post)
    {
        FXMLLoader loader = new FXMLLoader (getClass ().getResource ("/org/project/reddit/PostView.fxml"));
        try
        {
            Node node = loader.load ();

            PostViewController controller = loader.getController ();
            controller.post = post;

            controller.usernameText.setText (post.getUser ().getUsername ());
            controller.karmaCount.setText (String.valueOf (post.getKarma ()));
            controller.dateTimeText.setText (post.getDateTime ());
            controller.topicText.setText (post.getTitle ());
            controller.textBody.setText (post.getText ());

            controller.userViewController = this;
            if (MainViewController.user == null)
            {
                controller.likeButton.setVisible (false);
                controller.dislikeButton.setVisible (false);
                controller.deleteButton.setVisible (false);
                controller.editButton.setVisible (false);
                controller.saveButton.setVisible (false);
            }
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

    @FXML
    void sendMessage (ActionEvent event)
    {
        Message message = new Message (MainViewController.user, messageText.getText ());
        this.user.addMessage (message);
        messageText.clear ();
    }

    //endregion
}