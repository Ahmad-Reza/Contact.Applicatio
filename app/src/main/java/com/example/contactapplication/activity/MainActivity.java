package com.example.contactapplication.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.contactapplication.R;
import com.example.contactapplication.adapter.ViewPagerAdapter;
import com.example.contactapplication.database.SendMessageDB;
import com.example.contactapplication.fragment.ContactDetailsFragment;
import com.example.contactapplication.fragment.ContactFragment;
import com.example.contactapplication.fragment.MessageFragment;
import com.example.contactapplication.model.ContactModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ContactDetailsFragment.SendMessageListener {
    private static final int SEND_SMS_PERMISSIONS_REQUEST = 0;

    private ContactModel contactModel;
    private String sendMessage;

    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.material_toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        AppBarLayout appBarLayout = findViewById(R.id.appbar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }

                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getString(R.string.contact_app_label));

                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

        TextView contactTab = findViewById(R.id.contact_tab_view);
        TextView messageTab = findViewById(R.id.message_tab_view);

        viewPager = findViewById(R.id.viewpager_view);
        viewPager.setAdapter(new ViewPagerAdapter(this, getFragments()));

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    contactTab.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.tab_select));
                    contactTab.setTextColor(AppCompatResources.getColorStateList(getApplicationContext(), R.color.white));

                    messageTab.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.tab_deselected));
                    messageTab.setTextColor(AppCompatResources.getColorStateList(getApplicationContext(), R.color.textColor));

                } else if (position == 1) {
                    messageTab.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.tab_select));
                    messageTab.setTextColor(AppCompatResources.getColorStateList(getApplicationContext(), R.color.white));

                    contactTab.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.tab_deselected));
                    contactTab.setTextColor(AppCompatResources.getColorStateList(getApplicationContext(), R.color.textColor));
                }
            }
        });

        View.OnClickListener listener = view -> {
            if (view.getId() == R.id.contact_tab_view) {
                contactTab.setBackground(AppCompatResources.getDrawable(this, R.drawable.tab_select));
                contactTab.setTextColor(AppCompatResources.getColorStateList(this, R.color.white));

                messageTab.setBackground(AppCompatResources.getDrawable(this, R.drawable.round_corners));
                messageTab.setTextColor(AppCompatResources.getColorStateList(this, R.color.textColor));

                viewPager.setCurrentItem(0, true);

            } else if (view.getId() == R.id.message_tab_view) {
                messageTab.setBackground(AppCompatResources.getDrawable(this, R.drawable.tab_select));
                messageTab.setTextColor(AppCompatResources.getColorStateList(this, R.color.white));

                contactTab.setBackground(AppCompatResources.getDrawable(this, R.drawable.round_corners));
                contactTab.setTextColor(AppCompatResources.getColorStateList(this, R.color.textColor));
                viewPager.setCurrentItem(1, true);
            }
        };

        contactTab.setOnClickListener(listener);
        messageTab.setOnClickListener(listener);

        FloatingActionButton dialPadBtn = findViewById(R.id.dialpad_view);
        dialPadBtn.setOnClickListener(v -> {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            startActivity(dialIntent);
        });
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ContactFragment());
        fragments.add(new MessageFragment());

        return fragments;
    }

    private void sendSMS() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(contactModel.getNumber(), null, sendMessage, null, null);

        // store send message to database
        String otp = sendMessage.substring(sendMessage.length() - 6);
        SendMessageDB sendMessageDB = new SendMessageDB(this);
        sendMessageDB.updateSentMessage(contactModel.getName(), otp, LocalDateTime.now().toString());

        viewPager.setAdapter(new ViewPagerAdapter(this, getFragments()));
    }

    @Override
    public void onMessageSend(ContactModel contactModel) {
        this.contactModel = contactModel;

        Random rnd = new Random();
        int sixDigit = rnd.nextInt(999999);
        String OTP = String.format("%06d", sixDigit);
        sendMessage = ("Hi. Your OTP is: " + OTP).trim();

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.messages_label)
                .setMessage(sendMessage)
                .setCancelable(false);

        builder.setPositiveButton("Send", ((dialogInterface, i) -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSIONS_REQUEST);
            } else {
                sendSMS();
            }
        }));

        builder.setNegativeButton("Cancel", ((dialogInterface, i) -> {
            dialogInterface.cancel();
        }));

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SEND_SMS_PERMISSIONS_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMS();
                Toast.makeText(getApplicationContext(), "SMS Permission Granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "SMS Permission dennied.", Toast.LENGTH_LONG).show();
            }
        }
    }
}