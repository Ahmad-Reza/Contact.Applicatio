package com.example.contactapplication.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapplication.R;
import com.example.contactapplication.activity.ContactDetailsActivity;
import com.example.contactapplication.adapter.ContactAdapter;
import com.example.contactapplication.model.ContactModel;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {
    private static final int READ_NUMBER_REQUEST = 1001;

    private ProgressBar contactLoading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);

        contactLoading = rootView.findViewById(R.id.contact_loading);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_CONTACTS}, READ_NUMBER_REQUEST);
        }

        RecyclerView contactRecycler = rootView.findViewById(R.id.contact_recycler);
        contactRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        ContactAdapter contactAdapter = new ContactAdapter(getContactList(), ((position, item) -> {
            Intent intent = new Intent(getContext(), ContactDetailsActivity.class);
            intent.putExtra(ContactDetailsActivity.CONTACT_MODEL, item);
            startActivity(intent);
        }));

        contactRecycler.setAdapter(contactAdapter);

        return rootView;
    }

    @SuppressLint("Range")
    private List<ContactModel> getContactList() {
        List<ContactModel> contactList = new ArrayList<>();

        contactLoading.setVisibility(View.VISIBLE);

        ContentResolver contentResolver = requireContext().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if (cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER) >= 0
                        || cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER) == -1) {
                    Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                    while (cursorInfo.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String number = cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactList.add(new ContactModel(Integer.parseInt(id), name, number));
                    }
                    cursorInfo.close();
                }
            }
        }

        cursor.close();
        contactLoading.setVisibility(View.GONE);

        return contactList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_NUMBER_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Read contact Permission Granted", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(requireContext(), "Read contact dennied.", Toast.LENGTH_LONG).show();
            }
        }
    }
}