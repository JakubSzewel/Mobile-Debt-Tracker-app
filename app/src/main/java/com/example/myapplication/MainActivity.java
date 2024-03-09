package com.example.myapplication;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FriendAdapter.OnItemClickListener, FriendAdapter.OnHistoryButtonClickListener {

    private List<Friend> friendList = new ArrayList<>();
    private FriendAdapter friendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        friendList = FriendsManager.loadFriends(MainActivity.this);

        RecyclerView recyclerViewFriends = findViewById(R.id.recyclerViewFriends);
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(this));
        friendAdapter = new FriendAdapter(MainActivity.this, friendList, this, this);
        recyclerViewFriends.setAdapter(friendAdapter);

        FloatingActionButton fabAddFriend = findViewById(R.id.fabAddFriend);
        fabAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pass the friendList to AddFriendActivity
                Intent intent = new Intent(MainActivity.this, AddFriendActivity.class);

                // Check for null before serializing
                if (friendList != null) {
                    intent.putExtra("friendList", (Serializable) friendList);
                } else {
                    intent.putExtra("friendList", new ArrayList<Friend>());
                }

                startActivityForResult(intent, 1);
            }
        });

        Button btnAddAmountOwed = findViewById(R.id.btnAddAmountOwed);
        btnAddAmountOwed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddAmountOwedActivity.class);
                intent.putExtra("friendList", (Serializable) friendList);
                startActivityForResult(intent, 2);
            }
        });

        friendAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(Friend friend) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Phone Number", friend.getPhoneNumber());
        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(this, "Numer telefonu skopiowany: " + friend.getPhoneNumber(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHistoryButtonClick(Friend friend) {
        Intent intent = new Intent(MainActivity.this, DebtHistoryActivity.class);
        intent.putExtra("debtHistory", (Serializable) friend.getDebtHistory());
        startActivity(intent);
    }

    // MainActivity.java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Friend newFriend = (Friend) data.getSerializableExtra("newFriend");

            if (newFriend != null) {
                if (friendList == null) {
                    friendList = new ArrayList<>();
                }
                friendList.add(newFriend);

                friendAdapter.notifyDataSetChanged();
                FriendsManager.saveFriends(MainActivity.this, friendList);
            }
        }
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            List<Friend> updatedFriendList = (List<Friend>) data.getSerializableExtra("friendList");

            friendList.clear();
            friendList.addAll(updatedFriendList);
            friendAdapter.notifyDataSetChanged();
            FriendsManager.saveFriends(MainActivity.this, friendList);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        new LoadFriendsTask().execute();
    }

    private class LoadFriendsTask extends AsyncTask<Void, Void, List<Friend>> {
        @Override
        protected List<Friend> doInBackground(Void... voids) {
            return FriendsManager.loadFriends(MainActivity.this);
        }

        @Override
        protected void onPostExecute(List<Friend> result) {
            super.onPostExecute(result);

            friendList.clear();
            friendList.addAll(result);
            friendAdapter.notifyDataSetChanged();
        }
    }

}
