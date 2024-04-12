package org.project.reddit.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import org.project.reddit.classes.DataManager;
import org.project.reddit.classes.User;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.util.Objects;
import java.io.IOException;

public class SignupViewController
{
    //region Objects

    @FXML
    private TextField emailText;

    @FXML
    private TextField usernameText;

    @FXML
    private PasswordField passwordText;

    @FXML
    private Label emailValidity;

    @FXML
    private Label usernameValidity;

    @FXML
    private Label passwordValidity;

    //endregion

    //region Buttons Functions

    @FXML
    void cancelSignUp (ActionEvent event) throws IOException
    {
        Parent root  = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("/org/project/reddit/GuestView.fxml")));
        Stage  stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
        Scene  scene = new Scene (root);

        stage.setScene (scene);
        stage.setResizable (false);
        System.out.println ("> opening main panel");
    }

    @FXML
    void logIn (ActionEvent event) throws IOException
    {
        Parent root  = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("/org/project/reddit/LoginView.fxml")));
        Stage  stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
        Scene  scene = new Scene (root);

        stage.setScene (scene);
        stage.setResizable (false);
        System.out.println ("> opening login page");
    }

    @FXML
    void signUp (ActionEvent event) throws IOException
    {
        this.emailValidity.setText ("");
        this.usernameValidity.setText ("");
        this.passwordValidity.setText ("");

        if (User.validateEmail (this.emailText.getText ()) == 0)
        {
            this.emailValidity.setText ("Invalid email. Please fix your email to continue");
            return;
        }
        if (User.validateEmail (this.emailText.getText ()) == 2)
        {
            this.emailValidity.setText ("This email is already taken. Please provide another email or log in");
            return;
        }
        if (User.validateUsername (this.usernameText.getText ()) == 0)
        {
            this.usernameValidity.setText ("Invalid username. Username must be between 3 and 20 characters");
            return;
        }
        if (User.validateUsername (this.usernameText.getText ()) == 2)
        {
            this.emailValidity.setText ("This username is already taken. Please provide another username or log in");
            return;
        }
        if (User.validatePassword (this.passwordText.getText ()) == 0)
        {
            this.passwordValidity.setText ("Password must be at least 8 characters long, contain one numeral, one special, and one uppercase");
            return;
        }
        User.signUp (this.emailText.getText (), this.usernameText.getText (), this.passwordText.getText ());
        User user = User.findUserViaUsername (this.usernameText.getText ());

        MainViewController.user    = user;
        ProfileViewController.user = user;

        Parent root  = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("/org/project/reddit/MainView.fxml")));
        Stage  stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
        Scene  scene = new Scene (root);

        DataManager.saveData ();

        stage.setScene (scene);
        stage.setResizable (false);
        System.out.println ("> opening user panel");
    }

    //endregion
}