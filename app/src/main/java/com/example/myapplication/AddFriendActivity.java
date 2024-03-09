package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AddFriendActivity extends AppCompatActivity {

    private EditText editTextFriendName;
    private EditText editTextPhoneNumber;

    private static final int CONTACT_PICKER_RESULT = 2;

    private static final int CONTACTS_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        editTextFriendName = findViewById(R.id.editTextFriendName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);

        Button buttonSaveFriend = findViewById(R.id.buttonSaveFriend);
        Button buttonPickContact = findViewById(R.id.buttonPickContact);

        buttonSaveFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String friendName = editTextFriendName.getText().toString();
                String phoneNumber = editTextPhoneNumber.getText().toString();

                if (friendName.isEmpty() || phoneNumber.isEmpty()) {
                    Toast.makeText(AddFriendActivity.this, "Proszę w wypełnić wszystkie pola", Toast.LENGTH_SHORT).show();
                    return;
                }

                Friend newFriend = new Friend(friendName, 0.0, phoneNumber);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("newFriend", newFriend);
                setResult(RESULT_OK, resultIntent);

                finish();
            }
        });

        buttonPickContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkContactsPermission()) {
                    launchContactPicker();
                } else {
                    requestContactsPermission();
                }
            }
        });
    }

    private boolean checkContactsPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestContactsPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, CONTACTS_PERMISSION_REQUEST_CODE);
    }

    private void launchContactPicker() {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CONTACTS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchContactPicker();
            } else {
                Toast.makeText(this, "Odmowa dostępu. Nie można wybrać kontaktu.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CONTACT_PICKER_RESULT && resultCode == Activity.RESULT_OK) {
            processContactData(data);
        }
    }

    private void processContactData(Intent data) {
        Cursor cursor = null;
        try {
            Uri result = data.getData();
            cursor = getContentResolver().query(result, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                int phoneNumberIndex = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);

                String contactName = cursor.getString(nameIndex);

                editTextFriendName.setText(contactName);

                if (Integer.parseInt(cursor.getString(phoneNumberIndex)) > 0) {
                    @SuppressLint("Range") String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                    Cursor phoneCursor = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                            null, null);

                    if (phoneCursor != null && phoneCursor.moveToFirst()) {
                        int phoneNumberIndexContact = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        String contactPhoneNumber = phoneCursor.getString(phoneNumberIndexContact);

                        editTextPhoneNumber.setText(contactPhoneNumber);
                    }

                    if (phoneCursor != null) {
                        phoneCursor.close();
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
