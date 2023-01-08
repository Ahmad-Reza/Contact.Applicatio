package com.example.contactapplication.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.contactapplication.R;
import com.example.contactapplication.adapter.SentMessageAdapter;
import com.example.contactapplication.database.SendMessageDB;
import com.example.contactapplication.model.SendMessageModel;

import java.util.List;

public class MessageFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message, container, false);

        ProgressBar sentMessageLoading = rootView.findViewById(R.id.sent_message_loading);
        sentMessageLoading.setVisibility(View.VISIBLE);
        SendMessageDB sendMessageDB = new SendMessageDB(getContext());
        List<SendMessageModel> sendMessageModels = sendMessageDB.fetchSchedulePlant();
        sentMessageLoading.setVisibility(View.GONE);

        RecyclerView messageRecycler = rootView.findViewById(R.id.sent_message_recycler);
        messageRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        messageRecycler.setAdapter(new SentMessageAdapter(sendMessageModels));

        return rootView;
    }
}