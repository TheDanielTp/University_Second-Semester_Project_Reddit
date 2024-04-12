package org.project.reddit.controllers;

import javafx.fxml.FXML;
import java.util.Objects;
import javafx.scene.Parent;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import org.project.reddit.classes.User;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;

public class LoginViewController
{
    //region Objects

    @FXML
    private Label usernameValidity;

    @FXML
    private Label passwordValidity;

    @FXML
    private TextField usernameText;

    @FXML
    private PasswordField passwordText;

    //endregion

    //region Buttons Functions

    @FXML
    void cancelLogIn (ActionEvent event) throws IOException
    {
        Parent root  = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("/org/project/reddit/GuestView.fxml")));
        Stage  stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
        Scene  scene = new Scene (root);

        stage.setScene (scene);
        stage.setResizable (false);

        System.out.println ("> returned to main panel");
    }

    @FXML
    void logIn (ActionEvent event) throws IOException
    {
        User user = User.findUserViaUsername (this.usernameText.getText ());
        if (user == null)
        {
            this.usernameValidity.setText ("Invalid Username");
            return;
        }
        if (! user.checkPassword (this.passwordText.getText ()))
        {
            this.passwordValidity.setText ("Incorrect Password");
            return;
        }
        MainViewController.user    = user;
        ProfileViewController.user = user;

        Parent root  = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("/org/project/reddit/MainView.fxml")));
        Stage  stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
        Scene  scene = new Scene (root);

        stage.setScene (scene);
        stage.setResizable (false);
        System.out.println ("> opening user panel");
    }

    @FXML
    void signUp (ActionEvent event) throws IOException
    {
        Parent root  = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("/org/project/reddit/SignupView.fxml")));
        Stage  stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
        Scene  scene = new Scene (root);

        stage.setScene (scene);
        stage.setResizable (false);
        System.out.println ("> opening signup page");
    }

    //endregion
}