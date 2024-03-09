package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.Serializable;
import java.util.List;

public class AddAmountOwedActivity extends AppCompatActivity {

    private Spinner spinnerFriends;
    private EditText editTextAmountOwed;
    private EditText editTextTitle;

    private List<Friend> friendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_amount_owed);

        spinnerFriends = findViewById(R.id.spinnerFriends);
        editTextAmountOwed = findViewById(R.id.editTextAmountOwed);
        editTextTitle = findViewById(R.id.editTextTitle);

        friendList = (List<Friend>) getIntent().getSerializableExtra("friendList");

        ArrayAdapter<Friend> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, friendList);
        spinnerFriends.setAdapter(adapter);

        Button buttonSaveAmountOwed = findViewById(R.id.buttonSaveAmountOwed);
        buttonSaveAmountOwed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedIndex = spinnerFriends.getSelectedItemPosition();

                if (selectedIndex != AdapterView.INVALID_POSITION && selectedIndex < friendList.size()) {
                    Friend selectedFriend = friendList.get(selectedIndex);
                    String amountOwedText = editTextAmountOwed.getText().toString().trim();

                    if (TextUtils.isEmpty(amountOwedText)) {
                        Toast.makeText(AddAmountOwedActivity.this, "Proszę wpisać wartość", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double amountOwed = Double.parseDouble(amountOwedText);

                    selectedFriend.updateAmountOwed(amountOwed);

                    String title = editTextTitle.getText().toString();

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("selectedFriend", selectedFriend);
                    resultIntent.putExtra("friendList", (Serializable) friendList);
                    resultIntent.putExtra("title", title);
                    setResult(RESULT_OK, resultIntent);

                    Toast.makeText(AddAmountOwedActivity.this, "Dodana wartość zadłużenia: " + amountOwed, Toast.LENGTH_SHORT).show();

                    selectedFriend.addHistory(title, amountOwed);
                    finish();
                } else {
                    Toast.makeText(AddAmountOwedActivity.this, "Proszę wybrać znajomego", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
