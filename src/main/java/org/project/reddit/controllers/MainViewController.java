package org.project.reddit.controllers;

import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import org.project.reddit.classes.Post;
import org.project.reddit.classes.Subreddit;
import org.project.reddit.classes.User;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.Objects;
import java.io.IOException;
import java.util.ResourceBundle;

public class MainViewController implements Initializable
{
    //region Objects

    public static User user;

    @FXML
    private ListView <String> savedPostList;

    @FXML
    private ListView <String> subredditsList;

    @FXML
    private TextField searchText;

    @FXML
    private TextField topicText;

    @FXML
    private TextField descriptionText;

    @FXML
    private TabPane tabsPane;

    @FXML
    private VBox postBox;

    @FXML
    private Label inavlidLabel;

    //endregion

    //region Layout Functions

    @Override
    public void initialize (URL arg0, ResourceBundle arg1)
    {
        refreshAll ();
        System.out.println ("> initialized user panel");
    }

    @FXML
    void refreshAll ()
    {
        int size = user.getSavedPostList ().size ();

        String[] savedPosts = new String[size];
        for (int i = 0; i < size; i++)
        {
            savedPosts[i] = user.getSavedPostList ().get (i).getTitle ();
        }
        this.savedPostList.getItems ().clear ();
        this.savedPostList.getItems ().addAll (savedPosts);

        size = Subreddit.getSubredditList ().size ();

        String[] subreddits = new String[size];
        for (int i = 0; i < size; i++)
        {
            subreddits[i] = Subreddit.getSubredditList ().get (i).getTopic ();
        }
        this.subredditsList.getItems ().clear ();
        this.subredditsList.getItems ().addAll (subreddits);

        refreshTimeline ();
        inavlidLabel.setVisible (false);
        System.out.println ("> refreshed user panel");
    }

    void refreshTimeline ()
    {
        if (! postBox.getChildren ().isEmpty ())
        {
            postBox.getChildren ().remove (1, postBox.getChildren ().size ());
        }
        int size = user.getTimelinePostList ().size ();

        Post[] timelinePosts = new Post[size];
        if (size >= 10)
        {
            for (int i = size - 1; i >= size - 10; i--)
            {
                timelinePosts[i] = user.getTimelinePostList ().get (i);
            }
        }
        else
        {
            for (int i = size - 1; i >= 0; i--)
            {
                timelinePosts[i] = user.getTimelinePostList ().get (i);
            }
        }
        for (Post post : timelinePosts)
        {
            this.postBox.getChildren ().add (getPostLayout (post));
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

    void loadSubreddit (Subreddit subReddit)
    {
        FXMLLoader loader = new FXMLLoader (getClass ().getResource ("/org/project/reddit/SubredditView.fxml"));
        try
        {
            Node node = loader.load ();

            SubredditViewController controller = loader.getController ();
            controller.subReddit = subReddit;

            controller.topicText.setText (subReddit.getTopic ());
            controller.memberCount.setText ("Members: " + subReddit.getMemberCount ());
            controller.dateText.setText (subReddit.getDateTime ().substring (0, 10));

            if (controller.subReddit.getMemberList ().contains (user))
            {
                controller.joinButton.setVisible (false);
            }
            else
            {
                controller.leaveButton.setVisible (false);
            }
            controller.refreshAll ();

            Tab subredditTab = new Tab (subReddit.getTopic ());
            subredditTab.setClosable (true);
            subredditTab.setContent (node);

            this.tabsPane.getTabs ().add (subredditTab);
            this.tabsPane.getSelectionModel ().select (subredditTab);
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
            controller.karmaCount.setText (String.valueOf (user.getKarma ()));
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

    //region Buttons Functions

    @FXML
    void logOut (ActionEvent event) throws IOException
    {
        Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
        alert.setTitle ("Log Out");
        alert.setHeaderText ("Log out and return to main menu");
        alert.setContentText ("Do you confirm?");
        if (alert.showAndWait ().get () == ButtonType.OK)
        {
            user                       = null;
            ProfileViewController.user = null;
            Parent root  = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("/org/project/reddit/GuestView.fxml")));
            Stage  stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
            Scene  scene = new Scene (root);

            stage.setScene (scene);
            stage.setResizable (false);
            System.out.println ("> opening main panel");
        }
    }

    @FXML
    void searchAll (ActionEvent event)
    {
        if (this.searchText.getText ().startsWith ("r/"))
        {
            Subreddit subreddit = Subreddit.findSubreddit (this.searchText.getText ().substring (2));
            if (subreddit == null)
            {
                System.out.println ("> Invalid subreddit");
                return;
            }
            loadSubreddit (subreddit);
        }
        else if (this.searchText.getText ().startsWith ("u/"))
        {
            User user = User.findUserViaUsername (this.searchText.getText ().substring (2));
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

    @FXML
    void viewProfile (ActionEvent event) throws IOException
    {
        Parent root  = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("/org/project/reddit/ProfileView.fxml")));
        Stage  stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
        Scene  scene = new Scene (root);

        stage.setScene (scene);
        stage.setResizable (false);
        System.out.println ("> opening profile panel");
    }

    @FXML
    void createSubreddit ()
    {
        for (Subreddit subReddit : Subreddit.getSubredditList ())
        {
            if (subReddit.getTopic ().equals (topicText.getText ()))
            {
                System.out.println ("> subreddit already exists");
                topicText.setVisible (true);
                return;
            }

            user.createSubReddit (topicText.getText (), descriptionText.getText ());
            topicText.clear ();
            refreshAll ();
        }
    }

    //endregion
}
