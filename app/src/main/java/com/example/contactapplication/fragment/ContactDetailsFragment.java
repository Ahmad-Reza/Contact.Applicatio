package com.example.contactapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.example.contactapplication.R;
import com.example.contactapplication.model.ContactModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactDetailsFragment extends BottomSheetDialogFragment {
    private static final String CONTACT_MODEL = "CONTACT_MODEL";

    private ContactModel contactModel;
    private SendMessageListener listener;

    public ContactDetailsFragment() { }

    public static ContactDetailsFragment newInstance(ContactModel contactModel) {
        ContactDetailsFragment fragment = new ContactDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(CONTACT_MODEL, contactModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            contactModel = getArguments().getParcelable(CONTACT_MODEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_details, container, false);

        MaterialToolbar toolbar = rootView.findViewById(R.id.material_toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.close_button) {
                dismiss();
                return true;
            }
            return false;
        });

        if (getContext() != null) {
            toolbar.getMenu().findItem(R.id.close_button).setIconTintList(AppCompatResources.getColorStateList(getContext(), R.color.white));
        }

        TextView contactName = rootView.findViewById(R.id.contact_name);
        contactName.setText(contactModel.getName());

        TextView contactNumber = rootView.findViewById(R.id.contact_number);
        contactNumber.setText(contactModel.getNumber());

        ImageView sendMessage = rootView.findViewById(R.id.send_message);
        sendMessage.setOnClickListener(view -> {
            listener.onMessageSend(contactModel);
            dismiss();
        });

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (SendMessageListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface SendMessageListener {
        void onMessageSend(ContactModel contactModel);
    }
}