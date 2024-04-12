package org.project.reddit.controllers;

import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import org.project.reddit.classes.Comment;
import org.project.reddit.classes.Post;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;

import javafx.scene.layout.VBox;

import java.io.IOException;

public class GuestPostViewController extends PostViewController
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
    public TextArea newCommentText;

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

    //endregion

    //region Post Functions

    @FXML
    void savePost (ActionEvent event)
    {
        Alert alert = new Alert (Alert.AlertType.INFORMATION);
        alert.setTitle ("Guest");
        alert.setHeaderText ("You're not logged in!");
        alert.setContentText ("Please log in first to save posts.");
        alert.showAndWait ();
    }

    @FXML
    void upVotePost ()
    {
        Alert alert = new Alert (Alert.AlertType.INFORMATION);
        alert.setTitle ("Guest");
        alert.setHeaderText ("You're not logged in!");
        alert.setContentText ("Please log in first to up vote posts.");
        alert.showAndWait ();
    }

    @FXML
    void downVotePost ()
    {
        Alert alert = new Alert (Alert.AlertType.INFORMATION);
        alert.setTitle ("Guest");
        alert.setHeaderText ("You're not logged in!");
        alert.setContentText ("Please log in first to down vote posts.");
        alert.showAndWait ();
    }

    @FXML
    void sendComment ()
    {
        Alert alert = new Alert (Alert.AlertType.INFORMATION);
        alert.setTitle ("Guest");
        alert.setHeaderText ("You're not logged in!");
        alert.setContentText ("Please log in first to send comment posts.");
        alert.showAndWait ();
    }

    @FXML
    void deletePost ()
    {
        Alert alert = new Alert (Alert.AlertType.INFORMATION);
        alert.setTitle ("Guest");
        alert.setHeaderText ("You're not logged in!");
        alert.setContentText ("Please log in first to delete posts.");
        alert.showAndWait ();
    }

    @FXML
    void editPost ()
    {
        Alert alert = new Alert (Alert.AlertType.INFORMATION);
        alert.setTitle ("Guest");
        alert.setHeaderText ("You're not logged in!");
        alert.setContentText ("Please log in first to edit posts.");
        alert.showAndWait ();
    }

    //endregion

    //region Comment Functions

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
