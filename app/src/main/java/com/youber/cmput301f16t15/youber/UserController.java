package com.youber.cmput301f16t15.youber;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by Jess on 2016-11-08.
 */

public class UserController {

    private final static String FILENAME = "user.sav";
    private static Context c;

    private static User user = null;

    public static User getUser() {
        if(user == null) {
            user = loadUser();
        }

        return user;
    }

    public static void setContext(Context context) {
        c = context;
    }

    public static User loadUser() {
        try
        {
            FileInputStream fis = c.openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            Type listType = new TypeToken<User>(){}.getType();
            return gson.fromJson(in, listType);
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException();
        }
        catch (IOException e)
        {
            throw new RuntimeException();
        }
    }

    public static void saveUser(User u) {
        user = u;

        try
        {
            FileOutputStream fos = c.openFileOutput(FILENAME, 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);

            Gson gson = new Gson();
            gson.toJson(user, writer);

            writer.flush();
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException();
        }
        catch (IOException e)
        {
            throw new RuntimeException();
        }
    }

//    public static void notifyObservers() {
//
//    }
//
//    public static void addObserver()
    public static void addObserver(Observer obs) {
        user.addObserver(obs);
    }

    public static void setFirstName(String firstName) {
        user.setFirstName(firstName);
        saveUser(user);
    }

    public static void setDateOfBirth(String dateOfBirth) {
        user.setDateOfBirth(dateOfBirth);
        saveUser(user);
    }

    public static void setPhoneNumber(String phoneNumber) {
        user.setPhoneNumber(phoneNumber);
        saveUser(user);
    }

    public static void setLastName(String lastName) {
        user.setLastName(lastName);
        saveUser(user);
    }

    public static void setEmail(String email) {
        user.setEmail(email);
        saveUser(user);
    }
}