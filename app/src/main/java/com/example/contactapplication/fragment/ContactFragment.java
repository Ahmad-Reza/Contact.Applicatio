package com.example.contactapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapplication.R;
import com.example.contactapplication.adapter.ContactAdapter;
import com.example.contactapplication.model.ContactModel;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {
    private ProgressBar contactLoading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);

        contactLoading = rootView.findViewById(R.id.contact_loading);

        RecyclerView contactRecycler = rootView.findViewById(R.id.contact_recycler);
        contactRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        ContactAdapter contactAdapter = new ContactAdapter(getContactList(), ((position, item) -> {
            ContactDetailsFragment contactDetailsFragment = ContactDetailsFragment.newInstance(item);
            contactDetailsFragment.show(getChildFragmentManager(), "ContactDetailsFragment");
        }));
        contactRecycler.setAdapter(contactAdapter);

        return rootView;
    }

    private List<ContactModel> getContactList() {
        List<ContactModel> contactList = new ArrayList<>();

        contactLoading.setVisibility(View.VISIBLE);

        contactList.add(new ContactModel(0, "Ahmad Reza", "7903027781")); // Valid number
        contactList.add(new ContactModel(0, "Kisan Network", "9810153260")); // Valid number
        contactList.add(new ContactModel(2, "Akshay Reza", "7594735278"));
        contactList.add(new ContactModel(1, "Kundan kumar", "7594635278"));
        contactList.add(new ContactModel(3, "Nishant Goswami", "7594735278"));
        contactList.add(new ContactModel(4, "Praphul kumar", "7594735278"));
        contactList.add(new ContactModel(5, "Ahmad Reza", "7594735278"));
        contactList.add(new ContactModel(6, "Nishant Goswami", "7594735278"));
        contactList.add(new ContactModel(8, "Akshay Reza", "7594735278"));
        contactList.add(new ContactModel(9, "Kundan kumar", "7594635278"));
        contactList.add(new ContactModel(10, "Praphul kumar", "7594735278"));

        contactLoading.setVisibility(View.GONE);

        return contactList;
    }
}