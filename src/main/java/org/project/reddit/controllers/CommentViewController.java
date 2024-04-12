package org.project.reddit.controllers;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import org.project.reddit.classes.Comment;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.fxml.FXML;

public class CommentViewController
{
    //region Objects

    public PostViewController controller;

    public Comment comment;

    @FXML
    public AnchorPane commentPane;

    @FXML
    public Label usernameText;

    @FXML
    public Label dateTimeText;

    @FXML
    public Label commentContents;

    @FXML
    public Label karmaCount;

    @FXML
    public Button editButton;

    @FXML
    public Button deleteButton;

    int clickCount = 0;

    //endregion

    //region Comment Functions

    @FXML
    void voteDownComment ()
    {
        MainViewController.user.downVote (this.comment);
        this.karmaCount.setText (String.valueOf (this.comment.getKarma ()));
    }

    @FXML
    void voteUpComment ()
    {
        MainViewController.user.upVote (this.comment);
        this.karmaCount.setText (String.valueOf (this.comment.getKarma ()));
    }

    @FXML
    void deleteComment ()
    {
        MainViewController.user.removeComment (this.comment, this.comment.getPost ());
        controller.refreshComments ();
    }

    @FXML
    void editComment ()
    {
        if (clickCount > 0)
        {
            System.out.println ("> still in editing progress");
            return;
        }
        clickCount++;

        String    oldText = this.commentContents.getText ();
        TextField newText = new TextField (oldText);

        this.commentPane.getChildren ().remove (this.commentContents);
        this.commentPane.getChildren ().add (newText);

        newText.setLayoutX (14);
        newText.setLayoutY (37);
        newText.setOnKeyPressed (keyEvent ->
        {
            if (keyEvent.getCode () == KeyCode.ENTER)
            {
                MainViewController.user.editCommentText (this.comment, newText.getText ());
                this.commentContents.setText (newText.getText ());
                this.commentPane.getChildren ().remove (newText);
                this.commentPane.getChildren ().add (this.commentContents);
                clickCount--;
            }
        });
    }

    //endregion
}
