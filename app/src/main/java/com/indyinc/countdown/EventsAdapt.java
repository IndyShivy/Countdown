package com.indyinc.countdown;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventsAdapt extends RecyclerView.Adapter<EventsAdapt.ViewHolder> {
    private final ArrayList<DateItem> dateItems;
    private DateDatabase db;
    private final Context context;


    public EventsAdapt(ArrayList<DateItem> dateItems, Context context) {
        this.dateItems = dateItems;
        this.context = context;
        this.db = new DateDatabase(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.act_events_adapter_item, parent, false);
        db = new DateDatabase(view.getContext());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DateItem dateItem = dateItems.get(position);
        holder.eventTitleTextView.setText(dateItem.getTitle());
        holder.dateTextView.setText(dateItem.getDate());

        holder.removeButton.setOnClickListener(v -> {
            String eventTitle = dateItem.getTitle();
            AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogCustom);
            LayoutInflater backupInflate = LayoutInflater.from(context);
            View dialogView = backupInflate.inflate(R.layout.act_events_alert_dialog, null);
            builder.setView(dialogView);
            TextView title = dialogView.findViewById(R.id.dialog_title);
            String titleSet = "Delete Event";
            title.setText(titleSet);
            title.setTextColor(Color.WHITE);
            TextView message = dialogView.findViewById(R.id.dialog_message);
            String textSetter = "Remove " + "\"" + eventTitle + "\"" +" ?";
            message.setText(textSetter);
            message.setTextColor(Color.WHITE);  // Set the color
            builder.setPositiveButton("Yes", (dialog, which) -> {
                        db.deleteEvent(dateItem);
                        dateItems.remove(dateItem);
                        notifyItemRemoved(position);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return db.getAllDates().size();
    }
    // Create the view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitleTextView;
        TextView dateTextView;
        Button removeButton;

        // Constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitleTextView = itemView.findViewById(R.id.eventTitle);
            dateTextView = itemView.findViewById(R.id.eventDate);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }


}
