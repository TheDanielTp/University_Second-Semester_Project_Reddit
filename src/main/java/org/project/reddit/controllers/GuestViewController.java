package org.project.reddit.controllers;

import javafx.scene.layout.VBox;
import org.project.reddit.classes.Subreddit;
import org.project.reddit.classes.Post;
import org.project.reddit.classes.User;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.Node;

import java.net.URL;
import java.util.Objects;
import java.io.IOException;
import java.util.ResourceBundle;

public class GuestViewController implements Initializable
{
    //region Objects

    @FXML
    private TextField searchText;

    @FXML
    private VBox postBox;

    @FXML
    private TabPane tabsPane;

    //endregion

    //region Layout Functions

    @Override
    public void initialize (URL arg0, ResourceBundle arg1)
    {
        refreshTimeline ();
        System.out.println ("> initializing main page");
    }

    void refreshTimeline ()
    {
        if (! postBox.getChildren ().isEmpty ())
        {
            postBox.getChildren ().remove (1, postBox.getChildren ().size ());
        }

        int size = Post.getPostList ().size ();

        Post[] timelinePosts = new Post[size];
        if (size >= 10)
        {
            for (int i = size - 1; i >= size - 10; i--)
            {
                timelinePosts[i] = Post.getPostList ().get (i);
            }
        }
        else
        {
            for (int i = size - 1; i >= 0; i--)
            {
                timelinePosts[i] = Post.getPostList ().get (i);
            }
        }

        for (Post post : timelinePosts)
        {
            this.postBox.getChildren ().add (getPostLayout (post));
        }
    }

    Node getPostLayout (Post post)
    {
        FXMLLoader loader = new FXMLLoader (getClass ().getResource ("/org/project/reddit/GuestPostView.fxml"));
        try
        {
            Node node = loader.load ();

            PostViewController controller = loader.getController ();
            controller.post = post;

            controller.dateTimeText.setText (post.getDateTime ());
            controller.karmaCount.setText (String.valueOf (post.getKarma ()));
            controller.usernameText.setText ("r/" + post.getSubreddit ().getTopic () + " u/" + post.getUser ().getUsername ());
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
            throw new RuntimeException (e);
        }
    }

    //endregion

    //region Buttons Functions

    @FXML
    void openSignUpPage (ActionEvent event) throws IOException
    {
        Parent root  = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("/org/project/reddit/SignupView.fxml")));
        Stage  stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
        Scene  scene = new Scene (root);

        stage.setScene (scene);
        stage.setResizable (false);

        System.out.println ("> opening signup page");
    }

    @FXML
    void openLogInPage (ActionEvent event) throws IOException
    {
        Parent root  = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("/org/project/reddit/LoginView.fxml")));
        Stage  stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
        Scene  scene = new Scene (root);

        stage.setScene (scene);
        stage.setResizable (false);

        System.out.println ("> opening log in page");
    }

    @FXML
    void searchAll ()
    {
        if (searchText.getText ().startsWith ("r/"))
        {
            Subreddit subReddit = Subreddit.findSubreddit (searchText.getText ().substring (2));
            if (subReddit == null)
            {
                System.out.println ("> Invalid subreddit");
                return;
            }
            loadSubreddit (subReddit);
        }
        else if (searchText.getText ().startsWith ("u/"))
        {
            User user = User.findUserViaUsername (searchText.getText ().substring (2));
            if (user == null)
            {
                System.out.println ("> Invalid username");
                return;
            }
            loadUser (user);
        }
        else
        {
            System.out.println ("> Invalid input");
        }
    }

    void loadSubreddit (Subreddit subReddit)
    {
        FXMLLoader loader = new FXMLLoader (getClass ().getResource ("/org/project/reddit/SubredditView.fxml"));
        try
        {
            Node node = loader.load ();

            SubredditViewController controller = loader.getController ();
            controller.subReddit = subReddit;

            controller.topicText.setText (subReddit.getTopic ());
            controller.dateText.setText (subReddit.getDateTime ().substring (0, 10));
            controller.memberCount.setText ("Members: " + subReddit.getMemberCount ());

            controller.joinButton.setVisible (false);
            controller.createPostPane.setVisible (false);
            controller.refreshAll ();

            Tab subredditTab = new Tab (subReddit.getTopic ());
            subredditTab.setClosable (true);
            subredditTab.setContent (node);

            tabsPane.getTabs ().add (subredditTab);
            tabsPane.getSelectionModel ().select (subredditTab);
        }
        catch (IOException e)
        {
            throw new RuntimeException ();
        }
    }

    void loadUser (User user)
    {
        FXMLLoader loader = new FXMLLoader (getClass ().getResource ("/org/project/reddit/UserView.fxml"));
        try
        {
            Node node = loader.load ();

            UserViewController controller = loader.getController ();
            controller.user = user;

            controller.usernameText.setText (user.getUsername ());
            controller.karmaCount.setText ("Karma: " + user.getKarma ());

            controller.refreshUser ();

            Tab userTab = new Tab (user.getUsername ());
            userTab.setClosable (true);
            userTab.setContent (node);

            this.tabsPane.getTabs ().add (userTab);
            this.tabsPane.getSelectionModel ().select (userTab);
        }
        catch (IOException e)
        {
            throw new RuntimeException ();
        }
    }

    //endregion
}