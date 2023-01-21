package com.example.contactapplication.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.contactapplication.R;
import com.example.contactapplication.database.SendMessageDB;
import com.example.contactapplication.model.ContactModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDateTime;

public class ContactDetailsActivity extends AppCompatActivity {
    private static final int SEND_SMS_PERMISSIONS_REQUEST = 0;
    public static final String CONTACT_MODEL = "CONTACT_MODEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        ContactModel contactModel = getIntent().getParcelableExtra(CONTACT_MODEL);

        MaterialToolbar toolbar = findViewById(R.id.material_toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        TextView contactName = findViewById(R.id.contact_name);
        contactName.setText(contactModel.getName());

        TextView contactNumber = findViewById(R.id.contact_number);
        contactNumber.setText(contactModel.getNumber());

        LinearLayout audioCallView = findViewById(R.id.audio_call_view);
        audioCallView.setOnClickListener(view -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + contactModel.getNumber()));
            startActivity(callIntent);
        });

        LinearLayout sendMessageView = findViewById(R.id.send_message_view);
        sendMessageView.setOnClickListener(view -> {
            View dialogView = getLayoutInflater().inflate(R.layout.item_message, null);

            TextInputLayout messageLayout = dialogView.findViewById(R.id.message_layout);
            TextInputEditText messageInput = dialogView.findViewById(R.id.message_input);

            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(R.string.message_label)
                    .setCancelable(false)
                    .setView(dialogView);


            AlertDialog alert = builder.create();

            Button sendBtn = dialogView.findViewById(R.id.send_button);
            sendBtn.setOnClickListener(view1 -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSIONS_REQUEST);

                } else {
                    String message = messageInput != null ? String.valueOf(messageInput.getText()) : null;
                    if (message != null && !message.isEmpty()) {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(contactModel.getNumber(), null, message, null, null);

                        SendMessageDB sendMessageDB = new SendMessageDB(this);
                        sendMessageDB.updateSentMessage(contactModel.getName(), message, LocalDateTime.now().toString());

                        alert.dismiss();

                    } else messageLayout.setError("field should not be empty");
                }
            });

            Button cancelBtn = dialogView.findViewById(R.id.cancel_button);
            cancelBtn.setOnClickListener(view1 -> alert.dismiss());

            alert.show();
        });

        LinearLayout videoCallView = findViewById(R.id.video_call_view);
        videoCallView.setOnClickListener(view -> Toast.makeText(this, "Feature coming soon", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SEND_SMS_PERMISSIONS_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "SMS Permission Granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "SMS Permission dennied.", Toast.LENGTH_LONG).show();
            }
        }
    }
}