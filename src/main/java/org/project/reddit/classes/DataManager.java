package org.project.reddit.classes;

import java.io.*;
import java.util.ArrayList;

public class DataManager
{
    public static void saveData ()
    {
        try (ObjectOutputStream outputStream = new ObjectOutputStream (new FileOutputStream ("Data")))
        {
            outputStream.writeObject (User.getUserList ()); //save all users list in the file
        }
        catch (IOException e) //throw exception
        {
            e.printStackTrace ();
        }
    }

    public static void loadData ()
    {
        try (ObjectInputStream inputStream = new ObjectInputStream (new FileInputStream ("Data")))
        {
            //load all users list from the file
            ArrayList <User> loadedUsers = (ArrayList <User>) inputStream.readObject ();
            User.setUserList (loadedUsers);
        }
        catch (IOException | ClassNotFoundException e) //throw exception
        {
            e.printStackTrace ();
        }
    }
}
