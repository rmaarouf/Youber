package com.youber.cmput301f16t15.youber;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by aphilips on 11/7/16.
 */

public class ElasticSearchUser extends ElasticSearch{


    public static class add extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... users) {
            verifySettings();


            for (User user : users) {
                Index index = new Index.Builder(user).index("youber").type("user").build();

                try {
                    DocumentResult result = getClient().execute(index);
//                    if(result.isSucceeded()) {
//                        //user.setId(result.getId());
//                    }
                } catch (Exception e) {
                    Log.i("Error", "The app failed to build and sent the tweets"+e.toString());
                }
            }

            return null;
        }
    }

    public static class getObjects extends AsyncTask<String, Void, ArrayList<User>> {

        @Override
        protected ArrayList<User> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<User> users = new ArrayList<User>();

            Search search = new Search.Builder(search_parameters[0])
                    .addIndex("youber")
                    .addType("user")
                    .build();

            try {
                SearchResult result = getClient().execute(search);
                if(result.isSucceeded()) {
                    List<User> foundUsers = result.getSourceAsObjectList(User.class);
                    users.addAll(foundUsers);
                }
                else {
                    Log.i("Error", "The search execited but it didnt work");
                }
            }
            catch(Exception e) {
                Log.i("Error", "Executing the get tweets method failed");
            }

            return users;
        }
    }


}