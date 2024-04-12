package org.project.reddit.controllers;

import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.project.reddit.classes.Comment;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.KeyCode;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;

import javafx.scene.layout.VBox;

import org.project.reddit.classes.Post;

import java.io.IOException;

public class PostViewController
{
    //region Objects

    public Post post;

    public MainViewController mainViewController;

    public UserViewController userViewController;

    @FXML
    public AnchorPane postPane;

    @FXML
    public Label usernameText;

    @FXML
    public Label karmaCount;

    @FXML
    public Label dateTimeText;

    @FXML
    public Label topicText;

    @FXML
    public Label textBody;

    @FXML
    public Label tagsText;

    @FXML
    public Image userInputImage;

    @FXML
    public Button deleteButton;

    @FXML
    public Button editButton;

    @FXML
    public Button saveButton;

    @FXML
    public Button likeButton;

    @FXML
    public Button dislikeButton;

    @FXML
    public VBox commentBox;

    @FXML
    public TextArea newCommentText;

    int clickCount = 0;

    //endregion

    //region Post Functions

    @FXML
    void savePost (ActionEvent event)
    {
        if (! MainViewController.user.getSavedPostList ().contains (this.post))
        {
            MainViewController.user.savePost (this.post);
        }
        else
        {
            MainViewController.user.unsavePost (this.post);
        }
        if (mainViewController != null)
        {
            mainViewController.refreshAll ();
        }
        if (userViewController != null)
        {
            userViewController.refreshUser ();
        }

    }

    @FXML
    void upVotePost ()
    {
        MainViewController.user.upVote (this.post);
        karmaCount.setText (String.valueOf (this.post.getKarma ()));

        if (userViewController != null)
        {
            userViewController.karmaCount.setText (String.valueOf (userViewController.user.getKarma ()));
        }
    }

    @FXML
    void downVotePost ()
    {
        MainViewController.user.downVote (this.post);
        karmaCount.setText (String.valueOf (this.post.getKarma ()));

        if (userViewController != null)
        {
            userViewController.karmaCount.setText (String.valueOf (userViewController.user.getKarma ()));
        }
    }

    @FXML
    void deletePost ()
    {
        MainViewController.user.removePost (this.post, this.post.getSubreddit ());
        if (mainViewController != null)
        {
            mainViewController.refreshAll ();
        }
        if (userViewController != null)
        {
            userViewController.refreshUser ();
        }
    }

    @FXML
    void editPost ()
    {
        if (clickCount > 0)
        {
            System.out.println ("> still in editing progress");
            return;
        }
        clickCount++;

        String   oldText = this.textBody.getText ();
        TextArea newText = new TextArea (oldText);

        this.postPane.getChildren ().remove (this.textBody);
        this.postPane.getChildren ().add (newText);

        newText.setLayoutX (22);
        newText.setLayoutY (65);
        newText.setPrefWidth (516);
        newText.setPrefHeight (20);

        newText.setOnKeyPressed (keyEvent ->
        {
            if (keyEvent.getCode () == KeyCode.ENTER)
            {
                MainViewController.user.editPostText (this.post, newText.getText ());

                this.textBody.setText (newText.getText ());
                this.postPane.getChildren ().remove (newText);
                this.postPane.getChildren ().add (this.textBody);

                clickCount--;
            }
        });
    }

    //endregion

    //region Comment Functions

    @FXML
    void sendComment ()
    {
        MainViewController.user.createComment (this.newCommentText.getText (), this.post);
        this.newCommentText.clear ();
        this.refreshComments ();
    }

    @FXML
    public void refreshComments ()
    {
        this.commentBox.getChildren ().remove (1, this.commentBox.getChildren ().size ());
        int size = this.post.getCommentList ().size ();

        Comment[] postComments = new Comment[size];
        for (int i = size - 1; i >= 0; i--)
        {
            postComments[i] = this.post.getCommentList ().get (i);
        }
        for (Comment comment : postComments)
        {
            this.commentBox.getChildren ().add (getCommentLayout (comment));
        }
    }

    Node getCommentLayout (Comment comment)
    {
        FXMLLoader loader = new FXMLLoader (getClass ().getResource ("/org/project/reddit/CommentView.fxml"));
        try
        {
            Node node = loader.load ();

            CommentViewController controller = loader.getController ();
            controller.comment = comment;

            controller.usernameText.setText (comment.getUser ().getUsername ());
            controller.commentContents.setText (comment.getText ());
            controller.dateTimeText.setText (comment.getDateTime ());
            controller.karmaCount.setText (String.valueOf (comment.getKarma ()));

            controller.controller = this;
            if (! comment.getUser ().equals (MainViewController.user))
            {
                controller.editButton.setVisible (false);
            }
            if (! comment.getUser ().equals (MainViewController.user) && ! comment.getPost ().getSubreddit ().getAdminList ().contains (MainViewController.user))
            {
                controller.deleteButton.setVisible (false);
            }
            return node;
        }
        catch (IOException e)
        {
            throw new RuntimeException (e);
        }
    }

    //endregion
}
