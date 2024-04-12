package org.project.reddit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.project.reddit.classes.Subreddit;
import org.project.reddit.classes.User;

import java.io.IOException;

public class Main extends Application
{
    public static void main (String[] args)
    {
        User.signUp ("prof.danial4@gmail.com", "TheDanielTp", "Tdtp3148_P");
        User      user      = User.findUserViaUsername ("TheDanielTp");
        Subreddit subReddit = new Subreddit ("AskReddit", "Ask Us anything", user);
        user.createPost ("Title1", "Contents", subReddit);
        user.createPost ("Title2", "Contents", subReddit);
        user.createPost ("Title3", "Contents", subReddit);

        System.out.println ("> program is running");
        launch (args);
    }

    @Override
    public void start (Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader (Main.class.getResource ("GuestView.fxml"));

        Scene scene = new Scene (fxmlLoader.load (), 960, 540);

        stage.getIcons ().add (new Image ("org/project/reddit/pictures/icon.png"));
        stage.setTitle ("Reddit");
        stage.setScene (scene);
        stage.show ();
    }
}
