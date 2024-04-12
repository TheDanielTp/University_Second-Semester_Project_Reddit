package org.project.reddit.controllers;

import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import org.project.reddit.classes.Message;
import org.project.reddit.classes.Subreddit;
import org.project.reddit.classes.User;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.Objects;
import java.io.IOException;
import java.util.ResourceBundle;

public class ProfileViewController implements Initializable
{
    //region Objects

    public static int  timesClicked;
    public static User user;

    @FXML
    private Label emailText;

    @FXML
    private Label usernameText;

    @FXML
    private Label karmaCount;

    @FXML
    private Button styleButton;

    @FXML
    private TextField newUsernameText;

    @FXML
    private TextField newEmailText;

    @FXML
    private PasswordField oldPasswordText;

    @FXML
    private PasswordField newPasswordText;

    @FXML
    private ListView <String> subredditList;

    @FXML
    private ListView <String> messageList;

    //endregion

    //region Layout Functions

    @Override
    public void initialize (URL arg0, ResourceBundle arg1)
    {
        this.emailText.setText (user.getEmail ());
        this.usernameText.setText (user.getUsername ());
        this.karmaCount.setText ("Karma: " + user.getKarma ());
        this.styleButton.setText ("Style your Avatar?");

        for (Subreddit subReddit : user.getSubRedditList ())
        {
            String label = "";
            label += subReddit.getTopic () + " ";

            if (subReddit.getAdminList ().contains (user))
            {
                label += "(admin) ";
            }

            label += subReddit.getDateTime ();
            this.subredditList.getItems ().add (label);
        }
        for (Message message : user.getMessageList ())
        {
            String messageUser = message.getUser ().getUsername ();
            String messageText = message.getMessage ();
            String messageDate = message.getDateTime ();

            String fullMessage = "u/" + messageUser + ": " + messageText + " Date: " + messageDate;
            this.messageList.getItems ().add (fullMessage);
        }
        System.out.println ("> initialized profile page");
    }

    //endregion

    //region Buttons Functions

    @FXML
    void changeButtonText ()
    {
        timesClicked++;
        if (timesClicked == 1)
        {
            this.styleButton.setText ("You can't!");
        }
        else if (timesClicked == 2)
        {
            this.styleButton.setText ("lmao");
        }
        else if (timesClicked == 3)
        {
            this.styleButton.setText ("It's not implemented");
        }
        else if (timesClicked == 4)
        {
            this.styleButton.setText ("Stop clicking brother");
        }
        else if (timesClicked == 5)
        {
            this.styleButton.setText ("It's not fun anymore");
        }
        else if (timesClicked == 6)
        {
            this.styleButton.setText ("That's it. I'm leaving");
        }
        else if (timesClicked == 7)
        {
            this.styleButton.setText ("You think I'm joking?");
        }
        else if (timesClicked == 8)
        {
            this.styleButton.setText ("Seriously, stop");
        }
        else if (timesClicked == 9)
        {
            this.styleButton.setText ("One more and I'll leave");
        }
        else if (timesClicked >= 10)
        {
            this.styleButton.setText ("Fuck You");
            this.styleButton.setVisible (false);
        }
    }

    @FXML
    void updateEmail ()
    {
        if (User.validateEmail (this.newEmailText.getText ()) == 2)
        {
            Alert alert = new Alert (Alert.AlertType.ERROR);

            alert.setTitle ("Invalid email");
            alert.setHeaderText ("This email is already taken");
            alert.setContentText ("Change email address and try again");

            if (alert.showAndWait ().get () == ButtonType.OK)
            {
                return;
            }
        }
        if (User.validateEmail (this.newEmailText.getText ()) == 0)
        {
            Alert alert = new Alert (Alert.AlertType.ERROR);

            alert.setTitle ("Invalid email");
            alert.setHeaderText ("Invalid email");
            alert.setContentText ("Change email address and try again");

            if (alert.showAndWait ().get () == ButtonType.OK)
            {
                return;
            }
        }
        user.changeEmail (this.newEmailText.getText ());

        Alert alert = new Alert (Alert.AlertType.INFORMATION);

        alert.setTitle ("Update email");
        alert.setHeaderText ("Updated successfully");
        alert.setContentText ("Your email was successfully changed");

        if (alert.showAndWait ().get () == ButtonType.OK)
        {
            this.emailText.setText (this.newEmailText.getText ());
            this.newEmailText.clear ();
        }
        System.out.println ("> email address updated successfully");
    }

    @FXML
    void updateUsername ()
    {
        if (User.validateUsername (this.newUsernameText.getText ()) == 2)
        {
            Alert alert = new Alert (Alert.AlertType.ERROR);

            alert.setTitle ("Invalid username");
            alert.setHeaderText ("Username is already used or not valid");
            alert.setContentText ("Change username and try again");

            if (alert.showAndWait ().get () == ButtonType.OK)
            {
                return;
            }
        }
        if (User.validateUsername (this.newUsernameText.getText ()) == 0)
        {
            Alert alert = new Alert (Alert.AlertType.ERROR);

            alert.setTitle ("Invalid username");
            alert.setHeaderText ("Invalid username");
            alert.setContentText ("Change username and try again");

            if (alert.showAndWait ().get () == ButtonType.OK)
            {
                return;
            }
        }
        user.changeUsername (this.newUsernameText.getText ());

        Alert alert = new Alert (Alert.AlertType.INFORMATION);

        alert.setTitle ("Update username");
        alert.setHeaderText ("Updated successfully");
        alert.setContentText ("Your username was successfully changed");

        if (alert.showAndWait ().get () == ButtonType.OK)
        {
            this.usernameText.setText (this.newUsernameText.getText ());
            this.newUsernameText.clear ();
        }
        System.out.println ("> username updated successfully");
    }

    @FXML
    void updatePassword ()
    {
        if (user.checkPassword (this.oldPasswordText.getText ()))
        {
            Alert alert = new Alert (Alert.AlertType.ERROR);

            alert.setTitle ("Wrong password");
            alert.setHeaderText ("Old password do not match");
            alert.setHeaderText ("Enter the old password again");

            if (alert.showAndWait ().get () == ButtonType.OK)
            {
                return;
            }
        }
        if (User.validatePassword (this.newPasswordText.getText ()) == 0)
        {
            Alert alert = new Alert (Alert.AlertType.ERROR);

            alert.setTitle ("Invalid password");
            alert.setHeaderText ("Password is not valid");
            alert.setContentText ("Password must be at least 8 characters long, contain one numeral, one special, and one uppercase");

            if (alert.showAndWait ().get () == ButtonType.OK)
            {
                return;
            }
        }
        user.changePassword (this.newPasswordText.getText ());

        Alert alert = new Alert (Alert.AlertType.INFORMATION);

        alert.setTitle ("Update password");
        alert.setHeaderText ("Updated successfully");
        alert.setContentText ("Your password was successfully changed");

        if (alert.showAndWait ().get () == ButtonType.OK)
        {
            this.oldPasswordText.clear ();
            this.newPasswordText.clear ();
        }
        System.out.println ("> password updated successfully");
    }

    @FXML
    void backToMenu (ActionEvent event) throws IOException
    {
        timesClicked = 0;

        Parent root  = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("/org/project/reddit/MainView.fxml")));
        Stage  stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
        Scene  scene = new Scene (root);

        stage.setScene (scene);
        stage.setResizable (false);
        System.out.println ("> opening user panel");
    }

    //endregion
}