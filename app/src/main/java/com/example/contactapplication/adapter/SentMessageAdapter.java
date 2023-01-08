package com.example.contactapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapplication.R;
import com.example.contactapplication.model.SendMessageModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SentMessageAdapter extends RecyclerView.Adapter<SentMessageAdapter.ViewHolder> {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d MMM 'at' hh:mm a");

    private final List<SendMessageModel> sendMessageModels;

    public SentMessageAdapter(List<SendMessageModel> sendMessageModels) {
        this.sendMessageModels = sendMessageModels;
    }

    @NonNull
    @Override
    public SentMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sent_message, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SentMessageAdapter.ViewHolder holder, int position) {
        SendMessageModel sendMessageModel = sendMessageModels.get(position);

        holder.contactName.setText("Contact Name - " + sendMessageModel.getContactName());
        holder.sentOtp.setText("Sent OTP - " + sendMessageModel.getSentOTP());
        holder.sendMessageTime.setText(formatDateTime(sendMessageModel.getLocalDateTime()));
    }

    @Override
    public int getItemCount() {
        return sendMessageModels.size();
    }

    @Nullable
    public static String formatDateTime(LocalDateTime sentMessageTime) {
        if (sentMessageTime != null) {
            StringBuilder selectedTime = new StringBuilder(sentMessageTime.format(DATE_TIME_FORMATTER));

            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);
            LocalDate yesterday = today.minusDays(2);
            LocalDate sentDate = sentMessageTime.toLocalDate();

            if (yesterday.equals(sentDate)) {
                selectedTime = selectedTime.replace(0, selectedTime.indexOf(" at"), "Yesterday");

            } else if (today.equals(sentDate)) {
                selectedTime = selectedTime.replace(0, selectedTime.indexOf(" at"), "Today");

            } else if (tomorrow.equals(sentDate)) {
                selectedTime = selectedTime.replace(0, selectedTime.indexOf(" at"), "Tomorrow");
            }
            return String.valueOf(selectedTime);

        } else {
            return null;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView contactName;
        private final TextView sentOtp;
        private final TextView sendMessageTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            contactName = itemView.findViewById(R.id.contact_name);
            sentOtp = itemView.findViewById(R.id.sent_otp);
            sendMessageTime = itemView.findViewById(R.id.date_time_view);
        }
    }
}
