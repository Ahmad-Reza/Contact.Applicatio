package com.example.contactapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.contactapplication.R;
import com.example.contactapplication.model.ContactModel;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private final List<ContactModel> contactModels;
    private final ItemClickListener listener;

    public ContactAdapter(List<ContactModel> contactModels, ItemClickListener listener) {
        this.contactModels = contactModels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactModel modal = contactModels.get(position);
        holder.setItem(modal);

        holder.contactName.setText(modal.getName());

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();

        TextDrawable roundDrawable = TextDrawable.builder().beginConfig()
                .width(100) // width in px
                .height(100) // height in px
                .endConfig()
                .buildRound(modal.getName().substring(0, 1), color);
        holder.contactImage.setImageDrawable(roundDrawable);
    }

    @Override
    public int getItemCount() {
        return contactModels.size();
    }

    public interface ItemClickListener {
        void onItemClick(int position, ContactModel item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ContactModel contact;

        private final ImageView contactImage;
        private final TextView contactName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            contactImage = itemView.findViewById(R.id.image_container_view);
            contactName = itemView.findViewById(R.id.name_container_view);

            itemView.setOnClickListener(view -> {
                listener.onItemClick(getLayoutPosition(), contact);
            });
        }

        public void setItem(ContactModel contact) {
            this.contact = contact;
        }
    }
}
