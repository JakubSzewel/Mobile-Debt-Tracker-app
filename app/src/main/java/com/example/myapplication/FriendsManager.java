package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FriendsManager {
    private static final String PREFS_NAME = "MyPrefs";
    private static final String FRIEND_LIST_KEY = "friendList";

    public static void saveFriends(Context context, List<Friend> friendList) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(friendList);
        editor.putString(FRIEND_LIST_KEY, json);
        editor.apply();
    }

    public static List<Friend> loadFriends(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String storedJson = prefs.getString(FRIEND_LIST_KEY, null);

        if (storedJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Friend>>() {}.getType();
            List<Friend> friends = gson.fromJson(storedJson, type);

            if (friends != null) {
                Collections.sort(friends, new Comparator<Friend>() {
                    @Override
                    public int compare(Friend friend1, Friend friend2) {
                        return friend1.getName().compareToIgnoreCase(friend2.getName());
                    }
                });
            }

            return friends;
        } else {
            return null;
        }
    }

}

